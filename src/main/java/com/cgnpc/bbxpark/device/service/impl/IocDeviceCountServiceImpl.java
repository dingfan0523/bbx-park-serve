package com.cgnpc.bbxpark.device.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cgnpc.bbxpark.common.constant.AlarmInfoConstant;
import com.cgnpc.bbxpark.common.enums.*;
import com.cgnpc.bbxpark.common.utils.*;
import com.cgnpc.bbxpark.device.domain.*;
import com.cgnpc.bbxpark.device.dto.model.*;
import com.cgnpc.bbxpark.device.dto.param.AlarmInfoParam;
import com.cgnpc.bbxpark.device.dto.param.IocDevicePageParam;
import com.cgnpc.bbxpark.device.dto.param.IotDeviceRelationListParam;
import com.cgnpc.bbxpark.device.mapper.AlarmDeviceRepository;
import com.cgnpc.bbxpark.device.mapper.AlarmInfoRepository;
import com.cgnpc.bbxpark.device.mapper.IocDeviceRepository;
import com.cgnpc.bbxpark.device.service.IIocDeviceCountService;
import com.cgnpc.bbxpark.device.service.IIocProductService;
import com.cgnpc.bbxpark.device.service.IIotDeviceRelationService;
import com.cgnpc.bbxpark.space.domain.TenantInfo;
import com.cgnpc.bbxpark.space.dto.model.ParkSpaceFullModel;
import com.cgnpc.bbxpark.space.service.IParkSpaceService;
import com.cgnpc.bbxpark.space.service.ITenantInfoService;
import com.cgnpc.bbxpark.workorder.domain.WorkOrder;
import com.cgnpc.bbxpark.workorder.dto.model.WorkOrderCountModel;
import com.cgnpc.bbxpark.workorder.dto.model.WorkOrderModel;
import com.cgnpc.bbxpark.workorder.dto.param.WorkOrderPageParam;
import com.cgnpc.bbxpark.workorder.mapper.WorkOrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

/***
 * @Description ioc设备统计服务实现
 * @author huangyongtao
 * @date 2025/4/16 17:21
 */
@Slf4j
@Service
public class IocDeviceCountServiceImpl extends ServiceImpl<IocDeviceRepository, IocDevice> implements IIocDeviceCountService {

    @Autowired
    private IIocProductService iocProductService;

    @Autowired
    private IParkSpaceService parkSpaceService;

    @Autowired
    private AlarmInfoRepository alarmInfoRepository;

    @Autowired
    private AlarmDeviceRepository alarmDeviceRepository;

    @Autowired
    private WorkOrderRepository workOrderRepository;

    @Autowired
    private IIotDeviceRelationService iotDeviceRelationService;

    @Autowired
    private ITenantInfoService tenantInfoService;

    @Override
    public IPage<IocDeviceModel> pageDeviceCount(IocDevicePageParam param) {
        param.setLocalTime(new Date());
        //处理设备告警状态添加
        handleDeviceAlarm(param);
        param.setTenantId(WebFrameworkUtils.getHeaderTenantId());
        IPage<IocDevice> page =  this.page(new Page<>(param.getCurrent(), param.getSize()), buildQuery(param));
        if (CollectionUtil.isEmpty(page.getRecords())) {
            return ConvertUtil.pageEmptyConvert(param.getCurrent(), param.getSize());
        }
        List<IocDeviceModel> iocDeviceModels = BeanUtils.convertListTo(page.getRecords(), IocDeviceModel::new);
        //处理空间名称，产品名称，维保日期，告警状态
        handleNames(iocDeviceModels, param);
        return ConvertUtil.pageConvert(page.getCurrent(), page.getTotal(), page.getSize(), iocDeviceModels);
    }

