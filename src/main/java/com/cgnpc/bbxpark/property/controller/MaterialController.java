package com.cgnpc.bbxpark.property.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.bbxpark.common.constant.Constant;
import com.cgnpc.bbxpark.common.web.CudResult;
import com.cgnpc.bbxpark.property.dto.model.MaterialModel;
import com.cgnpc.bbxpark.property.dto.param.MaterialListParam;
import com.cgnpc.bbxpark.property.dto.param.MaterialPageParam;
import com.cgnpc.bbxpark.property.dto.param.MaterialParam;
import com.cgnpc.bbxpark.property.service.IMaterialService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/***
 * @Description 材料服务控制类
 * @author huangyongtao
 * @date 2025/9/23 11:34
 */
@RestController
@RequestMapping(Constant.BASE_PATH + "/material")
@Api(tags = "材料")
public class MaterialController {

    /**
     * 材料服务接口.
     */
    @Autowired
    private IMaterialService materialService;

    /**
     * 获取材料信息.
     */
    @ApiOperation(value = "获取材料信息")
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CudResult<MaterialModel> detail(@PathVariable Long id) {
        return  CudResult.success(materialService.detail(id));
    }

    /**
     * 获取材料列表(分页).
     */
    @ApiOperation(value = "获取材料列表(分页)")
    @PostMapping(value = "/page", produces = MediaType.APPLICATION_JSON_VALUE)
    public CudResult<IPage<MaterialModel>> page(@RequestBody MaterialPageParam param) {
        return  CudResult.success(materialService.page(param));
    }

    /**
     * 获取材料列表.
     */
    @ApiOperation(value = "获取材料列表")
    @PostMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public CudResult<List<MaterialModel>> list(@RequestBody MaterialListParam param) {
        return  CudResult.success(materialService.list(param));
    }

    /**
     * 新增材料.
     */
    @ApiOperation(value = "新增材料")
    @PostMapping(value = "/add")
    public CudResult<Boolean> add(@Validated//
                                      @RequestBody MaterialParam param) {
        return  CudResult.success(materialService.add(param));
    }


    /**
     * 删除材料.
     */
    @ApiOperation(value = "删除材料")
    @GetMapping(value = "/remove/{id}")
    public CudResult<Boolean> remove(@PathVariable Long id) {
        return  CudResult.success(materialService.remove(id));
    }

    /**
     * 编辑材料.
     */
    @ApiOperation(value = "编辑材料")
    @PostMapping(value = "/edit")
    public CudResult<Boolean> edit(@RequestBody MaterialParam param) {
        return  CudResult.success(materialService.edit(param));
    }

    /***
     *材料导出
     */
    @ApiOperation(value = "材料导出")
    @GetMapping(value = "/easyExport")
    public void easyExport(HttpServletResponse response, @ModelAttribute MaterialPageParam param) {
        materialService.materialEasyExport(response, param);
    }

}
