package com.stirwick.hotelmanagement.controller;
import com.stirwick.hotelmanagement.dto.student.*;
import com.stirwick.hotelmanagement.vo.student.StudentBedRecordVO;
import com.stirwick.hotelmanagement.vo.student.StudentCurrentBedVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

import com.stirwick.hotelmanagement.result.Result;
import com.stirwick.hotelmanagement.service.BedStudentRelService;

import java.util.List;

@RestController
@RequestMapping("/api/bedStudentRel")
@Tag(name = "学生和床位关系管理")
public class BedStudentRelController {
    @Autowired
    private BedStudentRelService bedStudentRelService;

    @PostMapping("/batchCheckIn")
    @Operation(summary = "批量入住床位接口", description = "接收多个学生和床位关系以及共有的入住区间信息")
    public Result<Void> batchCheckIn(@Valid @RequestBody StudentBedBatchCheckInDTO dto) {
        bedStudentRelService.batchCheckIn(dto);
        return Result.success();
    }

    @GetMapping("/studentRecord")
    @Operation(summary = "入住记录查询接口", description = "根据学生id查询入住记录")
    public Result<List<StudentBedRecordVO>> getStudentBedRecord(@Valid StudentBedRecordQueryDTO dto) {
        List<StudentBedRecordVO> voList = bedStudentRelService.getStudentBedRecord(dto.getStudentId());
        return Result.success(voList);
    }

    @PostMapping("/deleteRelRecord")
    @Operation(summary = "入住记录删除接口", description = "接收入入住记录id,删除入住记录")
    public Result<Void> deleteRelRecord(@Valid @RequestBody BedRelDeleteDTO dto) {
        bedStudentRelService.deleteRelRecord(dto.getId());
        return Result.success();
    }

    @PostMapping("/batchCheckOut")
    @Operation(summary = "退房接口", description = "接收学生id集合以及退房时间")
    public Result<Void> batchCheckOut(@Valid @RequestBody StudentCheckOutDTO dto) {
        bedStudentRelService.batchCheckOut(dto);
        return Result.success();
    }
    @GetMapping("/getCurrentCheckIn")
    @Operation(summary = "学生当前入住信息查询接口", description = "查询指定学生当前的入住信息")
    public Result<StudentCurrentBedVO>getCurrentCheckIn(@Valid StudentCurrentBedQueryDTO dto){
        StudentCurrentBedVO vo = bedStudentRelService.getCurrentCheckIn(dto);
        return Result.success(vo);
    }

    @PostMapping("/renew")
    @Operation(summary = "学生续住接口（同床位延长入住时间）", description = "校验床位在延长区间无其他入住记录，更新预计退宿时间")
    public Result<Void> renew(@Valid @RequestBody StudentRenewDTO dto){
        bedStudentRelService.renew(dto);
        return Result.success();
    }



}
