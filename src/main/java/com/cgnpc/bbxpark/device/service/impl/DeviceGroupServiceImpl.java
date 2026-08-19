
package com.cgnpc.bbxpark.device.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.cgnpc.bbxpark.acl.uic.service.IUserApiService;
import com.cgnpc.bbxpark.common.constant.Constant;
import com.cgnpc.bbxpark.common.constant.SystemResultCode;
import com.cgnpc.bbxpark.common.utils.AssertUtils;
import com.cgnpc.bbxpark.common.utils.BeanUtils;
import com.cgnpc.bbxpark.common.utils.WebFrameworkUtils;
import com.cgnpc.bbxpark.device.domain.DeviceGroup;
import com.cgnpc.bbxpark.device.domain.DeviceGroupRel;
import com.cgnpc.bbxpark.device.dto.model.DeviceGroupModel;
import com.cgnpc.bbxpark.device.dto.model.DeviceGroupTreeModel;
import com.cgnpc.bbxpark.device.dto.param.DeviceGroupListParam;
import com.cgnpc.bbxpark.device.dto.param.DeviceGroupParam;
import com.cgnpc.bbxpark.device.mapper.DeviceGroupRepository;
import com.cgnpc.bbxpark.device.service.IDeviceGroupRelService;
import com.cgnpc.bbxpark.device.service.IDeviceGroupService;
import com.cgnpc.bbxpark.space.dto.model.UserInfoModel;
import com.cgnpc.cud.core.service.BaseServiceImpl;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/***
 * @Description 设备分组服务实现
 * @author huangyongtao
 * @date 2024/8/12 13:57
 */
@Service
public class DeviceGroupServiceImpl extends BaseServiceImpl<DeviceGroupRepository, DeviceGroup> implements IDeviceGroupService {
	@Autowired
    IDeviceGroupRelService deviceGroupRelService;

    @Autowired
    private IUserApiService userApiService;

	/***
	 * @Description 获取用户相关的空间树
	 * @author huangyongtao
	 * @date 2024/7/2 11:09
	 * @param param
	 */
	@Override
	public List<DeviceGroupTreeModel> findTree(DeviceGroupListParam param){
		List<DeviceGroup> deviceGroups = findCondition();
		List<DeviceGroupModel> deviceGroupModels = BeanUtils.convertListTo(deviceGroups, DeviceGroupModel::new);
		return assembleTree(deviceGroupModels, Constant.SPACE_ROOT_ID, Constant.SPACE_ROOT_ID + 1);
	}

	/**
	 * 根据设备分组标识获得设备分组详情信息.
	 * @Param [id] 设备分组标识
	 * @Return 设备分组详情信息
	 */
	@Override
	public DeviceGroupModel detail(DeviceGroupParam param) {
		DeviceGroup deviceGroup = this.getById(param.getId());
		AssertUtils.notNull(deviceGroup, SystemResultCode.RESULT_DATA_NONE.message());
		DeviceGroupModel model = BeanUtils.convertTo(deviceGroup, DeviceGroupModel::new);
//		UserInfoModel user = userApiService.getByStaffNo(deviceGroup.getCreatorId());
//		if(ObjectUtil.isNotEmpty(user)){
//			model.setCreateUserName(user.getUserName());
//			model.setCreateUserNo(user.getStaffid());
//		}
		if (deviceGroup.getGroupParentId() != 0) {
			DeviceGroup parentDeviceGroup = getById(deviceGroup.getGroupParentId());
			if(ObjectUtil.isNotEmpty(parentDeviceGroup)){
				model.setParentGroupName(parentDeviceGroup.getGroupName());
			}
		}
		return model;
	}

