
package com.cgnpc.bbxpark.settings.controller;

import com.cgnpc.bbxpark.settings.dto.param.BurialPointParam;
import com.cgnpc.bbxpark.settings.service.IBurialPointService;
import com.cgnpc.bbxpark.common.constant.Constant;
import com.cgnpc.bbxpark.common.web.CudResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(Constant.BASE_PATH + "/burial/point")
@Api(tags = "埋点")
public class BurialPointController {


    /**
     * 埋点服务接口.
     */
    @Autowired
    private IBurialPointService burialPointService;



    /**
     * 新增埋点.
     */
    @ApiOperation(value = "新增埋点")
    @PostMapping(value = "/add")
    public CudResult<Boolean> add(@Validated @RequestBody BurialPointParam param) {
        return CudResult.success(burialPointService.add(param));
    }


}
