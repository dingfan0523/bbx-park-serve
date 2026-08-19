package com.cgnpc.bbxpark.device.dto.model;


import lombok.Data;

import java.io.Serializable;

@Data
public class IotLoginModel implements Serializable {


    private static final long serialVersionUID = 1L;

    private Integer code;
    private String message;
    private IotLogin data;
    private String requestId;

    @Data
    public static class IotLogin {
        private String token;
    }
}
