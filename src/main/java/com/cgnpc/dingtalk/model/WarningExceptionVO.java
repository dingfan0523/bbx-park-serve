package com.cgnpc.dingtalk.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@SuppressWarnings("all")
public class WarningExceptionVO {
    private String url;
    private String code;
    private String method;
    private String function;
    private String params;
    private String result;
    private Date time;
}