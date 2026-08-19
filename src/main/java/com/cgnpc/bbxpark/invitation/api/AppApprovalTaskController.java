
package com.cgnpc.bbxpark.invitation.api;

import com.cgnpc.bbxpark.common.web.CudPageDto;
import com.cgnpc.bbxpark.common.web.CudResult;
import com.cgnpc.bbxpark.invitation.dto.param.ApprovalActionParam;
import com.cgnpc.bbxpark.invitation.service.IApprovalTaskService;
import com.cgnpc.mobile.annotation.RequiredToken;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/approval/task/app")
@Api(tags = "移动端-审批任务")
public class AppApprovalTaskController {

    /**
     * Logger.
     */
    private final static Logger LOGGER = LoggerFactory.getLogger(AppApprovalTaskController.class);

    /**
     * 审批任务服务接口.
     */
    @Autowired
    private IApprovalTaskService approvalTaskService;


    /**
     * 审批任务
     */
    @ApiOperation(value = "根据业务进行审批")
    @PostMapping(value = "/businessApprove")
    @RequiredToken
    public CudResult<Boolean> approve(@RequestBody ApprovalActionParam param) {
        return  CudResult.success(approvalTaskService.businessApprove(param));
    }

    /**
     * 获取审批记录列表.
     */
    @ApiOperation(value = "获取待我审批数量")
    @GetMapping(value = "/pending/count")
    @RequiredToken
    public CudResult<Long> list() {
        CudPageDto param = new CudPageDto();
        param.setSize(1);
        return  CudResult.success(approvalTaskService.pendingApprovalPage(param).getTotal());
    }
}
