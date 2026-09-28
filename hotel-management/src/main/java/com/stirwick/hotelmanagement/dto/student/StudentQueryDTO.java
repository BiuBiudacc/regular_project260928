package com.stirwick.hotelmanagement.dto.student;
import com.stirwick.hotelmanagement.dto.BasePageDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
@Data
public class StudentQueryDTO extends BasePageDTO {
    /**
     * 学生名称 模糊
     */
    @Schema(description = "学生名称", example = "")
    private String studentName;
    /**
     * 批次id筛选
     */
    @Schema(description = "批次id", example = "")
    private Long batchId;
    /**
     * 入住状态：入住中 / 未入住
     */
    @Schema(description = "入住状态", example = "字符串,已入住,未入住")
    private String checkInStatus;
}
