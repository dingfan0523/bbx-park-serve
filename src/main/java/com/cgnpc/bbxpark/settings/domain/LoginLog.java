
package com.cgnpc.bbxpark.settings.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@TableName("bbx_login_log")
@Data
public class LoginLog implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

	/**
	*登录日志标识.
	**/
    @TableId(value = "id",type = IdType.AUTO)
	private Long id;
	/**
	*登录用户.
	**/
	private String userId;
	/**
	*登录类型，0登录1登出2保活3强制退出.
	**/
	private Short loginType;
	/**
	*登录时间.
	**/
	private Date loginTime;
	/**
	*登录地址.
	**/
	private String requestIp;
	/**
	*登录地点.
	**/
	private String requestAddress;
	/**
	*操作系统信息.
	**/
	private String requestSystem;
	/**
	*操作系统版本.
	**/
	private String requestSystemVersion;
	/**
	*浏览器信息.
	**/
	private String requestBrowser;
	/**
	*浏览器版本.
	**/
	private String requestBrowserVersion;
	/**
	*登录token信息.
	**/
	private String token;
	/**
	*状态，0成功1失败.
	**/
	private Short status;
	/**
	*备注.
	**/
	private String remark;

}
