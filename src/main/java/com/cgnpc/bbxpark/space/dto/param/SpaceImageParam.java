
package com.cgnpc.bbxpark.space.dto.param;


import com.cgnpc.bbxpark.common.constant.InsertGroup;
import com.cgnpc.bbxpark.common.constant.UpdateGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
public class SpaceImageParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -3064890511141237802L;

    @NotNull(groups = UpdateGroup.class)
    @ApiModelProperty(value = "id.")
    private Long id;

    @NotNull(groups = {InsertGroup.class, UpdateGroup.class})
    @ApiModelProperty(value = "关联空间ID.")
    private Long spaceId;

    @NotNull(groups = {InsertGroup.class, UpdateGroup.class})
    @ApiModelProperty(value = "图片类型.")
    private Integer type;

    @ApiModelProperty(value = "图片集合.")
    private List<String> imageList;

    @Length(max = 200)
    @ApiModelProperty(value = "备注.")
    private String remark;
}
