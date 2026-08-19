
package com.cgnpc.bbxpark.message.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.bbxpark.common.constant.Constant;
import com.cgnpc.bbxpark.common.web.CudResult;
import com.cgnpc.bbxpark.message.dto.req.*;
import com.cgnpc.bbxpark.message.dto.resp.MessageUserModel;
import com.cgnpc.bbxpark.message.dto.resp.UnreadModel;
import com.cgnpc.bbxpark.message.service.IMessageUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户消息服务控制类
 */
@RestController
@RequestMapping(Constant.BASE_PATH + "/message/user")
@Api(tags = "BBX-用户消息")
public class MessageUserController {

    /**
     * Logger.
     */
    private final static Logger LOGGER = LoggerFactory.getLogger(MessageUserController.class);

    /**
     * 用户消息服务接口.
     */
    @Autowired
    private IMessageUserService messageUserService;

    /**
     * 获取用户消息信息.
     */
    @ApiOperation(value = "获取用户消息信息")
    @GetMapping(value = "/detail/{id}")
    public CudResult<MessageUserModel> detail(@PathVariable Long id) {
        return CudResult.success(messageUserService.detail(id));
    }

    /**
     * 获取用户消息列表(分页).
     */
    @ApiOperation(value = "获取用户消息列表(分页)")
    @PostMapping(value = "/page")
    public CudResult<IPage<MessageUserModel>> page(@RequestBody MessageUserPageParam param) {
            return CudResult.success(messageUserService.page(param));
    }

    @ApiOperation(value = "消息盒子(分页)")
    @PostMapping(value = "/boxPage")
    public CudResult<IPage<MessageUserModel>> boxPage(@RequestBody MessageUserPageParam param) {
            return CudResult.success(messageUserService.boxPage(param));
    }

    /**
     * 获取用户消息列表.
     */
    @ApiOperation(value = "获取用户消息列表")
    @PostMapping(value = "/list")
    public CudResult<List<MessageUserModel>> list(@RequestBody MessageUserListParam param) {
            return CudResult.success(messageUserService.list(param));
    }

    /**
     * 新增用户消息.
     */
    @ApiOperation(value = "新增用户消息")
    @PostMapping(value = "/add")
    public CudResult<Boolean> add(@Validated @RequestBody MessageUserParam param) {
            return CudResult.success(messageUserService.add(param));

    }

    /**
     * 批量新增用户消息.
     */
    @ApiOperation(value = "批量新增用户消息")
    @PostMapping(value = "/add/batch")
    public CudResult<Boolean> addBatch(@Validated @RequestBody List<MessageUserParam> params) {
            return CudResult.success(messageUserService.addBatch(params));
    }


    /**
     * 删除用户消息.
     */
    @ApiOperation(value = "删除用户消息")
    @GetMapping(value = "/remove/{id}")
    public CudResult<Boolean> remove(@PathVariable Long id) {
            return CudResult.success(messageUserService.remove(id));
    }
    /**
     * 清空用户消息.
     */
    @ApiOperation(value = "清空用户消息")
    @GetMapping(value = "/empty")
    public CudResult<Boolean> empty() {
            return CudResult.success(messageUserService.empty());
    }

    /**
     * 未读数量.
     */
    @ApiOperation(value = "发送消息后，获取提示的未读数量")
    @PostMapping(value = "/unread")
    public CudResult<Integer> unread(@RequestBody UnreadParam param) {
            return CudResult.success(messageUserService.unread(param));
    }

    /**
     * 用户签收.
     */
    @ApiOperation(value = "用户签收")
    @GetMapping(value = "/confirm/{id}/{status}")
    public CudResult<Boolean> confirm(@PathVariable Long id,@PathVariable Integer status) {
            return CudResult.success(messageUserService.confirm(id,status));
    }

    /**
     * 用户点赞.
     */
    @ApiOperation(value = "用户点赞")
    @GetMapping(value = "/like/{id}/{status}")
    public CudResult<Boolean> like(@PathVariable Long id,@PathVariable Integer status) {
            return CudResult.success(messageUserService.like(id,status));
    }

    /**
     * 用户回复.
     */
    @ApiOperation(value = "用户回复")
    @PostMapping(value = "/reply")
    public CudResult<Boolean> reply(@RequestBody MessageReplyParam param) {
            return CudResult.success(messageUserService.reply(param));
    }

