package com.stirwick.hotelmanagement.vo.student;
import lombok.Data;
import java.time.LocalDateTime;
import io.swagger.v3.oas.annotations.media.Schema;
@Data
public class StudentBedRecordVO {
    @Schema(description = "入住记录id", example = "1")
    private Long id;
    @Schema(description = "房间id", example = "1")
    private Long roomId;
    @Schema(description = "房间名称", example = "")
    private String roomName;
    @Schema(description = "床位id", example = "")
    private Long bedId;
    @Schema(description = "床位名称", example = "")
    private String bedName;
    @Schema(description = "入住日期", example = "")
    private LocalDateTime checkInDate;
    @Schema(description = "离开日期", example = "")
    private LocalDateTime estimateLeaveTime;
    @Schema(description = "实际离开日期", example = "")
    private LocalDateTime realLeaveTime;
    @Schema(description = "入住状态,正在入住/待入住/已结束", example = "")
    private String status;
}
