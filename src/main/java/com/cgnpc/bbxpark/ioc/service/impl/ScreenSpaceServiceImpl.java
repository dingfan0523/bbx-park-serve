package com.cgnpc.bbxpark.ioc.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.cgnpc.bbxpark.acl.uic.model.OrgDepartmentNode;
import com.cgnpc.bbxpark.acl.uic.service.IDepartmentApiService;
import com.cgnpc.bbxpark.acl.uic.service.IUserApiService;
import com.cgnpc.bbxpark.common.constant.SystemResultCode;
import com.cgnpc.bbxpark.common.enums.Delete;
import com.cgnpc.bbxpark.common.enums.Status;
import com.cgnpc.bbxpark.common.enums.WorkOrderSourceEnum;
import com.cgnpc.bbxpark.common.enums.WorkOrderStatusEnum;
import com.cgnpc.bbxpark.common.utils.*;
import com.cgnpc.bbxpark.device.domain.IocDevice;
import com.cgnpc.bbxpark.device.dto.model.IocDeviceModel;
import com.cgnpc.bbxpark.device.dto.param.IocDevicePageParam;
import com.cgnpc.bbxpark.device.mapper.IocDeviceRepository;
import com.cgnpc.bbxpark.device.service.IIocDeviceService;
import com.cgnpc.bbxpark.ioc.dto.model.*;
import com.cgnpc.bbxpark.ioc.dto.param.ConstructionPageParam;
import com.cgnpc.bbxpark.ioc.dto.param.DevicePageParam;
import com.cgnpc.bbxpark.ioc.dto.param.StationPageParam;
import com.cgnpc.bbxpark.ioc.dto.param.StoragePageParam;
import com.cgnpc.bbxpark.ioc.service.IScreenSpaceService;
import com.cgnpc.bbxpark.meeting.domain.MeetingRoom;
import com.cgnpc.bbxpark.meeting.dto.model.MeetingRoomAvgRateModel;
import com.cgnpc.bbxpark.meeting.mapper.MeetingRoomDailyStatisticsRepository;
import com.cgnpc.bbxpark.meeting.mapper.MeetingRoomRepository;
import com.cgnpc.bbxpark.space.domain.*;
import com.cgnpc.bbxpark.space.dto.model.ParkSpaceFullModel;
import com.cgnpc.bbxpark.space.dto.model.SpaceBasicInfoModel;
import com.cgnpc.bbxpark.space.dto.model.UserInfoModel;
import com.cgnpc.bbxpark.space.dto.param.StationListParam;
import com.cgnpc.bbxpark.space.mapper.*;
import com.cgnpc.bbxpark.space.service.IParkSpaceService;
import com.cgnpc.bbxpark.space.service.ISpaceBasicInfoService;
import com.cgnpc.bbxpark.space.service.IStationService;
import com.cgnpc.bbxpark.workorder.domain.WorkOrder;
import com.cgnpc.bbxpark.workorder.domain.WorkOrderDevice;
import com.cgnpc.bbxpark.workorder.mapper.WorkOrderDeviceRepository;
import com.cgnpc.bbxpark.workorder.mapper.WorkOrderRepository;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author dingfan
 * @version 1.0
 * @date 2026/2/28 14:12
 */
@Service
public class ScreenSpaceServiceImpl implements IScreenSpaceService {
    @Autowired
    private SpaceBasicInfoRepository spaceBasicInfoRepository;
    @Autowired
    private StationRepository stationRepository;
    @Autowired
    private ParkSpaceRepository parkSpaceRepository;
    @Autowired
    private IocDeviceRepository iocDeviceRepository;
    @Autowired
    private SpaceManagerRepository spaceManagerRepository;
    @Autowired
    private SpaceImageRepository spaceImageRepository;
    @Autowired
    private MeetingRoomDailyStatisticsRepository meetingRoomDailyStatisticsRepository;
    @Autowired
    private MeetingRoomRepository meetingRoomRepository;
    @Autowired
    private WorkOrderRepository workOrderRepository;
    @Autowired
    private WorkOrderDeviceRepository workOrderDeviceRepository;
    @Autowired
    private IDepartmentApiService departmentApiService;
    @Autowired
    private IUserApiService userApiService;
    @Autowired
    private IStationService stationService;
    @Autowired
    private IIocDeviceService iocDeviceService;
    @Autowired
    private ISpaceBasicInfoService spaceBasicInfoService;
    @Autowired
    private IParkSpaceService parkSpaceService;


    @Override
    public List<SpaceTypeDistributionModel> getSpaceTypeDistribution() {
        List<SpaceBasicInfo> spaceList = spaceBasicInfoRepository.selectList(Wrappers.<SpaceBasicInfo>lambdaQuery()
                .eq(SpaceBasicInfo::getTenantId, WebFrameworkUtils.getHeaderTenantId())
                .eq(SpaceBasicInfo::getDeleted, Delete.NORMAL.getKey()));
        return spaceList.stream().collect(Collectors.groupingBy(SpaceBasicInfo::getType,Collectors.counting()))
                .entrySet().stream().map(e-> new SpaceTypeDistributionModel(e.getKey(),e.getValue()))
                .collect(Collectors.toList());
    }

    @Override
    public OfficeSpaceOverviewModel getOfficeSpaceOverview() {
        Long tenantId = WebFrameworkUtils.getHeaderTenantId();
        //获取办公区类型的空间数据
        List<SpaceBasicInfo> spaceList = spaceBasicInfoRepository.selectList(Wrappers.<SpaceBasicInfo>lambdaQuery()
                .eq(SpaceBasicInfo::getType,1).eq(SpaceBasicInfo::getTenantId, tenantId).eq(SpaceBasicInfo::getDeleted, Delete.NORMAL.getKey()));
        //办公总面积
        BigDecimal totalArea = spaceList.stream().map(SpaceBasicInfo::getArea).filter(Objects::nonNull).reduce(BigDecimal.ZERO,BigDecimal::add);
        //总工位
        int totalStation = spaceList.stream().mapToInt(SpaceBasicInfo::getCapacity).filter(Objects::nonNull).sum();
        BigDecimal avgArea = totalStation <= 0 ? BigDecimal.ZERO : totalArea.divide(BigDecimal.valueOf(totalStation),2, RoundingMode.HALF_UP);
        //已分配总人数
        Integer allocStation = stationRepository.selectCount(Wrappers.<Station>lambdaQuery().eq(Station::getTenantId, tenantId).eq(Station::getDeleted, Delete.NORMAL.getKey()));
        BigDecimal utilizationRate = BigDecimal.valueOf(allocStation).multiply(new BigDecimal(100)).divide(BigDecimal.valueOf(totalStation),2,RoundingMode.HALF_UP);
        return OfficeSpaceOverviewModel.builder().officeArea(totalArea).avgOfficeArea(avgArea.doubleValue()).utilizationRate(utilizationRate.doubleValue()).build();
    }

