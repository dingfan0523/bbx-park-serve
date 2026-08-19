package com.cgnpc.bbxpark.meeting.api;

import com.cgnpc.bbxpark.common.web.CudResult;
import com.cgnpc.bbxpark.meeting.dto.model.CardMessage;
import com.cgnpc.bbxpark.meeting.service.impl.CardService;
import com.cgnpc.mobile.annotation.RequiredToken;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author dingfan
 * @version 1.0
 * @date 2024/10/15 10:16
 */
@RestController
@RequestMapping("/api/card/app")
@Api(tags = "首页业务-移动端-卡片提醒服务")
public class AppCardController {
    @Resource
    private CardService cardService;

    /**
     * 移动端-查询卡片消息列表
     */
    @ApiOperation(value = "查询卡片消息列表")
    @PostMapping(value = "/list")
    @RequiredToken
    public CudResult<List<CardMessage>> list() {
        return CudResult.success(cardService.list());
    }
}
