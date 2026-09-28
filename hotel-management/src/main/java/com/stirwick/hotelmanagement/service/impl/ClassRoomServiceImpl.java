package com.stirwick.hotelmanagement.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.stirwick.hotelmanagement.dto.clas.ClassRoomAddDTO;
import com.stirwick.hotelmanagement.dto.clas.ClassRoomQueryDTO;
import com.stirwick.hotelmanagement.dto.clas.ClassRoomUpdateDTO;
import com.stirwick.hotelmanagement.entity.ClassInfo;
import com.stirwick.hotelmanagement.entity.ClassRoom;
import com.stirwick.hotelmanagement.entity.ClassRoomType;
import com.stirwick.hotelmanagement.exception.BusinessException;
import com.stirwick.hotelmanagement.mapper.ClassInfoMapper;
import com.stirwick.hotelmanagement.mapper.ClassRoomMapper;
import com.stirwick.hotelmanagement.mapper.ClassRoomTypeMapper;
import com.stirwick.hotelmanagement.service.ClassRoomService;
import com.stirwick.hotelmanagement.vo.page.PageVO;
import com.stirwick.hotelmanagement.vo.room.ClassRoomSelectVO;
import com.stirwick.hotelmanagement.vo.room.ClassRoomVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
@Service
public class ClassRoomServiceImpl extends ServiceImpl<ClassRoomMapper,ClassRoom>implements ClassRoomService{
    @Autowired
    private ClassRoomMapper classRoomMapper;
    @Autowired
    private ClassRoomTypeMapper classRoomTypeMapper;
    @Autowired
    private ClassInfoMapper classInfoMapper;
    @Override
    public List<ClassRoomSelectVO> getAvailableRoomList(){
        LambdaQueryWrapper<ClassRoom> wrapper = new LambdaQueryWrapper<>();
        // 只查询状态正常的教室：1正常，2停用
        wrapper.eq(ClassRoom::getRoomStatus, 1);
        // 按教室id升序
        wrapper.orderByAsc(ClassRoom::getId);
        List<ClassRoom> roomList = classRoomMapper.selectList(wrapper);
        return roomList.stream().map(entity -> {
            ClassRoomSelectVO vo = new ClassRoomSelectVO();
            BeanUtils.copyProperties(entity, vo);
            return vo;
        }).collect(Collectors.toList());
    }

    public PageVO<ClassRoomVO> pageQuery(ClassRoomQueryDTO dto){
        //1.构建分页对象，直接从dto拿分页参数
        Page<ClassRoom> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        LambdaQueryWrapper<ClassRoom> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(dto.getRoomName() != null, ClassRoom::getRoomName, dto.getRoomName());
        wrapper.eq(dto.getRoomStatus() != null, ClassRoom::getRoomStatus, dto.getRoomStatus());
        wrapper.orderByDesc(ClassRoom::getId);
        Page<ClassRoom> entityPage = this.page(page, wrapper);
        List<ClassRoom> roomList = entityPage.getRecords();

        //2.批量查询教室型号码表，做内存关联，避免循环查库
        List<Long> modelIdList = roomList.stream()
                .map(ClassRoom::getRoomTypeId)
                .distinct()
                .collect(Collectors.toList());

        Map<Long,String> modelIdToNameMap = classRoomTypeMapper.selectBatchIds(modelIdList).stream()
                .collect(Collectors.toMap(ClassRoomType::getId, ClassRoomType::getTypeName));

        //3.entity转VO，填充型号名称
        List<ClassRoomVO> voList = roomList.stream().map(entity -> {
            ClassRoomVO vo = new ClassRoomVO();
            BeanUtils.copyProperties(entity, vo);
            vo.setRoomId(entity.getId());
            vo.setModelName(modelIdToNameMap.get(entity.getRoomTypeId()));
            return vo;
        }).collect(Collectors.toList());
        PageVO<ClassRoomVO> result = PageVO.convert(entityPage,voList);
        return result;
    }

    public void addClassRoom(ClassRoomAddDTO dto){
        //1.校验教室类型是否存在
        ClassRoomType type = classRoomTypeMapper.selectById(dto.getRoomTypeId());
        if (type == null) {
            throw new BusinessException(10001, "所选教室类型不存在");
        }
        //2.校验教室名称不能重复
        LambdaQueryWrapper<ClassRoom> nameWrapper = new LambdaQueryWrapper<>();
        nameWrapper.eq(ClassRoom::getRoomName, dto.getRoomName());
        long count = this.count(nameWrapper);
        if (count > 0) {
            throw new BusinessException(10002, "该教室名称已存在");
        }
        //3.dto转实体保存
        ClassRoom classRoom = new ClassRoom();
        BeanUtils.copyProperties(dto, classRoom);
        this.save(classRoom);
    }

    @Override
    public void updateClassRoom(ClassRoomUpdateDTO dto) {
        // 1. 判断教室是否存在
        ClassRoom oldRoom = this.getById(dto.getId());
        if(oldRoom == null){
            throw new BusinessException(10003,"教室不存在");
        }
        // 2. 判断所选教室类型存在
        ClassRoomType type = classRoomTypeMapper.selectById(dto.getRoomTypeId());
        if (type == null) {
            throw new BusinessException(10001, "所选教室类型不存在");
        }
        // 3. 校验名称不能重复，排除当前这条自己
        LambdaQueryWrapper<ClassRoom> nameWrapper = new LambdaQueryWrapper<>();
        nameWrapper.eq(ClassRoom::getRoomName, dto.getRoomName())
                .ne(ClassRoom::getId, dto.getId());
        long count = this.count(nameWrapper);
        if(count > 0){
            throw new BusinessException(10002,"该教室名称已被其他教室占用");
        }
        //4. dto转实体，更新
        ClassRoom classRoom = new ClassRoom();
        BeanUtils.copyProperties(dto, classRoom);
        this.updateById(classRoom);
    }

    @Override
    public void deleteClassRoom(Long id) {
        //1. 判断教室是否存在
        ClassRoom room = this.getById(id);
        if(room == null){
            throw new BusinessException(10003,"教室不存在");
        }

        long bindCount = classInfoMapper.selectCount(Wrappers.<ClassInfo>lambdaQuery()
                .eq(ClassInfo::getId, id));
        if(bindCount >0){
             throw new BusinessException(10004,"该教室已被班级绑定，禁止删除");
         }

        this.removeById(id);
    }

}
