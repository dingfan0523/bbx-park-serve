package com.cgnpc.bbxpark.restaurant.dto.param;

import com.cgnpc.bbxpark.common.web.CudPageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


/**
 * <p>
 * ComboParam
 * </p>
 *
 * @author gujun
 * @time 2024-07-22
 */
@Data
public class ComboListParam extends CudPageDto implements Serializable {


    @ApiModelProperty(value = "套餐名称")
    private String name ;

    @ApiModelProperty(value = "套餐类型(字典)")
    private String type ;

    @ApiModelProperty(value = "状态(1->上架;0->下架)" )
    private Integer status ;


}