    @Override
    public List<IotDeviceRelationModel> findRelationDevices(IotDeviceRelationListParam param) {
        AssertUtils.notNull(param.getDeviceId(), "设备id不能为空");
        AssertUtils.notNull(param.getRelationType(), "设备关联类型不能为空");
        List<IotDeviceRelation> relations = iotDeviceRelationService.list(Wrappers.<IotDeviceRelation>lambdaQuery().eq(IotDeviceRelation::getDeviceId, param.getDeviceId()).eq(IotDeviceRelation::getRelationType, param.getRelationType()));
        if(CollectionUtil.isEmpty(relations)){
            return Collections.emptyList();
        }
        List<IotDeviceRelationModel> models = BeanUtils.convertListTo(relations, IotDeviceRelationModel::new);
        List<Long> deviceIds = relations.stream().map(IotDeviceRelation::getRelationDeviceId).collect(Collectors.toList());
        List<IocDevice> devices = this.list(Wrappers.<IocDevice>lambdaQuery().select(IocDevice::getId, IocDevice::getDeviceName, IocDevice::getSpaceId).in(IocDevice::getId, deviceIds));
        if(CollectionUtil.isNotEmpty(devices)){
            Map<Long, Long> deviceSpaceMap = devices.stream().filter(device -> ObjectUtil.isNotEmpty(device.getSpaceId())).collect(Collectors.toMap(IocDevice::getId, IocDevice::getSpaceId));
            List<Long> spaceIds = devices.stream().filter(device -> ObjectUtil.isNotEmpty(device.getSpaceId())).map(IocDevice::getSpaceId).collect(Collectors.toList());
            Map<Long, ParkSpaceFullModel> fullSpaceMap = CollectionUtil.isEmpty(spaceIds) ? new HashMap<>() : parkSpaceService.findFullSpaceMap(spaceIds, null);
            Map<Long, String> deviceNameMap = devices.stream().collect(Collectors.toMap(IocDevice::getId, IocDevice::getDeviceName));
            models.forEach(model -> {
                model.setRelationDeviceName(deviceNameMap.get(model.getRelationDeviceId()));
                model.setSpacesName(deviceSpaceMap.containsKey(model.getRelationDeviceId()) ? fullSpaceMap.get(deviceSpaceMap.get(model.getRelationDeviceId())).getFullPath() : "");
            });
        }
        return models;
    }

    @Override
    public IPage<AlarmInfoModel> pageAlarmInfo(AlarmInfoParam param) {
        IPage<AlarmInfo> page =  alarmInfoRepository.selectPage(new Page<>(param.getCurrent(), param.getSize()), handleAlarmQuery(param));
        List<AlarmInfoModel> alarmInfoModels = BeanUtils.convertListTo(page.getRecords(), AlarmInfoModel::new);
        return ConvertUtil.pageConvert(page.getCurrent(), page.getTotal(), page.getSize(), alarmInfoModels);
    }

    @Override
    public AlarmInfoCountModel countAlarmInfo(AlarmInfoParam param) {
        param.setTenantId(WebFrameworkUtils.getHeaderTenantId());
        return alarmInfoRepository.countAlarmInfo(param);
    }

    @Override
    public AlarmInfoCountModel getAlarmInfoCount(AlarmInfoParam param) {
        param.setNoAlarmStatus(AlarmInfoConstant.ALARM_STATUS_3);
        List<AlarmInfo> alarmInfos = alarmInfoRepository.selectList(handleAlarmQuery(param));
        AlarmInfoCountModel countModel = new AlarmInfoCountModel();
        if(CollectionUtil.isNotEmpty(alarmInfos)){
            countModel.setAlarmTime(alarmInfos.get(0).getAlarmLastTime());
            countModel.setNoHandleNum((long) alarmInfos.size());
        }
        return countModel;
    }

    @Override
    public IPage<WorkOrderModel> pageWorkOrder(WorkOrderPageParam param) {
        IPage<WorkOrder> page =  workOrderRepository.selectPage(new Page<>(param.getCurrent(), param.getSize()), handleWorkOrderQuery(param));
        List<WorkOrderModel> workOrderModels = BeanUtils.convertListTo(page.getRecords(), WorkOrderModel::new);
        return ConvertUtil.pageConvert(page.getCurrent(), page.getTotal(), page.getSize(), workOrderModels);
    }

