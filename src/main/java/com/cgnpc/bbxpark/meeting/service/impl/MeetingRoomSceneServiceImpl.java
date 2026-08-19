package com.cgnpc.bbxpark.meeting.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cgnpc.bbxpark.common.enums.Delete;
import com.cgnpc.bbxpark.common.enums.Status;
import com.cgnpc.bbxpark.common.utils.*;
import com.cgnpc.bbxpark.device.domain.IocDevice;
import com.cgnpc.bbxpark.device.dto.param.SceneControlParam;
import com.cgnpc.bbxpark.device.service.IIocDeviceService;
import com.cgnpc.bbxpark.device.service.ISceneControlService;
import com.cgnpc.bbxpark.meeting.domain.MeetingRoomScene;
import com.cgnpc.bbxpark.meeting.domain.MeetingRoomSceneConfig;
import com.cgnpc.bbxpark.meeting.dto.model.MeetingSceneConfigModel;
import com.cgnpc.bbxpark.meeting.dto.model.MeetingSceneDetailModel;
import com.cgnpc.bbxpark.meeting.dto.model.MeetingSceneModel;
import com.cgnpc.bbxpark.meeting.dto.param.MeetingSceneListParam;
import com.cgnpc.bbxpark.meeting.dto.param.MeetingSceneParam;
import com.cgnpc.bbxpark.meeting.mapper.MeetingRoomSceneRepository;
import com.cgnpc.bbxpark.meeting.service.IMeetingSceneConfigService;
import com.cgnpc.bbxpark.meeting.service.IMeetingSceneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author zhaoshuo
 * @time 2025/1/6
 * @desc 会议室场景业务层
 */
@Service
public class MeetingRoomSceneServiceImpl extends ServiceImpl<MeetingRoomSceneRepository, MeetingRoomScene> implements IMeetingSceneService {
    @Resource
    private IMeetingSceneConfigService meetingSceneConfigService;
    @Resource
    private ISceneControlService sceneControlService;

    @Autowired
    private IIocDeviceService iocDeviceService;

    @Override
    public List<MeetingSceneModel> list(MeetingSceneListParam param) {
        Long tenantId = WebFrameworkUtils.getHeaderTenantId();
        List<MeetingRoomScene> list = list(Wrappers.<MeetingRoomScene>lambdaQuery().eq(tenantId != null, MeetingRoomScene::getTenantId, tenantId).eq(MeetingRoomScene::getRoomId,param.getRoomId()).like(StringUtils.isNotEmpty(param.getSceneName()), MeetingRoomScene::getSceneName, param.getSceneName()).eq(MeetingRoomScene::getDeleted, Status.enabled.getKey()).orderByDesc(MeetingRoomScene::getCreateTime));
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }
        return BeanUtils.convertListTo(list, MeetingSceneModel::new);
    }

    @Override
    public MeetingSceneDetailModel detail(Long id) {
        MeetingRoomScene scene = getById(id);
        List<MeetingRoomSceneConfig> configList = meetingSceneConfigService.listBySceneId(id);
        MeetingSceneDetailModel model = BeanUtils.convertTo(scene, MeetingSceneDetailModel::new);
        List<MeetingSceneConfigModel> configModelList = configList.stream().map(config->{
            MeetingSceneConfigModel configModel = BeanUtils.convertTo(config, MeetingSceneConfigModel::new);
            configModel.setModelParamList(JsonUtil.convertJsonArrStrToList(config.getArgs(), MeetingSceneConfigModel.ThingModelParam.class));
            return configModel;
        }).collect(Collectors.toList());
        model.setSceneConfigList(configModelList);
        return model;
    }

    @Override
    public Boolean add(MeetingSceneParam param) {
        //场景保存
        MeetingRoomScene scene = BeanUtils.convertTo(param, MeetingRoomScene::new);
        scene.setDeleted((int)Status.enabled.getKey());
        save(scene);
        //场景配置保存
        List<MeetingRoomSceneConfig> configList = param.getSceneConfigList().stream().map(c->{
            MeetingRoomSceneConfig config = BeanUtils.convertTo(c,MeetingRoomSceneConfig::new);
            config.setSceneId(scene.getId());
            config.setArgs(JsonUtil.convertListToJsonStr(c.getModelParamList()));
            return config;
        }).collect(Collectors.toList());
        return meetingSceneConfigService.saveBatch(configList);
    }

    @Override
    public Boolean edit(MeetingSceneParam param) {
        MeetingRoomScene scene = getById(param.getId());
        AssertUtils.notNull(scene, "场景不存在（后台未找到该场景）");
        //场景保存
        BeanUtils.copyProperties(param,scene);
        updateById(scene);
        //场景配置保存
        meetingSceneConfigService.removeBySceneId(scene.getId());
        List<MeetingRoomSceneConfig> configList = param.getSceneConfigList().stream().map(c->{
            MeetingRoomSceneConfig config = BeanUtils.convertTo(c,MeetingRoomSceneConfig::new);
            config.setSceneId(scene.getId());
            config.setArgs(JsonUtil.convertListToJsonStr(c.getModelParamList()));
            return config;
        }).collect(Collectors.toList());
        return meetingSceneConfigService.saveBatch(configList);
    }

    @Override
    public Boolean remove(Long id) {
        MeetingRoomScene scene = getById(id);
        AssertUtils.isFalse(Objects.equals(scene.getDeleted(), Delete.DELETED.getKey()),"场景不存在（后台未找到该场景）");
        AssertUtils.notNull(scene, "场景不存在（后台未找到该场景）");
        scene.setDeleted((int)Status.disabled.getKey());
        return updateById(scene);
    }

    @Override
    public Boolean exec(Long id) {
        MeetingRoomScene scene = getById(id);
        AssertUtils.notNull(scene, "场景不存在（后台未找到该场景）");
        List<MeetingRoomSceneConfig> configList = meetingSceneConfigService.listBySceneId(id);
        Map<Long, String> deviceMap = handleDevice(configList);
        configList.forEach(config->{
            SceneControlParam param = new SceneControlParam();
            param.setDeviceId(deviceMap.get(config.getDeviceId()));
            param.setService(config.getIdentifier());
            List<MeetingSceneConfigModel.ThingModelParam> modelParamList = JsonUtil.convertJsonArrStrToList(config.getArgs(), MeetingSceneConfigModel.ThingModelParam.class);
            Map<String,Object> args = new HashMap<>(4);
            if(!modelParamList.isEmpty()){
                 args = modelParamList.stream().collect(Collectors.toMap(MeetingSceneConfigModel.ThingModelParam::getIdentifier, MeetingSceneConfigModel.ThingModelParam::getValue));
            }
            param.setArgs(args);
            sceneControlService.sendServiceControl(param);
        });
        return Boolean.TRUE;
    }

    private Map<Long, String> handleDevice(List<MeetingRoomSceneConfig> configList){
        List<Long> deviceIdList = configList.stream().map(MeetingRoomSceneConfig::getDeviceId).collect(Collectors.toList());
        if(CollectionUtil.isEmpty(deviceIdList)){
            return Collections.emptyMap();
        }
        List<IocDevice> iocDeviceList = (List<IocDevice>) iocDeviceService.listByIds(deviceIdList);
        return CollectionUtil.isEmpty(iocDeviceList) ? Collections.emptyMap() :iocDeviceList.stream().filter(p -> ObjectUtil.isNotEmpty(p.getIotDeviceDn())).collect(Collectors.toMap(IocDevice::getId, IocDevice::getIotDeviceDn));
    }
}
