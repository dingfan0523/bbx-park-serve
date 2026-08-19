package com.cgnpc.bbxpark.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/***
 * @Description 消息类型枚举类
 * @author huangyongtao
 * @date 2024/8/30 10:35
 */
@Getter
@AllArgsConstructor
public enum MessageTypeEnum {

    /**
     * 公告管理
     */
    NOTICE((short)0, "公告管理"),

    /**
     * 安全须知
     */
    SECURE((short)1, "安全须知"),

    /**
     * 投诉建议
     */
    COMPLAINTSUGGESTION((short)2, "投诉建议"),

    /**
     * 餐厅消息
     */
    RESTAURANT((short)3, "餐厅消息"),

    /**
     * 设备告警
     */
    DEVICE((short)5, "设备告警"),

    /**
     * 工单消息
     */
    WORK((short)6, "工单消息"),

    /**
     * 智慧会议
     */
    MEETING((short)7, "智慧会议");

    /**
     * 状态编码
     */
    private Short code;

    /**
     * 状态名称
     */
    private String name;

    public static Short getCode(String name) {
        for (MessageTypeEnum configEnum : MessageTypeEnum.values()) {
            if (configEnum.getName().equals(name)) {
                return configEnum.getCode();
            }
        }
        return null;
    }

    public static String getName(Short code) {
        for (MessageTypeEnum configEnum : MessageTypeEnum.values()) {
            if (configEnum.getCode().equals(code)) {
                return configEnum.getName();
            }
        }
        return "";
    }
}