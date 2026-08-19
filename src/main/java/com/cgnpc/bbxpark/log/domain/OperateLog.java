
package com.cgnpc.bbxpark.log.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


@TableName("uic_operate_log")
@Data
public class OperateLog implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -4476264707078765554L;

	/**
	*操作日志标识.
	**/
    @TableId(value = "id",type = IdType.AUTO)
	private Long id;
	/**
	*操作者.
	**/
	private String operatorId;
	/**
	*操作时间.
	**/
	private Date operateTime;
	/**
	*模块名.
	**/
	private String moduleName;
	/**
	*动作类型(新增、删除、编辑、启用、禁用、注销等).
	**/
	private String actionType;
	/**
	*操作结果，0成功1失败.
	**/
	private Short status;
	/**
	*失败原因，如无权限、系统异常.
	**/
	private String failureCause;
	/**
	*异常信息.
	**/
	private String exceptionInfo;
	/**
	*操作前数据值.
	**/
	private String beforeValue;
	/**
	*操作后数据值.
	**/
	private String afterValue;
	/**
	*请求ip.
	**/
	private String requestIp;
	/**
	*请求浏览器.
	**/
	private String requestBrowser;
	/**
	*浏览器版本.
	**/
	private String requestBrowserVersion;
	/**
	*请求地址.
	**/
	private String requestUrl;
	/**
	*请求参数.
	**/
	private String requestParam;
	/**
	*请求协议.
	**/
	private String requestProcotol;

	/**
	*请求方法.
	**/
	private String requestMethod;
	/**
	*链路标识.
	**/
	private String traceId;
	/**
	*操作耗时.
	**/
	private Long costTime;
	/**
	*备注.
	**/
	private String remark;
	/**
	 * 数据ID
	 */
	private Long tenantId;

}
