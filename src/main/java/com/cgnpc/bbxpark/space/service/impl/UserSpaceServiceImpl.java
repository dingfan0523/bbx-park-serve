
package com.cgnpc.bbxpark.space.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cgnpc.bbxpark.acl.uic.service.IUserApiService;
import com.cgnpc.bbxpark.common.constant.Constant;
import com.cgnpc.bbxpark.common.constant.SystemResultCode;
import com.cgnpc.bbxpark.common.enums.Status;
import com.cgnpc.bbxpark.common.exception.GenericException;
import com.cgnpc.bbxpark.common.utils.AssertUtils;
import com.cgnpc.bbxpark.common.utils.BeanUtils;
import com.cgnpc.bbxpark.common.utils.WebFrameworkUtils;
import com.cgnpc.bbxpark.space.domain.ParkSpace;
import com.cgnpc.bbxpark.space.domain.UserSpace;
import com.cgnpc.bbxpark.space.dto.model.ParkSpaceModel;
import com.cgnpc.bbxpark.space.dto.model.ParkSpaceTreeModel;
import com.cgnpc.bbxpark.space.dto.model.UserInfoModel;
import com.cgnpc.bbxpark.space.dto.model.UserSpaceModel;
import com.cgnpc.bbxpark.space.dto.param.ParkSpaceListParam;
import com.cgnpc.bbxpark.space.dto.param.UserInfoSpaceParam;
import com.cgnpc.bbxpark.space.dto.param.UserSpaceListParam;
import com.cgnpc.bbxpark.space.dto.param.UserSpaceParam;
import com.cgnpc.bbxpark.space.mapper.UserSpaceRepository;
import com.cgnpc.bbxpark.space.service.IUserSpaceService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/***
 * @Description 用户与空间访问权限服务实现
 * @author huangyongtao
 * @date 2024/7/1 16:36
 */
@Service
public class UserSpaceServiceImpl extends ServiceImpl<UserSpaceRepository, UserSpace> implements IUserSpaceService {

	@Resource
	private ParkSpaceServiceImpl parkSpaceInfoService;
    @Autowired
    private IUserApiService userApiService;


	/***
	 * @Description 获取用户相关的空间树
	 * @author huangyongtao
	 * @date 2024/7/2 11:09
	 * @param param
	 */
	@Override
	public List<ParkSpaceTreeModel> findTree(UserSpaceListParam param){
		if(ObjectUtil.isEmpty(param.getUserId())){
			throw GenericException.fail("请传入用户信息");
		}
		param.setTenantId(WebFrameworkUtils.getHeaderTenantId());
		//查询所有空间集合
		List<ParkSpace> parkSpaces = parkSpaceInfoService.list(Wrappers.<ParkSpace>lambdaQuery()
                .eq(ParkSpace::getSpaceStatus, Status.enabled.getKey())
                .eq(ParkSpace::getTenantId,param.getTenantId())
                .orderByAsc(ParkSpace::getOrderCode).orderByDesc(ParkSpace::getUpdateTime));
		if(CollectionUtil.isEmpty(parkSpaces)){
			return new ArrayList<>();
		}
		List<ParkSpaceModel> parkSpaceModels = BeanUtils.convertListTo(parkSpaces, ParkSpaceModel::new);
		List<UserSpace> userSpaces = findUserSpaces(param);
		List<Long> spaceIds = new ArrayList<>();
		if(CollectionUtil.isNotEmpty(userSpaces)){
			spaceIds = userSpaces.stream().map(UserSpace::getSpaceId).collect(Collectors.toList());
		}
		List<Long> finalSpaceIds = spaceIds;
		parkSpaceModels.stream().forEach(space ->{
			if(finalSpaceIds.contains(space.getId())){
				space.setFlag(true);
			}
		});
		List<ParkSpaceTreeModel> nodeVOList = assembleTree(parkSpaceModels, Constant.SPACE_ROOT_ID);
		return nodeVOList;
	}

