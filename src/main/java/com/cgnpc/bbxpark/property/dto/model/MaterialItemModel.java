package com.cgnpc.bbxpark.property.dto.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author dingfan
 * @version 1.0
 * @date 2025/11/5 9:44
 */
@Data
public class MaterialItemModel implements Serializable {
    @ApiModelProperty(value = "主键id.")
    private Long id;
    @ApiModelProperty(value = "材料名称.")
    private String materialName;
    @ApiModelProperty(value = "材料编码.")
    private String materialCode;
    @ApiModelProperty(value = "材料类型;（1：器材；2：耗材）.")
    private Integer materialType;
    @ApiModelProperty(value = "库存数量.")
    private Integer stockQuantity;
    @ApiModelProperty(value = "库存预警值.")
    private Integer stockWarning;
    @ApiModelProperty(value = "库存状态;（1：库存充足；2：库存不足；3：缺货）.")
    private Integer stockStatus;
    @ApiModelProperty(value = "备注.")
    private String remark;
    @ApiModelProperty(value = "删除状态(0->已删;1->未删).")
    private Integer deleted = 1;
}
