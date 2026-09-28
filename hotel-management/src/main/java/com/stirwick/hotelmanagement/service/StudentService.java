package com.stirwick.hotelmanagement.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.stirwick.hotelmanagement.dto.student.StudentCreateUpdateDTO;
import com.stirwick.hotelmanagement.dto.student.StudentQueryDTO;
import com.stirwick.hotelmanagement.entity.StudentInfo;
import com.stirwick.hotelmanagement.vo.page.PageVO;
import com.stirwick.hotelmanagement.vo.student.StudentPageVO;

public interface StudentService extends IService<StudentInfo> {
    PageVO<StudentPageVO> pageStudentList(StudentQueryDTO dto);
    void createStudent(StudentCreateUpdateDTO dto);
    void updateStudent(Long id, StudentCreateUpdateDTO dto);
    void deleteStudent(Long id);
}
