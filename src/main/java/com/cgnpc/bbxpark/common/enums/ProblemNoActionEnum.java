package com.cgnpc.bbxpark.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @create zhaoshuo
 * @time 2025/4/15
 * @desc 报事报修未处理原因枚举
 */
@Getter
@AllArgsConstructor
public enum ProblemNoActionEnum {
    /**
     * 转工单
     */
    TYPE_1("1","其他人已提交无需处理"),
    /**
     * 无需处理
     */
    TYPE_2("2","该问题较为复杂，将作为专项解决");

    private final String code;
    private final String description;

    public String getCode() {
        return code;
    }
    public String getDescription() {
        return description;
    }

    /**
     * 根据code获取枚举常量
     * @param code 枚举对应的code
     * @return 对应的枚举常量
     * @throws IllegalArgumentException 如果code不存在对应的枚举常量
     */
    public static String fromCode(String code) {
        for (ProblemNoActionEnum type : ProblemNoActionEnum.values()) {
            if (type.getCode().equals(code)) {
                return type.getDescription();
            }
        }
        return "";
    }
}
