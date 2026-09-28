package com.stirwick.hotelmanagement.service;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.stirwick.hotelmanagement.dto.clas.*;
import com.stirwick.hotelmanagement.entity.ClassInfo;
import com.stirwick.hotelmanagement.vo.clas.ClassSimpleVO;
import com.stirwick.hotelmanagement.vo.clas.StudentAdjustVO;
import com.stirwick.hotelmanagement.vo.clas.StudentClassVO;
import com.stirwick.hotelmanagement.vo.page.PageVO;
import com.stirwick.hotelmanagement.vo.clas.ClassPageVO;
import java.util.List;

public interface ClassInfoService extends IService<ClassInfo>{
    PageVO<ClassPageVO> pageQuery(ClassPageQuery classPageQuery);
    void createClass(ClassCreateDTO dto);
    void updateClass(ClassUpdateDTO dto);
    List<StudentClassVO> listAllStudentByClassId(Long classId);
    void removeClass(Long classId);
    List<StudentAdjustVO> listAdjustStudent(Long batchId);
    void batchAddStudent(StudentJoinClassDTO dto);
    void batchQuitStudent(StudentQuitClassDTO dto);
    List<ClassSimpleVO> listAllClass();

}
