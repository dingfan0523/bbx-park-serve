
package com.cgnpc.bbxpark.energy.controller;

import com.cgnpc.bbxpark.common.constant.Constant;
import com.cgnpc.bbxpark.common.enums.DeviceMeterMethodEnum;
import com.cgnpc.bbxpark.common.web.CudResult;
import com.cgnpc.bbxpark.energy.dto.model.MeterRecordBranchCountModel;
import com.cgnpc.bbxpark.energy.dto.model.MeterRecordCountModel;
import com.cgnpc.bbxpark.energy.dto.model.MeterRecordSonBranchCountModel;
import com.cgnpc.bbxpark.energy.dto.param.MeterRecordCountParam;
import com.cgnpc.bbxpark.energy.service.IMeterAutoRecordCountService;
import com.cgnpc.bbxpark.energy.service.IMeterPersonRecordCountService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/***
 * @Description 能耗流水服务控制类
 * @author huangyongtao
 * @date 2025/4/24 9:40
 */
@RestController
@RequestMapping(Constant.BASE_PATH + "/energy/record/count")
@Api(tags = "智慧能管-PC端-能耗统计")
public class MeterRecordCountController {


    /**
     * 抄表自动上报记录统计服务接口.
     */
    @Autowired
    private IMeterAutoRecordCountService meterAutoRecordCountService;

    /**
     * 抄表人工抄表记录统计服务接口.
     */
    @Autowired
    private IMeterPersonRecordCountService meterPersonRecordCountService;

    /**
     * 查询最新时间
     */
    @ApiOperation(value = "查询最新时间")
    @PostMapping(value = "/find/time", produces = MediaType.APPLICATION_JSON_VALUE)
    public CudResult<MeterRecordCountModel> findNewTime(@RequestBody MeterRecordCountParam param) {
        if(DeviceMeterMethodEnum.AUTO.getCode().equals(param.getMeterMethod())){
            return CudResult.success(meterAutoRecordCountService.findNewTime(param));
        }else{
            return CudResult.success(meterPersonRecordCountService.findNewTime(param));
        }

    }

    /**
     * 能耗概览
     */
    @ApiOperation(value = "能耗概览")
    @PostMapping(value = "/view", produces = MediaType.APPLICATION_JSON_VALUE)
    public CudResult<MeterRecordCountModel> energyCount(@RequestBody MeterRecordCountParam param) {
        if(DeviceMeterMethodEnum.AUTO.getCode().equals(param.getMeterMethod())){
            return CudResult.success(meterAutoRecordCountService.energyCount(param));
        }else{
            return CudResult.success(meterPersonRecordCountService.energyCount(param));
        }

    }

    /**
     * 支路能耗趋势
     */
    @ApiOperation(value = "支路能耗趋势")
    @PostMapping(value = "/branch/trend", produces = MediaType.APPLICATION_JSON_VALUE)
    public CudResult<List<MeterRecordBranchCountModel>> energyBranchCount(@RequestBody MeterRecordCountParam param) {
        if(DeviceMeterMethodEnum.AUTO.getCode().equals(param.getMeterMethod())){
            return CudResult.success(meterAutoRecordCountService.energyBranchCount(param));
        }else{
            return CudResult.success(meterPersonRecordCountService.energyBranchCount(param));
        }

    }

    /**
     * 子支路能耗概览
     */
    @ApiOperation(value = "子支路能耗概览")
    @PostMapping(value = "/son/branch/view", produces = MediaType.APPLICATION_JSON_VALUE)
    public CudResult<List<MeterRecordSonBranchCountModel>> energySonBranchCount(@RequestBody MeterRecordCountParam param) {
        if(DeviceMeterMethodEnum.AUTO.getCode().equals(param.getMeterMethod())){
            return CudResult.success(meterAutoRecordCountService.energySonBranchCount(param));
        }else{
            return CudResult.success(meterPersonRecordCountService.energySonBranchCount(param));
        }

    }

    /***
     *支路能耗趋势导出
     */
    @ApiOperation(value = "支路能耗趋势导出")
    @PostMapping(value = "/branch/trend/export", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public void energyBranchCountExport(HttpServletResponse response, @RequestBody MeterRecordCountParam param) {
        if(DeviceMeterMethodEnum.AUTO.getCode().equals(param.getMeterMethod())){
            meterAutoRecordCountService.energyBranchCountExport(response, param);
        }else{
            meterPersonRecordCountService.energyBranchCountExport(response, param);
        }
    }

    /***
     *子支路能耗概览导出
     */
    @ApiOperation(value = "子支路能耗概览导出")
    @PostMapping(value = "/son/branch/view/export", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public void energySonBranchCountExport(HttpServletResponse response, @RequestBody MeterRecordCountParam param) {
        if(DeviceMeterMethodEnum.AUTO.getCode().equals(param.getMeterMethod())){
            meterAutoRecordCountService.energySonBranchCountExport(response, param);
        }else{
            meterPersonRecordCountService.energySonBranchCountExport(response, param);
        }
    }

    /***
     *生成自动抄表集抄数据
     */
    @ApiOperation(value = "生成自动抄表集抄数据")
    @PostMapping(value = "/executeAutoReadingCount")
    public CudResult executeAutoReadingCount() {
        meterAutoRecordCountService.executeAutoReadingCount();
        meterPersonRecordCountService.executePersonReadingCount();
        return CudResult.success(true);
    }
}
