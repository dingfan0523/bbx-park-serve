package com.cgnpc.bbxpark.ioc.dto.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@ApiModel(value = "空间图片模型")
public class SpaceImageModel implements Serializable {
    private static final long serialVersionUID = -4436381185014831233L;
    
    @ApiModelProperty(value = "id")
    private Long id;
    
    @ApiModelProperty(value = "图片类型")
    private Integer type;
    
    @ApiModelProperty(value = "图片集合")
    private List<String> imageList;
    
    @ApiModelProperty(value = "备注")
    private String remark;
    
    @ApiModelProperty(value = "更新人姓名")
    private String userName;
    
    @ApiModelProperty(value = "更新人工号")
    private String staffId;
    
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;
}