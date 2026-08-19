
package com.cgnpc.bbxpark.space.dto.param;


import com.cgnpc.bbxpark.common.constant.UpdateGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class SpaceManagerParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -4036720184432976744L;

    @NotNull(groups = UpdateGroup.class)
    @ApiModelProperty(value = "id.")
    private Long id;

    @NotNull( message = "关联空间ID不能为空")
    @ApiModelProperty(value = "关联空间ID.")
    private Long spaceId;

    @NotNull( message = "人员类型不能为空")
    @ApiModelProperty(value = "人员类型.")
    private Integer type;

    @NotNull( message = "人员ID不能为空")
    @ApiModelProperty(value = "人员ID.")
    private String userId;

    @Length(max = 200)
    @ApiModelProperty(value = "备注.")
    private String remark;
}
