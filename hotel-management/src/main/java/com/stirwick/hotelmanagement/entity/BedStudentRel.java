package com.stirwick.hotelmanagement.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("bed_student_rel")
public class BedStudentRel {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long studentId;
    private Long bedId;
    private LocalDateTime checkInTime;
    /**
     * 预估退宿时间（入住时填写）
     */
    private LocalDateTime estimateCheckOutTime;
    /**
     * 实际退宿时间，null=正在入住
     */
    private LocalDateTime checkOutTime;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}

