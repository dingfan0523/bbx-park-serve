package com.cgnpc.bbxpark.space.api;

import com.cgnpc.bbxpark.common.web.CudResult;
import com.cgnpc.bbxpark.space.dto.model.UserInfoModel;
import com.cgnpc.bbxpark.space.dto.param.UserInfoSpaceParam;
import com.cgnpc.bbxpark.space.service.IUserSpaceService;
import com.cgnpc.mobile.annotation.RequiredToken;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/***
 * @Description用户与空间访问权限服务控制类
 * @author huangyongtao
 * @date 2024/7/1 17:00
 */
@RestController
@RequestMapping("/api/user/space")
@Api(value = "BBX-用户与空间访问权限")
public class ApiUserSpaceController {
    
    /**
     * 用户与空间访问权限服务接口.
     */
    @Autowired
    private IUserSpaceService userSpaceService;

    /**
     * 根据空间id获取用户信息集合.
     */
    @ApiOperation(value = "根据空间id获取用户信息集合")
    @PostMapping(value = "/findUserInfoBySpace")
    @RequiredToken
    public CudResult<List<UserInfoModel>> findUserInfoBySpace(@RequestBody UserInfoSpaceParam param) {
        return CudResult.success(userSpaceService.findUserInfoBySpace(param));
    }


}
