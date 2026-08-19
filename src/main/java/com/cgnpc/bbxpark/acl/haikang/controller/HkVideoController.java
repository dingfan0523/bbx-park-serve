package com.cgnpc.bbxpark.acl.haikang.controller;

import com.cgnpc.bbxpark.acl.haikang.model.PlayBackURLsModel;
import com.cgnpc.bbxpark.acl.haikang.model.PlayBackURLsParam;
import com.cgnpc.bbxpark.acl.haikang.model.PlayPreviewURLsParam;
import com.cgnpc.bbxpark.acl.haikang.service.HkVideoService;
import com.cgnpc.bbxpark.common.constant.Constant;
import com.cgnpc.bbxpark.common.web.CudResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequestMapping(Constant.BASE_PATH + "/haiKang/Video")
@Api(tags = "海康视频管理")
public class HkVideoController {
    @Autowired
    private HkVideoService videoService;

    /**
     * 获取监控点回放取流URLv2
     *
     * @param params
     * @return
     */
    @ApiOperation(value = "获取监控点回放取流URLv2")
    @PostMapping(value = "/getPlaybackURLs")
    public CudResult<PlayBackURLsModel> getPlaybackURLs(@RequestBody PlayBackURLsParam params) {
        return CudResult.success(videoService.getPlaybackURLs(params));
    }

    /**
     * 获取监控点预览取流URLv2
     *
     * @param params
     * @return
     */
    @ApiOperation(value = "获取监控点预览取流URLv2")
    @PostMapping(value = "/getPlayliveURLs")
    public CudResult<PlayBackURLsModel> getPlayliveURLs(@RequestBody PlayPreviewURLsParam params) {
        return CudResult.success(videoService.getPlayliveURLs(params));
    }
}
