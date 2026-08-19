
package com.cgnpc.bbxpark.log.vo;

import com.cgnpc.bbxpark.common.web.CudPageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.util.Date;


@Data
public class OperateLogPageParam extends CudPageDto implements Serializable  {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -4162129802403807495L;

    @ApiModelProperty(value = "操作者.")
    private String operatorId;

    @ApiModelProperty(value = "操作时间.")
    private Date operateTime;

    @Length(max = 100)
    @ApiModelProperty(value = "模块名.")
    private String moduleName;

}