    @Override
    public WorkOrderCountModel countWorkOrder(WorkOrderPageParam param) {
        param.setTenantId(WebFrameworkUtils.getHeaderTenantId());
        WorkOrderCountModel count = workOrderRepository.countWorkOrder(param);
        count.setSourceName(ObjectUtil.isNotEmpty(count.getSource()) ? WorkOrderSourceEnum.getName(count.getSource()) : null);
        return count;
    }

    @Override
    public Boolean deviceCountEasyExport(HttpServletResponse response, IocDevicePageParam param) {
        //生成excel
        TenantInfo tenantInfo = tenantInfoService.getById(WebFrameworkUtils.getHeaderTenantId());
        param.setSize(-1);
        param.setCurrent(1);
        String title = tenantInfo.getName() + "设备信息统计表";
        IPage<IocDeviceModel> pageResult =  this.pageDeviceCount(param);
        List<IocDeviceExportModel> exportModels = new ArrayList<>();
        if(CollectionUtil.isNotEmpty(pageResult.getRecords())){
            List<IocProduct> iocProducts = iocProductService.list(Wrappers.<IocProduct>lambdaQuery().in(IocProduct::getTenantId, WebFrameworkUtils.getHeaderTenantId()));
            Map<Long, IocProduct> productMap = CollectionUtil.isEmpty(iocProducts) ? new HashMap<>() : iocProducts.stream().collect(Collectors.toMap(IocProduct::getId, product -> product));
            pageResult.getRecords().forEach(model->{
                IocDeviceExportModel exportModel = new IocDeviceExportModel();
                BeanUtils.copyProperties(model, exportModel);
                IocProduct product = productMap.get(model.getProductId());
                if (product != null) {
                    BeanUtils.copyProperties(product, exportModel);
                }
                handleExportProperty(exportModel, model);
                exportModels.add(exportModel);
            });
        }
        ExcelExportUtils.exportExcel(response, title, exportModels, IocDeviceExportModel.class, title);
        return true;
    }

    @Override
    public IocDeviceMeterCountModel meterDeviceCount() {
        IocDeviceMeterCountModel model = new IocDeviceMeterCountModel();
        List<IocDevice> iocDevices = this.list(Wrappers.<IocDevice>lambdaQuery().select(IocDevice::getId, IocDevice::getReadingType,IocDevice::getIotDevicePlatform, IocDevice::getSpaceId)
                .in(IocDevice::getReadingDevice, Status.enabled.getKey())
                .eq(IocDevice::getDeleted, Status.enabled.getKey())
                .eq(IocDevice::getTenantId, WebFrameworkUtils.getHeaderTenantId()));
        if(CollectionUtil.isNotEmpty(iocDevices)){
            Map<String, List<IocDevice>> deviceMap = iocDevices.stream().collect(Collectors.groupingBy(IocDevice::getReadingType));
            if(deviceMap.containsKey(DeviceReadingTypeEnum.ELECTRICITY.getCode())){
                model.setElectricityNum((long)deviceMap.get(DeviceReadingTypeEnum.ELECTRICITY.getCode()).size());
                Map<Integer, List<IocDevice>> iotDeviceMap = deviceMap.get(DeviceReadingTypeEnum.ELECTRICITY.getCode()).stream().collect(Collectors.groupingBy(IocDevice::getIotDevicePlatform));
                model.setElectricityPersonNum((long) (iotDeviceMap.containsKey(DevicePlatformEnum.NO.getCode()) ? iotDeviceMap.get(DevicePlatformEnum.NO.getCode()).size() : 0));
                model.setElectricityAutoNum(model.getElectricityNum() - model.getElectricityPersonNum());
            }
            if(deviceMap.containsKey(DeviceReadingTypeEnum.WATER.getCode())){
                model.setWaterNum((long)deviceMap.get(DeviceReadingTypeEnum.WATER.getCode()).size());
                Map<Integer, List<IocDevice>> iotDeviceMap = deviceMap.get(DeviceReadingTypeEnum.WATER.getCode()).stream().collect(Collectors.groupingBy(IocDevice::getIotDevicePlatform));
                model.setWaterPersonNum((long) (iotDeviceMap.containsKey(DevicePlatformEnum.NO.getCode()) ? iotDeviceMap.get(DevicePlatformEnum.NO.getCode()).size() : 0));
                model.setWaterAutoNum(model.getWaterNum() - model.getWaterPersonNum());
            }
            if(deviceMap.containsKey(DeviceReadingTypeEnum.GAS.getCode())){
                model.setGasNum((long)deviceMap.get(DeviceReadingTypeEnum.GAS.getCode()).size());
                Map<Integer, List<IocDevice>> iotDeviceMap = deviceMap.get(DeviceReadingTypeEnum.GAS.getCode()).stream().collect(Collectors.groupingBy(IocDevice::getIotDevicePlatform));
                model.setGasPersonNum((long) (iotDeviceMap.containsKey(DevicePlatformEnum.NO.getCode()) ? iotDeviceMap.get(DevicePlatformEnum.NO.getCode()).size() : 0));
                model.setGasAutoNum(model.getGasNum() - model.getGasPersonNum());
            }

        }
        return model;
    }

