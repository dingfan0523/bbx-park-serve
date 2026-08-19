
package com.cgnpc.bbxpark.settings.api;

import com.cgnpc.bbxpark.common.web.CudResult;
import com.cgnpc.bbxpark.settings.dto.model.ApiScreenOverviewModel;
import com.cgnpc.bbxpark.settings.service.IScreenOverviewService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/screen/overview")
@Api(tags = "移动端-大屏总览")
public class ApiScreenOverviewController {

    /**
     * 模块服务接口.
     */
    @Autowired
    private IScreenOverviewService screenOverviewService;


    @ApiOperation(value = "获取模块数据")
    @PostMapping(value = "/get")
    public CudResult<ApiScreenOverviewModel> get() {
        return CudResult.success(screenOverviewService.get());
    }
}
