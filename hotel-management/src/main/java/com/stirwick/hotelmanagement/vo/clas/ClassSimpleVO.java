package com.stirwick.hotelmanagement.vo.clas;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
@Data
public class ClassSimpleVO {
    @Schema(description = "班级id", example ="1")
    private Long id;
    @Schema(description = "班级名称", example ="1")
    private String className;
}
