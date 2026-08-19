
package com.cgnpc.bbxpark.space.api;

import com.cgnpc.bbxpark.acl.uic.service.IUserApiService;
import com.cgnpc.bbxpark.common.web.CudResult;
import com.cgnpc.bbxpark.space.dto.model.ParkSpaceTreeModel;
import com.cgnpc.bbxpark.space.dto.param.ParkSpaceListParam;
import com.cgnpc.bbxpark.space.service.IParkSpaceService;
import com.cgnpc.bbxpark.space.service.IUserSpaceService;
import com.cgnpc.mobile.annotation.RequiredToken;
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
@RequestMapping("/api/park/space")
@Api(tags = "BBX-园区空间服务接口")
public class ApiParkSpaceController {

    /**
     * 园区空间列服务接口.
     */
    @Autowired
    private IParkSpaceService parkSpaceService;
    @Autowired
    private IUserSpaceService userSpaceService;
    @Autowired
    private IUserApiService userApiService;

    /**
     * 园区空间树形结构列表
     */
    @ApiOperation(value = "园区空间树形结构列表(不带角色校验)")
    @PostMapping(value = "/tree")
    @RequiredToken
    public CudResult<List<ParkSpaceTreeModel>> tree(@RequestBody @Validated ParkSpaceListParam param) {
        return CudResult.success(parkSpaceService.tree(param));
    }
}
