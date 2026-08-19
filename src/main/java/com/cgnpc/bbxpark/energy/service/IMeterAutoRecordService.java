
package com.cgnpc.bbxpark.energy.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cgnpc.bbxpark.energy.domain.MeterAutoRecord;
import com.cgnpc.bbxpark.energy.dto.model.MeterAutoRecordModel;
import com.cgnpc.bbxpark.energy.dto.model.MeterRecordModel;
import com.cgnpc.bbxpark.energy.dto.param.MeterAutoRecordParam;
import com.cgnpc.bbxpark.energy.dto.param.MeterRecordPageParam;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/***
 * @Description 抄表自动上报记录服务接口
 * @author huangyongtao
 * @date 2025/4/21 9:27
 */
public interface IMeterAutoRecordService extends IService<MeterAutoRecord> {

	/**
	 * 根据抄表自动上报记录标识获得抄表自动上报记录详情信息.
	 * @Param [id] 抄表自动上报记录标识
	 * @Return 抄表自动上报记录详情信息
	 */
	MeterAutoRecordModel detail(Long id);

	/**
	 * 获取抄表自动上报记录列表(分页).
	 * @Param param 抄表自动上报记录查询条件
	 * @Return 抄表自动上报记录信息列表（分页）
	 */
	IPage<MeterRecordModel> page(MeterRecordPageParam param);

	/**
	 * 获取抄表自动上报记录列表.
	 * @Param param 抄表自动上报记录查询条件
	 * @Return 抄表自动上报记录信息列表
	 */
	List<MeterAutoRecordModel> list(MeterAutoRecordParam param);

	/***
	 * @Description 抄表自动上报导出
	 * @author huangyongtao
	 * @date 2025/4/21 10:39
	 * @param response
	 * @param param
	 */
	Boolean meterAutoEasyExport(HttpServletResponse response, MeterRecordPageParam param);

	/***
	 * @Description 生成自动抄表数据
	 * @author huangyongtao
	 * @date 2025/4/21 14:51
	 */
	Boolean executeAutoReading();

}
