package com.cgnpc.bbxpark.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/***
 * @Description 会议预约结束类型枚举
 * @author huangyongtao
 * @date 2024/9/24 10:46
 */
@Getter
@AllArgsConstructor
public enum MeetingReserveEndTypeEnum {

    /**
     * 发起人结束会议
     */
    RESERVE(1, "发起人结束会议"),

    /**
     * 系统自动结束
     */
    AUTO( 2, "系统自动结束"),

    /**
     * 会服结束会议
     */
    SERVICE( 3, "会服结束会议");

    /**
     * 状态编码
     */
    private Integer code;

    /**
     * 状态名称
     */
    private String name;

    public static Integer getCode(String name) {
        for (MeetingReserveEndTypeEnum configEnum : MeetingReserveEndTypeEnum.values()) {
            if (configEnum.getName().equals(name)) {
                return configEnum.getCode();
            }
        }
        return null;
    }

    public static String getName(Integer code) {
        if(code == null){
            return "";
        }
        for (MeetingReserveEndTypeEnum configEnum : MeetingReserveEndTypeEnum.values()) {
            if (configEnum.getCode().equals(code)) {
                return configEnum.getName();
            }
        }
        return "";
    }
}