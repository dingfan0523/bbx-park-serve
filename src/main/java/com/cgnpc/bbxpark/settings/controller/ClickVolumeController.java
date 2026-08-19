
package com.cgnpc.bbxpark.settings.controller;

import com.cgnpc.bbxpark.settings.dto.model.ClickVolumeModel;
import com.cgnpc.bbxpark.settings.dto.param.ClickVolumeListParam;
import com.cgnpc.bbxpark.settings.dto.param.ClickVolumeParam;
import com.cgnpc.bbxpark.settings.service.IClickVolumeService;
import com.cgnpc.bbxpark.common.constant.Constant;
import com.cgnpc.bbxpark.common.utils.WebFrameworkUtils;
import com.cgnpc.bbxpark.common.web.CudResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;



@RestController
@RequestMapping(Constant.BASE_PATH + "/click/volume")
@Api(tags = "点击量")
public class ClickVolumeController {


    /**
     * 点击量服务接口.
     */
    @Autowired
    private IClickVolumeService clickVolumeService;


    /**
     * 获取点击量列表.
     */
    @ApiOperation(value = "获取点击量列表")
    @PostMapping(value = "/list")
    public CudResult<List<ClickVolumeModel>> list() {
        ClickVolumeListParam param = new ClickVolumeListParam();
        param.setUserId(WebFrameworkUtils.getHeaderUserId());
        return CudResult.success(clickVolumeService.list(param));
    }

    /**
     * 新增点击量.
     */
    @ApiOperation(value = "新增点击量")
    @PostMapping(value = "/add")
    public CudResult<Boolean> add(@Validated @RequestBody ClickVolumeParam param) {
        param.setUserId(WebFrameworkUtils.getHeaderUserId());
        return CudResult.success(clickVolumeService.add(param));
    }


}
