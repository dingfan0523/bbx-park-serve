package com.cgnpc.bbxpark.acl.iot.service;

import com.cgnpc.bbxpark.acl.iot.dto.req.IotServiceCommandDto;
import com.cgnpc.bbxpark.acl.iot.dto.resp.IotDeviceInfo;
import com.cgnpc.bbxpark.acl.iot.dto.resp.IotDeviceState;
import com.cgnpc.bbxpark.acl.iot.dto.resp.IotThingModel;

import java.util.List;

public interface IotCapacityService {
    /**
     * IOT授权接口
     * @return token
     */
    String auth();

    /**
     * IOT查询设备状态
     * @param deviceId 设备id
     * @return 设备状态信息
     */
    IotDeviceState queryDeviceState(String deviceId);

    /**
     * IOT批量查询设备状态信息
     * @param deviceIds 设备id集合
     * @return 设备状态信息集合
     */
    List<IotDeviceState> queryDeviceStates(List<String> deviceIds);

    /**
     * IOT-OPENAPI批量查询设备及属性信息集合
     * @param deviceIds 设备id集合
     * @param tenantId 租户id(设备分组id)
     * @return 设备及属性信息集合
     */
    List<IotDeviceInfo> queryDeviceProperties(List<String> deviceIds, Long tenantId);

    /**
     * IOT-OPENAPI查询产品物魔性
     * @param productKey 产品key
     * @return 产品物模型
     */
    IotThingModel.Model queryThingModel(String productKey);
    /**
     * IOT-OPENAPI服务调用
     *
     * @param commandDto 服务调用参数
     * @return 服务调用结果
     */
    String serviceInvoke(IotServiceCommandDto commandDto);
}
