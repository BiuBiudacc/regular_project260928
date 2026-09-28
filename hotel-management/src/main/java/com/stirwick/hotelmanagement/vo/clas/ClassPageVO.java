package com.stirwick.hotelmanagement.vo.clas;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ClassPageVO {
    /**
     * 班级id
     */
    @Schema(description = "班级id", example ="1")
    private Long id;

    /**
     * 班级名称
     */
    @Schema(description = "班级名称", example ="1")
    private String className;

    /**
     * 班级描述
     */
    @Schema(description = "班级描述", example ="1")
    private String classDesc;

    /**
     * 班级管理老师名称
     */
    @Schema(description = "班级老师名称", example ="1")
    private String manageTeacherName;

    /**
     * 老师联系电话
     */
    @Schema(description = "班级老师电话", example ="1")
    private String teacherPhone;

    /**
     * 班级状态：1待开班，2授课中，3已结束
     */
    @Schema(description = "班级状态", example ="1待开班，2授课中，3已结束")
    private Integer classStatus;

    /**
     * 教室id
     */
    @Schema(description = "教室id", example ="1")
    private Long classRoomId;

    /**
     * 【关联查询】教室名称
     */
    @Schema(description = "教室名称", example ="1")
    private String roomName;

    /**
     * 【统计】班级下学生数量
     */
    @Schema(description = "班级下学生数量", example ="1")
    private Long studentCount;
}

