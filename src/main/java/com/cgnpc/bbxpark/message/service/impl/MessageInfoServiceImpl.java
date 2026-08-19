
package com.cgnpc.bbxpark.message.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cgnpc.bbxpark.acl.uic.service.IUserApiService;
import com.cgnpc.bbxpark.common.constant.SystemResultCode;
import com.cgnpc.bbxpark.common.enums.Status;
import com.cgnpc.bbxpark.common.utils.*;
import com.cgnpc.bbxpark.message.domain.MessageInfo;
import com.cgnpc.bbxpark.message.domain.MessageNotice;
import com.cgnpc.bbxpark.message.domain.MessageUser;
import com.cgnpc.bbxpark.message.dto.req.MessageInfoListParam;
import com.cgnpc.bbxpark.message.dto.req.MessageInfoPageParam;
import com.cgnpc.bbxpark.message.dto.req.MessageInfoParam;
import com.cgnpc.bbxpark.message.dto.resp.MessageInfoModel;
import com.cgnpc.bbxpark.message.mapper.MessageInfoRepository;
import com.cgnpc.bbxpark.message.mapper.MessageUserRepository;
import com.cgnpc.bbxpark.message.service.IMessageInfoService;
import com.cgnpc.bbxpark.message.service.IMessageNoticeService;
import com.cgnpc.bbxpark.space.domain.TenantInfo;
import com.cgnpc.bbxpark.space.dto.model.UserInfoModel;
import com.cgnpc.bbxpark.space.dto.param.UserPageParam;
import com.cgnpc.bbxpark.space.service.ITenantInfoService;
import com.cgnpc.cud.core.exception.BaseException;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 消息内容服务实现
 */
@Service
public class MessageInfoServiceImpl extends ServiceImpl<MessageInfoRepository, MessageInfo> implements IMessageInfoService {
    /**
     * 注入repository.
     */
    @Autowired
    private MessageInfoRepository messageInfoRepository;

    @Autowired
    private MessageUserRepository messageUserRepository;

    @Autowired
    private IUserApiService userApiService;

    @Autowired
    private ITenantInfoService tenantInfoService;
    @Autowired
    private IMessageNoticeService messageNoticeService;



    /**
     * 根据消息内容标识获得消息内容详情信息.
     *
     * @Param [id] 消息内容标识
     * @Return 消息内容详情信息
     */
    @Override
    public MessageInfoModel detail(Long id, String userId) {
        MessageInfo messageInfo = this.getById(id);
        AssertUtils.notNull(messageInfo, SystemResultCode.RESULT_DATA_NONE.message());
        List objects = JSONObject.parseArray(messageInfo.getTemplateParam());
        MessageInfoModel messageInfoModel = BeanUtils.convertTo(messageInfo, MessageInfoModel::new);
        messageInfoModel.setTemplateParam(objects);
        messageNoticeService.messageLogic(messageInfo.getReceiverScope(), userId, messageInfo.getId());
        if (Objects.equals(messageInfo.getReceiverScope(), 1)) {
            UserInfoModel userInfo = userApiService.getByStaffNo(messageInfo.getReceiverId());
            if (ObjectUtil.isNotEmpty(userInfo)) {
                messageInfoModel.setNickName(userInfo.getUserName());
            }

        }
//		if (Objects.equals(messageInfo.getReceiverScope(),MessageNoticeExt.DEFAULT_4)) {
//			OrganizationInfo organizationInfo = organizationInfoRepository.get(Long.valueOf(messageInfo.getReceiverId()));
//			if (ObjectUtil.isNotEmpty(organizationInfo)){
//				messageInfoModel.setNickName(organizationInfo.getAbbreviationName());
//			}
//		}
        if (Objects.equals(messageInfo.getReceiverScope(), 7)) {
            TenantInfo tenantInfo = tenantInfoService.getById(Long.valueOf(messageInfo.getReceiverId()));
            if (ObjectUtil.isNotEmpty(tenantInfo)) {
                messageInfoModel.setNickName(tenantInfo.getName());
            }
        }
        return messageInfoModel;
    }

