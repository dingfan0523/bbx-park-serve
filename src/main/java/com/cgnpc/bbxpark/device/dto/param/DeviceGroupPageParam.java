
package com.cgnpc.bbxpark.device.dto.param;

import com.cgnpc.bbxpark.common.web.CudPageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
/***
 * @value 设备分组分页参数模型
 * @author huangyongtao
 * @date 2024/8/12 11:49
 */
@Data
public class DeviceGroupPageParam extends CudPageDto implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id.")
    private Long id;

    @ApiModelProperty(value = "分组编码.")
    private String groupCode;

    @ApiModelProperty(value = "分组名称.")
    private String groupName;

    @ApiModelProperty(value = "排序序号.")
    private Integer sortOrder;

    @ApiModelProperty(value = "分组父级id.")
    private Long groupParentId;

    @ApiModelProperty(value = "分组描述.")
    private String groupDescribe;

    @ApiModelProperty(value = "租户号.")
    private Long tenantId;

}
