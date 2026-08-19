package com.cgnpc.bbxpark.restaurant.dto.param;

import com.cgnpc.bbxpark.common.constant.InsertGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 包间取消预定入参数据模型
 * @author dingfan
 * @date 2024/8/2 15:33
 */
@Data
public class AppCompartmentReserveCancelParam implements Serializable {
    @ApiModelProperty(value = "预定id.")
    @NotNull(groups = InsertGroup.class,message = "预定id不能为空")
    private Long id;
    @ApiModelProperty(value = "取消原因.")
    @NotEmpty(groups = InsertGroup.class,message = "取消原因不能为空")
    private String cancelReason;
    @ApiModelProperty(value = "备注.")
    private String cancelRemark;
}
