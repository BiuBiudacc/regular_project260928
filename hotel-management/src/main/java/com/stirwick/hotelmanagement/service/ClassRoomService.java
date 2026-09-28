package com.stirwick.hotelmanagement.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.stirwick.hotelmanagement.dto.clas.ClassRoomAddDTO;
import com.stirwick.hotelmanagement.dto.clas.ClassRoomQueryDTO;
import com.stirwick.hotelmanagement.dto.clas.ClassRoomUpdateDTO;
import com.stirwick.hotelmanagement.entity.ClassRoom;
import com.stirwick.hotelmanagement.vo.page.PageVO;
import com.stirwick.hotelmanagement.vo.room.ClassRoomSelectVO;
import com.stirwick.hotelmanagement.vo.room.ClassRoomVO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public interface ClassRoomService extends IService<ClassRoom> {
    /**
     * 获取全部可用教室，用于班级新增下拉选择
     */
    List<ClassRoomSelectVO> getAvailableRoomList();
    PageVO<ClassRoomVO> pageQuery(ClassRoomQueryDTO dto);
    void addClassRoom(ClassRoomAddDTO dto);
    void updateClassRoom(ClassRoomUpdateDTO dto);
    void deleteClassRoom(Long id);
}