	/**
	 * 获取设备分组列表.
	 * @Param param 设备分组查询条件
	 * @Return 设备分组信息列表
	 */
	@Override
	@SneakyThrows
	public List<DeviceGroupModel> list(DeviceGroupListParam param) {
		List<Long> groupIds = new ArrayList<>();
		if(ObjectUtil.isNotEmpty(param.getDeviceId())){
			List<DeviceGroupRel> groupRelList = deviceGroupRelService.list(new LambdaQueryWrapper<DeviceGroupRel>().select(DeviceGroupRel::getGroupId).eq(DeviceGroupRel::getDeviceId,param.getDeviceId()));
			if(CollectionUtil.isNotEmpty(groupRelList)){
				groupIds = groupRelList.stream().map(DeviceGroupRel::getGroupId).collect(Collectors.toList());
			}
		}
		if(CollectionUtil.isEmpty(groupIds)){
			return new ArrayList<>();
		}
		param.setTenantId(WebFrameworkUtils.getHeaderTenantId());
		LambdaQueryWrapper<DeviceGroup> queryWrapper = Wrappers.lambdaQuery();
		queryWrapper.eq(ObjectUtil.isNotEmpty(param.getGroupParentId()), DeviceGroup::getGroupParentId, param.getGroupParentId())
				.like(ObjectUtil.isNotEmpty(param.getGroupName()), DeviceGroup::getGroupName, param.getGroupName())
				.eq(ObjectUtil.isNotEmpty(param.getTenantId()), DeviceGroup::getTenantId, param.getTenantId())
				.in(CollectionUtil.isNotEmpty(groupIds), DeviceGroup::getId, groupIds)
				.orderByAsc(DeviceGroup::getSortOrder)
				.orderByDesc(DeviceGroup::getCreateTime);
		List<DeviceGroup> deviceGroups = this.list(queryWrapper);
		if(CollectionUtil.isEmpty(deviceGroups)){
			return new ArrayList<>();
		}
		return BeanUtils.convertListTo(deviceGroups, DeviceGroupModel::new);
	}

	/**
	 * 新增设备分组.
	 * @Param param 设备分组信息
	 * @Return 新增设备分组是否成功
	 */
	@Override
	public Boolean add(DeviceGroupParam param) {
		param.setTenantId(WebFrameworkUtils.getHeaderTenantId());
		verifySaveOrEdit(param);
		DeviceGroup deviceGroup = BeanUtils.convertTo(param, DeviceGroup::new);
		deviceGroup.setId(null);
        deviceGroup.setCreateBy(userApiService.getCurrentStaffName());
		return this.save(deviceGroup);
	}

	/**
	 * 删除设备分组.
	 * @Param id 设备分组标识
	 * @Return 删除设备分组是否成功
	 */
	@Transactional(rollbackFor = Exception.class)
	@Override
	public Boolean remove(DeviceGroupParam param) {
		DeviceGroup deviceGroup = this.getById(param.getId());
		AssertUtils.notNull(deviceGroup, SystemResultCode.RESULT_DATA_NONE.message());
		List<DeviceGroup> deviceGroups = findCondition();
		List<DeviceGroup> deviceGroupChild = getChild(deviceGroups, param.getId());
		List<Long> deleteIds = new ArrayList<>();
		deleteIds.add(param.getId());
		if(CollectionUtil.isNotEmpty(deviceGroupChild)){
			List<Long> ids = deviceGroupChild.stream().map(DeviceGroup::getId).collect(Collectors.toList());
			deleteIds.addAll(ids);
		}
		deviceGroupRelService.remove(new LambdaQueryWrapper<DeviceGroupRel>().in(DeviceGroupRel::getGroupId,deleteIds));
		return this.removeByIds(deleteIds);
	}

	/**
	 * 编辑设备分组信息.
	 * @Param param 设备分组信息
	 * @Return 编辑设备分组是否成功
	 */
	@Override
	public Boolean edit(DeviceGroupParam param) {
		DeviceGroup deviceGroup = this.getById(param.getId());
		AssertUtils.notNull(deviceGroup, SystemResultCode.RESULT_DATA_NONE.message());
		param.setTenantId(WebFrameworkUtils.getHeaderTenantId());
		verifySaveOrEdit(param);
		DeviceGroup editParam = BeanUtils.convertTo(param, DeviceGroup::new);
		editParam.setGroupParentId(deviceGroup.getGroupParentId());
		editParam.setGroupCode(deviceGroup.getGroupCode());
        editParam.setUpdateBy(userApiService.getCurrentStaffName());
		return this.updateById(editParam);
	}

