package com.cgnpc.bbxpark.device.dto.param;

import com.cgnpc.bbxpark.common.web.CudPageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @create zhaoshuo
 * @time 2025/2/24
 * @desc ioc产品分页查询参数
 */
@Data
public class IocProductPageParam extends CudPageDto {

    @ApiModelProperty(value = "产品名称")
    private String pdName;

    @ApiModelProperty(value = "是否删除")
    private Integer deleted;
}
