
package com.cgnpc.bbxpark.restaurant.dto.param;

import com.cgnpc.bbxpark.common.web.CudPageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
import java.io.Serializable;

@Data
public class CompartmentEvaluatePageParam extends CudPageDto implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -4325779567277753700L;


    @ApiModelProperty(value = "包间id.")
    @NotNull(groups = Default.class,message = "包间id不能为空")
    private Long compartmentId;
}
