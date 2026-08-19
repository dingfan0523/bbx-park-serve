
package com.cgnpc.bbxpark.device.dto.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/***
 * @value 设备分组树形结构
 * @author huangyongtao
 * @date 2024/8/12 11:47
 */
@Data
public class DeviceGroupTreeModel implements Serializable {

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

    @ApiModelProperty(value = "层级")
    private Long level;

    @ApiModelProperty(value = "子级集合")
    private List<DeviceGroupTreeModel> children;


}
