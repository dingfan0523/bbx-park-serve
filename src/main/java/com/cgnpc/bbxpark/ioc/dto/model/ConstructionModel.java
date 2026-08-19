package com.cgnpc.bbxpark.ioc.dto.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(value = "安全管理列表模型")
public class ConstructionModel {
    @ApiModelProperty(value = "id")
    private Long id;
    @ApiModelProperty(value = "空间id")
    private Long spaceId;
    @ApiModelProperty(value = "空间编码")
    private String sslcCode;
    @ApiModelProperty(value = "空间名称")
    private String spaceName;
    @ApiModelProperty(value = "施工说明")
    private String description;
    @ApiModelProperty(value = "施工开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date startDate;
    @ApiModelProperty(value = "施工结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date endDate;
    @ApiModelProperty(value = "申请人工号")
    private String staffId;
    @ApiModelProperty(value = "申请人名称")
    private String staffName;
    @ApiModelProperty(value = "申请时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date applicationTime;
}