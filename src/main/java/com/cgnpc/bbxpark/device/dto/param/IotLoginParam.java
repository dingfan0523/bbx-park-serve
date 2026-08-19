package com.cgnpc.bbxpark.device.dto.param;

import lombok.Data;

import java.io.Serializable;

/**
 * iot授权登录参数
 */
@Data
public class IotLoginParam implements Serializable {

    private static final long serialVersionUID = 1L;

    private String username;

    private String password;

    private String tenantId;


}
