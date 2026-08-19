
package com.cgnpc.bbxpark.settings.dto.param;

import com.cgnpc.bbxpark.common.web.CudPageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

@Data
public class LoginLogPageParam extends CudPageDto implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "登录用户.")
    private String userId;

    @ApiModelProperty(value = "登录类型，0登录1登出2保活3强制退出.")
    private Short loginType;

    @Length(max = 50)
    @ApiModelProperty(value = "登录地址.")
    private String requestIp;

    @Length(max = 100)
    @ApiModelProperty(value = "登录地点.")
    private String requestAddress;

    @Length(max = 100)
    @ApiModelProperty(value = "登录token信息.")
    private String token;

    @ApiModelProperty(value = "状态，0成功1失败.")
    private Short status;

    @Length(max = 200)
    @ApiModelProperty(value = "备注.")
    private String remark;

}
