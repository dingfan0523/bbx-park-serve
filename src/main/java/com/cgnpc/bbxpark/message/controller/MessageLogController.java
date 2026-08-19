
package com.cgnpc.bbxpark.message.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.bbxpark.common.constant.Constant;
import com.cgnpc.bbxpark.common.web.CudResult;
import com.cgnpc.bbxpark.message.dto.req.MessageLogListParam;
import com.cgnpc.bbxpark.message.dto.req.MessageLogPageParam;
import com.cgnpc.bbxpark.message.dto.req.MessageLogParam;
import com.cgnpc.bbxpark.message.dto.resp.MessageLogModel;
import com.cgnpc.bbxpark.message.service.IMessageLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/***
 * @Description 消息日志服务控制类
 * @author huangyongtao
 * @date 2024/10/25 14:55
 */
@RestController
@RequestMapping(Constant.BASE_PATH + "/msg/log")
@Api(tags = "BBX-消息中心-pc端-消息日志")
public class MessageLogController {
    /**
     * 消息日志服务接口.
     */
    @Autowired
    private IMessageLogService messageLogService;

    /**
     * 获取消息日志信息.
     */
    @ApiOperation(value = "获取消息日志信息")
    @PostMapping(value = "/detail")
    public CudResult<MessageLogModel> detail(@RequestBody MessageLogParam param) {
        return CudResult.success(messageLogService.detail(param));
    }

    /**
     * 获取消息日志列表(分页).
     */
    @ApiOperation(value = "获取消息日志列表(分页)")
    @PostMapping(value = "/page")
    public CudResult<IPage<MessageLogModel>> page(@RequestBody MessageLogPageParam param) {
        return CudResult.success(messageLogService.page(param));
    }

    /**
     * 获取消息日志列表.
     */
    @ApiOperation(value = "获取消息日志列表")
    @PostMapping(value = "/list")
    public CudResult<List<MessageLogModel>> list(@RequestBody MessageLogListParam param) {
        return CudResult.success(messageLogService.list(param));
    }

    /**
     * 新增消息日志.
     */
    @ApiOperation(value = "新增消息日志")
    @PostMapping(value = "/add")
    public CudResult<Boolean> add(@Validated//
                          @RequestBody MessageLogParam param) {
        return CudResult.success(messageLogService.add(param));
    }

    /**
     * 编辑消息日志.
     */
    @ApiOperation(value = "编辑消息日志")
    @PostMapping(value = "/edit")
    public CudResult<Boolean> edit(@Validated//
                                      @RequestBody MessageLogParam param) {
        return CudResult.success(messageLogService.edit(param));
    }
}
