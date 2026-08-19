package com.cgnpc.bbxpark.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/***
 * @Description 评优评奖状态枚举
 * @author huangyongtao
 * @date 2025/11/12 17:16
 */
@Getter
@AllArgsConstructor
public enum AwardStatusEnum {

    /**
     * 待提交
     */
    SUBMIT(10, "待提交"),

    /**
     * 待审核
     */
    AUDIT(20, "待审核"),
    /**
     * 待展示
     */
    DISPLAY( 30, "待展示"),
    /**
     * 展示中
     */
    ONDISPLAY( 40, "展示中"),
    /**
     * 展示结束
     */
    ENDDISPLAY( 50, "展示结束");

    /**
     * 状态编码
     */
    private Integer code;

    /**
     * 状态名称
     */
    private String name;

    public static Integer getCode(String name) {
        for (AwardStatusEnum configEnum : AwardStatusEnum.values()) {
            if (configEnum.getName().equals(name)) {
                return configEnum.getCode();
            }
        }
        return null;
    }

    public static String getName(Integer code) {
        for (AwardStatusEnum configEnum : AwardStatusEnum.values()) {
            if (configEnum.getCode().equals(code)) {
                return configEnum.getName();
            }
        }
        return "";
    }
}