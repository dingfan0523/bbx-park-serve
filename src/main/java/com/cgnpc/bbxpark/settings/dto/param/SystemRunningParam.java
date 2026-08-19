package com.cgnpc.bbxpark.settings.dto.param;

import com.cgnpc.bbxpark.common.web.CudPageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * 查询系统运行日志入参
 * @author EDZ
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SystemRunningParam extends CudPageDto implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "异常类型")
    private String anomalousType;
    @ApiModelProperty(value = "异常描述")
    private String abnormalDescription;
    @ApiModelProperty(value = "日志等级")
    private String time;
    private String pageSize;
    private String pageNumber;
    private List<String> timeList;
    private String finishTime;
    private String keyName;
}
