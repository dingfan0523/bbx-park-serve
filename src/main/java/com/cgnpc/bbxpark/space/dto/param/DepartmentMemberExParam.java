package com.cgnpc.bbxpark.space.dto.param;

import com.cgnpc.bbxpark.common.web.CudPageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author dingfan
 * @version 1.0
 * @date 2025/9/24 10:46
 */
@Data
public class DepartmentMemberExParam extends CudPageDto implements Serializable {
    @ApiModelProperty(value = "部门标识.")
    private String orgId;
    @ApiModelProperty(value = "关键词")
    private String keyword;
    @ApiModelProperty(value = "空间id")
    private Integer spaceId;
}
