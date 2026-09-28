package com.stirwick.hotelmanagement.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.stirwick.hotelmanagement.dto.clas.*;
import com.stirwick.hotelmanagement.entity.ClassInfo;
import com.stirwick.hotelmanagement.entity.ClassRoom;
import com.stirwick.hotelmanagement.entity.StudentInfo;
import com.stirwick.hotelmanagement.exception.BusinessException;
import com.stirwick.hotelmanagement.mapper.ClassInfoMapper;
import com.stirwick.hotelmanagement.mapper.ClassRoomMapper;
import com.stirwick.hotelmanagement.mapper.StudentInfoMapper;
import com.stirwick.hotelmanagement.service.ClassInfoService;
import com.stirwick.hotelmanagement.vo.clas.ClassSimpleVO;
import com.stirwick.hotelmanagement.vo.clas.StudentAdjustVO;
import com.stirwick.hotelmanagement.vo.clas.StudentClassVO;
import com.stirwick.hotelmanagement.vo.page.PageVO;
import com.stirwick.hotelmanagement.vo.clas.ClassPageVO;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;
@Service
public class ClassInfoServiceImpl extends ServiceImpl<ClassInfoMapper, ClassInfo> implements ClassInfoService {
    @Resource
    private ClassRoomMapper classRoomMapper;
    @Resource
    private StudentInfoMapper studentInfoMapper;
    //班级创建方法
    public void createClass(ClassCreateDTO dto){
        Long classRoomId = dto.getClassRoomId();
        if(classRoomId != null){
            ClassRoom classRoom = classRoomMapper.selectById(classRoomId);
            if(classRoom == null){
                throw new BusinessException(5001,"选择的教室不存在");
            }
        }
        // 2. DTO转实体
        ClassInfo classInfo = new ClassInfo();
        BeanUtils.copyProperties(dto, classInfo);
        // 3. 保存
        this.save(classInfo);
    }

    //班级信息修改方法
    @Override
    public void updateClass(ClassUpdateDTO dto) {
        ClassInfo existClass = this.getById(dto.getId());
        if(existClass == null){
            throw new BusinessException(5003,"该班级信息不存在");
        }
        //2.校验教室（不为null才校验）
        Long classRoomId = dto.getClassRoomId();
        if(classRoomId != null){
            ClassRoom classRoom = classRoomMapper.selectById(classRoomId);
            if(classRoom == null){
                throw new BusinessException(5001,"选择的教室不存在");
            }
            if(classRoom.getRoomStatus() != 1){
                throw new BusinessException(5002,"该教室已经停用，无法选择");
            }
        }
        //3.dto转实体，updateById只会更新对象中set过的字段
        ClassInfo classInfo = new ClassInfo();
        BeanUtils.copyProperties(dto, classInfo);
        System.out.println("打印--------"+classInfo);
        System.out.println("打印222222--------"+dto);
        //4.执行更新
        this.updateById(classInfo);
    }

    public PageVO<ClassPageVO> pageQuery(ClassPageQuery classPageQuery){
        Long pageNum = Objects.nonNull(classPageQuery.getPageNum()) ? classPageQuery.getPageNum() : 1L;
        Long pageSize = Objects.nonNull(classPageQuery.getPageSize()) ? classPageQuery.getPageSize() : 10L;
        ClassQueryDTO query = classPageQuery.getQuery();
        if(Objects.isNull(query)){
            query = new ClassQueryDTO();
        }
        LambdaQueryWrapper<ClassInfo> wrapper = new LambdaQueryWrapper<>();
        if(StringUtils.hasText(query.getClassName())){
            wrapper.like(ClassInfo::getClassName, query.getClassName());
        }
        if(Objects.nonNull(query.getClassStatus())){
            wrapper.eq(ClassInfo::getClassStatus, query.getClassStatus());
        }
        if(Objects.nonNull(query.getClassRoomId())){
            wrapper.eq(ClassInfo::getClassRoomId, query.getClassRoomId());
        }

        Page<ClassInfo> classInfoPage = this.page(new Page<>(pageNum, pageSize), wrapper);
        List<ClassInfo> classInfoList = classInfoPage.getRecords();
        PageVO<ClassPageVO>voPage = PageVO.convert(classInfoPage,Collections.emptyList());
        if(CollectionUtils.isEmpty(classInfoList)){
            return voPage;
        }
        List<Long> classIdList = classInfoList.stream().map(ClassInfo::getId).collect(Collectors.toList());
        List<Long> roomIdList = classInfoList.stream()
                .map(ClassInfo::getClassRoomId)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        Map<Long, ClassRoom> roomMap = new HashMap<>();
        if(!CollectionUtils.isEmpty(roomIdList)){
            List<ClassRoom> roomList = classRoomMapper.selectBatchIds(roomIdList);
            Map<Long,ClassRoom> tempMap = roomList.stream().collect(Collectors.toMap(ClassRoom::getId, r -> r));
            roomMap.putAll(tempMap);
        }
        final Map<Long,Long> studentCountMap = new HashMap<>();
        List<Map<String,Object>> countResultList = studentInfoMapper.countStudentByClassIdList(classIdList);
        for(Map<String,Object> map : countResultList){
            Long cid = Long.parseLong(map.get("class_id").toString());
            Long cnt = Long.parseLong(map.get("cnt").toString());
            studentCountMap.put(cid, cnt);
        }

        List<ClassPageVO> voList = classInfoList.stream().map(item ->{
            ClassPageVO vo = new ClassPageVO();
            BeanUtils.copyProperties(item, vo);
            if(item.getClassRoomId() != null){
                ClassRoom room = roomMap.get(item.getClassRoomId());
                if(room != null){
                    vo.setRoomName(room.getRoomName());
                }
            }
            vo.setStudentCount(studentCountMap.getOrDefault(item.getId(),0L));
            return vo;
        }).collect(Collectors.toList());
        voPage.setRecords(voList);
        return voPage;
    }

