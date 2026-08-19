
package com.cgnpc.bbxpark.device.dto.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/***
 * @value ioc设备简要业务数据模型
 * @author huangyongtao
 * @date 2025/2/21 17:07
 */
@Data
public class IocDeviceSimpleModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id.")
    private Long id;

    @ApiModelProperty(value = "设备名称.")
    private String deviceName;

    @ApiModelProperty(value = "设备编码.")
    private String deviceCode;

    @ApiModelProperty(value = "所属空间ID.")
    private Long spaceId;

    @ApiModelProperty(value = "所属空间名称.")
    private String spaceName;

    @ApiModelProperty(value = "设备等级;（10：关键；20：重要；30：一般）.")
    private Integer deviceLevel = 30;

}
