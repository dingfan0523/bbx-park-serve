package com.cgnpc.bbxpark.common.enums;

/**
 * 文件类型枚举
 * @author dingfan
 * @version 1.0
 * @date 2024/9/24 10:39
 */
public enum FileTypeEnum {
    /**
     * 智慧会议
     */
    MEETING(1, "智慧会议"),

    /**
     * 会服
     */
    ATTENDANT(2, "会服"),

    /**
     * 产品
     */
    PRODUCT(3, "ioc产品"),

    /**
     * 智慧会议
     */
    DEVICE(4, "ioc设备"),
    /**
     * 报事报修
     */
    PROBLEMREPORT(5, "报事报修"),

    /**
     * 工单抄表设备
     */
    WORKPLANDEVICE(6, "工单抄表设备"),

    /**
     * 工单任务
     */
    WORKPLANTASK(7, "工单任务"),

    /**
     * 工单任务项
     */
    WORKPLANTASKITEM(8, "工单任务项"),

    /**
     * 一卡通消费信息
     */
    RESTAURANTCARD(31, "一卡通消费信息"),

    /**
     * 餐厅垃圾处理信息
     */
    RESTAURANTWASTE(32, "餐厅垃圾处理信息"),

    /**
     * 入库餐料信息
     */
    RESTAURANTINBOUND(33, "入库餐料信息"),

    /**
     * 餐料库存信息
     */
    RESTAURANTINVENTORY(34, "餐料库存信息"),

    /**
     * 车辆表
     */
    DWDVEHICLEINFO(41, "车辆表"),

    /**
     * 车辆保养信息表
     */
    DWDVEHICLEMAINTAIN(42, "车辆保养信息表"),

    /**
     * 轮胎更换记录表
     */
    DWDVEHICLEPARTREPLACE(43, "轮胎更换记录表"),

    /**
     * 车辆维修信息表
     */
    DWDVEHICLEREPAIR(44, "车辆维修信息表"),

    /**
     * 租车记录表
     */
    DWDVEHICLECARRENTAPPLY(45, "租车记录表"),

    /**
     * 司机表
     */
    DWDVEHICLEDRIVERINFO(46, "司机表"),

    /**
     * 车辆行驶记录表
     */
    DWDVEHICLECARTASKRECORD(47, "车辆行驶记录表"),

    /**
     * 电召车出车记录
     */
    DWDVEHICLEAPPLY(48, "电召车出车记录"),

    /**
     * 便民班车订单表
     */
    DWDVEHICLELINEORDERINFO(49, "便民班车订单表"),

    /**
     * 车辆费用结算表
     */
    DWDVEHICLEMONTHLYSETTLEMENT(50, "车辆费用结算表");


    /**
     * 状态编码
     */
    private Integer value;

    /**
     * 状态名称
     */
    private String name;

    FileTypeEnum(Integer value,String name){
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
