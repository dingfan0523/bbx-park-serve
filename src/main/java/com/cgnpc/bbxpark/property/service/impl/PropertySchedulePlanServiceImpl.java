
package com.cgnpc.bbxpark.property.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.cgnpc.bbxpark.common.constant.SystemResultCode;
import com.cgnpc.bbxpark.common.utils.AssertUtils;
import com.cgnpc.bbxpark.common.utils.BeanUtils;
import com.cgnpc.bbxpark.common.utils.WebFrameworkUtils;
import com.cgnpc.bbxpark.property.domain.PropertySchedule;
import com.cgnpc.bbxpark.property.domain.PropertySchedulePlan;
import com.cgnpc.bbxpark.property.domain.PropertyScheduleUser;
import com.cgnpc.bbxpark.property.dto.model.*;
import com.cgnpc.bbxpark.property.dto.param.PropertyDatePlanParam;
import com.cgnpc.bbxpark.property.dto.param.PropertyScheduleListParam;
import com.cgnpc.bbxpark.property.dto.param.PropertySchedulePlanListParam;
import com.cgnpc.bbxpark.property.dto.param.PropertySchedulePlanParam;
import com.cgnpc.bbxpark.property.mapper.PropertySchedulePlanRepository;
import com.cgnpc.bbxpark.property.service.IPropertySchedulePlanService;
import com.cgnpc.bbxpark.property.service.IPropertyScheduleService;
import com.cgnpc.bbxpark.property.service.IPropertyScheduleUserService;
import com.cgnpc.cud.core.service.BaseServiceImpl;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/***
 * @Description 物业分组排班计划服务实现
 * @author huangyongtao
 * @date 2025/9/28 11:55
 */
@Service("propertySchedulePlanService")
public class PropertySchedulePlanServiceImpl extends BaseServiceImpl<PropertySchedulePlanRepository, PropertySchedulePlan> implements IPropertySchedulePlanService {

	@Autowired
	private IPropertyScheduleService propertyScheduleService;

	@Autowired
	private IPropertyScheduleUserService propertyScheduleUserService;

	/**
	 * 根据物业分组排班计划标识获得物业分组排班计划详情信息.
	 * @Param [scheduleId] 物业分组排班计划标识
	 * @Return 物业分组排班计划详情信息
	 */
	@Override
	public PropertySchedulePlanModel detail(Long scheduleId) {
		AssertUtils.notNull(scheduleId, "物业分组id不能为空");
		List<PropertySchedulePlan> propertySchedulePlans = this.list(Wrappers.<PropertySchedulePlan>lambdaQuery()
				.eq(PropertySchedulePlan::getScheduleId, scheduleId)
		        .eq(ObjectUtil.isNotEmpty(WebFrameworkUtils.getHeaderTenantId()), PropertySchedulePlan::getTenantId, WebFrameworkUtils.getHeaderTenantId()));
		PropertySchedulePlanModel model = CollectionUtil.isEmpty(propertySchedulePlans) ? new PropertySchedulePlanModel() : BeanUtils.convertTo(propertySchedulePlans.get(0), PropertySchedulePlanModel::new);
		handleFindModel(model);
		return model;
	}

	/**
	 * 获取物业分组排班计划列表.
	 * @Param param 物业分组排班计划查询条件
	 * @Return 物业分组排班计划信息列表
	 */
	@Override
	@SneakyThrows
	public List<PropertySchedulePlanModel> list(PropertySchedulePlanListParam param) {
		List<PropertySchedulePlan> plans = this.list(Wrappers.<PropertySchedulePlan>lambdaQuery()
				.eq(ObjectUtil.isNotEmpty(param.getScheduleId()), PropertySchedulePlan::getScheduleId, param.getScheduleId())
				.in(CollectionUtil.isNotEmpty(param.getScheduleIds()), PropertySchedulePlan::getScheduleId, param.getScheduleIds())
				.eq(ObjectUtil.isNotEmpty(WebFrameworkUtils.getHeaderTenantId()), PropertySchedulePlan::getTenantId, WebFrameworkUtils.getHeaderTenantId()));
		List<PropertySchedulePlanModel> models = BeanUtils.convertListTo(plans, PropertySchedulePlanModel::new);
		for (PropertySchedulePlanModel model : models) {
			handleFindModel(model);
		}
		return models;
	}

