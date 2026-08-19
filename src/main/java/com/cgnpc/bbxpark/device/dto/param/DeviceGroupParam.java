
package com.cgnpc.bbxpark.device.dto.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
/***
 * @value 设备分组入参数据模型
 * @author huangyongtao
 * @date 2024/8/12 11:53
 */
@Data
public class DeviceGroupParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id.")
    private Long id;

    @NotBlank(message = "分组编码不能为空")
    @Length(max = 50)
    @ApiModelProperty(value = "分组编码.")
    private String groupCode;

    @NotBlank(message = "分组名称不能为空")
    @Length(max = 100)
    @ApiModelProperty(value = "分组名称.")
    private String groupName;

    @ApiModelProperty(value = "排序序号.")
    private Integer sortOrder;

    @ApiModelProperty(value = "分组父级id.")
    private Long groupParentId;

    @Length(max = 255)
    @ApiModelProperty(value = "分组描述.")
    private String groupDescribe;

    @ApiModelProperty(value = "租户号.")
    private Long tenantId;

}
