
package com.cgnpc.bbxpark.invitation.api;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.bbxpark.common.web.CudResult;
import com.cgnpc.bbxpark.invitation.dto.model.InvitationApproveModel;
import com.cgnpc.bbxpark.invitation.dto.model.InvitationModel;
import com.cgnpc.bbxpark.invitation.dto.param.InvitationListParam;
import com.cgnpc.bbxpark.invitation.dto.param.InvitationPageParam;
import com.cgnpc.bbxpark.invitation.dto.param.InvitationParam;
import com.cgnpc.bbxpark.invitation.service.IInvitationService;
import com.cgnpc.mobile.annotation.RequiredToken;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/***
 * @Description 邀约服务控制类
 * @author huangyongtao
 * @date 2025/8/1 17:16
 */
@RestController
@RequestMapping("/api/app/invitation")
@Api(tags = "邀约App")
public class AppInvitationController {

    /**
     * 邀约服务接口.
     */
    @Autowired
    private IInvitationService invitationService;

    /**
     * 获取邀约信息.
     */
    @ApiOperation(value = "获取邀约信息")
    @GetMapping(value = "/detail/{id}")
    @RequiredToken
    public CudResult<InvitationModel> detail(@PathVariable Long id) {
            return CudResult.success(invitationService.detail(id, true));
    }

    /**
     * 获取再次邀约信息.
     */
    @ApiOperation(value = "获取再次邀约信息")
    @GetMapping(value = "/again/detail/{id}")
    @RequiredToken
    public CudResult<InvitationModel> againDetail(@PathVariable Long id) {
        return CudResult.success(invitationService.detail(id, false));
    }

    /**
     * 获取邀约列表(分页).
     */
    @ApiOperation(value = "获取邀约列表(分页)")
    @PostMapping(value = "/page")
    @RequiredToken
    public CudResult<IPage<InvitationModel>> page(@RequestBody InvitationPageParam param) {
        return CudResult.success(invitationService.appPage(param));
    }

    /**
     * 获取邀约列表.
     */
    @ApiOperation(value = "获取邀约列表")
    @PostMapping(value = "/list")
    @RequiredToken
    public CudResult<List<InvitationModel>> list(@RequestBody InvitationListParam param) {
            return CudResult.success(invitationService.list(param));
    }

    /**
     * 新增邀约.
     */
    @ApiOperation(value = "新增邀约")
    @PostMapping(value = "/add")
    @RequiredToken
    public CudResult<Long> add(@RequestBody InvitationParam param) {
            return CudResult.success(invitationService.add(param));
    }

    /**
     * 取消邀约.
     */
    @ApiOperation(value = "取消邀约")
    @PostMapping(value = "/cancel")
    @RequiredToken
    public CudResult<Boolean> cancel(@RequestBody InvitationParam param) {
            return CudResult.success(invitationService.cancel(param));
    }

    /**
     * 查询区域的审批信息.
     */
    @ApiOperation(value = "查询区域的审批信息")
    @GetMapping(value = "/findApproveList/{spaceId}")
    @RequiredToken
    public CudResult<List<InvitationApproveModel>> findApproveList(@PathVariable Long spaceId) {
        return CudResult.success(invitationService.findApproveList(spaceId));
    }

    /**
     * 获取我的审批的邀约列表(分页).
     */
    @ApiOperation(value = "获取我的审批的邀约列表(分页)")
    @PostMapping(value = "/all/approve/page")
    @RequiredToken
    public CudResult<IPage<InvitationModel>> allApprovePage(@RequestBody InvitationPageParam param) {
        return CudResult.success(invitationService.allApprovePage(param));
    }

    /**
     * 获取我的待审批的邀约列表(分页).
     */
    @ApiOperation(value = "获取我的待审批的邀约列表(分页)")
    @PostMapping(value = "/pending/approve/page")
    @RequiredToken
    public CudResult<IPage<InvitationModel>> pendingApprovePage(@RequestBody InvitationPageParam param) {
        return CudResult.success(invitationService.pendingApprovePage(param));
    }

    /**
     * 获取我的已审批的邀约列表(分页).
     */
    @ApiOperation(value = "获取我的已审批的邀约列表(分页)")
    @PostMapping(value = "/approve/page")
    @RequiredToken
    public CudResult<IPage<InvitationModel>> approvePage(@RequestBody InvitationPageParam param) {
        return CudResult.success(invitationService.approvePage(param));
    }

}
