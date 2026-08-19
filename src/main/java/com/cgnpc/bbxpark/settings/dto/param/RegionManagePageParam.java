
package com.cgnpc.bbxpark.settings.dto.param;

import com.cgnpc.bbxpark.common.web.CudPageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
/***
 * @value 区域管理员管理分页参数模型
 * @author huangyongtao
 * @date 2025/3/11 11:13
 */
@Data
public class RegionManagePageParam extends CudPageDto implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "区域管理员id.")
    private String regionUid;

    @ApiModelProperty(value = "区域管理员名称.")
    private String regionUname;

    @ApiModelProperty(value = "区域管理员工号.")
    private String regionStaffid;

}
