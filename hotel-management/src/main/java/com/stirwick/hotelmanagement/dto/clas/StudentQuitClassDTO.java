package com.stirwick.hotelmanagement.dto.clas;

import lombok.Data;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;
@Data
public class StudentQuitClassDTO {
    /**
     * 需要退出班级的学生id集合
     */
    @Schema(description = "学生id集合",  requiredMode = Schema.RequiredMode.REQUIRED, example = "")
    @NotEmpty(message = "请选择至少一名学生")
    private List<Long> studentIdList;
}
