package com.stirwick.hotelmanagement.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("class_room")
public class ClassRoom {
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 教室名称
     */
    private String roomName;
    /**
     * 教室地址
     */
    private String roomAddress;
    /**
     * 教室型号id
     */
    private Long roomTypeId;
    /**
     * 教室状态：1正常，2停用
     */
    private Integer roomStatus;

    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}

