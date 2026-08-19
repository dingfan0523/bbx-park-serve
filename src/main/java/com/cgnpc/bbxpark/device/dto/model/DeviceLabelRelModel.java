
package com.cgnpc.bbxpark.device.dto.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 设备标签关系业务数据模型
 */
@Data
public class DeviceLabelRelModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "关系id;关系id.")
    private Long id;

    @ApiModelProperty(value = "标签id;标签编码.")
    private String labelId;

    @ApiModelProperty(value = "设备id;设备id.")
    private Long deviceId;

    @ApiModelProperty(value = "园区id;园区id.")
    private Long tenantId;

    @ApiModelProperty(value = "创建人工号.")
    private String createUserNo;

    @ApiModelProperty(value = "创建人名称.")
    private String createUserName;

    @ApiModelProperty(value = "创建时间.")
    private Date createDate;

}
