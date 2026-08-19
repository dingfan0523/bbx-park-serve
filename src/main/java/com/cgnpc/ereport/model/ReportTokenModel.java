package com.cgnpc.ereport.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 用途说明: 报表授权模型
 * 创建时间: 20240325 15:49
 * @author P629988
 */
@Data
public class ReportTokenModel {
    private String syscode;
    private String secretKey;
    private String accessToken;
    private String ereportUrl;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date authTime;
    private Long expiresIn;
    private String user;
}
