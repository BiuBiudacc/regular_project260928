package com.stirwick.hotelmanagement.vo.batch;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class BatchSimpleVO {
    /**
     * 批次id
     */
    @Schema(description = "批次id", example ="1")
    private Long batchId;
    /**
     * 批次名称
     */
    @Schema(description = "批次名称", example ="1")
    private String batchName;
    /**
     * 批次开始时间
     */
    @Schema(description = "批次开始时间", example ="1")
    private LocalDateTime batchStartTime;
    /**
     * 批次结束时间
     */
    @Schema(description = "批次结束时间", example ="1")
    private LocalDateTime batchEndTime;
}
