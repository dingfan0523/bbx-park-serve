package com.cgnpc.bbxpark.meeting.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cgnpc.bbxpark.common.enums.Delete;
import com.cgnpc.bbxpark.common.enums.Status;
import com.cgnpc.bbxpark.common.utils.CollectionUtils;
import com.cgnpc.bbxpark.common.utils.DateUtil;
import com.cgnpc.bbxpark.meeting.domain.MeetingReserve;
import com.cgnpc.bbxpark.meeting.domain.MeetingRoom;
import com.cgnpc.bbxpark.meeting.domain.MeetingRoomDailyStatistics;
import com.cgnpc.bbxpark.meeting.mapper.MeetingReserveRepository;
import com.cgnpc.bbxpark.meeting.mapper.MeetingRoomDailyStatisticsRepository;
import com.cgnpc.bbxpark.meeting.mapper.MeetingRoomRepository;
import com.cgnpc.bbxpark.meeting.service.IMeetingRoomDailyStatisticsService;
import com.cgnpc.bbxpark.space.domain.TenantInfo;
import com.cgnpc.bbxpark.space.service.ITenantInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MeetingRoomDailyStatisticsServiceImpl extends ServiceImpl<MeetingRoomDailyStatisticsRepository, MeetingRoomDailyStatistics> implements IMeetingRoomDailyStatisticsService {
    @Autowired
    private MeetingRoomDailyStatisticsRepository meetingRoomDailyStatisticsRepository;
    @Autowired
    private ITenantInfoService tenantInfoService;
    @Autowired
    private MeetingRoomRepository meetingRoomRepository;
    @Autowired
    private MeetingReserveRepository meetingReserveRepository;
    private final static BigDecimal BIG_8 = new BigDecimal(8);
    private final static BigDecimal BIG_100 = new BigDecimal(100);

    public void calculateYesterdayStatistics() {
        Date yesterdayStart = DateUtil.getFirstTimeOfDateOffset(new Date(), -1);
        Date yesterdayEnd = DateUtil.getLastTimeOfDateOffset(new Date(), -1);

        List<TenantInfo> tenants = tenantInfoService.list(Wrappers.<TenantInfo>lambdaQuery().eq(TenantInfo::getDeleted, Delete.NORMAL.getKey()));
        tenants.forEach(tenant -> {
            log.info("开始统计租户{}下{}的会议室利用率数据", tenant.getName(), yesterdayStart);
            //获取会议室数据
            List<MeetingRoom> rooms = meetingRoomRepository.selectList(Wrappers.<MeetingRoom>lambdaQuery()
                    .eq(MeetingRoom::getDeleted,Delete.NORMAL.getKey()).eq(MeetingRoom::getTenantId,tenant.getId()).select(MeetingRoom::getId));
            if(CollectionUtils.isEmpty(rooms)){
                return;
            }
            Set<Long> roomIds = rooms.stream().map(MeetingRoom::getId).filter(Objects::nonNull).collect(Collectors.toSet());
            //获取昨天有效的会议数据
            List<MeetingReserve> reserves = meetingReserveRepository.selectList(Wrappers.<MeetingReserve>lambdaQuery()
                    .between(MeetingReserve::getRealStartTime, yesterdayStart, yesterdayEnd)
                    .eq(MeetingReserve::getCancelFlag, Status.disabled.getKey()).eq(MeetingReserve::getInValidFlag,Status.disabled.getKey())
                    .eq(MeetingReserve::getDraft,Status.disabled.getKey()).eq(MeetingReserve::getTenantId,tenant.getId()));
            Map<Long, List<MeetingReserve>> recordMap = reserves.stream().collect(Collectors.groupingBy(MeetingReserve::getRoomId));
            List<MeetingRoomDailyStatistics> statisticsList = new ArrayList<>();
            roomIds.forEach(roomId->{
                //当前会议室的会议记录
                List<MeetingReserve> list = recordMap.get(roomId);
                double duration = 0.0;
                if(CollectionUtils.isNotEmpty(list)){
                    duration = list.stream().filter(m->m.getRealStartTime() != null && m.getRealEndTime() != null).mapToDouble(m->{
                        long millis = m.getRealEndTime().getTime() - m.getRealStartTime().getTime();
                        //转换成小时并保留两位小数
                        BigDecimal hours = BigDecimal.valueOf(millis).divide(new BigDecimal(3600000),2, RoundingMode.HALF_UP);
                        return hours.doubleValue();
                    }).sum();
                }
                MeetingRoomDailyStatistics record = new MeetingRoomDailyStatistics();
                record.setRoomId(roomId);
                record.setDuration(duration);
                record.setUtilizationRate(BigDecimal.valueOf(duration).multiply(BIG_100).divide(BIG_8,2,RoundingMode.HALF_UP).doubleValue());
                record.setStatisticsTime(yesterdayStart);
                record.setTenantId(tenant.getId());
                statisticsList.add(record);
            });
            saveBatch(statisticsList);
        });
    }
}
