package com.cgnpc.bbxpark.restaurant.dto.param;

import com.cgnpc.bbxpark.common.constant.UpdateGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
public class RestaurantNotificationParam implements Serializable {

    @ApiModelProperty(value = "id.")
    private Long id;


    @NotBlank(groups = UpdateGroup.class, message = "通知内容不能为空")
    @ApiModelProperty(value = "通知.")
    private String notification;


}
