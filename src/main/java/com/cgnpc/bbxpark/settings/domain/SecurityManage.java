
package com.cgnpc.bbxpark.settings.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cgnpc.bbxpark.common.entity.BaseExEntity;
import lombok.Data;

import java.io.Serializable;

/***
 * @Description 安全管理员数据模型实体
 * @author huangyongtao
 * @date 2025/7/31 17:39
 */
@Data
@TableName("bbx_security_manage")
public class SecurityManage extends BaseExEntity implements Serializable {
	/**
	 * serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

    /**
     * 安全管理员id.
     **/
    private String securityUid;

	/**
	 * 安全管理员名称.
	 **/
	private String securityUname;
	/**
	 * 安全管理员工号.
	 **/
	private String securityStaffid;
}
