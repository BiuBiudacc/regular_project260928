package com.stirwick.hotelmanagement.dto.student;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
@Data
@Schema(description = "批量学生入住DTO")
public class StudentBedBatchCheckInDTO {
    @Schema(description = "学生床位分配列表")
    @NotEmpty(message = "入住条目列表不能为空，请选择学生和床位")
    private List<StudentBedItemDTO> itemList;

    @NotNull(message = "入住时间不能为空")
    @Schema(description = "入住日期", requiredMode = Schema.RequiredMode.REQUIRED,example = "2026-09-11")
    private LocalDateTime checkInDate;
    @NotNull(message = "预估退宿时间不能为空")
    @Schema(description = "预估退宿日期",requiredMode = Schema.RequiredMode.REQUIRED,example = "2026-09-30")
    private LocalDateTime estimateCheckOutDate;
}