    @Override
    public List<DeptOfficeSpaceAreaModel> getDeptOfficeSpaceArea() {
        List<DeptOfficeSpaceAreaModel> list = spaceBasicInfoRepository.getDeptOfficeSpaceArea(WebFrameworkUtils.getHeaderTenantId());
//        Set<String> deptIds = list.stream().map(DeptOfficeSpaceAreaModel::getDeptId).filter(Objects::nonNull).collect(Collectors.toSet());
//        Map<String,String> deptMap = getDepartMap(deptIds);
//        list.stream().filter(d->deptMap.containsKey(d.getDeptId())).forEach(d->{
//            d.setDeptName(deptMap.get(d.getDeptId()));
//        });
        return list;
    }

    @Override
    public List<DeptOfficeSpaceStationModel> getDeptOfficeSpaceStation() {
        List<DeptOfficeSpaceStationModel> list = spaceBasicInfoRepository.getDeptOfficeSpaceStation(WebFrameworkUtils.getHeaderTenantId());
//        Set<String> deptIds = list.stream().map(DeptOfficeSpaceStationModel::getDeptId).filter(Objects::nonNull).collect(Collectors.toSet());
//        Map<String,String> deptMap = getDepartMap(deptIds);
//        list.stream().filter(d->deptMap.containsKey(d.getDeptId())).forEach(d->{
//                d.setDeptName(deptMap.get(d.getDeptId()));
//        });
        return list;
    }

    @Override
    public List<DeviceSpaceDistributionModel> getIntelligentDistribution(String sslcCode, Long spaceId) {
        if(StringUtils.isNotEmpty(sslcCode)){
            ParkSpace parkSpace = parkSpaceRepository.selectOne(Wrappers.<ParkSpace>lambdaQuery().eq(ParkSpace::getSslcCode,sslcCode));
            spaceId = parkSpace.getId();
        }
        return iocDeviceRepository.findIntelligentDistribution(WebFrameworkUtils.getHeaderTenantId(),spaceId,null);
    }

    @Override
    public MeetingRoomUtilizationOverviewModel getMeetingRoomUtilizationOverview() {
        List<MeetingRoomAvgRateModel> list = meetingRoomDailyStatisticsRepository.getMeetingRoomAvgRate(WebFrameworkUtils.getHeaderTenantId());
        list = list.stream().filter(r->r.getId() != null).collect(Collectors.toList());
        if(CollectionUtils.isEmpty(list)){
            return MeetingRoomUtilizationOverviewModel.builder().build();
        }
        //最高使用率会议室
        MeetingRoomAvgRateModel high = list.get(0);
        MeetingRoom highRoom = meetingRoomRepository.selectById(high.getId());
        //最低使用率会议室
        MeetingRoom lowRoom = list.size() == 1 ? highRoom : meetingRoomRepository.selectById(list.get(list.size() - 1).getId());
        return MeetingRoomUtilizationOverviewModel.builder().avgUseRate(high.getAvgRate())
                .highLoadMeetingRoom(highRoom.getRoomName()).lowLoadMeetingRoom(lowRoom.getRoomName()).build();
    }

    @Override
    public List<MeetingRoomUtilizationAnalysisModel> getMeetingRoomUtilizationAnalysis() {
        return meetingRoomDailyStatisticsRepository.getMeetingRoomUtilizationAnalysis(WebFrameworkUtils.getHeaderTenantId());
    }

    @Override
    public SecurityManagementOverviewModel getSecurityManagementOverview() {
        return SecurityManagementOverviewModel.builder().constructionCount(5).highRiskCount(5).build();
    }

    @Override
    public List<ConstructionModel> getConstructionList() {
        return generateConstructionListData(findRandomRoomsFromFlat());
    }

    @Override
    public IPage<ConstructionModel> getConstructionPage(ConstructionPageParam param) {
        List<ConstructionModel> list = generateConstructionListData(findRandomRoomsFromFlat());
        if(StringUtils.isNotEmpty(param.getSslcCode()) || param.getSpaceId() != null){
            Long spaceId = getSpaceIdOfSslcCode(param.getSslcCode(),param.getSpaceId());
            Map<Long, ParkSpaceFullModel> spaceMap = parkSpaceService.findFullSpaceMap(Collections.singletonList(spaceId),WebFrameworkUtils.getHeaderTenantId());
            list.forEach(l->l.setSpaceName(spaceMap.getOrDefault(spaceId,new ParkSpaceFullModel()).getFullPath()));
        }
        return ConvertUtil.pageConvert(param.getCurrent(),list.size(),param.getSize(),list);
    }

    @Override
    public List<StorageModel> getStorageList() {
        return generateStorageListData(findRandomRoomsFromFlat());
    }

    @Override
    public IPage<StorageModel> getStoragePage(StoragePageParam param) {
        List<StorageModel> list = generateStorageListData(findRandomRoomsFromFlat());
        if(StringUtils.isNotEmpty(param.getSslcCode()) || param.getSpaceId() != null){
            Long spaceId = getSpaceIdOfSslcCode(param.getSslcCode(),param.getSpaceId());
            Map<Long, ParkSpaceFullModel> spaceMap = parkSpaceService.findFullSpaceMap(Collections.singletonList(spaceId),WebFrameworkUtils.getHeaderTenantId());
            list.forEach(l->l.setSpaceName(spaceMap.getOrDefault(spaceId,new ParkSpaceFullModel()).getFullPath()));
        }
        return ConvertUtil.pageConvert(param.getCurrent(),list.size(),param.getSize(),list);
    }

    @Override
    public ConstructionDetailModel getConstructionDetail(Long id) {
        return generateConstructionDetailData(id);
    }

    @Override
    public StorageDetailModel getStorageDetail(Long id) {
        return generateStorageDetailData(id);
    }

