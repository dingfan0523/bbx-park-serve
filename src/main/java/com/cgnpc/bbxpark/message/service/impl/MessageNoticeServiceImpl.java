package com.cgnpc.bbxpark.message.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cgnpc.bbxpark.acl.uic.service.IUserApiService;
import com.cgnpc.bbxpark.common.constant.SystemResultCode;
import com.cgnpc.bbxpark.common.utils.*;
import com.cgnpc.bbxpark.message.domain.MessageInfo;
import com.cgnpc.bbxpark.message.domain.MessageNotice;
import com.cgnpc.bbxpark.message.domain.MessageUser;
import com.cgnpc.bbxpark.message.dto.req.MessageNoticeListParam;
import com.cgnpc.bbxpark.message.dto.req.MessageNoticePageParam;
import com.cgnpc.bbxpark.message.dto.req.MessageNoticeParam;
import com.cgnpc.bbxpark.message.dto.resp.MessageNoticeModel;
import com.cgnpc.bbxpark.message.mapper.MessageInfoRepository;
import com.cgnpc.bbxpark.message.mapper.MessageNoticeRepository;
import com.cgnpc.bbxpark.message.mapper.MessageUserRepository;
import com.cgnpc.bbxpark.message.service.IMessageNoticeService;
import com.cgnpc.bbxpark.message.service.IMessageUserService;
import com.cgnpc.bbxpark.space.domain.TenantMember;
import com.cgnpc.bbxpark.space.dto.model.UserInfoModel;
import com.cgnpc.bbxpark.space.service.ITenantMemberService;
import com.cgnpc.cud.core.exception.BaseException;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 通知公告服务实现
 */
@Service
public class MessageNoticeServiceImpl extends ServiceImpl<MessageNoticeRepository, MessageNotice> implements IMessageNoticeService {
    /**
     * 注入repository.
     */
    @Autowired
    private MessageNoticeRepository messageNoticeRepository;

    @Autowired
    private MessageInfoRepository messageInfoRepository;

    @Autowired
    private MessageUserRepository messageUserRepository;
    @Autowired
    private IMessageUserService messageUserService;
    @Autowired
    private ITenantMemberService tenantMemberService;

    @Autowired
    private IUserApiService userApiService;
    private MessageNoticeModel convert(MessageNotice notice){
        List<MessageNoticeModel> list = convertList(Collections.singletonList(notice));
        return CollectionUtils.isEmpty(list) ? new MessageNoticeModel() : list.get(0);
    }
    private List<MessageNoticeModel> convertList(List<MessageNotice> notices){
        if(CollectionUtils.isEmpty(notices)){
            return Collections.emptyList();
        }
        //消息信息
        List<Long> msgIds = notices.stream().map(MessageNotice::getMsgId).collect(Collectors.toList());
        List<MessageInfo> infos = messageInfoRepository.selectList(Wrappers.<MessageInfo>lambdaQuery().in(MessageInfo::getId,msgIds));
        Map<Long,MessageInfo> infoMap = infos.stream().collect(Collectors.toMap(MessageInfo::getId,m->m,(v1,v2)->v2));
        //用户信息
        Set<String> staffNos = new HashSet<>();
        infos.forEach(info->{
            Optional.ofNullable(info.getCreatorId()).ifPresent(staffNos::add);
            Optional.ofNullable(info.getPublisher()).ifPresent(staffNos::add);
            Optional.ofNullable(info.getUpdatorId()).ifPresent(staffNos::add);
        });
        List<UserInfoModel> userInfos = userApiService.getByStaffNos(new ArrayList<>(staffNos));
        Map<String,String> userNameMap = userInfos.stream().collect(Collectors.toMap(UserInfoModel::getStaffNo,UserInfoModel::getUserName,(v1,v2)->v2));
        //数据组装
        return notices.stream().map(notice -> {
            MessageNoticeModel messageNoticeModel = BeanUtils.convertTo(notice, MessageNoticeModel::new);
            if(infoMap.containsKey(notice.getMsgId())){
                MessageInfo info = infoMap.get(notice.getMsgId());
                messageNoticeModel.setTitle(info.getTitle());
                messageNoticeModel.setContent(info.getContent());
                messageNoticeModel.setPublishTime(info.getPublishTime());
                messageNoticeModel.setSummary(info.getSummary());
                messageNoticeModel.setCover(info.getCover());
                messageNoticeModel.setSendingSettings(info.getSendingSettings());
                messageNoticeModel.setReceiverScope(info.getReceiverScope());
                messageNoticeModel.setStatus(info.getStatus());
                messageNoticeModel.setRemark(info.getTitle());
                messageNoticeModel.setReceiverId(info.getReceiverId());
                messageNoticeModel.setCreateTime(info.getCreateTime());
                messageNoticeModel.setUpdateTime(info.getUpdateTime());
                if(info.getCreatorId() != null && userNameMap.containsKey(info.getCreatorId())){
                    messageNoticeModel.setNickName(userNameMap.get(info.getCreatorId()));
                    messageNoticeModel.setCreatorName(userNameMap.get(info.getCreatorId()));
                }
                if(info.getUpdatorId() != null && userNameMap.containsKey(info.getUpdatorId())){
                    messageNoticeModel.setUpdatorName(userNameMap.get(info.getUpdatorId()));
                }
                if(info.getPublisher() != null && userNameMap.containsKey(info.getPublisher())){
                    messageNoticeModel.setNickName(userNameMap.get(info.getPublisher()));
                }
            }
            return messageNoticeModel;
        }).collect(Collectors.toList());
    }

