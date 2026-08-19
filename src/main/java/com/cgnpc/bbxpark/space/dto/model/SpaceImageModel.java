
package com.cgnpc.bbxpark.space.dto.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


@Data
public class SpaceImageModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -4436381185014831233L;

    @ApiModelProperty(value = "id.")
    private Long id;

    @ApiModelProperty(value = "关联空间ID.")
    private Long spaceId;

    @ApiModelProperty(value = "图片类型.")
    private Integer type;

    @ApiModelProperty(value = "图片集合.")
    private List<String> imageList;

    @ApiModelProperty(value = "备注.")
    private String remark;

    @ApiModelProperty(value = "更新人姓名.")
    private String userName;

    @ApiModelProperty(value = "更新人工号")
    private String staffid;

    @ApiModelProperty(value = "更新时间.")
    private Date updateTime;
}
