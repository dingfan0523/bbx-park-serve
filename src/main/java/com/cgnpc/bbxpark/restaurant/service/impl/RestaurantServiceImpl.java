
package com.cgnpc.bbxpark.restaurant.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cgnpc.bbxpark.common.constant.Constants;
import com.cgnpc.bbxpark.common.constant.SystemResultCode;
import com.cgnpc.bbxpark.common.enums.Status;
import com.cgnpc.bbxpark.common.exception.GenericException;
import com.cgnpc.bbxpark.common.utils.*;
import com.cgnpc.bbxpark.restaurant.domain.Restaurant;
import com.cgnpc.bbxpark.restaurant.domain.RestaurantSpace;
import com.cgnpc.bbxpark.restaurant.domain.RestaurantTime;
import com.cgnpc.bbxpark.restaurant.dto.model.RestaurantModel;
import com.cgnpc.bbxpark.restaurant.dto.model.RestaurantSpaceModel;
import com.cgnpc.bbxpark.restaurant.dto.model.RestaurantTimeModel;
import com.cgnpc.bbxpark.restaurant.dto.param.*;
import com.cgnpc.bbxpark.restaurant.mapper.RestaurantRepository;
import com.cgnpc.bbxpark.restaurant.service.IMealLineService;
import com.cgnpc.bbxpark.restaurant.service.IRestaurantService;
import com.cgnpc.bbxpark.restaurant.service.IRestaurantSpaceService;
import com.cgnpc.bbxpark.restaurant.service.IRestaurantTimeService;
import com.cgnpc.bbxpark.settings.dto.model.ConfigInfoModel;
import com.cgnpc.bbxpark.settings.service.IConfigInfoService;
import com.cgnpc.bbxpark.settings.service.impl.DictServiceImpl;
import com.cgnpc.bbxpark.space.domain.ParkSpace;
import com.cgnpc.bbxpark.space.domain.TenantInfo;
import com.cgnpc.bbxpark.space.dto.model.ParkSpaceFullModel;
import com.cgnpc.bbxpark.space.service.IParkSpaceService;
import com.cgnpc.bbxpark.space.service.ITenantInfoService;
import com.cgnpc.bbxpark.space.service.IUserSpaceService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


