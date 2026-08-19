
package com.cgnpc.bbxpark.settings.dto.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/***
 * @value 安全管理员设备关联业务数据模型
 * @author huangyongtao
 * @date 2025/7/31 17:40
 */
@Data
public class SecurityDeviceRelationModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id.")
    private Long id;

    @ApiModelProperty(value = "安全管理id.")
    private Long securityManageId;

    @ApiModelProperty(value = "设备id.")
    private Long deviceId;

    @ApiModelProperty(value = "设备id.")
    private String deviceName;

    @ApiModelProperty(value = "空间id.")
    private Long spaceId;

    @ApiModelProperty(value = "安全管理员名称.")
    private String securityUname;

    @ApiModelProperty(value = "安全管理员工号.")
    private String securityStaffid;

    @ApiModelProperty(value = "安全管理员id.")
    private String securityUid;

}
