package com.cgnpc.bbxpark.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/***
 * @Description 材料记录类型枚举
 * @author huangyongtao
 * @date 2025/9/23 16:46
 */
@Getter
@AllArgsConstructor
public enum MaterialRecordTypeEnum {

    /**
     * 入库
     */
    IN(1, "入库"),

    /**
     * 出库
     */
    OUT( 2, "出库");

    /**
     * 状态编码
     */
    private Integer code;

    /**
     * 状态名称
     */
    private String name;

    public static Integer getCode(String name) {
        for (MaterialRecordTypeEnum configEnum : MaterialRecordTypeEnum.values()) {
            if (configEnum.getName().equals(name)) {
                return configEnum.getCode();
            }
        }
        return null;
    }

    public static String getName(Integer code) {
        for (MaterialRecordTypeEnum configEnum : MaterialRecordTypeEnum.values()) {
            if (configEnum.getCode().equals(code)) {
                return configEnum.getName();
            }
        }
        return "";
    }
}