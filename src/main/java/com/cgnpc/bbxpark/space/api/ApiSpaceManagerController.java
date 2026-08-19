
package com.cgnpc.bbxpark.space.api;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.bbxpark.common.constant.Constant;
import com.cgnpc.bbxpark.common.web.CudResult;
import com.cgnpc.bbxpark.space.dto.model.SpaceManagerModel;
import com.cgnpc.bbxpark.space.dto.param.SpaceManagerListParam;
import com.cgnpc.bbxpark.space.dto.param.SpaceManagerPageParam;
import com.cgnpc.bbxpark.space.dto.param.SpaceManagerParam;
import com.cgnpc.bbxpark.space.service.ISpaceManagerService;
import com.cgnpc.mobile.annotation.RequiredToken;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/space/manager")
@Api(tags = "智慧空间-空间责任人")
@Slf4j
public class ApiSpaceManagerController {

    /**
     * 空间责任人服务接口.
     */
    @Autowired
    private ISpaceManagerService spaceManagerService;

    /**
     * 获取空间责任人列表.
     */
    @ApiOperation(value = "获取空间责任人列表")
    @PostMapping(value = "/list")
    @RequiredToken
    public CudResult<List<SpaceManagerModel>> list(@RequestBody SpaceManagerListParam param) {
        return CudResult.success(spaceManagerService.list(param));
    }
}
