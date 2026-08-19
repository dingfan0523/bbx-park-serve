
package com.cgnpc.bbxpark.device.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cgnpc.bbxpark.device.domain.DeviceGroupRel;
import com.cgnpc.bbxpark.device.dto.model.DeviceGroupRelModel;
import com.cgnpc.bbxpark.device.dto.param.DeviceGroupRelParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/***
 * @Description 设备分组关系数据操作接口
 * @author huangyongtao
 * @date 2024/8/12 11:59
 */
@Mapper
public interface DeviceGroupRelRepository extends BaseMapper<DeviceGroupRel> {

    List<DeviceGroupRelModel> findGroupRel(@Param("condition") DeviceGroupRelParam param);

    List<DeviceGroupRelModel> findDevices(@Param("condition") DeviceGroupRelParam param);

}
