package com.stirwick.hotelmanagement.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.stirwick.hotelmanagement.dto.clas.ClassRoomTypePageDTO;
import com.stirwick.hotelmanagement.entity.ClassRoomType;
import com.stirwick.hotelmanagement.vo.page.PageVO;
import com.stirwick.hotelmanagement.vo.room.ClassRoomTypeVO;

public interface ClassRoomTypeService extends IService<ClassRoomType> {
    public PageVO<ClassRoomTypeVO> pageQuery(ClassRoomTypePageDTO classRoomTypePageDTO);
}
