package com.stirwick.hotelmanagement.dto.clas;
import lombok.Data;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;
@Data
public class StudentJoinClassDTO {
    /**
     * 目标班级id
     */
    @Schema(description = "班级id",  requiredMode = Schema.RequiredMode.REQUIRED, example = "")
    @NotNull(message = "目标班级id不能为空")
    private Long classId;
    /**
     * 学生id集合
     */
    @NotEmpty(message = "请选择至少一名学生")
    @Schema(description = "学生id集合",  requiredMode = Schema.RequiredMode.REQUIRED, example = "")
    private List<Long> studentIdList;
}
