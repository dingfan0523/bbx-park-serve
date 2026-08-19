
package com.cgnpc.bbxpark.restaurant.dto.param;

import com.cgnpc.bbxpark.common.web.CudPageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/***
 * @Description 包间预定分页参数模型
 * @author huangyongtao
 * @date 2024/7/30 14:45
 */
@Data
public class AppCompartmentReservePageParam extends CudPageDto implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "预定开始时间.")
    private Date reserveStartTime;
    @ApiModelProperty(value = "预定结束时间.")
    private Date reserveEndTime;
}
