package com.cgnpc.bbxpark.property.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.bbxpark.common.constant.Constant;
import com.cgnpc.bbxpark.common.web.CudResult;
import com.cgnpc.bbxpark.property.dto.model.MaterialRecordModel;
import com.cgnpc.bbxpark.property.dto.param.MaterialRecordListParam;
import com.cgnpc.bbxpark.property.dto.param.MaterialRecordPageParam;
import com.cgnpc.bbxpark.property.service.IMaterialRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/***
 * @Description 材料记录服务控制类
 * @author huangyongtao
 * @date 2025/9/23 11:41
 */
@RestController
@RequestMapping(Constant.BASE_PATH + "/material/record")
@Api(tags = "材料记录")
public class MaterialRecordController {

    /**
     * 材料记录服务接口.
     */
    @Autowired
    private IMaterialRecordService materialRecordService;

    /**
     * 获取材料记录信息.
     */
    @ApiOperation(value = "获取材料记录信息")
    @GetMapping(value = "/{id}")
    public CudResult<MaterialRecordModel> detail(@PathVariable Long id) {
        return CudResult.success(materialRecordService.detail(id));
    }

    /**
     * 获取材料记录列表(分页).
     */
    @ApiOperation(value = "获取材料记录列表(分页)")
    @PostMapping(value = "/page")
    public CudResult<IPage<MaterialRecordModel>> page(@RequestBody MaterialRecordPageParam param) {
        return CudResult.success(materialRecordService.page(param));
    }

    /**
     * 获取材料记录列表.
     */
    @ApiOperation(value = "获取材料记录列表")
    @PostMapping(value = "/list")
    public CudResult<List<MaterialRecordModel>> list(@RequestBody MaterialRecordListParam param) {
        return CudResult.success(materialRecordService.list(param));
    }

    /***
     *材料记录导出
     */
    @ApiOperation(value = "材料记录导出")
    @GetMapping(value = "/easyExport")
    public void easyExport(HttpServletResponse response, @ModelAttribute MaterialRecordPageParam param) {
        materialRecordService.materialRecordEasyExport(response, param);
    }

}
