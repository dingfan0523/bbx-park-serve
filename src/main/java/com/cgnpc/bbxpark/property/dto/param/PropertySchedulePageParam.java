
package com.cgnpc.bbxpark.property.dto.param;

import com.cgnpc.bbxpark.common.web.CudPageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 物业排班分页参数模型
 */
@Data
public class PropertySchedulePageParam extends CudPageDto implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "分组名称.")
    private String name;

}
