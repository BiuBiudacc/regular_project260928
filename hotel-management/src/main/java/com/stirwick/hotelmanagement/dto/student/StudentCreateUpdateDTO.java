package com.stirwick.hotelmanagement.dto.student;
import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;
@Data
public class StudentCreateUpdateDTO {
    @Schema(description = "学生名称",requiredMode = Schema.RequiredMode.REQUIRED, example = "")
    @NotBlank(message = "学生名称不能为空")
    private String studentName;
    @Schema(description = "身份证号码", example = "")
    private String idCard;
    @Schema(description = "学生性别", example = "")
    private Integer studentGender;
    @Schema(description = "学生年龄", example = "")
    private Integer studentAge;
    @Schema(description = "学生联系方式", example = "")
    private String studentPhone;
    @Schema(description = "学生紧急联系人", example = "")
    private String emergencyContactName;
    @Schema(description = "学生紧急联系人电话", example = "")
    private String emergencyContactPhone;
    @Schema(description = "所属批次id",requiredMode = Schema.RequiredMode.REQUIRED, example = "")
    @NotNull(message = "所属批次id不能为空")
    private Long batchId;
    @Schema(description = "所属班级id", example = "")
    private Long classId;
}
