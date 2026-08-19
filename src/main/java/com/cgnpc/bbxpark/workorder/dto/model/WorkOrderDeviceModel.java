
package com.cgnpc.bbxpark.workorder.dto.model;

import com.cgnpc.bbxpark.settings.dto.model.FileModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 工单关联设备业务数据模型
 */
@Data
public class WorkOrderDeviceModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键.")
    private Long id;

    @ApiModelProperty(value = "设备id.")
    private Long deviceId;

    @ApiModelProperty(value = "设备名称.")
    private String deviceName;

    @ApiModelProperty(value = "设备Dn")
    private String deviceDn;

    @ApiModelProperty(value = "设备历史状态.")
    private Boolean deviceHisState;

    @ApiModelProperty(value = "空间id.")
    private Long spaceId;

    @ApiModelProperty(value = "空间全路径.")
    private String spaceFullPath;

    @ApiModelProperty(value = "工单id.")
    private Long workOrderId;

    @ApiModelProperty(value = "租户id.")
    private String tenantId;

    @ApiModelProperty(value = "创建时间.")
    private Date createTime;

    @ApiModelProperty(value = "更新时间.")
    private Date updateTime;

    @ApiModelProperty(value = "创建人工号.")
    private String creatorId;

    @ApiModelProperty(value = "创建人名称.")
    private String createBy;

    @ApiModelProperty(value = "更新人工号.")
    private String updatorId;

    @ApiModelProperty(value = "更新人名称.")
    private String updateBy;

    @ApiModelProperty(value = "设备实时状态.")
    private Boolean deviceNowState;

    @ApiModelProperty(value = "抄表类型;（water：水表；electricity：电表；gas：燃气表）.")
    private String readingType;

    @ApiModelProperty(value = "抄表编码.")
    private String readingCode;

    @ApiModelProperty(value = "抄表倍率.")
    private Integer readingRate;

    @ApiModelProperty(value = "抄表值.")
    private BigDecimal readingValue;

    @ApiModelProperty(value = "抄表异常状态（1->是;0->否）.")
    private Integer readingErrorStatus;

    @ApiModelProperty(value = "文件列表")
    private List<FileModel> fileModelList;

    @ApiModelProperty(value = "上次抄表值.")
    private BigDecimal oldReadingValue;

    @ApiModelProperty(value = "上次抄表时间")
    private Date oldReadingTime;

    @ApiModelProperty(value = "抄表异常说明.")
    private String readingErrorRemark;
}
