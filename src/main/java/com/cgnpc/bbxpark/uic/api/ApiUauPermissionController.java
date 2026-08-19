
package com.cgnpc.bbxpark.uic.api;

import com.cgnpc.bbxpark.acl.uic.service.IUserPermissionApiService;
import com.cgnpc.bbxpark.common.web.CudResult;
import com.cgnpc.mobile.annotation.RequiredToken;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

/***
 * @Description 服务商服务控制类
 * @author huangyongtao
 * @date 2025/11/14 11:00
 */
@RestController
@RequestMapping("/api/uau/perm")
@Api(tags = "UAU")
public class ApiUauPermissionController {

    /**
     * 服务商服务接口.
     */
    @Autowired
    private IUserPermissionApiService userPermissionApiService;

    /**
     * 获取服务商信息.
     */
    @ApiOperation(value = "获取用户权限")
    @GetMapping(value = "/getPermissions")
    @RequiredToken
    public CudResult<Set<String>> getPermissions() {
        return CudResult.success(userPermissionApiService.getPermissionCodeByCurrent());
    }
}