	/***
	 * @Description 获取用户空间树
	 * @author huangyongtao
	 * @date 2024/7/2 11:09
	 * @param param
	 */
	@Override
	public List<ParkSpaceTreeModel> tree(UserSpaceListParam param){
		param.setTenantId(WebFrameworkUtils.getHeaderTenantId());
		param.setUserId(WebFrameworkUtils.getHeaderUserId());
		if(ObjectUtil.isNotEmpty(parkSpaceInfoService.findTenantIdByUserId())){
			return parkSpaceInfoService.tree(new ParkSpaceListParam());
		}
		List<UserSpace> userSpaces = findUserSpaces(param);
		if(CollectionUtil.isEmpty(userSpaces)){
			return new ArrayList<>();
		}
		List<Long> spaceIds = userSpaces.stream().map(UserSpace::getSpaceId).collect(Collectors.toList());
		//查询所有空间集合
		List<ParkSpace> parkSpaces = parkSpaceInfoService.list(Wrappers.<ParkSpace>lambdaQuery().eq(ParkSpace::getSpaceStatus, Status.enabled.getKey()).eq(ObjectUtil.isNotEmpty(param.getTenantId()), ParkSpace::getTenantId,param.getTenantId()).in(CollectionUtil.isNotEmpty(spaceIds), ParkSpace::getId, spaceIds).orderByAsc(ParkSpace::getOrderCode).orderByDesc(ParkSpace::getCreateTime));
		if(CollectionUtil.isEmpty(parkSpaces)){
			return new ArrayList<>();
		}
		List<ParkSpaceModel> parkSpaceModels = BeanUtils.convertListTo(parkSpaces, ParkSpaceModel::new);
		return handleTree(parkSpaceModels);
	}
	/**
	 * 获取用户与空间访问权限列表.
	 * @Param param 用户与空间访问权限查询条件
	 * @Return 用户与空间访问权限信息列表
	 */
	@Override
	@SneakyThrows
	public List<UserSpaceModel> list(UserSpaceListParam param) {
		param.setTenantId(WebFrameworkUtils.getHeaderTenantId());
		List<UserSpace> userSpaces = findUserSpaces(param);
		if(CollectionUtil.isEmpty(userSpaces)){
			return new ArrayList<>();
		}
		return BeanUtils.convertListTo(userSpaces, UserSpaceModel::new);
	}

	/**
	 * 获取用户与空间访问权限列表.
	 * @Param param 用户与空间访问权限查询条件
	 * @Return 用户与空间访问权限信息列表
	 */
	@Override
	public List<Long> findSpaceIds() {
		UserSpaceListParam param = new UserSpaceListParam();
		param.setTenantId(WebFrameworkUtils.getHeaderTenantId());
		param.setUserId(WebFrameworkUtils.getHeaderUserId());
		if(ObjectUtil.isNotEmpty(parkSpaceInfoService.findTenantIdByUserId())){
			//查询所有空间集合
			List<ParkSpace> parkSpaces = parkSpaceInfoService.list(Wrappers.<ParkSpace>lambdaQuery().eq(ObjectUtil.isNotEmpty(WebFrameworkUtils.getHeaderTenantId()), ParkSpace::getTenantId,WebFrameworkUtils.getHeaderTenantId())
					.eq(ParkSpace::getSpaceStatus, Status.enabled.getKey()));
			if(CollectionUtil.isEmpty(parkSpaces)){
				return new ArrayList<>();
			}
			return parkSpaces.stream().map(p->p.getId()).collect(Collectors.toList());
		}
		List<UserSpace> userSpaces = findUserSpaces(param);
		if(CollectionUtil.isEmpty(userSpaces)){
			return new ArrayList<>();
		}
		return userSpaces.stream().map(p->p.getSpaceId()).collect(Collectors.toList());
	}

	@Override
	public List<UserInfoModel> findUserInfoBySpace(UserInfoSpaceParam param) {
		param.setTenantId(WebFrameworkUtils.getHeaderTenantId());
		List<UserInfoModel> list = this.getBaseMapper().findUserInfoBySpace(param);
        if(CollectionUtil.isEmpty(list)){
            return Collections.emptyList();
        }
        List<String> staffIds = list.stream().map(UserInfoModel::getId).collect(Collectors.toList());
        return userApiService.getByStaffNos(staffIds);
	}

	/***
	 * @Description 条件查询用户与空间访问权限列表
	 * @author huangyongtao
	 * @date 2024/7/2 10:26
	 * @param param
	 */
	private List<UserSpace> findUserSpaces(UserSpaceListParam param){
		LambdaQueryWrapper<UserSpace> queryWrapper = Wrappers.lambdaQuery();
		queryWrapper.eq(ObjectUtil.isNotEmpty(param.getUserId()), UserSpace::getUserId, param.getUserId())
				.eq(ObjectUtil.isNotEmpty(param.getSpaceId()), UserSpace::getSpaceId, param.getSpaceId())
				.eq(ObjectUtil.isNotEmpty(param.getTenantId()), UserSpace::getTenantId, param.getTenantId());
		List<UserSpace> userSpaces = this.list(queryWrapper);
		if(CollectionUtil.isEmpty(userSpaces)){
			return new ArrayList<>();
		}
		return userSpaces;
	}

