package com.cgnpc.bbxpark.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/***
 * @Description 材料状态枚举
 * @author huangyongtao
 * @date 2025/9/23 16:46
 */
@Getter
@AllArgsConstructor
public enum MaterialStatusEnum {

    /**
     * 库存充足
     */
    ENOUGH(1, "库存充足"),

    /**
     * 库存不足
     */
    SHORTAGE(2, "库存不足"),

    /**
     * 缺货
     */
    NO( 3, "缺货");

    /**
     * 状态编码
     */
    private Integer code;

    /**
     * 状态名称
     */
    private String name;

    public static Integer getCode(String name) {
        for (MaterialStatusEnum configEnum : MaterialStatusEnum.values()) {
            if (configEnum.getName().equals(name)) {
                return configEnum.getCode();
            }
        }
        return null;
    }

    public static String getName(Integer code) {
        for (MaterialStatusEnum configEnum : MaterialStatusEnum.values()) {
            if (configEnum.getCode().equals(code)) {
                return configEnum.getName();
            }
        }
        return "";
    }
}