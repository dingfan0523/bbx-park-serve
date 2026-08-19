package com.cgnpc.bbxpark.space.controller;

import cn.hutool.core.util.ObjectUtil;
import com.cgnpc.bbxpark.common.constant.Constant;
import com.cgnpc.bbxpark.common.exception.GenericException;
import com.cgnpc.bbxpark.common.web.CudResult;
import com.cgnpc.bbxpark.space.dto.model.ParkSpaceTreeModel;
import com.cgnpc.bbxpark.space.dto.model.UserInfoModel;
import com.cgnpc.bbxpark.space.dto.model.UserSpaceModel;
import com.cgnpc.bbxpark.space.dto.param.UserInfoSpaceParam;
import com.cgnpc.bbxpark.space.dto.param.UserSpaceListParam;
import com.cgnpc.bbxpark.space.dto.param.UserSpaceParam;
import com.cgnpc.bbxpark.space.service.IUserSpaceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/***
 * @Description用户与空间访问权限服务控制类
 * @author huangyongtao
 * @date 2024/7/1 17:00
 */
@RestController
@RequestMapping(Constant.BASE_PATH + "/user/space")
@Api(value = "BBX-用户与空间访问权限")
public class UserSpaceController {
    
    /**
     * 用户与空间访问权限服务接口.
     */
    @Autowired
    private IUserSpaceService userSpaceService;

    /***
     * @Description 查询用户空间树
     * @author huangyongtao
     * @date 2024/7/2 11:33
     * @param param
     */
    @ApiOperation(value = "查询用户空间树")
    @PostMapping(value = "/findTree")
    public CudResult<List<ParkSpaceTreeModel>> findTree(@RequestBody @Validated UserSpaceListParam param) {
        if(ObjectUtil.isEmpty(param.getUserId())){
            throw GenericException.fail("请传入用户信息");
        }
        return CudResult.success(userSpaceService.findTree(param));
    }

   /***
    * @Description 查询用户分配的空间树
    * @author huangyongtao
    * @date 2024/7/25 13:56
    * @param param
    */
    @ApiOperation(value = "查询用户分配的空间树")
    @PostMapping(value = "/tree")
    public CudResult<List<ParkSpaceTreeModel>> tree(@RequestBody UserSpaceListParam param) {
        return CudResult.success(userSpaceService.tree(param));
    }


    /**
     * 获取用户与空间访问权限列表.
     */
    @ApiOperation(value = "获取用户与空间访问权限列表")
    @PostMapping(value = "/list")
    public CudResult<List<UserSpaceModel>> list(@RequestBody UserSpaceListParam param) {
        return CudResult.success(userSpaceService.list(param));
    }

    /**
     * 新增用户与空间访问权限.
     */
    @ApiOperation(value = "新增用户与空间访问权限")
    @PostMapping(value = "/add")
    public CudResult<Boolean> add(@Validated//
                                     @RequestBody UserSpaceParam param) {
        return CudResult.success(userSpaceService.add(param));
    }

    /**
     * 删除用户与空间访问权限.
     */
    @ApiOperation(value = "删除用户与空间访问权限")
    @PostMapping(value = "/remove")
    public CudResult<Boolean> remove(@RequestParam Long id) {
        return CudResult.success(userSpaceService.remove(id));
//        return  CudResult.success(userSpaceService.remove(id));
    }

    /**
     * 批量删除用户与空间访问权限.
     */
    @ApiOperation(value = "批量删除用户与空间访问权限")
    @PostMapping(value = "/remove/batch")
    public CudResult<Boolean> removeBatch(@RequestBody List<Long> ids) {
        return CudResult.success(userSpaceService.removeBatch(ids));
    }
    /**
     * 根据空间id获取用户信息集合.
     */
    @ApiOperation(value = "根据空间id获取用户信息集合")
    @PostMapping(value = "/findUserInfoBySpace")
    public CudResult<List<UserInfoModel>> findUserInfoBySpace(@RequestBody UserInfoSpaceParam param) {
        return CudResult.success(userSpaceService.findUserInfoBySpace(param));
    }


}
