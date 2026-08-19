package com.cgnpc.bbxpark.ioc.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.cgnpc.bbxpark.common.constant.AlarmInfoConstant;
import com.cgnpc.bbxpark.common.enums.Delete;
import com.cgnpc.bbxpark.common.enums.Status;
import com.cgnpc.bbxpark.common.utils.CollectionUtils;
import com.cgnpc.bbxpark.common.utils.WebFrameworkUtils;
import com.cgnpc.bbxpark.device.domain.AlarmDevice;
import com.cgnpc.bbxpark.device.domain.AlarmInfo;
import com.cgnpc.bbxpark.device.domain.IocDevice;
import com.cgnpc.bbxpark.device.mapper.AlarmDeviceRepository;
import com.cgnpc.bbxpark.device.mapper.AlarmInfoRepository;
import com.cgnpc.bbxpark.device.mapper.IocDeviceRepository;
import com.cgnpc.bbxpark.ioc.dto.model.StrategicMetricsModel;
import com.cgnpc.bbxpark.ioc.service.IIocCommonService;
import com.cgnpc.bbxpark.ioc.service.IMaterialScreenService;
import com.cgnpc.bbxpark.ioc.service.IScreenDeviceService;
import com.cgnpc.bbxpark.ioc.service.IWorkOrderScreenService;
import com.cgnpc.bbxpark.problemReport.service.IProblemReportService;
import org.apache.commons.math3.util.Precision;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

@Service
public class IocCommonServiceImpl implements IIocCommonService {
    @Resource
    private IMaterialScreenService materialScreenService;
    @Resource
    private IWorkOrderScreenService workOrderScreenService;
    @Resource
    private IScreenDeviceService screenDeviceService;
    @Resource
    private IocDeviceRepository deviceRepository;
    @Resource
    private AlarmInfoRepository alarmInfoRepository;
    @Resource
    private AlarmDeviceRepository alarmDeviceRepository;
    @Autowired
    @Qualifier("kafkaMsgThreadPool")
    private Executor executorService;


    @Override
    public StrategicMetricsModel getStrategicMetrics(String sslcCode) {
        StrategicMetricsModel model = new StrategicMetricsModel();
        CompletableFuture<Void> deviceFuture = CompletableFuture.runAsync(() -> model.setDeviceHealthRate(getDeviceHealthRate()),executorService);
        CompletableFuture<Void> workFuture = CompletableFuture.runAsync(() -> model.setWorkOrderClosedRate(workOrderScreenService.getWorkCompleteRate()),executorService);
        CompletableFuture<Void> materialFuture = CompletableFuture.runAsync(() -> model.setInventoryWarningRate(materialScreenService.getMaterialAlarmRate()),executorService);
        CompletableFuture.allOf(deviceFuture, workFuture, materialFuture).join();
        return model;
    }

    private BigDecimal getDeviceHealthRate(){
        //启用状态的所有设备
        Long tenantId = WebFrameworkUtils.getHeaderTenantId();
        List<IocDevice> devices = deviceRepository.selectList(Wrappers.<IocDevice>lambdaQuery().eq(IocDevice::getEnableStatus, Status.enabled.getKey())
                .eq(IocDevice::getDeleted, Delete.NORMAL.getKey()).eq(tenantId != null,IocDevice::getTenantId,tenantId));
        if(CollectionUtils.isEmpty(devices)){
            return BigDecimal.ZERO;
        }
        //报事报修工单的设备id集合
        List<Long> problemDeviceIds = workOrderScreenService.getProblemDeviceIds();
        //告警的设备id集合
        List<Long> alarmDeviceIds = findAlarmDeviceIds();
        //健康的设备数量
        long healthCount = devices.stream().filter(d->isHealth(d,alarmDeviceIds,problemDeviceIds)).count();
        return new BigDecimal(healthCount).multiply(new BigDecimal(100)).divide(new BigDecimal(devices.size()),2, RoundingMode.HALF_DOWN);
    }

    private List<Long> findAlarmDeviceIds(){
        List<AlarmInfo> alarmInfos = alarmInfoRepository.selectList(Wrappers.<AlarmInfo>lambdaQuery().select(AlarmInfo::getId).ne(AlarmInfo::getAlarmStatus, AlarmInfoConstant.ALARM_STATUS_3)
                .eq(AlarmInfo::getTenantId, WebFrameworkUtils.getHeaderTenantId()));
        if(CollectionUtils.isEmpty(alarmInfos)){
            return Collections.emptyList();
        }
        List<Long> alarmIds = alarmInfos.stream().map(AlarmInfo::getId).collect(Collectors.toList());
        List<AlarmDevice> alarmDevices = alarmDeviceRepository.selectList(Wrappers.<AlarmDevice>lambdaQuery().in(AlarmDevice::getAlarmId, alarmIds));
        return alarmDevices.stream().map(AlarmDevice::getDeviceId).collect(Collectors.toList());
    }

    /**
     * 是否健康设备
     * @param device 设备信息
     * @param alarmDeviceIds 告警设备id集合
     * @return true->健康;false->不健康
     */
    private boolean isHealth(IocDevice device,List<Long> alarmDeviceIds,List<Long> problemDeviceIds){
        //折旧未到期 && 未产生告警 && 未产生报事报修
        return !expiryDepreciation(device) && !alarmDeviceIds.contains(device.getId()) && !problemDeviceIds.contains(device.getId());
    }

    /**
     * 折旧是否到期
     * @param device 设备信息
     * @return true->到期;false->未到期
     */
    private boolean expiryDepreciation(IocDevice device){
        if(device.getUseDate() == null || device.getDepreciationMonth() == null){
            return false;
        }
        LocalDate now = LocalDate.now();
        LocalDate useDate = device.getUseDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        //使用月数
        long monthsUsed = ChronoUnit.MONTHS.between(useDate.withDayOfMonth(1),now.withDayOfMonth(1));
        return monthsUsed > device.getDepreciationMonth();
    }
}
