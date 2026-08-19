package com.cgnpc.bbxpark.ioc.dto.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(value = "车辆行驶记录")
public class CarTravelRecordModel {
    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "车辆单位")
    private String carCompany;

    @ApiModelProperty(value = "车牌号")
    private String carPlate;

    @ApiModelProperty(value = "驾驶员")
    private String driverName;

    @ApiModelProperty(value = "出车时间")
    private Date departTime;

    @ApiModelProperty(value = "收车时间")
    private Date returnTime;

    @ApiModelProperty(value = "单趟行驶里程")
    private Double singleMileage;
}