    @Override
    public List<StudentClassVO> listAllStudentByClassId(Long classId) {
        // 1.校验班级是否存在
        ClassInfo classInfo = this.getById(classId);
        if(classInfo == null){
            throw new BusinessException(6001,"该班级不存在");
        }

        // 2.查询该班级全部学生
        LambdaQueryWrapper<StudentInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StudentInfo::getClassId, classId);
        wrapper.orderByDesc(StudentInfo::getId);
        List<StudentInfo> studentInfoList = studentInfoMapper.selectList(wrapper);

        // 3.entity转vo
        return studentInfoList.stream().map(entity -> {
            StudentClassVO vo = new StudentClassVO();
            BeanUtils.copyProperties(entity, vo);
            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    public void removeClass(Long classId) {
        //1. 判断班级是否存在
        ClassInfo classInfo = this.getById(classId);
        if(classInfo == null){
            throw new BusinessException(7001,"班级不存在");
        }

        //2. 判断该班级下面是否存在学生
        LambdaQueryWrapper<StudentInfo> studentWrapper = new LambdaQueryWrapper<>();
        studentWrapper.eq(StudentInfo::getClassId, classId);
        // 只查count，不需要查全部数据，性能更好
        long studentCount = studentInfoMapper.selectCount(studentWrapper);
        if(studentCount > 0){
            throw new BusinessException(7002,"该班级下存在学生，不允许删除");
        }

        //3. 可以删除，执行删除
        this.removeById(classId);
    }
    @Override
    public List<StudentAdjustVO> listAdjustStudent(Long batchId) {
        //直接调用xml自定义查询
        List<Map<String,Object>> mapList = studentInfoMapper.selectAdjustStudentList(batchId);
        return mapList.stream().map(map -> {
            StudentAdjustVO vo = new StudentAdjustVO();
            vo.setStudentId((Long) map.get("student_id"));
            vo.setStudentName((String) map.get("student_name"));
            vo.setStudentGender((Integer) map.get("student_gender"));
            String className = (String) map.get("class_name");
            vo.setClassName(className);
            vo.setHasClass(className != null);
            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    public void batchAddStudent(StudentJoinClassDTO dto){
        Long targetClassId = dto.getClassId();
        List<Long> studentIdList = dto.getStudentIdList();
        //1、校验目标班级是否存在
        ClassInfo targetClass = this.getById(targetClassId);
        if(targetClass == null){
            throw new BusinessException(8001,"目标班级不存在");
        }
        //2、校验传入的学生id是否全部存在
        List<StudentInfo> studentList = studentInfoMapper.selectBatchIds(studentIdList);
        //查询出来的学生数量 和传入id数量不一致，说明有无效学生id
        if(studentList.size() != studentIdList.size()){
            throw new BusinessException(8002,"存在无效的学生id，请检查学生数据");
        }
        //3、批量更新学生的class_id，把学生设置到目标班级
        LambdaUpdateWrapper<StudentInfo> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.in(StudentInfo::getId, studentIdList)
                .set(StudentInfo::getClassId, targetClassId);

        studentInfoMapper.update(null, updateWrapper);
    }

    @Override
    public void batchQuitStudent(StudentQuitClassDTO dto) {
        List<Long> studentIdList = dto.getStudentIdList();

        // 校验学生id全部真实存在
        List<StudentInfo> dbStudentList = studentInfoMapper.selectBatchIds(studentIdList);
        if(dbStudentList.size() != studentIdList.size()){
            throw new BusinessException(8101,"存在无效学生id，请检查数据");
        }

        // 批量更新 class_id = null
        LambdaUpdateWrapper<StudentInfo> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.in(StudentInfo::getId, studentIdList)
                .set(StudentInfo::getClassId, null);

        //一条sql批量更新
        studentInfoMapper.update(null, updateWrapper);
    }

    @Override
    public List<ClassSimpleVO> listAllClass() {
        List<ClassInfo> classInfoList = this.list();
        return classInfoList.stream().map(item -> {
            ClassSimpleVO vo = new ClassSimpleVO();
            BeanUtils.copyProperties(item, vo);
            return vo;
        }).collect(Collectors.toList());
    }

}
