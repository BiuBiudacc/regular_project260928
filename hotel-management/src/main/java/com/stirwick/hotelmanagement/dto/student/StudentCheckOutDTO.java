package com.stirwick.hotelmanagement.dto.student;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.util.List;
@Data
@Schema(description = "批量退房DTO")
public class StudentCheckOutDTO {

    @NotEmpty(message = "学生id列表不能为空")
    @Schema(description = "学生id集合")
    private List<Long> studentIdList;
    @Schema(description = "退房日期，不传默认当天")
    private LocalDate checkOutDate;
}