package com.stirwick.hotelmanagement.dto.student;
import lombok.Data;
import javax.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;
@Data
public class StudentBedRecordQueryDTO {
    @NotNull(message = "学生id不能为空")
    @Schema(description = "学生id",requiredMode = Schema.RequiredMode.REQUIRED)
    private Long studentId;
}
