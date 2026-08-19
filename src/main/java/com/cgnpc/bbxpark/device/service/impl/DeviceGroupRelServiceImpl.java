
package com.cgnpc.bbxpark.device.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.bbxpark.common.utils.AssertUtils;
import com.cgnpc.bbxpark.common.utils.ConvertUtil;
import com.cgnpc.bbxpark.common.utils.WebFrameworkUtils;
import com.cgnpc.bbxpark.device.domain.DeviceGroupRel;
import com.cgnpc.bbxpark.device.dto.model.DeviceGroupRelModel;
import com.cgnpc.bbxpark.device.dto.model.IocDeviceModel;
import com.cgnpc.bbxpark.device.dto.param.DeviceGroupRelParam;
import com.cgnpc.bbxpark.device.dto.param.IocDevicePageParam;
import com.cgnpc.bbxpark.device.mapper.DeviceGroupRelRepository;
import com.cgnpc.bbxpark.device.service.IDeviceGroupRelService;
import com.cgnpc.bbxpark.device.service.IIocDeviceService;
import com.cgnpc.cud.core.service.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/***
 * @Description 设备分组关系服务实现
 * @author huangyongtao
 * @date 2024/8/12 13:57
 */
@Service
public class DeviceGroupRelServiceImpl extends BaseServiceImpl<DeviceGroupRelRepository, DeviceGroupRel> implements IDeviceGroupRelService {

    @Lazy
	@Autowired
    private IIocDeviceService iocDeviceService;

	public static final String MESSAGE = "设备分组不能为空";

	/**
	 * 新增设备分组关系.
	 * @Param param 设备分组关系信息
	 * @Return 新增设备分组关系是否成功
	 */
	@Override
	public Boolean addDeviceToGroup(DeviceGroupRelParam param) {
		AssertUtils.isTrue(ObjectUtil.isNotEmpty(param.getGroupId()), MESSAGE);
		List<DeviceGroupRel> rels = new ArrayList<>();
		List<Long> finalDeviceIds = getDeviceIds(param.getGroupId());
		param.getDeviceIdList().forEach(item->{
			if(!finalDeviceIds.contains(item)){
				DeviceGroupRel rel = new DeviceGroupRel();
				rel.setDeviceId(item);
				rel.setGroupId(param.getGroupId());
				rels.add(rel);
			}
		});
		return this.saveBatch(rels);
	}

	/**
	 * 删除设备分组关系.
	 * @Param id 设备分组关系标识
	 * @Return 删除设备分组关系是否成功
	 */
	@Override
	public Boolean removeDeviceToGroup(DeviceGroupRelParam param) {
		AssertUtils.isTrue(ObjectUtil.isNotEmpty(param.getGroupId()), MESSAGE);
		return this.remove(new LambdaQueryWrapper<DeviceGroupRel>().eq(DeviceGroupRel::getGroupId, param.getGroupId())
				.in(CollectionUtil.isNotEmpty(param.getDeviceIdList()),DeviceGroupRel::getDeviceId,param.getDeviceIdList())
				.eq(ObjectUtil.isNotEmpty(param.getDeviceId()),DeviceGroupRel::getDeviceId,param.getDeviceId()));
	}

	/***
	 * @Description 分页查询设备分组设备的列表
	 * @author huangyongtao
	 * @date 2024/8/27 15:24
	 * @param param
	 */
	@Override
	public IPage<IocDeviceModel> pageDevice(IocDevicePageParam param) {
		AssertUtils.isTrue(ObjectUtil.isNotEmpty(param.getGroupId()), MESSAGE);
		List<Long> deviceIds = getDeviceIds(param.getGroupId());
		if(CollectionUtil.isEmpty(deviceIds)){
			return ConvertUtil.pageEmptyConvert(param.getCurrent(), param.getSize());
		}
		param.setDeviceIdList(deviceIds);
		param.setAuth(false);
		param.setTenantId(WebFrameworkUtils.getHeaderTenantId());
		return iocDeviceService.pageDevice(param);
	}