    private void handleExportProperty(IocDeviceExportModel exportModel, IocDeviceModel model){
        exportModel.setDeviceLevelStr(DeviceLevelEnum.getName(model.getDeviceLevel()));
        exportModel.setIotDevicePlatformStr(DevicePlatformEnum.getName(model.getIotDevicePlatform()));
        exportModel.setEnableStatusStr(model.getEnableStatus().equals((int)Status.enabled.getKey()) ? "启用" : "禁用");
        exportModel.setSecureDateStr(ObjectUtil.isEmpty(model.getSecureDate()) ? "" : DateUtils.format(model.getSecureDate(),"yyyy-MM-dd"));
        exportModel.setUseDateStr(ObjectUtil.isEmpty(model.getUseDate()) ? "" : DateUtils.format(model.getUseDate(),"yyyy-MM-dd"));
        exportModel.setFixDateStr(ObjectUtil.isEmpty(model.getFixDate()) ? "" : DateUtils.format(model.getFixDate(),"yyyy-MM-dd"));
        exportModel.setWordDateStr(ObjectUtil.isEmpty(model.getWordDate()) ? "" : DateUtils.format(model.getWordDate(),"yyyy-MM-dd"));
        exportModel.setDeviceTypeStr(DeviceTypeEnum.getName(model.getDeviceType()));
        exportModel.setReadingDeviceStr(model.getReadingDevice().equals((int)Status.enabled.getKey()) ? "是" : "否");
        exportModel.setReadingTypeStr(DeviceReadingTypeEnum.getName(model.getReadingType()));
    }

