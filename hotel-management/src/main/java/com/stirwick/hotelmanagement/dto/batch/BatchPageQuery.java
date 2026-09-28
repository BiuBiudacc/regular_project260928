package com.stirwick.hotelmanagement.dto.batch;
import com.stirwick.hotelmanagement.dto.batch.BatchQueryDTO;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;
@Data
public class BatchPageQuery {
    /**
     * 分页页码
     */
    @Schema(description = "页码",  example = "")
    private Long pageNum = 1L;
    /**
     * 每页条数
     */
    @Schema(description = "每页数量",  example = "")
    private Long pageSize = 10L;

    /**
     * 查询过滤条件
     */
    private BatchQueryDTO query;
}