    /**
     * 根据通知公告标识获得通知公告详情信息.
     *
     * @Param [id] 通知公告标识
     * @Return 通知公告详情信息
     */
    @Override
    public MessageNoticeModel detail(Long id) {
        MessageNotice messageNotice = this.getById(id);
        AssertUtils.notNull(messageNotice, SystemResultCode.RESULT_DATA_NONE.message());
        MessageNoticeModel messageNoticeModel = convert(messageNotice);
        messageLogic(messageNoticeModel.getReceiverScope(),userApiService.getCurrentStaffNo(),messageNotice.getMsgId());
        String receiverId = messageNoticeModel.getReceiverId();
        if (StringUtils.isNotEmpty(receiverId)){
            String[] split = receiverId.split(",");
            List<UserInfoModel> userInfos = userApiService.getByStaffNos(Arrays.asList(split));
            if (ObjectUtil.isNotEmpty(userInfos)){
                String userNames = userInfos.stream().map(UserInfoModel::getUserName).collect(Collectors.joining(","));
                if (StringUtils.isNotEmpty(userNames)){
                    messageNoticeModel.setUserNames(userNames);
                }
            }
        }
        return messageNoticeModel;
    }


    public void messageLogic (Integer receiverScope,String userId,Long msgId){
        if (Objects.equals(receiverScope,0)){
            MessageUser message = new MessageUser();
            message.setUserId(userId);
            message.setMsgId(msgId);
            List<MessageUser> messageUserList = messageUserRepository.selectList(Wrappers.<MessageUser>lambdaQuery()
                    .eq(StringUtils.isNotEmpty(userId),MessageUser::getUserId,userId)
                    .eq(msgId != null,MessageUser::getMsgId,msgId));
            if (ObjectUtil.isEmpty(messageUserList)){
                MessageUser messageUser = new MessageUser();
                messageUser.setUserId(userId);
                messageUser.setMsgId(msgId);
                messageUser.setCreatorId(userId);
                messageUser.setCreateTime(new Date());
                messageUserRepository.insert(messageUser);
            }
        }
    }
    /**
     * 获取通知公告列表(分页).
     *
     * @Param param 通知公告查询条件
     * @Return 通知公告信息列表（分页）
     */
    @Override
    public IPage<MessageNoticeModel> page(MessageNoticePageParam param) {
        IPage<MessageNoticeModel> messageNoticePage = this.baseMapper.findPage(new Page<>(param.getCurrent(),param.getSize()),param);
        if(CollectionUtils.isEmpty(messageNoticePage.getRecords())){
            return ConvertUtil.pageEmptyConvert(param.getCurrent(),param.getSize());
        }
        List<MessageNoticeModel> messageNotices = messageNoticePage.getRecords();
        //员工号集合
        List<String> staffNos = messageNotices.stream().map(MessageNoticeModel::getPublisherId)
                .filter(StringUtils::isNotEmpty).distinct().collect(Collectors.toList());
        List<UserInfoModel> userInfos = userApiService.getByStaffNos(staffNos);
        Map<String,UserInfoModel> userInfoMap = CollectionUtils.isEmpty(userInfos) ? new HashMap<>(4) :
                userInfos.stream().collect(Collectors.toMap(UserInfoModel::getId,u->u,(v1,v2)->v2));

        //状态
        Date date = new Date();
        messageNotices.forEach(a -> {
            if (Objects.equals(a.getSendingSettings(),2L) && Objects.equals(a.getStatus(),1)){
                if (date.before(a.getPublishTime())) {
                    a.setStatus(2);
                }
            }
            if(StringUtils.isNotEmpty(a.getPublisherId()) && userInfoMap.containsKey(a.getPublisherId())){
                a.setPublisherName(userInfoMap.get(a.getPublisherId()).getUserName());
            }
        });
        return messageNoticePage;
    }

