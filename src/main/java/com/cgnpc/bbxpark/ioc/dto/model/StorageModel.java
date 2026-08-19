package com.cgnpc.bbxpark.ioc.dto.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(value = "安全管理列表模型")
public class StorageModel {
    @ApiModelProperty(value = "id")
    private Long id;
    @ApiModelProperty(value = "空间id")
    private Long spaceId;
    @ApiModelProperty(value = "空间编码")
    private String sslcCode;
    @ApiModelProperty(value = "空间名称")
    private String spaceName;
    @ApiModelProperty(value = "物资类型")
    private String type;
    @ApiModelProperty(value = "数量")
    private Integer count;
    @ApiModelProperty(value = "单位")
    private String unit;
    @ApiModelProperty(value = "危化品标识(1->是;0->否)")
    private Integer hazardous;
    @ApiModelProperty(value = "存放开始时间")
    private Date startDate;
    @ApiModelProperty(value = "存放结束时间")
    private Date endDate;
    @ApiModelProperty(value = "存放人工号")
    private String staffId;
    @ApiModelProperty(value = "存放人名称")
    private String staffName;
    @ApiModelProperty(value = "申请时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date applicationTime;
}