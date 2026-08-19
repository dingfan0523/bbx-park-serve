package com.cgnpc.bbxpark.device.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.bbxpark.device.domain.DeviceLabel;
import com.cgnpc.bbxpark.device.dto.model.DeviceLabelModel;
import com.cgnpc.bbxpark.device.dto.model.IocDeviceModel;
import com.cgnpc.bbxpark.device.dto.param.DeviceLabelListParam;
import com.cgnpc.bbxpark.device.dto.param.DeviceLabelPageParam;
import com.cgnpc.bbxpark.device.dto.param.DeviceLabelParam;
import com.cgnpc.bbxpark.device.dto.param.IocDevicePageParam;
import com.cgnpc.cud.core.service.IBaseService;

import java.util.List;

/**
 * 设备标签服务接口
 */
public interface IDeviceLabelService extends IBaseService<DeviceLabel> {


    IPage<DeviceLabelModel> pageResult(DeviceLabelPageParam param);

    Boolean add(DeviceLabelParam param);

    Boolean edit(DeviceLabelParam param);

    Boolean removeId(Long id);

    DeviceLabelModel get(Long id);


    Boolean addDeviceToLabel(DeviceLabelParam param);

    IPage<IocDeviceModel> pageDeviceToLabel(IocDevicePageParam param);

    Boolean removeDeviceToLabel(DeviceLabelListParam param);

    List<DeviceLabelModel> list(DeviceLabelListParam param);
}
