
package com.cgnpc.bbxpark.restaurant.dto.param;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
import java.io.Serializable;
import java.util.Date;

/***
 * @Description 包间预定时间列表参数模型
 * @author huangyongtao
 * @date 2024/7/30 14:44
 */
@Data
public class AppCompartmentReserveTimeListParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "包间id")
    @NotNull(groups = Default.class,message = "包间id不能为空")
    private Long compartmentId;

    @ApiModelProperty(value = "日期")
    @NotNull(groups = Default.class,message = "日期不能为空")
    private Date reserveDate;
}
