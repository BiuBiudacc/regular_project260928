package com.stirwick.hotelmanagement.dto.bed;
import lombok.Data;
import io.swagger.v3.oas.annotations.Parameter;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import io.swagger.v3.oas.annotations.media.Schema;
@Data
public class BedAvailableQueryDTO {
    @Schema(description = "入住起始时间", example = "")
    private LocalDate checkInDate;
    @Schema(description = "入住结束时间", example = "")
    private LocalDate checkOutDate;
    @Schema(description = "性别", example = "学生性别 1男 2女")
    private Integer studentGender;
}
