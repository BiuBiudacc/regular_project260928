package com.stirwick.hotelmanagement.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("bed")
public class Bed {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String bedName;
    private Integer bedStatus;
    private String bedDesc;
    private Long roomId;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}

