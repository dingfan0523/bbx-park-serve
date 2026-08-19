
package com.cgnpc.bbxpark.space.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cgnpc.bbxpark.acl.uic.model.OrgDepartmentNode;
import com.cgnpc.bbxpark.acl.uic.service.IDepartmentApiService;
import com.cgnpc.bbxpark.acl.uic.service.IUserApiService;
import com.cgnpc.bbxpark.common.constant.SystemResultCode;
import com.cgnpc.bbxpark.common.utils.AssertUtils;
import com.cgnpc.bbxpark.common.utils.BeanUtils;
import com.cgnpc.bbxpark.common.utils.CollectionUtils;
import com.cgnpc.bbxpark.common.utils.WebFrameworkUtils;
import com.cgnpc.bbxpark.space.domain.ParkSpace;
import com.cgnpc.bbxpark.space.domain.SpaceBasicInfo;
import com.cgnpc.bbxpark.space.domain.Station;
import com.cgnpc.bbxpark.space.dto.model.*;
import com.cgnpc.bbxpark.space.dto.param.ParkSpaceListParam;
import com.cgnpc.bbxpark.space.dto.param.SpaceBasicInfoListParam;
import com.cgnpc.bbxpark.space.dto.param.SpaceBasicInfoParam;
import com.cgnpc.bbxpark.space.mapper.SpaceBasicInfoRepository;
import com.cgnpc.bbxpark.space.mapper.StationRepository;
import com.cgnpc.bbxpark.space.service.IParkSpaceService;
import com.cgnpc.bbxpark.space.service.ISpaceBasicInfoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class SpaceBasicInfoServiceImpl extends ServiceImpl<SpaceBasicInfoRepository, SpaceBasicInfo> implements ISpaceBasicInfoService {
	@Resource
	private IParkSpaceService parkSpaceService;
	@Resource
	private IDepartmentApiService iDepartmentApiService;
	@Resource
	private IUserApiService userApiService;
    @Resource
    private IDepartmentApiService departmentApiService;
	@Resource
	private StationRepository stationRepository;

	@Override
	public List<SpaceTreeModel> tree(SpaceBasicInfoListParam param) {
		//查询园区空间树
		ParkSpaceListParam param1 = new ParkSpaceListParam();
		if(param.getUserId() != null){
			List<SpaceBasicInfo> list = this.list(Wrappers.<SpaceBasicInfo>lambdaQuery().eq(SpaceBasicInfo::getManagerId, param.getUserId()));
			List<Long> spaceIdList = list.stream().map(SpaceBasicInfo::getSpaceId).collect(Collectors.toList());
			spaceIdList.add(-1L);
			param1.setSpaceIdList(spaceIdList);
		}
		List<ParkSpaceTreeModel> spaceTreeModels = parkSpaceService.tree(param1);

		List<SpaceBasicInfo> spaceList = this.list(Wrappers.<SpaceBasicInfo>lambdaQuery().eq(SpaceBasicInfo::getTenantId, WebFrameworkUtils.getHeaderTenantId()));
		Map<Long, SpaceBasicInfo> spaceBasicInfoMap = spaceList.stream().collect(Collectors.toMap(SpaceBasicInfo::getSpaceId, info -> info));

		return convertNode(spaceTreeModels, spaceBasicInfoMap);
	}

    @Override
    public List<SimpleSpaceBasicInfoModel> list(SpaceBasicInfoListParam param) {
        ParkSpaceListParam param1 = new ParkSpaceListParam();
        if(param.getType() != null){
            List<SpaceBasicInfo> list = this.list(Wrappers.<SpaceBasicInfo>lambdaQuery().eq(SpaceBasicInfo::getType, param.getType()));
            List<Long> spaceIdList = list.stream().map(SpaceBasicInfo::getSpaceId).collect(Collectors.toList());
            spaceIdList.add(-1L);
            param1.setSpaceIdList(spaceIdList);
        }
        List<ParkSpaceTreeModel> spaceTreeModels = parkSpaceService.tree(param1);
        return spaceTreeModels.stream().map(s->BeanUtils.convertTo(s,SimpleSpaceBasicInfoModel::new)).collect(Collectors.toList());
    }

    /**
	 * 根据空间基础信息标识获得空间基础信息详情信息.
	 * @Param [id] 空间基础信息标识
	 * @Return 空间基础信息详情信息
	 */
	@Override
	public SpaceBasicInfoModel detail(Long id) {
		//查询园区空间信息
		ParkSpace parkSpace = parkSpaceService.getById(id);
		AssertUtils.notNull(parkSpace, SystemResultCode.RESULT_DATA_NONE.message());
		//查询上级空间信息
		ParkSpace parentSpace = parkSpaceService.getById(parkSpace.getParentSpaceId());
		//查询空间基础信息
		SpaceBasicInfo space = this.getOne(Wrappers.<SpaceBasicInfo>lambdaQuery().eq(SpaceBasicInfo::getSpaceId, id));

		//数据组装
		SpaceBasicInfoModel spaceModel = space == null ? new SpaceBasicInfoModel() : BeanUtils.convertTo(space,SpaceBasicInfoModel::new);
		spaceModel.setId(parkSpace.getId());
		spaceModel.setSpaceName(parkSpace.getSpaceName());
		spaceModel.setParentSpaceName(parentSpace != null ? parentSpace.getSpaceName() : "");
		spaceModel.setSpaceCode(parkSpace.getSpaceCode());
		spaceModel.setOrderCode(parkSpace.getOrderCode());
		//部门信息
		if(spaceModel.getDepartmentId() != null){
			OrgDepartmentNode deviceInfoDomain = Objects.requireNonNull(iDepartmentApiService.getOrgByNo(spaceModel.getDepartmentId()));
			spaceModel.setDepartmentName(deviceInfoDomain.getDeptName());
		}
		//工位管理员信息
		if(spaceModel.getManagerId() != null){
			UserInfoModel userInfoModel = Objects.requireNonNull(userApiService.detail(spaceModel.getManagerId()));
			spaceModel.setManagerName(userInfoModel.getUserName());
			spaceModel.setManagerStaffid(userInfoModel.getStaffid());
		}
		return spaceModel;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean edit(SpaceBasicInfoParam param) {
		//是否办公区空间
		boolean flag = param.getType() == 1;
		AssertUtils.isFalse(flag && param.getManagerId() == null, "工位管理员不能为空");

		ParkSpace parkSpace = parkSpaceService.getById(param.getId());
		AssertUtils.notNull(parkSpace, SystemResultCode.RESULT_DATA_NONE.message());

		SpaceBasicInfo space = this.getOne(Wrappers.<SpaceBasicInfo>lambdaQuery().eq(SpaceBasicInfo::getSpaceId, param.getId()));
		space = space == null ? new SpaceBasicInfo() : space;

		parkSpace.setOrderCode(param.getOrderCode());
		parkSpace.setSpaceName(param.getSpaceName());

		space.setSpaceId(parkSpace.getId());
		space.setDepartmentId(param.getDepartmentId());
		space.setManagerId(flag ? param.getManagerId() : null);
		space.setArea(param.getArea());
		space.setCapacity(param.getCapacity());
		space.setPurpose(param.getPurpose());
		space.setType(param.getType());
        if(param.getDepartmentId() != null){
            OrgDepartmentNode depart = departmentApiService.getOrgByNo(param.getDepartmentId());
            if(depart != null){
                space.setDepartmentName(depart.getDeptName());
            }
        }

		parkSpaceService.updateById(parkSpace);
		if(space.getId() != null){
			if(!flag){
				stationRepository.delete(Wrappers.<Station>lambdaQuery().eq(Station::getSpaceId, space.getSpaceId()));
			}
			this.update(Wrappers.<SpaceBasicInfo>lambdaUpdate()
					.set(SpaceBasicInfo::getSpaceId, space.getSpaceId())
					.set(SpaceBasicInfo::getDepartmentId, space.getDepartmentId())
                    .set(SpaceBasicInfo::getDepartmentName,space.getDepartmentName())
					.set(SpaceBasicInfo::getManagerId, space.getManagerId())
					.set(SpaceBasicInfo::getArea, space.getArea())
					.set(SpaceBasicInfo::getCapacity, space.getCapacity())
					.set(SpaceBasicInfo::getPurpose, space.getPurpose())
					.set(SpaceBasicInfo::getType, space.getType())
					.eq(SpaceBasicInfo::getId, space.getId()));
		}else {
			this.save(space);
		}
		return true;
	}


	/**
	 * 递归转换ParkSpaceTreeModel到SpaceTreeModel，并设置type字段
	 */
	private List<SpaceTreeModel> convertNode(List<ParkSpaceTreeModel> parkSpaces, Map<Long, SpaceBasicInfo> spaceBasicInfoMap) {
		if (parkSpaces == null || parkSpaces.isEmpty()) {
			return Collections.emptyList();
		}
		return parkSpaces.stream().map(parkSpace -> {
			SpaceTreeModel spaceTreeModel = new SpaceTreeModel();
			spaceTreeModel.setId(parkSpace.getId());
			spaceTreeModel.setParentSpaceId(parkSpace.getParentSpaceId());
			spaceTreeModel.setSpaceCode(parkSpace.getParentSpaceCode());
			spaceTreeModel.setSpaceName(parkSpace.getSpaceName());
			spaceTreeModel.setOrderCode(parkSpace.getOrderCode());
			// 设置type字段（从SpaceBasicInfo中获取）
			SpaceBasicInfo spaceBasicInfo = spaceBasicInfoMap.get(parkSpace.getId());
			if (spaceBasicInfo != null) {
				spaceTreeModel.setType(spaceBasicInfo.getType());
				// 分配按钮权限
				Boolean flag = spaceBasicInfo.getType() == 1 && userApiService.getCurrentStaffNo().equals(spaceBasicInfo.getManagerId());
				spaceTreeModel.setStationAllotAuth(flag);
			}
			// 递归处理子节点
			if (parkSpace.getChildren() != null && !parkSpace.getChildren().isEmpty()) {
				spaceTreeModel.setChildren(convertNode(parkSpace.getChildren(), spaceBasicInfoMap));
			}
			return spaceTreeModel;
		}).collect(Collectors.toList());
	}
}
