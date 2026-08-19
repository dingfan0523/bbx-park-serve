package com.cgnpc.bbxpark.device.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.bbxpark.acl.iot.dto.resp.IotDeviceState;
import com.cgnpc.bbxpark.device.domain.IocDevice;
import com.cgnpc.bbxpark.device.dto.model.*;
import com.cgnpc.bbxpark.device.dto.param.*;
import com.cgnpc.bbxpark.space.dto.model.DepartmentInfoModel;
import com.cgnpc.bbxpark.space.dto.model.UserInfoModel;
import com.cgnpc.cud.core.service.IBaseService;

import java.util.List;

/***
 * @Description ioc设备服务接口
 * @author huangyongtao
 * @date 2025/2/21 17:19
 */
public interface IIocDeviceService extends IBaseService<IocDevice> {

	/***
	 * @Description 分页查询
	 * @author huangyongtao
	 * @date 2025/2/24 11:00
	 * @param param
	 */
	IPage<IocDeviceModel> pageDevice(IocDevicePageParam param);

	/***
	 * @Description 查询设备列表
	 * @author huangyongtao
	 * @date 2025/2/24 11:42
	 * @param param
	 */
	List<IocDeviceModel> findList(IocDeviceListParam param);

	/***
	 * @Description 查询设备列表(定时任务查询)
	 * @author huangyongtao
	 * @date 2025/2/24 11:42
	 * @param param
	 */
	List<IocDeviceModel> findListByJob(IocDeviceListParam param);

	/***
	 * @Description 批量绑定设备空间
	 * @author huangyongtao
	 * @date 2025/2/24 15:10
	 * @param param
	 */
	Boolean bindDeviceSpace(IocDeviceBindingParam param);

	/***
	 * @Description 批量绑定设备产品
	 * @author huangyongtao
	 * @date 2025/2/24 15:10
	 * @param param
	 */
	Boolean bindDeviceProduct(IocDeviceBindingParam param);

	/***
	 * @Description 批量绑定设备部门
	 * @author huangyongtao
	 * @date 2025/2/24 15:10
	 * @param param
	 */
	Boolean bindDeviceDepartment(IocDeviceBindingParam param);

	/***
	 * @Description 设备的启用禁用
	 * @author huangyongtao
	 * @date 2025/2/24 16:10
	 * @param param
	 */
	Boolean deviceEnable(IocDeviceStatusParam param);

	/***
	 * @Description 设备的上线和下线
	 * @author huangyongtao
	 * @date 2025/2/24 16:10
	 * @param param
	 */
	Boolean deviceOnline(IocDeviceStatusParam param);

	/***
	 * @Description 校验母设备是否包含子设备
	 * @author huangyongtao
	 * @date 2025/2/24 16:10
	 * @param param
	 */
	Boolean checkDeviceComplex(IocDeviceParam param);

	/**
	 * 根据ioc设备标识获得ioc设备详情信息.
	 * @Param [id] ioc设备标识
	 * @Return ioc设备详情信息
	 */
	IocDeviceModel detail(Long id);


	/**
	 * 新增ioc设备.
	 * @Param param ioc设备信息
	 * @Return 新增ioc设备是否成功
	 */
	Boolean add(IocDeviceParam param);

	/**
	 * 删除ioc设备.
	 * @Param id ioc设备标识
	 * @Return 删除ioc设备是否成功
	 */
	Boolean remove(Long id);

	/**
	 * 编辑ioc设备信息.
	 * @Param param ioc设备信息
	 * @Return 编辑ioc设备是否成功
	 */
	Boolean edit(IocDeviceParam param);

	/***
	 * @Description 根据iot设备标识获取iot设备详情信息.
	 * @author huangyongtao
	 * @date 2025/2/25 13:55
	 * @param iotDeviceDn
	 */
	IotDeviceCheckModel findIotDevice(String iotDeviceDn);

	/**
	 * 查询设备物模型列表(目前只查服务和属性,不查事件)
	 *
	 * @param deviceId 参
	 * @return 物模型列表
	 */
	List<DeviceThingModel> thingModelList(Long deviceId);

	/**
	 * 根据部门id查询下级节点部门信息
	 * @return 部门信息
	 */
	List<DepartmentInfoModel> findSubDepartments();

	/**
	 * 根据部门信息查询人员列表
	 * @return 部门信息
	 */
	List<UserInfoModel> findUserInfoByDepartment(String departmentId);

	/**
	 * 查询Iot设备实时在线状态
	 *
	 * @param deviceIdList 设备id集合
	 * @return 集合
	 */
	List<IotDeviceState> fetchIotDeviceStates(List<String> deviceIdList);

	/***
	 * @Description 查询关联设备列表
	 * @author huangyongtao
	 * @date 2025/3/3 16:17
	 * @param param
	 */
	List<IotDeviceRelationModel> findRelationDevices(IotDeviceRelationListParam param);


	/***
	 * @Description 保存关联设备
	 * @author huangyongtao
	 * @date 2025/3/3 16:17
	 * @param param
	 */
	Boolean saveRelationDevices(IotDeviceRelationParam param);

	/***
	 * @Description 更新物联设备状态
	 * @author huangyongtao
	 * @date 2025/4/17 15:25
	 */
	Boolean updateIotDeviceStatus();

	/***
	 * @Description 空间设备树
	 * @author huangyongtao
	 * @date 2025/8/20 16:44
	 * @param param
	 */
	DeviceVideoTreeModel treeSpaceDevices(IocDeviceParam param);

	/***
	 * @Description 重点空间设备树
	 * @author huangyongtao
	 * @date 2025/8/20 16:44
	 * @param param
	 */
	DeviceVideoTreeModel treeKeySpaceDevices(IocDeviceParam param);

	/***
	 * @Description 标记重点设备
	 * @author huangyongtao
	 * @date 2025/8/20 17:26
	 * @param param
	 */
	Boolean signKeyAreas(IocDeviceParam param);

}
