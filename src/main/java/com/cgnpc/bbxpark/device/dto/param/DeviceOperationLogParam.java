
package com.cgnpc.bbxpark.device.dto.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 设备控制（操作）日志入参数据模型
 */
@Data
public class DeviceOperationLogParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @NotNull
    @ApiModelProperty(value = "主键.")
    private Long id;

    @Length(max = 100)
    @ApiModelProperty(value = "设备id.")
    private String deviceId;

    @ApiModelProperty(value = "类型(1规则,2场景,3能力清单).")
    private Long type;

    @Length(max = 65535)
    @ApiModelProperty(value = "控制报文.")
    private String params;

    @Length(max = 65535)
    @ApiModelProperty(value = "返回报文.")
    private String response;

    @ApiModelProperty(value = "园区id.")
    private Long tenantId;

    @ApiModelProperty(value = "创建人.")
    private String creatorId;

    @ApiModelProperty(value = "创建时间.")
    private Date createTime;

    @ApiModelProperty(value = "更新人.")
    private String updatorId;

    @ApiModelProperty(value = "更新时间.")
    private Date updateTime;

    @ApiModelProperty(value = "主键集合.")
    private List<Long> ids;
}
