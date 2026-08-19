package com.cgnpc.bbxpark.device.dto.model;

import com.cgnpc.bbxpark.acl.iot.dto.resp.IotDeviceState;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class IotDeviceModel implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer code;

    private String message;

    private List<IotDeviceState> data;

    private String requestId;
}
