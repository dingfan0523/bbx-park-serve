
package com.cgnpc.bbxpark.space.dto.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


@Data
public class ParkSpaceModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -4115487799614544671L;

    @ApiModelProperty(value = "空间ID.")
    private Long id;

    @ApiModelProperty(value = "空间编码.")
    private String spaceCode;

    @ApiModelProperty(value = "空间名称.")
    private String spaceName;

    @ApiModelProperty(value = "空间地址.")
    private String spaceAddr;

    @ApiModelProperty(value = "空间描述.")
    private String spaceDesc;

    @ApiModelProperty(value = "所属空间ID.")
    private Long parentSpaceId;

    @ApiModelProperty(value = "排序序号.")
    private Integer orderCode;

    @ApiModelProperty(value = "状态0在用1删除.")
    private Integer spaceStatus;

    @ApiModelProperty(value = "所属园区ID-租户号.")
    private String tenantId;

    @ApiModelProperty(value = "乐观锁.")
    private String revision;

    @ApiModelProperty(value = "创建人.")
    private String createUserId;

    @ApiModelProperty(value = "创建时间.")
    private Date createDate;

    @ApiModelProperty(value = "更新人.")
    private String modifyUserId;

    @ApiModelProperty(value = "更新时间.")
    private Date modifyDate;

    @ApiModelProperty(value = "是否选择 ture:是；false:否")
    private Boolean flag = false;

    @ApiModelProperty(value = "上级空间编码.")
    private String parentSpaceCode;

}
