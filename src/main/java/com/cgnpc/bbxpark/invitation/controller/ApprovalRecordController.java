
package com.cgnpc.bbxpark.invitation.controller;

import com.cgnpc.bbxpark.common.constant.Constant;
import com.cgnpc.bbxpark.common.web.CudResult;
import com.cgnpc.bbxpark.invitation.dto.model.ApprovalRecordModel;
import com.cgnpc.bbxpark.invitation.dto.param.ApprovalRecordListParam;
import com.cgnpc.bbxpark.invitation.service.IApprovalRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping(Constant.BASE_PATH + "/approval/record")
@Api(tags = "PC端-审批记录")
public class ApprovalRecordController {

    /**
     * Logger.
     */
    private final static Logger LOGGER = LoggerFactory.getLogger(ApprovalRecordController.class);

    /**
     * 审批记录服务接口.
     */
    @Autowired
    private IApprovalRecordService approvalRecordService;


    /**
     * 获取审批记录列表.
     */
    @ApiOperation(value = "获取审批记录列表")
    @PostMapping(value = "/list")
    public CudResult<List<ApprovalRecordModel>> list(@RequestBody ApprovalRecordListParam param) {
        return CudResult.success(approvalRecordService.list(param));
    }
}
