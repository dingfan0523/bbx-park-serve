
package com.cgnpc.bbxpark.ioc.dto.param;


import com.cgnpc.bbxpark.common.web.CudPageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 工单评价页参数模型
 */
@Data
public class WorkUnsatisfiedPageParam extends CudPageDto implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "工单来源.")
    private String source;

    @ApiModelProperty(value = "工单来源集合.")
    private List<String> sourceList;

    @ApiModelProperty(value = "部门id.")
    private String departmentId;

    @ApiModelProperty(value = "租户id")
    private Long tenantId;
}
