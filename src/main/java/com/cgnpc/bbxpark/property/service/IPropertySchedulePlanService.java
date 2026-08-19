
package com.cgnpc.bbxpark.property.service;

import com.cgnpc.bbxpark.property.domain.PropertySchedulePlan;
import com.cgnpc.bbxpark.property.dto.model.PropertyDatePlanDetailModel;
import com.cgnpc.bbxpark.property.dto.model.PropertyDatePlanModel;
import com.cgnpc.bbxpark.property.dto.model.PropertySchedulePlanModel;
import com.cgnpc.bbxpark.property.dto.param.PropertyDatePlanParam;
import com.cgnpc.bbxpark.property.dto.param.PropertySchedulePlanListParam;
import com.cgnpc.bbxpark.property.dto.param.PropertySchedulePlanParam;
import com.cgnpc.cud.core.service.IBaseService;

import java.util.List;

/***
 * @Description 物业分组排班计划服务接口
 * @author huangyongtao
 * @date 2025/9/28 11:53
 */
public interface IPropertySchedulePlanService extends IBaseService<PropertySchedulePlan> {

	/**
	 * 根据物业分组排班计划标识获得物业分组排班计划详情信息.
	 * @Param [id] 物业分组标识
	 * @Return 物业分组排班计划详情信息
	 */
	PropertySchedulePlanModel detail(Long scheduleId);

	/**
	 * 获取物业分组排班计划列表.
	 * @Param param 物业分组排班计划查询条件
	 * @Return 物业分组排班计划信息列表
	 */
	List<PropertySchedulePlanModel> list(PropertySchedulePlanListParam param);

	/**
	 * 新增物业分组排班计划.
	 * @Param param 物业分组排班计划信息
	 * @Return 新增物业分组排班计划是否成功
	 */
	Boolean add(PropertySchedulePlanParam param);


	/**
	 * 删除物业分组排班计划.
	 * @Param id 物业分组标识
	 * @Return 删除物业分组排班计划是否成功
	 */
	Boolean remove(Long scheduleId);

	/***
	 * @Description 物业分组排班日历
	 * @author huangyongtao
	 * @date 2025/9/28 14:40
	 * @param param
	 */
	List<PropertyDatePlanModel> getDatePlan(PropertyDatePlanParam param);

	/***
	 * @Description 物业分组排班日历详情
	 * @author huangyongtao
	 * @date 2025/9/28 15:51
	 */
	List<PropertyDatePlanDetailModel> getDatePlanDetail(PropertyDatePlanParam param);

}
