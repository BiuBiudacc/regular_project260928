package com.stirwick.hotelmanagement.controller;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.stirwick.hotelmanagement.dto.clas.ClassRoomTypeDTO;
import com.stirwick.hotelmanagement.dto.clas.ClassRoomTypePageDTO;
import com.stirwick.hotelmanagement.entity.ClassRoomType;
import com.stirwick.hotelmanagement.exception.BusinessException;
import com.stirwick.hotelmanagement.result.Result;
import com.stirwick.hotelmanagement.service.ClassRoomTypeService;
import com.stirwick.hotelmanagement.vo.page.PageVO;
import com.stirwick.hotelmanagement.vo.room.ClassRoomTypeVO;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;
import io.swagger.v3.oas.annotations.tags.Tag;          // @Tag 模块名称
import io.swagger.v3.oas.annotations.Operation;        // @Operation 接口说明
@RestController
@RequestMapping("/api/classRoomType")
@Tag(name = "教室型号接口")
public class ClassRoomTypeController {
    @Autowired
    private ClassRoomTypeService classRoomTypeService;

    @PostMapping("/page")
    @Operation(summary = "教室型号列表,支持分页", description = "返回教室型号信息")
    public Result<PageVO<ClassRoomTypeVO>> page(@Validated @RequestBody ClassRoomTypePageDTO dto){
        return Result.success(classRoomTypeService.pageQuery(dto));
    }

    @GetMapping("/listAll")
    @Operation(summary = "教室型号列表", description = "返回教室型号信息")
    public Result<List<ClassRoomTypeVO>> listAll() {
        List<ClassRoomType> list = classRoomTypeService.list(
                new LambdaQueryWrapper<ClassRoomType>().orderByDesc(ClassRoomType::getId)
        );
        List<ClassRoomTypeVO> voList = list.stream().map(entity -> {
            ClassRoomTypeVO vo = new ClassRoomTypeVO();
            vo.setId(entity.getId());
            vo.setTypeName(entity.getTypeName());
            return vo;
        }).collect(Collectors.toList());
        return Result.success(voList);
    }

    /**
     * 新增教室型号
     * POST /api/classRoomModel/add
     */
    @PostMapping("/add")
    @Operation(summary = "教室型号新增接口", description = "")
    public Result<Void> add(@Validated @RequestBody ClassRoomTypeDTO dto) {
        // 判断型号名称不能重复
        LambdaQueryWrapper<ClassRoomType> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ClassRoomType::getTypeName, dto.getTypeName());
        long count = classRoomTypeService.count(wrapper);
        if(count > 0){
            throw new BusinessException(9001,"该型号名称已存在");
        }
        ClassRoomType classRoomType = new ClassRoomType();
        BeanUtils.copyProperties(dto, classRoomType);
        classRoomTypeService.save(classRoomType);
        return Result.success();
    }

    @PostMapping("/update")
    @Operation(summary = "教室型号修改接口", description = "")
    public Result<Void> update(@Validated @RequestBody ClassRoomTypeDTO dto) {
        if(dto.getId() == null){
            throw new BusinessException(9000,"id不能为空");
        }
        ClassRoomType dbModel = classRoomTypeService.getById(dto.getId());
        if(dbModel == null){
            throw new BusinessException(9002,"型号不存在");
        }
        // 修改时校验名称不能和其他记录重复
        LambdaQueryWrapper<ClassRoomType> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ClassRoomType::getTypeName, dto.getTypeName())
                .ne(ClassRoomType::getId, dto.getId());
        long count = classRoomTypeService.count(wrapper);
        if(count > 0){
            throw new BusinessException(9001,"该型号名称已存在");
        }
        ClassRoomType model = new ClassRoomType();
        BeanUtils.copyProperties(dto, model);
        classRoomTypeService.updateById(model);
        return Result.success();
    }

    @PostMapping("/remove")
    @Operation(summary = "教室型号删除接口", description = "")
    public Result<Void> remove(@RequestParam(value = "id",required = false)  @Parameter(description = "型号id", example = "1") Long id) {
        if(id == null){
            throw new BusinessException(9000,"id不能为空");
        }
        ClassRoomType dbModel = classRoomTypeService.getById(id);
        if(dbModel == null){
            throw new BusinessException(9002,"型号不存在");
        }
        classRoomTypeService.removeById(id);
        return Result.success();
    }
}
