package com.stirwick.hotelmanagement.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.stirwick.hotelmanagement.dto.bed.BedAvailableQueryDTO;
import com.stirwick.hotelmanagement.entity.Bed;
import com.stirwick.hotelmanagement.mapper.BedMapper;
import com.stirwick.hotelmanagement.service.BedService;
import com.stirwick.hotelmanagement.vo.bed.BedAvailableVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BedServiceImpl extends ServiceImpl<BedMapper, Bed> implements BedService {
    @Autowired
    private BedMapper bedMapper;

    @Override
    public List<BedAvailableVO> queryAvailableBed(BedAvailableQueryDTO dto) {
        // 时间默认值
        LocalDate startDate = dto.getCheckInDate() == null ? LocalDate.now() : dto.getCheckInDate();
        LocalDate endDate = dto.getCheckOutDate() == null ? LocalDate.now() : dto.getCheckOutDate();

        // 1. 查询符合条件的空闲床位
        List<BedAvailableVO> freeBedVoList = bedMapper.selectFreeBed(startDate, endDate, dto.getStudentGender());
        if (CollectionUtils.isEmpty(freeBedVoList)) {
            return Collections.emptyList();
        }
        //空闲床位：状态空闲，studentName置空
        for (BedAvailableVO vo : freeBedVoList) {
            vo.setBedStatus("空闲");
            vo.setStudentName("");
        }

        // 提取房间id集合，去重
        List<Long> roomIdList = freeBedVoList.stream()
                .map(BedAvailableVO::getRoomId)
                .distinct()
                .collect(Collectors.toList());

        // 2. 根据房间id，查询这批房间目标时间段内【占用床位】完整VO
        List<BedAvailableVO> rawOccupiedBedList = bedMapper.selectOccupiedBedByRoomIds(roomIdList, startDate, endDate);


        List<BedAvailableVO> occupiedBedVoList = new ArrayList<>();

        // 遍历原始列表
        for (BedAvailableVO rawVo : rawOccupiedBedList) {
            Long currBedId = rawVo.getBedId();
            String currStudentName = rawVo.getStudentName();
            if (currStudentName == null || currStudentName.isBlank()) {
                continue;
            }

            // 去新list里面找有没有相同bedId
            BedAvailableVO existVo = null;
            for (BedAvailableVO item : occupiedBedVoList) {
                if (currBedId.equals(item.getBedId())) {
                    existVo = item;
                    break;
                }
            }

            if (existVo != null) {
                // 找到同床位，拼接名字
                existVo.setStudentName(existVo.getStudentName() + "," + currStudentName);
            } else {
                // 不存在，新增到list
                rawVo.setBedStatus("使用中");
                occupiedBedVoList.add(rawVo);
            }
        }

        //3、合并空闲床位 + 占用床位
        List<BedAvailableVO> resultList = new ArrayList<>(freeBedVoList);
        resultList.addAll(occupiedBedVoList);

        return resultList;
    }
}
