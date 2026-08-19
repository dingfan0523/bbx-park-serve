package com.cgnpc.bbxpark.message.api;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.bbxpark.common.web.CudResult;
import com.cgnpc.bbxpark.message.dto.req.MessageUserPageParam;
import com.cgnpc.bbxpark.message.dto.req.UnreadParam;
import com.cgnpc.bbxpark.message.dto.resp.MessageUserModel;
import com.cgnpc.bbxpark.message.dto.resp.UnreadModel;
import com.cgnpc.bbxpark.message.service.IMessageUserService;
import com.cgnpc.mobile.annotation.RequiredToken;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 会议签到控制类
 *
 * @author dingfan
 * @version 1.0
 * @date 2024/9/25 10:04
 */
@RestController
@RequestMapping("/api/message/user")
@Api(tags = "首页业务-移动端-消息服务")
public class ApiMessageUserController {
    @Autowired
    private IMessageUserService messageUserService;

    /**
     * 获取用户消息信息.
     */
    @ApiOperation(value = "获取用户消息信息")
    @GetMapping(value = "/detail/{id}")
    @RequiredToken
    public CudResult<MessageUserModel> detail(@PathVariable Long id) {
        return CudResult.success(messageUserService.detail(id));
    }

    /**
     * 获取用户消息列表(分页).
     */
    @ApiOperation(value = "获取用户消息列表(分页)")
    @PostMapping(value = "/pageZy")
    @RequiredToken
    public CudResult<IPage<MessageUserModel>> pageZy(@RequestBody MessageUserPageParam param) {
        return CudResult.success(messageUserService.pageZy(param));
    }

    /**
     * 移动端-查询指定类型的未读消息数量
     */
    @ApiOperation(value = "查询安全通知的未读消息数量")
    @PostMapping(value = "/unread")
    @RequiredToken
    public CudResult<Integer> count() {
        return CudResult.success(messageUserService.count());
    }

    /**
     * 移动端未读数量.
     */
    @ApiOperation(value = "发送消息后，获取提示的未读数量")
    @PostMapping(value = "/appUnread")
    @RequiredToken
    public CudResult<List<UnreadModel>> appUnread(@RequestBody UnreadParam param) {
        return CudResult.success(messageUserService.appUnread(param));
    }

    @ApiOperation(value = "根据消息类型获取用户消息信息")
    @PostMapping(value = "/findByTypeMessage")
    @RequiredToken
    public CudResult<List<MessageUserModel>> findByTypeMessage(@RequestBody MessageUserPageParam param) {
        return CudResult.success(messageUserService.findByTypeMessage(param));
    }

    /**
     * 批量已读用户消息.
     */
    @ApiOperation(value = "批量已读用户消息")
    @PostMapping(value = "/batchRead")
    @RequiredToken
    public CudResult<Boolean> batchRead(@RequestBody List<Long> ids) {
        return CudResult.success(messageUserService.batchRead(ids));
    }
}