    /**
     * 用户收藏.
     */
    @ApiOperation(value = "用户收藏")
    @GetMapping(value = "/collect/{id}/{status}")
    public CudResult<Boolean> collect(@PathVariable Long id,@PathVariable Integer status) {
            return CudResult.success(messageUserService.collect(id,status));

    }

    /**
     * 批量删除用户消息.
     */
    @ApiOperation(value = "批量删除用户消息")
    @PostMapping(value = "/remove/batch")
    public CudResult<Boolean> removeBatch(@RequestBody List<Long> ids) {
            return CudResult.success(messageUserService.removeBatch(ids));
    }
    /**
     * 批量删除用户消息.
     */
    @ApiOperation(value = "批量删除用户消息")
    @PostMapping(value = "/remove/userBatch")
    public CudResult<Boolean> userBatch(@RequestBody MessageUserRemoveParam removeParam) {
        return CudResult.success(messageUserService.removeUserBatch(removeParam));
    }

    /**
     * 编辑用户消息.
     */
    @ApiOperation(value = "编辑用户消息")
    @PostMapping(value = "/edit/{id}")
    public CudResult<Boolean> edit(@PathVariable Long id, @RequestBody MessageUserParam param) {
            return CudResult.success(messageUserService.edit(id, param));
    }

    /**
     * 批量编辑用户消息.
     */
    @ApiOperation(value = "批量编辑用户消息")
    @PostMapping(value = "/edit/batch")
    public CudResult<Boolean> editBatch(@Validated @RequestBody List<MessageUserParam> params) {
            return CudResult.success(messageUserService.editBatch(params));
    }

    /**
     * 启用用户消息.
     */
    @ApiOperation(value = "启用用户消息")
    @PostMapping(value = "/{id}/enable")
    public CudResult<Boolean> enable(@PathVariable Long id) {
            return CudResult.success(messageUserService.enable(id));
    }

    /**
     * 批量启用用户消息.
     */
    @ApiOperation(value = "批量启用用户消息")
    @PostMapping(value = "/enable/batch")
    public CudResult<Boolean> enableBatch(@RequestBody List<Long> ids) {
            return CudResult.success(messageUserService.enableBatch(ids));

    }

    /**
     * 禁用用户消息.
     */
    @ApiOperation(value = "禁用用户消息")
    @PostMapping(value = "/{id}/disable")
    public CudResult<Boolean> disable(@PathVariable Long id) {
            return CudResult.success(messageUserService.disable(id));

    }

    /**
     * 批量禁用用户消息.
     */
    @ApiOperation(value = "批量禁用用户消息")
    @PostMapping(value = "/disable/batch")
    public CudResult<Boolean> disableBatch(@RequestBody List<Long> ids) {
            return CudResult.success(messageUserService.disableBatch(ids));

    }

    @ApiOperation(value = "根据消息类型获取用户消息信息")
    @PostMapping(value = "/findByTypeMessage")
    public CudResult<List<MessageUserModel>> findByTypeMessage(@RequestBody MessageUserPageParam param) {
        return CudResult.success(messageUserService.findByTypeMessage(param));
    }

    /**
     * 获取用户消息列表(分页).
     */
    @ApiOperation(value = "获取用户消息列表(分页)")
    @PostMapping(value = "/pageZy")
    public CudResult<IPage<MessageUserModel>> pageZy(@RequestBody MessageUserPageParam param) {
        return CudResult.success(messageUserService.pageZy(param));
    }

    /**
     * 移动端未读数量.
     */
    @ApiOperation(value = "发送消息后，获取提示的未读数量")
    @PostMapping(value = "/appUnread")
    public CudResult<List<UnreadModel>> appUnread(@RequestBody UnreadParam param) {
        return CudResult.success(messageUserService.appUnread(param));
    }

    /**
     * 批量已读用户消息.
     */
    @ApiOperation(value = "批量已读用户消息")
    @PostMapping(value = "/batchRead")
    public CudResult<Boolean> batchRead(@RequestBody List<Long> ids) {
        return CudResult.success(messageUserService.batchRead(ids));
    }


}
