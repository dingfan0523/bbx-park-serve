package com.cgnpc.bbxpark.traffic.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 车辆配件更换记录表实体类
 * 对应数据库表: MATRIX.dwd_vehicle_part_replace
 */
@Data
@TableName("dwd_vehicle_part_replace")
public class DwdVehiclePartReplace implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键ID（建议使用SYS_GUID()或应用生成） */
    @TableId(value = "id",type = IdType.ID_WORKER_STR)
    private String id;

    /** 车牌号 */
    private String plateNum;

    /** 车辆单位 */
    private String vehicleDept;

    /** 申请日期 */
    private Date applyDate;

    /** 公里数(千米) */
    private Double mileage;

    /** 车型 */
    private String vehicleType;

    /** 更换数量(个) */
    private Integer replaceCount;

    /** 申报人 */
    private String reporter;

    /** 更换日期 */
    private Date replaceDate;

    /** 车管签名 */
    private String vehicleAdminSign;

    /** 更换原因 */
    private String replaceReason;

    /** 送车人 */
    private String carDeliverer;

    /** 流程状态 */
    private String processStatus;

    /** 当前处理人工号 */
    private String currentHandlerId;

    /** 备注 */
    private String remark;

    /** 数据导入时间 */
    private Date importTime;

    /** 文件id */
    private Long fileId;

}
