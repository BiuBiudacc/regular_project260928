package com.stirwick.hotelmanagement.entity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;
@Data
@TableName("class_info")
public class ClassInfo {
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 班级名称
     */
    private String className;
    /**
     * 班级描述
     */
    private String classDesc;
    /**
     * 班级管理老师名称
     */
    private String manageTeacherName;
    /**
     * 老师联系电话
     */
    private String teacherPhone;
    /**
     * 班级状态：1待开班，2授课中，3已结束
     */
    private Integer classStatus;
    /**
     * 班级所属教室id
     */
    private Long classRoomId;

    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
