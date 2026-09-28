package com.stirwick.hotelmanagement.service;
import com.stirwick.hotelmanagement.dto.student.StudentBedBatchCheckInDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.stirwick.hotelmanagement.dto.student.StudentCheckOutDTO;
import com.stirwick.hotelmanagement.dto.student.StudentCurrentBedQueryDTO;
import com.stirwick.hotelmanagement.dto.student.StudentRenewDTO;
import com.stirwick.hotelmanagement.entity.BedStudentRel;
import com.stirwick.hotelmanagement.vo.student.StudentBedRecordVO;
import com.stirwick.hotelmanagement.vo.student.StudentCurrentBedVO;

import java.util.List;

public interface BedStudentRelService extends IService<BedStudentRel> {
    void batchCheckIn(StudentBedBatchCheckInDTO dto);
    List<StudentBedRecordVO> getStudentBedRecord(Long studentId);
    void deleteRelRecord(Long id);
    void batchCheckOut(StudentCheckOutDTO dto);
    StudentCurrentBedVO getCurrentCheckIn(StudentCurrentBedQueryDTO dto);
    void renew(StudentRenewDTO dto);
}
