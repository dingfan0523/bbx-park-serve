package com.cgnpc.bbxpark.device.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cgnpc.bbxpark.common.utils.BeanUtils;
import com.cgnpc.bbxpark.common.utils.CollectionUtils;
import com.cgnpc.bbxpark.common.utils.StringUtils;
import com.cgnpc.bbxpark.common.utils.WebFrameworkUtils;
import com.cgnpc.bbxpark.device.domain.IocDevice;
import com.cgnpc.bbxpark.device.domain.ThingModelMessage;
import com.cgnpc.bbxpark.device.dto.model.ThingModelMessageModel;
import com.cgnpc.bbxpark.device.dto.param.ThingModelMessageParam;
import com.cgnpc.bbxpark.device.mapper.IocDeviceRepository;
import com.cgnpc.bbxpark.device.mapper.ThingModelMessageRepository;
import com.cgnpc.bbxpark.device.service.IAlarmInfoService;
import com.cgnpc.bbxpark.device.service.ThingModelMessageService;
import com.cgnpc.bbxpark.space.dto.model.ParkSpaceFullModel;
import com.cgnpc.bbxpark.space.mapper.ParkSpaceRepository;
import com.cgnpc.bbxpark.space.mapper.UserSpaceRepository;
import com.cgnpc.bbxpark.space.service.IParkSpaceService;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ThingModelMessageServiceImpl implements ThingModelMessageService {

    private static final Logger log = LoggerFactory.getLogger(ThingModelMessageServiceImpl.class);
    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Autowired
    private ThingModelMessageRepository thingModelMessageRepository;

    @Autowired
    private UserSpaceRepository userSpaceRepository;

    @Autowired
    private ParkSpaceRepository parkSpaceRepository;
    @Autowired
    private IParkSpaceService parkSpaceService;


    @Autowired
    private IocDeviceRepository iocDeviceRepository;

    @Autowired
    private IAlarmInfoService alarmInfoService;

    @Override
    public IPage<ThingModelMessage> selectPage(ThingModelMessageParam dto) {
        Integer maxTotal = 10000;
        int page = Math.toIntExact(dto.getCurrent());
        int limit = Math.toIntExact(dto.getSize());
        if(page * limit >= maxTotal){
            page = maxTotal/limit-1;
        }
        BoolQueryBuilder builder = QueryBuilders.boolQuery();

        if(StringUtils.isNotBlank(dto.getNoId())){
            builder.mustNot(QueryBuilders.termQuery("id", dto.getNoId()));
        }
        if (StringUtils.isNotBlank(dto.getProductKey())) {
            builder.must(QueryBuilders.termQuery("productKey", dto.getProductKey()));
        }
        if (StringUtils.isNotBlank(dto.getProductName())) {
            builder.must(QueryBuilders.matchQuery("productName", dto.getProductName()));
        }
        if (StringUtils.isNotBlank(dto.getType())) {
            builder.must(QueryBuilders.termQuery("type", dto.getType()));
        }
        if (StringUtils.isNotBlank(dto.getIdentifier())) {
            builder.must(QueryBuilders.termQuery("identifier", dto.getIdentifier()));
        }
        List<String> deviceIdList = getDeviceIdList(dto);
        if(StringUtils.isNotBlank(dto.getDeviceId())) {
            deviceIdList.add(dto.getDeviceId());
        }
        if(CollectionUtils.isNotEmpty(deviceIdList)){
            builder.must(QueryBuilders.termsQuery("deviceId", deviceIdList));
        }

        IPage<ThingModelMessage> iPage = null;
        NativeSearchQuery query = new NativeSearchQueryBuilder().withQuery(builder)
                .withTrackTotalHits(true).withTrackScores(true)
                .withPageable(PageRequest.of(Math.max(page - 1, 0), limit, Sort.by(Sort.Order.desc("time"))))
                .build();
        query.setTrackTotalHits(true);
        if(StringUtils.isNotEmpty(dto.getNoId())){
            query.setSearchAfter(Collections.singletonList(dto.getNoId()));
        }
        SearchHits<ThingModelMessage> result = elasticsearchRestTemplate.search(query, ThingModelMessage.class);

        List<ThingModelMessage> list = result.getSearchHits().stream()
                .map(m -> BeanUtil.toBean(m.getContent(), ThingModelMessage.class))
                .collect(Collectors.toList());

        if(!list.isEmpty()){
            Set<String> deviceIds = list.stream().map(ThingModelMessage::getDeviceId).collect(Collectors.toSet());
            if(!deviceIds.isEmpty()){
                LambdaQueryWrapper<IocDevice> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.in(IocDevice::getIotDeviceDn, deviceIds);
                List<IocDevice> deviceInfoList = iocDeviceRepository.selectList(queryWrapper);
                buildThingModelMessageDeviceName(list, deviceInfoList);
                List<Long> spaceIds = deviceInfoList.stream().map(IocDevice::getSpaceId).collect(Collectors.toList());
                if(!spaceIds.isEmpty()) {
                    Map<Long, ParkSpaceFullModel> spaceMap = parkSpaceService.findFullSpaceMap(spaceIds, WebFrameworkUtils.getHeaderTenantId());
                    if(!spaceMap.isEmpty()) {
                        buildThingModelMessage(list, deviceInfoList, spaceMap);
                    }
                }
            }
        }

        Long total = result.getTotalHits();
        if(total >= 10000){
            total = 10000L;
        }
        iPage = new Page(page, limit, total);
        iPage.setRecords(list);
       return iPage;
    }

    private List<String> getDeviceIdList(ThingModelMessageParam dto){
        List<String> spaceDeviceIdList;
        if(StringUtils.isEmpty(dto.getDeviceId()) && StringUtils.isEmpty(dto.getDeviceName()) && dto.getSpaceId() == null){
            return Collections.emptyList();
        }
        List<IocDevice> deviceList = iocDeviceRepository.selectList(new LambdaQueryWrapper<IocDevice>()
                .ne(IocDevice::getIotDevicePlatform,0)
                .eq(dto.getSpaceId() != null, IocDevice::getSpaceId, dto.getSpaceId())
                .eq(StringUtils.isNotEmpty(dto.getDeviceId()), IocDevice::getIotDeviceDn, dto.getDeviceId())
                .like(StringUtils.isNotEmpty(dto.getDeviceName()),IocDevice::getDeviceName,dto.getDeviceName())
                .select(IocDevice::getIotDeviceDn));
        spaceDeviceIdList = deviceList.stream().map(IocDevice::getIotDeviceDn).collect(Collectors.toList());
        if(spaceDeviceIdList.isEmpty()){
            spaceDeviceIdList.add("-1");
        }
        return spaceDeviceIdList;
    }
    private void buildThingModelMessageDeviceName(List<ThingModelMessage> list, List<IocDevice> deviceInfoList){
        for(ThingModelMessage msg : list){
            deviceInfoList.stream().filter(e -> e.getIotDeviceDn().equals(msg.getDeviceId())).findFirst().ifPresent(deviceInfo -> msg.setDeviceName(deviceInfo.getDeviceName()));

        }
    }
    private void  buildThingModelMessage(List<ThingModelMessage> list, List<IocDevice> deviceInfoList, Map<Long, ParkSpaceFullModel> spaceMap){
        for(ThingModelMessage msg : list){
            IocDevice deviceInfo = deviceInfoList.stream().filter(e->e.getIotDeviceDn().equals(msg.getDeviceId())).findFirst().orElse(null);
            if(deviceInfo != null){
                ParkSpaceFullModel space = spaceMap.get(deviceInfo.getSpaceId());
                if(space != null){
                    msg.setSpaceName(space.getFullPath());
                }
            }
        }
    }


    @Override
    public ThingModelMessageModel findById(String id) {
        Optional<ThingModelMessage> optional = thingModelMessageRepository.findById(id);
        if (optional.isPresent()){
            ThingModelMessage thingModelMessage = optional.get();
            return BeanUtils.convertTo(thingModelMessage, ThingModelMessageModel::new);
        }
        return null;
    }

    @Override
    public int save() {
        return 0;
    }

    @Override
    public List<ThingModelMessageModel> search() {

        int limit = 10;
        ThingModelMessageParam dto = new ThingModelMessageParam();
        for(int page = 1; page <= 10000; page++){
            BoolQueryBuilder builder = QueryBuilders.boolQuery();
            NativeSearchQuery query = new NativeSearchQueryBuilder().withQuery(builder)
                    .withTrackTotalHits(true).withTrackScores(true)
                    .withPageable(PageRequest.of(0, limit, Sort.by(Sort.Order.desc("id"))))
                    .build();
            if(dto.getNoId() != null){
                List<Object> searchAfterValues = new ArrayList<>();
                searchAfterValues.add(dto.getNoId());
                // 添加其他排序字段值
                query.setSearchAfter(searchAfterValues);
            }
            SearchHits<ThingModelMessage> result = elasticsearchRestTemplate.search(query, ThingModelMessage.class);
            List<SearchHit<ThingModelMessage>> list = result.getSearchHits();

            if(list.isEmpty()) {
                break;
            }
            log.info("result total size = {}; result size = {}; last id = {} ", result.getTotalHits(), list.size(), list.get(list.size()-1).getId());
            dto.setNoId(list.get(list.size()-1).getContent().getId());
        }
        return Collections.emptyList();
    }
}
