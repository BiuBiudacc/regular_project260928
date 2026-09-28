package com.stirwick.hotelmanagement.dto.batch;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
@Data
public class BatchUpdateDTO {
    @NotNull(message = "批次id不能为空")
    @Schema(description = "批次id", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long batchId;

    @Schema(description = "批次名称", example = "批次名称")
    private String batchName;

    @Schema(description = "批次描述", example = "批次描述")
    private String batchDesc;

    @PositiveOrZero(message = "预估学生数量不能为负数")
    @Schema(description = "批次预估学生数量", example = "12")
    private Integer estimateStudentNum;

    @PositiveOrZero(message = "预估老师数量不能为负数")
    @Schema(description = "预估老师数量", example = "12")
    private Integer estimateTeacherNum;
    @Schema(description = "主管老师", example = "张老师")
    private String chargeTeacher;
    @Schema(description = "主管老师电话", example = "张老师电话")
    private String chargePhone;
    @Schema(description = "批次开始时间", example = "")
    private LocalDateTime batchStartTime;
    @Schema(description = "批次结束时间", example = "")
    private LocalDateTime batchEndTime;
}