    @Override
    public List<SpaceViewModel> getSpaceView(String sslcCode,Integer type) {
        Long tenantId = WebFrameworkUtils.getHeaderTenantId();
        List<ParkSpace> children = new ArrayList<>();
        if(StringUtils.isNotEmpty(sslcCode)){
            ParkSpace parkSpace = parkSpaceRepository.selectOne(Wrappers.<ParkSpace>lambdaQuery().eq(ParkSpace::getSslcCode, sslcCode).eq(ParkSpace::getTenantId, tenantId));
            if(parkSpace != null){
                //查询指定的下级空间
                children = parkSpaceRepository.selectList(Wrappers.<ParkSpace>lambdaQuery().eq(ParkSpace::getParentSpaceId, parkSpace.getId()));
            }
        }
        if(CollectionUtils.isEmpty(children)){
            //查询BBX东区2，中区2360，西区163的楼层空间
            children.addAll(parkSpaceRepository.selectList(Wrappers.<ParkSpace>lambdaQuery().eq(ParkSpace::getParentSpaceId, 2)));
            children.addAll(parkSpaceRepository.selectList(Wrappers.<ParkSpace>lambdaQuery().eq(ParkSpace::getParentSpaceId, 163)));
            children.addAll(parkSpaceRepository.selectList(Wrappers.<ParkSpace>lambdaQuery().eq(ParkSpace::getParentSpaceId, 2360)));
        }
        if (CollectionUtils.isEmpty(children)) {
            return Collections.emptyList();
        }
        //查询空间基础信息
        List<SpaceBasicInfo> spaces = spaceBasicInfoRepository.selectList(Wrappers.<SpaceBasicInfo>lambdaQuery()
                .eq(type != null,SpaceBasicInfo::getType,type).eq(SpaceBasicInfo::getDeleted,Delete.NORMAL.getKey())
                .eq(SpaceBasicInfo::getTenantId,WebFrameworkUtils.getHeaderTenantId()));
        if(CollectionUtils.isEmpty(spaces)){
            return Collections.emptyList();
        }
        List<Long> spaceIds = spaces.stream().map(SpaceBasicInfo::getSpaceId).collect(Collectors.toList());
        //获取空间路径
        Map<Long, ParkSpaceFullModel> spaceMap = parkSpaceService.findFullSpaceMap(spaceIds, tenantId);
        Set<Long> spaceIdSet = spaceMap.values().stream().map(ParkSpaceFullModel::getIdFullPath)
                .filter(Objects::nonNull).filter(p -> !p.isEmpty()).flatMap(p -> Arrays.stream(p.split("-")))
                .map(Long::valueOf).collect(Collectors.toSet());
        //组装数据
        return children.stream().filter(s -> spaceIdSet.contains(s.getId())).map(s -> {
            SpaceViewModel spaceModel = new SpaceViewModel();
            spaceModel.setId(s.getId());
            spaceModel.setName(s.getSpaceName());
            spaceModel.setSslcCode(s.getSslcCode());
            return spaceModel;
        }).collect(Collectors.toList());
    }

    @Override
    public List<SpaceListModel> getSpaceList(String sslcCode) {
        Long tenantId = WebFrameworkUtils.getHeaderTenantId();
        ParkSpace parkSpace = parkSpaceRepository.selectOne(Wrappers.<ParkSpace>lambdaQuery().eq(ParkSpace::getSslcCode,sslcCode));
        //下级空间信息
        List<ParkSpace> children = parkSpaceRepository.selectList(Wrappers.<ParkSpace>lambdaQuery().eq(ParkSpace::getParentSpaceId,parkSpace.getId())
                .eq(ParkSpace::getSpaceStatus,Status.enabled.getKey()).eq(ParkSpace::getDeleted,Delete.NORMAL.getKey()).eq(ParkSpace::getTenantId,tenantId));
        if(CollectionUtils.isEmpty(children)){
            return Collections.emptyList();
        }
        Map<Long,ParkSpace> spaceMap = children.stream().collect(Collectors.toMap(ParkSpace::getId,s->s,(v1,v2)->v2));
        Set<Long> spaceIds = children.stream().map(ParkSpace::getId).collect(Collectors.toSet());
        //查询空间基础信息
        List<SpaceBasicInfo> spaces = spaceBasicInfoRepository.selectList(Wrappers.<SpaceBasicInfo>lambdaQuery()
                .in(SpaceBasicInfo::getSpaceId,spaceIds).eq(SpaceBasicInfo::getDeleted,Delete.NORMAL.getKey()).eq(SpaceBasicInfo::getTenantId,tenantId));
        if(CollectionUtils.isEmpty(spaces)){
            return Collections.emptyList();
        }
        Set<Long> finalSpaceIds = spaces.stream().map(SpaceBasicInfo::getSpaceId).collect(Collectors.toSet());
        Set<String> deptIds = spaces.stream().map(SpaceBasicInfo::getDepartmentId).filter(Objects::nonNull).collect(Collectors.toSet());
        //查询各空间效果图集合
        CompletableFuture<Map<Long, List<String>>> imagesFuture = CompletableFuture.supplyAsync(() -> queryImagesMap(finalSpaceIds));
        //查询各空间设备数量集合
        CompletableFuture<Map<Long, Long>> deviceCountFuture = CompletableFuture.supplyAsync(() -> queryDeviceCountMap(finalSpaceIds, tenantId));
        //查询各空间人员密度
        CompletableFuture<Map<Long, Long>> stationCountFuture = CompletableFuture.supplyAsync(() -> queryStationCountMap(finalSpaceIds, tenantId));
        //查询各空间使用部门信息
//        CompletableFuture<Map<String, String>> deptNameFuture = CompletableFuture.supplyAsync(() -> queryDepartmentNames(deptIds));
        //查询各空间报事报修及告警的工单id集合
        CompletableFuture<Map<Long, List<Long>>> workOrderFuture = CompletableFuture.supplyAsync(() -> queryWorkOrderIds(finalSpaceIds,tenantId));
        // 3. 等待所有异步任务完成
//        CompletableFuture.allOf(imagesFuture, deviceCountFuture, stationCountFuture, deptNameFuture).join();
        CompletableFuture.allOf(imagesFuture, deviceCountFuture, stationCountFuture).join();
        Map<Long, List<String>> imagesMap = imagesFuture.join();
        Map<Long, Long> deviceCountMap = deviceCountFuture.join();
        Map<Long, Long> stationCountMap = stationCountFuture.join();
//        Map<String, String> deptNameMap = deptNameFuture.join();
        Map<Long, List<Long>> workOrderMap = workOrderFuture.join();
        // 4. 组装返回结果
        Random random = new Random();
        return spaces.stream().map(s -> {
                    // 人员密度：类型1取工位人数，类型2随机生成
                    Long density = null;
                    if (s.getType() == 1) {
                        density = stationCountMap.getOrDefault(s.getSpaceId(), 0L);
                    } else if(s.getType() == 2){
                        density = RandomUtils.nextLong(1,4);
                    }

                    // 随机施工ID和危化品ID（演示：给一半的空间随机加1~3个ID）
                    List<Long> constructionIds = random.nextBoolean() ? randomLongs(random, 1, 3) : Collections.emptyList();
                    List<Long> hazardousIds = random.nextBoolean() ? randomLongs(random, 1, 3) : Collections.emptyList();

                    return SpaceListModel.builder().spaceId(s.getSpaceId()).type(s.getType())
                            .spaceName(spaceMap.get(s.getSpaceId()).getSpaceName()).area(s.getArea())
                            .sslcCode(spaceMap.get(s.getSpaceId()).getSslcCode())
                            .spaceImages(imagesMap.getOrDefault(s.getSpaceId(), Collections.emptyList()))
                            .useDept(s.getDepartmentName())
//                            .useDept(s.getDepartmentId() != null ? deptNameMap.get(s.getDepartmentId()) : null)
                            .deviceCount(deviceCountMap.getOrDefault(s.getSpaceId(), 0L).intValue())
                            .workOrderIds(workOrderMap.getOrDefault(s.getSpaceId(),Collections.emptyList()))
                            .personDensity(density).constructionIds(constructionIds)
                            .hazardousIds(hazardousIds).build();
                }).collect(Collectors.toList());
    }

