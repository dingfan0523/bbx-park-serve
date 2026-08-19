package com.cgnpc.bbxpark.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/***
 * @Description 投诉建议类型枚举
 * @author huangyongtao
 * @date 2025/3/31 11:55
 */
@Getter
@AllArgsConstructor
public enum ComplaintSuggestionTypeEnum {

    /**
     * 投诉
     */
    COMPLAINT("complaint", "投诉"),

    /**
     * 建议
     */
    SUGGESTION( "suggestion", "建议");

    /**
     * 状态编码
     */
    private String code;

    /**
     * 状态名称
     */
    private String name;

    public static String getCode(String name) {
        for (ComplaintSuggestionTypeEnum configEnum : ComplaintSuggestionTypeEnum.values()) {
            if (configEnum.getName().equals(name)) {
                return configEnum.getCode();
            }
        }
        return "";
    }

    public static String getName(String code) {
        for (ComplaintSuggestionTypeEnum configEnum : ComplaintSuggestionTypeEnum.values()) {
            if (configEnum.getCode().equals(code)) {
                return configEnum.getName();
            }
        }
        return "";
    }
}