	/**
	 * 新增用户与空间访问权限.
	 * @Param param 用户与空间访问权限信息
	 * @Return 新增用户与空间访问权限是否成功
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean add(UserSpaceParam param) {
		if(CollectionUtil.isEmpty(param.getRoleIds()) && CollectionUtil.isEmpty(param.getDepartIds()) && ObjectUtil.isEmpty(param.getUserIds())){
			return Boolean.TRUE;
		}
        List<String> userIdList = new ArrayList<>();
		if(CollectionUtil.isNotEmpty(param.getDepartIds())){
//			DepartmentMemberListParam memberListParam = new DepartmentMemberListParam();
//			memberListParam.setDepartmentIds(param.getDepartIds());
//			List<DepartmentMemberDomain> memberDomains = Objects.requireNonNull(departmentMemberFeignClient.list(memberListParam).getBody()).getResult();
//			if(CollectionUtil.isEmpty(memberDomains) ){
//				throw GenericException.fail("所选部门未配置用户，请确认。");
//			}
//			param.setUserIds(memberDomains.stream().map(DepartmentMemberDomain::getUserId).collect(Collectors.toList()));

		}else if(CollectionUtil.isNotEmpty(param.getRoleIds())){
//			UserRoleParam userRoleParam = new UserRoleParam();
//			userRoleParam.setRoleIds(param.getRoleIds());
//			List<UserRoleDomain> userRoleDomains = Objects.requireNonNull(userRoleFeignClient.list(userRoleParam).getBody()).getResult();
//			if(CollectionUtil.isEmpty(userRoleDomains) ){
//				throw GenericException.fail("所选角色未配置用户，请确认。");
//			}
//			param.setUserIds(userRoleDomains.stream().map(UserRoleDomain::getUserId).collect(Collectors.toList()));
		}else if(CollectionUtil.isNotEmpty(param.getUserIds())){
            userIdList.addAll(param.getUserIds());
        }

		Long tenantId = WebFrameworkUtils.getHeaderTenantId();
		List<UserSpace> useSpaces = new ArrayList<>();
		if(CollectionUtil.isEmpty(userIdList) || ObjectUtil.isEmpty(tenantId)) {
			return Boolean.FALSE;
		}
//		if(CollectionUtil.isEmpty(param.getSpaceIds())){
//			return removeByUserIds(param.getUserIds());
//		}
        if(CollectionUtil.isEmpty(userIdList)){
            return removeBySpaceIds(Collections.singletonList(param.getSpaceId()));
        }
		//校验空间是否删除
		checkSpace(Collections.singletonList(param.getSpaceId()), tenantId);
        userIdList.forEach(userId->{
            UserSpace userSpace = new UserSpace();
            userSpace.setId(null);
            userSpace.setUserId(userId);
            userSpace.setSpaceId(param.getSpaceId());
            useSpaces.add(userSpace);
		});
		if(CollectionUtil.isEmpty(useSpaces)){
			return Boolean.FALSE;
		}
//		removeByUserIds(param.getUserIds());
        removeBySpaceIds(Collections.singletonList(param.getSpaceId()));
		return this.saveBatch(useSpaces);
	}

	/***
	 * @Description 校验空间是否删除
	 * @author huangyongtao
	 * @date 2024/7/2 16:04
	 * @param spaceIds
	 * @param tenantId
	 */
	private void checkSpace(List<Long> spaceIds, Long tenantId){
		List<ParkSpace> parkSpaces = parkSpaceInfoService.getBaseMapper().selectList(Wrappers.<ParkSpace>lambdaQuery().eq(ParkSpace::getTenantId,tenantId).eq(ParkSpace::getSpaceStatus, Status.disabled.getKey()).in(ParkSpace::getId,spaceIds));
		if(CollectionUtil.isNotEmpty(parkSpaces)){
			throw GenericException.fail(parkSpaces.stream().map(ParkSpace::getSpaceName).collect(Collectors.joining(",")) + "的空间信息已删除，请重新分配。");
		}
	}

