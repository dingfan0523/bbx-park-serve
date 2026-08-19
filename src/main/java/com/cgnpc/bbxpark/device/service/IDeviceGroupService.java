package com.cgnpc.bbxpark.device.service;

import com.cgnpc.bbxpark.device.domain.DeviceGroup;
import com.cgnpc.bbxpark.device.dto.model.DeviceGroupModel;
import com.cgnpc.bbxpark.device.dto.model.DeviceGroupTreeModel;
import com.cgnpc.bbxpark.device.dto.param.DeviceGroupListParam;
import com.cgnpc.bbxpark.device.dto.param.DeviceGroupParam;
import com.cgnpc.cud.core.service.IBaseService;

import java.util.List;

/***
 * @Description 设备分组服务接口
 * @author huangyongtao
 * @date 2024/8/12 13:53
 */
public interface IDeviceGroupService extends IBaseService<DeviceGroup> {

	/***
	 * @Description 查询设备分组树
	 * @author huangyongtao
	 * @date 2024/8/12 16:11
	 * @param param
	 */
	List<DeviceGroupTreeModel> findTree(DeviceGroupListParam param);

	/**
	 * 根据设备分组标识获得设备分组详情信息.
	 * @Param [id] 设备分组标识
	 * @Return 设备分组详情信息
	 */
	DeviceGroupModel detail(DeviceGroupParam param);


	/**
	 * 获取设备分组列表.
	 * @Param param 设备分组查询条件
	 * @Return 设备分组信息列表
	 */
	List<DeviceGroupModel> list(DeviceGroupListParam param);

	/**
	 * 新增设备分组.
	 * @Param param 设备分组信息
	 * @Return 新增设备分组是否成功
	 */
	Boolean add(DeviceGroupParam param);

	/**
	 * 删除设备分组.
	 * @Param id 设备分组标识
	 * @Return 删除设备分组是否成功
	 */
	Boolean remove(DeviceGroupParam param);

	/**
	 * 编辑设备分组信息.
	 * @Param param 设备分组信息
	 * @Return 编辑设备分组是否成功
	 */
	Boolean edit(DeviceGroupParam param);

}
