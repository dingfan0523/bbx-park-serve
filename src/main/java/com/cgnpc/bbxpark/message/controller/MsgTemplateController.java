
package com.cgnpc.bbxpark.message.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.bbxpark.common.constant.Constant;
import com.cgnpc.bbxpark.common.web.CudResult;
import com.cgnpc.bbxpark.message.dto.req.MessageRoleParam;
import com.cgnpc.bbxpark.message.dto.req.MessageTemplateListParam;
import com.cgnpc.bbxpark.message.dto.req.MessageTemplatePageParam;
import com.cgnpc.bbxpark.message.dto.req.MessageTemplateParam;
import com.cgnpc.bbxpark.message.dto.resp.MessageRoleModel;
import com.cgnpc.bbxpark.message.dto.resp.MessageTemplateModel;
import com.cgnpc.bbxpark.message.service.IMessageTemplateService;
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
 * @Description 消息模版服务控制类
 * @author huangyongtao
 * @date 2024/10/25 11:46
 */
@RestController
@RequestMapping(Constant.BASE_PATH + "/msg/template")
@Api(tags = "BBX-消息中心-pc端-消息模版")
public class MsgTemplateController {
    /**
     * 消息模版服务接口.
     */
    @Autowired
    private IMessageTemplateService messageTemplateService;

    /**
     * 获取消息模版信息.
     */
    @ApiOperation(value = "获取消息模版信息")
    @PostMapping(value = "/detail")
    public CudResult<MessageTemplateModel> detail(@RequestBody MessageTemplateParam param) {
        return CudResult.success(messageTemplateService.detail(param));
    }

    /**
     * 获取消息模版列表(分页).
     */
    @ApiOperation(value = "获取消息模版列表(分页)")
    @PostMapping(value = "/page")
    public CudResult<IPage<MessageTemplateModel>> page(@RequestBody MessageTemplatePageParam param) {
        return CudResult.success(messageTemplateService.page(param));
    }

    /**
     * 获取消息模版列表.
     */
    @ApiOperation(value = "获取消息模版列表")
    @PostMapping(value = "/list")
    public CudResult<List<MessageTemplateModel>> list(@RequestBody MessageTemplateListParam param) {
        return CudResult.success(messageTemplateService.list(param));
    }

    /**
     * 新增消息模版.
     */
    @ApiOperation(value = "新增消息模版")
    @PostMapping(value = "/add")
    public CudResult<Boolean> add(@Validated//
                            @RequestBody MessageTemplateParam param) {
        return CudResult.success(messageTemplateService.add(param));
    }

    /**
     * 编辑消息模版.
     */
    @ApiOperation(value = "编辑消息模版")
    @PostMapping(value = "/edit")
    public CudResult<Boolean> edit(@Validated//
                                      @RequestBody MessageTemplateParam param) {
        return CudResult.success(messageTemplateService.edit(param));
    }

    /**
     * 删除消息模版.
     */
    @ApiOperation(value = "删除消息模版")
    @PostMapping(value = "/remove")
    public CudResult<Boolean> remove(@RequestBody MessageTemplateParam param) {
        return CudResult.success(messageTemplateService.remove(param));
    }

    /**
     * 启用消息模版.
     */
    @ApiOperation(value = "启用消息模版")
    @PostMapping(value = "/enable")
    public CudResult<Boolean> enable(@RequestBody MessageTemplateParam param) {
        return CudResult.success(messageTemplateService.enable(param));
    }

    /**
     * 禁用消息模版.
     */
    @ApiOperation(value = "禁用消息模版")
    @PostMapping(value = "/disable")
    public CudResult<Boolean> disable(@RequestBody MessageTemplateParam param) {
        return CudResult.success(messageTemplateService.disable(param));
    }

    /**
     * 获取消息模版配置信息.
     */
    @ApiOperation(value = "获取消息模版配置信息")
    @PostMapping(value = "/config/detail")
    public CudResult<MessageTemplateModel> detailConfig(@RequestBody MessageTemplateParam param) {
        return CudResult.success(messageTemplateService.detailConfig(param));
    }

    /**
     * 新增消息模版配置信息.
     */
    @ApiOperation(value = "新增消息模版配置信息")
    @PostMapping(value = "/config/add")
    public CudResult<Boolean> addConfig(@RequestBody MessageTemplateParam param) {
        return CudResult.success(messageTemplateService.addConfig(param));
    }

    /**
     * 获取消息模版列表详情
     */
    @ApiOperation(value = "获取消息模版列表详情")
    @PostMapping(value = "/finds")
    public CudResult<List<MessageTemplateModel>> findTemplates(@RequestBody MessageTemplateListParam param) {
        return CudResult.success(messageTemplateService.findTemplates(param));
    }

    /**
     * 查询角色列表
     */
    @ApiOperation(value = "查询角色列表")
    @PostMapping(value = "/role/find")
    public CudResult<List<MessageRoleModel>> findRoles(@RequestBody MessageRoleParam param) {
        return CudResult.success(messageTemplateService.findRoleList(param));
    }

}
