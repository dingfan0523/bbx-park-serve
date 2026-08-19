
package com.cgnpc.bbxpark.property.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.bbxpark.device.dto.model.IocDeviceModel;
import com.cgnpc.bbxpark.property.domain.MeterReadingPlan;
import com.cgnpc.bbxpark.property.dto.model.MeterReadingPlanModel;
import com.cgnpc.bbxpark.property.dto.param.*;
import com.cgnpc.bbxpark.workorder.dto.model.WorkOrderModel;
import com.cgnpc.bbxpark.workorder.dto.param.WorkOrderPageParam;
import com.cgnpc.cud.core.service.IBaseService;

import java.util.List;
import java.util.Map;

/***
 * @Description 抄表计划管理服务接口
 * @author huangyongtao
 * @date 2025/3/25 17:15
 */
public interface IMeterReadingPlanService extends IBaseService<MeterReadingPlan> {

	/**
	 * 根据抄表计划管理标识获得抄表计划管理详情信息.
	 * @Param [id] 抄表计划管理标识
	 * @Return 抄表计划管理详情信息
	 */
	MeterReadingPlanModel detail(Long id);

	/**
	 * 获取抄表计划管理列表(分页).
	 * @Param param 抄表计划管理查询条件
	 * @Return 抄表计划管理信息列表（分页）
	 */
	IPage<MeterReadingPlanModel> page(MeterReadingPlanPageParam param);

	/**
	 * 获取抄表计划管理列表.
	 * @Param param 抄表计划管理查询条件
	 * @Return 抄表计划管理信息列表
	 */
	List<MeterReadingPlanModel> list(MeterReadingPlanListParam param);

	/**
	 * 新增抄表计划管理.
	 * @Param param 抄表计划管理信息
	 * @Return 新增抄表计划管理是否成功
	 */
	Boolean add(MeterReadingPlanParam param);

	/**
	 * 删除抄表计划管理.
	 * @Param id 抄表计划管理标识
	 * @Return 删除抄表计划管理是否成功
	 */
	Boolean remove(Long id);

	/**
	 * 编辑抄表计划管理信息.
	 * @Param param 抄表计划管理信息
	 * @Return 编辑抄表计划管理是否成功
	 */
	Boolean edit(MeterReadingPlanParam param);

	/**
	 * 编辑启用状态抄表计划管理.
	 * @Param id 抄表计划管理标识
	 * @Return 启用抄表计划管理是否成功
	 */
	Boolean statusEdit(MeterReadingPlanStatusParam param);

	/***
	 * @Description 查询抄表设备列表
	 * @author huangyongtao
	 * @date 2025/3/26 14:03
	 * @param param
	 */
	List<IocDeviceModel> findDeviceList(MeterReadingPlanDeviceParam param);

	/**
	 * 获取工单主列表(分页).
	 * @Param param 工单主查询条件
	 * @Return 工单主信息列表（分页）
	 */
	IPage<WorkOrderModel> pageWorkOrder(WorkOrderPageParam param);

	/***
	 * @Description 执行抄表计划
	 * @author huangyongtao
	 * @date 2025/3/26 14:31
	 */
	Boolean executePlan(Map<Long, String> scheduleMap);
}
