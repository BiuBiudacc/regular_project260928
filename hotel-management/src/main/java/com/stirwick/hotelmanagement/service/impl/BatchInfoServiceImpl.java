package com.stirwick.hotelmanagement.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.Query;
import com.stirwick.hotelmanagement.dto.batch.BatchQueryDTO;
import com.stirwick.hotelmanagement.vo.batch.BatchSimpleVO;
import com.stirwick.hotelmanagement.vo.page.PageVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.stirwick.hotelmanagement.entity.BatchInfo;
import com.stirwick.hotelmanagement.mapper.BatchInfoMapper;
import com.stirwick.hotelmanagement.service.BatchInfoService;
import com.stirwick.hotelmanagement.vo.batch.BatchVO;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import com.stirwick.hotelmanagement.dto.batch.BatchSaveDTO;
import com.stirwick.hotelmanagement.dto.batch.BatchUpdateDTO;
import com.stirwick.hotelmanagement.exception.BusinessException;
import com.stirwick.hotelmanagement.enums.ResultCodeEnum;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.util.StringUtils;
import java.time.LocalDateTime;
@Service
public class BatchInfoServiceImpl extends ServiceImpl<BatchInfoMapper, BatchInfo> implements BatchInfoService {


    @Override
    public PageVO<BatchVO> pageBatch(Long pageNum, Long pageSize) {
        // 构造分页对象
        Page<BatchInfo> pageEntity = new Page<>(pageNum, pageSize);
        // 无条件分页查询，后续可以在这里加条件构造器做模糊查询
        Page<BatchInfo> pageResult = this.page(pageEntity);

        // entity 转 vo
        List<BatchVO> voList = pageResult.getRecords().stream().map(entity -> {
            BatchVO vo = new BatchVO();
            vo.setBatchId(entity.getBatchId());
            vo.setBatchName(entity.getBatchName());
            vo.setBatchDesc(entity.getBatchDesc());
            vo.setEstimateStudentNum(entity.getEstimateStudentNum());
            vo.setEstimateTeacherNum(entity.getEstimateTeacherNum());
            vo.setChargeTeacher(entity.getChargeTeacher());
            vo.setChargePhone(entity.getChargePhone());
            vo.setBatchStartTime(entity.getBatchStartTime());
            vo.setBatchEndTime(entity.getBatchEndTime());
            vo.setCreateTime(entity.getCreateTime());
            vo.setUpdateTime(entity.getUpdateTime());
            return vo;
        }).collect(Collectors.toList());

        // 组装返回分页VO对象
        PageVO<BatchVO> result = PageVO.convert(pageResult, voList);
        return result;
    }

    @Override
    public Long createBatch(BatchSaveDTO dto) {
        // =========业务校验=========
        // 结束时间不能早于等于开始时间
        if (dto.getBatchEndTime().isBefore(dto.getBatchStartTime()) || dto.getBatchEndTime().isEqual(dto.getBatchStartTime())) {
            throw new BusinessException(ResultCodeEnum.BATCHINFO_CREATE_ERROR.getCode(),ResultCodeEnum.BATCHINFO_CREATE_ERROR.getMsg() );
        }

        // DTO转PO实体
        BatchInfo batchInfo = new BatchInfo();
        batchInfo.setBatchName(dto.getBatchName());
        batchInfo.setBatchDesc(dto.getBatchDesc());
        batchInfo.setEstimateStudentNum(dto.getEstimateStudentNum());
        batchInfo.setEstimateTeacherNum(dto.getEstimateTeacherNum());
        batchInfo.setBatchStartTime(dto.getBatchStartTime());
        batchInfo.setBatchEndTime(dto.getBatchEndTime());

        // 保存数据库；createTime、updateTime由MetaObjectHandler自动填充，不需要手动set
        boolean save = this.save(batchInfo);
        if (!save) {
            throw new BusinessException(5002, "批次创建失败，请重试");
        }
        // 返回数据库自增主键
        return batchInfo.getBatchId();
    }

    @Override
    public void updateBatch(BatchUpdateDTO dto) {
        // 1.校验数据库是否存在该批次
        BatchInfo existBatch = this.getById(dto.getBatchId());
        if (existBatch == null) {
            throw new BusinessException(5003, "该批次数据不存在");
        }

        //2.业务时间校验：结束时间必须晚于开始时间
        if (dto.getBatchEndTime().isBefore(dto.getBatchStartTime()) || dto.getBatchEndTime().isEqual(dto.getBatchStartTime())) {
            throw new BusinessException(5001, "批次结束时间必须晚于开始时间");
        }

        //3.DTO转PO
        BatchInfo batchInfo = new BatchInfo();
        batchInfo.setBatchId(dto.getBatchId());
        batchInfo.setBatchName(dto.getBatchName());
        batchInfo.setBatchDesc(dto.getBatchDesc());
        batchInfo.setEstimateStudentNum(dto.getEstimateStudentNum());
        batchInfo.setEstimateTeacherNum(dto.getEstimateTeacherNum());
        batchInfo.setChargeTeacher(dto.getChargeTeacher());
        batchInfo.setChargePhone(dto.getChargePhone());
        batchInfo.setBatchStartTime(dto.getBatchStartTime());
        batchInfo.setBatchEndTime(dto.getBatchEndTime());

        // updateById：根据主键更新；只会更新你set过的字段；MetaObjectHandler自动填充updateTime
        boolean success = this.updateById(batchInfo);
        if (!success) {
            throw new BusinessException(5004, "批次修改失败");
        }
    }

