package com.cgnpc.bbxpark.ioc.dto.param;

import com.cgnpc.bbxpark.common.web.CudPageDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@ApiModel(value = "物资存放筛选参数")
public class StoragePageParam extends CudPageDto implements Serializable {
    @ApiModelProperty(value = "起始日期")
    private Date startDate;
    @ApiModelProperty(value = "截止日期")
    private Date endDate;
    @ApiModelProperty(value = "空间id")
    private Long spaceId;
    @ApiModelProperty(value = "空间模型编码")
    private String sslcCode;
}

