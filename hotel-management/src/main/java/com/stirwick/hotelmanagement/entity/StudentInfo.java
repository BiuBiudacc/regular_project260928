package com.stirwick.hotelmanagement.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("student_info")
public class StudentInfo {
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 学生名称
     */
    private String studentName;
    /**
     * 身份证号码
     */
    private String idCard;
    /**
     * 学生性别 1男 2女
     */
    private Integer studentGender;
    /**
     * 学生年龄
     */
    private Integer studentAge;
    /**
     * 学生电话
     */
    private String studentPhone;
    /**
     * 紧急联系人姓名
     */
    private String emergencyContactName;
    /**
     * 紧急联系人电话
     */
    private String emergencyContactPhone;
    /**
     * 所属批次id
     */
    private Long batchId;
    /**
     * 所属班级id
     */
    private Long classId;

    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}

