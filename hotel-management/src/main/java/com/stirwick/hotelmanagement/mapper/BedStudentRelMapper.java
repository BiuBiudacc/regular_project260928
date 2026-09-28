package com.stirwick.hotelmanagement.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.stirwick.hotelmanagement.entity.BedStudentRel;
import com.stirwick.hotelmanagement.vo.student.StudentBedRecordVO;
import com.stirwick.hotelmanagement.vo.student.StudentCurrentBedVO;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface BedStudentRelMapper extends BaseMapper<BedStudentRel> {
    /**
     * 查询时间段内有入住冲突的床位ID
     */
    List<Long> selectConflictBedIds(@Param("bedIdList") List<Long> bedIdList,
                                    @Param("checkInDate") LocalDateTime checkInDate,
                                    @Param("estimateOutDate") LocalDateTime estimateOutDate);

    /**
     * 批量插入
     */
    int insertBatchSomeColumn(@Param("list") List<BedStudentRel> list);

    List<StudentBedRecordVO> selectStudentBedRecordByStudentId(@Param("studentId" )Long studentId);

    StudentCurrentBedVO selectCurrentCheckInByStudentId(@Param("studentId")Long studentId);
}
