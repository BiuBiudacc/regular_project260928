package com.stirwick.hotelmanagement.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("batch_info")
public class BatchInfo {

    @TableId(type = IdType.AUTO)
    private Long batchId;

    private String batchName;

    private String batchDesc;

    private Integer estimateStudentNum;

    private Integer estimateTeacherNum;

    private String chargeTeacher;

    private String chargePhone;

    private LocalDateTime batchStartTime;

    private LocalDateTime batchEndTime;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
