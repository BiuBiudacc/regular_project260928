package com.stirwick.hotelmanagement.dto.clas;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;
import com.stirwick.hotelmanagement.dto.clas.ClassQueryDTO;
@Data
public class ClassPageQuery {

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
    private ClassQueryDTO query;
}
