
package com.cgnpc.bbxpark.traffic.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.bbxpark.common.constant.Constant;
import com.cgnpc.bbxpark.common.web.CudResult;
import com.cgnpc.bbxpark.meeting.dto.model.ImportReturnModel;
import com.cgnpc.bbxpark.settings.dto.model.FileModel;
import com.cgnpc.bbxpark.settings.dto.param.FilePageParam;
import com.cgnpc.bbxpark.traffic.service.ITrafficFileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

@Slf4j
@RestController
@RequestMapping(Constant.BASE_PATH + "/traffic/file")
@Api(tags = "智慧交通-PC端-交通文件管理")
public class TrafficFileController {
    /**
     * 餐厅服务接口.
     */
    @Autowired
    private ITrafficFileService trafficFileService;


    /**
     * 新增文件.
     */
    @ApiOperation(value = "导入文件")
    
    @PostMapping(value = "/import")
    public CudResult<ImportReturnModel> importFile(@RequestParam(value = "file") MultipartFile file,
                                                @RequestParam(value = "type") @ApiParam(value = "文件类型:41->车辆表;42->车辆保养信息表;43->轮胎更换记录表;44->车辆维修信息表;45->租车记录表;46->司机表;47->车辆行驶记录表;48->电召车出车记录;49->便民班车订单表;50->车辆费用结算表") Integer type) {
        return CudResult.success(trafficFileService.importFile(file, type));
    }
    /**
     * 删除文件.
     */
    @ApiOperation(value = "删除文件")
    @GetMapping(value = "/remove/{id}")
    public CudResult<Boolean> removeFile(@PathVariable @NotNull(message = "文件标识不能为空") Long id) {
        return CudResult.success(trafficFileService.removeFile(id));
    }
    /**
     * 文件列表(分页).
     */
    @ApiOperation(value = "文件列表(分页)")
    @PostMapping(value = "/page")
    public CudResult<IPage<FileModel>> pageFile(@RequestBody FilePageParam param) {
        return CudResult.success(trafficFileService.pageFile(param));
    }


    /**
     * 同步数据.
     */
    @ApiOperation(value = "同步数据")
    @GetMapping(value = "/sync/data")
    public CudResult<Boolean> syncData(@RequestParam(value = "type") @ApiParam(value = "文件类型:41->车辆表;42->车辆保养信息表;43->轮胎更换记录表;44->车辆维修信息表;45->租车记录表;46->司机表;47->车辆行驶记录表") Integer type) {
        return CudResult.success(trafficFileService.syncData(type));
    }


}
