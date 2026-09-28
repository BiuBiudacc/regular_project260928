package com.stirwick.hotelmanagement.dto;
import lombok.Data;
import javax.validation.constraints.NotBlank;
import io.swagger.v3.oas.annotations.media.Schema;
@Data
@Schema(description = "登录请求参数对象")
public class LoginDTO {
    @NotBlank(message = "账号不能为空")
    //文档参数数码,标记是否必填,示例参数
    @Schema(description = "登录用户名", requiredMode = Schema.RequiredMode.REQUIRED, example = "admin")
    private String username;
    @Schema(description = "登录密码", requiredMode = Schema.RequiredMode.REQUIRED, example = "123456")
    @NotBlank(message = "密码不能为空")
    private String password;
}
