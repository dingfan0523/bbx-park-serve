package com.cgnpc.bbxpark.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/***
 * @Description 邀约接待类型枚举
 * @author huangyongtao
 * @date 2025/8/4 16:46
 */
@Getter
@AllArgsConstructor
public enum InviteReceiveTypeEnum {

    /**
     * 本人接待
     */
    SELF(1, "本人接待"),

    /**
     * 他人接待
     */
    OTHER( 2, "他人接待");

    /**
     * 状态编码
     */
    private Integer code;

    /**
     * 状态名称
     */
    private String name;

    public static Integer getCode(String name) {
        for (InviteReceiveTypeEnum configEnum : InviteReceiveTypeEnum.values()) {
            if (configEnum.getName().equals(name)) {
                return configEnum.getCode();
            }
        }
        return null;
    }

    public static String getName(Integer code) {
        for (InviteReceiveTypeEnum configEnum : InviteReceiveTypeEnum.values()) {
            if (configEnum.getCode().equals(code)) {
                return configEnum.getName();
            }
        }
        return "";
    }
}