@Service
public class RestaurantServiceImpl extends ServiceImpl<RestaurantRepository, Restaurant> implements IRestaurantService {


    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private IRestaurantSpaceService restaurantSpaceService;
    @Autowired
    private IRestaurantTimeService restaurantTimeService;
    @Autowired
    private IParkSpaceService parkSpaceService;
    @Autowired
    private IUserSpaceService userSpaceService;
    @Autowired
    private IMealLineService mealLineService;
    @Autowired
    private IConfigInfoService configInfoService;
    @Autowired
    private ITenantInfoService tenantInfoService;
    @Autowired
    private DictServiceImpl dictService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean add(RestaurantParam param) {

        List<RestaurantTimeParam> timeList = param.getTimeList();
        List<RestaurantSpaceParam> spaceList = param.getSpaceList();

        validate(param, timeList, spaceList);

        Restaurant restaurant = BeanUtils.convertTo(param, Restaurant::new);
        restaurant.setStatus(1);
        restaurant.setTags(JsonUtil.convertListToJsonStr(param.getTags()));
        restaurantRepository.insert(restaurant);

        buildRestaurantSpace(restaurant, timeList, spaceList);

        List<RestaurantSpace> spaceEntities = BeanUtils.convertListTo(spaceList, RestaurantSpace::new);
        List<RestaurantTime> timeEntities = BeanUtils.convertListTo(timeList, RestaurantTime::new);
        restaurantSpaceService.saveBatch(spaceEntities);
        restaurantTimeService.saveBatch(timeEntities);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean edit(RestaurantParam param) {
        List<RestaurantTimeParam> timeList = param.getTimeList();
        List<RestaurantSpaceParam> spaceList = param.getSpaceList();

        validate(param, timeList, spaceList);

        Restaurant restaurant = BeanUtils.convertTo(param, Restaurant::new);
        restaurant.setTags(JsonUtil.convertListToJsonStr(param.getTags()));
        restaurantRepository.updateById(restaurant);

        buildRestaurantSpace(restaurant, timeList, spaceList);
        List<RestaurantSpace> oldSpaceList = restaurantSpaceService.list(new LambdaQueryWrapper<RestaurantSpace>().eq(RestaurantSpace::getRestaurantId, restaurant.getId()));

        restaurantSpaceService.remove(new LambdaQueryWrapper<RestaurantSpace>().eq(RestaurantSpace::getRestaurantId, restaurant.getId()));
        restaurantTimeService.remove(new LambdaQueryWrapper<RestaurantTime>().eq(RestaurantTime::getRestaurantId, restaurant.getId()));

        List<RestaurantSpace> spaceEntities = BeanUtils.convertListTo(spaceList, RestaurantSpace::new);
        List<RestaurantTime> timeEntities = BeanUtils.convertListTo(timeList, RestaurantTime::new);
        spaceEntities.forEach(e -> {
            oldSpaceList.stream().filter(old -> old.getSpaceId().equals(e.getSpaceId()) && old.getRestaurantId().equals(param.getId())).findFirst().ifPresent(space -> BeanUtils.copyProperties(space, e));
            e.setId(null);
        });
        restaurantSpaceService.saveBatch(spaceEntities);
        restaurantTimeService.saveBatch(timeEntities);
        return true;
    }

    @Override
    public IPage<RestaurantModel> pageRestaurantModel(RestaurantPageParam param) {
        IPage<Restaurant> page = new Page<>(param.getCurrent(), param.getSize());

        IPage<Restaurant> pageResult = this.page(page, handlePublicQuery(param));
        List<RestaurantModel> list = pageResult.getRecords().stream().map(r->{
            RestaurantModel model = BeanUtils.convertTo(r,RestaurantModel::new);
            model.setTags(JsonUtil.convertJsonArrStrToList(r.getTags()));
            return model;
        }).collect(Collectors.toList());
        IPage<RestaurantModel> pagedConvert = ConvertUtil.pageConvert(pageResult,list);
        buildRestaurant(pagedConvert.getRecords());
        return pagedConvert;
    }

    @Override
    public IPage<RestaurantModel> pageAppRestaurantModel(RestaurantPageParam param) {
        IPage<Restaurant> page = new Page<>(param.getCurrent(), param.getSize());
        IPage<Restaurant> pageResult = this.page(page, handlePublicQuery(param));
        List<RestaurantModel> list = pageResult.getRecords().stream().map(r->{
            RestaurantModel model = BeanUtils.convertTo(r,RestaurantModel::new);
            model.setTags(JsonUtil.convertJsonArrStrToList(r.getTags()));
            return model;
        }).collect(Collectors.toList());
        IPage<RestaurantModel> pagedConvert = ConvertUtil.pageConvert(pageResult,list);
        buildRestaurant(pagedConvert.getRecords());
        return pagedConvert;
    }

    @Override
    public List<RestaurantModel> listAppRestaurantModel(RestaurantListParam param) {
        RestaurantPageParam pageParam = BeanUtils.convertTo(param, RestaurantPageParam::new);
        List<Restaurant> list = this.list(handlePublicQuery(pageParam));
        List<RestaurantModel> resultList = list.stream().map(r->{
            RestaurantModel model = BeanUtils.convertTo(r,RestaurantModel::new);
            model.setTags(JsonUtil.convertJsonArrStrToList(r.getTags()));
            return model;
        }).collect(Collectors.toList());
        buildRestaurant(resultList);
        return resultList;
    }

    @Override
    public List<RestaurantModel> listRestaurantModel(RestaurantListParam param) {
        RestaurantPageParam pageParam = BeanUtils.convertTo(param, RestaurantPageParam::new);
        List<Restaurant> list = this.list(handlePublicQuery(pageParam));
        List<RestaurantModel> resultList = list.stream().map(r->{
            RestaurantModel model = BeanUtils.convertTo(r,RestaurantModel::new);
            model.setTags(JsonUtil.convertJsonArrStrToList(r.getTags()));
            return model;
        }).collect(Collectors.toList());
        buildRestaurant(resultList);
        return resultList;
    }

    @Override
    public RestaurantModel detail(Long id) {
        Restaurant restaurant = this.getById(id);
        AssertUtils.notNull(restaurant, SystemResultCode.RESULT_DATA_NONE.message());
        RestaurantModel model = BeanUtils.convertTo(restaurant, RestaurantModel::new);
        model.setTags(JsonUtil.convertJsonArrStrToList(restaurant.getTags()));
        List<RestaurantTime> timeList = restaurantTimeService.list(new LambdaQueryWrapper<RestaurantTime>().eq(RestaurantTime::getRestaurantId, id));
        model.setRestaurantTimeModelList(BeanUtils.convertListTo(timeList, RestaurantTimeModel::new));
        if (CollectionUtils.isNotEmpty(timeList)) {
            Date now = DateUtils.currentDate();
            boolean runState = isRange(timeList, now);
            model.setRestaurantTimeModelList(BeanUtils.convertListTo(timeList, RestaurantTimeModel::new));
            model.setRunState(runState);
        }
        //空间
        List<RestaurantSpace> restaurantSpaceList = restaurantSpaceService.list(new LambdaQueryWrapper<RestaurantSpace>()
                .eq(RestaurantSpace::getRestaurantId, id));
        List<Long> spaceIdList = restaurantSpaceList.stream().map(RestaurantSpace::getSpaceId).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(spaceIdList)) {
            Map<Long, ParkSpaceFullModel> spaceFullMap = parkSpaceService.findFullSpaceMap(spaceIdList, WebFrameworkUtils.getHeaderTenantId());
            if (CollectionUtils.isNotEmpty(spaceFullMap)) {
                List<ParkSpaceFullModel> spaceList1 = restaurantSpaceList.stream().map(f -> spaceFullMap.get(f.getSpaceId())).filter(Objects::nonNull).collect(Collectors.toList());

                List<String> idFullPathList = spaceList1.stream().map(ParkSpaceFullModel::getIdFullPath).collect(Collectors.toList());
                List<Long> ids = idFullPathList.stream()
                        .flatMap(idFullPath -> Arrays.stream(idFullPath.split("-"))) // 分割字符串
                        .map(Long::valueOf) // 将分割后的字符串转换为 Long
                        .collect(Collectors.toList()); // 收集结果到 List<Long>
                if (!ids.isEmpty()) {
                    Collection<ParkSpace> spaceList = parkSpaceService.listByIds(ids);
                    Map<Long, ParkSpace> spaceMap = spaceList.stream().collect(Collectors.toMap(ParkSpace::getId, Function.identity()));
                    List<String> mergedPaths = mergePaths(idFullPathList);

                    String pathLabel = getSpaceFullPath(mergedPaths, spaceMap);

                    List<RestaurantSpaceModel> restaurantSpaceModelList = BeanUtils.convertListTo(spaceList1, RestaurantSpaceModel::new);
                    restaurantSpaceModelList.forEach(k -> {
                        k.setSpaceId(k.getId());
                        k.setRestaurantId(id);
                    });
                    model.setSpaceLabel(pathLabel);
                    model.setSpaceList(restaurantSpaceModelList);
                }
            }
        }
        return model;
    }

