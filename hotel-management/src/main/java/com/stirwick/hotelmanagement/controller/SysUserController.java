package com.stirwick.hotelmanagement.controller;
import com.stirwick.hotelmanagement.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.stirwick.hotelmanagement.exception.BusinessException;
import com.stirwick.hotelmanagement.enums.ResultCodeEnum;
import org.springframework.validation.annotation.Validated;
import com.stirwick.hotelmanagement.service.SysUserService;
import com.stirwick.hotelmanagement.dto.LoginDTO;
import javax.annotation.Resource;
import io.swagger.v3.oas.annotations.tags.Tag;          // @Tag 模块名称
import io.swagger.v3.oas.annotations.Operation;        // @Operation 接口说明
import io.swagger.v3.oas.annotations.Parameter;
import java.util.Map;
@RestController
@RequestMapping("/api/user")
@Tag(name = "登录接口")
public class SysUserController {
    @Autowired
    private SysUserService sysUserService;

    @PostMapping("/login")
    @Operation(summary = "登录接口", description = "校验用户登录详细,返回token")
    public Result<Map<String,String>> login(@Validated @RequestBody LoginDTO loginDTO){
        String token = sysUserService.login(loginDTO);
        System.out.println("token打印测试"+token);
        return Result.success(Map.of("token", token));
    }

    @GetMapping("/user/list")
    public Result<?> test(){
        throw new BusinessException(ResultCodeEnum.ROOM_NOT_EXIST,"测试抛出房间不存在");
        //return Result.success();
    }
}
