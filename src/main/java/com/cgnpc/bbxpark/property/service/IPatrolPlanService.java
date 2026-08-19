package com.cgnpc.bbxpark.property.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cgnpc.bbxpark.property.domain.PatrolPlan;
import com.cgnpc.bbxpark.property.dto.model.PatrolPlanListModel;
import com.cgnpc.bbxpark.property.dto.model.PatrolPlanModel;
import com.cgnpc.bbxpark.property.dto.model.SimplePatrolRouteModel;
import com.cgnpc.bbxpark.property.dto.param.InspectionPointStatusParam;
import com.cgnpc.bbxpark.property.dto.param.PatrolPlanListParam;
import com.cgnpc.bbxpark.property.dto.param.PatrolPlanPageParam;
import com.cgnpc.bbxpark.property.dto.param.PatrolPlanParam;
import com.cgnpc.bbxpark.workorder.dto.model.WorkOrderModel;
import com.cgnpc.bbxpark.workorder.dto.param.WorkOrderPageParam;

import java.util.List;
import java.util.Map;

/**
 * 巡更计划管理服务接口
 */
public interface IPatrolPlanService extends IService<PatrolPlan> {

	/**
	 * 根据巡更计划管理标识获得巡更计划管理详情信息.
	 * @Param [id] 巡更计划管理标识
	 * @Return 巡更计划管理详情信息
	 */
	PatrolPlanModel detail(Long id);

	/**
	 * 获取巡更计划管理列表(分页).
	 * @Param param 巡更计划管理查询条件
	 * @Return 巡更计划管理信息列表（分页）
	 */
	IPage<PatrolPlanListModel> page(PatrolPlanPageParam param);

	/**
	 * 获取巡更计划管理列表.
	 * @Param param 巡更计划管理查询条件
	 * @Return 巡更计划管理信息列表
	 */
	List<PatrolPlanListModel> list(PatrolPlanListParam param);

	/**
	 * 新增巡更计划管理.
	 * @Param param 巡更计划管理信息
	 * @Return 新增巡更计划管理是否成功
	 */
	Boolean add(PatrolPlanParam param);

	/**
	 * 删除巡更计划管理.
	 * @Param id 巡更计划管理标识
	 * @Return 删除巡更计划管理是否成功
	 */
	Boolean remove(Long id);

	/**
	 * 编辑巡更计划管理信息.
	 * @Param param 巡更计划管理信息
	 * @Return 编辑巡更计划管理是否成功
	 */
	Boolean edit(Long id, PatrolPlanParam param);

    /**
     * 修改巡更计划状态
     * @param param 修改巡更计划状态参数
     * @return 修改成功
     */
    Boolean statusEdit(InspectionPointStatusParam param);

	/**
	 * 获取巡更计划关联的路线列表
	 * @param planId 巡更计划标识
	 * @return 巡更计划关联的路线列表
	 */
	List<SimplePatrolRouteModel> findRouteList(Long planId);

	/**
	 * 获取工单主列表(分页).
	 * @Param param 工单主查询条件
	 * @Return 工单主信息列表（分页）
	 */
	IPage<WorkOrderModel> pageWorkOrder(WorkOrderPageParam param);

	/***
	 * @Description 执行巡更计划
	 * @author huangyongtao
	 * @date 2025/3/26 14:31
	 */
	Boolean executePlan(Map<Long, String> scheduleMap);
}