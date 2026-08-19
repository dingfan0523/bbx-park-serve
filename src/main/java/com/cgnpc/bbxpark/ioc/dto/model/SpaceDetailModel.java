package com.cgnpc.bbxpark.ioc.dto.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@ApiModel(value = "空间详情模型")
public class SpaceDetailModel implements Serializable {
    private static final long serialVersionUID = -3238461757583605592L;
    
    @ApiModelProperty(value = "id")
    private Long id;
    
    @ApiModelProperty(value = "空间名称")
    private String spaceName;
    
    @ApiModelProperty(value = "空间类型")
    private Integer type;
    
    @ApiModelProperty(value = "使用单位名称")
    private String departmentName;
    
    @ApiModelProperty(value = "面积（㎡）")
    private BigDecimal area;
    
    @ApiModelProperty(value = "空间容量（人）")
    private Integer capacity;
    
    @ApiModelProperty(value = "空间用途")
    private String purpose;
    
    @ApiModelProperty(value = "工位管理员名称")
    private String managerName;
    
    @ApiModelProperty(value = "工位管理员工号")
    private String managerStaffId;
    
    @ApiModelProperty(value = "已分配工位")
    private Integer allocatedStations;
    
    @ApiModelProperty(value = "空闲工位")
    private Integer freeStations;
}