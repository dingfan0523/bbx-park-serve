
package com.cgnpc.bbxpark.workorder.dto.param;

import com.cgnpc.bbxpark.settings.dto.param.FileParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 工单关联设备入参数据模型
 */
@Data
public class WorkOrderDeviceParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键.")
    private Long id;

    @Length(max = 255)
    @ApiModelProperty(value = "设备id.")
    private Long deviceId;

    @Length(max = 255)
    @ApiModelProperty(value = "设备名称.")
    private String deviceName;

    @ApiModelProperty(value = "设备Dn.")
    private String deviceDn;

    @ApiModelProperty(value = "设备历史状态.")
    private Boolean deviceHisState;

    @ApiModelProperty(value = "空间id.")
    private Long spaceId;

    @Length(max = 255)
    @ApiModelProperty(value = "空间全路径.")
    private String spaceFullPath;

    @ApiModelProperty(value = "抄表类型;（water：水表；electricity：电表；gas：燃气表）.")
    private String readingType;

    @ApiModelProperty(value = "抄表编码.")
    private String readingCode;

    @ApiModelProperty(value = "抄表倍率.")
    private Integer readingRate;

    @ApiModelProperty(value = "抄表值.")
    private BigDecimal readingValue;

    @ApiModelProperty(value = "抄表异常状态（0->是;1->否）.")
    private Integer readingErrorStatus;

    @ApiModelProperty(value = "抄表异常说明.")
    private String readingErrorRemark;

    @ApiModelProperty(value = "文件列表")
    private List<FileParam> fileParamList;
}
