package com.cgnpc.bbxpark.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/***
 * @Description 投诉建议流转类型枚举
 * @author huangyongtao
 * @date 2024/7/15 11:55
 */
@Getter
@AllArgsConstructor
public enum ComplaintSuggestionRomanTypeEnum {

    /**
     * 回复
     */
    REPLY("reply", "回复"),

    /**
     * 审核
     */
    AUDIT( "audit", "审核");

    /**
     * 状态编码
     */
    private String code;

    /**
     * 状态名称
     */
    private String name;

    public static String getCode(String name) {
        for (ComplaintSuggestionRomanTypeEnum configEnum : ComplaintSuggestionRomanTypeEnum.values()) {
            if (configEnum.getName().equals(name)) {
                return configEnum.getCode();
            }
        }
        return "";
    }

    public static String getName(String code) {
        for (ComplaintSuggestionRomanTypeEnum configEnum : ComplaintSuggestionRomanTypeEnum.values()) {
            if (configEnum.getCode().equals(code)) {
                return configEnum.getName();
            }
        }
        return "";
    }
}