    @Override
    public void deleteBatch(Long batchId) {
        // 1.判断批次本身是否存在
        BatchInfo batchInfo = this.getById(batchId);
        if (batchInfo == null) {
            throw new BusinessException(5003, "该批次数据不存在");
        }

        // =====================【后续关联表开发时打开下面代码】=====================
        // 示例：后续有学生表、床位分配表持有batch_id外键时
        // long studentCount = studentMapper.countByBatchId(batchId);
        // long bedAssignCount = bedAssignMapper.countByBatchId(batchId);
        // if(studentCount > 0 || bedAssignCount > 0){
        //     throw new BusinessException(5007,"该批次下存在关联业务数据，请先删除关联数据后再删除批次");
        // }
        // ======================================================================

        // 无关联数据，执行物理删除
        boolean removeOk = this.removeById(batchId);
        if (!removeOk) {
            throw new BusinessException(5008, "批次删除失败，请重试");
        }
    }

    @Override
    public PageVO<BatchVO> pageBatchAdvanced(Long pageNum, Long pageSize, BatchQueryDTO queryDTO) {
        LambdaQueryWrapper<BatchInfo> wrapper = Wrappers.lambdaQuery();
        //判断批次序号是否存在,如果存在就拼接条件
        if(queryDTO.getBatchId()!=null){
            wrapper.eq(BatchInfo::getBatchId,queryDTO.getBatchId());
        }
        //判断批次信息是否存在,如果存在就拼接条件
        if(StringUtils.hasText(queryDTO.getBatchInfo())){
            wrapper.and(w->w.like(BatchInfo::getBatchName,queryDTO.getBatchInfo())
                    .or()
                    .like(BatchInfo::getBatchDesc,queryDTO.getBatchInfo()));
        }
        //判断是否存在主管老师信息
        //主管老师名称 等于
        if (StringUtils.hasText(queryDTO.getChargeTeacher())) {
            wrapper.like(BatchInfo::getChargeTeacher, queryDTO.getChargeTeacher());
        }

        LocalDate qStart = queryDTO.getQueryStartTime();
        LocalDate qEnd = queryDTO.getQueryEndTime();
        if (qStart != null || qEnd != null) {
            wrapper.and(w -> {
                if(qStart != null){
                    w.ge(BatchInfo::getBatchEndTime, qStart.atStartOfDay());
                }
                if(qEnd != null){
                    w.le(BatchInfo::getBatchStartTime, qEnd.atTime(23,59,59));
                }
            });
        }
        //执行分页查询;
        Page<BatchInfo> pageResult = this.page(new Page<>(pageNum,pageSize),wrapper);
        List<BatchVO> voList =  pageResult.getRecords().stream().map(entity->{
            BatchVO vo = new BatchVO();
            vo.setBatchId(entity.getBatchId());
            vo.setBatchName(entity.getBatchName());
            vo.setBatchDesc(entity.getBatchDesc());
            vo.setEstimateStudentNum(entity.getEstimateStudentNum());
            vo.setEstimateTeacherNum(entity.getEstimateTeacherNum());
            vo.setChargeTeacher(entity.getChargeTeacher());
            vo.setChargePhone(entity.getChargePhone());
            vo.setBatchStartTime(entity.getBatchStartTime());
            vo.setBatchEndTime(entity.getBatchEndTime());
            vo.setCreateTime(entity.getCreateTime());
            vo.setUpdateTime(entity.getUpdateTime());
            return vo;
        }).collect(Collectors.toList());
        // 组装返回分页VO对象
        PageVO<BatchVO> result = PageVO.convert(pageResult, voList);
        return result;

    }

    @Override
    public List<BatchSimpleVO> listAllBatchSimple() {
        LambdaQueryWrapper<BatchInfo> wrapper = new LambdaQueryWrapper<>();
        // 查询需要的字段
        wrapper.select(BatchInfo::getBatchId, BatchInfo::getBatchName,
                BatchInfo::getBatchStartTime, BatchInfo::getBatchEndTime);
        // 创建时间倒序，新批次放前面
        wrapper.orderByDesc(BatchInfo::getCreateTime);
        List<BatchInfo> batchList = this.list(wrapper);

        // entity转vo
        return batchList.stream().map(entity -> {
            BatchSimpleVO vo = new BatchSimpleVO();
            vo.setBatchId(entity.getBatchId());
            vo.setBatchName(entity.getBatchName());
            vo.setBatchStartTime(entity.getBatchStartTime());
            vo.setBatchEndTime(entity.getBatchEndTime());
            return vo;
        }).collect(Collectors.toList());
    }

}