    @Override
    public Boolean notificationSave(RestaurantNotificationParam param) {
        Restaurant restaurant = this.getById(param.getId());
        AssertUtils.notNull(restaurant, "餐厅不存在");
        restaurant.setNotification(param.getNotification());
        return this.updateById(restaurant);
    }

    @Override
    public Boolean enable(Long id) {
        Restaurant restaurant = this.getById(id);
        AssertUtils.notNull(restaurant, "餐厅不存在");
        restaurant.setStatus(Status.enabled.getKey());
        return this.updateById(restaurant);
    }

    @Override
    public Boolean disable(Long id) {
        Restaurant restaurant = this.getById(id);
        AssertUtils.notNull(restaurant, "餐厅不存在");
        restaurant.setStatus(Status.disabled.getKey());
        return this.updateById(restaurant);
    }

    /**
     * 删除餐厅
     * 如果
     *
     * @param id
     * @return
     */
    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public Boolean removeRestaurantById(Long id) {
        Restaurant restaurant = this.getById(id);
        AssertUtils.notNull(restaurant, "餐厅不存在");
        AssertUtils.state(Constants.STATUS_ENABLED != restaurant.getStatus(), "餐厅已启用，不能删除");
        restaurantTimeService.remove(new LambdaQueryWrapper<RestaurantTime>().eq(RestaurantTime::getRestaurantId, id));
        restaurantSpaceService.remove(new LambdaQueryWrapper<RestaurantSpace>().eq(RestaurantSpace::getRestaurantId, id));
        mealLineService.removeByRestaurantId(id);
        return this.removeById(id);
    }

