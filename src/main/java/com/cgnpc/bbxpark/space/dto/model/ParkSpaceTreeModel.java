package com.cgnpc.bbxpark.space.dto.model;

import com.cgnpc.bbxpark.device.dto.model.DeviceVideoModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 园区空间树形业务数据模型
 *
 * @author dingfan
 * @date 2024/7/1 15:33
 */
@Data
public class ParkSpaceTreeModel implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -4115487799614544671L;

    @ApiModelProperty(value = "空间ID.")
    private Long id;
    @ApiModelProperty(value = "所属空间ID.")
    private Long parentSpaceId;
    @ApiModelProperty(value = "租户id")
    private Long tenantId;
    @ApiModelProperty(value = "空间编码.")
    private String spaceCode;
    @ApiModelProperty(value = "所属空间编码.")
    private String parentSpaceCode;
    @ApiModelProperty(value = "空间名称.")
    private String spaceName;
    @ApiModelProperty(value = "空间地址.")
    private String spaceAddr;
    @ApiModelProperty(value = "空间描述.")
    private String spaceDesc;
    @ApiModelProperty(value = "排序序号.")
    private Integer orderCode;
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    @ApiModelProperty(value = "是否选择 ture:是；false:否")
    private Boolean flag = false;
    @ApiModelProperty(value = "子级集合")
    private List<ParkSpaceTreeModel> children;
    @ApiModelProperty(value = "是否有下级")
    private Boolean hasChildren = false;
    @ApiModelProperty(value = "设备数量")
    private Integer deviceNum = 0;
    @ApiModelProperty(value = "视频设备集合")
    private List<DeviceVideoModel> deviceVideoModels;
}