    /**
     * 获取消息内容列表(分页).
     *
     * @Param param 消息内容查询条件
     * @Return 消息内容信息列表（分页）
     */
    @Override
    public IPage<MessageInfoModel> page(MessageInfoPageParam param) {
        if (ObjectUtil.isEmpty(param.getStatus())) {
            param.setStatus(9);
        }
        //todo:筛选
        IPage<MessageInfo> result = this.page(new Page<>(param.getCurrent(), param.getSize()), Wrappers.<MessageInfo>lambdaQuery()
                .ne(param.getStatus() != null && param.getStatus() == 9, MessageInfo::getStatus, 3)
                .eq(param.getStatus() != null && param.getStatus() != 9, MessageInfo::getStatus, param.getStatus()));
        if (CollectionUtils.isEmpty(result.getRecords())) {
            return ConvertUtil.pageEmptyConvert(param.getCurrent(), param.getSize());
        }
        List<MessageInfoModel> list = result.getRecords().stream().map(messageInfo -> {
            MessageInfoModel model = BeanUtils.convertTo(messageInfo, MessageInfoModel::new);
            if (Objects.equals(messageInfo.getReceiverScope(), 1)) {
                UserInfoModel userInfo = userApiService.getByStaffNo(messageInfo.getReceiverId());
                if (ObjectUtil.isNotEmpty(userInfo)) {
                    model.setNickName(userInfo.getUserName());
                }
            }
//			if (Objects.equals(messageInfo.getReceiverScope(),MessageNoticeExt.DEFAULT_4)) {
//				OrganizationInfo organizationInfo = organizationInfoRepository.get(messageInfo.getReceiverId());
//				if (ObjectUtil.isNotEmpty(organizationInfo)){
//					messageInfo.setNickName(organizationInfo.getAbbreviationName());
//				}
//			}
            if (Objects.equals(messageInfo.getReceiverScope(), 7)) {
                TenantInfo tenantInfo = tenantInfoService.getById(messageInfo.getReceiverId());
                if (ObjectUtil.isNotEmpty(tenantInfo)) {
                    model.setNickName(tenantInfo.getName());
                }
            }
            return model;
        }).collect(Collectors.toList());
        return ConvertUtil.pageConvert(result, list);
    }


    /**
     * 获取消息内容列表.
     *
     * @Param param 消息内容查询条件
     * @Return 消息内容信息列表
     */
    @Override
    @SneakyThrows
    public List<MessageInfoModel> list(MessageInfoListParam param) {
        //todo:筛选
        List<MessageInfo> result = list(Wrappers.<MessageInfo>lambdaQuery()
                .ne(param.getStatus() != null && param.getStatus() == 9, MessageInfo::getStatus, 3)
                .eq(param.getStatus() != null && param.getStatus() != 9, MessageInfo::getStatus, param.getStatus()));
        return BeanUtils.convertListTo(result,MessageInfoModel::new);
    }

    /**
     * 新增消息内容.
     *
     * @Param param 消息内容信息
     * @Return 新增消息内容是否成功
     */
    @Override
    public Boolean add(MessageInfoParam param) {
        MessageInfo messageInfo = BeanUtils.convertTo(param, MessageInfo::new);
        messageInfo.setId(null);
        messageInfo.setStatus(0);
        messageInfo.setTemplateParam(JSONObject.toJSONString(param.getTemplateParam()));
        return save(messageInfo);
    }

    /**
     * 批量新增消息内容.
     *
     * @Param params 消息内容信息列表
     * @Return 批量新增消息内容是否成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean addBatch(List<MessageInfoParam> params) {
        List<MessageInfo> messageInfos = BeanUtils.convertListTo(params, MessageInfo::new);
        return saveBatch(messageInfos);
    }

    /**
     * 删除消息内容.
     *
     * @Param id 消息内容标识
     * @Return 删除消息内容是否成功
     */
    @Override
    public Boolean remove(Long id) {
        MessageInfo messageInfo = this.getById(id);
        AssertUtils.notNull(messageInfo, "数据已更新，请刷新页面！");
        messageInfo.setStatus(3);
        updateById(messageInfo);
        messageUserRepository.delete(Wrappers.<MessageUser>lambdaQuery().eq(MessageUser::getMsgId,id));
        return true;
    }

