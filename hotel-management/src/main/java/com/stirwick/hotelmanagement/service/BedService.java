package com.stirwick.hotelmanagement.service;
import com.stirwick.hotelmanagement.dto.bed.BedAvailableQueryDTO;
import com.stirwick.hotelmanagement.vo.bed.BedAvailableVO;
import java.util.List;
public interface BedService {
    List<BedAvailableVO> queryAvailableBed(BedAvailableQueryDTO dto);
}
