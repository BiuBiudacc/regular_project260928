package com.stirwick.hotelmanagement.controller;
import com.stirwick.hotelmanagement.dto.bed.BedAvailableQueryDTO;
import com.stirwick.hotelmanagement.result.Result;
import com.stirwick.hotelmanagement.service.BedService;
import com.stirwick.hotelmanagement.vo.bed.BedAvailableVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import java.util.List;
@RestController
@RequestMapping("/api/bed")
@Tag(name = "床位接口")
public class BedController {

    @Resource
    private BedService bedService;

    @PostMapping("/available")
    @Operation(summary = "可用床位查询接口", description = "查询房间全部床位（空闲+占用），同一床位多条入住记录会逗号拼接studentName")
    public Result<List<BedAvailableVO>> availableBed(@RequestBody BedAvailableQueryDTO dto) {
        List<BedAvailableVO> voList = bedService.queryAvailableBed(dto);
        return Result.success(voList);
    }
}
