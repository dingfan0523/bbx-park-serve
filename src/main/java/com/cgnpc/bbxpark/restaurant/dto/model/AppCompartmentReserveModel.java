
package com.cgnpc.bbxpark.restaurant.dto.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/***
 * @Description 包间预定业务数据模型
 * @author huangyongtao
 * @date 2024/7/30 14:41
 */
@Data
public class AppCompartmentReserveModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id.")
    private Long id;
    @ApiModelProperty(value = "包间id")
    private Long compartmentId;
    @ApiModelProperty(value = "包间名称.")
    private String compartmentName;
    @ApiModelProperty(value = "包间图片")
    private String compartmentImageUrl;
    @ApiModelProperty(value = "餐厅名称")
    private String restaurantName;
    @ApiModelProperty(value = "预定状态.")
    private Integer reserveStatus;
    @ApiModelProperty(value = "满意度.")
    private Integer satisfaction;
    @ApiModelProperty(value = "菜品.")
    private String dishes;
    @ApiModelProperty(value = "环境.")
    private String environment;
    @ApiModelProperty(value = "服务.")
    private String service;
    @ApiModelProperty(value = "取消原因")
    private String cancelReason;
    @ApiModelProperty(value = "预定开始时间.")
    private Date reserveStartTime;
    @ApiModelProperty(value = "预定结束时间.")
    private Date reserveEndTime;
    @ApiModelProperty(value = "创建时间.")
    private Date createTime;
}
