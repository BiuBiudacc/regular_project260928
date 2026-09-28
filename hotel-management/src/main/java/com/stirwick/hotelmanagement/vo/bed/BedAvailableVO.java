package com.stirwick.hotelmanagement.vo.bed;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;
@Data
public class BedAvailableVO {
    //房间信息
    private Long roomId;
    private String roomName;
    private String roomDesc;
    private String typeName; //房间型号名称

    //床位信息
    private Long bedId;
    private String bedName;
    private String bedStatus; //使用中 / 空闲
    private String studentName; //入住人员名称
}
