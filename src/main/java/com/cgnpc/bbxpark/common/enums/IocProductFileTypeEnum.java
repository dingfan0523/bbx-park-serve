package com.cgnpc.bbxpark.common.enums;

/**
 * @create zhaoshuo
 * @time 2025/2/25
 * @desc ioc产品文件类型
 */
public enum IocProductFileTypeEnum {
    /**
     * 产品文件
     */
    PRODUCT(1, "产品"),

    /**
     * 设备文件
     */
    DEVICE(2, "设备");

    /**
     * 状态编码
     */
    private Integer value;

    /**
     * 状态名称
     */
    private String name;

    IocProductFileTypeEnum(Integer value,String name){
        this.value = value;
        this.name = name;
    }

    public Integer getValue() {
        return value;
    }

    public String getName() {
        return name;
    }
}
