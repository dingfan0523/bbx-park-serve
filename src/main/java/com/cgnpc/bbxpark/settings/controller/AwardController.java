
package com.cgnpc.bbxpark.settings.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.bbxpark.settings.dto.model.AwardModel;
import com.cgnpc.bbxpark.settings.dto.param.AwardHandleParam;
import com.cgnpc.bbxpark.settings.dto.param.AwardListParam;
import com.cgnpc.bbxpark.settings.dto.param.AwardPageParam;
import com.cgnpc.bbxpark.settings.dto.param.AwardParam;
import com.cgnpc.bbxpark.settings.service.IAwardService;
import com.cgnpc.bbxpark.common.constant.Constant;
import com.cgnpc.bbxpark.common.constant.InsertGroup;
import com.cgnpc.bbxpark.common.constant.UpdateGroup;
import com.cgnpc.bbxpark.common.web.CudResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.groups.Default;
import java.util.List;

/***
 * @Description 评优评奖服务控制类
 * @author huangyongtao
 * @date 2025/11/13 9:22
 */
@RestController
@RequestMapping(Constant.BASE_PATH + "/award")
@Api(tags = "评优评奖")
public class AwardController {

    /**
     * 评优评奖服务接口.
     */
    @Autowired
    private IAwardService awardService;

    /**
     * 获取评优评奖信息.
     */
    @ApiOperation(value = "获取评优评奖信息")
    
    @GetMapping(value = "/detail/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CudResult<AwardModel> detail(@PathVariable Long id) {
            return CudResult.success(awardService.detail(id));
    }

    /**
     * 获取评优评奖列表(分页).
     */
    @ApiOperation(value = "获取评优评奖列表(分页)")
    
    @PostMapping(value = "/page", produces = MediaType.APPLICATION_JSON_VALUE)
    public CudResult<IPage<AwardModel>> page(@RequestBody AwardPageParam param) {
            return CudResult.success(awardService.page(param));
    }

    /**
     * 获取评优评奖列表.
     */
    @ApiOperation(value = "获取评优评奖列表")
    
    @PostMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public CudResult<List<AwardModel>> list(@RequestBody AwardListParam param) {
            return CudResult.success(awardService.list(param));
    }

    /**
     * 新增评优评奖.
     */
    @ApiOperation(value = "新增评优评奖")
    
    @PostMapping(value = "/add",  produces = MediaType.APPLICATION_JSON_VALUE)
    public CudResult<Long> add(@Validated({Default.class, InsertGroup.class}) @RequestBody AwardParam param) {
            return CudResult.success(awardService.add(param));
    }

    /**
     * 删除评优评奖.
     */
    @ApiOperation(value = "删除评优评奖")
    
    @GetMapping(value = "/remove/{id}",  produces = MediaType.APPLICATION_JSON_VALUE)
    public CudResult remove(@PathVariable Long id) {
            return CudResult.success(awardService.remove(id));
    }

    /**
     * 编辑评优评奖.
     */
    @ApiOperation(value = "编辑评优评奖")
    
    @PostMapping(value = "/edit",  produces = MediaType.APPLICATION_JSON_VALUE)
    public CudResult edit(@Validated({Default.class, UpdateGroup.class}) @RequestBody AwardParam param) {
            return CudResult.success(awardService.edit(param));
    }

    /**
     * 撤回评优评奖.
     */
    @ApiOperation(value = "撤回评优评奖")
    
    @PostMapping(value = "/recall",  produces = MediaType.APPLICATION_JSON_VALUE)
    public CudResult recall(@Validated({Default.class, UpdateGroup.class})  @RequestBody AwardHandleParam param) {
            return CudResult.success(awardService.recall(param));
    }

    /**
     * 提交评优评奖.
     */
    @ApiOperation(value = "提交评优评奖")
    
    @PostMapping(value = "/submit",  produces = MediaType.APPLICATION_JSON_VALUE)
    public CudResult submit(@Validated({Default.class, UpdateGroup.class})  @RequestBody AwardHandleParam param) {
        return CudResult.success(awardService.submit(param));
    }

    /**
     * 审批评优评奖.
     */
    @ApiOperation(value = "审批评优评奖")
    
    @PostMapping(value = "/audit",  produces = MediaType.APPLICATION_JSON_VALUE)
    public CudResult audit(@Validated({Default.class, UpdateGroup.class})  @RequestBody AwardHandleParam param) {
        return CudResult.success(awardService.audit(param));
    }
    /**
     * 取消展示评优评奖.
     */
    @ApiOperation(value = "取消展示评优评奖")
    
    @PostMapping(value = "/cancel",  produces = MediaType.APPLICATION_JSON_VALUE)
    public CudResult cancel(@Validated({Default.class, UpdateGroup.class})  @RequestBody AwardHandleParam param) {
        return CudResult.success(awardService.cancel(param));
    }

    /**
     * 执行评优评奖定时任务.
     */
    @ApiOperation(value = "执行评优评奖定时任务")
    @PostMapping(value = "/executeAwardDisplayTime",  produces = MediaType.APPLICATION_JSON_VALUE)
    public CudResult executeAwardDisplayTime() {
        awardService.executeAwardDisplayTime();
        return CudResult.success(true);
    }


}
