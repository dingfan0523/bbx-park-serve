
package com.cgnpc.bbxpark.property.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cgnpc.bbxpark.acl.uic.service.IUserApiService;
import com.cgnpc.bbxpark.common.constant.SystemResultCode;
import com.cgnpc.bbxpark.common.enums.Status;
import com.cgnpc.bbxpark.common.utils.*;
import com.cgnpc.bbxpark.property.domain.PropertySchedule;
import com.cgnpc.bbxpark.property.domain.PropertySchedulePlan;
import com.cgnpc.bbxpark.property.domain.PropertyScheduleUser;
import com.cgnpc.bbxpark.property.dto.model.PropertyScheduleListModel;
import com.cgnpc.bbxpark.property.dto.model.PropertyScheduleModel;
import com.cgnpc.bbxpark.property.dto.model.PropertyScheduleUserModel;
import com.cgnpc.bbxpark.property.dto.param.PropertyScheduleListParam;
import com.cgnpc.bbxpark.property.dto.param.PropertySchedulePageParam;
import com.cgnpc.bbxpark.property.dto.param.PropertyScheduleParam;
import com.cgnpc.bbxpark.property.mapper.PropertySchedulePlanRepository;
import com.cgnpc.bbxpark.property.mapper.PropertyScheduleRepository;
import com.cgnpc.bbxpark.property.service.IPropertyScheduleService;
import com.cgnpc.bbxpark.property.service.IPropertyScheduleUserService;
import com.cgnpc.bbxpark.space.dto.model.UserInfoModel;
import com.cgnpc.cud.core.service.BaseServiceImpl;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 物业排班服务实现
 */
@Service("propertyScheduleService")
@Slf4j
public class PropertyScheduleServiceImpl extends BaseServiceImpl<PropertyScheduleRepository, PropertySchedule> implements IPropertyScheduleService {
	@Resource
	private IPropertyScheduleUserService propertyScheduleUserService;
//	@Resource
//	private UserInfoFeignClient userInfoFeignClient;
	@Resource
	private PropertySchedulePlanRepository propertySchedulePlanRepository;

    @Resource
    private IUserApiService userApiService;

	/**
	 * 根据物业排班标识获得物业排班详情信息.
	 * @Param [id] 物业排班标识
	 * @Return 物业排班详情信息
	 */
	@Override
	public PropertyScheduleModel detail(Long id) {
		PropertySchedule propertySchedule = this.getById(id);
		AssertUtils.notNull(propertySchedule, SystemResultCode.RESULT_DATA_NONE.message());
		PropertyScheduleModel model = BeanUtils.convertTo(propertySchedule, PropertyScheduleModel::new);
		model.setUserList(propertyScheduleUserService.list(Collections.singletonList(id)));
		return model;
	}

	/**
	 * 获取物业排班列表(分页).
	 * @Param param 物业排班查询条件
	 * @Return 物业排班信息列表（分页）
	 */
	@Override
	public IPage<PropertyScheduleListModel> page(PropertySchedulePageParam param) {
		Long tenantId = WebFrameworkUtils.getHeaderTenantId();
		IPage<PropertySchedule> page = this.page(new Page<>(param.getCurrent(),param.getSize()),Wrappers.<PropertySchedule>lambdaQuery().eq(tenantId != null,PropertySchedule::getTenantId, tenantId)
				.like(StringUtils.isNotEmpty(param.getName()),PropertySchedule::getName, param.getName())
				.eq(PropertySchedule::getDeleted, Status.enabled.getKey())
				.orderByDesc(PropertySchedule::getCreateTime));
		if(CollectionUtils.isEmpty(page.getRecords())){
			return ConvertUtil.pageEmptyConvert(page.getCurrent(),page.getSize());
		}

		List<Long> scheduleIds = page.getRecords().stream().map(PropertySchedule::getId).collect(Collectors.toList());
		//人员数据
		List<PropertyScheduleUser> userList = propertyScheduleUserService.list(Wrappers.<PropertyScheduleUser>lambdaQuery().in(PropertyScheduleUser::getScheduleId, scheduleIds));
		Map<Long, Long> userCountMap = userList.stream().collect(Collectors.groupingBy(PropertyScheduleUser::getScheduleId, Collectors.counting()));
		//人员数据
		Map<String, UserInfoModel> userMap = getUserInfoMap(page.getRecords().stream().map(PropertySchedule::getCreatorId).distinct().collect(Collectors.toList()));
		List<PropertyScheduleListModel> list = page.getRecords().stream().map(schedule -> {
			PropertyScheduleListModel model = BeanUtils.convertTo(schedule, PropertyScheduleListModel::new);
			model.setUserCount(userCountMap.getOrDefault(schedule.getId(), 0L));
			if(userMap.containsKey(schedule.getCreatorId())){
                model.setCreateBy(userMap.get(schedule.getCreatorId()).getUserName());
			}
			return model;
		}).collect(Collectors.toList());
		return ConvertUtil.pageConvert(page.getCurrent(),page.getTotal(),page.getSize(),list);
	}

