
package com.cgnpc.bbxpark.restaurant.dto.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


@Data
public class CompartmentTimeModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -3339168835820572122L;

    @ApiModelProperty(value = "id.")
    private Long id;

    @ApiModelProperty(value = "包间id.")
    private Long compartmentId;

    @ApiModelProperty(value = "用餐类型（字典）.")
    private String type;

    @ApiModelProperty(value = "开始时间.")
    private String startTime;

    @ApiModelProperty(value = "结束时间.")
    private String endTime;

    @ApiModelProperty(value = "租户id.")
    private Long tenantId;

    @ApiModelProperty(value = "创建人id.")
    private String creatorId;

    @ApiModelProperty(value = "创建时间.")
    private Date createTime;

    @ApiModelProperty(value = "更新人.")
    private Long updatorId;

    @ApiModelProperty(value = "更新时间.")
    private Date updateTime;

    @ApiModelProperty(value = "是否被占用")
    private Boolean occupyFlag = false;


}
