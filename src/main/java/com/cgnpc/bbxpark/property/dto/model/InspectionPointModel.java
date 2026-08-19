package com.cgnpc.bbxpark.property.dto.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 巡检点业务数据模型
 */
@Data
public class InspectionPointModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -3236140037498573132L;

    @ApiModelProperty(value = "主键id.")
    private Long id;

    @ApiModelProperty(value = "名称.")
    private String name;

    @ApiModelProperty(value = "编码.")
    private String code;

    @ApiModelProperty(value = "位置id.")
    private Long spaceId;

    @ApiModelProperty(value = "位置名称.")
    private String spaceName;

    @ApiModelProperty(value = "设备ID;多个以英文逗号隔开，例如：1,2,3.")
    private String deviceId;

    @ApiModelProperty(value = "设备名称;多个以英文逗号隔开，例如：设备1,设备2,设备3.")
    private String deviceName;

    @ApiModelProperty(value = "巡检要求.")
    private String remark;

    @ApiModelProperty(value = "启用状态;0->否;1->是.")
    private Integer status = 1;

    @ApiModelProperty(value = "删除状态(0->已删;1->未删).")
    private Integer deleted = 1;

    @ApiModelProperty(value = "创建人工号.")
    private String creatorId;

    @ApiModelProperty(value = "创建人名称.")
    private String createBy;

    @ApiModelProperty(value = "创建时间.")
    private Date createTime;
}
