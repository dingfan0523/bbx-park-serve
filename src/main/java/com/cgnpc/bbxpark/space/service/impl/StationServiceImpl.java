
package com.cgnpc.bbxpark.space.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cgnpc.bbxpark.acl.uic.model.StaffModel;
import com.cgnpc.bbxpark.acl.uic.service.IDepartmentApiService;
import com.cgnpc.bbxpark.acl.uic.service.IUserApiService;
import com.cgnpc.bbxpark.common.constant.SystemResultCode;
import com.cgnpc.bbxpark.common.exception.GenericException;
import com.cgnpc.bbxpark.common.utils.AssertUtils;
import com.cgnpc.bbxpark.common.utils.BeanUtils;
import com.cgnpc.bbxpark.common.utils.ConvertUtil;
import com.cgnpc.bbxpark.common.utils.WebFrameworkUtils;
import com.cgnpc.bbxpark.space.domain.SpaceBasicInfo;
import com.cgnpc.bbxpark.space.domain.Station;
import com.cgnpc.bbxpark.space.dto.model.*;
import com.cgnpc.bbxpark.space.dto.param.DepartmentMemberExParam;
import com.cgnpc.bbxpark.space.dto.param.StationListParam;
import com.cgnpc.bbxpark.space.dto.param.StationPageParam;
import com.cgnpc.bbxpark.space.dto.param.StationParam;
import com.cgnpc.bbxpark.space.mapper.StationRepository;
import com.cgnpc.bbxpark.space.service.IParkSpaceService;
import com.cgnpc.bbxpark.space.service.ISpaceBasicInfoService;
import com.cgnpc.bbxpark.space.service.IStationService;
import com.cgnpc.bbxpark.space.service.ITenantMemberService;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


@Service
public class StationServiceImpl extends ServiceImpl<StationRepository, Station> implements IStationService {

	@Resource
	private IUserApiService userApiService;
	@Resource
	private IDepartmentApiService departmentApiService;
	@Resource
	private ITenantMemberService tenantMemberService;
	@Resource
	private ISpaceBasicInfoService spaceBasicInfoService;

	@Resource
	private IParkSpaceService parkSpaceService;

	/**
	 * 获取空间工位列表(分页).
	 * @Param param 空间工位查询条件
	 * @Return 空间工位信息列表（分页）
	 */
	@Override
	public IPage<StationModel> page(StationPageParam param) {
		Long tenantId = WebFrameworkUtils.getHeaderTenantId();
		IPage<Station> page = page(new Page<> (param.getCurrent(),param.getSize()), Wrappers.<Station>lambdaQuery()
				.eq(tenantId != null,Station::getTenantId,tenantId)
                .eq(param.getSpaceId() != null,Station::getSpaceId,param.getSpaceId())
                .orderByDesc(Station::getCreateTime));
		if(CollectionUtils.isEmpty(page.getRecords())){
			return ConvertUtil.pageEmptyConvert(page.getCurrent(),page.getSize());
		}
        //人员信息集合
        List<String> staffNos = page.getRecords().stream().map(Station::getUserId).collect(Collectors.toList());
        List<UserInfoModel> userInfos = userApiService.getByStaffNos(staffNos);
		Map<String, UserInfoModel> userMap = Optional.of(userInfos).map(users -> users.stream()
                .collect(Collectors.toMap(UserInfoModel::getId, Function.identity()))).orElse(Collections.emptyMap());

		List<StationModel> list = page.getRecords().stream().map(station -> {
			StationModel model = BeanUtils.convertTo(station, StationModel::new);
            //人员部门
			Optional.of(station.getUserId()).ifPresent(userId -> {
                UserInfoModel user = userMap.getOrDefault(userId,new UserInfoModel());
                model.setDepartmentId(user.getDepartmentId());
				model.setDepartmentName(user.getDepartmentName());
			});
			return model;
		}).collect(Collectors.toList());
		return ConvertUtil.pageConvert(page.getCurrent(),page.getTotal(),page.getSize(),list);
	}

	/**
	 * 获取空间工位列表.
	 * @Param param 空间工位查询条件
	 * @Return 空间工位信息列表
	 */
	@Override
	@SneakyThrows
	public List<StationModel> list(StationListParam param) {
		Long tenantId = WebFrameworkUtils.getHeaderTenantId();
		List<Station> stations = this.list(Wrappers.<Station>lambdaQuery().eq(tenantId != null,Station::getTenantId, tenantId).eq(param.getSpaceId() != null,Station::getSpaceId, param.getSpaceId()).orderByDesc(Station::getCreateTime));
		if(CollectionUtils.isEmpty(stations)){
			return Collections.emptyList();
		}

        //人员信息集合
        List<String> staffNos = stations.stream().map(Station::getUserId).collect(Collectors.toList());
        List<UserInfoModel> userInfos = userApiService.getByStaffNos(staffNos);
        Map<String, UserInfoModel> userMap = Optional.of(userInfos).map(users -> users.stream()
                .collect(Collectors.toMap(UserInfoModel::getId, Function.identity()))).orElse(Collections.emptyMap());

		return stations.stream().map(station -> {
			StationModel model = BeanUtils.convertTo(station, StationModel::new);
            //人员部门
            Optional.of(station.getUserId()).ifPresent(userId -> {
                UserInfoModel user = userMap.getOrDefault(userId,new UserInfoModel());
                model.setDepartmentId(user.getDepartmentId());
                model.setDepartmentName(user.getDepartmentName());
            });
			return model;
		}).collect(Collectors.toList());
	}

