package com.stirwick.hotelmanagement.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.stirwick.hotelmanagement.dto.student.StudentCreateUpdateDTO;
import com.stirwick.hotelmanagement.dto.student.StudentQueryDTO;
import com.stirwick.hotelmanagement.entity.BatchInfo;
import com.stirwick.hotelmanagement.entity.BedStudentRel;
import com.stirwick.hotelmanagement.entity.ClassInfo;
import com.stirwick.hotelmanagement.entity.StudentInfo;
import com.stirwick.hotelmanagement.exception.BusinessException;
import com.stirwick.hotelmanagement.mapper.BedStudentRelMapper;
import com.stirwick.hotelmanagement.mapper.StudentInfoMapper;
import com.stirwick.hotelmanagement.service.BatchInfoService;
import com.stirwick.hotelmanagement.service.ClassInfoService;
import com.stirwick.hotelmanagement.service.StudentService;
import com.stirwick.hotelmanagement.vo.page.PageVO;
import com.stirwick.hotelmanagement.vo.student.StudentPageVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.lang.invoke.LambdaConversionException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl extends ServiceImpl<StudentInfoMapper, StudentInfo> implements StudentService {

    @Autowired
    private ClassInfoService classInfoService;
    @Autowired
    private BatchInfoService batchInfoService;
    @Autowired
    private BedStudentRelMapper bedStudentRelMapper;

    @Override
    public PageVO<StudentPageVO> pageStudentList(StudentQueryDTO dto) {
        LocalDateTime now = LocalDateTime.now();
        LocalDate today = now.toLocalDate();
        Page<StudentPageVO> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        IPage<StudentPageVO> voList = this.baseMapper.selectStudentPage(page, dto);
        List<StudentPageVO> voResult = voList.getRecords().stream().map(vo->{
            if(vo.getCheckInStatus().equals("未入住")){
                vo.setWarnStatus(null);
            }else{
                LocalDate estimateLeaveDate = vo.getEstimateLeaveTime().toLocalDate();
                if(estimateLeaveDate.isEqual(today)){
                    vo.setWarnStatus("1");
                }else if(estimateLeaveDate.isAfter(today)){
                    vo.setWarnStatus("3");
                }else{
                    vo.setWarnStatus("2");
                }
            }
            return vo;
                }
        ).collect(Collectors.toList());
        return PageVO.convert(voList, voResult);
    }

    @Override
    public void createStudent(StudentCreateUpdateDTO dto) {
        BatchInfo batch = batchInfoService.getById(dto.getBatchId());
        if(batch ==null){
            throw new BusinessException(5001,"该批次不存在");
        }

        if(dto.getClassId()!=null){
            ClassInfo classInfo = classInfoService.getById(dto.getClassId());
            if(classInfo == null){
                throw new BusinessException(5002,"该班级不存在");
            }
        }
        StudentInfo student = new StudentInfo();
        BeanUtils.copyProperties(dto,student);
        boolean save = this.save(student);
        if (!save){
            throw new BusinessException(10004,"学生新增失败");
        }
    }
    @Override
    public void updateStudent(Long id,StudentCreateUpdateDTO dto) {
        StudentInfo studentDb = this.getById(id);
        if (studentDb == null) {
            throw new BusinessException(10005, "该学生不存在");
        }
        //2. 校验批次
        BatchInfo batch = batchInfoService.getById(dto.getBatchId());
        if (batch == null) {
            throw new BusinessException(10001, "选择的批次不存在");
        }

        if(dto.getClassId()!=null){
            ClassInfo classInfo = classInfoService.getById(dto.getClassId());
            if(classInfo == null){
                throw new BusinessException(5002,"该班级不存在");
            }
        }
        StudentInfo result = new StudentInfo();
        result.setId(id);
        BeanUtils.copyProperties(dto, result);
        boolean update = this.updateById(result);
        if (!update) {
            throw new BusinessException(10006, "学生修改失败");
        }
    }

    @Override
    public void deleteStudent(Long id) {
        // 1. 判断学生是否存在
        StudentInfo studentDb = this.getById(id);
        if (studentDb == null) {
            throw new BusinessException(10005, "该学生不存在");
        }
        LambdaQueryWrapper<BedStudentRel> relWrapper = new LambdaQueryWrapper<>();
        relWrapper.eq(BedStudentRel::getStudentId, id);
        long relCount = bedStudentRelMapper.selectCount(relWrapper);
        if(relCount>0){
            throw new BusinessException(10008, "该学生存在入住记录，不允许删除");
        }
        boolean remove = this.removeById(id);
        if (!remove) {
            throw new BusinessException(10009, "学生删除失败");
        }
    }

}

