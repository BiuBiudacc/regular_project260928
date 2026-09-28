package com.stirwick.hotelmanagement.dto.student;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotNull;

@Data
@Schema(description = "单条学生床位分配项")
public class StudentBedItemDTO {
    @Schema(description = "学生id",requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "学生id不能为空")
    private Long studentId;
    @Schema(description = "床位id",requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "床位id不能为空")
    private Long bedId;
}
