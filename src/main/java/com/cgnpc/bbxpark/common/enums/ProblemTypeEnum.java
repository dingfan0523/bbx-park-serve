package com.cgnpc.bbxpark.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @create zhaoshuo
 * @time 2025/4/22
 * @desc 报事报修类型枚举
 */
@Getter
@AllArgsConstructor
public enum ProblemTypeEnum {
    PERSION(1, "报事报修"),
    ENERGY_ABNORMAL(2, "能耗异常");

    private final int code;
    private final String description;


    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static String getName(int code) {
        for (ProblemTypeEnum status : ProblemTypeEnum.values()) {
            if (status.getCode() == code) {
                return status.getDescription();
            }
        }
        throw new IllegalArgumentException("未知code: " + code);
    }
}
