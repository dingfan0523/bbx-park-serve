package com.cgnpc.bbxpark.property.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cgnpc.bbxpark.property.domain.MaintainDevice;
import com.cgnpc.bbxpark.property.dto.model.MaintainDeviceModel;
import com.cgnpc.bbxpark.property.dto.param.MaintainDeviceParam;

import java.util.List;

/***
 * @Description 维保设备关联服务接口
 * @author huangyongtao
 * @date 2025/10/16 15:16
 */
public interface IMaintainDeviceService extends IService<MaintainDevice> {


	/**
	 * 获取维保设备关联列表.
	 * @Param param 维保设备关联查询条件
	 * @Return 维保设备关联信息列表
	 */
	List<MaintainDeviceModel> list(Long maintainId);


	/**
	 * 新增维保设备关联.
	 * @Param param 维保设备关联信息
	 * @Return 新增维保设备关联是否成功
	 */
	Boolean add(MaintainDeviceParam param);

	/**
	 * 批量新增维保设备关联.
	 * @Param params 维保设备关联信息列表
	 * @Return 批量新增维保设备关联是否成功
	 */
	Boolean addBatch(List<MaintainDeviceParam> params);

	/**
	 * 删除维保设备关联.
	 * @Param maintainId 维保设备关联标识
	 * @Return 删除维保设备关联是否成功
	 */
	Boolean remove(Long maintainId);

}