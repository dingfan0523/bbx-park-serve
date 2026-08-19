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
public enum AwardRomanStatusEnum {

    /**
     * 提交
     */
    SUBMIT(1, "提交"),

    /**
     * 撤回
     */
    RECALL(2, "撤回"),
    /**
     * 审批
     */
    AUDIT( 3, "审批"),
    /**
     * 信息展示
     */
    ONDISPLAY( 4, "信息展示"),
    /**
     * 展示结束
     */
    ENDDISPLAY( 5, "展示结束");

    /**
     * 状态编码
     */
    private Integer code;

    /**
     * 状态名称
     */
    private String name;

    public static Integer getCode(String name) {
        for (AwardRomanStatusEnum configEnum : AwardRomanStatusEnum.values()) {
            if (configEnum.getName().equals(name)) {
                return configEnum.getCode();
            }
        }
        return null;
    }

    public static String getName(Integer code) {
        for (AwardRomanStatusEnum configEnum : AwardRomanStatusEnum.values()) {
            if (configEnum.getCode().equals(code)) {
                return configEnum.getName();
            }
        }
        return "";
    }
}