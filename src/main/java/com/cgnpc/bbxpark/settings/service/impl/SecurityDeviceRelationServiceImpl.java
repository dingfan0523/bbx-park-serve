
package com.cgnpc.bbxpark.settings.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.cgnpc.bbxpark.acl.uic.service.IUserApiService;
import com.cgnpc.bbxpark.settings.domain.SecurityDeviceRelation;
import com.cgnpc.bbxpark.settings.dto.model.SecurityDeviceRelationModel;
import com.cgnpc.bbxpark.settings.dto.param.SecurityDeviceRelationListParam;
import com.cgnpc.bbxpark.settings.dto.param.SecurityDeviceRelationParam;
import com.cgnpc.bbxpark.settings.mapper.SecurityDeviceRelationRepository;
import com.cgnpc.bbxpark.settings.service.ISecurityDeviceRelationService;
import com.cgnpc.bbxpark.common.utils.AssertUtils;
import com.cgnpc.bbxpark.common.utils.BeanUtils;
import com.cgnpc.bbxpark.common.utils.WebFrameworkUtils;
import com.cgnpc.bbxpark.device.domain.IocDevice;
import com.cgnpc.bbxpark.device.service.IIocDeviceService;
import com.cgnpc.cud.core.service.BaseServiceImpl;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/***
 * @Description 安全管理员设备关联服务实现
 * @author huangyongtao
 * @date 2025/8/1 11:52
 */
@Service("securityDeviceRelationService")
public class SecurityDeviceRelationServiceImpl extends BaseServiceImpl<SecurityDeviceRelationRepository, SecurityDeviceRelation> implements ISecurityDeviceRelationService {

	@Autowired
	private SecurityDeviceRelationRepository securityDeviceRelationRepository;

	@Autowired
	private IIocDeviceService iocDeviceService;

    @Autowired
    private IUserApiService userApiService;

	/**
	 * 获取安全管理员设备关联列表.
	 * @Param param 安全管理员设备关联查询条件
	 * @Return 安全管理员设备关联信息列表
	 */
	@Override
	@SneakyThrows
	public List<SecurityDeviceRelationModel> list(SecurityDeviceRelationListParam param) {
		if (ObjectUtil.isEmpty(param.getSecurityManageId())) {
			return Collections.emptyList();
		}
		List<SecurityDeviceRelation> relations = this.list(Wrappers.<SecurityDeviceRelation>lambdaQuery().eq(SecurityDeviceRelation::getSecurityManageId, param.getSecurityManageId()));
		if (ObjectUtil.isEmpty(relations)) {
			return Collections.emptyList();
		}
		List<SecurityDeviceRelationModel> models = BeanUtils.convertListTo(relations, SecurityDeviceRelationModel::new);
		List<Long> deviceIds = relations.stream().map(SecurityDeviceRelation::getDeviceId).collect(Collectors.toList());
		List<IocDevice> devices = iocDeviceService.list(Wrappers.<IocDevice>lambdaQuery().select(IocDevice::getId, IocDevice::getDeviceName).in(IocDevice::getId, deviceIds));
		Map<Long, String> deviceNameMap = CollectionUtil.isNotEmpty(devices) ? devices.stream().collect(Collectors.toMap(IocDevice::getId, IocDevice::getDeviceName)) : new HashMap<>();
		models.stream().forEach(relation -> {
			relation.setDeviceName(deviceNameMap.get(relation.getDeviceId()));
		});
		return models;
	}

	/**
	 * 新增安全管理员设备关联.
	 * @Param param 安全管理员设备关联信息
	 * @Return 新增安全管理员设备关联是否成功
	 */
	@Override
	public Boolean add(SecurityDeviceRelationParam param) {
		AssertUtils.notNull(param.getSecurityManageId(), "安全员管理id不能为空");
		//删除所有关联的空间
		this.remove(Wrappers.<SecurityDeviceRelation>lambdaQuery().eq(SecurityDeviceRelation::getSecurityManageId, param.getSecurityManageId()));
		if (CollectionUtil.isNotEmpty(param.getDeviceIds())) {
			// 将输入参数转换为目标对象列表
			List<SecurityDeviceRelation> regionSpaceRelations = param.getDeviceIds().stream()
					.map(spaceId -> {
						SecurityDeviceRelation relation = new SecurityDeviceRelation();
						relation.setDeviceId(spaceId);
						relation.setSecurityManageId(param.getSecurityManageId());
						// 设置其他必要的字段
						return relation;
					})
					.collect(Collectors.toList());
			this.saveBatch(regionSpaceRelations);
		}
		return true;
	}

	@Override
	public List<Long> queryAllDeviceIdByLoginUser() {
		String securityStaffid = userApiService.getCurrentStaffNo();
		Long tenantId = WebFrameworkUtils.getHeaderTenantId();
		List<Long> deviceIds = securityDeviceRelationRepository.queryAllDeviceIdByLoginUser(securityStaffid, tenantId);
		if (CollectionUtil.isNotEmpty(deviceIds)){
			return deviceIds;
		}
		return Collections.emptyList();
	}

	@Override
	public List<SecurityDeviceRelationModel> findAllBySpaceIds(List<Long> spaceIds) {
        if(CollectionUtil.isEmpty(spaceIds)){
            return Collections.emptyList();
        }
		List<SecurityDeviceRelationModel> result = securityDeviceRelationRepository.findAllBySpaceIds(spaceIds);
		return result;
	}
}
