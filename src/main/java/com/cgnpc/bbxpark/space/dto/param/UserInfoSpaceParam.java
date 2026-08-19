package com.cgnpc.bbxpark.space.dto.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/***
 * @Description 用户空间入参
 * @author huangyongtao
 * @date 2025/3/17 11:51
 */
@Data
public class UserInfoSpaceParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1;

//    @NotNull(groups = UpdateGroup.class)
    @ApiModelProperty(value = "用户id")
    private Long id;

    @ApiModelProperty(value = "用户名称.")
    private String userName;

    @ApiModelProperty(value = "用户工号.")
    private String staffid;

    @ApiModelProperty(value = "部门名称.")
    private String departmentName;

    @ApiModelProperty(value = "空间ID.")
    private Long spaceId;

    @ApiModelProperty(value = "园区ID-租户号.")
    private Long tenantId;

    @ApiModelProperty(value = "标识集合.")
    private List<Long> ids;

    @ApiModelProperty(value = "空间id集合.")
    private List<Long> spaceIds;

    @ApiModelProperty(value = "部门id集合.")
    private List<Long> departIds;
}
