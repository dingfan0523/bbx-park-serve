package com.cgnpc.bbxpark.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/***
 * @Description 评优评奖排序规则枚举
 * @author huangyongtao
 * @date 2025/11/12 17:16
 */
@Getter
@AllArgsConstructor
public enum AwardSortRuleEnum {

    /**
     * 按排序
     */
    SORTNUM(1, "按排序"),

    /**
     * 待审核
     */
    PINYIN(2, "按拼音");

    /**
     * 状态编码
     */
    private Integer code;

    /**
     * 状态名称
     */
    private String name;

    public static Integer getCode(String name) {
        for (AwardSortRuleEnum configEnum : AwardSortRuleEnum.values()) {
            if (configEnum.getName().equals(name)) {
                return configEnum.getCode();
            }
        }
        return null;
    }

    public static String getName(Integer code) {
        for (AwardSortRuleEnum configEnum : AwardSortRuleEnum.values()) {
            if (configEnum.getCode().equals(code)) {
                return configEnum.getName();
            }
        }
        return "";
    }
}