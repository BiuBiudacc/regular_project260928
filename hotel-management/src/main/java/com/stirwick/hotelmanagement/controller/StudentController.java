package com.stirwick.hotelmanagement.controller;
import com.stirwick.hotelmanagement.dto.student.StudentCreateUpdateDTO;
import com.stirwick.hotelmanagement.dto.student.StudentQueryDTO;
import com.stirwick.hotelmanagement.result.Result;
import com.stirwick.hotelmanagement.service.StudentService;
import com.stirwick.hotelmanagement.vo.page.PageVO;
import com.stirwick.hotelmanagement.vo.student.StudentPageVO;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;          // @Tag 模块名称
import io.swagger.v3.oas.annotations.Operation;        // @Operation 接口说明

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/api/student")
@Tag(name = "学生接口")
public class StudentController {
    @Autowired
    private StudentService studentService;

    @PostMapping("/page")
    @Operation(summary = "学生信息接口,支持分页", description = "返回学生列表信息")
    public Result<PageVO<StudentPageVO>> page(@Validated @RequestBody StudentQueryDTO dto){
        return Result.success(studentService.pageStudentList(dto));
    }

    @PostMapping("/create")
    @Operation(summary = "学生信息创建接口", description = "")
    public Result<Void> create(@Validated @RequestBody StudentCreateUpdateDTO dto){
        studentService.createStudent(dto);
        return Result.success();
    }

    @PostMapping("/update")
    @Operation(summary = "学生修改信息接口", description = "")
    public Result<Void>update(@NotNull(message = "学生id不能为空") @RequestParam @Parameter(description = "学生", example = "1") Long id, @Validated @RequestBody StudentCreateUpdateDTO dto){
        studentService.updateStudent(id, dto);
        return Result.success();
    }

    @PostMapping("/delete/{id}")
    @Operation(summary = "学生删除接口", description = "")
    public Result<Void> delete(@PathVariable @NotNull(message = "学生id不能为空") @Parameter(description = "学生id", example = "1") Long id) {
        studentService.deleteStudent(id);
        return Result.success();
    }

}
