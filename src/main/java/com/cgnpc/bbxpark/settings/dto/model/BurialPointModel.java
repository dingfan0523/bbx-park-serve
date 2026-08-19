
package com.cgnpc.bbxpark.settings.dto.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


@Data
public class BurialPointModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -3523660805644818074L;

    @ApiModelProperty(value = "主键标识.")
    private Long id;

    @ApiModelProperty(value = "租户id.")
    private Long tenantId;

    @ApiModelProperty(value = "埋点功能编码;例：投诉访问，投诉恢复.")
    private String burialPointFunctionCode;

    @ApiModelProperty(value = "事件类型;例：点击事件、浏览事件.")
    private String eventType;

    @ApiModelProperty(value = "触发时间.")
    private Date triggerTime;

    @ApiModelProperty(value = "停留时长.")
    private Integer durationStay;

    @ApiModelProperty(value = "用户id.")
    private String userId;

    @ApiModelProperty(value = "创建时间.")
    private Date createTime;

    @ApiModelProperty(value = "创建者.")
    private String creatorId;

    @ApiModelProperty(value = "更新时间.")
    private Date updateTime;

    @ApiModelProperty(value = "更新者.")
    private String updatorId;


}
