
package com.cgnpc.bbxpark.meeting.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cgnpc.bbxpark.acl.uic.service.IRoleApiService;
import com.cgnpc.bbxpark.acl.uic.service.IUserApiService;
import com.cgnpc.bbxpark.common.constant.Constant;
import com.cgnpc.bbxpark.common.enums.MeetingAttendantRoomStatusEnum;
import com.cgnpc.bbxpark.common.utils.BeanUtils;
import com.cgnpc.bbxpark.common.utils.ConvertUtil;
import com.cgnpc.bbxpark.common.utils.StringUtils;
import com.cgnpc.bbxpark.common.utils.WebFrameworkUtils;
import com.cgnpc.bbxpark.meeting.domain.MeetingAttendant;
import com.cgnpc.bbxpark.meeting.domain.MeetingAttendantRoom;
import com.cgnpc.bbxpark.meeting.domain.MeetingAttendantTask;
import com.cgnpc.bbxpark.meeting.domain.MeetingRoom;
import com.cgnpc.bbxpark.meeting.dto.model.MeetingAttendantDetailModel;
import com.cgnpc.bbxpark.meeting.dto.model.MeetingAttendantModel;
import com.cgnpc.bbxpark.meeting.dto.model.MeetingAttendantRoomModel;
import com.cgnpc.bbxpark.meeting.dto.model.SimpleMeetingAttendantModel;
import com.cgnpc.bbxpark.meeting.dto.param.MeetingAttendantPageParam;
import com.cgnpc.bbxpark.meeting.dto.param.MeetingAttendantRoomParam;
import com.cgnpc.bbxpark.meeting.mapper.MeetingAttendantRepository;
import com.cgnpc.bbxpark.meeting.mapper.MeetingRoomRepository;
import com.cgnpc.bbxpark.meeting.service.IMeetingAttendantRoomService;
import com.cgnpc.bbxpark.meeting.service.IMeetingAttendantRoomTempService;
import com.cgnpc.bbxpark.meeting.service.IMeetingAttendantService;
import com.cgnpc.bbxpark.meeting.service.IMeetingAttendantTaskService;
import com.cgnpc.bbxpark.settings.dto.model.ConfigInfoModel;
import com.cgnpc.bbxpark.settings.service.IConfigInfoService;
import com.cgnpc.bbxpark.space.dto.model.UserInfoModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 会服人员服务实现
 */
@Service
public class MeetingAttendantServiceImpl extends ServiceImpl<MeetingAttendantRepository, MeetingAttendant> implements IMeetingAttendantService {
    @Autowired
    private IMeetingAttendantRoomService meetingAttendantRoomService;
    @Autowired
    private IMeetingAttendantRoomTempService meetingAttendantRoomTempService;
    @Autowired
    private IMeetingAttendantTaskService meetingAttendantTaskService;
    @Autowired
    private MeetingRoomRepository meetingRoomRepository;
    @Autowired
    private IUserApiService userApiService;
    @Autowired
    private IRoleApiService roleApiService;
    @Autowired
    private IMeetingAttendantRoomService attendantRoomService;
    @Autowired
    private IConfigInfoService configInfoService;

    @Override
    public List<MeetingAttendantModel> findList(){
        ConfigInfoModel configInfo = configInfoService.getByCodeDetail(Constant.MEETING_ATTENDANT_ROLE_CONFIG);
        if(configInfo == null){
            return Collections.emptyList();
        }
        List<String> staffNos = roleApiService.findStaffNoByRoleCode(configInfo.getValue());
        List<UserInfoModel> staffs = userApiService.getByStaffNos(staffNos);
        if(CollectionUtils.isEmpty(staffs)){
            return Collections.emptyList();
        }
        return BeanUtils.convertListTo(staffs,MeetingAttendantModel::new);
    }

    @Override
    public List<MeetingAttendantModel> findList(String userName) {
        List<MeetingAttendantModel> list = findList();
        if(StringUtils.isNotEmpty(userName)){
            list = list.stream().filter(l->l.getUserName().contains(userName)).collect(Collectors.toList());
        }
        return list;
    }

    @Override
    public IPage<MeetingAttendantModel> page(MeetingAttendantPageParam param) {
        List<MeetingAttendantModel> list = findList(param.getUserName());
        if (CollectionUtils.isEmpty(list)) {
            return ConvertUtil.pageEmptyConvert(param.getCurrent(), param.getSize());
        }
        //会服人员-会议室
        List<MeetingAttendantRoomModel> roomModelList = meetingAttendantRoomService.listAllByUserIdIn(list.stream().map(MeetingAttendantModel::getUserId).collect(Collectors.toList()));
        Map<String, List<String>> roomMap = roomModelList.stream().collect(Collectors.groupingBy(MeetingAttendantRoomModel::getUserId,
                Collectors.mapping(m->m.getRoomName() + statusHandle(m.getStatus()), Collectors.toList())
        ));
        //数据聚合
        list.forEach(item -> item.setRoomNameList(roomMap.get(item.getUserId())));
        return ConvertUtil.pageConvert(param.getCurrent(),list.size(),param.getSize(),list);
    }

