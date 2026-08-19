package com.cgnpc.bbxpark.device.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.bbxpark.device.domain.DeviceGroupRel;
import com.cgnpc.bbxpark.device.dto.model.IocDeviceModel;
import com.cgnpc.bbxpark.device.dto.param.DeviceGroupRelParam;
import com.cgnpc.bbxpark.device.dto.param.IocDevicePageParam;
import com.cgnpc.cud.core.service.IBaseService;

/***
 * @Description 设备分组关系服务接口
 * @author huangyongtao
 * @date 2024/8/12 13:54
 */
public interface IDeviceGroupRelService extends IBaseService<DeviceGroupRel> {

	/**
	 * 新增设备分组关系.
	 * @Param param 设备分组关系信息
	 * @Return 新增设备分组关系是否成功
	 */
	Boolean addDeviceToGroup(DeviceGroupRelParam param);


	/**
	 * 删除设备分组关系.
	 * @Param id 设备分组关系标识
	 * @Return 删除设备分组关系是否成功
	 */
	Boolean removeDeviceToGroup(DeviceGroupRelParam param);

	/***
	 * @Description 分页查询设备分组设备的列表
	 * @author huangyongtao
	 * @date 2024/8/13 10:32
	 * @param param
	 */
    IPage<IocDeviceModel> pageDevice(IocDevicePageParam param);

	/***
	 * @Description 分页查询排除设备分组设备的列表
	 * @author huangyongtao
	 * @date 2024/8/13 10:32
	 * @param param
	 */
    IPage<IocDeviceModel> pageNoDevice(IocDevicePageParam param);

	/***
	 * @Description 分页查询设备分组设备列表(对外提供)
	 * @author huangyongtao
	 * @date 2024/8/27 14:41
	 * @param param
	 */
    IPage<IocDeviceModel> pageGroupDevice(IocDevicePageParam param);

}