    @Override
    public List<ManagerModel> getManagerList(String sslcCode) {
        ParkSpace parkSpace = parkSpaceRepository.selectOne(Wrappers.<ParkSpace>lambdaQuery().eq(ParkSpace::getSslcCode,sslcCode));
        AssertUtils.notNull(parkSpace, SystemResultCode.RESULT_DATA_NONE.message());
        List<SpaceManager> list = spaceManagerRepository.selectList(Wrappers.<SpaceManager>lambdaQuery().eq(SpaceManager::getSpaceId, parkSpace.getId())
                .eq(SpaceManager::getDeleted,Delete.NORMAL.getKey()).eq(SpaceManager::getTenantId, WebFrameworkUtils.getHeaderTenantId()));
        if(CollectionUtils.isEmpty(list)){
            return Collections.emptyList();
        }
        return list.stream().map(s->{
            ManagerModel model = BeanUtils.convertTo(s, ManagerModel::new);
            model.setStaffId(s.getStaffid());
            model.setStaffName(s.getName());
            return model;
        }).collect(Collectors.toList());
    }

    @Override
    public List<StationModel> getStationList(String sslcCode, Long spaceId) {
        StationListParam param = new StationListParam();
        param.setSpaceId(getSpaceIdOfSslcCode(sslcCode,spaceId));
        List<com.cgnpc.bbxpark.space.dto.model.StationModel> stations = stationService.list(param);
        return stations.stream().map(s->{
           StationModel model = BeanUtils.convertTo(s,StationModel::new);
           model.setStaffId(s.getStaffid());
           model.setAssignerStaffId(s.getAssignerStaffid());
           return model;
        }).collect(Collectors.toList());
    }

    @Override
    public IPage<StationModel> getStationPage(StationPageParam param) {
        param.setSpaceId(getSpaceIdOfSslcCode(param.getSslcCode(),param.getSpaceId()));
        IPage<com.cgnpc.bbxpark.space.dto.model.StationModel> stationPage = stationService.page(BeanUtils.convertTo(param, com.cgnpc.bbxpark.space.dto.param.StationPageParam::new));
        if(CollectionUtils.isEmpty(stationPage.getRecords())){
            return ConvertUtil.pageEmptyConvert(param.getCurrent(),param.getSize());
        }
        List<StationModel> list = stationPage.getRecords().stream().map(s->{
            StationModel model = BeanUtils.convertTo(s,StationModel::new);
            model.setStaffId(s.getStaffid());
            model.setAssignerStaffId(s.getAssignerStaffid());
            return model;
        }).collect(Collectors.toList());
        return ConvertUtil.pageConvert(stationPage,list);
    }

    @Override
    public List<SpaceImageModel> getImageList(String sslcCode, Long spaceId) {
        spaceId = getSpaceIdOfSslcCode(sslcCode,spaceId);
        List<SpaceImage> images = spaceImageRepository.selectList(Wrappers.<SpaceImage>lambdaQuery().eq(SpaceImage::getSpaceId, spaceId)
                .eq(SpaceImage::getDeleted,Delete.NORMAL.getKey()).eq(SpaceImage::getTenantId, WebFrameworkUtils.getHeaderTenantId()));
        //数据组装
        return images.stream().map(item -> {
            SpaceImageModel model = BeanUtils.convertTo(item, SpaceImageModel::new);
            //图片集合
            model.setImageList(JsonUtil.convertJsonArrStrToList(item.getImages()));
            //更新人信息
            model.setStaffId(item.getUpdatorId());
            model.setUserName(item.getUpdateBy());
            return model;
        }).collect(Collectors.toList());
    }

    @Override
    public IPage<DeviceListModel> getDevicePage(DevicePageParam param) {
        IocDevicePageParam deviceParam = BeanUtils.convertTo(param,IocDevicePageParam::new);
        deviceParam.setSpaceId(getSpaceIdOfSslcCode(param.getSslcCode(),param.getSpaceId()));
        IPage<IocDeviceModel> devicePage = iocDeviceService.pageDevice(deviceParam);
        if(CollectionUtils.isEmpty(devicePage.getRecords())){
            return ConvertUtil.pageEmptyConvert(param.getCurrent(),param.getSize());
        }
        List<DeviceListModel> list = BeanUtils.convertListTo(devicePage.getRecords(),DeviceListModel::new);
        return ConvertUtil.pageConvert(devicePage,list);
    }

    @Override
    public SpaceDetailModel detail(String sslcCode, Long spaceId) {
        spaceId = getSpaceIdOfSslcCode(sslcCode,spaceId);
        SpaceBasicInfoModel spaceModel = spaceBasicInfoService.detail(spaceId);
        SpaceDetailModel model = BeanUtils.convertTo(spaceModel,SpaceDetailModel::new);
        //已分配工位
        Integer allocStation = stationRepository.selectCount(Wrappers.<Station>lambdaQuery().eq(Station::getSpaceId,spaceId)
                .eq(Station::getTenantId, WebFrameworkUtils.getHeaderTenantId()).eq(Station::getDeleted, Delete.NORMAL.getKey()));
        model.setAllocatedStations(allocStation);
        model.setFreeStations(model.getCapacity() - allocStation);
        model.setManagerStaffId(spaceModel.getManagerStaffid());
        return model;
    }

