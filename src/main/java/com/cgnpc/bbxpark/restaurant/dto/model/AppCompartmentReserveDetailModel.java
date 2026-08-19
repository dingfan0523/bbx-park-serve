package com.cgnpc.bbxpark.restaurant.dto.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 包间预定详情业务数据模型
 * @author dingfan
 * @date 2024/8/2 17:19
 */
@Data
public class AppCompartmentReserveDetailModel implements Serializable {
    @ApiModelProperty(value = "预定id.")
    private Long id;
    @ApiModelProperty(value = "包间id")
    private Long compartmentId;
    @ApiModelProperty(value = "预定状态")
    private Integer reserveStatus;
    @ApiModelProperty(value = "包间名称.")
    private String compartmentName;
    @ApiModelProperty(value = "包间图片")
    private String compartmentImageUrl;
    @ApiModelProperty(value = "餐厅名称")
    private String restaurantName;
    @ApiModelProperty(value = "套餐名称.")
    private String comboName;
    @ApiModelProperty(value = "套餐描述")
    private String comboDescription;
    @ApiModelProperty(value = "预定开始时间.")
    private Date reserveStartTime;
    @ApiModelProperty(value = "预定结束时间.")
    private Date reserveEndTime;
    @ApiModelProperty(value = "提交时间.")
    private Date createTime;
    @ApiModelProperty(value = "到店时间.")
    private Date useTime;
    @ApiModelProperty(value = "取消原因")
    private String cancelReason;
    @ApiModelProperty(value = "取消备注.")
    private String cancelRemark;

    /**
     * 评价相关信息
     */
    @ApiModelProperty(value = "满意度")
    private Integer satisfaction;
    @ApiModelProperty(value = "菜品.")
    private String dishes;
    @ApiModelProperty(value = "环境.")
    private String environment;
    @ApiModelProperty(value = "服务.")
    private String service;
}
