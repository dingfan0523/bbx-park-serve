package com.cgnpc.dingtalk.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.cgnpc.cud.workbench.manage.dingtalk.api.DingTalkSendService;
import com.cgnpc.cud.workbench.manage.dingtalk.model.DingTalkSendDTO;
import com.cgnpc.dingtalk.enums.MsgTypeEnum;
import com.cgnpc.dingtalk.model.DingTalkWorkNoticeInputVO;
import com.cgnpc.dingtalk.model.WorkNotice;
import com.cgnpc.mobile.config.DtalkProperties;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @Description 请勿删除 该类用于实现流程鹭钉通知
 * @Author P629041
 * @Date 2023/10/31 10:53
 */
@Slf4j
@Service
public class ProcessDingTalkService implements DingTalkSendService {

    @Resource
    private WarningMsgService warningMsgService;

    @Autowired(required = false)
    private DtalkProperties dtalkProperties;

    /**
     * @Author P629041
     * @Description 重写发送方法
     * @Date 10:54 2023/10/31
     * @Param [dingTalkSendDTO]
     * @return java.lang.Boolean
     **/
    @Override
    public Boolean send(DingTalkSendDTO dingTalkSendDTO) {
        log.info("鹭钉发送请求参数：{}", JSON.toJSONString(dingTalkSendDTO));
        DingTalkWorkNoticeInputVO dingTalkWorkNoticeInputVO = new DingTalkWorkNoticeInputVO();
        dingTalkWorkNoticeInputVO.setUserIdList(dingTalkSendDTO.getUserIdList());
        dingTalkWorkNoticeInputVO.setWorkNotice(WorkNotice.builder()
                .msgType(MsgTypeEnum.LINK.getMsgType())
                .url(dtalkProperties.getMessageUrl())
                .title(StrUtil.format("{}({})",dingTalkSendDTO.getTitle(),DateUtil.now()))
                .content(dingTalkSendDTO.getContent()).build());
        dingTalkWorkNoticeInputVO.setToAllUser(false);
        this.warningMsgService.sendWorkNotice(dingTalkWorkNoticeInputVO);
        return Boolean.TRUE;
    }
}
