package com.cgnpc.bbxpark.space.dto.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 用户与空间访问权限入参数据模型
 */
@Data
public class UserSpaceParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -3336887677676855086L;

//    @ApiModelProperty(value = "用户ID.")
//    private String userId;

    @ApiModelProperty(value = "空间ID.")
    private Long spaceId;

//    @ApiModelProperty(value = "空间id集合.")
//    private List<Long> spaceIds;

    @ApiModelProperty(value = "角色id集合.")
    private List<Long> roleIds;

    @ApiModelProperty(value = "部门id集合.")
    private List<Long> departIds;

    @ApiModelProperty(value = "用户id集合.")
    private List<String> userIds;


    /**
     * 园区空间校验参数模型
     * @author dingfan
     * @date 2024/7/1 17:08
     */
    @Data
    public static class ParkSpaceCheckCodeParam implements Serializable {
        /**
         * serialVersionUID.
         */
        private static final long serialVersionUID = -4587033727007666635L;

        @ApiModelProperty(value = "空间标识(仅编辑空间时传)")
        private Long id;
        @ApiModelProperty(value = "所属园区ID-租户号.")
        @NotNull
        private Long tenantId;
        @ApiModelProperty(value = "空间编码.")
        @NotNull
        private String spaceCode;
    }

    /**
     * 园区空间校验参数模型
     *
     * @author dingfan
     * @date 2024/7/1 17:08
     */
    @Data
    public static class ParkSpaceCheckNameParam implements Serializable {
        /**
         * serialVersionUID.
         */
        private static final long serialVersionUID = -4587033727007666635L;

        @ApiModelProperty(value = "空间标识(仅编辑空间时传)")
        private Long id;
        @ApiModelProperty(value = "所属空间id")
        @NotNull
        private Long parentSpaceId;
        @ApiModelProperty(value = "空间名称.")
        @NotNull
        private String spaceName;
    }
}
