
package com.cgnpc.bbxpark.settings.controller;

import com.cgnpc.bbxpark.settings.dto.model.DocumentViewRecordsModel;
import com.cgnpc.bbxpark.settings.dto.param.DocumentViewRecordsListParam;
import com.cgnpc.bbxpark.settings.dto.param.DocumentViewRecordsParam;
import com.cgnpc.bbxpark.settings.service.IDocumentViewRecordsService;
import com.cgnpc.bbxpark.common.constant.Constant;
import com.cgnpc.bbxpark.common.web.CudResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping(Constant.BASE_PATH + "/document/records")
@Api(tags = "文档查看记录")
public class DocumentViewRecordsController {


    /**
     * 文档查看记录服务接口.
     */
    @Autowired
    private IDocumentViewRecordsService documentViewRecordsService;


    /**
     * 获取文档查看记录列表.
     */
    @ApiOperation(value = "获取文档查看记录列表")
    @PostMapping(value = "/list")
    public CudResult<List<DocumentViewRecordsModel>> list(@RequestBody DocumentViewRecordsListParam param) {
        return CudResult.success(documentViewRecordsService.list(param));
    }

    /**
     * 新增文档查看记录.
     */
    @ApiOperation(value = "新增文档查看记录")
    @PostMapping(value = "/add")
    public CudResult<Boolean> add(@Validated @RequestBody DocumentViewRecordsParam param) {
        return CudResult.success(documentViewRecordsService.add(param));
    }


}
