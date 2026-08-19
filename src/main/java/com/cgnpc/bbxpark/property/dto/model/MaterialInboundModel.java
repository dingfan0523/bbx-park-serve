package com.cgnpc.bbxpark.property.dto.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/***
 * @Description 材料入库业务数据模型
 * @author huangyongtao
 * @date 2025/9/22 15:47
 */
@Data
public class MaterialInboundModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id.")
    private Long id;

    @ApiModelProperty(value = "入库单号.")
    private String inboundNo;

    @ApiModelProperty(value = "材料id.")
    private Long materialId;

    @ApiModelProperty(value = "材料名称.")
    private String materialName;

    @ApiModelProperty(value = "材料编码.")
    private String materialCode;

    @ApiModelProperty(value = "材料类型;（1：器材；2：耗材）.")
    private Integer materialType;

    @ApiModelProperty(value = "入库数量.")
    private Integer quantity;

    @ApiModelProperty(value = "备注.")
    private String remark;

    @ApiModelProperty(value = "创建人名称.")
    private String createBy;

    @ApiModelProperty(value = "创建人.")
    private String creatorId;

    @ApiModelProperty(value = "创建时间.")
    private Date createTime;
}
