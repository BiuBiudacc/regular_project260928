package com.stirwick.hotelmanagement.vo.clas;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class StudentClassVO {
    /**
     * 学生id
     */
    @Schema(description = "学生id", example ="1")
    private Long id;
    /**
     * 学生名称
     */
    @Schema(description = "学生名称", example ="1")
    private String studentName;
    /**
     * 身份证号
     */
    @Schema(description = "学生身份证号码", example ="1")
    private String idCard;
    /**
     * 学生性别
     */
    @Schema(description = "学生性别", example ="1")
    private Integer studentGender;
    /**
     * 学生年龄
     */
    @Schema(description = "学生年龄", example ="1")
    private Integer studentAge;
    /**
     * 学生电话
     */
    @Schema(description = "学生电话", example ="1")
    private String studentPhone;
    /**
     * 紧急联系人姓名
     */
    @Schema(description = "学生紧急联系人", example ="1")
    private String emergencyContactName;
    /**
     * 紧急联系人电话
     */
    @Schema(description = "学生联系电话", example ="1")
    private String emergencyContactPhone;
    /**
     * 所属批次id
     */
    @Schema(description = "学生所属批次", example ="1")
    private Long batchId;
    /**
     * 所属班级id
     */
    private Long classId;
}
