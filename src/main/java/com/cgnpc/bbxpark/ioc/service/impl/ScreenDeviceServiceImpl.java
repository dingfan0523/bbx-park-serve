package com.cgnpc.bbxpark.ioc.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cgnpc.bbxpark.acl.haikang.model.HkResult;
import com.cgnpc.bbxpark.acl.haikang.model.PlayBackURLsModel;
import com.cgnpc.bbxpark.acl.haikang.model.PlayPreviewURLsParam;
import com.cgnpc.bbxpark.acl.haikang.util.HaikangApiEnum;
import com.cgnpc.bbxpark.acl.haikang.util.HikHttpUtil;
import com.cgnpc.bbxpark.acl.uic.service.IUserApiService;
import com.cgnpc.bbxpark.common.constant.AlarmInfoConstant;
import com.cgnpc.bbxpark.common.enums.Delete;
import com.cgnpc.bbxpark.common.enums.Status;
import com.cgnpc.bbxpark.common.enums.WorkOrderSourceEnum;
import com.cgnpc.bbxpark.common.enums.WorkOrderStatusEnum;
import com.cgnpc.bbxpark.common.utils.*;
import com.cgnpc.bbxpark.device.domain.*;
import com.cgnpc.bbxpark.device.dto.model.IocDeviceModel;
import com.cgnpc.bbxpark.device.mapper.*;
import com.cgnpc.bbxpark.device.service.IIocDeviceService;
import com.cgnpc.bbxpark.ioc.dto.model.*;
import com.cgnpc.bbxpark.ioc.dto.param.AlarmAnalysisParam;
import com.cgnpc.bbxpark.ioc.dto.param.DevicePageParam;
import com.cgnpc.bbxpark.ioc.dto.param.DevicePointParam;
import com.cgnpc.bbxpark.ioc.service.IScreenDeviceService;
import com.cgnpc.bbxpark.settings.dto.model.FileModel;
import com.cgnpc.bbxpark.space.domain.ParkSpace;
import com.cgnpc.bbxpark.space.dto.model.ParkSpaceFullModel;
import com.cgnpc.bbxpark.space.mapper.ParkSpaceRepository;
import com.cgnpc.bbxpark.space.service.IParkSpaceService;
import com.cgnpc.bbxpark.workorder.domain.WorkOrder;
import com.cgnpc.bbxpark.workorder.domain.WorkOrderDevice;
import com.cgnpc.bbxpark.workorder.mapper.WorkOrderDeviceRepository;
import com.cgnpc.bbxpark.workorder.mapper.WorkOrderRepository;
import org.apache.commons.math3.util.Precision;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ScreenDeviceServiceImpl implements IScreenDeviceService {
    @Resource
    private ParkSpaceRepository parkSpaceRepository;
    @Resource
    private IParkSpaceService parkSpaceService;
    @Resource
    private IocDeviceRepository iocDeviceRepository;
    @Resource
    private IocProductRepository productRepository;
    @Autowired
    private AlarmInfoRepository alarmInfoRepository;
    @Autowired
    private AlarmDeviceRepository alarmDeviceRepository;
    @Autowired
    private DevicePointRepository devicePointRepository;
    @Autowired
    private DeviceHealthRecordRepository deviceHealthRecordRepository;
    @Autowired
    private DeviceGroupRepository deviceGroupRepository;
    @Autowired
    private DeviceGroupRelRepository deviceGroupRelRepository;
    @Autowired
    private WorkOrderRepository workOrderRepository;
    @Autowired
    private WorkOrderDeviceRepository workOrderDeviceRepository;
    @Autowired
    private IIocDeviceService iIocDeviceService;
    @Autowired
    private IUserApiService userApiService;
    @Value("${hrcenter.department.cangnan}")
    private String parentDeptId;
    @Autowired
    @Qualifier("kafkaMsgThreadPool")
    private Executor executorService;

    @Override
    public DeviceOverviewModel getDeviceOverview(String sslcCode) {
        Long tenantId = WebFrameworkUtils.getHeaderTenantId();
        List<IocDevice> list = iocDeviceRepository.listBySpace(tenantId,sslcCode,null);
        DeviceOverviewModel model = new DeviceOverviewModel();
        //过滤出非物联网平台设备
        long nonIntelligentCount = list.stream().filter(d->d.getIotDevicePlatform() != null && d.getIotDevicePlatform() == 0).count();
        model.setTotal(list.size());
        model.setNonIntelligentCount((int) nonIntelligentCount);
        model.setIntelligentCount(model.getTotal() - model.getNonIntelligentCount());
        return model;
    }
//    private Optional<String[]> findSecondDepart(String departmentIdPath, String departmentNamePath){
//        List<String> ids = Arrays.asList(departmentIdPath.split("\\\\"));
//        List<String> names = Arrays.asList(departmentNamePath.split("\\\\"));
//        return IntStream.range(0,ids.size() - 1).filter(i -> ids.get(i).equals(parentDeptId))
//                .mapToObj(i -> new String[]{ids.get(i + 1),names.get(i + 1)}).findFirst();
//    }

    @Override
    public List<DeviceDeptDistributionModel> getDeptDeviceDistribution(String sslcCode) {
        return iocDeviceRepository.findDeptDeviceDistribution(WebFrameworkUtils.getHeaderTenantId(),sslcCode);
//        Long tenantId = WebFrameworkUtils.getHeaderTenantId();
//        //查询设备数据
//        List<IocDevice> devices = iocDeviceRepository.listBySpace(tenantId,sslcCode,null);
//        //获取设备使用人id集合
//        List<String> staffNos = devices.stream().map(IocDevice::getUseUid).filter(StringUtils::isNotEmpty).collect(Collectors.toList());
//        //根据使用人id集合获取用户信息集合
//        List<UserInfoModel> users = userApiService.getByStaffNos(staffNos);
//
//        Map<String,String> userDeptIdMap = new HashMap<>(16);
//        Map<String,String> deptMap = new HashMap<>(16);
//        Map<String,Integer> deptNumMap = new HashMap<>(16);
//        //遍历用户集合,存储用户id->部门id映射数据,以及部门id->部门名称数据
//        users.forEach(user->{
//            //因为需要统计的是二级部门,所以需要根据用户所在的部门路径找到对应层级的部门id和部门名称
//            Optional<String[]> depart = findSecondDepart(user.getDepartmentIdPath(),user.getDepartmentNamePath());
//            depart.ifPresent(d->{
//                userDeptIdMap.put(user.getUserId(),d[0]);
//                deptMap.put(d[0],d[1]);
//            });
//        });
//        //获取总条数
//        long total = devices.stream().filter(d->StringUtils.isNotEmpty(d.getUseUid()) && userDeptIdMap.containsKey(d.getUseUid())).count();
//        //遍历得到每个部门的设备数量
//        devices.stream().filter(d->StringUtils.isNotEmpty(d.getUseUid()) && userDeptIdMap.containsKey(d.getUseUid())).forEach(d->{
//            String deptId = userDeptIdMap.get(d.getUseUid());
//            if(!deptNumMap.containsKey(deptId)){
//                deptNumMap.put(deptId,0);
//            }
//            deptNumMap.put(deptId,deptNumMap.get(deptId) + 1);
//        });
//        //遍历组装数据,得到部门id、部门名称、部门设备数量、部门设备数量占比
//        List<DeviceDeptDistributionModel> list = new ArrayList<>();
//        deptNumMap.forEach((k,v)->{
//            DeviceDeptDistributionModel model = new DeviceDeptDistributionModel();
//            model.setDeptId(k);
//            model.setDeptName(deptMap.get(k));
//            model.setDeviceCount(deptNumMap.get(k));
//            model.setNumRate((double) deptNumMap.get(k) / total);
//            list.add(model);
//        });
//        return list;
    }

    @Override
    public DeviceLifeDistributionModel getLifeDistribution(String sslcCode) {
//        List<IocDevice> devices = iocDeviceRepository.selectList(wrapper.isNotNull(IocDevice::getUseDate));
        List<IocDevice> devices = iocDeviceRepository.listBySpace(WebFrameworkUtils.getHeaderTenantId(),sslcCode,null);
        int total = devices.size();
        devices = devices.stream().filter(d -> d.getUseDate() != null && d.getDepreciationMonth() != null).collect(Collectors.toList());
        //预定义区间
        List<String> rangeLabels = Arrays.asList("<=1年","1-3年","3-5年",">5年");
        //按区间分组计数
        LocalDate now = LocalDate.now();
        Map<String,Long> rangeMap = devices.stream().map(d -> d.getUseDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
                .map(date -> ChronoUnit.YEARS.between(date,now)).collect(Collectors.groupingBy(year -> {
                    if(year <= 1) return rangeLabels.get(0);
                    else if(year<=3) return rangeLabels.get(1);
                    else if(year<=5) return rangeLabels.get(2);
                    else return rangeLabels.get(3);
                },Collectors.counting()));
        //数组组装
        List<DeviceLifeDistributionModel.LifeRange> ranges = rangeLabels.stream().map(label -> {
            DeviceLifeDistributionModel.LifeRange range = new DeviceLifeDistributionModel.LifeRange();
            range.setRange(label);
            range.setCount(rangeMap.getOrDefault(label,0L).intValue());
            return range;
        } ).collect(Collectors.toList());
        return DeviceLifeDistributionModel.builder().warningThreshold((int)(total * 0.25)).ranges(ranges).build();
    }

    @Override
    public DeviceDepreciationAnalysisModel getDepreciationAnalysis(String sslcCode) {
        List<IocDevice> devices = iocDeviceRepository.listBySpace(WebFrameworkUtils.getHeaderTenantId(),sslcCode,null);
        int total = devices.size();
        devices = devices.stream().filter(d -> d.getUseDate() != null).collect(Collectors.toList());
        //预定义区间
        List<String> rangeLabels = Arrays.asList("0-10%","10-50%","50-90%","90-100%");
        //按区间分组计数
        Map<String,Long> rangeMap = devices.stream().map(this::calculateDepreciationRate).collect(Collectors.groupingBy(rate -> {
                    if(rate <= 10) return rangeLabels.get(0);
                    else if(rate <= 50) return rangeLabels.get(1);
                    else if(rate <= 90) return rangeLabels.get(2);
                    else return rangeLabels.get(3);
                },Collectors.counting()));
        //数组组装
        List<DeviceDepreciationAnalysisModel.DepreciationRange> ranges = rangeLabels.stream().map(label -> {
            DeviceDepreciationAnalysisModel.DepreciationRange range = new DeviceDepreciationAnalysisModel.DepreciationRange();
            range.setRange(label);
            range.setCount(rangeMap.getOrDefault(label,0L).intValue());
            return range;
        } ).collect(Collectors.toList());
        return DeviceDepreciationAnalysisModel.builder().warningThreshold((int)(total * 0.2)).ranges(ranges).build();
    }

    @Override
    public StrategicMetricsModel getStrategicMetrics(String sslcCode) {
        return null;
    }

    @Override
    public IntelligentDeviceOverview getIntelligentOverview(String sslcCode) {
        List<IocDevice> devices = iocDeviceRepository.listBySpace(WebFrameworkUtils.getHeaderTenantId(),sslcCode,null);
        devices = devices.stream().filter(d->d.getIotDevicePlatform() != 0).collect(Collectors.toList());
        //正在告警的设备id集合
        List<Long> alarmDeviceIds = findAlarmDeviceIds();
        //告警设备数量
        long alarmDeviceCount = devices.stream().filter(d->alarmDeviceIds.contains(d.getId())).count();
        //告警设备占比
        double alarmDeviceRate = Precision.round((double)(alarmDeviceCount * 100 / devices.size()),2);
        return IntelligentDeviceOverview.builder().total(devices.size())
                .alarmCount((int)alarmDeviceCount).alarmRate(alarmDeviceRate)
                .normalCount((int)(devices.size() - alarmDeviceCount)).normalRate(100 - alarmDeviceRate).build();
    }

    /**
     * todo:是否存在下级还未返回
     *
     */
    @Override
    public List<DeviceSpaceDistributionModel> getIntelligentDistribution(String sslcCode,Long spaceId) {
        if(StringUtils.isNotEmpty(sslcCode)){
            ParkSpace parkSpace = parkSpaceRepository.selectOne(Wrappers.<ParkSpace>lambdaQuery().eq(ParkSpace::getSslcCode,sslcCode));
            spaceId = parkSpace.getId();
        }
        return iocDeviceRepository.findIntelligentDistribution(WebFrameworkUtils.getHeaderTenantId(),spaceId,true);
    }

    @Override
    public List<DeviceHealthTrendModel> getHealthTrend(String sslcCode) {
        return deviceHealthRecordRepository.findDeviceHealthRecord(WebFrameworkUtils.getHeaderTenantId(),sslcCode);
    }

    @Override
    public List<AlarmAnalysisModel> getAlarmAnalysis(AlarmAnalysisParam param) {
        //根据设备分组维度统计告警数量
        List<AlarmAnalysisModel> list = iocDeviceRepository.findDeviceGroupAlarm(param,WebFrameworkUtils.getHeaderTenantId());
        if(CollectionUtils.isEmpty(list)){
            //如果不存在数据,可能是到了最下级分组了,就直接统计分组下每个设备的告警数量
            return iocDeviceRepository.findDeviceAlarm(param,WebFrameworkUtils.getHeaderTenantId());
        }
        return list;
    }

    @Override
    public List<DevicePointModel> pointList(DevicePointParam param) {
        Long tenantId = WebFrameworkUtils.getHeaderTenantId();
        List<IocDevice> iocDevices = iocDeviceRepository.listBySpace(WebFrameworkUtils.getHeaderTenantId(),param.getSslcCode(),param.getSpaceId());
        //智能化设备
        List<DevicePointModel> list = iocDevices.stream().filter(d -> d.getIotDevicePlatform() != null && d.getIotDevicePlatform() != 0)
                .map(d -> BeanUtils.convertTo(d,DevicePointModel::new)).collect(Collectors.toList());
        if(CollectionUtils.isEmpty(list)){
            return Collections.emptyList();
        }
        //查询设备点位信息
        CompletableFuture<Void> pointFuture = CompletableFuture.runAsync(() -> devicePositionHandle(list),executorService);
        //设备分组信息处理
        CompletableFuture<Void> groupFuture = CompletableFuture.runAsync(() -> deviceGroupHandle(list),executorService);
       //查询设备告警信息
        CompletableFuture<Void> alarmFuture = CompletableFuture.runAsync(() -> deviceAlarmHandle(list,tenantId),executorService);
        //设备空间信息处理
        CompletableFuture<Void> spaceFuture = CompletableFuture.runAsync(() -> spaceHandle(list),executorService);
        //设备产品信息处理
        CompletableFuture<Void> productFuture = CompletableFuture.runAsync(() -> productHandle(list),executorService);
        //报事报修工单设备处理
        CompletableFuture<Void> workOrderFuture = CompletableFuture.runAsync(() -> workOrderHandle(list,tenantId),executorService);
        CompletableFuture.allOf(pointFuture,groupFuture, alarmFuture, spaceFuture, productFuture,workOrderFuture).join();
        return list;
    }

    @Override
    public IPage<DeviceModel> list(DevicePageParam param) {
        if(StringUtils.isNotEmpty(param.getSslcCode())){
            ParkSpace space = parkSpaceRepository.selectOne(Wrappers.<ParkSpace>lambdaQuery().eq(ParkSpace::getSslcCode,param.getSslcCode()));
            if(space != null){
                param.setSpaceId(space.getId());
            }
        }
        return iocDeviceRepository.pageByScreen(new Page<>(param.getCurrent(),param.getSize()),param,WebFrameworkUtils.getHeaderTenantId());
    }

    /**
     * todo:告警和在线状态以及时间
     * @param id
     * @return
     */
    @Override
    public DeviceDetailModel detail(Long id) {
        IocDeviceModel iocDeviceModel = iIocDeviceService.detail(id);
        DeviceDetailModel model = BeanUtils.convertTo(iocDeviceModel,DeviceDetailModel::new);
        model.setGroupName(iocDeviceModel.getGroupNames());
        model.setDeviceName(iocDeviceModel.getDeviceName());
        model.setDeviceStatus(iocDeviceModel.getIotDeviceStatus());
        model.setDeviceStatusTime(iocDeviceModel.getIotDeviceStatusTime());
        model.setUseDeptName(iocDeviceModel.getDepartmentName());
        if(CollectionUtils.isNotEmpty(iocDeviceModel.getFileModelList())){
            model.setImages(iocDeviceModel.getFileModelList().stream().map(FileModel::getUrl).collect(Collectors.toList()));
        }
        AlarmInfo alarmInfo = getLastAlarmByDeviceId(id);
        model.setAlarmStatus(alarmInfo != null ? 1:0);
        model.setAlarmStatusTime(alarmInfo != null ?alarmInfo.getAlarmLastTime():null);
        if(iocDeviceModel.getIocProductModel() != null){
            model.setProduct(BeanUtils.convertTo(iocDeviceModel.getIocProductModel(),ProductModel::new));
            if(CollectionUtils.isNotEmpty(iocDeviceModel.getIocProductModel().getFileList())){
                model.getProduct().setPdImages(iocDeviceModel.getIocProductModel().getFileList().stream().map(FileModel::getUrl).collect(Collectors.toList()));
            }
        }
        return model;
    }



    @Override
    public DeviceSummaryModel summaryDetail(Long id) {
        IocDevice device = iocDeviceRepository.selectById(id);
        DeviceSummaryModel model = BeanUtils.convertTo(device,DeviceSummaryModel::new);
        model.setDeviceDn(device.getIotDeviceDn());
        model.setDeviceStatus(device.getIotDeviceStatus());
        //空间名称
        Map<Long, ParkSpaceFullModel> spaceMap = parkSpaceService.findFullSpaceMap(Collections.singletonList(device.getSpaceId()),WebFrameworkUtils.getHeaderTenantId());
        model.setSpaceName(spaceMap.getOrDefault(device.getSpaceId(),new ParkSpaceFullModel()).getFullPath());
        //产品名称
        IocProduct product = productRepository.selectById(device.getProductId());
        Optional.of(product).ifPresent(p->model.setProductName(p.getPdName()));
        //分组信息
        List<DeviceGroupRel> rels = deviceGroupRelRepository.selectList(Wrappers.<DeviceGroupRel>lambdaQuery().eq(DeviceGroupRel::getDeviceId,id));
        DeviceGroup deviceGroup = deviceGroupRepository.selectById(rels.get(0).getGroupId());
        model.setGroupName(deviceGroup.getGroupName());
        model.setGroupCode(deviceGroup.getGroupCode());
        //告警信息
        List<AlarmInfo> alarmInfos = alarmInfoRepository.selectList(Wrappers.<AlarmInfo>lambdaQuery().select(AlarmInfo::getId).ne(AlarmInfo::getAlarmStatus, AlarmInfoConstant.ALARM_STATUS_3).eq(AlarmInfo::getTenantId, WebFrameworkUtils.getHeaderTenantId()));
        if(CollectionUtils.isNotEmpty(alarmInfos)){
            List<Long> alarmIds = alarmInfos.stream().map(AlarmInfo::getId).collect(Collectors.toList());
            List<AlarmDevice> alarmDevices = alarmDeviceRepository.selectList(Wrappers.<AlarmDevice>lambdaQuery().in(AlarmDevice::getAlarmId, alarmIds));
            List<Long> deviceIds = alarmDevices.stream().map(AlarmDevice::getDeviceId).collect(Collectors.toList());
            model.setAlarmStatus(deviceIds.contains(id) ? Status.enabled.getKey() : Status.disabled.getKey());
        }
        return model;
    }

    @Override
    public List<ProductListModel> productList() {
        List<IocProduct> products = productRepository.selectList(Wrappers.<IocProduct>lambdaQuery()
                .eq(IocProduct::getDeleted,Delete.NORMAL.getKey()).eq(IocProduct::getTenantId,WebFrameworkUtils.getHeaderTenantId()));
        return products.stream().map(p->BeanUtils.convertTo(p,ProductListModel::new)).collect(Collectors.toList());
    }

    @Override
    public List<KeywordSearchModel> search(String keyword) {
        List<IocDevice> devices = iocDeviceRepository.selectList(Wrappers.<IocDevice>lambdaQuery()
                .eq(IocDevice::getReadingDevice,Status.enabled.getKey()).eq(IocDevice::getEnableStatus,Status.enabled.getKey())
                .and(w->w.like(IocDevice::getDeviceName,keyword).or().like(IocDevice::getDeviceCode,keyword))
                .eq(IocDevice::getDeleted,Delete.NORMAL.getKey()).eq(IocDevice::getTenantId,WebFrameworkUtils.getHeaderTenantId()));
        Set<Long> spaceIds = devices.stream().map(IocDevice::getSpaceId).collect(Collectors.toSet());
        List<ParkSpace> spaces = CollectionUtils.isEmpty(spaceIds) ? Collections.emptyList() : parkSpaceRepository.selectList(Wrappers.<ParkSpace>lambdaQuery().in(ParkSpace::getId,spaceIds).select(ParkSpace::getId,ParkSpace::getSslcCode));
        Map<Long,String> spaceMap = spaces.stream().filter(s->StringUtils.isNotEmpty(s.getSslcCode())).collect(Collectors.toMap(ParkSpace::getId,ParkSpace::getSslcCode,(v1,v2)->v2));
        return devices.stream().map(d->{
            KeywordSearchModel m = new KeywordSearchModel();
            m.setSpaceId(d.getId());
            m.setSslcCode(d.getSpaceId() == null ? "" : spaceMap.getOrDefault(d.getSpaceId(),""));
            m.setContent(d.getDeviceName());
            return m;
        }).collect(Collectors.toList());
    }

    @Override
    public PlayBackURLsModel getHKCameraPreviewUrl(Long id, Integer streamType) {
        AssertUtils.isNotEmpty(id, "设备id不能为空");
        IocDevice iocDevice = iocDeviceRepository.selectById(id);
        AssertUtils.isNotEmpty(iocDevice, "设备不存在");
        AssertUtils.isNotEmpty(iocDevice.getIotDeviceDn(), "非物联网设备");
        PlayPreviewURLsParam params = new PlayPreviewURLsParam();
        params.setProtocol("ws");
        params.setCameraIndexCode(iocDevice.getIotDeviceDn());
        params.setStreamType(ObjectUtil.isEmpty(streamType) ? 1  : streamType);
        HkResult result = HikHttpUtil.doPostStringArtemis(HaikangApiEnum.PLAYLIVEURLS, JSON.toJSONString(params));
        return JSONUtil.toBean(JSONUtil.toJsonStr(result.getData()), PlayBackURLsModel.class);
    }

    /**
     * 计算设备折旧率
     * @param device 设备
     * @return 折旧率
     */
    private Integer calculateDepreciationRate(IocDevice device){
        try{
            LocalDate useDate = device.getUseDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate now = LocalDate.now();
            //使用月数
            long monthsUsed = ChronoUnit.MONTHS.between(useDate.withDayOfMonth(1),now.withDayOfMonth(1));
            //如果使用日期大于当前日期,则返回0
            if(monthsUsed < 0){
                return 0;
            }
            //否则计算折扣率
            int rate = (int)((monthsUsed * 100) / device.getDepreciationMonth());
            //折扣率不能超过100%
            return Math.min(rate,100);
        }catch (Exception e){
            return 0;
        }
    }

    /**
     * 查询告警设备id集合
     * @return 设备id集合
     */
    private List<Long> findAlarmDeviceIds(){
        List<AlarmInfo> alarmInfos = alarmInfoRepository.selectList(Wrappers.<AlarmInfo>lambdaQuery().select(AlarmInfo::getId).ne(AlarmInfo::getAlarmStatus, AlarmInfoConstant.ALARM_STATUS_3).eq(AlarmInfo::getTenantId, WebFrameworkUtils.getHeaderTenantId()));
        if(CollectionUtils.isEmpty(alarmInfos)){
            return Collections.emptyList();
        }
        List<Long> alarmIds = alarmInfos.stream().map(AlarmInfo::getId).collect(Collectors.toList());
        List<AlarmDevice> alarmDevices = alarmDeviceRepository.selectList(Wrappers.<AlarmDevice>lambdaQuery().in(AlarmDevice::getAlarmId, alarmIds));
        return alarmDevices.stream().map(AlarmDevice::getDeviceId).collect(Collectors.toList());
    }

    /**
     * 设备点位数据处理
     * @param devices 设备集合
     */
    private void devicePositionHandle(List<DevicePointModel> devices){
        List<String> deviceIds = devices.stream().map(DevicePointModel::getIotDeviceDn).filter(ObjectUtil::isNotEmpty).collect(Collectors.toList());
        List<DevicePoint> points = devicePointRepository.selectList(Wrappers.<DevicePoint>lambdaQuery().in(DevicePoint::getDeviceId,deviceIds).select(DevicePoint::getDeviceId,DevicePoint::getPositioning));
        Map<String,String> pointMap = points.stream().collect(Collectors.toMap(DevicePoint::getDeviceId,DevicePoint::getPositioning,(v1,v2)->v2));
        devices.forEach(m-> {
            m.setPositioning(pointMap.getOrDefault(m.getIotDeviceDn(),""));
            m.setDeviceStatus(Status.enabled.getKey().equals(m.getIotDeviceStatus()) ? "1" :"0");
        });
    }

    /**
     * 设备分组数据处理
     * @param devices 设备集合
     */
    private void deviceGroupHandle(List<DevicePointModel> devices){
        List<Long> deviceIds = devices.stream().map(DevicePointModel::getId).filter(ObjectUtil::isNotEmpty).collect(Collectors.toList());
        List<DeviceGroupRel> rels = deviceGroupRelRepository.selectList(Wrappers.<DeviceGroupRel>lambdaQuery().in(DeviceGroupRel::getDeviceId,deviceIds).select(DeviceGroupRel::getGroupId,DeviceGroupRel::getDeviceId));
        if(CollectionUtils.isEmpty(rels)){
            return;
        }
        Map<Long,Long> relMap = rels.stream().collect(Collectors.toMap(DeviceGroupRel::getDeviceId,DeviceGroupRel::getGroupId,(v1,v2)->v1));
        Set<Long> groupIds = rels.stream().map(DeviceGroupRel::getGroupId).collect(Collectors.toSet());
        List<DeviceGroup> groups = deviceGroupRepository.selectList(Wrappers.<DeviceGroup>lambdaQuery().in(DeviceGroup::getId,groupIds).select(DeviceGroup::getId,DeviceGroup::getGroupCode,DeviceGroup::getGroupName));
        Map<Long,DeviceGroup> groupMap = groups.stream().collect(Collectors.toMap(DeviceGroup::getId, Function.identity()));
        devices.stream().filter(d->relMap.containsKey(d.getId())).forEach(d->{
            DeviceGroup group = groupMap.getOrDefault(relMap.get(d.getId()),null);
            if(group != null){
                d.setGroupCode(group.getGroupCode());
                d.setGroupName(group.getGroupName());
            }
        });
    }

    /**
     * 设备告警信息处理
     * @param devices 设备集合
     */
    private void deviceAlarmHandle(List<DevicePointModel> devices,Long tenantId){
        List<AlarmInfo> alarmInfos = alarmInfoRepository.selectList(Wrappers.<AlarmInfo>lambdaQuery().select(AlarmInfo::getId).ne(AlarmInfo::getAlarmStatus, AlarmInfoConstant.ALARM_STATUS_3).eq(AlarmInfo::getTenantId, tenantId));
        if(CollectionUtils.isEmpty(alarmInfos)){
            return;
        }
        List<Long> alarmIds = alarmInfos.stream().map(AlarmInfo::getId).collect(Collectors.toList());
        List<AlarmDevice> alarmDevices = alarmDeviceRepository.selectList(Wrappers.<AlarmDevice>lambdaQuery().in(AlarmDevice::getAlarmId, alarmIds));
        if(CollectionUtils.isEmpty(alarmDevices)){
            return;
        }
        Map<Long,AlarmInfo> alarmMap = alarmInfos.stream().collect(Collectors.toMap(AlarmInfo::getId,a->a,(v1,v2)->v2));
        Map<Long,List<Long>> deviceAlarmMap = alarmDevices.stream().collect(Collectors.groupingBy(AlarmDevice::getDeviceId,Collectors.mapping(AlarmDevice::getAlarmId,Collectors.toList())));

        devices.stream().filter(l->deviceAlarmMap.containsKey(l.getId())).forEach(d->{
            List<AlarmModel> alarms = deviceAlarmMap.get(d.getId()).stream().map(alarmId-> BeanUtils.convertTo(alarmMap.get(alarmId),AlarmModel::new)).collect(Collectors.toList());
            d.setAlarmStatus(Status.enabled.getValue());
            d.setAlarms(alarms);
        });
    }

    /**
     * 设备空间信息处理
     * @param devices 设备集合
     */
    private void spaceHandle(List<DevicePointModel> devices) {
        if(CollectionUtil.isNotEmpty(devices)){
            List<Long> spaceIds = devices.stream().map(DevicePointModel::getSpaceId).filter(ObjectUtil::isNotEmpty).collect(Collectors.toList());
            Map<Long, ParkSpaceFullModel> fullSpaceMap = CollectionUtil.isEmpty(spaceIds) ? new HashMap<>() : parkSpaceService.findFullSpaceMap(spaceIds, null);
            devices.stream().filter(d -> d.getSpaceId() != null).forEach(d -> d.setSpaceName(fullSpaceMap.getOrDefault(d.getSpaceId(),new ParkSpaceFullModel()).getFullPath()));
        }
    }

    /**
     * 设备产品信息处理
     * @param devices 设备集合
     */
    private void productHandle(List<DevicePointModel> devices){
        if(CollectionUtil.isNotEmpty(devices)){
            List<Long> productIds = devices.stream().map(DevicePointModel::getProductId).filter(ObjectUtil::isNotEmpty).collect(Collectors.toList());
            List<IocProduct> iocProducts = CollectionUtil.isEmpty(productIds) ? Collections.emptyList() : productRepository.selectList(Wrappers.<IocProduct>lambdaQuery().in(IocProduct::getId, productIds));
            Map<Long, String> productMap = CollectionUtil.isEmpty(iocProducts)? new HashMap<>(4) : iocProducts.stream().collect(Collectors.toMap(IocProduct::getId, IocProduct::getPdName));
            devices.stream().filter(d -> d.getProductId() != null).forEach(d -> d.setProductName(productMap.getOrDefault(d.getProductId(),"")));
        }
    }

    private void workOrderHandle(List<DevicePointModel> devices,Long tenantId){
        if(CollectionUtils.isEmpty(devices)){
            //查询报事报修类型工单设备
            List<WorkOrder> workOrders = workOrderRepository.selectList(Wrappers.<WorkOrder>lambdaQuery().eq(WorkOrder::getSource, WorkOrderSourceEnum.PERSON.getCode())
                    .ne(WorkOrder::getStatus, WorkOrderStatusEnum.COMPLETED.getCode()).eq(WorkOrder::getDeleted, Delete.NORMAL.getKey())
                    .eq(WorkOrder::getTenantId,tenantId).select(WorkOrder::getId));
            List<Long> orderIds = workOrders.stream().map(WorkOrder::getId).collect(Collectors.toList());
            if(CollectionUtils.isNotEmpty(orderIds)){
                List<WorkOrderDevice> workOrderDevices = workOrderDeviceRepository.selectList(Wrappers.<WorkOrderDevice>lambdaQuery()
                        .eq(WorkOrderDevice::getWorkOrderId,orderIds).isNotNull(WorkOrderDevice::getDeviceId).select(WorkOrderDevice::getDeviceId));
                Set<Long> deviceIds = workOrderDevices.stream().map(WorkOrderDevice::getDeviceId).collect(Collectors.toSet());
                devices.stream().filter(d->deviceIds.contains(d.getId())).forEach(d->d.setWorkOrderStatus("1"));
            }
        }
    }

    private AlarmInfo getLastAlarmByDeviceId(Long id){
        //设备告警数据
        List<AlarmInfo> alarmInfos = alarmInfoRepository.selectList(Wrappers.<AlarmInfo>lambdaQuery().select(AlarmInfo::getId,AlarmInfo::getAlarmLastTime).ne(AlarmInfo::getAlarmStatus, AlarmInfoConstant.ALARM_STATUS_3).eq(AlarmInfo::getTenantId, WebFrameworkUtils.getHeaderTenantId()));
        if (CollectionUtil.isEmpty(alarmInfos)) {
            return null;
        }
        List<Long> alarmIds = alarmInfos.stream().map(AlarmInfo::getId).collect(Collectors.toList());
        List<AlarmDevice> alarmDevices = alarmDeviceRepository.selectList(Wrappers.<AlarmDevice>lambdaQuery().in(AlarmDevice::getAlarmId, alarmIds).eq(AlarmDevice::getDeviceId,id));
        if(CollectionUtils.isEmpty(alarmDevices)){
            return null;
        }
        List<Long> finalAlarmIds = alarmDevices.stream().map(AlarmDevice::getAlarmId).collect(Collectors.toList());
        return alarmInfos.stream().filter(e->finalAlarmIds.contains(e.getId())).max(Comparator.comparing(AlarmInfo::getAlarmLastTime)).orElse(null);
    }
}