    private LambdaQueryWrapper<WorkOrder> handleWorkOrderQuery(WorkOrderPageParam param) {
        LambdaQueryWrapper<WorkOrder> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(WorkOrder::getDeleted,Status.enabled.getKey());
        queryWrapper.like(ObjectUtil.isNotEmpty(param.getName()), WorkOrder::getName, param.getName());
        queryWrapper.eq(ObjectUtil.isNotEmpty(param.getStatus()), WorkOrder::getStatus, param.getStatus());
        queryWrapper.eq(ObjectUtil.isNotEmpty(param.getTenantId()),WorkOrder::getTenantId, WebFrameworkUtils.getHeaderTenantId());
        queryWrapper.eq(ObjectUtil.isNotEmpty(param.getSource()), WorkOrder::getSource, param.getSource());
        if (param.getStartTime() != null && param.getEndTime() != null ){
            queryWrapper.between(WorkOrder::getCreateTime, param.getStartTime(), param.getEndTime());
        }
        // 添加设备ID查询条件
//        queryWrapper.inSql(ObjectUtil.isNotEmpty(param.getDeviceId()), WorkOrder::getId, "SELECT distinct work_order_id FROM bbx_work_order_device WHERE device_id = " + param.getDeviceId());
        // 构造合并后的 IN 子查询
        String unionSql = "(SELECT DISTINCT work_order_id FROM bbx_work_order_device WHERE device_id = "
                + param.getDeviceId()
                + " UNION SELECT DISTINCT work_id FROM bbx_work_task WHERE business_type IN (1,4) AND business_id = "
                + param.getDeviceId()
                + ")";

        // 放入 queryWrapper.inSql 中
        queryWrapper.inSql(
                ObjectUtil.isNotEmpty(param.getDeviceId()),
                WorkOrder::getId,
                unionSql
        );
        // 排序方式
        queryWrapper.orderByDesc(WorkOrder::getCreateTime);
        return queryWrapper;
    }

    /***
     * @Description 查询字段的处理
     */
    private LambdaQueryWrapper handleAlarmQuery(AlarmInfoParam param) {
        LambdaQueryWrapper<AlarmInfo> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.like(ObjectUtil.isNotEmpty(param.getAlarmName()), AlarmInfo::getAlarmName, param.getAlarmName());
        queryWrapper.eq(ObjectUtil.isNotEmpty(param.getAlarmStatus()), AlarmInfo::getAlarmStatus, param.getAlarmStatus());
        queryWrapper.ne(ObjectUtil.isNotEmpty(param.getNoAlarmStatus()), AlarmInfo::getAlarmStatus, param.getNoAlarmStatus());
        queryWrapper.eq(AlarmInfo::getTenantId, WebFrameworkUtils.getHeaderTenantId());
        if (param.getAlarmEndTimeStart() != null && param.getAlarmEndTimeEnd() != null ){
            queryWrapper.between(AlarmInfo::getAlarmFirstTime, param.getAlarmEndTimeStart(), param.getAlarmEndTimeEnd());
        }
        // 添加设备ID查询条件
        queryWrapper.inSql(ObjectUtil.isNotEmpty(param.getDeviceId()), AlarmInfo::getId, "SELECT distinct alarm_id FROM bbx_alarm_device WHERE device_id = " + param.getDeviceId());
        // 排序方式
        queryWrapper.orderByDesc(AlarmInfo::getCreateTime);
        return queryWrapper;
    }