    /**
     * 发布
     *
     * @return 是否成功
     */
    @Transactional
    @Override
    public synchronized Boolean announce(Long id, Long typeId) {
        MessageInfo messageInfo = this.getById(id);
        AssertUtils.notNull(messageInfo, SystemResultCode.RESULT_DATA_NONE.message());
        AssertUtils.isFalse(!Objects.equals(messageInfo.getStatus(), 0),"消息处于未发布状态才能发布！");
        if (Objects.equals(typeId, messageInfo.getTypeId())) {
            List<MessageNotice> messageNotices = messageNoticeService.list(Wrappers.<MessageNotice>lambdaQuery().eq(MessageNotice::getMsgId,id));
            messageNoticeService.publishNotify(messageNotices.get(0).getId());
        } else if (ObjectUtil.isNotEmpty(messageInfo.getTemplateId())) {
//            MessageTemplate messageTemplate = messageTemplateRepository.get(messageInfo.getTemplateId());
//            if (Objects.equals(messageTemplate.getStatus(), MessageTemplateExt.DEFAULT_1)) {
//                throw GenericException.fail("模板已被禁用，无法发送消息！");
//            }
//            List<Map> templateParams = JSONObject.parseArray(messageInfo.getTemplateParam(), Map.class);
//            Map<Object, Object> map = new HashMap<>();
//            templateParams.forEach(a -> {
//                map.put(a.get("code"), a.get("value"));
//            });
//            String templateContent = messageTemplate.getContent().replace("#", "");
//            String messageContent = templateContent.replace("$", "");
//            String content = StrUtil.format(messageContent, map);
//            messageInfo.setContent(content);
//            messageInfo.setStatus(MessageInfoExt.DEFAULT_1);
//            messageInfo.setPublishTime(new Date());
//            messageInfo.setPublisher(userid);
//            update(messageInfo);
        } else {
            throw new BaseException("无对应模板信息，发布失败！");
        }
        messageNoticeService.messageUserInsert(messageInfo, userApiService.getCurrentStaffNo());
        return true;
    }

    /**
     * 批量删除消息内容.
     *
     * @Param ids 消息内容标识列表
     * @Return 批量删除消息内容是否成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean removeBatch(List<Long> ids) {
        return removeByIds(ids);
    }

    /**
     * 编辑消息内容信息.
     *
     * @Param param 消息内容信息
     * @Return 编辑消息内容是否成功
     */
    @Override
    public Boolean edit(Long id, MessageInfoParam param) {
        MessageInfo messageInfo = this.getById(id);
        if (Objects.equals(messageInfo.getStatus(), 2) || Objects.equals(messageInfo.getStatus(), 0)) {
            AssertUtils.notNull(messageInfo, SystemResultCode.RESULT_DATA_NONE.message());
            MessageInfo editParam = BeanUtils.convertTo(param, MessageInfo::new);
            if (Objects.equals(messageInfo.getStatus(), 2)) {
                editParam.setStatus(0);
            }
            /**
             * 保护不可编辑字段
             */

            editParam.setTemplateParam(JSONObject.toJSONString(param.getTemplateParam()));
            return updateById(editParam);
        }
        throw new BaseException("消息处于未发布或已撤销状态才能编辑！");
    }

    /**
     * 批量编辑消息内容信息.
     *
     * @Param params 消息内容信息列表
     * @Return 批量编辑消息内容是否成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean editBatch(List<MessageInfoParam> params) {
        List<MessageInfo> messageInfos = BeanUtils.convertListTo(params, MessageInfo::new);
        return updateBatchById(messageInfos);
    }

    /**
     * 启用消息内容.
     *
     * @Param id 消息内容标识
     * @Return 启用消息内容是否成功
     */
    @Override
    public Boolean enable(Long id) {
        MessageInfo messageInfo = new MessageInfo();
        messageInfo.setId(id);
        messageInfo.setStatus(Status.enabled.getKey());
        return updateById(messageInfo);
    }