    @Override
    public List<KeywordSearchModel> keywordSearch(String keyword) {
        if(StringUtils.isEmpty(keyword)){
            return Collections.emptyList();
        }
        //空间检索
        List<KeywordSearchModel> spaces = searchSpace(keyword);
        if(spaces.size() >= 30){
            return spaces;
        }
        Long tenantId = WebFrameworkUtils.getHeaderTenantId();
        //员工号工位空间检索
        CompletableFuture<List<KeywordSearchModel>> userFuture = CompletableFuture.supplyAsync(() -> searchUsers(keyword, tenantId));
        //工单位置空间检索
        CompletableFuture<List<KeywordSearchModel>> workOrderFuture = CompletableFuture.supplyAsync(() -> searchWorkOrder(keyword, tenantId));
        //设备位置空间检索
        CompletableFuture<List<KeywordSearchModel>> deviceFuture = CompletableFuture.supplyAsync(() -> searchDevice(keyword, tenantId));
        //等待查询结果
        List<KeywordSearchModel> users = userFuture.join();
        List<KeywordSearchModel> workOrders = workOrderFuture.join();
        List<KeywordSearchModel> devices = deviceFuture.join();
        // 分离三个维度的「有空间ID」和「无空间ID」数据
        List<List<KeywordSearchModel>> withSpace = Arrays.asList(
                users.stream().filter(m -> m.getSpaceId() != null).collect(Collectors.toList()),
                workOrders.stream().filter(m -> m.getSpaceId() != null).collect(Collectors.toList()),
                devices.stream().filter(m -> m.getSpaceId() != null).collect(Collectors.toList())
        );
        List<List<KeywordSearchModel>> withoutSpace = Arrays.asList(
                users.stream().filter(m -> m.getSpaceId() == null).collect(Collectors.toList()),
                workOrders.stream().filter(m -> m.getSpaceId() == null).collect(Collectors.toList()),
                devices.stream().filter(m -> m.getSpaceId() == null).collect(Collectors.toList())
        );
        int remaining = 30 - spaces.size();
        // 从三个维度中均分选取（优先有空间ID的）
        List<KeywordSearchModel> otherResults = allocateEvenly(withSpace, withoutSpace, remaining);
        //批量查询空间模型id
        Set<Long> spaceIds = otherResults.stream().map(KeywordSearchModel::getSpaceId).filter(Objects::nonNull).collect(Collectors.toSet());
        if(!CollectionUtils.isEmpty(spaceIds)){
            Map<Long, String> modelMap = parkSpaceRepository.selectList(Wrappers.<ParkSpace>lambdaQuery().in(ParkSpace::getId, spaceIds).select(ParkSpace::getId, ParkSpace::getSslcCode))
                    .stream().filter(s->StringUtils.isNotEmpty(s.getSslcCode())).collect(Collectors.toMap(ParkSpace::getId, ParkSpace::getSslcCode));
            otherResults.stream().filter(r -> r.getSpaceId() != null && modelMap.containsKey(r.getSpaceId()))
                    .forEach(r -> r.setSslcCode(modelMap.get(r.getSpaceId())));
        }
        //结果合并
        spaces.addAll(otherResults);
        return spaces;
    }

    @Override
    public FloorDeviceCountModel getFloorDeviceCount(String sslcCode) {
        FloorDeviceCountModel model = new FloorDeviceCountModel();
        if(ObjectUtil.isEmpty(sslcCode)){
            return model;
        }
        Long tenantId = WebFrameworkUtils.getHeaderTenantId();
        ParkSpace parkSpace = parkSpaceRepository.selectOne(Wrappers.<ParkSpace>lambdaQuery()
                .eq(ParkSpace::getSslcCode,sslcCode)
                .eq(ParkSpace::getDeleted, Delete.NORMAL.getKey())
                .eq(ObjectUtil.isNotEmpty(tenantId), ParkSpace::getTenantId,tenantId)
                .last("limit 1"));

        if(ObjectUtil.isEmpty(parkSpace)){
            return model;
        }
        model.setSpaceName(parkSpace.getSpaceName());
        List<SpaceBasicInfo> spaces = spaceBasicInfoRepository.selectList(Wrappers.<SpaceBasicInfo>lambdaQuery()
                .eq(SpaceBasicInfo::getSpaceId, parkSpace.getId())
                .eq(SpaceBasicInfo::getDeleted,Delete.NORMAL.getKey())
                .eq(ObjectUtil.isNotEmpty(tenantId), SpaceBasicInfo::getTenantId,tenantId));
        if(CollectionUtil.isNotEmpty(spaces)){
            model.setSpaceArea(spaces.get(0).getArea() == null ? 0d : spaces.get(0).getArea().doubleValue());
        }
        List<IocDevice> list = iocDeviceRepository.listBySpace(tenantId, sslcCode,null);
        if(CollectionUtil.isEmpty(list)){
            return model;
        }
        //过滤出物联网平台设备
        List<IocDevice> devices = list.stream().filter(d->d.getIotDevicePlatform() != null && d.getIotDevicePlatform() != 0).collect(Collectors.toList());
        model.setIntelligentDeviceCount(devices.size());
        model.setOfflineCount((int) devices.stream().filter(p->Status.disabled.getKey().equals(p.getIotDeviceStatus())).count());
        model.setOnlineCount(model.getIntelligentDeviceCount() - model.getOfflineCount());
        return model;
    }

    /**
     * 空间名称检索
     * @param keyword 关键词
     * @return 检索数据
     */
    private List<KeywordSearchModel> searchSpace(String keyword){
        //检索空间数据
        List<ParkSpace> spaces = parkSpaceRepository.selectList(Wrappers.<ParkSpace>lambdaQuery().like(ParkSpace::getSpaceName,keyword)
                .eq(ParkSpace::getSpaceStatus,Status.enabled.getKey()).eq(ParkSpace::getDeleted,Delete.NORMAL.getKey())
                .eq(ParkSpace::getTenantId,WebFrameworkUtils.getHeaderTenantId()).select(ParkSpace::getId,ParkSpace::getSpaceName,ParkSpace::getSslcCode).last("LIMIT 30"));
        return spaces.stream().map(s->new KeywordSearchModel(s.getId(),s.getSslcCode(),s.getSpaceName())).collect(Collectors.toList());
    }

