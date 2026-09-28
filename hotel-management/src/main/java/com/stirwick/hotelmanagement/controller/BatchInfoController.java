package com.stirwick.hotelmanagement.controller;
import com.stirwick.hotelmanagement.dto.batch.BatchPageQuery;
import com.stirwick.hotelmanagement.dto.batch.BatchUpdateDTO;
import com.stirwick.hotelmanagement.dto.batch.BatchQueryDTO;
import com.stirwick.hotelmanagement.result.Result;
import com.stirwick.hotelmanagement.service.BatchInfoService;
import com.stirwick.hotelmanagement.vo.batch.BatchSimpleVO;
import com.stirwick.hotelmanagement.vo.batch.BatchVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.validation.annotation.Validated;
import com.stirwick.hotelmanagement.dto.batch.BatchSaveDTO;
import io.swagger.v3.oas.annotations.tags.Tag;          // @Tag 模块名称
import io.swagger.v3.oas.annotations.Operation;        // @Operation 接口说明
import io.swagger.v3.oas.annotations.Parameter;
import com.stirwick.hotelmanagement.vo.page.PageVO;

import java.util.List;

@RestController
@RequestMapping("/api/batch")
@RequiredArgsConstructor
@Tag(name = "批次接口")
public class BatchInfoController {

    private final BatchInfoService batchInfoService;

    /**
     * 批次分页查询
     * GET /api/batch/page
     */
    @GetMapping("/page")
    @Operation(summary = "批次分页查询接口", description = "查询全量批次信息,支持分页")
    public Result<PageVO<BatchVO>> page(@RequestParam(defaultValue = "1") @Parameter(description = "页码", example = "1") Long pageNum, @RequestParam(defaultValue = "10") @Parameter(description = "数量", example = "10") Long pageSize){
        PageVO<BatchVO> pageVo = batchInfoService.pageBatch(pageNum, pageSize);
        return Result.success(pageVo);
    }

    @PostMapping("/create")
    @Operation(summary = "批次添加接口", description = "创建批次")
    public Result<Long> create(@RequestBody @Validated BatchSaveDTO dto){
        Long batchId = batchInfoService.createBatch(dto);
        return Result.success();
    }

    /**
     * 修改批次
     * PUT /api/batch/update
     */
    @PostMapping("/update")
    @Operation(summary = "批次修改接口", description = "基于批次id进行修改")
    public Result<Void> update(@RequestBody @Validated BatchUpdateDTO dto){
        batchInfoService.updateBatch(dto);
        return Result.success();
    }

    @PostMapping("/delete/{batchId}")
    @Operation(summary = "批次删除接口", description = "仅批次下没有关联数据时才会删除")
    public Result<Void> delete(@PathVariable @Parameter(description = "批次id", example = "1") Long batchId) {
        batchInfoService.deleteBatch(batchId);
        return Result.success();
    }
    @PostMapping("/advancedPage")
    @Operation(summary = "高级查询接口", description = "支持根据批次id,批次信息,主管老师进行查询")
    public Result<PageVO<BatchVO>>advancePage(@RequestBody BatchPageQuery batchPageQuery){
        Long pageNum = batchPageQuery.getPageNum();
        Long pageSize = batchPageQuery.getPageSize();
        BatchQueryDTO queryDTO = batchPageQuery.getQuery();
        // 做保护，防止前端传null
        if(pageNum == null || pageNum < 1){
            pageNum = 1L;
        }
        if(pageSize == null || pageSize < 1){
            pageSize = 10L;
        }
        // 如果query为null，构造空对象，避免service内部空指针
        if(queryDTO == null){
            queryDTO = new BatchQueryDTO();
        }
        PageVO<BatchVO> voPage = batchInfoService.pageBatchAdvanced(pageNum, pageSize, queryDTO);
        return Result.success(voPage);
    }

    @GetMapping("/listSimple")
    @Operation(summary = "批次简易查询接口", description = "仅返回必要信息")
    public Result<List<BatchSimpleVO>> listSimple() {
        List<BatchSimpleVO> voList = batchInfoService.listAllBatchSimple();
        return Result.success(voList);
    }

}
