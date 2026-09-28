package com.stirwick.hotelmanagement.dto.clas;
import com.stirwick.hotelmanagement.dto.BasePageDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ClassRoomTypePageDTO extends BasePageDTO {
    @Schema(description = "教室型号名称", example = "1")
    private String typeName;
}
