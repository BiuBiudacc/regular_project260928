package com.stirwick.hotelmanagement.dto.batch;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;

@Data
public class BatchSaveDTO {

    /**
     * 批次名称，非空
     */
    @NotBlank(message = "批次名称不能为空")
    @Schema(description = "批次名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "批次名称")
    private String batchName;

    /**
     * 批次描述，可以为空
     */
    @Schema(description = "批次描述", example = "批次描述")
    private String batchDesc;

    /**
     * 预估学生数量，>=0
     */
    @PositiveOrZero(message = "预估学生数量不能为负数")
    @Schema(description = "预估学生数量", example = "1")
    private Integer estimateStudentNum;

    /**
     * 预估老师数量，>=0
     */
    @PositiveOrZero(message = "预估老师数量不能为负数")
    @Schema(description = "预估老师数量", example = "1")
    private Integer estimateTeacherNum;

    /**
     * 批次开始时间
     */
    @NotNull(message = "批次开始时间不能为空")
    @Schema(description = "批次开始时间", example = "")
    private LocalDateTime batchStartTime;

    /**
     * 批次结束时间
     */
    @NotNull(message = "批次结束时间不能为空")
    @Schema(description = "批次结束时间", example = "")
    private LocalDateTime batchEndTime;
}
