package com.cgnpc.bbxpark.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 渠道结果枚举
 */
@Getter
@AllArgsConstructor
public enum ChannelEnum {

    /**
     * 移动端
     */
    APP("app", "移动端"),

    /**
     * 用户端
     */
    PC("pc", "用户端"),
    /**
     * 大屏
     */
    SCREEN("screen", "大屏");

    /**
     * 状态编码
     */
    private String code;

    /**
     * 状态名称
     */
    private String name;


    public static String getCode(String name) {
        for (ChannelEnum configEnum : ChannelEnum.values()) {
            if (configEnum.getName().equals(name)) {
                return configEnum.getCode();
            }
        }
        return null;
    }

    public static String getName(String code) {
        for (ChannelEnum configEnum : ChannelEnum.values()) {
            if (configEnum.getCode().equals(code)) {
                return configEnum.getName();
            }
        }
        return "";
    }
}