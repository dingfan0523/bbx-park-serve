
package com.cgnpc.bbxpark.settings.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.bbxpark.settings.dto.model.AttentionManageModel;
import com.cgnpc.bbxpark.settings.dto.param.AttentionManageListParam;
import com.cgnpc.bbxpark.settings.dto.param.AttentionManagePageParam;
import com.cgnpc.bbxpark.settings.dto.param.AttentionManageParam;
import com.cgnpc.bbxpark.settings.service.IAttentionManageService;
import com.cgnpc.bbxpark.common.constant.Constant;
import com.cgnpc.bbxpark.common.web.CudResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/***
 * @Description 关注人管理服务控制类
 * @author huangyongtao
 * @date 2025/3/11 10:37
 */
@RestController
@RequestMapping(Constant.BASE_PATH + "/attention")
@Api(tags = "PC端-智慧后勤-关注人管理")
public class AttentionManageController {

    /**
     * 关注人管理服务接口.
     */
    @Autowired
    private IAttentionManageService attentionManageService;


    /**
     * 获取关注人管理列表(分页).
     */
    @ApiOperation(value = "获取关注人管理列表(分页)")
    @PostMapping(value = "/page", produces = MediaType.APPLICATION_JSON_VALUE)
    public CudResult<IPage<AttentionManageModel>> page(@RequestBody AttentionManagePageParam param) {
            return CudResult.success(attentionManageService.page(param));
    }

    /**
     * 获取关注人管理列表.
     */
    @ApiOperation(value = "获取关注人管理列表")
    @PostMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public CudResult<List<AttentionManageModel>> list(@RequestBody AttentionManageListParam param) {
            return CudResult.success(attentionManageService.list(param));
    }

    /**
     * 新增关注人管理.
     */
    @ApiOperation(value = "新增关注人管理")
    @PostMapping(value = "/add",  produces = MediaType.APPLICATION_JSON_VALUE)
    public CudResult<Boolean> add(@Validated @RequestBody AttentionManageParam param) {
            return CudResult.success(attentionManageService.add(param));
    }

    /**
     * 批量新增关注人管理.
     */
    @ApiOperation(value = "批量新增关注人管理")
    @PostMapping(value = "/add/batch",  produces = MediaType.APPLICATION_JSON_VALUE)
    public CudResult addBatch(@Validated @RequestBody List<AttentionManageParam> params) {
            return CudResult.success(attentionManageService.addBatch(params));
    }

    /**
     * 删除关注人管理.
     */
    @ApiOperation(value = "删除关注人管理")
    @GetMapping(value = "/remove/{id}",  produces = MediaType.APPLICATION_JSON_VALUE)
    public CudResult remove(@PathVariable Long id) {
            return CudResult.success(attentionManageService.remove(id));
    }

}
