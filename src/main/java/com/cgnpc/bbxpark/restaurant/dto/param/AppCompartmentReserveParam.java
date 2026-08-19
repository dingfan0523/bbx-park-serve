
package com.cgnpc.bbxpark.restaurant.dto.param;

import com.cgnpc.bbxpark.common.constant.InsertGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/***
 * @Description 包间预定入参数据模型
 * @author huangyongtao
 * @date 2024/7/30 14:46
 */
@Data
public class AppCompartmentReserveParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "包间id.")
    @NotNull(groups = InsertGroup.class,message = "包间id不能为空")
    private Long compartmentId;

    @ApiModelProperty(value = "套餐id.")
    private Long comboId;

    @ApiModelProperty(value = "预定开始时间.")
    @NotNull(groups = InsertGroup.class,message = "预定时间不能为空")
    private Date reserveStartTime;

    @ApiModelProperty(value = "预定结束时间.")
    @NotNull(groups = InsertGroup.class,message = "预定时间不能为空")
    private Date reserveEndTime;
}
