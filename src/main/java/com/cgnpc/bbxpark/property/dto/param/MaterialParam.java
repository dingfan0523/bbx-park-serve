package com.cgnpc.bbxpark.property.dto.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
/***
 * @Description 材料入参数据模型
 * @author huangyongtao
 * @date 2025/9/22 15:59
 */
@Data
public class MaterialParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id.")
    private Long id;

    @ApiModelProperty(value = "空间id")
    private Long spaceId;

    @Length(max = 50)
    @ApiModelProperty(value = "材料名称.")
    private String materialName;

    @Length(max = 50)
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

    @Length(max = 255)
    @ApiModelProperty(value = "备注.")
    private String remark;
}
