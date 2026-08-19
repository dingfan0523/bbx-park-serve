
package com.cgnpc.bbxpark.workorder.dto.param;



import com.cgnpc.bbxpark.common.web.CudPageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 工单关联设备分页参数模型
 */
@Data
public class WorkOrderDevicePageParam  extends CudPageDto implements Serializable {
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
    private Long updatorId;

    @ApiModelProperty(value = "更新时间.")
    private Date updateTime;

}
