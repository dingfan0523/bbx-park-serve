package com.cgnpc.bbxpark.property.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cgnpc.bbxpark.common.utils.BeanUtils;
import com.cgnpc.bbxpark.property.domain.MaintainDevice;
import com.cgnpc.bbxpark.property.dto.model.MaintainDeviceModel;
import com.cgnpc.bbxpark.property.dto.param.MaintainDeviceParam;
import com.cgnpc.bbxpark.property.mapper.MaintainDeviceRepository;
import com.cgnpc.bbxpark.property.service.IMaintainDeviceService;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/***
 * @Description 维保设备关联服务实现
 * @author huangyongtao
 * @date 2025/10/16 15:16
 */
@Service
public class MaintainDeviceServiceImpl extends ServiceImpl<MaintainDeviceRepository, MaintainDevice> implements IMaintainDeviceService {

	/**
	 * 获取维保设备关联列表.
	 * @Param param 维保设备关联查询条件
	 * @Return 维保设备关联信息列表
	 */
	@Override
	@SneakyThrows
	public List<MaintainDeviceModel> list(Long maintainId) {
		List<MaintainDevice> maintainDevices = this.list(buildQuery(maintainId));
		return BeanUtils.convertListTo(maintainDevices, MaintainDeviceModel::new);
	}

	/**
	 * 新增维保设备关联.
	 * @Param param 维保设备关联信息
	 * @Return 新增维保设备关联是否成功
	 */
	@Override
	public Boolean add(MaintainDeviceParam param) {
		MaintainDevice maintainDevice = BeanUtils.convertTo(param, MaintainDevice::new);
		maintainDevice.setId(null);
		return this.save(maintainDevice);
	}

	/**
	 * 批量新增维保设备关联.
	 * @Param params 维保设备关联信息列表
	 * @Return 批量新增维保设备关联是否成功
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean addBatch(List<MaintainDeviceParam> params) {
		if (CollectionUtil.isEmpty(params)) {
			return true;
		}
		this.remove(params.get(0).getMaintainId());
		List<MaintainDevice> maintainDevices = BeanUtils.convertListTo(params, MaintainDevice::new);
		return this.saveBatch(maintainDevices);
	}

	/**
	 * 删除维保设备关联.
	 * @Param maintainId 维保设备关联标识
	 * @Return 删除维保设备关联是否成功
	 */
	@Override
	public Boolean remove(Long maintainId) {
		return this.remove(Wrappers.<MaintainDevice>lambdaQuery().eq(MaintainDevice::getMaintainId, maintainId));
	}
	
	private LambdaQueryWrapper<MaintainDevice> buildQuery(Long maintainId) {
		LambdaQueryWrapper<MaintainDevice> query = new LambdaQueryWrapper<>();
		// 根据维保id筛选
		query.eq(ObjectUtil.isNotEmpty(maintainId), MaintainDevice::getMaintainId, maintainId);
		query.orderByAsc(MaintainDevice::getTaskGroup);
		return query;
	}
}