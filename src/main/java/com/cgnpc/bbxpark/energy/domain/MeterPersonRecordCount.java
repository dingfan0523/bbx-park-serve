
package com.cgnpc.bbxpark.energy.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cgnpc.bbxpark.common.entity.BaseExEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/***
 * @Description 抄表人工抄表记录统计数据模型实体
 * @author huangyongtao
 * @date 2025/4/18 17:27
 */
@Data
@TableName("bbx_meter_person_record_count")
public class MeterPersonRecordCount extends BaseExEntity {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

	/**
	*设备id.
	**/
	private Long deviceId;
	/**
	*抄表值.
	**/
	private BigDecimal readingValue;
	/**
	*统计时间.
	**/
	private Date countTime;
}