	/**
	 * 分配空间工位.
	 * @Param param 空间工位信息
	 * @Return 分配空间工位是否成功
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean allot(StationParam param) {
		Long tenantId = WebFrameworkUtils.getHeaderTenantId();
		Long spaceId = param.getSpaceId();

		SpaceBasicInfo space = spaceBasicInfoService.getOne(Wrappers.<SpaceBasicInfo>lambdaQuery().eq(SpaceBasicInfo::getSpaceId, spaceId));
		AssertUtils.notNull(space, SystemResultCode.RESULT_DATA_NONE.message());
        //校验空间类型
        AssertUtils.isTrue(space.getType() != null && space.getType() == 1,"空间类型已变更,无法分配");

		if(!CollectionUtils.isEmpty(param.getUserIdList())){
			//检查用户是否已经分配在其他空间
			List<Station> existingStations = this.list(Wrappers.<Station>lambdaQuery()
					.eq(tenantId != null,Station::getTenantId, tenantId).in(Station::getUserId, param.getUserIdList()).ne(Station::getSpaceId, spaceId));
			AssertUtils.isFalse(!CollectionUtils.isEmpty(existingStations),"用户已分配在其他空间");
		}
		//删除当前空间下工位数据
		this.remove(Wrappers.<Station>lambdaQuery().eq(tenantId != null,Station::getTenantId, tenantId).eq(Station::getSpaceId, spaceId));
		if(!CollectionUtils.isEmpty(param.getUserIdList())){
			//获取当前用户信息
			UserInfoModel nowUserInfo = Optional.ofNullable(userApiService.detail(WebFrameworkUtils.getHeaderUserId())).orElseThrow(() -> GenericException.fail("获取当前用户信息失败"));
			//批量获取用户信息
			List<UserInfoModel> userInfoList = Optional.ofNullable(userApiService.getByStaffNos(param.getUserIdList()))
					.orElse(Collections.emptyList());
			Map<String, UserInfoModel> userInfoMap = Optional.of(userInfoList).map(users -> users.stream()
                    .collect(Collectors.toMap(UserInfoModel::getId, Function.identity()))).orElse(Collections.emptyMap());

			//保存数据
			List<Station> list = param.getUserIdList().stream().map(userId -> {
				Station station = new Station();
				station.setSpaceId(spaceId);
				station.setUserId(userId);
                UserInfoModel user = userInfoMap.getOrDefault(userId,new UserInfoModel());
                station.setUserName(user.getUserName());
                station.setStaffid(user.getStaffid());
                station.setDepartmentId(user.getDepartmentId());
				station.setAssignerId(nowUserInfo.getId());
				station.setAssignerName(nowUserInfo.getUserName());
				station.setAssignerStaffid(nowUserInfo.getStaffid());
				return station;
			}).collect(Collectors.toList());
			return this.saveBatch(list);
		}
		return true;
	}

	@Override
	public Boolean check(StationParam param) {
		Long spaceId = param.getSpaceId();
		//校验空间容量是否满足
		SpaceBasicInfo space = spaceBasicInfoService.getOne(Wrappers.<SpaceBasicInfo>lambdaQuery().eq(SpaceBasicInfo::getSpaceId, spaceId));
		AssertUtils.notNull(space, SystemResultCode.RESULT_DATA_NONE.message());
		return space.getCapacity() >= param.getUserIdList().size();
	}

	@Override
	public StationStatisticsModel statistics(Long spaceId) {
		Long tenantId = WebFrameworkUtils.getHeaderTenantId();
		//空间信息
		SpaceBasicInfo space = spaceBasicInfoService.getOne(Wrappers.<SpaceBasicInfo>lambdaQuery().eq(SpaceBasicInfo::getSpaceId, spaceId));
		Integer capacity = space == null || space.getCapacity() == null ? 0 : space.getCapacity();

		StationStatisticsModel model = new StationStatisticsModel();
        int count = this.count(Wrappers.<Station>lambdaQuery().eq(tenantId != null, Station::getTenantId, tenantId).eq(Station::getSpaceId, spaceId));
        model.setAllocatedCount((long) count);
		model.setIdleCount(capacity - model.getAllocatedCount());
		model.setIdleCount(model.getIdleCount() < 0 ? 0 : model.getIdleCount());
		return model;
	}

	/**
	 * 删除空间工位.
	 * @Param id 空间工位标识
	 * @Return 删除空间工位是否成功
	 */
	@Override
	public Boolean remove(Long id) {
		Station station = this.getById(id);
		AssertUtils.notNull(station, SystemResultCode.RESULT_DATA_NONE.message());
		return removeById(id);
	}

