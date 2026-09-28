package com.stirwick.hotelmanagement.dto.clas;
import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;
@Data
public class ClassUpdateDTO {
    /**
     * 班级主键id，修改必填
     */
    @Schema(description = "班级id",  requiredMode = Schema.RequiredMode.REQUIRED, example = "")
    @NotNull(message = "班级id不能为空")
    private Long id;
    /**
     * 班级名称
     */
    @Schema(description = "班级名称",  requiredMode = Schema.RequiredMode.REQUIRED, example = "")
    @NotBlank(message = "班级名称不能为空")
    private String className;

    /**
     * 班级描述
     */
    @Schema(description = "班级描述", example = "")
    private String classDesc;
    /**
     * 班级管理老师名称
     */
    @Schema(description = "班级老师", example = "")
    private String manageTeacherName;

    @Schema(description = "班级状态",  requiredMode = Schema.RequiredMode.REQUIRED, example = "")
    @NotNull(message = "状态不能为空")
    private Integer classStatus;
    /**
     * 老师联系方式
     */
    @Schema(description = "老师电话", example = "")
    private String teacherPhone;

    /**
     * 班级所在教室id，允许null（解除绑定）
     */
    private Long classRoomId;
}
