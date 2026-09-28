package com.stirwick.hotelmanagement.controller;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.stirwick.hotelmanagement.dto.clas.*;
import com.stirwick.hotelmanagement.exception.BusinessException;
import com.stirwick.hotelmanagement.result.Result;
import com.stirwick.hotelmanagement.service.ClassInfoService;
import com.stirwick.hotelmanagement.vo.clas.ClassPageVO;
import com.stirwick.hotelmanagement.vo.clas.ClassSimpleVO;
import com.stirwick.hotelmanagement.vo.clas.StudentAdjustVO;
import com.stirwick.hotelmanagement.vo.clas.StudentClassVO;
import com.stirwick.hotelmanagement.vo.page.PageVO;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import java.util.List;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;          // @Tag 模块名称
import io.swagger.v3.oas.annotations.Operation;        // @Operation 接口说明
@RestController
@RequestMapping("/api/class")
@Tag(name = "班级接口")
public class ClassInfoController {

    @Autowired
    private ClassInfoService classInfoService;

    @PostMapping("/pageQuery")
    @Operation(summary = "班级分页查询接口", description = "查询班级信息,支持检索")
    public Result<PageVO<ClassPageVO>> pageQuery(@RequestBody ClassPageQuery classPageQuery){
        return Result.success(classInfoService.pageQuery(classPageQuery));
    }

    /**
     * 新增班级
     */
    @PostMapping("/create")
    @Operation(summary = "班级创建接口", description = "接收班级信息,创建班级")
    public Result<Void>create(@Validated @RequestBody ClassCreateDTO dto){
        classInfoService.createClass(dto);
        return Result.success();
    }

    /**
     * 修改班级
     */
    @PostMapping("/update")
    @Operation(summary = "班级信息修改接口", description = "修改班级信息")
    public Result<Void> update(@Validated @RequestBody ClassUpdateDTO dto){
        classInfoService.updateClass(dto);
        return Result.success();
    }

    @GetMapping("/listStudent")
    @Operation(summary = "班级学生列表接口", description = "返回指定班级的学生信息")
    public Result<List<StudentClassVO>> listStudent(@RequestParam("classId") Long classId){
        List<StudentClassVO> voList = classInfoService.listAllStudentByClassId(classId);
        return Result.success(voList);
    }

    /**
     * 删除班级
     * POST /api/class/remove
     * 参数 classId
     */
    @PostMapping("/remove")
    @Operation(summary = "班级删除接口", description = "删除没有学生的班级")
    public Result<Void> remove(@RequestParam(value = "classId",required = false) @Parameter(description = "班级id", example = "1") Long classId){
        if(classId == null){
            throw new BusinessException(7000,"班级id不能为空");
        }
        classInfoService.removeClass(classId);
        return Result.success();
    }

    @GetMapping("/adjustStudentList")
    @Operation(summary = "指定批次学生学习查询接口", description = "仅返回需要的字段")
    public Result<List<StudentAdjustVO>> adjustStudentList(
            @RequestParam(value = "batchId",required = false) @Parameter(description = "批次id", example = "1")Long batchId
    ){
        List<StudentAdjustVO> voList = classInfoService.listAdjustStudent(batchId);
        return Result.success(voList);
    }

    /**
     * 批量学生加入班级（支持转班）
     * POST /api/class/batchAddStudent
     */
    @PostMapping("/batchAddStudent")
    @Operation(summary = "批量设置班级学生接口", description = "接收班级id和学生id集合")
    public Result<Void> batchAddStudent(@Validated @RequestBody StudentJoinClassDTO dto){
        classInfoService.batchAddStudent(dto);
        return Result.success();
    }

    /**
     * 批量学生退出班级
     * POST /api/class/batchQuitStudent
     */
    @PostMapping("/batchQuitStudent")
    @Operation(summary = "批量退出班级接口", description = "接收学生id")
    public Result<Void> batchQuitStudent(@Validated @RequestBody StudentQuitClassDTO dto){
        classInfoService.batchQuitStudent(dto);
        return Result.success();
    }

    @GetMapping("/listSimple")
    @Operation(summary = "班级信息简易查询接口", description = "仅返回班级id,班级名称")
    public Result<List<ClassSimpleVO>> listSimple(){
        List<ClassSimpleVO> voList = classInfoService.listAllClass();
        return Result.success(voList);
    }




}