    /**
     * 撤销
     *
     * @param id 消息内容标识
     * @return 是否成功
     */
    @Override
    public Boolean revocation(Long id) {
        MessageInfo messageInfo = this.getById(id);
        AssertUtils.notNull(messageInfo, SystemResultCode.RESULT_DATA_NONE.message());
        messageInfo.setStatus(2);
        messageInfo.setPublishTime(null);
        updateById(messageInfo);
        messageUserRepository.delete(Wrappers.<MessageUser>lambdaQuery().eq(MessageUser::getMsgId,id));
        return true;
    }

    /**
     * 批量启用消息内容信息.
     *
     * @Param ids 消息内容标识列表
     * @Return 批量启用消息内容是否成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean enableBatch(List<Long> ids) {
        List<MessageInfo> messageInfos = ids.stream().map(id -> {
            MessageInfo messageInfo = new MessageInfo();
            messageInfo.setId(id);
            messageInfo.setStatus( Status.enabled.getKey());
            return messageInfo;
        }).collect(Collectors.toList());
        return updateBatchById(messageInfos);
    }

    /**
     * 禁用消息内容.
     *
     * @Param id 消息内容标识
     * @Return 禁用消息内容是否成功
     */
    @Override
    public Boolean disable(Long id) {
        MessageInfo messageInfo = new MessageInfo();
        messageInfo.setId(id);
        messageInfo.setStatus( Status.disabled.getKey());
        return updateById(messageInfo);
    }

    /**
     * 批量禁用消息内容信息.
     *
     * @Param ids 消息内容标识列表
     * @Return 批量禁用消息内容是否成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean disableBatch(List<Long> ids) {
        List<MessageInfo> messageInfos = ids.stream().map(id -> {
            MessageInfo messageInfo = new MessageInfo();
            messageInfo.setId(id);
            messageInfo.setStatus( Status.disabled.getKey());
            return messageInfo;
        }).collect(Collectors.toList());
        return updateBatchById(messageInfos);
    }

    @Override
    public IPage<UserInfoModel> message_user_page(UserPageParam param) {
        Long messageId = param.getMessageId();
        MessageInfo info = messageInfoRepository.selectById(messageId);
        AssertUtils.notNull(info, SystemResultCode.RESULT_DATA_NONE.message());
        List<String> userIdList = getMessageUserId(info);
        if (CollectionUtils.isEmpty(userIdList)) {
            return ConvertUtil.pageEmptyConvert(param.getCurrent(),param.getSize());
        }
        long start = param.getCurrent() * param.getSize() - param.getSize();
        long end = param.getCurrent() * param.getSize();
        end = end > userIdList.size() ? userIdList.size() : end;
        List<String> userIds = userIdList.subList(Integer.parseInt(Long.toString(start)),Integer.parseInt(Long.toString(end)));
        List<UserInfoModel> staffs = userApiService.getByStaffNos(userIds);
        List<UserInfoModel> users = staffs.stream().map(staff->{
            UserInfoModel user = new UserInfoModel();
            user.setId(staff.getStaffNo());
            user.setStaffNo(staff.getStaffNo());
            user.setUserName(staff.getUserName());
            user.setDepartmentId(staff.getDepartmentId());
            user.setDepartmentName(staff.getDepartmentName());
            return user;
        }).collect(Collectors.toList());
        return ConvertUtil.pageConvert(param.getCurrent(),userIdList.size(),param.getSize(),users);
    }

    /**
     * 获取消息选择的所有用户id
     *
     * @param info
     * @return
     */
    private List<String> getMessageUserId(MessageInfo info) {
        List<String> userIdList = new ArrayList<>();
        if (Integer.valueOf("0").equals(info.getStatus())) {
            String receiverId = info.getReceiverId();
            if (StrUtil.isNotEmpty(receiverId)) {
                // 使用逗号作为分隔符将字符串分割成数组
                String[] idsArray = receiverId.split(",");
                userIdList.addAll(Arrays.asList(idsArray));
            }
        } else {
            List<MessageUser> list = messageUserRepository.selectList(Wrappers.<MessageUser>lambdaQuery().eq(MessageUser::getMsgId,info.getId()));
            userIdList = list.stream().map(MessageUser::getUserId).collect(Collectors.toList());
        }
        return userIdList;
    }
}
