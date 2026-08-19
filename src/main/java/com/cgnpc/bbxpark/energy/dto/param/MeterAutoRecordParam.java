
package com.cgnpc.bbxpark.energy.dto.param;

import com.cgnpc.bbxpark.common.constant.UpdateGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/***
 * @Description 抄表自动上报记录入参数据模型
 * @author huangyongtao
 * @date 2025/4/21 9:15
 */
@Data
public class MeterAutoRecordParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @NotNull(groups = UpdateGroup.class)
    @ApiModelProperty(value = "主键id.")
    private Long id;

    @ApiModelProperty(value = "设备id.")
    private Long deviceId;

    @Length(max = 50)
    @ApiModelProperty(value = "设备名称.")
    private String deviceName;

    @ApiModelProperty(value = "所属空间ID.")
    private Long spaceId;

    @Length(max = 50)
    @ApiModelProperty(value = "所属空间名称.")
    private String spaceName;

    @Length(max = 30)
    @ApiModelProperty(value = "抄表类型;（water：水表；electricity：电表；gas：燃气表）.")
    private String readingType;

    @Length(max = 30)
    @ApiModelProperty(value = "抄表编码.")
    private String readingCode;

    @ApiModelProperty(value = "抄表倍率.")
    private Integer readingRate;

    @ApiModelProperty(value = "抄表值.")
    private BigDecimal readingValue;

    @ApiModelProperty(value = "设备类型;（1：智能；0：非智能）.")
    private Integer deviceType;

    @ApiModelProperty(value = "园区ID-租户号.")
    private Long tenantId;
}
