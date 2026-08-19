
package com.cgnpc.bbxpark.message.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.bbxpark.common.constant.Constant;
import com.cgnpc.bbxpark.common.constant.InsertGroup;
import com.cgnpc.bbxpark.common.constant.UpdateGroup;
import com.cgnpc.bbxpark.common.web.CudResult;
import com.cgnpc.bbxpark.message.dto.req.MessageNoticeListParam;
import com.cgnpc.bbxpark.message.dto.req.MessageNoticePageParam;
import com.cgnpc.bbxpark.message.dto.req.MessageNoticeParam;
import com.cgnpc.bbxpark.message.dto.resp.MessageNoticeModel;
import com.cgnpc.bbxpark.message.service.IMessageNoticeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 通知公告服务控制类
 */
@RestController
@RequestMapping(Constant.BASE_PATH + "/message/notice")
@Api(tags = "BBX-通知公告")
public class MessageNoticeController {

    /**
     * Logger.
     */
    private final static Logger LOGGER = LoggerFactory.getLogger(MessageNoticeController.class);

    /**
     * 通知公告服务接口.
     */
    @Autowired
    private IMessageNoticeService messageNoticeService;

    /**
     * 获取通知公告信息.
     */
    @ApiOperation(value = "获取通知公告信息")
    @GetMapping(value = "/detail/{id}")
    public CudResult<MessageNoticeModel> detail(@PathVariable Long id) {
            return CudResult.success(messageNoticeService.detail(id));
    }

    /**
     * 获取通知公告列表(分页).
     */
    @ApiOperation(value = "获取通知公告列表(分页)")
    @PostMapping(value = "/page")
    public CudResult<IPage<MessageNoticeModel>> page(@RequestBody MessageNoticePageParam param) {
            return CudResult.success(messageNoticeService.page(param));

    }

    /**
     * 获取通知公告列表.
     */
    @ApiOperation(value = "获取通知公告列表")
    @PostMapping(value = "/list")
    public CudResult<List<MessageNoticeModel>> list(@RequestBody MessageNoticeListParam param) {
            return CudResult.success(messageNoticeService.list(param));

    }

    /**
     * 新增通知公告.
     */
    @ApiOperation(value = "新增通知公告")
    @PostMapping(value = "/add")
    public CudResult<Boolean> add(@Validated({InsertGroup.class}) @RequestBody MessageNoticeParam param) {
            return CudResult.success(messageNoticeService.add(param));
    }

    /**
     * 批量新增通知公告.
     */
    @ApiOperation(value = "批量新增通知公告")
    @PostMapping(value = "/add/batch")
    public CudResult<Boolean> addBatch(@Validated @RequestBody List<MessageNoticeParam> params) {
            return CudResult.success(messageNoticeService.addBatch(params));

    }

    /**
     * 删除通知公告.
     */
    @ApiOperation(value = "删除通知公告")
    @GetMapping(value = "/remove/{id}")
    public CudResult<Boolean> remove(@PathVariable Long id) {
            return CudResult.success(messageNoticeService.remove(id));

    }

    /**
     * 发布
     * @return 是否成功
     */
    @ApiOperation(value = "发布通知公告")
    @GetMapping(value = "/publishNotify/{id}")
    public CudResult<Boolean> publishNotify(@PathVariable Long id) {
            return CudResult.success(messageNoticeService.publishNotify(id));

    }

    /**
     * 批量删除通知公告.
     */
    @ApiOperation(value = "批量删除通知公告")
    @PostMapping(value = "/remove/batch")
    public CudResult<Boolean> removeBatch(@RequestBody List<Long> ids) {
            return CudResult.success(messageNoticeService.removeBatch(ids));

    }

    /**
     * 编辑通知公告.
     */
    @ApiOperation(value = "编辑通知公告")
    @PostMapping(value = "/edit/{id}")
    public CudResult<Boolean> edit(@PathVariable Long id, @RequestBody @Validated({UpdateGroup.class}) MessageNoticeParam param) {
                return CudResult.success(messageNoticeService.edit(id, param));
    }

    /**
     * 撤销通知公告信息.
     */
    @ApiOperation(value = "撤销通知公告信息")
    @GetMapping(value = "/revocation/{id}")
    public CudResult<Boolean> revocation( @PathVariable Long id) {
            return CudResult.success(messageNoticeService.revocation(id));

    }


    /**
     * 批量编辑通知公告.
     */
    @ApiOperation(value = "批量编辑通知公告")
    @PostMapping(value = "/edit/batch")
    public CudResult<Boolean> editBatch(@Validated @RequestBody List<MessageNoticeParam> params) {
            return CudResult.success(messageNoticeService.editBatch(params));

    }
}
