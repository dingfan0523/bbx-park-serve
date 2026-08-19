
package com.cgnpc.bbxpark.device.dto.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/***
 * @value 设备分组业务数据模型
 * @author huangyongtao
 * @date 2024/8/12 11:47
 */
@Data
public class DeviceGroupModel implements Serializable {

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

    @ApiModelProperty(value = "父级分组名称.")
    private String parentGroupName;

    @ApiModelProperty(value = "创建人工号.")
    private String creatorId;

    @ApiModelProperty(value = "创建人名称.")
    private String createBy;

    @ApiModelProperty(value = "创建时间.")
    private Date createTime;


}
