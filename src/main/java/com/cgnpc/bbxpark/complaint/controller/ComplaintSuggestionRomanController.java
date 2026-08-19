
package com.cgnpc.bbxpark.complaint.controller;

import com.cgnpc.bbxpark.common.constant.Constant;
import com.cgnpc.bbxpark.common.web.CudResult;
import com.cgnpc.bbxpark.complaint.dto.model.ComplaintSuggestionRomanModel;
import com.cgnpc.bbxpark.complaint.dto.param.ComplaintSuggestionRomanListParam;
import com.cgnpc.bbxpark.complaint.dto.param.ComplaintSuggestionRomanParam;
import com.cgnpc.bbxpark.complaint.service.IComplaintSuggestionRomanService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/***
 * @Description 投诉建议流转表;服务控制类
 * @author huangyongtao
 * @date 2024/7/12 15:26
 */
@RestController
@RequestMapping(Constant.BASE_PATH + "/complaintSuggest/roman")
 @Api(tags = "投诉建议流转表;")
public class ComplaintSuggestionRomanController {

    /**
     * 投诉建议流转表;服务接口.
     */
    @Autowired
    private IComplaintSuggestionRomanService complaintSuggestionRomanService;

    /**
     * 获取投诉建议流转表;信息.
     */
    @ApiOperation(value = "获取投诉建议流转表;信息")
    @GetMapping(value = "/{id}")
    public CudResult<ComplaintSuggestionRomanModel> detail(@PathVariable Long id) {
            return  CudResult.success(complaintSuggestionRomanService.detail(id));
    }


    /**
     * 获取投诉建议流转表;列表.
     */
    @ApiOperation(value = "获取投诉建议流转表;列表")
    @PostMapping(value = "/list")
    public CudResult<List<ComplaintSuggestionRomanModel>> list(@RequestBody ComplaintSuggestionRomanListParam param) {
            return  CudResult.success(complaintSuggestionRomanService.list(param));
    }

    /**
     * 新增投诉建议流转表;.
     */
    @ApiOperation(value = "新增投诉建议流转表;")
    @PostMapping(value = "/add")
    public CudResult add(@Validated @RequestBody ComplaintSuggestionRomanParam param) {
            return  CudResult.success(complaintSuggestionRomanService.add(param));
    }

    /**
     * 批量新增投诉建议流转表;.
     */
    @ApiOperation(value = "批量新增投诉建议流转表;")
    @PostMapping(value = "/add/batch")
    public CudResult addBatch(@Validated @RequestBody List<ComplaintSuggestionRomanParam> params) {
            return  CudResult.success(complaintSuggestionRomanService.addBatch(params));
    }

    /**
     * 删除投诉建议流转表;.
     */
    @ApiOperation(value = "删除投诉建议流转表;")
    @DeleteMapping(value = "/{id}")
    public CudResult remove(@PathVariable Long id) {
            return  CudResult.success(complaintSuggestionRomanService.remove(id));
    }

    /**
     * 批量删除投诉建议流转表;.
     */
    @ApiOperation(value = "批量删除投诉建议流转表;")
    @PostMapping(value = "/remove/batch")
    public CudResult removeBatch(@RequestBody List<Long> ids) {
            return  CudResult.success(complaintSuggestionRomanService.removeBatch(ids));
    }
}