	/***
	 * @Description 分页查询排除设备分组设备的列表
	 * @author huangyongtao
	 * @date 2024/8/27 15:24
	 * @param param
	 */
	@Override
	public IPage<IocDeviceModel> pageNoDevice(IocDevicePageParam param) {
		AssertUtils.isTrue(ObjectUtil.isNotEmpty(param.getGroupId()), MESSAGE);
		List<Long> deviceIds = getDeviceIds(param.getGroupId());
		if(CollectionUtil.isNotEmpty(deviceIds)){
			param.setNoDeviceIdList(deviceIds);
		}
		param.setAuth(false);
		param.setTenantId(WebFrameworkUtils.getHeaderTenantId());
		return iocDeviceService.pageDevice(param);
	}

	/***
	 * @Description 分页查询设备分组设备列表(对外提供)
	 * @author huangyongtao
	 * @date 2024/8/27 15:24
	 * @param param
	 */
	@Override
	public IPage<IocDeviceModel> pageGroupDevice(IocDevicePageParam param) {
		param.setTenantId(WebFrameworkUtils.getHeaderTenantId());
		if(ObjectUtil.isNotEmpty(param.getGroupId())){
			List<Long> deviceIds = getDeviceIds(param.getGroupId());
			if(CollectionUtil.isEmpty(deviceIds)){
				return ConvertUtil.pageEmptyConvert(param.getCurrent(), param.getSize());
			}
			param.setDeviceIdList(deviceIds);
		}else if(ObjectUtil.isNotEmpty(param.getGroupCode())){
			DeviceGroupRelParam relParam = new DeviceGroupRelParam();
			relParam.setGroupCode(param.getGroupCode());
			relParam.setTenantId(param.getTenantId());
			List<DeviceGroupRelModel> relModels = this.getBaseMapper().findDevices(relParam);
			if(CollectionUtil.isEmpty(relModels)){
                return ConvertUtil.pageEmptyConvert(param.getCurrent(), param.getSize());
			}
			param.setDeviceIdList(relModels.stream().map(DeviceGroupRelModel::getDeviceId).collect(Collectors.toList()));
		}
        IPage<IocDeviceModel> devicePage = iocDeviceService.pageDevice(param);
		List<IocDeviceModel> deviceInfoModels = devicePage.getRecords();
		if (ObjectUtil.isNotEmpty(deviceInfoModels)){
			List<Long> deviceIds = deviceInfoModels.stream().map(IocDeviceModel::getId).collect(Collectors.toList());
			DeviceGroupRelParam relParam = new DeviceGroupRelParam();
			relParam.setDeviceIdList(deviceIds);
			relParam.setTenantId(param.getTenantId());
			List<DeviceGroupRelModel> relModels = this.getBaseMapper().findGroupRel(relParam);
			if(CollectionUtil.isNotEmpty(relModels)){
				Map<Long, List<DeviceGroupRelModel>> relMap = relModels.stream().collect(Collectors.groupingBy(DeviceGroupRelModel::getDeviceId));
				deviceInfoModels.forEach(item-> item.setDeviceGroupRelModel(relMap.get(item.getId())));
			}
		}
		return devicePage;
	}

	private List<Long> getDeviceIds(Long groupId){
		List<DeviceGroupRel> deviceGroupRels = this.list(new LambdaQueryWrapper<DeviceGroupRel>().select(DeviceGroupRel::getDeviceId).eq(DeviceGroupRel::getGroupId, groupId));
		List<Long> deviceIds = new ArrayList<>();
		if(CollectionUtil.isNotEmpty(deviceGroupRels)){
			deviceIds = deviceGroupRels.stream().map(DeviceGroupRel::getDeviceId).collect(Collectors.toList());
		}
		return deviceIds;
	}
}
