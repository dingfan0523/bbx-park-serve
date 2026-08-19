
package com.cgnpc.bbxpark.settings.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.bbxpark.settings.dto.model.AwardWinnerModel;
import com.cgnpc.bbxpark.settings.dto.param.AwardWinnerListParam;
import com.cgnpc.bbxpark.settings.dto.param.AwardWinnerPageParam;
import com.cgnpc.bbxpark.settings.dto.param.AwardWinnerParam;
import com.cgnpc.bbxpark.settings.service.IAwardWinnerService;
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
 * @Description 获奖人信息服务控制类
 * @author huangyongtao
 * @date 2025/11/13 9:30
 */
@RestController
@RequestMapping(Constant.BASE_PATH + "/award/winner")
@Api(tags = "获奖人信息")
public class AwardWinnerController {

    /**
     * 获奖人信息服务接口.
     */
    @Autowired
    private IAwardWinnerService awardWinnerService;

    /**
     * 获取获奖人信息信息.
     */
    @ApiOperation(value = "获取获奖人信息信息")
    
    @GetMapping(value = "/detail/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CudResult<AwardWinnerModel> detail(@PathVariable Long id) {
        return CudResult.success(awardWinnerService.detail(id));
    }

    /**
     * 获取获奖人信息列表(分页).
     */
    @ApiOperation(value = "获取获奖人信息列表(分页)")
    
    @PostMapping(value = "/page", produces = MediaType.APPLICATION_JSON_VALUE)
    public CudResult<IPage<AwardWinnerModel>> page(@RequestBody AwardWinnerPageParam param) {
        return CudResult.success(awardWinnerService.page(param));
    }

    /**
     * 获取获奖人信息列表.
     */
    @ApiOperation(value = "获取获奖人信息列表")
    
    @PostMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public CudResult<List<AwardWinnerModel>> list(@RequestBody AwardWinnerListParam param) {
        return CudResult.success(awardWinnerService.list(param));
    }

    /**
     * 新增获奖人信息.
     */
    @ApiOperation(value = "新增获奖人信息")
    
    @PostMapping(value = "/add",  produces = MediaType.APPLICATION_JSON_VALUE)
    public CudResult add(@Validated({Default.class, InsertGroup.class}) @RequestBody AwardWinnerParam param) {
        return CudResult.success(awardWinnerService.add(param));
    }

    /**
     * 删除获奖人信息.
     */
    @ApiOperation(value = "删除获奖人信息")
    
    @GetMapping(value = "/remove/{id}",  produces = MediaType.APPLICATION_JSON_VALUE)
    public CudResult remove(@PathVariable Long id) {
        return CudResult.success(awardWinnerService.remove(id));
    }

    /**
     * 批量删除获奖人信息.
     */
    @ApiOperation(value = "批量删除获奖人信息")
    @PostMapping(value = "/remove/batch",  produces = MediaType.APPLICATION_JSON_VALUE)
    public CudResult removeBatch(@RequestBody List<Long> ids) {
        return CudResult.success(awardWinnerService.removeBatch(ids));
    }

    /**
     * 编辑获奖人信息.
     */
    @ApiOperation(value = "编辑获奖人信息")
    
    @PostMapping(value = "/edit",  produces = MediaType.APPLICATION_JSON_VALUE)
    public CudResult edit(@RequestBody @Validated({Default.class, UpdateGroup.class}) AwardWinnerParam param) {
        return CudResult.success(awardWinnerService.edit(param));
    }

}
