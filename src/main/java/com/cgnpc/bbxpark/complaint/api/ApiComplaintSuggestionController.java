
package com.cgnpc.bbxpark.complaint.api;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.bbxpark.common.enums.ComplaintSuggestionStatusEnum;
import com.cgnpc.bbxpark.common.enums.ErrorCode;
import com.cgnpc.bbxpark.common.utils.WebFrameworkUtils;
import com.cgnpc.bbxpark.common.web.CudResult;
import com.cgnpc.bbxpark.complaint.dto.model.ComplaintSuggestionModel;
import com.cgnpc.bbxpark.complaint.dto.param.ComplaintSuggestionPageParam;
import com.cgnpc.bbxpark.complaint.dto.param.ComplaintSuggestionParam;
import com.cgnpc.bbxpark.complaint.service.IComplaintSuggestionService;
import com.cgnpc.mobile.annotation.RequiredToken;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

/***
 * @Description 投诉建议主表;服务控制类
 * @author huangyongtao
 * @date 2024/7/12 15:26
 */
@RestController
@RequestMapping("/api/complaintSuggest")
@Api(tags = "投诉建议主表;")
public class ApiComplaintSuggestionController {


    /**
     * 投诉建议主表;服务接口.
     */
    @Autowired
    private IComplaintSuggestionService complaintSuggestionService;

    /**
     * 查询投诉建议详情
     */
    @ApiOperation(value = "查询投诉建议详情")
    @PostMapping(value = "/mobile/get")
    @RequiredToken
    public CudResult<ComplaintSuggestionModel> get(@RequestBody @Validated ComplaintSuggestionParam param) {
        if(ObjectUtil.isEmpty(param.getId())){
            return CudResult.errorMessage(ErrorCode.DATA_INVALID.getMsgCn());
        }
        ComplaintSuggestionModel model = complaintSuggestionService.detail(param.getId());
        if(!ComplaintSuggestionStatusEnum.COMPLATE.getCode().equals(model.getStatus()) && !ComplaintSuggestionStatusEnum.COMMENT.getCode().equals(model.getStatus())){
            model.setReplyRemark(null);
            model.setReplyDate(null);
            model.setReplyStaffid(null);
            model.setReplyUname(null);
            model.setReplyUid(null);
        }
        return CudResult.success(model);
    }

    /**
     * 移动端获取投诉建议列表(分页)
     */
    @ApiOperation(value = "移动端获取投诉建议列表(分页)")
    @PostMapping(value = "/mobile/page")
    @RequiredToken
    public CudResult<IPage<ComplaintSuggestionModel>> pageByMobile(@RequestBody @Validated ComplaintSuggestionPageParam param) {
        param.setSubmitUid(WebFrameworkUtils.getHeaderUserId());
        if(ObjectUtil.isNotEmpty(param.getStatus()) && ComplaintSuggestionStatusEnum.REPLY.getCode().equals(param.getStatus())){
            param.setStatus(null);
            param.setStatusList(Arrays.asList(ComplaintSuggestionStatusEnum.REPLY.getCode(),ComplaintSuggestionStatusEnum.ASSIGNMENT.getCode(),ComplaintSuggestionStatusEnum.AUDIT.getCode(),ComplaintSuggestionStatusEnum.AUDIT_REJECT.getCode()));
        }
        IPage<ComplaintSuggestionModel> page = complaintSuggestionService.page(param);
        page.getRecords().forEach(item->{
            if(!ComplaintSuggestionStatusEnum.COMPLATE.getCode().equals(item.getStatus()) && !ComplaintSuggestionStatusEnum.COMMENT.getCode().equals(item.getStatus())){
                item.setStatus(ComplaintSuggestionStatusEnum.REPLY.getCode());
            }
        });
        return  CudResult.success(page);
    }

    /**
     * 新增投诉建议主表;.
     */
    @ApiOperation(value = "新增投诉建议主表;")
    @PostMapping(value = "/mobile/add")
    @RequiredToken
    public CudResult<Boolean> add(@Validated @RequestBody ComplaintSuggestionParam param) {
        return  CudResult.success(complaintSuggestionService.add(param));
    }

    /**
     * 评价投诉建议
     */
    @ApiOperation(value = "评价投诉建议")
    @PostMapping(value = "/mobile/comment")
    @RequiredToken
    public CudResult<Boolean> comment(@RequestBody @Validated ComplaintSuggestionParam param) {
        if(ObjectUtil.isEmpty(param.getId()) || ObjectUtil.isEmpty(param.getCommentScore())){
            return CudResult.errorMessage(ErrorCode.DATA_INVALID.getMsgCn());
        }
        return  CudResult.success(complaintSuggestionService.comment(param));
    }
}
