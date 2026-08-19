
package com.cgnpc.bbxpark.meeting.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.bbxpark.common.constant.Constant;
import com.cgnpc.bbxpark.common.web.CudResult;
import com.cgnpc.bbxpark.meeting.dto.model.*;
import com.cgnpc.bbxpark.meeting.dto.param.*;
import com.cgnpc.bbxpark.meeting.service.IThirdMeetingRecordService;
import com.cgnpc.bbxpark.meeting.service.impl.ThirdMeetingRecordImportService;
import com.cgnpc.bbxpark.settings.dto.model.FileModel;
import com.cgnpc.bbxpark.settings.dto.param.FilePageParam;
import com.cgnpc.bbxpark.settings.service.IFileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;

@RestController
@Validated
@RequestMapping(Constant.BASE_PATH + "/thirdMeeting")
@Api(tags = "智慧会议-PC端-集团会议数据")
@Slf4j
public class ThirdMeetingRecordController {
    @Autowired
    private ThirdMeetingRecordImportService thirdMeetingRecordImportService;
    @Autowired
    private IThirdMeetingRecordService thirdMeetingRecordService;
    @Autowired
    private IFileService fileService;

    @ApiOperation(value = "PC端-获取集团会议文件分页列表")
    @PostMapping(value = "/file/page")
    public CudResult<IPage<FileModel>> page(@RequestBody FilePageParam param) {
        Integer[] types = {10, 20};
        param.setTypes(Arrays.asList(types));
        return CudResult.success(fileService.findByTypeIn(param));
    }

    @ApiOperation(value = "PC端-获取集团会议文件分页列表")
    @GetMapping(value = "/file/remove/{id}")
    public CudResult<Boolean> remove(@PathVariable Long id) {
        if(thirdMeetingRecordService.removeByFileId(id)){
            return CudResult.success(fileService.remove(id));
        }
        return CudResult.error();
    }

    @ApiOperation(value = "集团会议数据文件导入")
    @PostMapping(value = "/import")
    public CudResult<ImportReturnModel> importSchedule(@RequestParam(value = "file") MultipartFile file,
                                                       @RequestParam(value = "type")@ApiParam(value = "文件类型:10->本地会议信息;20->视频会议信息") Integer type) {
        return CudResult.success(thirdMeetingRecordImportService.importMeeting(file,type));
    }
}
