package com.cgnpc.bbxpark.ioc.dto.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(value = "物资存放详情模型")
public class StorageDetailModel {
    @ApiModelProperty(value = "存放位置")
    private String spaceName;
    @ApiModelProperty(value = "存放数量")
    private Integer count;
    @ApiModelProperty(value = "单位")
    private String unit;
    @ApiModelProperty(value = "存放开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date startDate;
    @ApiModelProperty(value = "存放结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date endDate;
    @ApiModelProperty(value = "工号")
    private String staffId;
    @ApiModelProperty(value = "名称")
    private String staffName;
    @ApiModelProperty(value = "申请时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date applicationTime;
    @ApiModelProperty(value = "危化品标识(1->是;0->否)")
    private Integer hazardous;
}