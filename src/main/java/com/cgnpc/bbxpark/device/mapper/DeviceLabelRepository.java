
package com.cgnpc.bbxpark.device.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.bbxpark.device.domain.DeviceLabel;
import com.cgnpc.bbxpark.device.dto.model.IocDeviceModel;
import com.cgnpc.bbxpark.device.dto.param.IocDevicePageParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 设备标签数据操作接口
 */
@Mapper
public interface DeviceLabelRepository extends BaseMapper<DeviceLabel> {


    IPage<IocDeviceModel> pageDeviceToLabel(IPage<IocDeviceModel> iPage, @Param("condition") IocDevicePageParam param);
}
