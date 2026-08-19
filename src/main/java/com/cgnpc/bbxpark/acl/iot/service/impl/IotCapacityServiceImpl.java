package com.cgnpc.bbxpark.acl.iot.service.impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.cgnpc.bbxpark.acl.iot.config.IotConfig;
import com.cgnpc.bbxpark.acl.iot.dto.req.IotServiceCommandDto;
import com.cgnpc.bbxpark.acl.iot.dto.resp.IotDeviceInfo;
import com.cgnpc.bbxpark.acl.iot.dto.resp.IotDeviceState;
import com.cgnpc.bbxpark.acl.iot.dto.resp.IotPage;
import com.cgnpc.bbxpark.acl.iot.dto.resp.IotThingModel;
import com.cgnpc.bbxpark.acl.iot.service.IotCapacityService;
import com.cgnpc.bbxpark.acl.iot.util.IotHttpUtil;
import com.cgnpc.bbxpark.common.utils.CollectionUtils;
import com.cgnpc.bbxpark.device.dto.param.IotLoginParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author dingfan
 * @version 1.0
 * @date 2025/6/30 9:44
 */
@Service
public class IotCapacityServiceImpl implements IotCapacityService {
    @Autowired
    private IotConfig iotConfig;

    @Override
    public String auth() {
        Map<String, Object> dataMap = new HashMap<>(4);
        IotLoginParam iotLoginParam = new IotLoginParam();
        iotLoginParam.setTenantId(iotConfig.getTenantId());
        iotLoginParam.setUsername(iotConfig.getUsername());
        iotLoginParam.setPassword(iotConfig.getPassword());
        dataMap.put("data", iotLoginParam);
        String loginUrl = iotConfig.getIotIp() + iotConfig.getAuthServe();
        String result = IotHttpUtil.post(loginUrl, JSON.toJSONString(dataMap));
        JSONObject json = JSONObject.parseObject(result);
        return json.getString("token");
    }

    @Override
    public IotDeviceState queryDeviceState(String deviceId) {
        List<IotDeviceState> list = queryDeviceStates(Collections.singletonList(deviceId));
        return CollectionUtils.isEmpty(list) ? null : list.get(0);
    }

    @Override
    public List<IotDeviceState> queryDeviceStates(List<String> deviceIds) {
        Map<String, Object> dataMap = new HashMap<>(4);
        Map<String, Object> paramMap = new HashMap<>(4);
        paramMap.put("deviceIds", deviceIds);
        dataMap.put("data", paramMap);
        String stateUrl = iotConfig.getIotIp() + iotConfig.getStateServe();

        String result = IotHttpUtil.post(stateUrl, JSON.toJSONString(dataMap), auth());
        List<IotDeviceState> states = JSON.parseArray(result, IotDeviceState.class);
        if (CollectionUtils.isEmpty(states)) {
            return Collections.emptyList();
        }
        return states;
    }

    @Override
    public List<IotDeviceInfo> queryDeviceProperties(List<String> deviceIds, Long tenantId) {
        Map<String,Object> paramMap = new HashMap<>(4);
        paramMap.put("deviceIds",deviceIds);
        paramMap.put("groupId",tenantId);
        paramMap.put("pageSize",deviceIds.size());
        paramMap.put("pageNum",1);
        String url = iotConfig.getIotOpenIp() + iotConfig.getInfoListServe();

        String result = IotHttpUtil.get(url,paramMap);
        IotPage<IotDeviceInfo> devicePage = JSON.parseObject(result,new TypeReference<IotPage<IotDeviceInfo>>(){});
        if(devicePage == null){
            return Collections.emptyList();
        }
        return devicePage.getRows();
    }

    @Override
    public IotThingModel.Model queryThingModel(String productKey) {
        Map<String,Object> paramMap = new HashMap<>(4);
        paramMap.put("productKey",productKey);
        String url = iotConfig.getIotOpenIp() + iotConfig.getThingModelServe();
        String result = IotHttpUtil.get(url,paramMap);
        IotThingModel iotThingModel = JSON.parseObject(result,IotThingModel.class);
        return iotThingModel.getModel();
    }

    @Override
    public String serviceInvoke(IotServiceCommandDto commandDto) {
        String url = iotConfig.getIotOpenIp() + iotConfig.getServiceServe();
        return IotHttpUtil.post(url,JSON.toJSONString(commandDto));
    }
}
