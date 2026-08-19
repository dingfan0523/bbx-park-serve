
package com.cgnpc.bbxpark.energy.dto.param;

import com.cgnpc.bbxpark.common.web.CudPageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/***
 * @Description 抄表人工抄表记录分页参数模型
 * @author huangyongtao
 * @date 2025/4/21 9:17
 */
@Data
public class MeterPersonRecordPageParam extends CudPageDto implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id.")
    private Long id;

    @ApiModelProperty(value = "设备id.")
    private Long deviceId;

    @ApiModelProperty(value = "设备名称.")
    private String deviceName;

    @ApiModelProperty(value = "所属空间ID.")
    private Long spaceId;

    @ApiModelProperty(value = "所属空间名称.")
    private String spaceName;

    @ApiModelProperty(value = "抄表类型;（water：水表；electricity：电表；gas：燃气表）.")
    private String readingType;

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

    @ApiModelProperty(value = "创建时间.")
    private Date createTime;

    @ApiModelProperty(value = "创建时间开始.")
    private Date createTimeStart;

    @ApiModelProperty(value = "创建时间结束")
    private Date createTimeEnd;

}
