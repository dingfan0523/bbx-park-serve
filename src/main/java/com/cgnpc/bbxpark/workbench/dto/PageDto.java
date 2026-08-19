package com.cgnpc.bbxpark.workbench.dto;

import lombok.Data;

/******************************
 * 用途说明: 流程表单新增用
 * 作者姓名: P629988
 * 创建时间: 2022/8/4
 ******************************/
@Data
public class PageDto {
    /**
     * 分页
     */
    private int pageIndex;
    /**
     * 分页大小
     */
    private int pageSize;

}
