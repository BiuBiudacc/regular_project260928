package com.stirwick.hotelmanagement.dto.clas;
import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;
@Data
public class ClassRoomAddDTO {
    /**
     * 教室名称
     */
    @NotBlank(message = "教室名称不能为空")
    @Schema(description = "教室名称",  requiredMode = Schema.RequiredMode.REQUIRED, example = "")
    private String roomName;

    /**
     * 教室地址
     */
    @Schema(description = "教室地址", example = "")
    private String roomAddress;

    /**
     * 教室类型id（关联class_room_type）
     */
    @NotNull(message = "请选择教室类型")
    private Long roomTypeId;

    /**
     * 教室状态 0正常，1停用
     */
    @NotNull(message = "教室状态不能为空")
    private Integer roomStatus;
}
