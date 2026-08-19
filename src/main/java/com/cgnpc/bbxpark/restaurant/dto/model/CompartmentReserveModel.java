
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
public class CompartmentReserveModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id.")
    private Long id;

    @ApiModelProperty(value = "包间id.")
    private Long compartmentId;

    @ApiModelProperty(value = "包间名称.")
    private String compartmentName;

    @ApiModelProperty(value = "预订人id.")
    private String subscriberId;

    @ApiModelProperty(value = "预订人名称.")
    private String subscriberName;

    @ApiModelProperty(value = "预订人工号.")
    private String subscriberStaffid;

    @ApiModelProperty(value = "套餐id.")
    private Long comboId;

    @ApiModelProperty(value = "套餐名称.")
    private String comboName;

    @ApiModelProperty(value = "套餐价格")
    private Double comboPrice;

    @ApiModelProperty(value = "套餐描述.")
    private String comboDescription;

    @ApiModelProperty(value = "预定开始时间.")
    private Date reserveStartTime;

    @ApiModelProperty(value = "预定结束时间.")
    private Date reserveEndTime;

    @ApiModelProperty(value = "联系方式.")
    private String phone;

    @ApiModelProperty(value = "预定状态.")
    private Integer reserveStatus;

    @ApiModelProperty(value = "预定来源.")
    private String reserveSource;

    @ApiModelProperty(value = "到店时间.")
    private Date useTime;

    @ApiModelProperty(value = "取消时间.")
    private Date cancelTime;

    @ApiModelProperty(value = "取消原因.")
    private String cancelReason;

    @ApiModelProperty(value = "取消备注.")
    private String cancelRemark;

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


}
