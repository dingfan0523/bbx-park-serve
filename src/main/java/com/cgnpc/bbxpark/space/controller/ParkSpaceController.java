
package com.cgnpc.bbxpark.space.controller;

import com.cgnpc.bbxpark.acl.uic.service.IUserApiService;
import com.cgnpc.bbxpark.common.constant.Constant;
import com.cgnpc.bbxpark.common.utils.CollectionUtils;
import com.cgnpc.bbxpark.common.web.CudResult;
import com.cgnpc.bbxpark.space.dto.model.ParkSpaceModel;
import com.cgnpc.bbxpark.space.dto.model.ParkSpaceTreeModel;
import com.cgnpc.bbxpark.space.dto.model.UserInfoModel;
import com.cgnpc.bbxpark.space.dto.model.UserSpaceModel;
import com.cgnpc.bbxpark.space.dto.param.*;
import com.cgnpc.bbxpark.space.service.IParkSpaceService;
import com.cgnpc.bbxpark.space.service.IUserSpaceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping(Constant.BASE_PATH + "/park/space")
@Api(tags = "BBX-园区空间服务接口")
public class ParkSpaceController {


    /**
     * 园区空间列服务接口.
     */
    @Autowired
    private IParkSpaceService parkSpaceService;
    @Autowired
    private IUserSpaceService userSpaceService;
    @Autowired
    private IUserApiService userApiService;

    @ApiOperation(value = "园区空间列表(角色校验)")
    @PostMapping(value = "/list")
    public CudResult<List<ParkSpaceTreeModel>> list(@RequestBody @Validated ParkSpaceListParam param) {
        return CudResult.success(parkSpaceService.findTreeList(param));
    }

    /**
     * 园区空间树形结构列表
     */
    @ApiOperation(value = "园区空间树形结构列表(角色校验)")
    @PostMapping(value = "/findTreeList")
    public CudResult<List<ParkSpaceTreeModel>> findTreeList(@RequestBody @Validated ParkSpaceListParam param) {
        return CudResult.success(parkSpaceService.findTreeList(param));
    }

    /**
     * 园区空间树形结构列表
     */
    @ApiOperation(value = "园区空间树形结构列表(不带角色校验)")
    @PostMapping(value = "/tree")
    public CudResult<List<ParkSpaceTreeModel>> tree(@RequestBody @Validated ParkSpaceListParam param) {
        return CudResult.success(parkSpaceService.tree(param));
    }

    /**
     * 根据园区ID和空间编码校验唯一性
     */
    @ApiOperation(value = "根据园区ID和空间编码校验唯一性")
    @PostMapping(value = "/checkOnlyByParkIdAndCode")
    public CudResult<Boolean> checkOnlyByParkIdAndCode(@RequestBody @Validated//({InsertGroup.class, Default.class})
                                                    ParkSpaceCheckCodeParam param) {
        return CudResult.success(parkSpaceService.checkOnlyByParkIdAndCode(param));
    }

    /**
     * 同一层级空间名称校验唯一性
     */
    @ApiOperation(value = "同一层级空间名称校验唯一性")
    @PostMapping(value = "/checkOnlyByParentIdAndName")
    public CudResult<Boolean> checkOnlyByParentIdAndName(@RequestBody @Validated//({InsertGroup.class, Default.class})
                                                        ParkSpaceCheckNameParam param) {
        return CudResult.success(parkSpaceService.checkOnlyByParentIdAndName(param));
    }

    /**
     * 新增园区空间列.
     */
    @ApiOperation(value = "新增园区空间列")
    @PostMapping(value = "/add")
    public CudResult<ParkSpaceModel> add(@Validated//
                                          @RequestBody ParkSpaceParam param) {
        return CudResult.success(parkSpaceService.add(param));
    }

    /**
     * 编辑园区空间列.
     */
    @ApiOperation(value = "编辑园区空间列")
    @PostMapping(value = "edit")
    public CudResult<Boolean> edit(@RequestBody @Validated//
                                      ParkSpaceParam param) {
        return CudResult.success(parkSpaceService.edit(param));
    }

    /**
     * 删除园区空间列.
     */
    @ApiOperation(value = "删除园区空间列")
    @GetMapping(value = "remove/{id}")
    public CudResult<Boolean> remove(@PathVariable @NotNull(message = "园区空间列标识不能为空") Long id) {
        return CudResult.success(parkSpaceService.remove(id));
    }

    @ApiOperation(value = "新增空间与用户访问权限")
    @PostMapping(value = "/alloc")
    public CudResult<Boolean> alloc(@Validated//
                                 @RequestBody UserSpaceParam param) {
        return CudResult.success(userSpaceService.add(param));
    }

    @ApiOperation(value = "查询空间下的用户列表")
    @PostMapping(value = "/findUserList")
    public CudResult<List<UserSpaceModel>> findUserList(@Validated//
                                   @RequestBody UserSpaceListParam param) {
        List<UserSpaceModel> list = userSpaceService.list(param);
        if(CollectionUtils.isNotEmpty(list)){
            List<String> staffNos = list.stream().map(UserSpaceModel::getUserId).collect(Collectors.toList());
            List<UserInfoModel> staffs = userApiService.getByStaffNos(staffNos);
            Map<String,UserInfoModel> map = staffs.stream().collect(Collectors.toMap(UserInfoModel::getStaffNo,v->v,(v1,v2)->v2));
            list.forEach(l->{
                if(map.containsKey(l.getUserId())){
                    l.setUserName(map.get(l.getUserId()).getUserName());
                }
            });
        }
        return CudResult.success(list);
    }

    /**
     * 新增园区.
     */
    @ApiOperation(value = "新增园区")
    @PostMapping(value = "/addParkAndCreateASpace")
    public CudResult<Boolean> addParkAndCreateASpace(@Validated//
                                                      @RequestBody TenantInfoParam param) {
        return CudResult.success(parkSpaceService.addParkAndCreateSpace(param));
    }

    /**
     * 新增园区.
     */
    @ApiOperation(value = "新增园区")
    @PostMapping(value = "/editParkAndCreateASpace")
    public CudResult<Boolean> editParkAndCreateAspace(@Validated//
                                                         @RequestBody TenantInfoParam param) {
        return CudResult.success(parkSpaceService.editParkAndCreateAspace(param));
    }

}
