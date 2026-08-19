package com.cgnpc.bbxpark.common.enums;

/**
 * 删除枚举
 */
public enum Delete {
    /**
     * 表示启用.
     */
    NORMAL(1, "正常"),
    /**
     * 表示禁用.
     */
    DELETED(0, "删除");

    /**
     * 对应存入数据库中的标识。
     */
    private final Integer key;
    /**
     * 对应界面展现的值.
     */
    private final String value;

    public String getValue() {
        return value;
    }

    public Integer getKey() {
        return key;
    }

    /**
     *
     * 私有造成函数.
     *
     * @param ind int类型 对应Key的值
     * @param str 字符串类型对应str的值
     */
    Delete(Integer ind, String str) {

        this.key = ind;
        this.value = str;
    }
}
