package com.cgnpc.bbxpark.ioc.dto.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(value = "施工详情模型")
public class ConstructionDetailModel {
    @ApiModelProperty(value = "施工位置")
    private String spaceName;
    @ApiModelProperty(value = "施工开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date startDate;
    @ApiModelProperty(value = "施工结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date endDate;
    @ApiModelProperty(value = "施工说明")
    private String description;
    @ApiModelProperty(value = "工号")
    private String staffId;
    @ApiModelProperty(value = "名称")
    private String staffName;
    @ApiModelProperty(value = "申请时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date applicationTime;
}