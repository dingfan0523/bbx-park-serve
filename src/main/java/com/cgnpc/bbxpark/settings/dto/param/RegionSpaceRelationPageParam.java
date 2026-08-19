
package com.cgnpc.bbxpark.settings.dto.param;

import com.cgnpc.bbxpark.common.web.CudPageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
/***
 * @value 区域管理员空间关联分页参数模型
 * @author huangyongtao
 * @date 2025/3/11 11:14
 */
@Data
public class RegionSpaceRelationPageParam extends CudPageDto implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "区域管理id.")
    private Long regionId;

    @ApiModelProperty(value = "空间id.")
    private Long spaceId;

}
