package com.cgnpc.bbxpark.device.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cgnpc.bbxpark.device.domain.IocDevice;
import com.cgnpc.bbxpark.device.dto.model.*;
import com.cgnpc.bbxpark.device.dto.param.AlarmInfoParam;
import com.cgnpc.bbxpark.device.dto.param.IocDevicePageParam;
import com.cgnpc.bbxpark.device.dto.param.IotDeviceRelationListParam;
import com.cgnpc.bbxpark.workorder.dto.model.WorkOrderCountModel;
import com.cgnpc.bbxpark.workorder.dto.model.WorkOrderModel;
import com.cgnpc.bbxpark.workorder.dto.param.WorkOrderPageParam;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/***
 * @Description ioc设备统计服务接口
 * @author huangyongtao
 * @date 2025/4/16 17:19
 */
public interface IIocDeviceCountService extends IService<IocDevice> {


    /***
     * @Description 分页查询
     * @author huangyongtao
     * @date 2025/4/16 11:00
     * @param param
     */
    IPage<IocDeviceModel> pageDeviceCount(IocDevicePageParam param);

    /***
     * @Description 查询关联设备列表
     * @author huangyongtao
     * @date 2025/4/17 16:17
     * @param param
     */
    List<IotDeviceRelationModel> findRelationDevices(IotDeviceRelationListParam param);

    /***
     * @Description 设备告警信息分页查询
     * @author huangyongtao
     * @date 2025/4/17 16:18
     * @param param
     */
    IPage<AlarmInfoModel> pageAlarmInfo(AlarmInfoParam param);

    /***
     * @Description 告警信息统计
     * @author huangyongtao
     * @date 2025/4/17 17:26
     * @param param
     */
    AlarmInfoCountModel countAlarmInfo(AlarmInfoParam param);

    /***
     * @Description 获取未完成的告警信息统计
     * @author huangyongtao
     * @date 2025/4/17 17:26
     * @param param
     */
    AlarmInfoCountModel getAlarmInfoCount(AlarmInfoParam param);

    /***
     * @Description 分页查询设备工单信息
     * @author huangyongtao
     * @date 2025/4/17 17:32
     * @param param
     */
    IPage<WorkOrderModel> pageWorkOrder(WorkOrderPageParam param);

    /***
     * @Description 工单信息统计
     * @author huangyongtao
     * @date 2025/4/17 17:26
     * @param param
     */
    WorkOrderCountModel countWorkOrder(WorkOrderPageParam param);

    /***
     * @Description  设备台账详情导出(easyExcel)
     * @author huangyongtao
     * @date 2025/4/18 14:59
     * @param response
     * @param param
     */
    Boolean deviceCountEasyExport(HttpServletResponse response, IocDevicePageParam param);

    /***
     * @Description 抄表设备统计
     * @author huangyongtao
     * @date 2025/4/24 16:44
     */
    IocDeviceMeterCountModel meterDeviceCount();

}
