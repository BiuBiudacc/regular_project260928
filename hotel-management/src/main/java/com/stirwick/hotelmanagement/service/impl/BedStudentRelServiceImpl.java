package com.stirwick.hotelmanagement.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.stirwick.hotelmanagement.dto.student.*;
import com.stirwick.hotelmanagement.entity.Bed;
import com.stirwick.hotelmanagement.service.BedStudentRelService;
import com.stirwick.hotelmanagement.vo.student.StudentBedRecordVO;
import com.stirwick.hotelmanagement.vo.student.StudentCurrentBedVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.stirwick.hotelmanagement.entity.BedStudentRel;
import com.stirwick.hotelmanagement.exception.BusinessException;
import com.stirwick.hotelmanagement.mapper.BedMapper;
import com.stirwick.hotelmanagement.mapper.BedStudentRelMapper;
import com.stirwick.hotelmanagement.mapper.StudentInfoMapper;
import org.springframework.util.CollectionUtils;

@Service
public class BedStudentRelServiceImpl extends ServiceImpl<BedStudentRelMapper, BedStudentRel> implements BedStudentRelService {
    @Autowired
    private BedStudentRelMapper bedStudentRelMapper;
    @Autowired
    private BedMapper bedMapper;
    @Autowired
    private StudentInfoMapper studentInfoMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchCheckOut(StudentCheckOutDTO dto) {
        // 退房日期：不传取今天
        LocalDate targetDate = dto.getCheckOutDate();
        if(targetDate == null){
            targetDate = LocalDate.now();
        }
        LocalDateTime targetDateTime = targetDate.atStartOfDay();
        List<Long> studentIdList = dto.getStudentIdList();
        // 查询：学生id列表 + 实际退房时间为空（在住记录）
        List<BedStudentRel> relList = this.baseMapper.selectList(
                new LambdaQueryWrapper<BedStudentRel>()
                        .in(BedStudentRel::getStudentId, studentIdList)
                        .isNull(BedStudentRel::getCheckOutTime)
        );
        if(CollectionUtils.isEmpty(relList)){
            throw new BusinessException(6001,"所选学生无正在入住记录，退房失败");
        }
        // 拿到本次需要更新的关联表主键id
        List<Long> relIdList = relList.stream()
                .map(BedStudentRel::getId)
                .toList();
        // 更新：actualLeaveTime、estimateLeaveTime 都改为退房时间
        LambdaUpdateWrapper<BedStudentRel> updateWrapper = new LambdaUpdateWrapper<BedStudentRel>();
        updateWrapper.in(BedStudentRel::getId, relIdList)
                .set(BedStudentRel::getCheckOutTime, targetDateTime)
                .set(BedStudentRel::getEstimateCheckOutTime, targetDateTime);

        boolean update = this.update(updateWrapper);
        if(!update){
            throw new BusinessException(6002,"退房更新失败");
        }
    }


    @Override
    public void deleteRelRecord(Long id) {
        if(bedStudentRelMapper.selectById(id)==null){
            throw new BusinessException(5003,"入住记录不存在");
        }
        bedStudentRelMapper.deleteById(id);
    }

    @Override
    public List<StudentBedRecordVO> getStudentBedRecord(Long studentId) {
        List<StudentBedRecordVO> voList = bedStudentRelMapper.selectStudentBedRecordByStudentId(studentId);
        LocalDateTime now = LocalDateTime.now();
        List<StudentBedRecordVO> voResult =voList.stream().map(vo->{
            if(vo.getRealLeaveTime() != null){
                vo.setStatus("已结束");
            }else{
                if (now.isBefore(vo.getCheckInDate())) {
                    vo.setStatus("待入住");
                } else if (now.isAfter(vo.getCheckInDate()) && now.isBefore(vo.getEstimateLeaveTime())) {
                    vo.setStatus("正在入住");
                } else {
                    // 当前超过预估离开时间，实际离开时间为空，标记为已结束
                    vo.setStatus("已结束");
                }
            }
            return vo;
        }).collect(Collectors.toList());
        return voResult;
    }

