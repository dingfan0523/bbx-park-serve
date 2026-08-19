
package com.cgnpc.bbxpark.meeting.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cgnpc.bbxpark.common.utils.WebFrameworkUtils;
import com.cgnpc.bbxpark.meeting.domain.MeetingRoom;
import com.cgnpc.bbxpark.meeting.dto.model.AreaXmlModel;
import com.cgnpc.bbxpark.meeting.dto.model.ConferAreaXmlModel;
import com.cgnpc.bbxpark.meeting.dto.model.ConferRoomXmlModel;
import com.cgnpc.bbxpark.meeting.dto.model.RoomXmlModel;
import com.cgnpc.bbxpark.meeting.dto.param.ConferRoomAreaXmlParam;
import com.cgnpc.bbxpark.meeting.dto.param.ConferRoomXmlParam;
import com.cgnpc.bbxpark.meeting.mapper.MeetingRoomRepository;
import com.cgnpc.bbxpark.meeting.service.IMeetingRoomSyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

/***
 * @Description 会议室同步服务实现
 * @author huangyongtao
 * @date 2024/10/11 15:45
 */
@Service
public class MeetingRoomSyncServiceImpl extends ServiceImpl<MeetingRoomRepository, MeetingRoom> implements IMeetingRoomSyncService {

    /**
     * http客户端.
     */
    @Autowired
    private RestTemplate restTemplate;

    /**
     * 会议室列表路径
     */
    @Value("${meeting.room.url:http://10.100.4.198:8081/pcms/cgnapi/listConferRoom.do}")
    private String meetingRoomUrl;

    /**
     * 片区列表路径
     */
    @Value("${meeting.area.url:http://10.100.4.198:8081/pcms/cgnapi/listArea.do}")
    private String meetingAreaUrl;

    /**
     * 会议园区id
     */
    @Value("${meeting.tenantId:35}")
    private String meetingTenantId;

    @Override
    public Boolean sync() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_XML);
        ConferRoomXmlParam param = new ConferRoomXmlParam();
        param.setAreaId("");
        String paramStr = null;// BeanToXmlUtil.beanToXml(param, ConferRoomXmlParam.class);
        // 根据实际接口参数格式生成Xml字符串
        //String xmlData = "<conferRoomList><areaId></areaId></conferRoomList>";

        // 组装请求体
        HttpEntity<String> request = new HttpEntity<>(paramStr, headers);

        //发起请求 postForObject():返回body对象, postForEntity():返回全部的信息
//        String boyStr = restTemplate.postForObject(meetingRoomUrl, request, String.class);
//        ConferRoomXmlModel conferRoomXml = (ConferRoomXmlModel) BeanToXmlUtil.xmlStringToBean(boyStr, ConferRoomXmlModel.class);
//        if(ObjectUtil.isEmpty(conferRoomXml) || CollectionUtil.isEmpty(conferRoomXml.getRoomList())){
//            return Boolean.FALSE;
//        }
        List<RoomXmlModel> roomXmls = new ArrayList<>();
        RoomXmlModel roomXml1 =  new RoomXmlModel("123", "会议室1", "A1", null);
        roomXmls.add(roomXml1);
        RoomXmlModel roomXml2 = new RoomXmlModel("134", "会议室2", "A2", null);
        roomXmls.add(roomXml2);
        ConferRoomXmlModel conferRoomXmlTest = new ConferRoomXmlModel();
        conferRoomXmlTest.setResult(200);
        conferRoomXmlTest.setRoomList(roomXmls);
        Long tenantId = ObjectUtil.isEmpty(WebFrameworkUtils.getHeaderTenantId()) ? Long.valueOf(meetingTenantId) : WebFrameworkUtils.getHeaderTenantId();
        List<MeetingRoom> rooms = this.list(Wrappers.<MeetingRoom>lambdaQuery().eq(MeetingRoom::getTenantId, tenantId));
        Map<String, Long> thirdRoomMap = new HashMap<>();
        if(CollectionUtil.isNotEmpty(rooms)){
            thirdRoomMap = rooms.stream().filter(p->ObjectUtil.isNotEmpty(p.getThirdRoomId())).collect(Collectors.toMap(MeetingRoom::getThirdRoomId, MeetingRoom::getId, (k1, k2) -> k1));
        }
        List<MeetingRoom> roomAdds = new ArrayList<>();
        List<MeetingRoom> roomUpdates = new ArrayList<>();
        List<String> thirdRoomIds = new ArrayList<>();
        Map<String, String> areaMap = getAreaInMap();
        for (RoomXmlModel roomXml : conferRoomXmlTest.getRoomList()) {
            thirdRoomIds.add(roomXml.getRoomId());
            MeetingRoom room = new MeetingRoom();
            room.setRoomName(roomXml.getRoomName());
            room.setThirdRoomId(roomXml.getRoomId());
            room.setAreaId(roomXml.getRoomFId());
            room.setAreaName(areaMap.get(roomXml.getRoomFId()));
            room.setTenantId(tenantId);
            if(CollectionUtil.isNotEmpty(thirdRoomMap) && thirdRoomMap.containsKey(roomXml.getRoomId())) {
                room.setId(thirdRoomMap.get(roomXml.getRoomId()));
                room.setUpdateTime(new Date());
                roomUpdates.add(room);
            }else{
                roomAdds.add(room);
            }
        }
