package com.stirwick.hotelmanagement.vo.room;

import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;
@Data
public class ClassRoomVO {
    /**
     * 教室id
     */
    @Schema(description = "教室id", example = "1")
    private Long roomId;
    /**
     * 教室名称
     */
    @Schema(description = "教室名称", example = "1")
    private String roomName;
    /**
     * 教室地址
     */
    @Schema(description = "教室地址", example = "1")
    private String roomAddress;
    /**
     * 教室型号名称（关联教室型号码表）
     */
    @Schema(description = "型号名称", example = "1")
    private String modelName;
    /**
     * 教室状态 0正常 1停用
     */
    @Schema(description = "教室状态", example = "1")
    private Integer roomStatus;
}
