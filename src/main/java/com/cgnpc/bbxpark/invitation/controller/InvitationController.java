
package com.cgnpc.bbxpark.invitation.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.bbxpark.common.constant.Constant;
import com.cgnpc.bbxpark.common.web.CudResult;
import com.cgnpc.bbxpark.invitation.dto.model.InvitationModel;
import com.cgnpc.bbxpark.invitation.dto.param.InvitationListParam;
import com.cgnpc.bbxpark.invitation.dto.param.InvitationPageParam;
import com.cgnpc.bbxpark.invitation.service.IInvitationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/***
 * @Description 邀约服务控制类
 * @author huangyongtao
 * @date 2025/8/1 17:16
 */
@RestController
@RequestMapping(Constant.BASE_PATH + "/invitation")
@Api(tags = "邀约PC")
public class InvitationController {

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
    public CudResult<InvitationModel> detail(@PathVariable Long id) {
            return CudResult.success(invitationService.detail(id, true));
    }

    /**
     * 获取邀约列表(分页).
     */
    @ApiOperation(value = "获取邀约列表(分页)")
    @PostMapping(value = "/page")
    public CudResult<IPage<InvitationModel>> page(@RequestBody InvitationPageParam param) {
            return CudResult.success(invitationService.page(param));
    }

    /**
     * 获取邀约列表.
     */
    @ApiOperation(value = "获取邀约列表")
    @PostMapping(value = "/list")
    public CudResult<List<InvitationModel>> list(@RequestBody InvitationListParam param) {
            return CudResult.success(invitationService.list(param));
    }

    /***
     *邀约记录导出
     */
    @ApiOperation(value = "邀约记录导出")
    @GetMapping(value = "/easyExport")
    public void easyExport(HttpServletResponse response, @ModelAttribute InvitationPageParam param) {
        invitationService.invitationEasyExport(response, param);
    }

}
