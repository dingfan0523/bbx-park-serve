
package com.cgnpc.bbxpark.space.api;

import com.cgnpc.bbxpark.common.web.CudResult;
import com.cgnpc.bbxpark.space.dto.model.ParkSpaceFullModel;
import com.cgnpc.bbxpark.space.service.IStationService;
import com.cgnpc.mobile.annotation.RequiredToken;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/space/station")
@Api(tags = "智慧空间-空间工位")
@Slf4j
public class ApiStationController {

    /**
     * 空间工位服务接口.
     */
    @Autowired
    private IStationService stationService;

    /***
     * @Description 获取用户办公地点
     * @author huangyongtao
     * @date 2025/9/25 15:22
     */
    @ApiOperation(value = "获取用户办公地点")
    @GetMapping(value = "/use")
    @RequiredToken
    public CudResult<ParkSpaceFullModel> getUserStationSpace() {
        return CudResult.success(stationService.getUserStationSpace());
    }
}