	/***
	 * @Description 校验编码
	 * @author huangyongtao
	 * @date 2024/8/12 15:10
	 * @param tenantId
	 * @param id
	 * @param groupCode
	 */
	private boolean verifyCode(Long tenantId, Long id, String groupCode) {
		return count(Wrappers.<DeviceGroup>lambdaQuery().eq(DeviceGroup::getTenantId, tenantId)
				.eq(DeviceGroup::getGroupCode, groupCode)
				//如果id不为空,那么说明是编辑,编辑时仅校验空间编码是否被园区下其他空间使用
				.ne(id != null, DeviceGroup::getId, id)) > 0;
	}

	/***
	 * @Description 校验名称
	 * @author huangyongtao
	 * @date 2024/8/12 15:11
	 * @param parentId
	 * @param id
	 * @param groupName
	 */
	private boolean verifyName(Long parentId, Long id, String groupName) {
		return count(Wrappers.<DeviceGroup>lambdaQuery().eq(DeviceGroup::getGroupParentId, parentId)
				.eq(DeviceGroup::getGroupName, groupName)
				//如果id不为空,那么说明是编辑,需排除自身
				.ne(id != null, DeviceGroup::getId, id)) > 0;
	}

	/***
	 * @Description 新增编辑校验
	 * @author huangyongtao
	 * @date 2024/8/12 15:16
	 * @param param
	 */
	private void verifySaveOrEdit(DeviceGroupParam param){
		//同一层级校验名称重复
//		AssertUtils.isFalse(verifyName(param.getGroupParentId(), param.getId(), param.getGroupName()), "设备分组名称重复");
		//同一园区校验编码重复
		AssertUtils.isFalse(verifyCode(param.getTenantId(), param.getId(), param.getGroupCode()), "设备分组编码重复");
		//非最上级空间,需判断上级空间是否存在
		if (ObjectUtil.isNotEmpty(param.getGroupParentId()) && param.getGroupParentId() != 0) {
			DeviceGroup deviceGroup = getById(param.getGroupParentId());
			AssertUtils.isFalse(deviceGroup == null, "上级设备分组不存在");
		}
	}

	/***
	 * @Description 将设备分组转换为树形结构数据
	 * @author huangyongtao
	 * @date 2024/8/12 16:08
	 * @param deviceGroupModels
	 * @param parentId
	 */
	private List<DeviceGroupTreeModel> assembleTree(List<DeviceGroupModel> deviceGroupModels, Long parentId, Long level) {
		List<DeviceGroupTreeModel> rootList = new LinkedList<>();
		for (DeviceGroupModel groupModel : deviceGroupModels) {
			if (parentId.equals(groupModel.getGroupParentId())) {
				// 子节点信息处理
				DeviceGroupTreeModel node = new DeviceGroupTreeModel();
				BeanUtil.copyProperties(groupModel, node);
				node.setLevel(level);
				// 获取子节点
				List<DeviceGroupTreeModel> childrenNode = this.assembleTree(deviceGroupModels, node.getId(), level+1);
				node.setChildren(childrenNode);
				rootList.add(node);
			}
		}
		return rootList;
	}

	/***
	 * @Description 获取子集
	 * @author huangyongtao
	 * @date 2024/8/12 16:18
	 * @param deviceGroupModels
	 * @param parentId
	 */
	private List<DeviceGroup> getChild(List<DeviceGroup> deviceGroupModels, Long parentId) {
		List<DeviceGroup> child = new LinkedList<>();
		for (DeviceGroup groupModel : deviceGroupModels) {
			if (parentId.equals(groupModel.getGroupParentId())) {
				// 获取子节点
				List<DeviceGroup> childrenNode = this.getChild(deviceGroupModels, groupModel.getId());
				child.add(groupModel);
				if(CollectionUtil.isNotEmpty(childrenNode)){
					child.addAll(childrenNode);
				}
			}
		}
		return child;
	}

	private List<DeviceGroup> findCondition(){
		List<DeviceGroup> deviceGroups = this.getBaseMapper().selectList(Wrappers.<DeviceGroup>lambdaQuery().eq(DeviceGroup::getTenantId,WebFrameworkUtils.getHeaderTenantId()).orderByAsc(DeviceGroup::getSortOrder).orderByDesc(DeviceGroup::getCreateTime));
		if(CollectionUtil.isEmpty(deviceGroups)){
			return new ArrayList<>();
		}
		return deviceGroups;
	}
}