    private void handleDeviceAlarm(IocDevicePageParam param) {
        //物联网设备状态集合处理
        if (CollectionUtil.isNotEmpty(param.getIotDeviceStatusList())) {
            if(CollectionUtil.isEmpty(param.getIotDevicePlatformList())){
                param.setIotDevicePlatformList(Arrays.asList(DevicePlatformEnum.OWN.getCode(), DevicePlatformEnum.UNIFIED.getCode(), DevicePlatformEnum.SECURE.getCode()));
            }else{
                param.getIotDevicePlatformList().remove(DevicePlatformEnum.NO.getCode());
            }
            param.setIotDeviceStatus(param.getIotDeviceStatusList().size() == 1 ? param.getIotDeviceStatusList().get(0) : null);
            param.setIotDeviceStatusList(null);
        }
        //启用状态集合处理
        if (CollectionUtil.isNotEmpty(param.getEnableStatusList())) {
            if(param.getEnableStatusList().size() == 1){
                param.setEnableStatus(param.getEnableStatusList().get(0));
            }
            param.setEnableStatusList(null);
        }
        if (CollectionUtil.isNotEmpty(param.getAlarmStatusList())) {
            if(CollectionUtil.isEmpty(param.getIotDevicePlatformList())){
                param.setIotDevicePlatformList(Arrays.asList(DevicePlatformEnum.OWN.getCode(), DevicePlatformEnum.UNIFIED.getCode(), DevicePlatformEnum.SECURE.getCode()));
            }else{
                param.getIotDevicePlatformList().remove(DevicePlatformEnum.NO.getCode());
            }
            if (param.getAlarmStatusList().size() == 1) {
                param.setAlarmStatus(param.getAlarmStatusList().get(0));
            }
            param.setAlarmStatusList(null);
        }
        List<AlarmInfo> alarmInfos = alarmInfoRepository.selectList(Wrappers.<AlarmInfo>lambdaQuery().select(AlarmInfo::getId).ne(AlarmInfo::getAlarmStatus, AlarmInfoConstant.ALARM_STATUS_3).eq(AlarmInfo::getTenantId, WebFrameworkUtils.getHeaderTenantId()));
        if (CollectionUtil.isEmpty(alarmInfos)) {
            return;
        }
        List<Long> alarmIds = alarmInfos.stream().map(AlarmInfo::getId).collect(Collectors.toList());
        List<AlarmDevice> alarmDevices = alarmDeviceRepository.selectList(Wrappers.<AlarmDevice>lambdaQuery().in(AlarmDevice::getAlarmId, alarmIds));
        if (CollectionUtil.isEmpty(alarmDevices)) {
            return;
        }
        param.setDeviceIdList(alarmDevices.stream().map(AlarmDevice::getDeviceId).distinct().collect(Collectors.toList()));
    }
    private void handleNames(List<IocDeviceModel> deviceInfoModels, IocDevicePageParam param) {
        if(CollectionUtil.isEmpty(deviceInfoModels)){
            return;
        }
        List<Long> spaceIds = deviceInfoModels.stream().filter(device -> ObjectUtil.isNotEmpty(device.getSpaceId())).map(IocDeviceModel::getSpaceId).collect(Collectors.toList());
        List<Long> productIds = deviceInfoModels.stream().filter(device -> ObjectUtil.isNotEmpty(device.getProductId())).map(IocDeviceModel::getProductId).collect(Collectors.toList());
        Map<Long, ParkSpaceFullModel> fullSpaceMap = CollectionUtil.isEmpty(spaceIds) ? new HashMap<>() : parkSpaceService.findFullSpaceMap(spaceIds, null);
        List<IocProduct> iocProducts = CollectionUtil.isEmpty(productIds) ? Collections.emptyList() : iocProductService.getBaseMapper().selectList(Wrappers.<IocProduct>lambdaQuery().in(IocProduct::getId, productIds));
        Map<Long, IocProduct> productMap = CollectionUtil.isEmpty(iocProducts)? new HashMap<>() : iocProducts.stream().collect(Collectors.toMap(IocProduct::getId, product -> product));
        deviceInfoModels.forEach(device -> {
            //空间名称
            if(ObjectUtil.isNotEmpty(device.getSpaceId())){
                ParkSpaceFullModel parkSpaceFullModel = fullSpaceMap.get(device.getSpaceId());
                Optional.ofNullable(parkSpaceFullModel).ifPresent(model -> device.setSpaceName(model.getFullPath()));
            }

            //产品名称
            if(ObjectUtil.isNotEmpty(device.getProductId())){
                IocProduct iocProduct = productMap.get(device.getProductId());
                Optional.ofNullable(iocProduct).ifPresent(model -> device.setProductName(handleProductName(model)));
            }

            //维保日期
            if(ObjectUtil.isNotEmpty(device.getSecureDate())){
                device.setSecureStatus(param.getLocalTime().before(device.getSecureDate()) ? 0 : 1);
            }

            //设备告警状态
            if(!DevicePlatformEnum.NO.getCode().equals(device.getIotDevicePlatform())){
                if(CollectionUtil.isNotEmpty(param.getDeviceIdList()) && param.getDeviceIdList().contains(device.getId())){
                    device.setAlarmStatus(1);
                }else{
                    device.setAlarmStatus(0);
                }
            }
        });
    }

