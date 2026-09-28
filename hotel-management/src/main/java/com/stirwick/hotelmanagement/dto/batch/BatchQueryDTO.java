package com.stirwick.hotelmanagement.dto.batch;
import lombok.Data;
import java.time.LocalDate;
import io.swagger.v3.oas.annotations.media.Schema;
@Data
public class BatchQueryDTO {
    /**
     * 批次id 真实batchId，等于查询
     */
    @Schema(description = "批次id",  example = "批次id,根据批次id找对应的批次数据")
    private Long batchId;

    /**
     * 批次信息，模糊匹配名称/描述
     */
    @Schema(description = "批次信息",  example = "基于批次信息和批次名称,批次描述做模糊查询")
    private String batchInfo;

    /**
     * 主管老师名称，等于
     */
    @Schema(description = "主管老师名称",  example = "根据主管老师名称进行模糊查询")
    private String chargeTeacher;

    /**
     * 查询时间范围-开始
     */
    @Schema(description = "批次开始时间",  example = "")
    private LocalDate queryStartTime;

    /**
     * 查询时间范围-结束
     */
    @Schema(description = "批次结束时间",  example = "")
    private LocalDate queryEndTime;

}
