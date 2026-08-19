package com.cgnpc.bbxpark.device.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.bbxpark.common.constant.Constant;
import com.cgnpc.bbxpark.common.web.CudResult;
import com.cgnpc.bbxpark.device.domain.ThingModelMessage;
import com.cgnpc.bbxpark.device.dto.model.ThingModelMessageModel;
import com.cgnpc.bbxpark.device.dto.param.ThingModelMessageParam;
import com.cgnpc.bbxpark.device.service.ThingModelMessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = Constant.BASE_PATH+"/device/thingModelMessage")
@Api(tags = "BBX-设备状态日志")
public class ThingModelMessageController {

    @Autowired
    private ThingModelMessageService thingModelMessageService;

    @ApiOperation(value = "搜索设备日志")
    @PostMapping(value = "/page")
    public CudResult<IPage<ThingModelMessage>> list(@RequestBody ThingModelMessageParam param) {
        return CudResult.success(thingModelMessageService.selectPage(param));
    }

    @ApiOperation(value = "日志详情")
    @GetMapping(value = "/{id}")
    public CudResult<ThingModelMessageModel> selectWeather(@PathVariable String id) {
        return CudResult.success(thingModelMessageService.findById(id));
    }

    @ApiOperation(value = "测试-es初始化数据")
    @GetMapping(value = "/save/test")
    public CudResult<Integer> selectWeather() {
        return CudResult.success(thingModelMessageService.save());
    }

    @ApiOperation(value = "测试-无限查找")
    @GetMapping(value = "/search/test")
    public CudResult<List<ThingModelMessageModel>> search() {
        return CudResult.success(thingModelMessageService.search());
    }
}
