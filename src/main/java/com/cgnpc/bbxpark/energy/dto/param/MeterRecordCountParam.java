
package com.cgnpc.bbxpark.energy.dto.param;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/***
 * @Description 抄表记录统计入参数据模型
 * @author huangyongtao
 * @date 2025/4/21 9:11
 */
@Data
public class MeterRecordCountParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "当前支路信息")
    private EnergyBranchParam branchParam;

    @ApiModelProperty(value = "子支路信息集合")
    private List<EnergyBranchParam> sonBranchParams;

    @ApiModelProperty(value = "时间类型（month：月；year：年）")
    private String timeType;

    @ApiModelProperty(value = "子时间类型（month：月；year：年）")
    private String sonTimeType;

    @ApiModelProperty(value = "抄表类型;（water：水表；electricity：电表；gas：燃气表）.")
    private String readingType;

    @ApiModelProperty(value = "抄表方式;（person：人工抄表；auto：自动上报.")
    private String meterMethod;

    @ApiModelProperty(value = "园区ID-租户号.")
    private Long tenantId;

    @ApiModelProperty(value = "开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startDate;

    @ApiModelProperty(value = "结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endDate;

    @ApiModelProperty(value = "支路能耗（导出使用）")
    private List<MeterRecordBranchCountParam> branchCountParams;

    @ApiModelProperty(value = "子支路能耗（导出使用）")
    private List<MeterRecordSonBranchCountParam> sonBranchCountParams;
}
