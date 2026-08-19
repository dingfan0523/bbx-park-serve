package com.cgnpc.bbxpark.settings.api;

import com.cgnpc.bbxpark.common.web.CudResult;
import com.cgnpc.bbxpark.settings.dto.model.DictItemModel;
import com.cgnpc.bbxpark.settings.service.impl.DictServiceImpl;
import com.cgnpc.mobile.annotation.RequiredToken;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/***
 * @Description 字典服务控制类
 * @author huangyongtao
 * @date 2024/8/12 17:31
 */
@RestController
@RequestMapping("/api/dict/item")
@Api(tags = "BBX-移动端-数据字典")
public class ApiDictController {

    /**
     * 字典服务接口.
     */
    @Autowired
    private DictServiceImpl dictServiceImpl;


    /**
     * 获取字典项列表.
     */
    @ApiOperation(value = "获取字典项列表")
    @PostMapping(value = "/dictCodeList")
    @RequiredToken
    public CudResult<List<DictItemModel>> findDictItemList() {
        return CudResult.success(dictServiceImpl.findDictItemList());
    }


    /**
     * 通过字典编码获取字典项列表
     */
    @ApiOperation(value = "通过字典编码获取字典项列表")
    @PostMapping(value = "/findItemsByDictType")
    @RequiredToken
    public CudResult<List<DictItemModel>> findItemsByDictType(String dictType) {
        return CudResult.success(dictServiceImpl.findItemsByDictType(dictType));
    }

}
