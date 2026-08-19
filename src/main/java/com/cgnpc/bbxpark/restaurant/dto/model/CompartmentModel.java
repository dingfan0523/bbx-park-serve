
package com.cgnpc.bbxpark.restaurant.dto.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


@Data
public class CompartmentModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -4025654190149645613L;

    @ApiModelProperty(value = "id.")
    private Long id;

    @ApiModelProperty(value = "所属餐厅id.")
    private Long restaurantId;

    @ApiModelProperty(value = "所属餐厅名称.")
    private String restaurantName;

    @ApiModelProperty(value = "包间名称.")
    private String name;

    @ApiModelProperty(value = "包间图片.")
    private String imageUrl;

    @ApiModelProperty(value = "包间位置id.")
    private Long spaceId;

    @ApiModelProperty(value = "包间位置全路径.")
    private String spaceFullPath;

    @ApiModelProperty(value = "位置名称(冗余字段).")
    private String spaceName;

    @ApiModelProperty(value = "容纳人数.")
    private Integer people;

    @ApiModelProperty(value = "面积.")
    private Double area;

    @ApiModelProperty(value = "包间介绍.")
    private String introduce;

    @ApiModelProperty(value = "状态(1->启用;0->禁用).")
    private Integer status;

    @ApiModelProperty(value = "租户id.")
    private Long tenantId;

    @ApiModelProperty(value = "删除状态(1->未删;0->已删).")
    private Integer deleted;

    @ApiModelProperty(value = "创建人id.")
    private String creatorId;

    @ApiModelProperty(value = "创建时间.")
    private Date createTime;

    @ApiModelProperty(value = "更新人.")
    private Long updatorId;

    @ApiModelProperty(value = "更新时间.")
    private Date updateTime;

    @ApiModelProperty(value = "营业时间.")
    private List<CompartmentTimeModel> compartmentTimeModels;

    @ApiModelProperty(value = "设施集合.")
    private List<CompartmentDeviceModel> compartmentDeviceModels;

    @ApiModelProperty(value = "包间套餐关联入参数.")
    private List<CompartmentComboModel> compartmentComboModels;
}
