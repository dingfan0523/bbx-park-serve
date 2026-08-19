package com.cgnpc.bbxpark.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/***
 * @Description 会议预约开始类型枚举
 * @author huangyongtao
 * @date 2024/9/24 10:46
 */
@Getter
@AllArgsConstructor
public enum MeetingReserveStartTypeEnum {

    /**
     * 参会人签到
     */
    SIGN(1, "参会人签到"),

    /**
     * 会议到达开始时间
     */
    AUTO( 2, "会议到达开始时间");

    /**
     * 状态编码
     */
    private Integer code;

    /**
     * 状态名称
     */
    private String name;

    public static Integer getCode(String name) {
        for (MeetingReserveStartTypeEnum configEnum : MeetingReserveStartTypeEnum.values()) {
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
        for (MeetingReserveStartTypeEnum configEnum : MeetingReserveStartTypeEnum.values()) {
            if (configEnum.getCode().equals(code)) {
                return configEnum.getName();
            }
        }
        return "";
    }
}