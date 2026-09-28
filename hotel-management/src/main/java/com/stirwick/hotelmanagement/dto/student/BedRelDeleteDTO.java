package com.stirwick.hotelmanagement.dto.student;
import lombok.Data;
import javax.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;
@Data
public class BedRelDeleteDTO {
    @Schema(description = "入住记录id",requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "入住记录id不能为空")
    private Long id;
}
