package com.cgnpc.dingtalk.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.cgnpc.dingtalk.enums.MsgTypeEnum;
import com.cgnpc.dingtalk.model.DingTalkWorkNoticeInputVO;
import com.cgnpc.dingtalk.model.WorkNotice;
import com.cgnpc.pro.api.ICudDingTalkService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author P629041
 * @Description 请勿删除 该类用于实现表单鹭钉通知
 * @Date 14:20 2024/1/22
 **/
@Slf4j
@Service
public class FormDingTalkService implements ICudDingTalkService {

    @Resource
    private WarningMsgService warningMsgService;


    /**
     * @Author P629041
     * @Description 鹭钉发送
     * @Date 14:32 2024/1/22
     * @Param [userIdList 接收人用户列表, content 发送内容, title 发送标题,type 发送类型：此类型用于区分业务表单跟智能表单 0为业务表单 1为智能表单]
     * @return java.lang.Boolean
     **/
    @Override
    public Boolean send(List<String> userIdList,String content,String title,Integer sendType) {
        DingTalkWorkNoticeInputVO dingTalkWorkNoticeInputVO = new DingTalkWorkNoticeInputVO();
        dingTalkWorkNoticeInputVO.setUserIdList(userIdList);
        dingTalkWorkNoticeInputVO.setWorkNotice(WorkNotice.builder()
                .msgType(MsgTypeEnum.LINK.getMsgType())
                .url("https://cuddemo4-t/#/smartFormTask")
                .title(StrUtil.format("{}({})",title,DateUtil.now()))
                .content(content).build());
        dingTalkWorkNoticeInputVO.setToAllUser(false);
        this.warningMsgService.sendWorkNotice(dingTalkWorkNoticeInputVO);
        return Boolean.TRUE;
    }
}
