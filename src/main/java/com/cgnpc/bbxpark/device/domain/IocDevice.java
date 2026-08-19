
package com.cgnpc.bbxpark.device.domain;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cgnpc.bbxpark.common.entity.BaseExEntity;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/***
 * @Description ioc设备数据模型实体
 * @author huangyongtao
 * @date 2025/2/21 17:03
 */
@Data
@TableName("bbx_ioc_device")
public class IocDevice extends BaseExEntity implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

	/**
	*设备名称.
	**/
	private String deviceName;
	/**
	*设备编码.
	**/
	private String deviceCode;
	/**
	*产品id.
	**/
	@TableField(strategy = FieldStrategy.IGNORED)
	private Long productId;
	/**
	*所属空间ID.
	**/
    @TableField(strategy = FieldStrategy.IGNORED)
	private Long spaceId;
	/**
	*所属部门id.
	**/
    @TableField(strategy = FieldStrategy.IGNORED)
	private String departmentId;
	/**
	 *所属部门名称.
	 **/
	@TableField(strategy = FieldStrategy.IGNORED)
	private String departmentName;
	/**
	*设备等级;（10：关键；20：重要；30：一般）.
	**/
	private Integer deviceLevel;
	/**
	*启用状态;（0->否;1->是）.
	**/
	private Integer enableStatus;
	/**
	*上线状态;（0->否;1->是）.
	**/
	private Integer onlineStatus;
	/**
	*下线备注.
	**/
	private String onlineRemark;
	/**
	*设备类型;（1：单体设备；2：母子设备）.
	**/
	private Integer deviceType;
	/**
	*母子设备类型;（1:母设备；2：子设备）.
	**/
	@TableField(strategy = FieldStrategy.IGNORED)
	private Integer deviceComplexType;
	/**
	*母设备id.
	**/
	@TableField(strategy = FieldStrategy.IGNORED)
	private Long deviceComplexId;
	/**
	*抄表设备;（0->否;1->是）.
	**/
	private Integer readingDevice;
	/**
	*抄表类型;（water：水表；electricity：电表；gas：燃气表）.
	**/
	@TableField(strategy = FieldStrategy.IGNORED)
	private String readingType;

	/**
	 *抄表编码
	 **/
	@TableField(strategy = FieldStrategy.IGNORED)
	private String readingCode;

	/**
	 *抄表倍率
	 **/
	@TableField(strategy = FieldStrategy.IGNORED)
	private Integer readingRate;

	/**
	 *抄表值
	 **/
	private BigDecimal readingValue;

	/**
	 *抄表时间
	 **/
	private Date readingTime;

	/**
	*物联设备平台;（0：非物联网设备；1：自有平台；2：统建平台；3：安消平台）.
	**/
	private Integer iotDevicePlatform;
	/**
	*物联设备识别码.
	**/
	@TableField(strategy = FieldStrategy.IGNORED)
	private String iotDeviceDn;
	/**
	*物理设备在线状态;（0->在线;1->离线）.
	**/
	@TableField(strategy = FieldStrategy.IGNORED)
	private Integer iotDeviceStatus;

	/**
	 *物联设备在线状态更新时间
	 **/
	@TableField(strategy = FieldStrategy.IGNORED)
	private Date iotDeviceStatusTime;

	/**
	*物联产品类型.
	**/
	@TableField(strategy = FieldStrategy.IGNORED)
	private String iotProductCode;
	/**
	*出厂编码.
	**/
	private String workCode;
	/**
	*出厂日期.
	**/
	@TableField(strategy = FieldStrategy.IGNORED)
	private Date wordDate;
	/**
	*生产批次.
	**/
	private String productionBatch;
	/**
	*维保到期日期.
	**/
	@TableField(strategy = FieldStrategy.IGNORED)
	private Date secureDate;
	/**
	*预计报废日期.
	**/
	@TableField(strategy = FieldStrategy.IGNORED)
	private Date scrapDate;
	/**
	*投用日期.
	**/
	@TableField(strategy = FieldStrategy.IGNORED)
	private Date useDate;
	/**
	*安装日期.
	**/
	@TableField(strategy = FieldStrategy.IGNORED)
	private Date fixDate;
	/**
	*安装单位.
	**/
	private String fixUnit;
	/**
	*备注.
	**/
	private String remark;
	/**
	 *是否重点(0->否;1->是).
	 **/
	private Integer keyArea;


	/**
	 *设备分类；1：弱电设备；2：CIT设备；3：固定资产
	 **/
	@TableField(strategy = FieldStrategy.IGNORED)
	private Integer deviceCategory;
	/**
	 *规格型号.
	 **/
	private String standardModel;
	/**
	 *总折旧月数
	 **/
	@TableField(strategy = FieldStrategy.IGNORED)
	private Integer depreciationMonth ;
	/**
	 *责任部门id.
	 **/
	@TableField(strategy = FieldStrategy.IGNORED)
	private Long dutyDepartmentId;
	/**
	 *责任部门名称.
	 **/
	@TableField(strategy = FieldStrategy.IGNORED)
	private String dutyDepartmentName;
	/**
	 *责任人id.
	 **/
	@TableField(strategy = FieldStrategy.IGNORED)
	private String dutyUid;
	/**
	 *责任人员名称.
	 **/
	@TableField(strategy = FieldStrategy.IGNORED)
	private String dutyUname;
	/**
	 *责任人联系电话.
	 **/
	@TableField(strategy = FieldStrategy.IGNORED)
	private String dutyMobile;
	/**
	 *责任人工号.
	 **/
	@TableField(strategy = FieldStrategy.IGNORED)
	private String dutyStaffid;
	/**
	 *使用人id.
	 **/
	@TableField(strategy = FieldStrategy.IGNORED)
	private String useUid ;
	/**
	 *使用人名称.
	 **/
	@TableField(strategy = FieldStrategy.IGNORED)
	private String useUname ;
	/**
	 *使用工号.
	 **/
	@TableField(strategy = FieldStrategy.IGNORED)
	private String useStaffid;
	/**
	 *供应商id.
	 **/
	@TableField(strategy = FieldStrategy.IGNORED)
	private Long supplierId;
	/**
	 *供应商名称
	 **/
	@TableField(strategy = FieldStrategy.IGNORED)
	private String supplierName;
    /**
     *供应商联系人id.
     **/
    @TableField(strategy = FieldStrategy.IGNORED)
    private Long supplierPersonId;
	/**
	 *供应商联系人.
	 **/
	@TableField(strategy = FieldStrategy.IGNORED)
	private String supplierPerson;
	/**
	 *供应商联系电话.
	 **/
	@TableField(strategy = FieldStrategy.IGNORED)
	private String supplierMobile;

    /**
     *抄表图片
     **/
    private String readingImg;

}
