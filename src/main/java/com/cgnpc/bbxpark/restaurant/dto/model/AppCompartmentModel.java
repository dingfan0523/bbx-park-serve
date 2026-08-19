package com.cgnpc.bbxpark.restaurant.dto.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 移动端-包间业务数据模型
 * @author dingfan
 * @date 2024/7/31 15:01
 */
@Data
public class AppCompartmentModel implements Serializable {
    @ApiModelProperty(value = "id.")
    private Long id;
    @ApiModelProperty(value = "所属餐厅id.")
    private Long restaurantId;
    @ApiModelProperty(value = "包间名称.")
    private String name;
    @ApiModelProperty(value = "所属餐厅名称")
    private String restaurantName;
    @ApiModelProperty(value = "包间图片.")
    private String imageUrl;
    @ApiModelProperty(value = "包间位置id.")
    private Long spaceId;
    @ApiModelProperty(value = "包间位置名称")
    private String spaceName;
    @ApiModelProperty(value = "容纳人数.")
    private Integer people;
    @ApiModelProperty(value = "面积.")
    private Double area;
    @ApiModelProperty(value = "包间介绍.")
    private String introduce;
    @ApiModelProperty(value = "满意度")
    private Double satisfaction;
    @ApiModelProperty(value = "营业时间.")
    private List<AppCompartmentTimeModel> timeList;
    @ApiModelProperty(value = "设施集合")
    private List<AppCompartmentDeviceModel> deviceList;
    @ApiModelProperty(value = "评价总数")
    private int evaluateTotal;
}