	/***
	 * @Description 根据用户id集合删除
	 * @author huangyongtao
	 * @date 2024/7/2 9:46
	 * @param userIds
	 */
	@Transactional(rollbackFor = Exception.class)
	public Boolean removeByUserIds(List<Long> userIds){
		return this.remove(new LambdaQueryWrapper<UserSpace>().in(CollectionUtil.isNotEmpty(userIds), UserSpace::getUserId, userIds));
	}

	/***
	 * @Description 根据空间id集合删除
	 * @author huangyongtao
	 * @date 2024/7/2 9:46
	 * @param spaceIds
	 */
	@Transactional(rollbackFor = Exception.class)
	public Boolean removeBySpaceIds(List<Long> spaceIds){
		return this.remove(new LambdaQueryWrapper<UserSpace>().in(CollectionUtil.isNotEmpty(spaceIds), UserSpace::getSpaceId, spaceIds));
	}

	/**
	 * 删除用户与空间访问权限.
	 * @Param id 用户与空间访问权限标识
	 * @Return 删除用户与空间访问权限是否成功
	 */
	@Override
	public Boolean remove(Long id) {
		UserSpace userSpace = this.getById(id);
		AssertUtils.notNull(userSpace, SystemResultCode.RESULT_DATA_NONE.message());
		return this.removeById(id);
	}

	/**
	 * 批量删除用户与空间访问权限.
	 * @Param ids 用户与空间访问权限标识列表
	 * @Return 批量删除用户与空间访问权限是否成功
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean removeBatch(List<Long> ids) {
		return this.removeBatch(ids);
	}

	/***
	 * @Description 将空间信息转换为树形结构数据
	 * @author huangyongtao
	 * @date 2024/7/2 11:26
	 * @param parkSpaceModels
	 * @param parentId
	 */
	private List<ParkSpaceTreeModel> assembleTree(List<ParkSpaceModel> parkSpaceModels, Long parentId) {
		List<ParkSpaceTreeModel> rootList = new LinkedList<>();
		for (ParkSpaceModel spaceInfo : parkSpaceModels) {
			if (parentId.equals(spaceInfo.getParentSpaceId())) {
				// 子节点信息处理
				ParkSpaceTreeModel node = new ParkSpaceTreeModel();
				BeanUtil.copyProperties(spaceInfo, node);
				// 获取子节点
				List<ParkSpaceTreeModel> childrenNode = this.assembleTree(parkSpaceModels, node.getId());
				node.setChildren(childrenNode);
				if(CollectionUtil.isNotEmpty(childrenNode)){
					node.setFlag(Boolean.FALSE);
				}
				rootList.add(node);
			}
		}
		return rootList;
	}

	private List<ParkSpaceTreeModel> handleTree(List<ParkSpaceModel> parkSpaceModels) {
		Map<Long, List<ParkSpaceModel>> parentIdMap = parkSpaceModels.stream().collect(Collectors.groupingBy(ParkSpaceModel::getParentSpaceId));
		Map<Long, ParkSpaceModel> idMap = parkSpaceModels.stream().collect(Collectors.toMap(ParkSpaceModel::getId, Function.identity()));
		List<Long> parentIds = new ArrayList<>();
		for (Map.Entry<Long, List<ParkSpaceModel>> parent : parentIdMap.entrySet()) {
			if(!idMap.containsKey(parent.getKey())){
				parentIds.add(parent.getKey());
			}
		}
		List<ParkSpaceTreeModel> nodeVOList = buildTree(parkSpaceModels, parentIds);
		return nodeVOList;
	}

	private List<ParkSpaceTreeModel> buildTree(List<ParkSpaceModel> parkSpaceModels, List<Long> parentIds) {
		List<ParkSpaceTreeModel> rootList= new LinkedList<>();
		for (ParkSpaceModel spaceInfo : parkSpaceModels) {
			if (parentIds.contains(spaceInfo.getParentSpaceId())) {
				// 子节点信息处理
				ParkSpaceTreeModel node = new ParkSpaceTreeModel();
				BeanUtil.copyProperties(spaceInfo, node);
				// 获取子节点
				List<ParkSpaceTreeModel> childrenNode = this.buildTree(parkSpaceModels, Arrays.asList(node.getId()));
				node.setChildren(childrenNode);
				rootList.add(node);
			}
		}
		return rootList;
	}

}
