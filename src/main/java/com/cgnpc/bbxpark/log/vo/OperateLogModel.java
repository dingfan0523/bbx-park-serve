
package com.cgnpc.bbxpark.log.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;


@Data
public class OperateLogModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -4404382362511697498L;

    @NotNull
    @ApiModelProperty(value = "操作日志标识.")
    private Long id;

    @NotNull
    @ApiModelProperty(value = "操作者.")
    private String operatorId;

    @ApiModelProperty(value = "操作时间.")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date operateTime;

    @NotBlank
    @Length(max = 100)
    @ApiModelProperty(value = "模块名.")
    private String moduleName;

    @Length(max = 50)
    @ApiModelProperty(value = "动作类型(新增、删除、编辑、启用、禁用、注销等).")
    private String actionType;

    @ApiModelProperty(value = "操作结果，0成功1失败.")
    private Short status;

    @Length(max = 200)
    @ApiModelProperty(value = "失败原因，如无权限、系统异常.")
    private String failureCause;

    @Length(max = 65535)
    @ApiModelProperty(value = "异常信息.")
    private String exceptionInfo;

    @Length(max = 65535)
    @ApiModelProperty(value = "操作前数据值.")
    private String beforeValue;

    @Length(max = 65535)
    @ApiModelProperty(value = "操作后数据值.")
    private String afterValue;

    @Length(max = 50)
    @ApiModelProperty(value = "请求ip.")
    private String requestIp;

    @Length(max = 50)
    @ApiModelProperty(value = "请求浏览器.")
    private String requestBrowser;

    @Length(max = 50)
    @ApiModelProperty(value = "浏览器版本.")
    private String requestBrowserVersion;

    @Length(max = 100)
    @ApiModelProperty(value = "请求地址.")
    private String requestUrl;

    @Length(max = 1000)
    @ApiModelProperty(value = "请求参数.")
    private String requestParam;

    @Length(max = 20)
    @ApiModelProperty(value = "请求协议.")
    private String requestProcotol;

    @Length(max = 500)
    @ApiModelProperty(value = "请求方法.")
    private String requestMethod;

    @Length(max = 200)
    @ApiModelProperty(value = "链路标识.")
    private String traceId;

    @ApiModelProperty(value = "操作耗时.")
    private Long costTime;

    @Length(max = 200)
    @ApiModelProperty(value = "备注.")
    private String remark;

    @ApiModelProperty(value = "操作类型：保持：KIP；修改：MOD；新增：ADD；删除：DEL")
    private String operType;

    @ApiModelProperty(value = "头像")
    private String avatar;

    @ApiModelProperty(value = "姓名")
    private String userName;

    private String statusName;
}
