package com.stirwick.hotelmanagement.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.stirwick.hotelmanagement.entity.Bed;
import com.stirwick.hotelmanagement.vo.bed.BedAvailableVO;
import org.apache.ibatis.annotations.Param;
import java.time.LocalDate;
import java.util.List;
public interface BedMapper extends BaseMapper<Bed> {
    List<BedAvailableVO> selectFreeBed(@Param("startDate") LocalDate startDate,
                                       @Param("endDate") LocalDate endDate,
                                       @Param("studentGender") Integer studentGender);

    List<BedAvailableVO> selectOccupiedBedByRoomIds(@Param("roomIdList") List<Long> roomIdList,
                                                    @Param("startDate") LocalDate startDate,
                                                    @Param("endDate") LocalDate endDate);

    Long selectCountByIds(@Param("idList") List<Long> idList);
}
