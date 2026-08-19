package com.cgnpc.bbxpark.traffic.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 车辆行驶记录表实体类
 * 对应数据库表: MATRIX.dwd_vehicle_car_task_record
 */
@Data
@TableName("dwd_vehicle_car_task_record")
public class DwdVehicleCarTaskRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @TableId(value = "id",type = IdType.ID_WORKER_STR)
    private String id;

    /** 车辆单位 */
    private String carCompany;

    /** 车牌号 */
    private String carPlate;

    /** 驾驶员 */
    private String driverName;

    /** 任务类型 */
    private String taskType;

    /** 任务情况 */
    private String taskDetail;

    /** 出车时间 */
    private Date departTime;

    /** 出车前公里数（KM） */
    private String beforeMileage;

    /** 收车时间 */
    private Date returnTime;

    /** 收车后公里数（KM） */
    private String afterMileage;

    /** 单趟行驶里程 */
    private Double singleMileage;

    /** 用车单位 */
    private String useDept;

    /** 用车人 */
    private String carUser;

    /** 数据导入时间 */
    private Date importTime;

    /** 文件id */
    private Long fileId;

}
