package com.cgnpc.bbxpark.ioc.dto.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 大屏餐厅后厨信息
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RestaurantKitchenStaffTempModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "总人数.")
    private String totalStaff;

    @ApiModelProperty(value = "男性数量.")
    private String maleNum;

    @ApiModelProperty(value = "女性数量.")
    private String femaleNum;

    @ApiModelProperty(value = "有效体检数量.")
    private String healthCheckupNum;

    @ApiModelProperty(value = "人员培训数量.")
    private String trainingNum;
    
    @ApiModelProperty(value = "健康证到期数量.")
    private String expiringCertNum;

    @ApiModelProperty(value = "30岁以下.")
    private String ageUnder30;

    @ApiModelProperty(value = "30-40岁.")
    private String age30To40;

    @ApiModelProperty(value = "40-50岁.")
    private String age40To50;

    @ApiModelProperty(value = "50岁以上.")
    private String ageOver50;
}

