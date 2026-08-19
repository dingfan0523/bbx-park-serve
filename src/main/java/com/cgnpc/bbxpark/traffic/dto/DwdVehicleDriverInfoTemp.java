package com.cgnpc.bbxpark.traffic.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 司机表临时模型
 */
@Data
public class DwdVehicleDriverInfoTemp implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private String id;

    /** 驾驶员 */
    private String driver_name;

    /** 驾驶员班组 */
    private String driver_team;

    /** 电话 */
    private String phone;

    /** 性别 */
    private String gender;

    /** 年龄 */
    private String age;

    /** 工作时间 */
    private String work_hours;

    /** 入职时间 */
    private String entry_time;

    /** 广核工龄 */
    private String gnh_work_years;

    /** 国家工龄 */
    private String nation_work_years;

    /** 工作状态 */
    private String work_status;

    /** 驾驶证号 */
    private String driver_license_no;

    /** 驾驶证有效结束日期 */
    private String license_expire_time;

    /** 数据导入时间 */
    private String import_time;

}
