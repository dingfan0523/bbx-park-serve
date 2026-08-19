package com.cgnpc.bbxpark.property.dto.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
/***
 * @Description 材料记录入参数据模型
 * @author huangyongtao
 * @date 2025/9/22 16:06
 */
@Data
public class MaterialRecordParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id.")
    private Long id;

    @Length(max = 50)
    @ApiModelProperty(value = "入库单号.")
    private String inboundNo;

    @ApiModelProperty(value = "记录类型;（1：入库；2：出库）.")
    private Integer recordType;

    @ApiModelProperty(value = "材料id.")
    private Long materialId;

    @Length(max = 50)
    @ApiModelProperty(value = "材料名称.")
    private String materialName;

    @Length(max = 50)
    @ApiModelProperty(value = "材料编码.")
    private String materialCode;

    @ApiModelProperty(value = "材料类型;（1：器材；2：耗材）.")
    private Integer materialType;

    @ApiModelProperty(value = "入库数量.")
    private Integer quantity;

    @ApiModelProperty(value = "当前库存.")
    private Integer currentStock;

    @ApiModelProperty(value = "数据来源;（1：手动入库；2：工单维修）.")
    private Integer dataSource;

    @Length(max = 255)
    @ApiModelProperty(value = "备注.")
    private String remark;

}
