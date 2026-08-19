package com.cgnpc.bbxpark.property.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cgnpc.bbxpark.device.dto.model.IocDeviceModel;
import com.cgnpc.bbxpark.property.domain.MaintainPlan;
import com.cgnpc.bbxpark.property.dto.model.MaintainPlanModel;
import com.cgnpc.bbxpark.property.dto.param.*;
import com.cgnpc.bbxpark.workorder.dto.model.WorkOrderModel;
import com.cgnpc.bbxpark.workorder.dto.param.WorkOrderPageParam;

import java.util.List;
import java.util.Map;

/***
 * @Description 维保计划管理服务接口
 * @author huangyongtao
 * @date 2025/10/16 15:12
 */
public interface IMaintainPlanService extends IService<MaintainPlan> {

	/**
	 * 根据维保计划管理标识获得维保计划管理详情信息.
	 * @Param [id] 维保计划管理标识
	 * @Return 维保计划管理详情信息
	 */
	MaintainPlanModel detail(Long id);

	/**
	 * 获取维保计划管理列表(分页).
	 * @Param param 维保计划管理查询条件
	 * @Return 维保计划管理信息列表（分页）
	 */
	IPage<MaintainPlanModel> page(MaintainPlanPageParam param);

	/**
	 * 获取维保计划管理列表.
	 * @Param param 维保计划管理查询条件
	 * @Return 维保计划管理信息列表
	 */
	List<MaintainPlanModel> list(MaintainPlanListParam param);

	/**
	 * 新增维保计划管理.
	 * @Param param 维保计划管理信息
	 * @Return 新增维保计划管理是否成功
	 */
	Boolean add(MaintainPlanParam param);


	/**
	 * 删除维保计划管理.
	 * @Param id 维保计划管理标识
	 * @Return 删除维保计划管理是否成功
	 */
	Boolean remove(Long id);


	/**
	 * 编辑维保计划管理信息.
	 * @Param param 维保计划管理信息
	 * @Return 编辑维保计划管理是否成功
	 */
	Boolean edit(MaintainPlanParam param);


	/**
	 * 编辑启用状态维保计划管理.
	 * @Param id 维保计划管理标识
	 * @Return 启用维保计划管理是否成功
	 */
	Boolean statusEdit(MaintainPlanStatusParam param);

	/***
	 * @Description 查询维保设备列表
	 * @author huangyongtao
	 * @date 2025/3/26 14:03
	 * @param param
	 */
	List<IocDeviceModel> findDeviceList(MaintainPlanDeviceParam param);

	/**
	 * 获取工单主列表(分页).
	 * @Param param 工单主查询条件
	 * @Return 工单主信息列表（分页）
	 */
	IPage<WorkOrderModel> pageWorkOrder(WorkOrderPageParam param);

	/***
	 * @Description 执行维保计划
	 * @author huangyongtao
	 * @date 2025/3/26 14:31
	 */
	Boolean executePlan(Map<Long, String> scheduleMap);

}