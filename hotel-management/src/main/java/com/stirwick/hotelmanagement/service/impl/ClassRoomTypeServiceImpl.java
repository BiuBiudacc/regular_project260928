package com.stirwick.hotelmanagement.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.stirwick.hotelmanagement.dto.clas.ClassRoomTypePageDTO;
import com.stirwick.hotelmanagement.entity.ClassRoomType;
import com.stirwick.hotelmanagement.mapper.ClassRoomTypeMapper;
import com.stirwick.hotelmanagement.service.ClassRoomTypeService;
import com.stirwick.hotelmanagement.vo.page.PageVO;
import com.stirwick.hotelmanagement.vo.room.ClassRoomTypeVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClassRoomTypeServiceImpl extends ServiceImpl<ClassRoomTypeMapper, ClassRoomType> implements ClassRoomTypeService {
    public PageVO<ClassRoomTypeVO> pageQuery(ClassRoomTypePageDTO classRoomTypePageDTO){
        Page<ClassRoomType> page = new Page<>(classRoomTypePageDTO.getPageNum(), classRoomTypePageDTO.getPageSize());

        LambdaQueryWrapper<ClassRoomType> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(classRoomTypePageDTO.getTypeName() != null, ClassRoomType::getTypeName, classRoomTypePageDTO.getTypeName());
        wrapper.orderByDesc(ClassRoomType::getId);

        Page<ClassRoomType> entityPage = this.page(page, wrapper);
        List<ClassRoomType> records = entityPage.getRecords();

        List<ClassRoomTypeVO> voList = records.stream().map(entity -> {
            ClassRoomTypeVO vo = new ClassRoomTypeVO();
            vo.setId(entity.getId());
            BeanUtils.copyProperties(entity, vo);
            return vo;
        }).collect(Collectors.toList());

        PageVO<ClassRoomTypeVO> voPage = PageVO.convert(entityPage,voList);
        return voPage;
    }
}
