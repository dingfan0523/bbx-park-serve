
package com.cgnpc.bbxpark.restaurant.dto.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


@Data
public class CompartmentEvaluateModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -4909561597434478012L;

    @ApiModelProperty(value = "id.")
    private Long id;

    @ApiModelProperty(value = "包间id.")
    private Long compartmentId;

    @ApiModelProperty(value = "包间预定id.")
    private Long reserveId;

    @ApiModelProperty(value = "满意度.")
    private Integer satisfaction;

    @ApiModelProperty(value = "菜品.")
    private String dishes;

    @ApiModelProperty(value = "环境.")
    private String environment;

    @ApiModelProperty(value = "服务.")
    private String service;

    @ApiModelProperty(value = "评价人id.")
    private String appraiserId;

    @ApiModelProperty(value = "评价人名称.")
    private String appraiserName;

    @ApiModelProperty(value = "评价人工号.")
    private String appraiserStaffid;

    @ApiModelProperty(value = "匿名状态(0->未匿名;1->匿名).")
    private Integer anonymityStatus;

    @ApiModelProperty(value = "创建时间.")
    private Date createTime;


}
