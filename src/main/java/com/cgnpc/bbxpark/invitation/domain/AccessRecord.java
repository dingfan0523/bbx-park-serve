
package com.cgnpc.bbxpark.invitation.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cgnpc.bbxpark.common.entity.BaseExEntity;
import lombok.Data;

import java.io.Serializable;


@Data
@TableName("bbx_access_record")
public class AccessRecord extends BaseExEntity implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;
	/**
	 *人员类型.
	 **/
	private String personType;
	/**
	*人员标识.
	**/
	private String personIdentity;
	/**
	*姓名.
	**/
	private String name;
	/**
	 *工号.
	 **/
	private String staffid;
	/**
	*联系方式.
	**/
	private String mobile;
	/**
	*通行方向.
	**/
	private String accessDir;
	/**
	*通行方式.
	**/
	private String accessWay;
	/**
	 *通行结果.
	 **/
	private String accessResult;
	/**
	*设备id.
	**/
	private Long deviceId;
	/**
	*设备名称.
	**/
	private String deviceName;
	/**
	 *设备位置id.
	 **/
	private Long deviceLocationId;
	/**
	 *设备位置.
	 **/
	private String deviceLocation;
	/**
	*卡号.
	**/
	private String card;
	/**
	*人脸图片.
	**/
	private String faceImg;
}