    @Override
    public StudentCurrentBedVO getCurrentCheckIn(StudentCurrentBedQueryDTO dto){
        StudentCurrentBedVO vo = bedStudentRelMapper.selectCurrentCheckInByStudentId(dto.getStudentId());
        if(vo == null){
            throw new BusinessException(6001,"该学生无正在入住记录,无法续住");
        }
        return vo;
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchCheckIn(StudentBedBatchCheckInDTO dto) {
        List<StudentBedItemDTO> itemList = dto.getItemList();
        LocalDateTime checkInDate = dto.getCheckInDate();
        LocalDateTime estimateOutDate = dto.getEstimateCheckOutDate();

        if (estimateOutDate.isBefore(checkInDate)) {
            throw new BusinessException(60003, "预估退宿时间不能早于入住时间");
        }
        // 校验：本次提交不能重复同一个学生
        Set<Long> studentSet = new HashSet<>();
        for (StudentBedItemDTO item : itemList) {
            if (!studentSet.add(item.getStudentId())) {
                throw new BusinessException(60007, "学生id:" + item.getStudentId() + " 在本次提交重复分配");
            }
        }

        // 校验：本次提交同一个床位不能分给多个学生
        Set<Long> bedSet = new HashSet<>();
        for (StudentBedItemDTO item : itemList) {
            if (!bedSet.add(item.getBedId())) {
                throw new BusinessException(60008, "床位id:" + item.getBedId() + " 在本次提交重复分配");
            }
        }

        // 提取所有学生id、床位id
        List<Long> studentIdList = itemList.stream()
                .map(StudentBedItemDTO::getStudentId)
                .collect(Collectors.toList());

        List<Long> bedIdList = itemList.stream()
                .map(StudentBedItemDTO::getBedId)
                .collect(Collectors.toList());
        // 校验学生全部存在
        Long studentCount = studentInfoMapper.selectCountByIds(studentIdList);
        if (studentCount != studentIdList.size()) {
            throw new BusinessException(60004, "存在不存在的学生id");
        }

        checkStudentNoActiveRecord(studentIdList);

        // 校验床位全部存在
        Long bedCount = bedMapper.selectCountByIds(bedIdList);
        if (bedCount != bedIdList.size()) {
            throw new BusinessException(60005, "存在不存在的床位id");
        }
        // 校验床位时间段冲突
        List<Long> conflictBedIdList = bedStudentRelMapper.selectConflictBedIds(bedIdList, checkInDate, estimateOutDate);
        if (!conflictBedIdList.isEmpty()) {
            throw new BusinessException(60006, "床位【" + conflictBedIdList + "】在该时间段已有入住记录，无法重复入住");
        }

        // 组装批量插入实体
        List<BedStudentRel> insertList = new ArrayList<>();
        for (StudentBedItemDTO item : itemList) {
            BedStudentRel rel = new BedStudentRel();
            rel.setStudentId(item.getStudentId());
            rel.setBedId(item.getBedId());
            rel.setCheckInTime(checkInDate);
            rel.setEstimateCheckOutTime(estimateOutDate);
            insertList.add(rel);
        }
        //批量插入
        bedStudentRelMapper.insertBatchSomeColumn(insertList);
    }

    private void checkStudentNoActiveRecord(List<Long> studentIdList) {
        if (CollectionUtils.isEmpty(studentIdList)) {
            return;
        }
        LambdaQueryWrapper<BedStudentRel> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(BedStudentRel::getStudentId, studentIdList);
        wrapper.isNull(BedStudentRel::getCheckOutTime);
        // 查出冲突的学生
        List<BedStudentRel> existActiveList = bedStudentRelMapper.selectList(wrapper);
        if (!existActiveList.isEmpty()) {
            // 提取冲突学生id
            List<Long> conflictStudentIds = existActiveList.stream()
                    .map(BedStudentRel::getStudentId)
                    .distinct()
                    .collect(Collectors.toList());
            throw new BusinessException(6001,"学生【"+conflictStudentIds+"】存在未退宿入住记录，不能新增入住");
        }
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void renew(StudentRenewDTO dto){
        // 1. 根据床位关联id查询入住记录
        BedStudentRel rel = this.getById(dto.getBedRelId());
        if (rel == null) {
            throw new BusinessException(5006, "入住记录不存在");
        }
        // 2. 基础业务校验
        if (!rel.getStudentId().equals(dto.getStudentId())) {
            throw new BusinessException(5007, "入住记录与学生不匹配");
        }
        if (rel.getCheckOutTime() != null) {
            throw new BusinessException(5008, "该记录已办理退房，无法续住");
        }
        // 乐观锁兜底：校验原预计离开日期和前端传入一致，防止并发修改
        if (!rel.getEstimateCheckOutTime().isEqual(dto.getOldEstimateLeaveDate())) {
            throw new BusinessException(5009, "原预计离开时间已发生变更，请刷新页面重试");
        }
        LambdaQueryWrapper<BedStudentRel> overlapWrapper = new LambdaQueryWrapper<>();
        LambdaUpdateWrapper<BedStudentRel> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(BedStudentRel::getEstimateCheckOutTime, dto.getNewEstimateLeaveDate())
                .eq(BedStudentRel::getId, dto.getBedRelId())
                .eq(BedStudentRel::getStudentId, dto.getStudentId())
                .isNull(BedStudentRel::getCheckOutTime);
        // 改用service自带update
        boolean success = this.update(updateWrapper);
        int row = success ? 1 : 0;
        if (row <= 0) {
            throw new BusinessException(5011, "续住更新失败，请刷新重试");
        }
    }

}
