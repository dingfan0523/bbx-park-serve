package com.cgnpc.bbxpark.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/***
 * @Description 会议预约状态枚举
 * @author huangyongtao
 * @date 2024/8/26 16:46
 */
@Getter
@AllArgsConstructor
public enum MeetingReserveStatusEnum {

    /**
     * 待开始
     */
    START(10, "待开始"),

    /**
     * 进行中
     */
    GOING( 20, "进行中"),

    /**
     * 已结束
     */
    END( 30, "已结束");

    /**
     * 状态编码
     */
    private Integer code;

    /**
     * 状态名称
     */
    private String name;

    public static Integer getCode(String name) {
        for (MeetingReserveStatusEnum configEnum : MeetingReserveStatusEnum.values()) {
            if (configEnum.getName().equals(name)) {
                return configEnum.getCode();
            }
        }
        return null;
    }

    public static String getName(Integer code) {
        for (MeetingReserveStatusEnum configEnum : MeetingReserveStatusEnum.values()) {
            if (configEnum.getCode().equals(code)) {
                return configEnum.getName();
            }
        }
        return "";
    }
}