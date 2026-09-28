package com.stirwick.hotelmanagement.dto.student;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
@Data
public class StudentCurrentBedQueryDTO {
    @NotNull(message = "学生id不能为空")
    @Schema(description = "学生id")
    private Long studentId;
}
