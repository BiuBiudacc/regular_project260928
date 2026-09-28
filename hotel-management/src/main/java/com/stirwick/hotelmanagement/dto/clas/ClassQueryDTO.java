package com.stirwick.hotelmanagement.dto.clas;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;
@Data
public class ClassQueryDTO {
    /**
     * 班级名称，模糊
     */
    @Schema(description = "班级名称",  example = "")
    private String className;

    /**
     * 班级状态
     */
    @Schema(description = "班级状态",  example = "")
    private Integer classStatus;

    /**
     * 教室id
     */
    @Schema(description = "教室id",  example = "")
    private Long classRoomId;
}