package com.cgnpc.dingtalk.service;

import com.alibaba.fastjson.JSON;
import com.cgnpc.cud.psc.client.sdk.config.WfAuthConfig;
import com.cgnpc.dingtalk.model.DingTalkWorkNoticeInputVO;
import com.cgnpc.mobile.config.DtalkProperties;
import com.cgnpc.psc.client.feign.PscTaskFeign;
import com.cgnpc.psc.client.utils.FeignClientUtil;
import com.cgnpc.psc.core.model.request.ServiceRequest;
import com.cgnpc.psc.core.model.result.ServiceResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


@Service
@Slf4j
@SuppressWarnings("all")
public class AsyncWarningMsgService {

    @Autowired
    private WfAuthConfig wfAuthConfig;

    @Autowired
    private DtalkProperties dtalkProperties;

    @Autowired
    private WarningMsgService warningMsgService;

    private PscTaskFeign getFeignClient(){
        return FeignClientUtil.createClient(PscTaskFeign.class, wfAuthConfig.getAuthInfo().getPscUrl());
    }

    @Async
    public void test() throws InterruptedException {
        ServiceResponse resp = getFeignClient().isTaskProcessed(new ServiceRequest<>(), wfAuthConfig.getAuthInfo().getAccessToken());
        System.out.println(JSON.toJSONString(resp));
    }


    /**
     * 异步发送钉钉消息
     */
    @Async
    public void sendWorkNotice(DingTalkWorkNoticeInputVO dingTalkWorkNoticeInputVO){
        dingTalkWorkNoticeInputVO.setToAllUser(false);
        long taskId = warningMsgService.sendWorkNotice(dingTalkWorkNoticeInputVO);
        System.out.println(taskId);
    }


}
