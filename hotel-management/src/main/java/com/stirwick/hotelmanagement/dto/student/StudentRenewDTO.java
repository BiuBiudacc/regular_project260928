package com.stirwick.hotelmanagement.dto.student;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;
@Data

public class StudentRenewDTO {
    @NotNull(message = "学生ID不能为空")
    @Schema(description = "学生id", example = "1")
    private Long studentId;

    @NotNull(message = "床位入住关联id不能为空")
    @Schema(description = "床位学生关联主键id", example = "10")
    private Long bedRelId;

    @NotNull(message = "床位id不能为空")
    @Schema(description = "床位id", example = "5")
    private Long bedId;

    @NotNull(message = "原预计离开日期不能为空")
    @Schema(description = "原预计离开日期（用于乐观锁校验）")
    private LocalDateTime oldEstimateLeaveDate;

    @NotNull(message = "续住后的预计离开日期不能为空")
    @Schema(description = "续住新的预计离开日期")
    private LocalDateTime newEstimateLeaveDate;
}
