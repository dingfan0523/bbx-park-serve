package com.cgnpc.bbxpark.workbench.dto;

import lombok.Data;

/******************************
 * 用途说明: 流程表单新增用
 * 作者姓名: P629988
 * 创建时间: 2022/8/4
 ******************************/
@Data
public class ConditionDto {

    /**
     * 表达号
     */
    private String expression;
    /**
     * 值
     */
    private String value;

}
