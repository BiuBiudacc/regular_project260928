package com.stirwick.hotelmanagement.service;
import com.stirwick.hotelmanagement.dto.batch.BatchQueryDTO;
import com.stirwick.hotelmanagement.vo.batch.BatchSimpleVO;
import com.stirwick.hotelmanagement.vo.page.PageVO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.stirwick.hotelmanagement.entity.BatchInfo;
import com.stirwick.hotelmanagement.vo.batch.BatchVO;
import com.stirwick.hotelmanagement.dto.batch.BatchSaveDTO;
import com.stirwick.hotelmanagement.dto.batch.BatchUpdateDTO;

import java.util.List;

public interface BatchInfoService extends IService<BatchInfo> {

    /**
     * 批次分页查询
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @return Page<BatchVO>
     */
    PageVO<BatchVO> pageBatch(Long pageNum, Long pageSize);
    Long createBatch(BatchSaveDTO dto);
    void updateBatch(BatchUpdateDTO dto);
    void deleteBatch(Long batchId);//批次物理删除
    PageVO<BatchVO> pageBatchAdvanced(Long pageNum, Long pageSize, BatchQueryDTO queryDTO);
    List<BatchSimpleVO> listAllBatchSimple();
}