    /**
     * 发布
     * @return 是否成功
     */
    @Transactional
    @Override
    public synchronized Boolean publishNotify(Long id) {
        MessageNotice messageNotice = getById(id);
        String userId = userApiService.getCurrentStaffNo();
        MessageInfo info = messageInfoRepository.selectById(messageNotice.getMsgId());
        if (Objects.equals(messageNotice.getEffectiveStatus(), 0) && Objects.equals(info.getStatus(), 0)) {
            MessageInfo messageInfo = new MessageInfo();
            messageInfo.setId(messageNotice.getMsgId());
            messageInfo.setStatus(1);
            messageInfo.setPublisher(userId);
            messageInfo.setPublishTime(new Date());
            messageInfo.setReceiverScope(info.getReceiverScope());
            messageInfo.setReceiverId(info.getReceiverId());
            messageInfoRepository.updateById(messageInfo);
            //落库用户消息
            messageUserInsert(messageInfo,userId);
            return true;
        }
        throw new BaseException("通知公告状态异常，发布失败！");
    }

    @Override
    public void messageUserInsert(MessageInfo messageInfo,String creatorId){
        /**
         * 0全员1特定用户2分组3角色4组织5当前部门6当前部门及子部门7租户.
         */
        if (Objects.equals(messageInfo.getReceiverScope(),1)) {
            List<String> userIds = Arrays.asList( messageInfo.getReceiverId().split(","));
            //查询已经存在的用户
            List<MessageUser> messageUsers = messageUserRepository.selectList(Wrappers.<MessageUser>lambdaQuery().eq(MessageUser::getMsgId,messageInfo.getId()));
            List<String> messageUserIds = messageUsers.stream().map(MessageUser::getUserId).collect(Collectors.toList());
            // 排除userId在messageUserIds中的用户
            userIds = userIds.stream().filter(a->!messageUserIds.contains(a)).collect(Collectors.toList());
            userInsert(userIds,messageInfo.getId(),creatorId);
        }
        //获取组织下面的用户进行洛库
//        if (Objects.equals(messageInfo.getReceiverScope(),MessageNoticeExt.DEFAULT_4)) {
//            OrganizationMember organizationMember = new OrganizationMember();
//            organizationMember.setOrganizationId(Long.valueOf(messageInfo.getReceiverId()));
//            List<OrganizationMember> organizationMembers = organizationMemberRepository.selectEntitys(organizationMember);
//            List<Long> userIds = organizationMembers.stream().map(OrganizationMember::getUserId).collect(Collectors.toList());
//            List<UserInfo> userInfoList = userInfoRepository.selectList(new WhereClause().addIn(UserInfoExt.C_ID, userIds));
//            userInsert(userInfoList,messageInfo.getId(),creatorId);
//        }
        //获取租户下面的用户进行洛库
        if (Objects.equals(messageInfo.getReceiverScope(),7)){
            List<TenantMember> tenantMembers = tenantMemberService.list(Wrappers.<TenantMember>lambdaQuery().eq(TenantMember::getTenantId,WebFrameworkUtils.getHeaderTenantId()));
            List<String> userIds = tenantMembers.stream().map(TenantMember::getUserId).collect(Collectors.toList());
            userInsert(userIds,messageInfo.getId(),creatorId);
        }
    }