    @Override
    public List<SimpleMeetingAttendantModel> listByRoomId(Long roomId) {
        return attendantRoomService.listByRoomId(roomId);
    }

    /**
     * 会服人员绑定会议室
     * 1.当关联会议室时(新数据),如果关联数据不存在,则新增关联关系并设置为待新增,第二天生效,如果存在关联数据且状态为待删除,则设置状态为正常
     * 2.当取消关联会议室时(老数据存在,新数据不存在),如果关联数据为待新增,那么直接删除,如果关联数据为正常,则设置状态为待删除,第二天生效
     *
     * @param param 参数
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public synchronized Boolean bindRoom(MeetingAttendantRoomParam param) {
        //新数据相比于旧的正常数据多出来的标记为待新增
        //旧数据相比于新的正常数据多出来的标记为待删除
        //新数据存在旧的待删除数据相当于取消删除操作，将待删除更改为正常
        //新数据不存在旧的待新增数据相当于取消新增操作，将待新增删除
        List<Long> roomIdList = param.getRoomIdList();
        //先查询出旧数据
        List<MeetingAttendantRoom> list = meetingAttendantRoomService.listAllByUserId(param.getUserId());
        //筛选出需要新增的会议室id
        List<Long> addRoomIdList = roomIdList.stream().filter(id->list.stream().noneMatch(room->room.getRoomId().equals(id))).collect(Collectors.toList());
        //需要删除的会议室id
        List<Long> delIdList = new ArrayList<>();
        //遍历旧数据
        Iterator<MeetingAttendantRoom> iterator = list.listIterator();
        while (iterator.hasNext()) {
            MeetingAttendantRoom item = iterator.next();
            if (!roomIdList.contains(item.getRoomId())) {
                //取消关联操作
                if (item.getStatus() == 2) {
                    //待新增又取消关联,直接删除
                    delIdList.add(item.getId());
                } else {
                    //否则设置为待删除
                    item.setStatus(3);
                }
            } else if (item.getStatus() == 3) {
                //关联操作,待删除又关联,设置为正常
                item.setStatus(1);
            } else {
                iterator.remove();
            }
        }
        //处理状态变更的关联数据
        Optional.of(list).filter(l->!l.isEmpty()).ifPresent(meetingAttendantRoomService::updateBatchById);
        //处理需要删除的关联数据
        Optional.of(delIdList).filter(l->!l.isEmpty()).ifPresent(meetingAttendantRoomService::removeByIds);
        //处理需要新增的关联数据
        List<MeetingAttendantRoom> addList = addRoomIdList.stream().map(roomId -> {
            MeetingAttendantRoom attendantRoom = new MeetingAttendantRoom();
            attendantRoom.setUserId(param.getUserId());
            attendantRoom.setRoomId(roomId);
            attendantRoom.setStatus(MeetingAttendantRoomStatusEnum.WAITING_ADD.getValue());
            return attendantRoom;
        }).collect(Collectors.toList());
        Optional.of(addList).filter(l->!l.isEmpty()).ifPresent(meetingAttendantRoomService::saveBatch);
//
//
//
//        //将旧数据按照目前状态分组:正常状态、待新增、待删除
//        Map<Integer, List<MeetingAttendantRoom>> groupedByStatus = list.stream().collect(Collectors.groupingBy(MeetingAttendantRoom::getStatus));
//        //筛选出需要新增的会议室id集合
//        List<Long> addRoomIdList = new ArrayList<>(roomIdList);
//        addRoomIdList.removeAll(list.stream().map(MeetingAttendantRoom::getRoomId).collect(Collectors.toList()));
//        //筛选出需要删除的会议室id集合
//        List<Long> delRoomIdList = list.stream().filter(room->!roomIdList.contains(room.getRoomId()) && room.getStatus() == 2).map(MeetingAttendantRoom::getRoomId).collect(Collectors.toList());
//
//
//        //遍历旧数据
//        list.forEach(item -> {
//            if(!param.getRoomIdList().contains(item.getRoomId())){
//                //新增会议室关联,并设置为待新增状态
//                MeetingAttendantRoom attendantRoom = new MeetingAttendantRoom();
//                attendantRoom.
//            }
//        });
//
//
//
//        //重新关联会议室时,先将旧关联数据标记为删除状态,等待第二天定时任务生效删除
//        LambdaUpdateWrapper<MeetingAttendantRoom> wrapper = new LambdaUpdateWrapper<>();
//        wrapper.eq(MeetingAttendantRoom::getUserId, attendant.getUserId()).eq(MeetingAttendantRoom::getTenantId, tenantId);
//        wrapper.set(MeetingAttendantRoom::getDeleted, (int) Status.disabled.getKey());
//        meetingAttendantRoomService.update(wrapper);
//        //重新关联会议室时,先将新关联数据存入临时表中,等待第二天定时任务生效,再将临时表中的关联数据迁移过来
//        meetingAttendantRoomTempService.add(attendant.getUserId(), param.getRoomIdList());
        return Boolean.TRUE;
    }

    @Override
    public MeetingAttendantDetailModel detail(String userId) {
        List<MeetingAttendantRoomModel> roomList = meetingAttendantRoomService.listAllByUserIdIn(Collections.singletonList(userId));
        MeetingAttendantDetailModel model = new MeetingAttendantDetailModel();
        // model.setId(userId);
        model.setUserId(userId);
        model.setRoomIdList(roomList.stream().map(MeetingAttendantRoomModel::getRoomId).collect(Collectors.toList()));
        model.setRoomNameList(roomList.stream().map(MeetingAttendantRoomModel::getRoomName).collect(Collectors.toList()));
        return model;
    }

    @Override
    public Boolean updateTask() {
        //会服人员集合
        List<MeetingAttendantModel> users = findList();
        //查询会服人员集合
        List<String> userIdList = users.stream().map(MeetingAttendantModel::getUserId).collect(Collectors.toList());
        //删除被移除会服角色的人员与会议室的关联信息
        meetingAttendantRoomService.remove(Wrappers.<MeetingAttendantRoom>lambdaQuery().notIn(!CollectionUtils.isEmpty(userIdList),MeetingAttendantRoom::getUserId, userIdList));

        //删除被标记为待删除的会议室关联信息
        meetingAttendantRoomService.remove(Wrappers.<MeetingAttendantRoom>lambdaQuery().eq(MeetingAttendantRoom::getStatus, MeetingAttendantRoomStatusEnum.WAITING_DEL.getValue()));
        //更新被标记为待新增的会议室关联信息
        LambdaUpdateWrapper<MeetingAttendantRoom> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(MeetingAttendantRoom::getStatus, MeetingAttendantRoomStatusEnum.WAITING_ADD.getValue());
        wrapper.set(MeetingAttendantRoom::getStatus, MeetingAttendantRoomStatusEnum.NORMAL.getValue());
        meetingAttendantRoomService.update(wrapper);
        return Boolean.TRUE;
    }

    @Override
    public List<String> findUnCompleteTask(String userId) {
        List<MeetingAttendantRoom> roomList = meetingAttendantRoomService.listByUserId(userId);
        if (CollectionUtils.isEmpty(roomList)) {
            return Collections.emptyList();
        }
        List<Long> roomIdList = roomList.stream().map(MeetingAttendantRoom::getRoomId).collect(Collectors.toList());
        List<MeetingAttendantTask> taskList = meetingAttendantTaskService.findUnCompleteTask(roomIdList);
        roomIdList = taskList.stream().map(MeetingAttendantTask::getRoomId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(roomIdList)) {
            return Collections.emptyList();
        }
        List<MeetingRoom> rooms = meetingRoomRepository.selectList(Wrappers.<MeetingRoom>lambdaQuery().in(MeetingRoom::getId, roomIdList));
        return rooms.stream().map(MeetingRoom::getRoomName).collect(Collectors.toList());
    }

    @Override
    public MeetingAttendantModel detailByLogin() {
        List<MeetingAttendantRoom> attendants = meetingAttendantRoomService.list(Wrappers.<MeetingAttendantRoom>lambdaQuery().eq(MeetingAttendantRoom::getUserId, WebFrameworkUtils.getHeaderUserId()).eq(MeetingAttendantRoom::getTenantId, WebFrameworkUtils.getHeaderTenantId()));
        MeetingAttendantModel model = new MeetingAttendantModel();
        if (ObjectUtil.isEmpty(model) || CollectionUtils.isEmpty(attendants)) {
            return model;
        }
        UserInfoModel user = getUser(WebFrameworkUtils.getHeaderUserId());
        model.setUserId(attendants.get(0).getUserId());
        model.setCreateTime(attendants.get(0).getCreateTime());
        model.setStaffid(user.getStaffid());
        model.setUserName(user.getUserName());
        return model;
    }

    /***
     * @Description 获取用户信息
     * @author huangyongtao
     * @date 2024/8/2 10:34
     * @param userId
     */
    private UserInfoModel getUser(String userId) {
        return Objects.requireNonNull(userApiService.detail(userId));
    }

    /**
     * 状态参数处理
     * @param status 状态
     * @return 状态
     */
    private String statusHandle(Integer status){
        for(MeetingAttendantRoomStatusEnum statusEnum:MeetingAttendantRoomStatusEnum.values()){
            if(statusEnum == MeetingAttendantRoomStatusEnum.NORMAL){
                continue;
            }
            if(statusEnum.getValue().equals(status)){
                return "("+statusEnum.getName()+")";
            }
        }
        return "";
    }
}
