package com.cgnpc.bbxpark.workbench.dto;

import com.cgnpc.cud.workflow2.base.model.WfRequestDto;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/******************************
 * 用途说明: 业务表单删除数据
 * 作者姓名: P629988
 * 创建时间: 2022/8/8
 ******************************/
@Data
@NotNull(message = "busiData cannot be null")
public class DeleteBusiDataDto extends WfRequestDto {
    /**
     * 表单id
     */
    private String formId;
    /**
     * 数据对应id
     */
    private List<String> ids;

}
