package com.stirwick.hotelmanagement.dto.clas;
import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;
@Data
public class ClassCreateDTO {
    /**
     * 班级名称 必填
     */
    @Schema(description = "班级名称",  requiredMode = Schema.RequiredMode.REQUIRED, example = "")
    @NotBlank(message = "班级名称不能为空")
    private String className;
    /**
     * 班级描述
     */
    @Schema(description = "班级描述",  example = "")
    private String classDesc;

    /**
     * 班级管理老师名称 必填
     */
    @Schema(description = "班级管理老师",  example = "")
    private String manageTeacherName;

    /**
     * 老师联系方式
     */
    @Schema(description = "老师联系电话",  example = "")
    private String teacherPhone;

    /**
     * 班级状态 1待开班 2授课中 3已结束 必填
     */
    @NotNull(message = "班级状态不能为空")
    private Integer classStatus;
    /**
     * 班级所在教室id，可以为null
     */
    @Schema(description = "班级所在教室",  example = "")
    private Long classRoomId;
}
