package com.cgnpc.bbxpark.ioc.dto.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 大屏餐厅后厨信息
 */
@Data
public class RestaurantKitchenStaffModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "总人数.")
    private Integer totalStaff;

    @ApiModelProperty(value = "男性数量.")
    private Integer maleNum;

    @ApiModelProperty(value = "男性比例.")
    private Double maleRate;

    @ApiModelProperty(value = "女性数量.")
    private Integer femaleNum;

    @ApiModelProperty(value = "女性比例.")
    private Double femaleRate;

    @ApiModelProperty(value = "有效体检数量.")
    private Integer healthCheckupNum;

    @ApiModelProperty(value = "有效体检率.")
    private Double healthCheckupRate;

    @ApiModelProperty(value = "人员培训数量.")
    private Integer trainingNum;

    @ApiModelProperty(value = "人员培训率.")
    private Double trainingRate;

    @ApiModelProperty(value = "健康证到期数量.")
    private Integer expiringCertNum;

    @ApiModelProperty(value = "健康证到期率.")
    private Double expiringCertRate;

    @ApiModelProperty(value = "30岁以下.")
    private Integer ageUnder30;

    @ApiModelProperty(value = "30-40岁.")
    private Integer age30To40;

    @ApiModelProperty(value = "40-50岁.")
    private Integer age40To50;

    @ApiModelProperty(value = "50岁以上.")
    private Integer ageOver50;

    @ApiModelProperty(value = "警戒线.")
    private Integer alarmLine;
}