    @Override
    public List<RestaurantSpaceModel> imgList(RestaurantSpaceParam param) {
        List<RestaurantSpace> spaceList = restaurantSpaceService.list(new LambdaQueryWrapper<RestaurantSpace>()
                .eq(param.getRestaurantId() != null, RestaurantSpace::getRestaurantId, param.getRestaurantId()));
        List<RestaurantSpaceModel> list = BeanUtils.convertListTo(spaceList, RestaurantSpaceModel::new);
        List<Long> spaceIdList = list.stream().map(RestaurantSpaceModel::getSpaceId).collect(Collectors.toList());
        Map<Long, ParkSpaceFullModel> spaceFullModelMap = parkSpaceService.findFullSpaceMap(spaceIdList, WebFrameworkUtils.getHeaderTenantId());
        list.forEach(space -> {
            ParkSpaceFullModel fullModel = spaceFullModelMap.get(space.getSpaceId());
            if (fullModel != null) {
                space.setFullPath(fullModel.getFullPath());
            }
        });
        return list;
    }

    @Override
    public ConfigInfoModel configNotic(ConfigInfoModel param) {
        String code = "RestaurantNotic_" + param.getCode();
        return configInfoService.getByCodeDetail(code);
    }


    @Override
    public ConfigInfoModel configNotice(Long tenantId) {
        TenantInfo tenantInfo = tenantInfoService.getById(tenantId);
        AssertUtils.notNull(tenantInfo,"未获取到园信息");
        String code = "RestaurantNotic_" + tenantInfo.getCode();
        return configInfoService.getByCodeDetail(code);
    }


