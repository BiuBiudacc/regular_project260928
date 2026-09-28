package com.stirwick.hotelmanagement.dto.clas;
import lombok.Data;
import javax.validation.constraints.NotBlank;
import io.swagger.v3.oas.annotations.media.Schema;
@Data
public class ClassRoomTypeDTO {
    /**
     * id：新增不传，修改传
     */
    @Schema(description = "id", example = "1")
    private Long id;

    /**
     * 型号名称
     */
    @Schema(description = "型号名称", example = "1")
    @NotBlank(message = "型号名称不能为空")
    private String typeName;
}
