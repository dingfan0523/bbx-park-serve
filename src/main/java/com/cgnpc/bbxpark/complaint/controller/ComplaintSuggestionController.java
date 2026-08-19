
package com.cgnpc.bbxpark.complaint.controller;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.bbxpark.common.constant.Constant;
import com.cgnpc.bbxpark.common.enums.ComplaintSuggestionStatusEnum;
import com.cgnpc.bbxpark.common.enums.ErrorCode;
import com.cgnpc.bbxpark.common.exception.GenericException;
import com.cgnpc.bbxpark.common.utils.WebFrameworkUtils;
import com.cgnpc.bbxpark.common.web.CudResult;
import com.cgnpc.bbxpark.complaint.dto.model.ComplaintSuggestionModel;
import com.cgnpc.bbxpark.complaint.dto.param.ComplaintSuggestionListParam;
import com.cgnpc.bbxpark.complaint.dto.param.ComplaintSuggestionPageParam;
import com.cgnpc.bbxpark.complaint.dto.param.ComplaintSuggestionParam;
import com.cgnpc.bbxpark.complaint.service.IComplaintSuggestionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/***
 * @Description 投诉建议主表;服务控制类
 * @author huangyongtao
 * @date 2024/7/12 15:26
 */
@RestController
@RequestMapping(Constant.BASE_PATH + "/complaintSuggest")
@Api(tags = "投诉建议主表;")
public class ComplaintSuggestionController {


    /**
     * 投诉建议主表;服务接口.
     */
    @Autowired
    private IComplaintSuggestionService complaintSuggestionService;

    /**
     * 查询投诉建议详情（包含流转信息）
     */
    @ApiOperation(value = "查询投诉建议详情（包含流转信息）")
    @PostMapping(value = "/detail")
    public CudResult<ComplaintSuggestionModel> detail(@RequestBody @Validated ComplaintSuggestionParam param) {
        if(ObjectUtil.isEmpty(param.getId())){
            return CudResult.error(null,ErrorCode.DATA_INVALID.getMsg());
        }
        return  CudResult.success(complaintSuggestionService.detail(param.getId()));
    }

    /**
     * 获取投诉建议主表;列表(分页).
     */
    @ApiOperation(value = "获取投诉建议主表;列表(分页)")
    @PostMapping(value = "/page")
    public CudResult<IPage<ComplaintSuggestionModel>> page(@RequestBody ComplaintSuggestionPageParam param) {
        return  CudResult.success(complaintSuggestionService.page(param));
    }

    /**
     * 获取投诉建议列表(分页，用户筛选)
     */
    @ApiOperation(value = "获取投诉建议列表(分页，用户筛选)")
    @PostMapping(value = "/pageByUser")
    public CudResult<IPage<ComplaintSuggestionModel>> pageByUser(@RequestBody @Validated ComplaintSuggestionPageParam param) {
        param.setReplyUid(WebFrameworkUtils.getHeaderUserId());
        return  CudResult.success(complaintSuggestionService.page(param));
    }

    /**
     * 分配投诉建议
     */
    @ApiOperation(value = "分配投诉建议")
    @PostMapping(value = "/assignment")
    public CudResult assignment(@RequestBody @Validated ComplaintSuggestionParam param) {
        if(ObjectUtil.isEmpty(param.getReplyUid()) || ObjectUtil.isEmpty(param.getReplyStaffid()) || ObjectUtil.isEmpty(param.getReplyUname()) || ObjectUtil.isEmpty(param.getId())){
            return CudResult.errorMessage(ErrorCode.DATA_INVALID.getMsgCn());
        }
        return  CudResult.success(complaintSuggestionService.assignment(param));
    }

    /**
     * 审核投诉建议
     */
    @ApiOperation(value = "审核投诉建议")
    @PostMapping(value = "/audit")
    public CudResult audit(@RequestBody @Validated ComplaintSuggestionParam param) {
        if(ObjectUtil.isEmpty(param.getStatus()) || ObjectUtil.isEmpty(param.getId()) || ObjectUtil.isEmpty(param.getAuditRemark()) || ObjectUtil.isEmpty(param.getRomanId())){
            return CudResult.errorMessage(ErrorCode.DATA_INVALID.getMsgCn());
        }
        if(!ComplaintSuggestionStatusEnum.AUDIT_REJECT.getCode().equals(param.getStatus()) && !ComplaintSuggestionStatusEnum.COMPLATE.getCode().equals(param.getStatus())){
            throw GenericException.fail("审核状态有误！");
        }
        return  CudResult.success(complaintSuggestionService.audit(param));
    }

    /**
     * 回复投诉建议
     */
    @ApiOperation(value = "回复投诉建议")
    @PostMapping(value = "/reply")
    public CudResult reply(@RequestBody @Validated ComplaintSuggestionParam param) {
        if(ObjectUtil.isEmpty(param.getId()) || ObjectUtil.isEmpty(param.getReplyRemark())){
            return CudResult.errorMessage(ErrorCode.DATA_INVALID.getMsgCn());
        }
        return  CudResult.success(complaintSuggestionService.reply(param));
    }

    /**
     * 获取投诉建议主表;列表.
     */
    @ApiOperation(value = "获取投诉建议主表;列表")
    @PostMapping(value = "/list")
    public CudResult<List<ComplaintSuggestionModel>> list(@RequestBody ComplaintSuggestionListParam param) {
            return  CudResult.success(complaintSuggestionService.list(param));
    }

    /**
     * 删除投诉建议主表;.
     */
    @ApiOperation(value = "删除投诉建议主表;")
    @DeleteMapping(value = "/{id}")
    public CudResult remove(@PathVariable Long id) {
            return  CudResult.success(complaintSuggestionService.remove(id));
    }

    /**
     * 批量删除投诉建议主表;.
     */
    @ApiOperation(value = "批量删除投诉建议主表;")
    @PostMapping(value = "/remove/batch")
    public CudResult removeBatch(@RequestBody List<Long> ids) {
            return  CudResult.success(complaintSuggestionService.removeBatch(ids));
    }
}