    /**
     * 用户名称检索
     * @param keyword 关键词
     * @return 检索数据
     */
    private List<KeywordSearchModel> searchUsers(String keyword,Long tenantId){
        //1.优先查询空间工位表
        List<Station> stations = stationRepository.selectList(Wrappers.<Station>lambdaQuery().like(Station::getUserName,keyword)
                .eq(Station::getDeleted,Delete.NORMAL.getKey()).eq(Station::getTenantId,tenantId).last("LIMIT 30"));
        List<KeywordSearchModel> list = stations.stream().map(s->new KeywordSearchModel(s.getSpaceId(),s.getUserName())).collect(Collectors.toList());
        if(list.size() >= 30){
            return list;
        }
        Set<String> userNames = list.stream().map(KeywordSearchModel::getContent).collect(Collectors.toSet());
        //2.查询用户中台补充数据
        List<UserInfoModel> staffs = userApiService.getStaffsByOrgId("",1,30,keyword);
        List<KeywordSearchModel> suppleList = staffs.stream().filter(s->!userNames.contains(s.getUserName())).map(s->new KeywordSearchModel(null,s.getUserId())).collect(Collectors.toList());
        list.addAll(suppleList.subList(0,Math.min(30 - suppleList.size(),suppleList.size())));
        return list;
    }

    /**
     * 工单编号检索
     * @param keyword 关键词
     * @return 检索数据
     */
    private List<KeywordSearchModel> searchWorkOrder(String keyword,Long tenantId){
        //检索工单数据
        List<WorkOrder> workOrders = workOrderRepository.selectList(Wrappers.<WorkOrder>lambdaQuery().like(WorkOrder::getCode,keyword)
                .eq(WorkOrder::getDeleted,Delete.NORMAL.getKey()).eq(WorkOrder::getTenantId,tenantId)
                .orderByAsc(WorkOrder::getStatus).select(WorkOrder::getId,WorkOrder::getName,WorkOrder::getCode).last("LIMIT 30"));
        if(CollectionUtils.isEmpty(workOrders)){
            return Collections.emptyList();
        }
        List<Long> workOrderIds = workOrders.stream().map(WorkOrder::getId).collect(Collectors.toList());
        //查询工单位置数据
        List<WorkOrderDevice> workOrderDevices = workOrderDeviceRepository.selectList(Wrappers.<WorkOrderDevice>lambdaQuery()
                .in(WorkOrderDevice::getWorkOrderId,workOrderIds).isNotNull(WorkOrderDevice::getSpaceId).eq(WorkOrderDevice::getDeleted,Delete.NORMAL.getKey()));
        Map<Long,Long> workOrderMap = workOrderDevices.stream().collect(Collectors.toMap(WorkOrderDevice::getWorkOrderId,WorkOrderDevice::getSpaceId,(v1,v2)->v2));
        return workOrders.stream().map(w->new KeywordSearchModel(workOrderMap.getOrDefault(w.getId(),null),w.getCode())).collect(Collectors.toList());
    }

    /**
     * 设备名称筛选
     * @param keyword 关键词
     * @return 检索数据
     */
    private List<KeywordSearchModel> searchDevice(String keyword,Long tenantId){
        //检索设备数据
        List<IocDevice> devices = iocDeviceRepository.selectList(Wrappers.<IocDevice>lambdaQuery().like(IocDevice::getDeviceName,keyword)
                .eq(IocDevice::getEnableStatus,Status.enabled.getKey()).eq(IocDevice::getDeleted,Delete.NORMAL.getKey())
                .eq(IocDevice::getTenantId,tenantId).select(IocDevice::getSpaceId,IocDevice::getDeviceName).last("LIMIT 30"));
        return devices.stream().map(d->new KeywordSearchModel(d.getSpaceId(),d.getDeviceName())).collect(Collectors.toList());
    }

    /**
     * 查询各空间效果图集合
     */
    private Map<Long, List<String>> queryImagesMap(Set<Long> spaceIds) {
        List<SpaceImage> images = spaceImageRepository.selectList(Wrappers.<SpaceImage>lambdaQuery().in(SpaceImage::getSpaceId, spaceIds)
                .eq(SpaceImage::getType, 5).eq(SpaceImage::getDeleted, Delete.NORMAL.getKey()).select(SpaceImage::getSpaceId,SpaceImage::getImages));
        Map<Long,List<String>> result = new HashMap<>();
        for (SpaceImage img:images){
            result.computeIfAbsent(img.getSpaceId(),k->new ArrayList<>())
                    .addAll(JsonUtil.convertJsonArrStrToList(img.getImages()));
        }
        return result;
    }

    /**
     * 查询各空间设备数量map
     */
    private Map<Long, Long> queryDeviceCountMap(Set<Long> spaceIds, Long tenantId) {
        List<IocDevice> devices = iocDeviceRepository.selectList(Wrappers.<IocDevice>lambdaQuery().in(IocDevice::getSpaceId, spaceIds)
                .eq(IocDevice::getEnableStatus, Status.enabled.getKey()).eq(IocDevice::getDeleted, Delete.NORMAL.getKey())
                .eq(IocDevice::getTenantId, tenantId).select(IocDevice::getId,IocDevice::getSpaceId));
        return devices.stream().collect(Collectors.groupingBy(IocDevice::getSpaceId, Collectors.counting()));
    }

    /**
     * 查询各空间人员工位数量map
     */
    private Map<Long, Long> queryStationCountMap(Set<Long> spaceIds, Long tenantId) {
        List<Station> stations = stationRepository.selectList(Wrappers.<Station>lambdaQuery().in(Station::getSpaceId, spaceIds)
                .eq(Station::getDeleted, Delete.NORMAL.getKey()).eq(Station::getTenantId, tenantId)
                .select(Station::getId,Station::getSpaceId));
        return stations.stream().collect(Collectors.groupingBy(Station::getSpaceId, Collectors.counting()));
    }

    /**
     * 查询各空间使用部门map
     */
    private Map<String, String> queryDepartmentNames(Set<String> deptIds) {
        List<OrgDepartmentNode> departments = departmentApiService.getOrgsByOrgIds(deptIds);
        return departments.stream().collect(Collectors.toMap(OrgDepartmentNode::getDeptNo, OrgDepartmentNode::getDeptName, (v1, v2) -> v1));
    }

