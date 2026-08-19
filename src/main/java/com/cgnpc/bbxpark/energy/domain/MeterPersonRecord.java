
package com.cgnpc.bbxpark.energy.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cgnpc.bbxpark.common.entity.BaseExEntity;
import lombok.Data;

import java.math.BigDecimal;

/***
 * @Description 抄表人工抄表记录数据模型实体
 * @author huangyongtao
 * @date 2025/4/18 17:26
 */
@Data
@TableName("bbx_meter_person_record")
public class MeterPersonRecord extends BaseExEntity {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

	/**
	*设备id.
	**/
	private Long deviceId;
	/**
	*设备名称.
	**/
	private String deviceName;
	/**
	*所属空间ID.
	**/
	private Long spaceId;
	/**
	*所属空间名称.
	**/
	private String spaceName;
	/**
	*抄表类型;（water：水表；electricity：电表；gas：燃气表）.
	**/
	private String readingType;
	/**
	*抄表编码.
	**/
	private String readingCode;
	/**
	*抄表倍率.
	**/
	private Integer readingRate;
	/**
	*抄表值.
	**/
	private BigDecimal readingValue;
	/**
	*设备类型;（1：智能；0：非智能）.
	**/
	private Integer deviceType = 0;

    /**
     *抄表图片
     **/
    private String readingImg;

    /**
     *工单ID.
     **/
    private Long workOrderId;

    /**
     *工单名称.
     **/
    private String workOrderName;

}
