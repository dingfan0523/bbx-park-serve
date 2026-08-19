package com.cgnpc.bbxpark.property.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cgnpc.bbxpark.property.domain.InventoryPlan;
import com.cgnpc.bbxpark.property.dto.model.DeviceItemModel;
import com.cgnpc.bbxpark.property.dto.model.InventoryPlanListModel;
import com.cgnpc.bbxpark.property.dto.model.InventoryPlanModel;
import com.cgnpc.bbxpark.property.dto.model.MaterialItemModel;
import com.cgnpc.bbxpark.property.dto.param.*;
import com.cgnpc.bbxpark.workorder.dto.model.WorkOrderModel;
import com.cgnpc.bbxpark.workorder.dto.param.WorkOrderPageParam;

import java.util.List;
import java.util.Map;

/**
 * 盘点计划管理服务接口
 */
public interface IInventoryPlanService extends IService<InventoryPlan> {

	/**
	 * 根据盘点计划管理标识获得盘点计划管理详情信息.
	 * @Param [id] 盘点计划管理标识
	 * @Return 盘点计划管理详情信息
	 */
	InventoryPlanModel detail(Long id);

	/**
	 * 获取盘点计划管理列表(分页).
	 * @Param param 盘点计划管理查询条件
	 * @Return 盘点计划管理信息列表（分页）
	 */
	IPage<InventoryPlanListModel> page(InventoryPlanPageParam param);

	/**
	 * 获取盘点计划管理列表.
	 * @Param param 盘点计划管理查询条件
	 * @Return 盘点计划管理信息列表
	 */
	List<InventoryPlanListModel> list(InventoryPlanListParam param);

	/**
	 * 新增盘点计划管理.
	 * @Param param 盘点计划管理信息
	 * @Return 新增盘点计划管理是否成功
	 */
	Boolean add(InventoryPlanParam param);

	/**
	 * 编辑盘点计划管理信息.
	 * @Param param 盘点计划管理信息
	 * @Return 编辑盘点计划管理是否成功
	 */
	Boolean edit(Long id, InventoryPlanParam param);

	/**
	 * 删除盘点计划管理.
	 * @Param id 盘点计划管理标识
	 * @Return 删除盘点计划管理是否成功
	 */
	Boolean remove(Long id);

	/**
	 * 修改盘点计划状态
	 * @param param 修改盘点计划状态参数
	 * @return 修改成功
	 */
	Boolean statusEdit(InspectionPointStatusParam param);

	/**
	 * 获取工单主列表(分页).
	 * @Param param 工单主查询条件
	 * @Return 工单主信息列表（分页）
	 */
	IPage<WorkOrderModel> pageWorkOrder(WorkOrderPageParam param);

	/***
	 * @Description 执行盘点计划
	 * @author huangyongtao
	 * @date 2025/3/26 14:31
	 */
	Boolean executePlan(Map<Long, String> scheduleMap);
	
	/**
	 * 查询盘点计划下材料列表
	 * @param param 查询条件
	 * @return 材料列表
	 */
	List<MaterialItemModel> findMaterialList(InventoryPlanItemParam param);
	
	/**
	 * 查询盘点计划下设备列表
	 * @param param 查询条件
	 * @return 设备列表
	 */
	List<DeviceItemModel> findDeviceList(InventoryPlanItemParam param);
}