    private void buildRestaurant(List<RestaurantModel> restaurantList) {
        if (CollectionUtils.isEmpty(restaurantList)) {
            return;
        }
        List<Long> idList = restaurantList.stream().map(RestaurantModel::getId).collect(Collectors.toList());
        // 餐厅相关空间集合
        List<RestaurantSpace> restaurantSpaceList = restaurantSpaceService.list(new LambdaQueryWrapper<RestaurantSpace>().in(RestaurantSpace::getRestaurantId, idList));
        List<Long> spaceIdList = restaurantSpaceList.stream().map(RestaurantSpace::getSpaceId).collect(Collectors.toList());

        if (CollectionUtils.isNotEmpty(spaceIdList)) {
            Map<Long, ParkSpaceFullModel> spaceFullMapList = parkSpaceService.findFullSpaceMap(spaceIdList, WebFrameworkUtils.getHeaderTenantId());

            if (CollectionUtils.isNotEmpty(spaceFullMapList)) {
                restaurantList.forEach(e -> {
                    List<RestaurantSpace> spaceEntityList = restaurantSpaceList.stream().filter(f -> f.getRestaurantId().equals(e.getId())).collect(Collectors.toList());
                    if (spaceEntityList.isEmpty()) {
                        return;
                    }
                    List<ParkSpaceFullModel> spaceList1 = spaceEntityList.stream().map(f -> spaceFullMapList.get(f.getSpaceId())).filter(Objects::nonNull).collect(Collectors.toList());

                    List<String> idFullPathList = spaceList1.stream().map(ParkSpaceFullModel::getIdFullPath).collect(Collectors.toList());


                    List<Long> ids = idFullPathList.stream()
                            .flatMap(idFullPath -> Arrays.stream(idFullPath.split("-"))) // 分割字符串
                            .map(Long::valueOf) // 将分割后的字符串转换为 Long
                            .collect(Collectors.toList()); // 收集结果到 List<Long>
                    if (ids.isEmpty()) {
                        return;
                    }
                    Collection<ParkSpace> spaceList = parkSpaceService.listByIds(ids);

                    //.filter(sp->sp.getParentSpaceId()!=0L)
                    Map<Long, ParkSpace> spaceMap = spaceList.stream().collect(Collectors.toMap(ParkSpace::getId, Function.identity()));
                    List<String> mergedPaths = mergePaths(idFullPathList);

                    String pathLabel = getSpaceFullPath(mergedPaths, spaceMap);


//                    String spaceLabel = spaceList1.stream().map(ParkSpaceFullModel::getFullPath).collect(Collectors.joining(","));


                    List<RestaurantSpaceModel> restaurantSpaceModelList = BeanUtils.convertListTo(spaceList1, RestaurantSpaceModel::new);
                    restaurantSpaceModelList.forEach(k -> k.setRestaurantId(e.getId()));
                    e.setSpaceList(restaurantSpaceModelList);
                    e.setSpaceLabel(pathLabel);
                });
            }
        }

        List<RestaurantTime> timeList = restaurantTimeService.list(new LambdaQueryWrapper<RestaurantTime>().in(RestaurantTime::getRestaurantId, idList));
        if (CollectionUtils.isNotEmpty(timeList)) {
            Map<Long, List<RestaurantTime>> timeMap = timeList.stream().collect(Collectors.groupingBy(RestaurantTime::getRestaurantId));
            Date now = DateUtils.currentDate();
            restaurantList.forEach(e -> {
                List<RestaurantTime> timeEntityList = timeMap.get(e.getId());
                boolean runState = isRange(timeEntityList, now);
                e.setRestaurantTimeModelList(BeanUtils.convertListTo(timeEntityList, RestaurantTimeModel::new));
                e.setRunState(runState);
            });
        }
    }

    /**
     * 空间路径合并
     *
     * @param mergedPaths
     * @param spaceMap
     * @return
     */
    private String getSpaceFullPath(List<String> mergedPaths, Map<Long, ParkSpace> spaceMap) {
        if (CollectionUtils.isEmpty(mergedPaths) || CollectionUtils.isEmpty(spaceMap)) {
            return "";
        }
        String pathLabel = "";
        for (String path : mergedPaths) {
            String[] ph = path.split("/");
            for (String p : ph) {
                String[] ps = p.split(",");
                String childPathLabel = "";
                if (ps.length > 1) {
                    for (String s : ps) {
                        if (StringUtils.isEmpty(s)) {
                            continue;
                        }
                        ParkSpace space = spaceMap.get(Long.valueOf(s));
                        if (space != null) {
                            childPathLabel += space.getSpaceName() + ",";
                        }
                    }

                    if (StringUtils.isNotEmpty(childPathLabel)) {
                        childPathLabel = childPathLabel.substring(0, childPathLabel.lastIndexOf(","));
                        pathLabel += childPathLabel;
                    }
                } else {
                    if (ps.length == 0 || StringUtils.isEmpty(p)) {
                        continue;
                    }
                    ParkSpace space = spaceMap.get(Long.valueOf(p));
                    if (space != null) {
                        pathLabel += space.getSpaceName() + "/";
                    }
                }
            }
            if (StringUtils.isNotEmpty(pathLabel) && pathLabel.substring(pathLabel.length() - 1, pathLabel.length()).equals("/")) {
                pathLabel = pathLabel.substring(0, pathLabel.lastIndexOf("/"));
            }
            pathLabel = pathLabel + "；";
        }
        pathLabel = pathLabel.substring(0, pathLabel.lastIndexOf("；"));

        return pathLabel;
    }

