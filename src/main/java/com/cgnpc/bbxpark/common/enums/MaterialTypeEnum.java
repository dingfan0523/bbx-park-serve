package com.cgnpc.bbxpark.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/***
 * @Description 材料类型枚举
 * @author huangyongtao
 * @date 2025/9/23 16:46
 */
@Getter
@AllArgsConstructor
public enum MaterialTypeEnum {

    /**
     * 器材
     */
    EQUIPMENT(1, "器材"),

    /**
     * 耗材
     */
    CONSUME_PART( 2, "耗材-备品"),

    /**
     * 耗材
     */
    CONSUME_SPARE( 2, "耗材-备件");

    /**
     * 状态编码
     */
    private Integer code;

    /**
     * 状态名称
     */
    private String name;

    public static Integer getCode(String name) {
        for (MaterialTypeEnum configEnum : MaterialTypeEnum.values()) {
            if (configEnum.getName().equals(name)) {
                return configEnum.getCode();
            }
        }
        return null;
    }

    public static String getName(Integer code) {
        for (MaterialTypeEnum configEnum : MaterialTypeEnum.values()) {
            if (configEnum.getCode().equals(code)) {
                return configEnum.getName();
            }
        }
        return "";
    }
}