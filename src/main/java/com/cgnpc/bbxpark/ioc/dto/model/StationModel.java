package com.cgnpc.bbxpark.ioc.dto.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;

@Data
@ApiModel(value = "工位人员模型")
public class StationModel implements Serializable {
    private static final long serialVersionUID = -4557652080190166177L;
    
    @ApiModelProperty(value = "id")
    private Long id;
    
    @ApiModelProperty(value = "人员名称")
    private String userName;
    
    @ApiModelProperty(value = "人员工号")
    private String staffId;
    
    @ApiModelProperty(value = "部门名称")
    private String departmentName;
    
    @ApiModelProperty(value = "分配人名称")
    private String assignerName;
    
    @ApiModelProperty(value = "分配人工号")
    private String assignerStaffId;
    
    @ApiModelProperty(value = "分配时间")
    private Date createTime;
}