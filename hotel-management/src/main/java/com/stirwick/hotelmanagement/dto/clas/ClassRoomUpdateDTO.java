package com.stirwick.hotelmanagement.dto.clas;

import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;
@Data
public class ClassRoomUpdateDTO {
    /**
     * 教室主键id
     */
    @NotNull(message = "教室id不能为空")
    private Long id;

    /**
     * 教室名称
     */
    @NotBlank(message = "教室名称不能为空")
    private String roomName;

    /**
     * 教室地址
     */
    private String roomAddress;

    /**
     * 教室类型id
     */
    @NotNull(message = "请选择教室类型")
    private Long roomTypeId;

    /**
     * 教室状态 0正常，1停用
     */
    @NotNull(message = "教室状态不能为空")
    private Integer roomStatus;
}

