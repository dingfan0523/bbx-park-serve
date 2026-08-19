
package com.cgnpc.bbxpark.energy.api;

import com.cgnpc.bbxpark.common.enums.DeviceMeterMethodEnum;
import com.cgnpc.bbxpark.common.web.CudResult;
import com.cgnpc.bbxpark.device.dto.model.IocDeviceMeterCountModel;
import com.cgnpc.bbxpark.device.service.IIocDeviceCountService;
import com.cgnpc.bbxpark.energy.dto.model.MeterRecordCountModel;
import com.cgnpc.bbxpark.energy.dto.model.MeterRecordSonBranchCountModel;
import com.cgnpc.bbxpark.energy.dto.param.MeterRecordCountParam;
import com.cgnpc.bbxpark.energy.service.IMeterAutoRecordCountService;
import com.cgnpc.bbxpark.energy.service.IMeterPersonRecordCountService;
import com.cgnpc.mobile.annotation.RequiredToken;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/***
 * @Description 能耗流水服务控制类
 * @author huangyongtao
 * @date 2025/4/24 9:40
 */
@RestController
@RequestMapping("/api/app/energy/record/count")
@Api(tags = "智慧能管-移动端-能耗统计")
public class AppMeterRecordCountController {


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

    @Autowired
    private IIocDeviceCountService iocDeviceCountService;

    /**
     * 抄表设备概览
     */
    @ApiOperation(value = "抄表设备概览")
    @PostMapping(value = "/device/view")
    @RequiredToken
    public CudResult<IocDeviceMeterCountModel> energyCount() {
        return CudResult.success(iocDeviceCountService.meterDeviceCount());
    }

    /**
     * 能耗概览
     */
    @ApiOperation(value = "能耗概览")
    @PostMapping(value = "/view")
    @RequiredToken
    public CudResult<MeterRecordCountModel> energyCount(@RequestBody MeterRecordCountParam param) {
        if(DeviceMeterMethodEnum.AUTO.getCode().equals(param.getMeterMethod())){
            return CudResult.success(meterAutoRecordCountService.energyCount(param));
        }else{
            return CudResult.success(meterPersonRecordCountService.energyCount(param));
        }

    }

    /**
     * 子支路能耗概览
     */
    @ApiOperation(value = "子支路能耗概览")
    @PostMapping(value = "/son/branch/view")
    @RequiredToken
    public CudResult<List<MeterRecordSonBranchCountModel>> energySonBranchCount(@RequestBody MeterRecordCountParam param) {
        if(DeviceMeterMethodEnum.AUTO.getCode().equals(param.getMeterMethod())){
            return CudResult.success(meterAutoRecordCountService.appEnergySonBranchCount(param));
        }else{
            return CudResult.success(meterPersonRecordCountService.appEnergySonBranchCount(param));
        }

    }

}
