
package com.cgnpc.bbxpark.device.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.bbxpark.device.domain.IocDevice;
import com.cgnpc.bbxpark.ioc.dto.model.AlarmAnalysisModel;
import com.cgnpc.bbxpark.ioc.dto.model.DeviceDeptDistributionModel;
import com.cgnpc.bbxpark.ioc.dto.model.DeviceModel;
import com.cgnpc.bbxpark.ioc.dto.model.DeviceSpaceDistributionModel;
import com.cgnpc.bbxpark.ioc.dto.param.AlarmAnalysisParam;
import com.cgnpc.bbxpark.ioc.dto.param.DevicePageParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/***
 * @Description ioc设备数据操作接口
 * @author huangyongtao
 * @date 2025/2/21 17:16
 */
public interface IocDeviceRepository extends BaseMapper<IocDevice> {

    Boolean batchUpdateDevices(@Param("iocDevices") List<IocDevice> iocDevices);

    /**
     * 大屏-根据空间及下级空间查询设备集合
     * @param sslcCode 空间模型code
     * @param spaceId 空间id
     * @return 设备列表
     */
    List<IocDevice> listBySpace(@Param("tenantId")Long tenantId,@Param("sslcCode")String sslcCode,@Param("spaceId")Long spaceId);

    IPage<DeviceModel> pageByScreen(IPage<DevicePageParam> page, @Param("condition") DevicePageParam condition, @Param("tenantId")Long tenantId);

    /**
     * 大屏-根据设备分组编码查询设备信息
     */
    List<IocDevice> findByGroup(@Param("groupCode")String groupCode,@Param("tenantId")Long tenantId);
    /**
     * 大屏-根据部门统计设备数量及百分比
     * @return 数据
     */
    List<DeviceDeptDistributionModel> findDeptDeviceDistribution(@Param("tenantId")Long tenantId,@Param("sslcCode")String sslcCode);

    /**
     * 大屏-空间下智能化设备数量数据
     * @param spaceId 空间id
     * @param iotDevice true->智能化设备,其他->全部设备
     * @return 数据
     */
    List<DeviceSpaceDistributionModel> findIntelligentDistribution(@Param("tenantId")Long tenantId,@Param("spaceId")Long spaceId,@Param("iotDevice")Boolean iotDevice);

    /**
     * 大屏-获取设备分组告警数量数据
     * @return 数据
     */
    List<AlarmAnalysisModel> findDeviceGroupAlarm(@Param("condition")AlarmAnalysisParam condition,@Param("tenantId")Long tenantId);

    /**
     * 大屏-获取分组下设备告警数量数据
     */
    List<AlarmAnalysisModel> findDeviceAlarm(@Param("condition")AlarmAnalysisParam condition,@Param("tenantId")Long tenantId);
}
