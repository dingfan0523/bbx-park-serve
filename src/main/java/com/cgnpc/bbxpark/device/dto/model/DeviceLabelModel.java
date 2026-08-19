
package com.cgnpc.bbxpark.device.dto.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 设备标签业务数据模型
 */
@Data
public class DeviceLabelModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id.")
    private Long id;

    @ApiModelProperty(value = "标签编码.")
    private String labelCode;

    @ApiModelProperty(value = "标签名称.")
    private String labelName;

    @ApiModelProperty(value = "标签设备数量.")
    private Integer labelDeviceNum = 0;

    @ApiModelProperty(value = "标签颜色.")
    private String labelColour;

    @ApiModelProperty(value = "标签描述.")
    private String labelDescribe;

    @ApiModelProperty(value = "租户号.")
    private Long tenantId;

    @ApiModelProperty(value = "员工号.")
    private String staffId;

    @ApiModelProperty(value = "创建人工号.")
    private String creatorId;

    @ApiModelProperty(value = "创建人名称.")
    private String createBy;

    @ApiModelProperty(value = "创建时间.")
    private Date createTime;
}