    /**
     * 用户消息落库
     * @param userIds 用户list
     * @param msgId 消息标识
     * @param creatorId 创建者
     */
    private void userInsert (List<String> userIds,Long msgId,String creatorId){
        if (ObjectUtil.isNotEmpty(userIds)){
            Long headerTenantId = WebFrameworkUtils.getHeaderTenantId();
            List<MessageUser> messageUserList = userIds.stream().map(userId -> {
                MessageUser messageUser = new MessageUser();
                messageUser.setUserId(userId);
                messageUser.setTenantId(headerTenantId);
                messageUser.setMsgId(msgId);
                messageUser.setCreatorId(creatorId);
                messageUser.setCreateTime(new Date());
                return messageUser;
            }).collect(Collectors.toList());
            messageUserService.saveBatch(messageUserList);
        }
    }

    /**
     * 获取通知公告列表.
     *
     * @Param param 通知公告查询条件
     * @Return 通知公告信息列表
     */
    @Override
    @SneakyThrows
    public List<MessageNoticeModel> list(MessageNoticeListParam param) {
        return this.baseMapper.findList(param);
    }

    /**
     * 新增通知公告.
     *
     * @Param param 通知公告信息
     * @Return 新增通知公告是否成功
     */
    @Override
    @Transactional
    public Boolean add(MessageNoticeParam param) {
        Long headerTenantId = param.getTenantId() == null ? WebFrameworkUtils.getHeaderTenantId() : param.getTenantId();

        MessageNotice messageNotice = BeanUtils.convertTo(param, MessageNotice::new);
        messageNotice.setTenantId(headerTenantId);
        messageNotice.setId(null);
        MessageInfo messageInfo = new MessageInfo();
        BeanUtils.copyProperties(param, messageInfo);
        messageInfo.setPublishTime(param.getPublishTime() != null ? param.getPublishTime() : new Date());
        messageInfo.setPublisher(userApiService.getCurrentStaffNo());
        messageInfo.setCreateBy(userApiService.getCurrentStaffName());
        messageInfo.setTenantId(headerTenantId);
        messageInfo.setContent(SimpleHtmlUtil.transform(messageInfo.getContent()));
        messageInfoRepository.insert(messageInfo);
        messageNotice.setMsgId(messageInfo.getId());
        // 如果是已发送，那么就直接发布这条消息
        if (messageInfo.getStatus() == 1) {
            messageUserInsert(messageInfo,messageInfo.getCreatorId());
        }
        return this.save(messageNotice);
    }

    /**
     * 批量新增通知公告.
     *
     * @Param params 通知公告信息列表
     * @Return 批量新增通知公告是否成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean addBatch(List<MessageNoticeParam> params) {
        List<MessageNotice> messageNotices = BeanUtils.convertListTo(params, MessageNotice::new);
        return saveBatch(messageNotices);
    }

    /**
     * 删除通知公告.
     *
     * @Param id 通知公告标识
     * @Return 删除通知公告是否成功
     */
    @Override
    public Boolean remove(Long id) {
        MessageNotice messageNotice = this.getById(id);
        AssertUtils.notNull(messageNotice, SystemResultCode.RESULT_DATA_NONE.message());
        messageInfoRepository.deleteById(messageNotice.getMsgId());
        messageNotice.setEffectiveStatus(1);
        return updateById(messageNotice);
    }

