
package com.cgnpc.bbxpark.settings.api;

import com.cgnpc.bbxpark.common.web.CudResult;
import com.cgnpc.bbxpark.settings.dto.param.BurialPointParam;
import com.cgnpc.bbxpark.settings.service.IBurialPointService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/burial/point")
@Api(tags = "埋点")
public class ApiBurialPointController {


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
