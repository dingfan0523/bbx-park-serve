package com.cgnpc.bbxpark.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/***
 * @Description 告警级别枚举类
 * @author huangyongtao
 * @date 2024/8/30 10:35
 */
@Getter
@AllArgsConstructor
public enum AlarmLevelEnum {

    /**
     * 紧急告警
     */
    EXIGENCY("1", "紧急告警","紧急"),

    /**
     * 重要告警
     */
    IMPORTANCE("2", "重要告警","重要"),

    /**
     * 次要告警
     */
    SECONDARY("3", "通知告警","通知"),

    /**
     * 提示告警
     */
    WARN("4", "一般告警","一般"),

    /**
     * 其他级别
     */
    OTHER("5", "其他级别","其他");

    /**
     * 状态编码
     */
    private String code;

    /**
     * 状态名称
     */
    private String name;

    /**
     * 级别缩写
     */
    private String desc;

    public static String getCode(String name) {
        for (AlarmLevelEnum configEnum : AlarmLevelEnum.values()) {
            if (configEnum.getName().equals(name)) {
                return configEnum.getCode();
            }
        }
        return null;
    }
    public static String getDesc(String code) {
        for (AlarmLevelEnum configEnum : AlarmLevelEnum.values()) {
            if (configEnum.getCode().equals(code)) {
                return configEnum.getDesc();
            }
        }
        return "";
    }

    public static String getName(String code) {
        for (AlarmLevelEnum configEnum : AlarmLevelEnum.values()) {
            if (configEnum.getCode().equals(code)) {
                return configEnum.getName();
            }
        }
        return "";
    }
}