    /**
     * 查询各空间工单情况
     */
    private Map<Long, List<Long>> queryWorkOrderIds(Set<Long> spaceIds,Long tenantId){
        String[] sources = {WorkOrderSourceEnum.PERSON.getCode(),WorkOrderSourceEnum.ALARM.getCode()};
        List<WorkOrder> workOrders = workOrderRepository.selectList(Wrappers.<WorkOrder>lambdaQuery()
                .ne(WorkOrder::getStatus, WorkOrderStatusEnum.COMPLETED.getCode()).in(WorkOrder::getSource,Arrays.asList(sources))
                .eq(WorkOrder::getDeleted,Delete.NORMAL.getKey()).eq(WorkOrder::getTenantId,tenantId).select(WorkOrder::getId));
        if(CollectionUtils.isEmpty(workOrders)){
            return Collections.emptyMap();
        }
        List<Long> workOrderIds = workOrders.stream().map(WorkOrder::getId).collect(Collectors.toList());
        //查询工单位置数据
        List<WorkOrderDevice> workOrderDevices = workOrderDeviceRepository.selectList(Wrappers.<WorkOrderDevice>lambdaQuery()
                .in(WorkOrderDevice::getWorkOrderId,workOrderIds).isNotNull(WorkOrderDevice::getSpaceId).eq(WorkOrderDevice::getDeleted,Delete.NORMAL.getKey()));
        return workOrderDevices.stream().collect(Collectors.groupingBy(WorkOrderDevice::getSpaceId,Collectors.mapping(WorkOrderDevice::getWorkOrderId,Collectors.toList())));
    }


    /**
     * 根据sslcCode获取空间id，当sslcCode不存在时使用spaceId
     * @param sslcCode 空间模型编码
     * @param spaceId 空间id
     * @return 空间id
     */
    private Long getSpaceIdOfSslcCode(String sslcCode,Long spaceId){
        if(StringUtils.isNotEmpty(sslcCode)){
            ParkSpace parkSpace = parkSpaceRepository.selectOne(Wrappers.<ParkSpace>lambdaQuery().eq(ParkSpace::getSslcCode,sslcCode));
            AssertUtils.notNull(parkSpace, SystemResultCode.RESULT_DATA_NONE.message());
            return parkSpace.getId();
        }
        return spaceId;
    }

    /**
     * 根据部门id集合查询部门信息map
     */
    private Map<String,String> getDepartMap(Set<String> deptIds){
        List<OrgDepartmentNode> departs = departmentApiService.getOrgsByOrgIds(deptIds);
        return departs.stream().collect(Collectors.toMap(OrgDepartmentNode::getDeptNo,OrgDepartmentNode::getDeptName,(v1,v2)->v2));
    }

    /**
     * 从三个维度中均分选取指定数量的数据，优先从有空间ID的列表中取，不足时再从无空间ID的列表中取
     * @param withSpace   三个维度的有空间ID列表（索引0:用户,1:工单,2:设备）
     * @param withoutSpace 三个维度的无空间ID列表
     * @param totalNeeded  需要选取的总数
     * @return 选取的数据列表
     */
    private List<KeywordSearchModel> allocateEvenly(List<List<KeywordSearchModel>> withSpace,
                                                    List<List<KeywordSearchModel>> withoutSpace,
                                                    int totalNeeded) {
        if (totalNeeded <= 0) return Collections.emptyList();

        // 先计算每个维度可以贡献的有空间ID的最大数量
        int[] withSize = withSpace.stream().mapToInt(List::size).toArray();
        int totalWith = IntStream.of(withSize).sum();

        List<KeywordSearchModel> result = new ArrayList<>();

        if (totalWith >= totalNeeded) {
            // 有空间ID的数据足够，按配额从每个维度中取
            int base = totalNeeded / 3;
            int remainder = totalNeeded % 3;
            for (int i = 0; i < 3; i++) {
                int take = Math.min(withSize[i], base + (i < remainder ? 1 : 0));
                if (take > 0) {
                    result.addAll(withSpace.get(i).subList(0, take));
                }
            }
        } else {
            // 有空间ID的数据不足，全部取用，剩余从无空间ID中补
            for (int i = 0; i < 3; i++) {
                result.addAll(withSpace.get(i));
            }
            int needMore = totalNeeded - totalWith;
            // 从无空间ID列表中按配额补足
            int[] withoutSize = withoutSpace.stream().mapToInt(List::size).toArray();
            int base = needMore / 3;
            int remainder = needMore % 3;
            for (int i = 0; i < 3; i++) {
                int take = Math.min(withoutSize[i], base + (i < remainder ? 1 : 0));
                if (take > 0) {
                    result.addAll(withoutSpace.get(i).subList(0, take));
                }
            }
        }
        return result;
    }

