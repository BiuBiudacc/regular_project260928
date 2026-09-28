package com.stirwick.hotelmanagement.vo.clas;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class StudentAdjustVO {
    /**
     * 学生id
     */
    @Schema(description = "学生id", example ="1")
    private Long studentId;
    /**
     * 学生姓名
     */
    @Schema(description = "学生姓名", example ="1")
    private String studentName;
    /**
     * 学生性别
     */
    @Schema(description = "性别", example ="1")
    private Integer studentGender;
    /**
     * 是否已分配班级 true已有班级 false无班级
     */
    @Schema(description = "是否已有班级", example ="1")
    private Boolean hasClass;
    /**
     * 所属班级名称，没有班级为null
     */
    @Schema(description = "班级名称", example ="1")
    private String className;
}
