package com.stirwick.hotelmanagement.vo.page;
import lombok.Data;
import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Schema(description = "通用分页返回结构体")
public class PageVO<T>{
    @Schema(description = "当前页码", example = "1")
    private long current;

    @Schema(description = "每页条数", example = "10")
    private long size;

    @Schema(description = "总记录数", example = "56")
    private long total;

    @Schema(description = "总页数", example = "6")
    private long pages;

    @Schema(description = "当前页数据列表")
    private List<T> records;
    public static <T> PageVO<T> convert(com.baomidou.mybatisplus.core.metadata.IPage<?> page, List<T> recordsVO) {
        PageVO<T> vo = new PageVO<>();
        vo.setCurrent(page.getCurrent());
        vo.setSize(page.getSize());
        vo.setTotal(page.getTotal());
        vo.setPages(page.getPages());
        vo.setRecords(recordsVO);
        return vo;
    }
}