	@Override
	public List<OrgDeptExTreeNode> listOrgTree(Long organizationId) {
//		List<OrgDeptTreeNode> orgDeptTree = departmentApiService.listOrgTree(organizationId);
//		// 获取当前用户部门id
//		String currentUserDepartmentId = userApiService.getCurrentUserDepartmentId(WebFrameworkUtils.getHeaderUserId());
//		// 如果无法获取当前用户或用户部门信息，则返回未选中的树
//		if (currentUserDepartmentId == null) {
//			return convertToOrgDeptExTree(orgDeptTree, null);
//		}
//
//		// 转换为OrgDeptExTreeNode并设置选中状态
//		return convertToOrgDeptExTree(orgDeptTree, currentUserDepartmentId);
        return null;
	}

	@Override
	public IPage<DepartmentMemberExModel> departmentMemberPage(DepartmentMemberExParam param) {
        List<StaffModel> staffs = userApiService.getOriginalStaffsByOrgId(param.getOrgId(),(int)param.getCurrent(),(int)param.getSize(),param.getKeyword());
        if(CollectionUtils.isEmpty(staffs)){
            return ConvertUtil.pageEmptyConvert(param.getCurrent(),param.getSize());
        }
        //当前人员页
        List<String> userIds = staffs.stream().map(StaffModel::getEmpId).collect(Collectors.toList());
        //需要禁用的人员id
        List<Station> stations = this.list(Wrappers.<Station>lambdaQuery().in(Station::getUserId, userIds).ne(Station::getSpaceId, param.getSpaceId()).eq(Station::getTenantId, WebFrameworkUtils.getHeaderTenantId()));
        Set<String> disabledUserIds = stations.stream().map(Station::getUserId).collect(Collectors.toSet());

        List<DepartmentMemberExModel> memberExModels = staffs.stream().map(member -> {
            DepartmentMemberExModel memberExModel = BeanUtils.convertTo(member, DepartmentMemberExModel::new);
            //可选人员：未分配在其他空间
            memberExModel.setDisabled(disabledUserIds.contains(member.getEmpId()));
            return memberExModel;
        }).collect(Collectors.toList());
        return ConvertUtil.pageConvert(param.getCurrent(), memberExModels.size(), param.getSize(), memberExModels);
	}

	@Override
	public ParkSpaceFullModel getUserStationSpace() {
        String userId = userApiService.getCurrentStaffNo();
		List<Station> stations = this.list(Wrappers.<Station>lambdaQuery().eq(Station::getUserId, userId).eq(Station::getTenantId, WebFrameworkUtils.getHeaderTenantId()));
		if(CollectionUtils.isEmpty(stations)){
			return new ParkSpaceFullModel();
		}
		Long spaceId = stations.get(0).getSpaceId();
		Map<Long, ParkSpaceFullModel> parkSpaceFullModelMap = parkSpaceService.findFullSpaceMap(Collections.singletonList(spaceId), WebFrameworkUtils.getHeaderTenantId());
		return ObjectUtil.isEmpty(parkSpaceFullModelMap) ? new ParkSpaceFullModel() : parkSpaceFullModelMap.get(spaceId);
	}


	/**
	 * 将OrgDeptTreeNode转换为OrgDeptExTreeNode，并根据指定部门ID设置选中状态
	 * @param nodeList 原始节点列表
	 * @param targetDepartmentId 目标部门ID
	 * @return 转换后的节点列表
	 */
	private List<OrgDeptExTreeNode> convertToOrgDeptExTree(List<OrgDeptTreeNode> nodeList, String targetDepartmentId) {
		if (CollectionUtils.isEmpty(nodeList)) {
			return Collections.emptyList();
		}

		return nodeList.stream()
				.map(node -> convertNode(node, targetDepartmentId))
				.collect(Collectors.toList());
	}

	/**
	 * 转换单个节点
	 * @param node 原始节点
	 * @param targetDepartmentId 目标部门ID
	 * @return 转换后的节点
	 */
	private OrgDeptExTreeNode convertNode(OrgDeptTreeNode node, String targetDepartmentId) {
		OrgDeptExTreeNode exNode = new OrgDeptExTreeNode();
		exNode.setId(node.getId());
		exNode.setDeptId(node.getDeptId());
		exNode.setOrgId(node.getOrgId());
		exNode.setName(node.getName());
		exNode.setType(node.getType());
		exNode.setParentId(node.getParentId());
		exNode.setStatus(node.getStatus());
		exNode.setTreePath(node.getTreePath());

		// 判断是否选中
		boolean isChecked = targetDepartmentId != null &&
				(("dept".equals(node.getType()) && targetDepartmentId.equals(node.getDeptId())) ||
						("org".equals(node.getType()) && targetDepartmentId.equals(node.getOrgId())));
		exNode.setChecked(isChecked);

		// 递归转换子节点
		if (!CollectionUtils.isEmpty(node.getChildren())) {
			exNode.setChildren(convertToOrgDeptExTree(node.getChildren(), targetDepartmentId));
		}

		return exNode;
	}
}