//        if(CollectionUtil.isNotEmpty(roomAdds)){
//            this.saveBatch(roomAdds);
//        }
//        if(CollectionUtil.isNotEmpty(roomUpdates)){
//            this.updateBatchById(roomUpdates);
//        }
//        if(CollectionUtil.isNotEmpty(rooms)){
//            List<MeetingRoom> roomDel = rooms.stream().filter(p->!thirdRoomIds.contains(p.getThirdRoomId()) && p.getValidFlag().equals((int)Status.enabled.getKey()))
//                    .map(room->{
//                        room.setUpdateTime(new Date());
//                        room.setValidFlag((int) Status.disabled.getKey());
//                        return room;
//                    }).collect(Collectors.toList());
//            this.updateBatchById(roomDel);
//        }
        return Boolean.TRUE;
    }

    private Map<String, String> getAreaInMap(){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_XML);
        ConferRoomAreaXmlParam param = new ConferRoomAreaXmlParam();
        param.setParentId("");
        String paramStr =  null;// BeanToXmlUtil.beanToXml(param, ConferRoomXmlParam.class);
        // 根据实际接口参数格式生成Xml字符串
        //String xmlData = "<conferRoomList><parentId ></parentId ></conferRoomList>";

        // 组装请求体
        HttpEntity<String> request = new HttpEntity<>(paramStr, headers);

        //发起请求 postForObject():返回body对象, postForEntity():返回全部的信息
//        String boyStr = restTemplate.postForObject(meetingAreaUrl, request, String.class);
//        ConferAreaXmlModel conferAreaXml = (ConferAreaXmlModel) BeanToXmlUtil.xmlStringToBean(boyStr, ConferAreaXmlModel.class);
//        if(ObjectUtil.isEmpty(conferAreaXml) || CollectionUtil.isEmpty(conferAreaXml.getAreaList())){
//            return new HashMap<>();
//        }
        List<AreaXmlModel> areaXmls = new ArrayList<>();
        AreaXmlModel areaXml1 =  new AreaXmlModel("A1", "片区1", "2", null);
        areaXmls.add(areaXml1);
        AreaXmlModel areaXml2 = new AreaXmlModel("A2", "片区2", "2", null);
        areaXmls.add(areaXml2);
        ConferAreaXmlModel conferAreaXmlTest = new ConferAreaXmlModel();
        conferAreaXmlTest.setResult(200);
        conferAreaXmlTest.setAreaList(areaXmls);
        return conferAreaXmlTest.getAreaList().stream().collect(Collectors.toMap(AreaXmlModel::getAreaId, AreaXmlModel::getAreaName,(k1,k2)->k1));
    }
}
