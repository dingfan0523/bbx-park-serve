
package com.cgnpc.bbxpark.invitation.dto.param;

import com.cgnpc.bbxpark.common.web.CudPageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


@Data
public class AccessRecordPageParam extends CudPageDto implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -4532699242092737490L;

    @ApiModelProperty(value = "人员类型.")
    private String personType;

    @ApiModelProperty(value = "姓名.")
    private String name;

    @ApiModelProperty(value = "通行方向.")
    private String accessDir;

    @ApiModelProperty(value = "通行方式.")
    private String accessWay;

    @ApiModelProperty(value = "通行结果.")
    private String accessResult;

    @ApiModelProperty(value = "设备id.")
    private Long deviceId;

    @ApiModelProperty(value = "设备id集合.")
    private List<Long> deviceIds;

    @ApiModelProperty(value = "设备位置id.")
    private Long deviceLocationId;

    @ApiModelProperty(value = "设备位置id.")
    private List<Long> deviceLocationIds;

    @ApiModelProperty(value = "开始时间.")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @ApiModelProperty(value = "结束时间.")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;
}
