package com.cgnpc.bbxpark.ioc.dto.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@ApiModel(value = "空间列表模型")
public class SpaceListModel {
    @ApiModelProperty(value = "空间id")
    private Long spaceId;
    @ApiModelProperty(value = "空间类型")
    private Integer type;
    @ApiModelProperty(value = "空间名称")
    private String spaceName;
    @ApiModelProperty(value = "模型编码")
    private String sslcCode;
    @ApiModelProperty(value = "空间图片集合")
    private List<String> spaceImages;
    @ApiModelProperty(value = "面积")
    private BigDecimal area;
    @ApiModelProperty(value = "使用部门")
    private String useDept;
    @ApiModelProperty(value = "人员密度")
    private Long personDensity;
    @ApiModelProperty(value = "设备数量")
    private Integer deviceCount;
    @ApiModelProperty(value = "施工id集合")
    private List<Long> constructionIds;
    @ApiModelProperty(value = "危化品id集合")
    private List<Long> hazardousIds;
    @ApiModelProperty(value = "工单id集合")
    private List<Long> workOrderIds;
}