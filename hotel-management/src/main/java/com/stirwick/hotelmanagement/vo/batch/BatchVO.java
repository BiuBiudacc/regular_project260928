package com.stirwick.hotelmanagement.vo.batch;

import lombok.Data;
import java.time.LocalDateTime;
import io.swagger.v3.oas.annotations.media.Schema;
@Data
public class BatchVO {
    @Schema(description = "批次id", example ="1")
    private Long batchId;
    @Schema(description = "批次名称", example ="批次名称")
    private String batchName;
    @Schema(description = "批次描述", example ="批次描述")
    private String batchDesc;
    @Schema(description = "预计学生数量", example ="12")
    private Integer estimateStudentNum;
    @Schema(description = "预计老师数量", example ="12")
    private Integer estimateTeacherNum;
    @Schema(description = "主管老师姓名", example ="张三")
    private String chargeTeacher;
    @Schema(description = "主管老师手机号码", example =" ")
    private String chargePhone;
    @Schema(description = "批次开始时间", example =" ")
    private LocalDateTime batchStartTime;
    @Schema(description = "批次结束时间", example =" ")
    private LocalDateTime batchEndTime;
    @Schema(description = "数据创建时间", example =" ")
    private LocalDateTime createTime;
    @Schema(description = "数据修改时间", example =" ")
    private LocalDateTime updateTime;
}
