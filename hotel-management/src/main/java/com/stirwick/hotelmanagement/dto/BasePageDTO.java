package com.stirwick.hotelmanagement.dto;

import lombok.Data;
import javax.validation.constraints.Min;
import io.swagger.v3.oas.annotations.media.Schema;
/**
 * 通用分页入参DTO，所有分页查询直接继承
 */
@Data
public class BasePageDTO {

    /**
     * 当前页码，最小为1
     */
    @Min(value = 1, message = "页码不能小于1")
    @Schema(description = "页码", example = "")
    private Long pageNum = 1L;

    /**
     * 每页条数，最小为1
     */
    @Schema(description = "每页条数",  example = "")
    @Min(value = 1, message = "每页条数不能小于1")
    private Long pageSize = 10L;
}
