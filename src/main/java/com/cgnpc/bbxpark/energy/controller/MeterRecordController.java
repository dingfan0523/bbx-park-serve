
package com.cgnpc.bbxpark.energy.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.bbxpark.common.constant.Constant;
import com.cgnpc.bbxpark.common.enums.DeviceMeterMethodEnum;
import com.cgnpc.bbxpark.common.web.CudResult;
import com.cgnpc.bbxpark.energy.dto.model.MeterRecordModel;
import com.cgnpc.bbxpark.energy.dto.param.MeterRecordPageParam;
import com.cgnpc.bbxpark.energy.service.IMeterAutoRecordService;
import com.cgnpc.bbxpark.energy.service.IMeterPersonRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/***
 * @Description 能耗流水服务控制类
 * @author huangyongtao
 * @date 2025/4/21 9:40
 */
@RestController
@RequestMapping(Constant.BASE_PATH + "/energy/record")
@Api(tags = "智慧能管-PC端-能耗流水")
public class MeterRecordController {


    /**
     * 抄表自动上报记录服务接口.
     */
    @Autowired
    private IMeterAutoRecordService meterAutoRecordService;

    /**
     * 抄表人工抄表记录服务接口.
     */
    @Autowired
    private IMeterPersonRecordService meterPersonRecordService;

    /**
     * 获取抄表自动上报记录列表(分页).
     */
    @ApiOperation(value = "获取抄表自动上报记录列表(分页)")
    @PostMapping(value = "/page", produces = MediaType.APPLICATION_JSON_VALUE)
    public CudResult<IPage<MeterRecordModel>> autoPage(@RequestBody MeterRecordPageParam param) {
        if(DeviceMeterMethodEnum.AUTO.getCode().equals(param.getMeterMethod())){
            return CudResult.success(meterAutoRecordService.page(param));
        }else{
            return CudResult.success(meterPersonRecordService.page(param));
        }

    }

    /***
     *抄表记录导出
     */
    @ApiOperation(value = "抄表记录导出")
    @GetMapping(value = "/meterRecordEasyExport")
    public void meterRecordEasyExport(HttpServletResponse response, @ModelAttribute MeterRecordPageParam param) {
        if(DeviceMeterMethodEnum.AUTO.getCode().equals(param.getMeterMethod())){
           meterAutoRecordService.meterAutoEasyExport(response, param);
        }else{
            meterPersonRecordService.meterPersonEasyExport(response, param);
        }
    }

    /***
     *生成自动抄表数据
     */
    @ApiOperation(value = "生成自动抄表数据")
    @PostMapping(value = "/executeAutoReading")
    public CudResult executeAutoReading() {
        return CudResult.success(meterAutoRecordService.executeAutoReading());
    }
}
