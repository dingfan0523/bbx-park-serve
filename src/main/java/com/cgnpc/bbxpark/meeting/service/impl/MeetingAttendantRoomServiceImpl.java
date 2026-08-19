
package com.cgnpc.bbxpark.meeting.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cgnpc.bbxpark.acl.uic.service.IUserApiService;
import com.cgnpc.bbxpark.common.enums.MeetingAttendantRoomStatusEnum;
import com.cgnpc.bbxpark.common.utils.WebFrameworkUtils;
import com.cgnpc.bbxpark.meeting.domain.MeetingAttendantRoom;
import com.cgnpc.bbxpark.meeting.dto.model.MeetingAttendantRoomModel;
import com.cgnpc.bbxpark.meeting.dto.model.SimpleMeetingAttendantModel;
import com.cgnpc.bbxpark.meeting.mapper.MeetingAttendantRoomRepository;
import com.cgnpc.bbxpark.meeting.service.IMeetingAttendantRoomService;
import com.cgnpc.bbxpark.space.dto.model.UserInfoModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 会服人员-会议室服务实现
 */
@Service
public class MeetingAttendantRoomServiceImpl extends ServiceImpl<MeetingAttendantRoomRepository, MeetingAttendantRoom> implements IMeetingAttendantRoomService {

    @Autowired
    private IUserApiService userApiService;
    @Override
    public List<MeetingAttendantRoomModel> listByUserIdIn(List<String> userIds) {
        return getBaseMapper().listByUserId(WebFrameworkUtils.getHeaderTenantId(),MeetingAttendantRoomStatusEnum.NORMAL.getValue(),userIds);
    }

    @Override
    public List<MeetingAttendantRoomModel> listAllByUserIdIn(List<String> userIds) {
        return getBaseMapper().listByUserId(WebFrameworkUtils.getHeaderTenantId(),null,userIds);
    }

    @Override
    public List<MeetingAttendantRoom> listByUserId(String userId) {
        return list(Wrappers.<MeetingAttendantRoom>lambdaQuery().eq(MeetingAttendantRoom::getUserId,userId).eq(MeetingAttendantRoom::getStatus, MeetingAttendantRoomStatusEnum.NORMAL.getValue()).eq(MeetingAttendantRoom::getTenantId,WebFrameworkUtils.getHeaderTenantId()));
    }

    @Override
    public List<MeetingAttendantRoom> listAllByUserId(String userId) {
        return list(Wrappers.<MeetingAttendantRoom>lambdaQuery().eq(MeetingAttendantRoom::getUserId,userId).eq(MeetingAttendantRoom::getTenantId,WebFrameworkUtils.getHeaderTenantId()));
    }

    @Override
    public List<SimpleMeetingAttendantModel> listByRoomId(Long roomId){
        List<MeetingAttendantRoom> list = list(Wrappers.<MeetingAttendantRoom>lambdaQuery().eq(MeetingAttendantRoom::getRoomId, roomId)
                .eq(MeetingAttendantRoom::getTenantId, WebFrameworkUtils.getHeaderTenantId()));
        List<String> userIds = list.stream().map(MeetingAttendantRoom::getUserId).distinct().collect(Collectors.toList());
//        List<UserInfoModel> staffNos = userApiService.getByStaffNos(userIds);
        List<UserInfoModel> staffNos = new ArrayList<>();
        for(String userId:userIds){
            //需要返回用户手机号,批量查询没有手机号信息,所以遍历查询
            staffNos.add(userApiService.getByStaffNo(userId));
        }

        Map<String, UserInfoModel> modelMap = staffNos.stream().collect(Collectors.toMap(UserInfoModel::getStaffNo, item -> item));
        List<SimpleMeetingAttendantModel> models = list.stream().map(mar -> {
            UserInfoModel infoModel = modelMap.get(mar.getUserId());
            SimpleMeetingAttendantModel sam = new SimpleMeetingAttendantModel();
            sam.setId(mar.getId());
            sam.setUserId(mar.getUserId());
            sam.setUserName(infoModel.getUserName());
            sam.setMobile(infoModel.getMobile());
            return sam;
        }).collect(Collectors.toList());
        return models;
    }


}
