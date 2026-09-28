package com.stirwick.hotelmanagement.vo.room;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;
@Data
public class ClassRoomSelectVO {
    /**
     * 教室id
     */
    @Schema(description = "教室id", example = "1")
    private Long id;
    /**
     * 教室名称
     */
    @Schema(description = "教室名称", example = "1")
    private String roomName;
}
