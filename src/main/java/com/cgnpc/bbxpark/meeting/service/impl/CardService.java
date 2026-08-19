package com.cgnpc.bbxpark.meeting.service.impl;

import com.cgnpc.bbxpark.common.enums.CardTypeEnum;
import com.cgnpc.bbxpark.meeting.dto.model.AppSimpleReserveModel;
import com.cgnpc.bbxpark.meeting.dto.model.CardMessage;
import com.cgnpc.bbxpark.meeting.service.IMeetingReserveService;
import com.cgnpc.bbxpark.restaurant.service.ICompartmentReserveService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author dingfan
 * @version 1.0
 * @date 2024/10/15 10:21
 */
@Service
public class CardService {
    @Resource
    private IMeetingReserveService meetingReserveService;
    @Resource
    private ICompartmentReserveService compartmentReserveService;

    public List<CardMessage> list(){
        AppSimpleReserveModel meeting = meetingReserveService.getNearest();
        com.cgnpc.bbxpark.restaurant.dto.model.AppSimpleReserveModel compartment = compartmentReserveService.getNearest();
        if(meeting == null && compartment == null){
            return Collections.emptyList();
        }
        List<CardMessage> list = new ArrayList<>();
        //包间卡片提醒
        Optional.ofNullable(compartment).filter(m1->m1.getId() != null).ifPresent(m -> {
            m.setRestaurantName(m.getRestaurantName() + "("+m.getCompartmentName()+")");
            list.add(buildCardMessage(CardTypeEnum.RESTAURANT, m.getId(), m.getRestaurantName(), m.getStartTime(), m.getEndTime()));
        });
        //会议卡片提醒
        Optional.ofNullable(meeting).filter(m1->m1.getId() != null).ifPresent(m ->
                list.add(buildCardMessage(CardTypeEnum.MEETING, m.getId(), m.getReserveName(), m.getStartTime(), m.getEndTime())));
        return list;
    }

    /**
     * 构建卡片消息
     * @param type 卡片类型枚举
     * @param businessId 业务id
     * @param title 卡片标题
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 卡片消息
     */
    private CardMessage buildCardMessage(CardTypeEnum type, Long businessId, String title, Date startTime, Date endTime){
        CardMessage card = new CardMessage();
        card.setType(type.getCode());
        card.setTypeName(type.getName());
        card.setBusinessId(businessId);
        card.setTitle(title);
        card.setStartTime(startTime);
        card.setEndTime(endTime);
        return card;
    }
}