    private LambdaQueryWrapper<IocDevice> buildQuery(IocDevicePageParam param) {
        if(ObjectUtil.isEmpty(param.getDeleted())){
            param.setDeleted(Delete.NORMAL.getKey());
        }
        LambdaQueryWrapper<IocDevice> query = new LambdaQueryWrapper<>();
        // 查询未删除的设备
        query.eq(IocDevice::getDeleted, param.getDeleted());
        //设备名称
        query.like(ObjectUtil.isNotEmpty(param.getDeviceName()), IocDevice::getDeviceName, param.getDeviceName());
        //产品id
        query.eq(ObjectUtil.isNotEmpty(param.getProductId()), IocDevice::getProductId, param.getProductId());
        // 空间位置查询
        query.eq(ObjectUtil.isNotEmpty(param.getSpaceId()), IocDevice::getSpaceId, param.getSpaceId());
        // 空间位置集合查询
        query.in(CollectionUtil.isNotEmpty(param.getSpaceIds()), IocDevice::getSpaceId, param.getSpaceIds());
        //设备等级
        query.eq(ObjectUtil.isNotEmpty(param.getDeviceLevel()), IocDevice::getDeviceLevel, param.getDeviceLevel());
        //设备等级集合
        query.in(CollectionUtil.isNotEmpty(param.getDeviceLevelList()), IocDevice::getDeviceLevel, param.getDeviceLevelList());
        //使用部门
        query.eq(ObjectUtil.isNotEmpty(param.getDepartmentId()), IocDevice::getDepartmentId, param.getDepartmentId());
        //设备平台
        query.eq(ObjectUtil.isNotEmpty(param.getIotDevicePlatform()), IocDevice::getIotDevicePlatform, param.getIotDevicePlatform());
        //设备平台集合
        query.in(CollectionUtil.isNotEmpty(param.getIotDevicePlatformList()), IocDevice::getIotDevicePlatform, param.getIotDevicePlatformList());
        //启用状态
        query.eq(ObjectUtil.isNotEmpty(param.getEnableStatus()), IocDevice::getEnableStatus, param.getEnableStatus());
        //物联设备状态
        query.eq(ObjectUtil.isNotEmpty(param.getIotDeviceStatus()), IocDevice::getIotDeviceStatus, param.getIotDeviceStatus());
        //设备维保日期
        if(CollectionUtil.isNotEmpty(param.getSecureStatusList())){
            if(param.getSecureStatusList().size() == 1){
                query.lt(param.getSecureStatusList().get(0).equals(Status.enabled.getKey()), IocDevice::getSecureDate, param.getLocalTime());
                query.gt(param.getSecureStatusList().get(0).equals(Status.disabled.getKey()), IocDevice::getSecureDate, param.getLocalTime());
            }else{
                query.isNotNull(IocDevice::getSecureDate);
            }
        }
        //设备告警状态
        if(ObjectUtil.isNotEmpty(param.getAlarmStatus())){
            query.notIn(param.getAlarmStatus().equals(Status.disabled.getKey()) && CollectionUtil.isNotEmpty(param.getDeviceIdList()), IocDevice::getId, param.getDeviceIdList());
            query.in(param.getAlarmStatus().equals(Status.enabled.getKey()) && CollectionUtil.isNotEmpty(param.getDeviceIdList()), IocDevice::getId, param.getDeviceIdList());
            query.eq(CollectionUtil.isEmpty(param.getDeviceIdList()), IocDevice::getId, 0);
        }
        query.eq(ObjectUtil.isNotEmpty(param.getTenantId()), IocDevice::getTenantId, param.getTenantId());
        query.orderByDesc(IocDevice::getCreateTime);
        return query;
    }

    private String handleProductName(IocProduct iocProduct){
        return iocProduct.getPdName() + (Status.enabled.getKey().equals(iocProduct.getDeleted()) ? "" : "（已删除）");
    }

}