	/**
	 * 新增物业分组排班计划.
	 * @Param param 物业分组排班计划信息
	 * @Return 新增物业分组排班计划是否成功
	 */
	@Override
	public Boolean add(PropertySchedulePlanParam param) {
		checkParam(param);
		handleAddModel(param);
		PropertySchedulePlan plan = BeanUtils.convertTo(param, PropertySchedulePlan::new);
		if(ObjectUtil.isEmpty(param.getId())){
			this.save(plan);
		}else{
			PropertySchedulePlan propertySchedulePlan = this.getById(param.getId());
			AssertUtils.notNull(propertySchedulePlan, SystemResultCode.RESULT_DATA_NONE.message());
			this.updateById(plan);
		}
		return true;
	}

	/**
	 * 删除物业分组排班计划.
	 * @Param id 物业分组标识
	 * @Return 删除物业分组排班计划是否成功
	 */
	@Override
	public Boolean remove(Long scheduleId) {
		return this.remove(Wrappers.<PropertySchedulePlan>lambdaQuery().eq(PropertySchedulePlan::getScheduleId, scheduleId));
	}

	@Override
	public List<PropertyDatePlanModel> getDatePlan(PropertyDatePlanParam param) {
		AssertUtils.notNull(param.getStartTime(), "开始时间不能为空");
		AssertUtils.notNull(param.getEndTime(), "结束时间不能为空");
		//物业分组集合
		List<PropertyScheduleListModel> propertyScheduleModels = propertyScheduleService.list(new PropertyScheduleListParam());
		//物业分组排班计划集合
		List<PropertySchedulePlanModel> propertySchedulePlans = this.list(new PropertySchedulePlanListParam());
		Map<Long, PropertySchedulePlanModel> propertySchedulePlanModelMap = CollectionUtil.isEmpty(propertySchedulePlans) ? new HashMap<>() : propertySchedulePlans.stream().collect(Collectors.toMap(PropertySchedulePlanModel::getScheduleId, propertySchedulePlanModel -> propertySchedulePlanModel, (key1, key2) -> key1));
		List<PropertyDatePlanModel> propertyDatePlanModels = new ArrayList<>();
		for (DateTime monthDate : DateUtil.rangeToList(param.getStartTime(), param.getEndTime(), DateField.DAY_OF_MONTH)) {
			PropertyDatePlanModel propertyDatePlanModel = new PropertyDatePlanModel();
			List<PropertyScheduleListModel> scheduleModels = new ArrayList<>();
			for (PropertyScheduleListModel propertyScheduleModel : propertyScheduleModels) {
				PropertySchedulePlanModel propertySchedulePlanModel = propertySchedulePlanModelMap.get(propertyScheduleModel.getId());
				if(ObjectUtil.isNotEmpty(propertySchedulePlanModel) && isDateValid(monthDate, propertySchedulePlanModel)){
					scheduleModels.add(propertyScheduleModel);
				}
			}

			propertyDatePlanModel.setTimeDate(monthDate);
			propertyDatePlanModel.setPropertyScheduleList(scheduleModels);
			propertyDatePlanModels.add(propertyDatePlanModel);
		}
		return propertyDatePlanModels;
	}

