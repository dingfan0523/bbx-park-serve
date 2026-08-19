package com.cgnpc.bbxpark.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/***
 * @Description 埋点事件类型
 * @author lhy
 * @date 2024/07/15 17:41
 */
@Getter
@AllArgsConstructor
public enum EventTypeEnum {

    /**
     * 点击事件
     */
    CLICK("click", "点击事件"),

    /**
     * 浏览事件
     */
    BROWSE( "browse", "浏览事件");

    /**
     * 状态编码
     */
    private String code;

    /**
     * 状态名称
     */
    private String name;

    public static String getCode(String name) {
        for (EventTypeEnum configEnum : EventTypeEnum.values()) {
            if (configEnum.getName().equals(name)) {
                return configEnum.getCode();
            }
        }
        return "0";
    }

    public static String getName(String code) {
        for (EventTypeEnum configEnum : EventTypeEnum.values()) {
            if (configEnum.getCode().equals(code)) {
                return configEnum.getName();
            }
        }
        return String.valueOf(code);
    }
}