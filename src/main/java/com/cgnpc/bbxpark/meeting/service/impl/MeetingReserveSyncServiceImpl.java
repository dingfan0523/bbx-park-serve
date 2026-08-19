
package com.cgnpc.bbxpark.meeting.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cgnpc.bbxpark.acl.uic.service.IUserApiService;
import com.cgnpc.bbxpark.common.enums.Status;
import com.cgnpc.bbxpark.common.utils.DateUtils;
import com.cgnpc.bbxpark.common.utils.WebFrameworkUtils;
import com.cgnpc.bbxpark.meeting.domain.MeetingReserve;
import com.cgnpc.bbxpark.meeting.domain.MeetingRoom;
import com.cgnpc.bbxpark.meeting.dto.model.ConferXmlModel;
import com.cgnpc.bbxpark.meeting.dto.model.RoomConferXmlModel;
import com.cgnpc.bbxpark.meeting.dto.param.RoomConferXmlParam;
import com.cgnpc.bbxpark.meeting.mapper.MeetingReserveRepository;
import com.cgnpc.bbxpark.meeting.service.IMeetingReserveSyncService;
import com.cgnpc.bbxpark.meeting.service.IMeetingRoomSyncService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

/***
 * @Description 会议预约同步实现
 * @author huangyongtao
 * @date 2024/10/11 15:11
 */
@Slf4j
@Service
public class MeetingReserveSyncServiceImpl extends ServiceImpl<MeetingReserveRepository, MeetingReserve> implements IMeetingReserveSyncService {

    @Autowired
    private IMeetingRoomSyncService meetingRoomSyncService;

    @Autowired
    private IUserApiService userApiService;

    /**
     * http客户端.
     */
    @Autowired
    private RestTemplate restTemplate;

    /**
     * 会议列表路径
     */
    @Value("${meeting.reserve.url:http://10.100.4.198:8081/pcms/cgnapi/listRoomConfer.do}")
    private String meetingReserveUrl;

    /**
     * 会议园区id
     */
    @Value("${meeting.tenantId:35}")
    private String meetingTenantId;

    @Autowired
    @Qualifier("kafkaMsgThreadPool")
    private Executor executor;

    @Override
    public Boolean sync() {
        Long tenantId = ObjectUtil.isEmpty(WebFrameworkUtils.getHeaderTenantId()) ? Long.valueOf(meetingTenantId) : WebFrameworkUtils.getHeaderTenantId();
        List<MeetingRoom> rooms = meetingRoomSyncService.getBaseMapper().selectList(Wrappers.<MeetingRoom>lambdaQuery()
                .eq(MeetingRoom::getTenantId, tenantId)
                .eq(MeetingRoom::getValidFlag, Status.enabled.getKey())
                .isNotNull(MeetingRoom::getThirdRoomId));
        if(CollectionUtil.isEmpty(rooms)){
            return Boolean.FALSE;
        }
        String dayFormat = DateUtils.formatYMD(new Date());
        Date startTime = DateUtils.format(dayFormat + " 00:00:00", "yyyy-MM-dd HH:mm:ss");
        Date endTime = DateUtils.format(dayFormat + " 23:59:59", "yyyy-MM-dd HH:mm:ss");
        List<MeetingReserve> meetingReserveList = this.list(Wrappers.<MeetingReserve>lambdaQuery()
                .select(MeetingReserve::getId, MeetingReserve::getThirdReserveId, MeetingReserve::getRoomId)
                .eq(MeetingReserve::getTenantId, tenantId)
                .ge(MeetingReserve::getStartTime, startTime)
                .le(MeetingReserve::getEndTime, endTime));
        if(CollectionUtil.isEmpty(meetingReserveList)){
            meetingReserveList = new ArrayList<>();
        }
//        for(MeetingRoom room : rooms){
//            List<MeetingReserve> finalMeetingReserveList = meetingReserveList;
//            executor.execute(()->{
//                handleMeetingReserve(room, dayFormat, finalMeetingReserveList.stream().filter(p->room.getId().equals(p.getRoomId())).collect(Collectors.toList()));
//            });
//        }
        return Boolean.TRUE;
    }

