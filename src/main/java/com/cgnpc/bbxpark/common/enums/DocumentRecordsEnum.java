package com.cgnpc.bbxpark.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/***
 * @Description 文档查看类型
 * @author lhy
 * @date 2024/07/15 17:41
 */
@Getter
@AllArgsConstructor
public enum DocumentRecordsEnum {

    /**
     * 用户隐私协议
     */
    USERPRIVACYAGREE("userPrivacyAgree", "用户隐私协议");

    /**
     * 状态编码
     */
    private String code;

    /**
     * 状态名称
     */
    private String name;

    public static String getCode(String name) {
        for (DocumentRecordsEnum configEnum : DocumentRecordsEnum.values()) {
            if (configEnum.getName().equals(name)) {
                return configEnum.getCode();
            }
        }
        return "0";
    }

    public static String getName(Byte code) {
        for (DocumentRecordsEnum configEnum : DocumentRecordsEnum.values()) {
            if (configEnum.getCode().equals(code)) {
                return configEnum.getName();
            }
        }
        return String.valueOf(code);
    }
}