    private List<Long> randomLongs(Random random, int min, int max) {
        int count = min + random.nextInt(max - min + 1);
        List<Long> list = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            list.add((long)(random.nextInt(1000) + 1)); // 随机1~1000的ID
        }
        return list;
    }

    /**
     * 生成施工信息Mock数据
     * @param spaces 空间信息
     * @return 施工数据
     */
    private List<ConstructionModel> generateConstructionListData(List<ParkSpace> spaces) {
        List<ConstructionModel> result = new ArrayList<>();
        String[] constructionDescriptions = {"天花板维修", "水电改造", "网络布线", "设备安装"};
        String[] applicantNames = {"张三", "李四", "王五", "赵六"};

        // 确保空间信息列表不为空
        if (CollectionUtils.isEmpty(spaces)) {
            return result;
        }

        // 生成5条施工信息
        for (int i = 0; i < 6; i++) {
            ConstructionModel model = new ConstructionModel();
            // 随机选择一个空间
            ParkSpace space = spaces.get(RandomUtils.nextInt(0, spaces.size()));
            model.setId((long) (i + 1));
            model.setSpaceId(space.getId());
            model.setSslcCode(space.getSslcCode());
            model.setSpaceName(space.getSpaceName());
            model.setDescription(constructionDescriptions[RandomUtils.nextInt(0, constructionDescriptions.length)]);
            // 设置日期
            model.setStartDate(java.sql.Date.valueOf("2026-01-01"));
            model.setEndDate(java.sql.Date.valueOf("2026-01-10"));
            model.setApplicationTime(java.sql.Date.valueOf("2025-12-20"));
            model.setStaffId("EMP" + String.format("%04d", i + 1000));
            model.setStaffName(applicantNames[RandomUtils.nextInt(0, applicantNames.length)]);
            result.add(model);
        }

        return result;
    }

    /**
     * 生成物资存放Mock数据
     * @param spaces 空间数据
     * @return 物资存放数据
     */
    private List<StorageModel> generateStorageListData(List<ParkSpace> spaces) {
        List<StorageModel> result = new ArrayList<>();
        String[] materialTypes = {"钢材", "水泥", "电线", "管道", "灯具"};
        String[] units = {"吨", "袋", "米", "根", "个"};
        String[] staffNames = {"张三", "李四", "王五", "赵六"};

        // 确保空间信息列表不为空
        if (CollectionUtils.isEmpty(spaces)) {
            return result;
        }

        // 生成5条物资存放信息
        for (int i = 0; i < 5; i++) {
            StorageModel model = new StorageModel();
            // 随机选择一个空间
            ParkSpace space = spaces.get(RandomUtils.nextInt(0, spaces.size()));
            int count = RandomUtils.nextInt(0, materialTypes.length);
            model.setId((long) (i + 1));
            model.setSpaceId(space.getId());
            model.setSslcCode(space.getSslcCode());
            model.setSpaceName(space.getSpaceName());
            model.setType(materialTypes[count]);
            model.setCount(RandomUtils.nextInt(10, 100));
            model.setUnit(units[count]);
            model.setHazardous(RandomUtils.nextInt(0, 2)); // 0或1
            // 设置日期
            model.setStartDate(java.sql.Date.valueOf("2026-01-01"));
            model.setEndDate(java.sql.Date.valueOf("2026-06-30"));
            model.setApplicationTime(java.sql.Date.valueOf("2025-12-20"));
            model.setStaffId("EMP" + String.format("%04d", i + 1000));
            model.setStaffName(staffNames[RandomUtils.nextInt(0, staffNames.length)]);
            result.add(model);
        }

        return result;
    }

    /**
     * 生成施工信息Mock数据
     */
    private ConstructionDetailModel generateConstructionDetailData(Long id) {
        ConstructionDetailModel model = new ConstructionDetailModel();
        model.setSpaceName("苍南基地/BBX/东区/2F/E207");
        model.setStartDate(java.sql.Date.valueOf("2026-12-23"));
        model.setEndDate(java.sql.Date.valueOf("2026-12-31"));
        model.setDescription("填写的施工说明");
        model.setStaffId("P12345");
        model.setStaffName("张三");
        model.setApplicationTime(java.sql.Date.valueOf("2026-06-20"));
        return model;
    }

    /**
     * 生成物资存放数据
     */
    private StorageDetailModel generateStorageDetailData(Long id) {
        StorageDetailModel model = new StorageDetailModel();
        model.setSpaceName("苍南基地/BBX/东区/2F/E207");
        model.setCount(30);
        model.setUnit("个");
        model.setStartDate(java.sql.Date.valueOf("2026-03-12"));
        model.setEndDate(java.sql.Date.valueOf("2026-06-20"));
        model.setStaffId("P12345");
        model.setStaffName("张三");
        model.setApplicationTime(java.sql.Date.valueOf("2026-06-20"));
        model.setHazardous(1); // 1表示是危化品
        return model;
    }

    /**
     * 从BBX下的东区和西区随机取房间层级的空间
     * @return 空间集合
     */
    public List<ParkSpace> findRandomRoomsFromFlat() {
        List<ParkSpace> spaceList = parkSpaceRepository.selectList(Wrappers.<ParkSpace>lambdaQuery()
                .eq(ParkSpace::getSpaceStatus, Status.enabled.getKey()).eq(ParkSpace::getDeleted,Delete.NORMAL.getKey())
                .eq(ParkSpace::getTenantId,WebFrameworkUtils.getHeaderTenantId())
                .select(ParkSpace::getId,ParkSpace::getSpaceName,ParkSpace::getParentSpaceId,ParkSpace::getSslcCode));
        // 1. 按 parentSpaceId 分组，方便快速获取子节点
        Map<Long, List<ParkSpace>> childrenMap = spaceList.stream()
                .filter(s -> s.getParentSpaceId() != null)
                .collect(Collectors.groupingBy(ParkSpace::getParentSpaceId));

        // 2. 找到 BBX 楼栋
        ParkSpace building = spaceList.stream()
                .filter(s -> "BBX".equals(s.getSpaceName()))
                .findFirst().orElse(null);
        if (building == null) return Collections.emptyList();

        // 3. 找到东区和西区
        List<ParkSpace> buildingChildren = childrenMap.getOrDefault(building.getId(), Collections.emptyList());
        ParkSpace eastZone = findChildByName(buildingChildren, "东区");
        ParkSpace westZone = findChildByName(buildingChildren, "西区");

        // 4. 收集结果
        List<ParkSpace> result = new ArrayList<>();
        Random random = new Random();

        for (ParkSpace zone : Arrays.asList(eastZone, westZone)) {
            if (zone == null) continue;
            List<ParkSpace> floors = childrenMap.getOrDefault(zone.getId(), Collections.emptyList());
            if (floors.isEmpty()) continue;

            // 随机取 3~5 个楼层
            int floorCount = Math.min(floors.size(), 3 + random.nextInt(3));
            List<ParkSpace> selectedFloors = randomSelect(floors, floorCount, random);

            for (ParkSpace floor : selectedFloors) {
                List<ParkSpace> rooms = childrenMap.getOrDefault(floor.getId(), Collections.emptyList());
                if (rooms.isEmpty()) continue;
                // 每个楼层随机取 3 个房间
                int roomCount = Math.min(rooms.size(), 3);
                List<ParkSpace> selectedRooms = randomSelect(rooms, roomCount, random);
                result.addAll(selectedRooms);
            }
        }
        return result;
    }

    // 辅助方法：在子节点列表中按名称查找
    private ParkSpace findChildByName(List<ParkSpace> children, String name) {
        return children.stream()
                .filter(c -> name.equals(c.getSpaceName()))
                .findFirst()
                .orElse(null);
    }

    // 辅助方法：随机选取 count 个元素
    private List<ParkSpace> randomSelect(List<ParkSpace> list, int count, Random random) {
        if (count <= 0 || list.isEmpty()) return Collections.emptyList();
        if (count >= list.size()) return new ArrayList<>(list);
        List<ParkSpace> shuffled = new ArrayList<>(list);
        Collections.shuffle(shuffled, random);
        return shuffled.subList(0, count);
    }
}