    private void handleMeetingReserve(MeetingRoom room, String dayFormat, List<MeetingReserve> meetingReserveList) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_XML);
        RoomConferXmlParam param = new RoomConferXmlParam();
        param.setRoomId(room.getThirdRoomId());
        param.setDate(dayFormat);
        String paramStr = null ;// BeanToXmlUtil.beanToXml(param, ConferRoomXmlParam.class);
        // 根据实际接口参数格式生成Xml字符串
        //String xmlData = "<conferRoomList><roomId>123</roomId><date>2024-10-12</date></conferRoomList>";
        // 组装请求体
        HttpEntity<String> request = new HttpEntity<>(paramStr, headers);
        //发起请求 postForObject():返回body对象, postForEntity():返回全部的信息
//        String boyStr = restTemplate.postForObject(meetingReserveUrl, request, String.class);
//        RoomConferXmlModel roomConferXmlModel = (RoomConferXmlModel) BeanToXmlUtil.xmlStringToBean(boyStr, RoomConferXmlModel.class);
//        if(ObjectUtil.isEmpty(roomConferXmlModel) || CollectionUtil.isEmpty(roomConferXmlModel.getConferList())){
//            return;
//        }
        List<ConferXmlModel> conferList = new ArrayList<>();
        ConferXmlModel conferXml1 =  new ConferXmlModel("122", "会议1", "测试人员1", "2024-10-12 09:00:00", "2024-10-12 10:00:00", "会议室1", null);
        conferList.add(conferXml1);
//        ConferXmlModel conferXml2 = new ConferXmlModel("133", "会议2", "测试人员1", "2024-10-12 09:00:00", "2024-10-12 10:00:00", "会议室1", null);
//        conferList.add(conferXml2);
        RoomConferXmlModel roomConferXmlTest = new RoomConferXmlModel();
        roomConferXmlTest.setResult(200);
        roomConferXmlTest.setConferList(conferList);

        Map<String, Long> thirdReserveMap = new HashMap<>();
        if(CollectionUtil.isNotEmpty(meetingReserveList)){
            thirdReserveMap = meetingReserveList.stream().filter(p->ObjectUtil.isNotEmpty(p.getThirdReserveId()) && room.getId().equals(p.getRoomId())).collect(Collectors.toMap(MeetingReserve::getThirdReserveId, MeetingReserve::getId, (k1, k2) -> k1));
        }
        List<MeetingReserve> reserveAdds = new ArrayList<>();
        List<MeetingReserve> reserveUpdates = new ArrayList<>();
        List<String> thirdReserveIds = new ArrayList<>();
        for (ConferXmlModel conferXml : roomConferXmlTest.getConferList()) {
            thirdReserveIds.add(conferXml.getConferId());
            MeetingReserve reserve = new MeetingReserve();
            reserve.setThirdReserveId(conferXml.getConferId());
            reserve.setRoomId(room.getId());
            reserve.setRoomName(room.getRoomName());
            reserve.setReserveName(conferXml.getConferName());
            reserve.setStartTime(DateUtils.format(conferXml.getStartTime(), "yyyy-MM-dd HH:mm:ss"));
            reserve.setEndTime(DateUtils.format(conferXml.getEndTime(), "yyyy-MM-dd HH:mm:ss"));
            reserve.setReserveUid("");
            reserve.setReserveUname(conferXml.getAccountName());
            reserve.setReserveStaffid("0001");
            reserve.setReserveDepartment("纪检监察处9");
            if(CollectionUtil.isNotEmpty(thirdReserveMap) && thirdReserveMap.containsKey(conferXml.getConferId())) {
                reserve.setId(thirdReserveMap.get(conferXml.getConferId()));
                reserveUpdates.add(reserve);
            }else{
                reserve.setLastRuleTime(new Date());
                reserve.setOperateTime(reserve.getEndTime());
                reserve.setOperateUname("系统");
                reserve.setOperateReason("正常召开");
                reserve.setRevision(0);
                reserveAdds.add(reserve);
            }
        }
        if(CollectionUtil.isNotEmpty(reserveAdds)){
            this.saveBatch(reserveAdds);
        }
        if(CollectionUtil.isNotEmpty(reserveUpdates)){
            this.updateBatchById(reserveUpdates);
        }
        if(CollectionUtil.isNotEmpty(meetingReserveList)){
            List<MeetingReserve> reserveDel = meetingReserveList.stream().filter(p->!thirdReserveIds.contains(p.getThirdReserveId()) && new Date().before(p.getEndTime()) && p.getValidFlag().equals((int)Status.enabled.getKey()))
                    .map(reserve->{
                        reserve.setUpdateTime(new Date());
                        reserve.setValidFlag(Status.disabled.getKey());
                        return reserve;
                    }).collect(Collectors.toList());
            this.updateBatchById(reserveDel);
        }
    }
}
