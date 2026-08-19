
package com.cgnpc.bbxpark.property.dto.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


@Data
public class PatrolPointModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -4521810271625420915L;

    @ApiModelProperty(value = "主键id.")
    private Long id;

    @ApiModelProperty(value = "名称.")
    private String name;

    @ApiModelProperty(value = "编码.")
    private String code;

    @ApiModelProperty(value = "类型（10：安保；20：保洁；30：消控；40：环境；50：设备）.")
    private Integer type;

    @ApiModelProperty(value = "方式（10：拍照；20：其他）.")
    private Integer way;

    @ApiModelProperty(value = "位置id.")
    private Long spaceId;

    @ApiModelProperty(value = "位置名称.")
    private String spaceName;

    @ApiModelProperty(value = "巡更要求.")
    private String remark;

    @ApiModelProperty(value = "重点检查(0->否;1->是).")
    private Integer keyPoint = 0;

    @ApiModelProperty(value = "启用状态;0->否;1->是.")
    private Integer status = 1;

    @ApiModelProperty(value = "创建人.")
    private String creatorId;

    @ApiModelProperty(value = "创建人名称.")
    private String createBy;

    @ApiModelProperty(value = "创建时间.")
    private Date createTime;
}
