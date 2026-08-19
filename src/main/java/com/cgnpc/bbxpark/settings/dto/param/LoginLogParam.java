
package com.cgnpc.bbxpark.settings.dto.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class LoginLogParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @NotNull
    @ApiModelProperty(value = "登录日志标识.")
    private Long id;

    @ApiModelProperty(value = "登录用户.")
    private String userId;

    @ApiModelProperty(value = "登录类型，0登录1登出2保活3强制退出.")
    private Short loginType;

    @ApiModelProperty(value = "登录时间.")
    private Date loginTime;

    @Length(max = 50)
    @ApiModelProperty(value = "登录地址.")
    private String requestIp;

    @Length(max = 100)
    @ApiModelProperty(value = "登录地点.")
    private String requestAddress;

    @Length(max = 50)
    @ApiModelProperty(value = "操作系统信息.")
    private String requestSystem;

    @Length(max = 50)
    @ApiModelProperty(value = "操作系统版本.")
    private String requestSystemVersion;

    @Length(max = 50)
    @ApiModelProperty(value = "浏览器信息.")
    private String requestBrowser;

    @Length(max = 50)
    @ApiModelProperty(value = "浏览器版本.")
    private String requestBrowserVersion;

    @Length(max = 500)
    @ApiModelProperty(value = "登录token信息.")
    private String token;

    @ApiModelProperty(value = "状态，0成功1失败（用户名/密码错误）2失败（验证码错误）.")
    private Short status;

    @Length(max = 200)
    @ApiModelProperty(value = "备注.")
    private String remark;

    @ApiModelProperty(value = "登录日志标识集合")
    private List<Long> ids;
}