    /**
     * 合并路径
     *
     * @param paths
     * @return
     */
    public static List<String> mergePaths(List<String> paths) {
        Map<String, Set<String>> pathMap = new HashMap<>();

        for (String path : paths) {
            String[] nodes = path.split("-");
            String pathKey = String.join(",", Arrays.copyOf(nodes, nodes.length - 1)); // 使用除最后一个节点外的部分作为路径键

            if (pathMap.containsKey(pathKey)) {
                pathMap.get(pathKey).add(nodes[nodes.length - 1]); // 添加最后一个节点到集合中
            } else {
                Set<String> nodeSet = new HashSet<>();
                nodeSet.add(nodes[nodes.length - 1]);
                pathMap.put(pathKey, nodeSet); // 创建一个新的集合
            }
        }
        List<String> mergedPaths = new ArrayList<>();
        for (Map.Entry<String, Set<String>> entry : pathMap.entrySet()) {
            String pathKey = entry.getKey();
            Set<String> nodeSet = entry.getValue();
            String mergedPath = pathKey.replaceAll(",", "/") + "/" + String.join(",", nodeSet);
            mergedPaths.add(mergedPath);
        }

        return mergedPaths;
    }

    public static List<String> convertIdsToNames(List<String> paths, Map<String, String> idToNameMap) {
        List<String> namedPaths = new ArrayList<>();

        for (String path : paths) {
            String[] nodes = path.split(",");
            List<String> namedNodes = new ArrayList<>();
            for (String node : nodes) {
                namedNodes.add(idToNameMap.getOrDefault(node, "-")); // 将ID转换为名称，如果找不到则保留原ID
            }

            namedPaths.add(String.join(",", namedNodes));
        }

        return namedPaths;
    }

    public static void main(String[] args) {
        List<String> paths = Arrays.asList(
                "291,292,293",
                "291,292,295",
                "291,292,294",
                "291,292,294,294-1",
                "291,292,294,294-2",
                "291,100,100-1"
        );

        List<String> mergedPaths = mergePaths(paths);
        System.out.println(mergedPaths.stream().collect(Collectors.joining(",")));
//        List<String> namedPaths = convertIdsToNames(mergedPaths, null);
        System.out.println(mergedPaths);
    }


    /**
     * List<Long>
     * <id, space>
     * 判断是否在营业中
     *
     * @param timeEntityList
     * @param now
     * @return
     */
    private boolean isRange(List<RestaurantTime> timeEntityList, Date now) {
        if (timeEntityList != null) {
            String yyyyMMdd = DateUtils.formatYMD(now);
            boolean isWithinTimeRange = timeEntityList.stream().anyMatch(time -> {
                Date startTime = DateUtils.parse(yyyyMMdd + " " + time.getStartTime(), "yyyy-MM-dd HH:mm");
                Date endTime = DateUtils.parse(yyyyMMdd + " " + time.getEndTime(), "yyyy-MM-dd HH:mm");

                return now.compareTo(startTime) >= 0 && now.compareTo(endTime) < 0;
            });
            return isWithinTimeRange;
        }
        return false;
    }


    private LambdaQueryWrapper<Restaurant> handlePublicPCQuery(RestaurantPageParam param) {
        LambdaQueryWrapper<Restaurant> queryWrapper = handlePublicQuery(param);
        List<Long> spaceIds = userSpaceService.findSpaceIds();
        if (spaceIds.isEmpty()) {
            queryWrapper.eq(Restaurant::getId, -1);
            return queryWrapper;
        }
        List<Long> restaurantIdList = restaurantSpaceService.list(new LambdaQueryWrapper<RestaurantSpace>()
                        .in(RestaurantSpace::getSpaceId, spaceIds)
                        .select(RestaurantSpace::getRestaurantId))
                .stream().map(RestaurantSpace::getRestaurantId).collect(Collectors.toList());
        if (restaurantIdList.isEmpty()) {
            queryWrapper.eq(Restaurant::getId, -1);
            return queryWrapper;
        }
        queryWrapper.in(Restaurant::getId, restaurantIdList);
        return queryWrapper;
    }

