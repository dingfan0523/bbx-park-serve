package com.cgnpc.bbxpark.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @create zhaoshuo
 * @time 2025/4/23
 * @desc 支路类型枚举
 */
@Getter
@AllArgsConstructor
public enum BranchEnergyTypeEnum {
    ELECTRICITY("electricity", "电（KW·H）"),
    WATER("water", "水（m³）"),
    GAS("gas", "燃气（m³）");

    private final String code;
    private final String description;


    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static String getName(String code) {
        for (BranchEnergyTypeEnum status : BranchEnergyTypeEnum.values()) {
            if (status.getCode().equals(code)) {
                return status.getDescription();
            }
        }
        throw new IllegalArgumentException("未知code: " + code);
    }
}
