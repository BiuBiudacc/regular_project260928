package com.stirwick.hotelmanagement.vo.student;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;
@Data
public class StudentCurrentBedVO {
    @Schema(description = "入住记录id")
    private Long id;
    @Schema(description = "房间id")
    private Long roomId;
    @Schema(description = "房间名称")
    private String roomName;
    @Schema(description = "床位id")
    private Long bedId;
    @Schema(description = "床位名称")
    private String bedName;
    @Schema(description = "入住时间")
    private LocalDateTime checkInDate;
    @Schema(description = "预计退房时间")
    private LocalDateTime estimateLeaveTime;
}