    /**
     * 批量删除通知公告.
     *
     * @Param ids 通知公告标识列表
     * @Return 批量删除通知公告是否成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean removeBatch(List<Long> ids) {
        ids.forEach(id -> {
            MessageNotice messageNotice = this.getById(id);
            MessageInfo info = messageInfoRepository.selectById(messageNotice.getMsgId());
            if (Objects.equals(info.getStatus(), 0)) {
                messageInfoRepository.deleteById(messageNotice.getMsgId());
                messageNotice.setEffectiveStatus(1);
                updateById(messageNotice);
            }
        });
        return true;
    }

    /**
     * 编辑通知公告信息.
     *
     * @Param param 通知公告信息
     * @Return 编辑通知公告是否成功
     */
    @Override
    public Boolean edit(Long id, MessageNoticeParam param) {
        MessageNotice messageNotice = this.getById(id);
        AssertUtils.notNull(messageNotice, SystemResultCode.RESULT_DATA_NONE.message());
        MessageNotice editParam = BeanUtils.convertTo(param, MessageNotice::new);
        editParam.setEffectiveStatus(0);
        MessageInfo messageInfo = messageInfoRepository.selectById(messageNotice.getMsgId());
        String receiverId = messageInfo.getReceiverId();
        BeanUtils.copyProperties(param, messageInfo);
        messageInfo.setId(messageNotice.getMsgId());
        messageInfo.setStatus(param.getStatus());
        if (messageInfo.getStatus() != 0) {
            if (StringUtils.isNotEmpty(param.getReceiverId())) {
                messageInfo.setReceiverId(param.getReceiverId());
            } else {
                messageInfo.setReceiverId(receiverId);
            }
        }
        if (StringUtils.isEmpty(messageInfo.getReceiverId())){
            messageUserRepository.delete(Wrappers.<MessageUser>lambdaQuery().eq(MessageUser::getMsgId,messageNotice.getMsgId()));
        }
        messageInfo.setContent(SimpleHtmlUtil.transform(messageInfo.getContent()));
        messageInfo.setPublishTime(param.getPublishTime() != null ? param.getPublishTime() : new Date());
        messageInfoRepository.updateById(messageInfo);
        if (messageInfo.getStatus() != 0 &&StringUtils.isNotEmpty(messageInfo.getReceiverId())) {
            messageUserInsert(messageInfo, messageInfo.getUpdatorId());
        }
        /**
         * 保护不可编辑字段
         */
        return updateById(editParam);
    }

    /**
     * 撤销通知公告信息
     *
     * @param id 消息标识
     * @return
     */
    @Override
    public Boolean revocation(Long id) {
        MessageNotice messageNotice = this.getById(id);
        AssertUtils.notNull(messageNotice, SystemResultCode.RESULT_DATA_NONE.message());
        MessageInfo messageInfo = new MessageInfo();
        messageInfo.setId(messageNotice.getMsgId());
        messageInfo.setStatus(2);
        messageInfo.setPublishTime(null);
        MessageNotice notice = new MessageNotice();
        notice.setId(messageNotice.getId());
        notice.setEffectiveStatus(1);
        updateById(notice);
        messageUserRepository.delete(Wrappers.<MessageUser>lambdaQuery().eq(MessageUser::getMsgId,messageNotice.getMsgId()));
        return messageInfoRepository.updateById(messageInfo) > 0;
    }

    /**
     * 批量编辑通知公告信息.
     *
     * @Param params 通知公告信息列表
     * @Return 批量编辑通知公告是否成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean editBatch(List<MessageNoticeParam> params) {
        List<MessageNotice> messageNotices = BeanUtils.convertListTo(params, MessageNotice::new);
        return updateBatchById(messageNotices);
    }

}
