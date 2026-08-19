package com.cgnpc.bbxpark.restaurant.dto.param;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

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
public class ComboParam implements Serializable {

    @ApiModelProperty(value = "id")
    private Long id ;

    @ApiModelProperty(value = "套餐名称")
    @Length(max = 30,message = "长度不超过30")
    private String name ;

    @ApiModelProperty(value = "套餐类型(字典)")
    private String type ;

    @ApiModelProperty(value = "套餐价格")
    private Double price ;

    @ApiModelProperty(value = "套餐描述")
    @Length(max = 200,message = "长度不超过200")
    private String description ;

}
