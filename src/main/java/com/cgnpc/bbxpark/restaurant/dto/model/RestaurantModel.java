package com.cgnpc.bbxpark.restaurant.dto.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**********************************************************

 * 摘    要： [餐厅业务数据模型]

 *
 *****************************************************************/
@Data
public class RestaurantModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -3722473084892844204L;

    @ApiModelProperty(value = "id.")
    private Long id;

    @ApiModelProperty(value = "餐厅名称.")
    private String name;

    @ApiModelProperty(value = "餐厅图片.")
    private String imageUrl;

    @ApiModelProperty(value = "容纳人数.")
    private Integer capacity;


    @ApiModelProperty(value = "餐厅标签.")
    private List<String> tags;

    @ApiModelProperty(value = "餐厅电话.")
    private String telphone;

    @ApiModelProperty(value = "介绍.")
    private String introduce;

    @ApiModelProperty(value = "通知.")
    private String notification;

    @ApiModelProperty(value = "状态(1->启用;0->禁用).")
    private Integer status;

    @ApiModelProperty(value = "创建时间.")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;


    /*********需组装参数***********/
    @ApiModelProperty(value = "餐厅位置.")
    private String spaceLabel;

    @ApiModelProperty(value = "餐厅集合.")
    private List<RestaurantSpaceModel> spaceList;

    @ApiModelProperty(value = "营业状态.")
    private Boolean runState;

    @ApiModelProperty(value = "营业时间.")
    private List<RestaurantTimeModel> restaurantTimeModelList;


}
