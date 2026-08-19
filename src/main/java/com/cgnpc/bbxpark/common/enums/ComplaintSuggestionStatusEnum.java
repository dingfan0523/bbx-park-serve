package com.cgnpc.bbxpark.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/***
 * @Description 投诉建议状态枚举
 * @author huangyongtao
 * @date 2024/7/15 11:57
 */
@Getter
@AllArgsConstructor
public enum ComplaintSuggestionStatusEnum {

    /**
     * 待回复
     */
    REPLY(10, "待回复"),

    /**
     * 已分配（待处理）
     */
    ASSIGNMENT( 20, "已分配（待处理）"),

    /**
     * 待审核
     */
    AUDIT( 30, "待审核"),

    /**
     * 审核驳回
     */
    AUDIT_REJECT( 40, "审核驳回"),

    /**
     * 已完成
     */
    COMPLATE( 50, "已完成"),

    /**
     * 已评价
     */
    COMMENT( 60, "已评价");

    /**
     * 状态编码
     */
    private Integer code;

    /**
     * 状态名称
     */
    private String name;

    public static Integer getCode(String name) {
        for (ComplaintSuggestionStatusEnum configEnum : ComplaintSuggestionStatusEnum.values()) {
            if (configEnum.getName().equals(name)) {
                return configEnum.getCode();
            }
        }
        return null;
    }

    public static String getName(Integer code) {
        for (ComplaintSuggestionStatusEnum configEnum : ComplaintSuggestionStatusEnum.values()) {
            if (configEnum.getCode().equals(code)) {
                return configEnum.getName();
            }
        }
        return "";
    }
}