    /***
     * @Description 查询字段的处理
     * @author huangyongtao
     * @date 2024/7/17 11:31
     * @param param
     */
    private LambdaQueryWrapper<Restaurant> handlePublicQuery(RestaurantPageParam param) {
        LambdaQueryWrapper<Restaurant> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.like(StringUtils.isNotEmpty(param.getName()), Restaurant::getName, param.getName())
                .eq(param.getTenantId() != null, Restaurant::getTenantId, param.getTenantId())
                .in(CollectionUtils.isNotEmpty(param.getIds()), Restaurant::getId, param.getIds())
                .eq(param.getStatus() != null, Restaurant::getStatus, param.getStatus());

        if (param.getSpaceId() != null) {
            List<Long> restaurantIds = restaurantSpaceService.list(
                    new LambdaQueryWrapper<RestaurantSpace>()
                            .eq(RestaurantSpace::getSpaceId, param.getSpaceId())
                            .eq(param.getTenantId() != null, RestaurantSpace::getTenantId, param.getTenantId())
            ).stream().map(RestaurantSpace::getRestaurantId).collect(Collectors.toList());

            if (CollectionUtils.isEmpty(restaurantIds)) {
                queryWrapper.eq(Restaurant::getId, -1); // 添加一个不可能满足的条件 防止sql报错
            } else {
                queryWrapper.in(Restaurant::getId, restaurantIds);
            }
        }
        queryWrapper.orderByDesc(Restaurant::getCreateTime);
        return queryWrapper;
    }

    private void validate(RestaurantParam param, List<RestaurantTimeParam> timeList, List<RestaurantSpaceParam> spaceList) {
        Long tenantId = WebFrameworkUtils.getHeaderTenantId();
        Set<String> timeSets = timeList.stream().map(RestaurantTimeParam::getType).collect(Collectors.toSet());
        AssertUtils.isEquals(timeList.size(), timeSets.size(), "营业时间类型重复，请确认");
        AssertUtils.isTrue(!timeOverlapChecker(timeList), "营业时间重复，请确认");

        int num = restaurantRepository.selectCount(new LambdaQueryWrapper<Restaurant>()
                .eq(Restaurant::getName, param.getName()).eq(tenantId != null, Restaurant::getTenantId, tenantId)
                .ne(param.getId() != null, Restaurant::getId, param.getId()));
        AssertUtils.isTrue(num == 0, "餐厅名称已存在，请确认");
    }


    private void buildRestaurantSpace(Restaurant restaurant, List<RestaurantTimeParam> timeList, List<RestaurantSpaceParam> spaceList) {
        timeList.forEach(e -> e.setRestaurantId(restaurant.getId()));

        spaceList.forEach(e -> e.setRestaurantId(restaurant.getId()));
    }

    private boolean timeOverlapChecker(List<RestaurantTimeParam> timeList) {
        ArrayList<TimeRange> timeRanges = new ArrayList<TimeRange>();
        // 添加时间段到列表
        timeList.forEach(e -> {
            Date startTime = DateUtils.parse("2024-07-22 " + e.getStartTime(), "yyyy-MM-dd HH:mm");
            Date endTime = DateUtils.parse("2024-07-22 " + e.getEndTime(), "yyyy-MM-dd HH:mm");

            // 检查开始时间是否大于结束时间
            if (startTime.after(endTime)) {
                throw GenericException.fail("营业开始时间不能大于结束时间");
            }
            if (startTime.compareTo(endTime) == 0) {
                throw GenericException.fail("营业开始时间不能等于结束时间");
            }
            timeRanges.add(new TimeRange(startTime, endTime));

        });

        // 2. 对时间段列表按照开始时间进行排序
        Collections.sort(timeRanges, new Comparator<TimeRange>() {
            public int compare(TimeRange tr1, TimeRange tr2) {
                return tr1.getStartTime().compareTo(tr2.getStartTime());
            }
        });
        // 3. 遍历时间段列表，判断相邻时间段是否有交叉
        boolean hasOverlap = false;
        Date prevEndTime = null;
        for (TimeRange tr : timeRanges) {
            if (prevEndTime != null && tr.getStartTime().before(prevEndTime)) {
                hasOverlap = true;
                break;
            }
            prevEndTime = tr.getEndTime();
        }
        return hasOverlap;
    }

    @Data
    class TimeRange {

        private Date startTime;

        private Date endTime;

        public TimeRange(Date parse, Date parse1) {
            this.startTime = parse;
            this.endTime = parse1;
        }
    }


}
