
package com.cgnpc.bbxpark.workorder.dto.param;



import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 工单关联设备列表参数模型
 */
@Data
public class WorkOrderDeviceListParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键.")
    private Integer id;

    @ApiModelProperty(value = "设备id.")
    private String deviceId;

    @ApiModelProperty(value = "设备名称.")
    private String deviceName;

    @ApiModelProperty(value = "设备历史状态.")
    private Boolean deviceHisState;

    @ApiModelProperty(value = "空间id.")
    private Long spaceId;

    @ApiModelProperty(value = "空间全路径.")
    private String spaceFullPath;

    @ApiModelProperty(value = "工单id.")
    private Long workOrderId;

    @ApiModelProperty(value = "租户id.")
    private Long tenantId;

    @ApiModelProperty(value = "创建人id.")
    private String creatorId;

    @ApiModelProperty(value = "创建时间.")
    private Date createTime;

    @ApiModelProperty(value = "更新人id.")
    private String updatorId;

    @ApiModelProperty(value = "更新时间.")
    private Date updateTime;

    @ApiModelProperty(value = "主键集合.")
    private List<Integer> ids;
}
