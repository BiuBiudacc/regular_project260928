package com.stirwick.hotelmanagement.vo.room;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;
@Data
public class ClassRoomTypeVO {
    @Schema(description = "教室型号id", example = "1")
    private Long id;
    @Schema(description = "教室名称", example = "1")
    private String typeName;
}
