package com.cgnpc.bbxpark.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/***
 * @Description 会议签到类型枚举
 * @author huangyongtao
 * @date 2024/9/24 10:46
 */
@Getter
@AllArgsConstructor
public enum MeetingSignTypeEnum {
    /**
     * 正常签到
     */
    NORMAL(1, "本人签到"),
    /**
     * 补签
     */
    REPAIR( 2, "补签"),
    /**
     * 代签
     */
    BEHALF( 3, "代签到"),
    /**
     * 代补签
     */
    BEHALF_REPAIR( 4, "代补签"),

    /**
     * 未签到
     */
    NOT( 5, "未签到");

    /**
     * 编码
     */
    private Integer code;

    /**
     * 名称
     */
    private String name;

    public static Integer getCode(String name) {
        for (MeetingSignTypeEnum configEnum : MeetingSignTypeEnum.values()) {
            if (configEnum.getName().equals(name)) {
                return configEnum.getCode();
            }
        }
        return null;
    }

    public static String getName(Integer code) {
        for (MeetingSignTypeEnum configEnum : MeetingSignTypeEnum.values()) {
            if (configEnum.getCode().equals(code)) {
                return configEnum.getName();
            }
        }
        return "";
    }
}