package com.stirwick.hotelmanagement.vo.student;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class StudentPageVO {
    @Schema(description = "学生id", example = "1")
    private Long id;
    @Schema(description = "学生姓名", example = "1")
    private String studentName;
    @Schema(description = "批次id", example = "1")
    private Long batchId;
    @Schema(description = "批次名称", example = "1")
    private String batchName;
    @Schema(description = "学生性别", example = "1")
    private Integer studentGender;
    @Schema(description = "学生年龄", example = "")
    private Integer studentAge;
    @Schema(description = "班级id", example = "1")
    private String className;
    @Schema(description = "联系方式", example = "1")
    private String studentPhone;
    @Schema(description = "身份证号码", example = "")
    private String idCard;
    @Schema(description = "学生紧急联系人", example = "")
    private String emergencyContactName;
    @Schema(description = "学生紧急联系人电话", example = "")
    private String emergencyContactPhone;
    @Schema(description = "所属班级id", example = "")
    private Long classId;
    @Schema(description = "宿舍id", example = "1")
    private Long roomId;
    @Schema(description = "宿舍名称", example = "1")
    private String roomName;
    @Schema(description = "床位id", example = "1")
    private Long bedId;
    @Schema(description = "床位名称", example = "1")
    private String bedName;
    @Schema(description = "入住状态", example = "已入住/未入住")
    private String checkInStatus;
    @Schema(description = "到期退房提醒,1代表今日到期,2代表未到期,3代表已超期,", example = "")
    private String warnStatus;
    @Schema(description = "预计离开日期", example = "")
    private LocalDateTime estimateLeaveTime;
}



