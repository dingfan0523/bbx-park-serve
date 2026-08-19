package com.cgnpc.bbxpark.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/***
 * @Description 卡片类型枚举类
 * @author huangyongtao
 * @date 2024/8/30 10:35
 */
@Getter
@AllArgsConstructor
public enum CardTypeEnum {

    /**
     * 餐厅消息
     */
    RESTAURANT(3, "包间预定"),

    /**
     * 智慧会议
     */
    MEETING(7, "参会提醒");

    /**
     * 状态编码
     */
    private Integer code;

    /**
     * 状态名称
     */
    private String name;

    public static Integer getCode(String name) {
        for (CardTypeEnum configEnum : CardTypeEnum.values()) {
            if (configEnum.getName().equals(name)) {
                return configEnum.getCode();
            }
        }
        return null;
    }

    public static String getName(Short code) {
        for (CardTypeEnum configEnum : CardTypeEnum.values()) {
            if (configEnum.getCode().equals(code)) {
                return configEnum.getName();
            }
        }
        return "";
    }
}