	@Override
	public List<PropertyDatePlanDetailModel> getDatePlanDetail(PropertyDatePlanParam param) {
		if(CollectionUtil.isEmpty(param.getIds())){
			return Collections.emptyList();
		}
		//物业分组集合
		List<PropertyDatePlanDetailModel> models =findPropertyScheduleList(param.getIds());
		//物业分组排班计划集合
		PropertySchedulePlanListParam propertySchedulePlanListParam = new PropertySchedulePlanListParam();
		propertySchedulePlanListParam.setScheduleIds(param.getIds());
		List<PropertySchedulePlanModel> propertySchedulePlans = this.list(propertySchedulePlanListParam);
		Map<Long, PropertySchedulePlanModel> propertySchedulePlanModelMap = CollectionUtil.isEmpty(propertySchedulePlans) ? new HashMap<>() : propertySchedulePlans.stream().collect(Collectors.toMap(PropertySchedulePlanModel::getScheduleId, propertySchedulePlanModel -> propertySchedulePlanModel, (key1, key2) -> key1));
		models.forEach(model -> {
			PropertySchedulePlanModel propertySchedulePlanModel = propertySchedulePlanModelMap.get(model.getId());
			if(propertySchedulePlanModel != null){
				model.setPeriodStartTime(DateUtil.format(propertySchedulePlanModel.getPeriodStartTime(), "HH:mm"));
				model.setPeriodEndTime(DateUtil.format(propertySchedulePlanModel.getPeriodEndTime(), "HH:mm"));
			}
		});
		return models;
	}
	/**
	 * 获取物业排班列表.
	 * @Param param 物业排班查询条件
	 * @Return 物业排班信息列表
	 */
	private List<PropertyDatePlanDetailModel> findPropertyScheduleList(List<Long> ids) {
		Long tenantId = WebFrameworkUtils.getHeaderTenantId();
		List<PropertySchedule> schedules = propertyScheduleService.list(Wrappers.<PropertySchedule>lambdaQuery()
				.eq(tenantId != null,PropertySchedule::getTenantId, tenantId)
				.in(!CollectionUtils.isEmpty(ids),PropertySchedule::getId, ids)
				.orderByDesc(PropertySchedule::getCreateTime));
		if(CollectionUtils.isEmpty(schedules)){
			return Collections.emptyList();
		}
		List<Long> scheduleIds = schedules.stream().map(PropertySchedule::getId).collect(Collectors.toList());
		//人员数据
		List<PropertyScheduleUser> userList = propertyScheduleUserService.list(Wrappers.<PropertyScheduleUser>lambdaQuery().in(PropertyScheduleUser::getScheduleId, scheduleIds));
		Map<Long, List<PropertyScheduleUser>> userListMap = userList.stream().collect(Collectors.groupingBy(PropertyScheduleUser::getScheduleId));
		return schedules.stream().map(schedule -> {
			PropertyDatePlanDetailModel model = BeanUtils.convertTo(schedule, PropertyDatePlanDetailModel::new);
			List<PropertyScheduleUser> users = userListMap.getOrDefault(schedule.getId(), Collections.emptyList());
			model.setUserCount((long) users.size());
			model.setUserList(BeanUtils.convertListTo(users, PropertyScheduleUserModel::new));
			return model;
		}).collect(Collectors.toList());
	}


	/***
	 * @Description 校验是否满足排班
	 * @author huangyongtao
	 * @date 2025/9/28 15:39
	 * @param currentDate
	 * @param model
	 */
	private boolean isDateValid(Date currentDate, PropertySchedulePlanModel model) {
		if (model.getPeriodType() == null || model.getPeriodSigns() == null || model.getPeriodStartTime() == null
				|| model.getPeriodEndTime() == null || model.getPlanStartTime() == null || model.getPlanEndTime() == null) {
			return false; // 如果周期类型或标识为空，直接返回 false
		}
		if(DateUtil.beginOfDay(currentDate).before(DateUtil.beginOfDay(model.getPlanStartTime())) || DateUtil.beginOfDay(currentDate).after(DateUtil.beginOfDay(model.getPlanEndTime()))){
			return false;
		}
		switch (model.getPeriodType()) {
			case "month":
				int month = DateUtil.dayOfMonth(currentDate);
				return model.getPeriodSigns().contains(month);
			case "week":
				int week = DateUtil.dayOfWeek(currentDate) - 1 == 0 ? 7 : DateUtil.dayOfWeek(currentDate) - 1 ;
				return model.getPeriodSigns().contains(week);
			default:
				return false; // 未知周期类型
		}
	}

	private void checkParam(PropertySchedulePlanParam param){
		AssertUtils.notNull(param.getScheduleId(), "物业分组id不能为空");
		AssertUtils.notNull(param.getPeriodType(), "工作周期不能为空");
		AssertUtils.notEmpty(param.getPeriodSigns(), "工作周期不能为空");
		AssertUtils.notNull(param.getPeriodStartTime(), "工作开始时间不能为空");
		AssertUtils.notNull(param.getPeriodEndTime(), "工作结束时间不能为空");
		AssertUtils.notNull(param.getPlanStartTime(), "排班开始时间不能为空");
		AssertUtils.notNull(param.getPlanEndTime(), "排班结束时间不能为空");
	}

	private void handleFindModel(PropertySchedulePlanModel model){
		if (ObjectUtil.isNotEmpty(model.getPeriodSign())){
			String [] periodSigns = model.getPeriodSign().split(",");
			List<Integer> periodSignList = Arrays.stream(periodSigns)
					.map(Integer::valueOf)
					.collect(Collectors.toList());
			model.setPeriodSigns(periodSignList);
		}else{
			model.setPeriodSigns(new ArrayList<>());
		}
	}

	private void handleAddModel(PropertySchedulePlanParam model){
		if (CollectionUtil.isNotEmpty(model.getPeriodSigns())){
			model.setPeriodSign(model.getPeriodSigns().stream().map(String::valueOf).collect(Collectors.joining(",")));
		}
	}
}
