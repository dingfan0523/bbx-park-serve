package com.cgnpc.bbxpark.property.dto.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/***
 * @Description 材料业务数据模型
 * @author huangyongtao
 * @date 2025/9/22 15:48
 */
@Data
public class MaterialModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id.")
    private Long id;

    @ApiModelProperty(value = "空间id")
    private Long spaceId;

    @ApiModelProperty(value = "空间名称")
    private String spaceName;

    @ApiModelProperty(value = "材料名称.")
    private String materialName;

    @ApiModelProperty(value = "材料编码.")
    private String materialCode;

    @ApiModelProperty(value = "材料类型;（1：器材；2：耗材-备品；3：耗材-备件）.")
    private Integer materialType;

    @ApiModelProperty(value = "库存数量.")
    private Integer stockQuantity;

    @ApiModelProperty(value = "库存预警值.")
    private Integer stockWarning;

    @ApiModelProperty(value = "库存状态;（1：库存充足；2：库存不足；3：缺货）.")
    private Integer stockStatus;

    @ApiModelProperty(value = "备注.")
    private String remark;

    @ApiModelProperty(value = "创建人名称.")
    private String createBy;

    @ApiModelProperty(value = "创建人.")
    private String creatorId;

    @ApiModelProperty(value = "创建时间.")
    private Date createTime;


}
