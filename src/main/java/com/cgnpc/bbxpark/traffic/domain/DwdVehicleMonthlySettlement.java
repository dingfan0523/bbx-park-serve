package com.cgnpc.bbxpark.traffic.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 车辆费用结算表实体类
 */
@Data
@TableName("dwd_vehicle_monthly_settlement")
public class DwdVehicleMonthlySettlement implements Serializable {

    private static final long serialVersionUID = 1L;

    /** id */
    @TableId(value = "id",type = IdType.ID_WORKER_STR)
    private String id;

    /** 车号 */
    private String carPlate;

    /** 车辆型号 */
    private String vehicleModel;

    /** 购买日期 */
    private Date purchaseDate;

    /** 月初公里数 */
    private Double startMonthMileage;

    /** 月末公里数 */
    private Double endMonthMileage;

    /** 月行驶公里数 */
    private Double monthMileage;

    /** 包月公里数 */
    private Double monthlyPackageMileage;

    /** 月租费（元） */
    private Double monthlyRent;

    /** 当月15日油价 */
    private Double oilPrice;

    /** 油耗 */
    private Double fuelConsumption;

    /** 燃油补差 */
    private Double fuelSubsidy;

    /** 住宿费 */
    private Double accommodationFee;

    /** 差旅补贴 */
    private Double travelAllowance;

    /** 物资配置费 */
    private Double materialConfigFee;

    /** 停车费 */
    private Double parkingFee;

    /** 路桥费 */
    private Double tollFee;

    /** 路桥停车住宿税费 */
    private Double tollParkingAccommodationTax;

    /**
     * 数据导入时间
     */
    private Date importTime;

    /** 文件id */
    private Long fileId;

    /**
     * 结算日期
     */
    private Date settlementDate;
}
