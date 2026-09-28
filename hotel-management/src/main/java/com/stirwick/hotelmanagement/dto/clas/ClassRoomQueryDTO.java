package com.stirwick.hotelmanagement.dto.clas;
import com.stirwick.hotelmanagement.dto.BasePageDTO;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;
@Data
public class ClassRoomQueryDTO extends BasePageDTO {
    /**
     * 教室名称（模糊查询）
     */
    @Schema(description = "教室名称",  example = "")
    private String roomName;

    /**
     * 教室状态：0正常 1停用
     */
    @Schema(description = "教室状态",  example = "")
    private Integer roomStatus;
}
