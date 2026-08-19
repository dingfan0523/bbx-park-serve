
package com.cgnpc.bbxpark.invitation.controller;

import com.cgnpc.bbxpark.common.constant.Constant;
import com.cgnpc.bbxpark.common.web.CudPageDto;
import com.cgnpc.bbxpark.common.web.CudResult;
import com.cgnpc.bbxpark.invitation.service.IApprovalTaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


@Slf4j
@RestController
@RequestMapping(Constant.BASE_PATH + "/approval/task")
@Api(tags = "PC端-审批任务")
public class ApprovalTaskController {


    /**
     * 审批任务服务接口.
     */
    @Resource
    private IApprovalTaskService approvalTaskService;


    /**
     * 获取审批记录列表.
     */
    @ApiOperation(value = "获取待我审批数量")
    @GetMapping(value = "/pending/count")
    public CudResult<Long> list() {
        CudPageDto param = new CudPageDto();
        param.setSize(1);
        return  CudResult.success(approvalTaskService.pendingApprovalPage(param).getTotal());
    }
}
