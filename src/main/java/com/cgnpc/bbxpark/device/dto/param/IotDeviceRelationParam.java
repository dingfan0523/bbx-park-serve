package com.cgnpc.bbxpark.device.dto.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.util.List;

/***
 * @Description ioc设备关联入参数据模型
 * @author huangyongtao
 * @date 2025/3/3 15:38
 */
@Data
public class IotDeviceRelationParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

   @ApiModelProperty(value = "主键id.")
    private Long id;

    @Length(max = 30)
   @ApiModelProperty(value = "关联类型;(video：视频设备).")
    private String relationType;

   @ApiModelProperty(value = "关联的设备id.")
    private Long relationDeviceId;

   @ApiModelProperty(value = "关联的设备id集合")
    private List<Long> relationDeviceIds;

   @ApiModelProperty(value = "设备id.")
    private Long deviceId;

   @ApiModelProperty(value = "园区ID-租户号.")
    private Long tenantId;
}
