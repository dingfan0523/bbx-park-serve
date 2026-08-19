
package com.cgnpc.bbxpark.message.controller;

import com.cgnpc.bbxpark.common.constant.Constant;
import com.cgnpc.bbxpark.common.web.CudResult;
import com.cgnpc.bbxpark.message.dto.req.MessageReSendParam;
import com.cgnpc.bbxpark.message.dto.req.MessageSendParam;
import com.cgnpc.bbxpark.message.service.IMessageSendService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/***
 * @Description 消息发送服务控制类
 * @author huangyongtao
 * @date 2024/10/25 14:55
 */
@RestController
@RequestMapping(Constant.BASE_PATH + "/msg")
@Api(tags = "BBX-消息中心-pc端-消息发送")
public class MessageSendController {

    /**
     * 消息发送服务接口.
     */
    @Resource
    private IMessageSendService messageSendService;

    /**
     * 根据模板发送消息.
     */
    @ApiOperation(value = "根据模板发送消息")
    @PostMapping(value = "/sendByTemplate")
    public CudResult<Boolean> sendByTemplate(@RequestBody MessageSendParam param) {
        return CudResult.success(messageSendService.sendByTemplate(param));
    }

    /**
     * 根据日志id重新发送消息.
     */
    @ApiOperation(value = "根据日志id重新发送消息")
    @PostMapping(value = "/sendById")
    public CudResult<Boolean> sendById(@RequestBody MessageReSendParam param) {
        return CudResult.success(messageSendService.sendById(param.getId()));
    }
}
