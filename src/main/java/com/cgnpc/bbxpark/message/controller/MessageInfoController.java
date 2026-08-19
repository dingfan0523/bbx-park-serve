
package com.cgnpc.bbxpark.message.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.bbxpark.common.constant.Constant;
import com.cgnpc.bbxpark.common.web.CudResult;
import com.cgnpc.bbxpark.message.dto.req.MessageInfoListParam;
import com.cgnpc.bbxpark.message.dto.req.MessageInfoPageParam;
import com.cgnpc.bbxpark.message.dto.req.MessageInfoParam;
import com.cgnpc.bbxpark.message.dto.resp.MessageInfoModel;
import com.cgnpc.bbxpark.message.service.IMessageInfoService;
import com.cgnpc.bbxpark.space.dto.model.UserInfoModel;
import com.cgnpc.bbxpark.space.dto.param.UserPageParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 消息内容服务控制类
 */
@RestController
@RequestMapping(Constant.BASE_PATH + "/message/info")
@Api(tags = "BBX-消息内容")
public class MessageInfoController {

    /**
     * Logger.
     */
    private final static Logger LOGGER = LoggerFactory.getLogger(MessageInfoController.class);

    /**
     * 消息内容服务接口.
     */
    @Autowired
    private IMessageInfoService messageInfoService;

    @ApiOperation(value = "公告、安全类型-已选择人员")
    @PostMapping(value = "/message_user_page")
    public CudResult<IPage<UserInfoModel>> message_user_page(@RequestBody UserPageParam param) {
        return CudResult.success(messageInfoService.message_user_page(param));
    }
    /**
     * 获取消息内容信息.
     */
    @ApiOperation(value = "获取消息内容信息")
    @GetMapping(value = "/detail/{id}/{userId}")
    public CudResult<MessageInfoModel> detail(@PathVariable Long id, @PathVariable String userId) {
        return CudResult.success(messageInfoService.detail(id,userId));
    }

    /**
     * 获取消息内容列表(分页).
     */
    @ApiOperation(value = "获取消息内容列表(分页)")
    @PostMapping(value = "/page")
    public CudResult<IPage<MessageInfoModel>> page(@RequestBody MessageInfoPageParam param) {
        return CudResult.success(messageInfoService.page(param));

    }

    /**
     * 获取消息内容列表.
     */
    @ApiOperation(value = "获取消息内容列表")
    @PostMapping(value = "/list")
    public CudResult<List<MessageInfoModel>> list(@RequestBody MessageInfoListParam param) {
            return CudResult.success(messageInfoService.list(param));
    }

    /**
     * 新增消息内容.
     */
    @ApiOperation(value = "新增消息内容")
    @PostMapping(value = "/add")
    public CudResult<Boolean> add(@Validated @RequestBody MessageInfoParam param) {
            return CudResult.success(messageInfoService.add(param));
    }

    /**
     * 批量新增消息内容.
     */
    @ApiOperation(value = "批量新增消息内容")
    @PostMapping(value = "/add/batch")
    public CudResult<Boolean> addBatch(@Validated @RequestBody List<MessageInfoParam> params) {
            return CudResult.success(messageInfoService.addBatch(params));

    }

    /**
     * 删除消息内容.
     */
    @ApiOperation(value = "删除消息内容")
    @GetMapping(value = "/remove/{id}")
    public CudResult<Boolean> remove(@PathVariable Long id) {
            return CudResult.success(messageInfoService.remove(id));
    }

    /**
     * 发布
     * @return 是否成功
     */
    @ApiOperation(value = "发布消息")
    @GetMapping(value = "/announce/{id}/{typeId}")
    public CudResult<Boolean> publishNotify(@PathVariable Long id,@PathVariable String userId,@PathVariable Long typeId) {
        return CudResult.success(messageInfoService.announce(id,typeId));
    }


    @ApiOperation(value = "撤销消息内容")
    @GetMapping(value = "/revocation/{id}")
    public CudResult<Boolean> revocation(@PathVariable Long id) {
            return CudResult.success(messageInfoService.revocation(id));
    }
    /**
     * 批量删除消息内容.
     */
    @ApiOperation(value = "批量删除消息内容")
    @PostMapping(value = "/remove/batch")
    public CudResult<Boolean> removeBatch(@RequestBody List<Long> ids) {
            return CudResult.success(messageInfoService.removeBatch(ids));
    }

    /**
     * 编辑消息内容.
     */
    @ApiOperation(value = "编辑消息内容")
    @PostMapping(value = "/edit/{id}")
    public CudResult<Boolean> edit(@PathVariable Long id, @RequestBody MessageInfoParam param) {
            return CudResult.success(messageInfoService.edit(id, param));
    }

    /**
     * 批量编辑消息内容.
     */
    @ApiOperation(value = "批量编辑消息内容")
    @PostMapping(value = "/edit/batch")
    public CudResult<Boolean> editBatch(@Validated @RequestBody List<MessageInfoParam> params) {
            return CudResult.success(messageInfoService.editBatch(params));
    }

    /**
     * 启用消息内容.
     */
    @ApiOperation(value = "启用消息内容")
    @PostMapping(value = "/{id}/enable")
    public CudResult<Boolean> enable(@PathVariable Long id) {
            return CudResult.success(messageInfoService.enable(id));
    }

    /**
     * 批量启用消息内容.
     */
    @ApiOperation(value = "批量启用消息内容")
    @PostMapping(value = "/enable/batch")
    public CudResult<Boolean> enableBatch(@RequestBody List<Long> ids) {
            return CudResult.success(messageInfoService.enableBatch(ids));
    }

    /**
     * 禁用消息内容.
     */
    @ApiOperation(value = "禁用消息内容")
    @PostMapping(value = "/{id}/disable")
    public CudResult<Boolean> disable(@PathVariable Long id) {
            return CudResult.success(messageInfoService.disable(id));
    }

    /**
     * 批量禁用消息内容.
     */
    @ApiOperation(value = "批量禁用消息内容")
    @PostMapping(value = "/disable/batch")
    public CudResult<Boolean> disableBatch(@RequestBody List<Long> ids) {
            return CudResult.success(messageInfoService.disableBatch(ids));
    }
}
