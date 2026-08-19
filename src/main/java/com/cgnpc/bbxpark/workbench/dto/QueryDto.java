package com.cgnpc.bbxpark.workbench.dto;

import com.cgnpc.cud.workflow2.base.model.WfRequestDto;
import lombok.Data;

import java.util.Map;

/******************************
 * 用途说明: 业务表单数据查询所用
 * 作者姓名: P629988
 * 创建时间: 2022/8/5
 ******************************/
@Data
public class QueryDto extends WfRequestDto {
    /**
     * 表单id
     */
    private String formId;
    /**
     * 查询条件
     */
    private Map<String, ConditionDto> conditionDto;
    /**
     * 分页
     */
    private PageDto pageDto;

}
