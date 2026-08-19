package com.cgnpc.bbxpark.ioc.dto.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 大屏餐厅实况
 */
@Data
public class RestaurantOverviewModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "就餐总人次")
    private Integer diningTotal;

    @ApiModelProperty(value = "餐线摄像头设备集合")
    private List<videoDevice> videoDevices;

    @Data
    @ApiModel(value = "餐线摄像头设备")
    public static class videoDevice {
        @ApiModelProperty(value = "主键id.")
        private Long id;

        @ApiModelProperty(value = "物联网设备dn.")
        private String iotDeviceDn;

        @ApiModelProperty(value = "设备名称.")
        private String deviceName;
    }
}

