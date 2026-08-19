package com.cgnpc.bbxpark.property.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cgnpc.bbxpark.property.domain.InspectionPlan;
import com.cgnpc.bbxpark.property.dto.model.InspectionPlanListModel;
import com.cgnpc.bbxpark.property.dto.model.InspectionPlanModel;
import com.cgnpc.bbxpark.property.dto.model.InspectionPointModel;
import com.cgnpc.bbxpark.property.dto.param.InspectionPlanListParam;
import com.cgnpc.bbxpark.property.dto.param.InspectionPlanPageParam;
import com.cgnpc.bbxpark.property.dto.param.InspectionPlanParam;
import com.cgnpc.bbxpark.property.dto.param.InspectionPointStatusParam;
import com.cgnpc.bbxpark.workorder.dto.model.WorkOrderModel;
import com.cgnpc.bbxpark.workorder.dto.param.WorkOrderPageParam;

import java.util.List;
import java.util.Map;

/**
 * 巡检计划管理服务接口
 */
public interface IInspectionPlanService extends IService<InspectionPlan> {

	/**
	 * 获取巡检计划管理列表(分页).
	 * @Param param 巡检计划管理查询条件
	 * @Return 巡检计划管理信息列表（分页）
	 */
	IPage<InspectionPlanListModel> page(InspectionPlanPageParam param);

	/**
	 * 获取巡检计划管理列表.
	 * @Param param 巡检计划管理查询条件
	 * @Return 巡检计划管理信息列表
	 */
	List<InspectionPlanListModel> list(InspectionPlanListParam param);

    /**
     * 根据巡检计划管理标识获得巡检计划管理详情信息.
     * @Param [id] 巡检计划管理标识
     * @Return 巡检计划管理详情信息
     */
    InspectionPlanModel detail(Long id);

	/**
	 * 新增巡检计划管理.
	 * @Param param 巡检计划管理信息
	 * @Return 新增巡检计划管理是否成功
	 */
	Boolean add(InspectionPlanParam param);

    /**
     * 编辑巡检计划管理信息.
     * @Param param 巡检计划管理信息
     * @Return 编辑巡检计划管理是否成功
     */
    Boolean edit(InspectionPlanParam param);

	/**
	 * 删除巡检计划管理.
	 * @Param id 巡检计划管理标识
	 * @Return 删除巡检计划管理是否成功
	 */
	Boolean remove(Long id);

	/**
	 * 修改巡检计划状态
	 * @param param 修改巡检计划状态参数
	 * @return 修改成功
	 */
	Boolean statusEdit(InspectionPointStatusParam param);

	/**
	 * 根据巡检路线标识获得巡检路线详情信息.
	 * @Param [id] 巡检路线标识
	 * @Return 巡检路线详情信息
	 */
	List<InspectionPointModel> findPointList(Long routeId);

	/**
	 * 获取工单主列表(分页).
	 * @Param param 工单主查询条件
	 * @Return 工单主信息列表（分页）
	 */
	IPage<WorkOrderModel> pageWorkOrder(WorkOrderPageParam param);

	/***
	 * @Description 执行巡检计划
	 * @author huangyongtao
	 * @date 2025/3/26 14:31
	 */
	Boolean executePlan(Map<Long, String> scheduleMap);
}