package com.stirwick.hotelmanagement.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.stirwick.hotelmanagement.dto.student.StudentQueryDTO;
import com.stirwick.hotelmanagement.entity.StudentInfo;
import com.stirwick.hotelmanagement.vo.student.StudentPageVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

@Mapper
public interface StudentInfoMapper extends BaseMapper<StudentInfo> {
    /**
     * 根据班级id集合批量统计每个班级学生数量
     */
    List<Map<String,Object>> countStudentByClassIdList(@Param("classIdList") List<Long> classIdList);
    List<Map<String,Object>> selectAdjustStudentList(@Param("batchId") Long batchId);
    IPage<StudentPageVO> selectStudentPage(Page<StudentPageVO> page, @Param("dto") StudentQueryDTO dto);
    Long selectCountByIds(@Param("idList") List<Long> idList);
}