	/**
	 * 获取物业排班列表.
	 * @Param param 物业排班查询条件
	 * @Return 物业排班信息列表
	 */
	@Override
	@SneakyThrows
	public List<PropertyScheduleListModel> list(PropertyScheduleListParam param) {
		Long tenantId = WebFrameworkUtils.getHeaderTenantId();
		List<PropertySchedule> schedules = this.list(Wrappers.<PropertySchedule>lambdaQuery()
				.eq(tenantId != null,PropertySchedule::getTenantId, tenantId)
				.like(StringUtils.isNotEmpty(param.getName()),PropertySchedule::getName, param.getName())
				.in(!CollectionUtils.isEmpty(param.getIds()),PropertySchedule::getId, param.getIds())
				.eq(PropertySchedule::getDeleted, Status.enabled.getKey())
				.orderByDesc(PropertySchedule::getCreateTime));
		if(CollectionUtils.isEmpty(schedules)){
			return Collections.emptyList();
		}
		List<Long> scheduleIds = schedules.stream().map(PropertySchedule::getId).collect(Collectors.toList());
		//人员数据
		List<PropertyScheduleUser> userList = propertyScheduleUserService.list(Wrappers.<PropertyScheduleUser>lambdaQuery().in(PropertyScheduleUser::getScheduleId, scheduleIds));
		Map<Long, Long> userCountMap = userList.stream().collect(Collectors.groupingBy(PropertyScheduleUser::getScheduleId, Collectors.counting()));
		return schedules.stream().map(schedule -> {
			PropertyScheduleListModel model = BeanUtils.convertTo(schedule, PropertyScheduleListModel::new);
			model.setUserCount(userCountMap.getOrDefault(schedule.getId(), 0L));
			return model;
		}).collect(Collectors.toList());
	}

	/**
	 * 新增物业排班.
	 * @Param param 物业排班信息
	 * @Return 新增物业排班是否成功
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean add(PropertyScheduleParam param) {
		if(!CollectionUtils.isEmpty(param.getUserList())){
			long count = param.getUserList().stream().filter(p->p.getManager() != null && p.getManager() == 0).count();
			AssertUtils.isFalse(count > 1, "分组负责人不能存在多个");
		}
		PropertySchedule schedule = BeanUtils.convertTo(param,PropertySchedule::new);
		schedule.setId(null);
		//用户关联数据
		propertyScheduleUserService.remove(Wrappers.<PropertyScheduleUser>lambdaQuery().eq(PropertyScheduleUser::getScheduleId, schedule.getId()));
		if(this.save(schedule) && !CollectionUtils.isEmpty(param.getUserList())){
			propertyScheduleUserService.addBatch(schedule.getId(), param.getUserList());
		}
		return true;
	}

	/**
	 * 编辑物业排班信息.
	 * @Param param 物业排班信息
	 * @Return 编辑物业排班是否成功
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean edit(PropertyScheduleParam param) {
		if(!CollectionUtils.isEmpty(param.getUserList())){
			long count = param.getUserList().stream().filter(p->p.getManager() != null && p.getManager() == 0).count();
			AssertUtils.isFalse(count > 1, "分组负责人不能存在多个");
		}
		PropertySchedule schedule = this.getById(param.getId());
		AssertUtils.notNull(schedule, SystemResultCode.RESULT_DATA_NONE.message());
		schedule.setName(param.getName());
		schedule.setRemark(param.getRemark());
		//用户关联数据
		propertyScheduleUserService.remove(Wrappers.<PropertyScheduleUser>lambdaQuery().eq(PropertyScheduleUser::getScheduleId, schedule.getId()));
		if(this.updateById(schedule) && !CollectionUtils.isEmpty(param.getUserList())){
			propertyScheduleUserService.addBatch(schedule.getId(), param.getUserList());
		}
		return true;
	}

	/**
	 * 删除物业排班.
	 * @Param id 物业排班标识
	 * @Return 删除物业排班是否成功
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean remove(Long id) {
		PropertySchedule propertySchedule = this.getById(id);
		AssertUtils.notNull(propertySchedule, SystemResultCode.RESULT_DATA_NONE.message());
		propertySchedule.setDeleted((int) Status.disabled.getKey());
		propertyScheduleUserService.remove(Wrappers.<PropertyScheduleUser>lambdaQuery().eq(PropertyScheduleUser::getScheduleId, id));
		propertySchedulePlanRepository.delete(Wrappers.<PropertySchedulePlan>lambdaQuery().eq(PropertySchedulePlan::getScheduleId, id));
		return updateById(propertySchedule);
	}

	@Override
	public List<PropertyScheduleUserModel> findUserList(Long id) {
		PropertySchedule propertySchedule = this.getById(id);
		AssertUtils.notNull(propertySchedule, SystemResultCode.RESULT_DATA_NONE.message());
		return propertyScheduleUserService.list(Collections.singletonList(id));
	}

	@Override
	public Map<Long, String> getScheduleNameMap(List<Long> scheduleIds){
		List<PropertySchedule> schedules = this.list(Wrappers.<PropertySchedule>lambdaQuery().in(CollectionUtil.isNotEmpty(scheduleIds), PropertySchedule::getId, scheduleIds).eq(PropertySchedule::getDeleted, Status.enabled.getKey()).eq(ObjectUtil.isNotEmpty(WebFrameworkUtils.getHeaderTenantId()), PropertySchedule::getTenantId, WebFrameworkUtils.getHeaderTenantId()));
		return CollectionUtil.isEmpty(schedules) ? new HashMap<>() : schedules.stream().collect(Collectors.toMap(PropertySchedule::getId, PropertySchedule::getName));
	}

	/**
	 * 获取用户信息.
	 * @param userIds 用户标识列表
	 * @return 用户信息列表
	 */
	private Map<String, UserInfoModel> getUserInfoMap(List<String> userIds){
        List<UserInfoModel> staffTysModels = new ArrayList<>();
        if(CollectionUtil.isNotEmpty(userIds)){
            staffTysModels = userApiService.getByStaffNos(userIds);
        }
		return staffTysModels.stream().collect(Collectors.toMap(UserInfoModel::getStaffNo, userInfoModel -> userInfoModel));
	}
}
