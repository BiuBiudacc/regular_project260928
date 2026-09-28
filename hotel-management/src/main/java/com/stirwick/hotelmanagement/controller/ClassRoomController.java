package com.stirwick.hotelmanagement.controller;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.stirwick.hotelmanagement.dto.clas.ClassRoomAddDTO;
import com.stirwick.hotelmanagement.dto.clas.ClassRoomQueryDTO;
import com.stirwick.hotelmanagement.dto.clas.ClassRoomUpdateDTO;
import com.stirwick.hotelmanagement.result.Result;
import com.stirwick.hotelmanagement.service.ClassRoomService;
import com.stirwick.hotelmanagement.vo.page.PageVO;
import com.stirwick.hotelmanagement.vo.room.ClassRoomSelectVO;
import javax.annotation.Resource;
import java.util.List;

import com.stirwick.hotelmanagement.vo.room.ClassRoomVO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;          // @Tag 模块名称
import io.swagger.v3.oas.annotations.Operation;        // @Operation 接口说明
@RestController
@RequestMapping("/api/classRoom")
@Tag(name = "教室接口")
public class ClassRoomController {

    @Resource
    private ClassRoomService classRoomService;

    /**
     * 获取可用教室下拉列表（班级新增页面使用）
     */
    @GetMapping("/selectList")
    @Operation(summary = "教室信息查询接口-表单", description = "简单查询,服务于表单下拉组件")
    public Result<List<ClassRoomSelectVO>> selectList(){
        List<ClassRoomSelectVO> list = classRoomService.getAvailableRoomList();
        return Result.success(list);
    }

    @PostMapping("/page")
    @Operation(summary = "教室查询接口-分页", description = "")
    public Result<PageVO<ClassRoomVO>> page(@Validated @RequestBody ClassRoomQueryDTO dto){
        return Result.success(classRoomService.pageQuery(dto));
    }

    @PostMapping("/add")
    @Operation(summary = "教室创建接口", description = "")
    public Result<Void> add(@Validated @RequestBody ClassRoomAddDTO dto) {
        classRoomService.addClassRoom(dto);
        return Result.success();
    }
    @Operation(summary = "教室修改", description = "")
    @PostMapping("/update")
    public Result<Void> update(@Validated @RequestBody ClassRoomUpdateDTO dto){
        classRoomService.updateClassRoom(dto);
        return Result.success();
    }

    @PostMapping("/delete")
    @Operation(summary = "教室删除", description = "")
    public Result<Void> delete(@RequestParam Long id){
        classRoomService.deleteClassRoom(id);
        return Result.success();
    }
}

