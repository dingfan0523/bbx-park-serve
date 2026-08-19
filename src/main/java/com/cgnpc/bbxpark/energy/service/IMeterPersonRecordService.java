
package com.cgnpc.bbxpark.energy.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cgnpc.bbxpark.energy.domain.MeterPersonRecord;
import com.cgnpc.bbxpark.energy.dto.model.MeterPersonRecordModel;
import com.cgnpc.bbxpark.energy.dto.model.MeterRecordModel;
import com.cgnpc.bbxpark.energy.dto.param.MeterPersonRecordParam;
import com.cgnpc.bbxpark.energy.dto.param.MeterRecordPageParam;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/***
 * @Description 抄表人工抄表记录服务接口
 * @author huangyongtao
 * @date 2025/4/21 9:28
 */
public interface IMeterPersonRecordService extends IService<MeterPersonRecord> {

	/**
	 * 根据抄表人工抄表记录标识获得抄表人工抄表记录详情信息.
	 * @Param [id] 抄表人工抄表记录标识
	 * @Return 抄表人工抄表记录详情信息
	 */
	MeterPersonRecordModel detail(Long id);

	/**
	 * 获取抄表人工抄表记录列表(分页).
	 * @Param param 抄表人工抄表记录查询条件
	 * @Return 抄表人工抄表记录信息列表（分页）
	 */
	IPage<MeterRecordModel> page(MeterRecordPageParam param);

	/**
	 * 获取抄表人工抄表记录列表.
	 * @Param param 抄表人工抄表记录查询条件
	 * @Return 抄表人工抄表记录信息列表
	 */
	List<MeterPersonRecordModel> list(MeterPersonRecordParam param);

	/***
	 * @Description 抄表人工抄表导出
	 * @author huangyongtao
	 * @date 2025/4/21 10:39
	 * @param response
	 * @param param
	 */
	Boolean meterPersonEasyExport(HttpServletResponse response, MeterRecordPageParam param);

}
