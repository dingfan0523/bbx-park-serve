package com.cgnpc.bbxpark.common.enums;

import com.cgnpc.bbxpark.common.constant.Constants;

import java.util.Arrays;
import java.util.List;

/**
 * 状态枚举
 */
public enum Status {
    /**
     * 表示启用.
     */
    enabled(Constants.STATUS_ENABLED, Constants.STATUS_ENABLED_CN),
    /**
     * 表示禁用.
     */
    disabled(Constants.STATUS_DISABLED, Constants.STATUS_DISABLED_CN),
    /**
     * 表示注销.
     */
    deletion(Constants.STATUS_DELETION, Constants.STATUS_DISABLED_CN);

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
    Status(Integer ind, String str) {

        this.key = ind;
        this.value = str;
    }

    /**
     *
     * 根据关键字返回对应枚举类型.
     *
     * @param key 对应的关键字
     * @return 返回对应的枚举类型
     */
    public static Status getEnumByKey(int key) {

        switch (key) {

            case Constants.STATUS_ENABLED:
                return enabled;
            case Constants.STATUS_DISABLED:
                return disabled;
            case Constants.STATUS_DELETION:
                return deletion;
            default:
                return null;

        }
    }

    /**
     *
     * 根据value返回对应枚举类型.
     *
     * @param value 对应的是页面显示值
     * @return 返回对应的枚举类型
     */
    public static Status getEnumByString(String value) {

        switch (value) {

            case Constants.STATUS_ENABLED_CN:
                return enabled;
            case Constants.STATUS_DISABLED_CN:
                return disabled;
            case Constants.STATUS_DELETION_CN:
                return deletion;
            default:
                return null;

        }
    }

    /**
     * 枚举类型返回列表.
     *
     * @return 返回一个List表提供数据字典使用。
     */
    public static List<Status> getArrayToList() {

        return Arrays.asList(Status.values());

    }
}
