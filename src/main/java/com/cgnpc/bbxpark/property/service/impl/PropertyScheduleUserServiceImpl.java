
package com.cgnpc.bbxpark.property.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.cgnpc.bbxpark.acl.uic.service.IUserApiService;
import com.cgnpc.bbxpark.common.utils.BeanUtils;
import com.cgnpc.bbxpark.property.domain.PropertyScheduleUser;
import com.cgnpc.bbxpark.property.dto.model.PropertyScheduleUserModel;
import com.cgnpc.bbxpark.property.dto.param.PropertyScheduleUserParam;
import com.cgnpc.bbxpark.property.mapper.PropertyScheduleUserRepository;
import com.cgnpc.bbxpark.property.service.IPropertyScheduleUserService;
import com.cgnpc.bbxpark.space.dto.model.UserInfoModel;
import com.cgnpc.cud.core.service.BaseServiceImpl;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 物业排班人员服务实现
 */
@Service("propertyScheduleUserService")
@Slf4j
public class PropertyScheduleUserServiceImpl extends BaseServiceImpl<PropertyScheduleUserRepository, PropertyScheduleUser> implements IPropertyScheduleUserService {
//	@Resource
//	private UserInfoFeignClient userInfoFeignClient;
//	@Resource
//	private DepartmentMemberFeignClient departmentMemberFeignClient;

    @Resource
    private IUserApiService userApiService;


	/**
	 * 获取物业排班人员列表.
	 * @Param param 物业排班人员查询条件
	 * @Return 物业排班人员信息列表
	 */
	@Override
	@SneakyThrows
	public List<PropertyScheduleUserModel> list(List<Long> scheduleIds) {
		List<PropertyScheduleUser> users = this.list(Wrappers.<PropertyScheduleUser>lambdaQuery().in(PropertyScheduleUser::getScheduleId, scheduleIds));
		return BeanUtils.convertListTo(users, PropertyScheduleUserModel::new);
	}

	/**
	 * 批量新增物业排班人员.
	 * @Param params 物业排班人员信息列表
	 * @Return 批量新增物业排班人员是否成功
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean addBatch(Long scheduleId, List<PropertyScheduleUserParam> params) {
		List<String> userIds = params.stream().map(PropertyScheduleUserParam::getUserId).collect(Collectors.toList());
		//查询用户信息
		Map<String, UserInfoModel> userMap = getUserInfoMap(userIds);
		//查询用户部门信息
//		Map<Long, DepartmentMemberDomain> memberMap = getDepartMemberMap(userIds);

		List<PropertyScheduleUser> scheduleUsers = params.stream().map(u -> {
            UserInfoModel user = userMap.getOrDefault(u.getUserId(),new UserInfoModel());
//			DepartmentMemberDomain member = memberMap.getOrDefault(u.getUserId(),new DepartmentMemberDomain());
//			DepartmentMemberDomain member = getDepartmentMember(u.getUserId());

            PropertyScheduleUser scheduleUser = new PropertyScheduleUser();
			scheduleUser.setScheduleId(scheduleId);
			scheduleUser.setManager(u.getManager() == null || u.getManager() == 1 ? 1 : 0);
            scheduleUser.setUserId(user.getId());
			scheduleUser.setUserName(user.getUserName());
			scheduleUser.setPhone(user.getMobile());
			scheduleUser.setStaffid(user.getStaffNo());
			scheduleUser.setDepartmentId(user.getDepartmentId());
			scheduleUser.setDepartmentName(user.getDepartmentName());
			return scheduleUser;
		}).collect(Collectors.toList());
		return this.saveBatch(scheduleUsers);
	}


	/**
	 * 获取用户信息.
	 * @param userIds 用户标识列表
	 * @return 用户信息列表
	 */
//	private Map<Long,UserInfoModel> getUserInfoMap(List<Long> userIds){
//		List<UserInfoModel> userInfos = Optional.of(userIds).filter(list -> !list.isEmpty()).map(ids -> {
//					UserInfoListParam userParam = new UserInfoListParam();
//					userParam.setIds(userIds);
//					return userParam;
//				}).flatMap(userParam -> Optional.ofNullable(userInfoFeignClient.list(userParam)))
//				.map(ResponseEntity::getBody).map(ResultSet::getResult).orElse(Collections.emptyList());
//		return userInfos.stream().collect(Collectors.toMap(UserInfoModel::getId, userInfoModel -> userInfoModel));
//	}

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

	/**
	 * 获取用户信息.
	 * @param userIds 用户标识列表
	 * @return 用户信息列表
	 */
//	private Map<Long,DepartmentMemberDomain> getDepartMemberMap(List<Long> userIds){
//		List<DepartmentMemberDomain> members = Optional.of(userIds).filter(list -> !list.isEmpty()).map(ids -> {
//					DepartmentMemberListParam param = new DepartmentMemberListParam();
//					param.setOrganizationId(1005L);
//					return param;
//				}).flatMap(param -> Optional.ofNullable(departmentMemberFeignClient.list(param)))
//				.map(ResponseEntity::getBody).map(ResultSet::getResult).orElse(Collections.emptyList());
//		return members.stream().collect(Collectors.toMap(DepartmentMemberDomain::getUserId, model -> model));
//	}
//
//	private DepartmentMemberDomain getDepartmentMember(String userId){
//		DepartmentMemberListParam param = new DepartmentMemberListParam();
//		param.setOrganizationId(1005L);
//		param.setUserId(userId);
//		return Optional.ofNullable(departmentMemberFeignClient.list(param))
//				.map(ResponseEntity::getBody)
//				.map(ResultSet::getResult)
//				.filter(list -> !list.isEmpty())  // 过滤空列表
//				.map(list -> list.get(0))         // 安全地获取第一个元素
//				.orElse(new DepartmentMemberDomain());                    // 如果没有找到，返回null
//	}
}
