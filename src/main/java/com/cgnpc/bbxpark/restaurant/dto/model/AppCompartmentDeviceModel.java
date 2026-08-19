
package com.cgnpc.bbxpark.restaurant.dto.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


@Data
public class AppCompartmentDeviceModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -3372132278626950718L;

    @ApiModelProperty(value = "设施id.")
    private Long deviceId;
    @ApiModelProperty(value = "设施名称.")
    private String deviceName;
}
