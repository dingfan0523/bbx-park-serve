package com.cgnpc.bbxpark.device.dto.param;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class IotDeviceParam implements Serializable {
    private static final long serialVersionUID = 1L;

    private DeviceData data;

    @Data
    public static class DeviceData{

        private List<String> deviceIds;
    }
}
