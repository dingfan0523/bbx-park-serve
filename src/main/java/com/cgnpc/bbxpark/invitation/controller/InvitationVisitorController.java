
package com.cgnpc.bbxpark.invitation.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.bbxpark.common.constant.Constant;
import com.cgnpc.bbxpark.common.web.CudResult;
import com.cgnpc.bbxpark.invitation.dto.model.InvitationVisitorModel;
import com.cgnpc.bbxpark.invitation.dto.param.InvitationVisitorListParam;
import com.cgnpc.bbxpark.invitation.dto.param.InvitationVisitorPageParam;
import com.cgnpc.bbxpark.invitation.service.IInvitationVisitorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/***
 * @Description 邀约访客服务控制类
 * @author huangyongtao
 * @date 2025/8/1 17:21
 */
@RestController
@RequestMapping(Constant.BASE_PATH + "/invitation/visitor")
@Api(tags = "邀约访客PC")
public class InvitationVisitorController {

    /**
     * 邀约访客服务接口.
     */
    @Autowired
    private IInvitationVisitorService invitationVisitorService;


    /**
     * 获取邀约访客列表(分页).
     */
    @ApiOperation(value = "获取邀约访客列表(分页)")

    @PostMapping(value = "/page", produces = MediaType.APPLICATION_JSON_VALUE)
    public CudResult<IPage<InvitationVisitorModel>> page(@RequestBody InvitationVisitorPageParam param) {
            return CudResult.success(invitationVisitorService.page(param));
    }

    /**
     * 获取邀约访客列表.
     */
    @ApiOperation(value = "获取邀约访客列表")
    
    @PostMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public CudResult<List<InvitationVisitorModel>> list(@RequestBody InvitationVisitorListParam param) {
            return CudResult.success(invitationVisitorService.list(param));
    }

}
