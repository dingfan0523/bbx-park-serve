
package com.cgnpc.bbxpark.settings.controller;

import com.cgnpc.bbxpark.settings.dto.model.DictItemModel;
import com.cgnpc.bbxpark.settings.service.impl.DictServiceImpl;
import com.cgnpc.bbxpark.common.constant.Constant;
import com.cgnpc.bbxpark.common.web.CudResult;
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
@RequestMapping(Constant.BASE_PATH + "/dict/item")
@Api(tags = "数据字典")
public class DictController {

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
    public CudResult<List<DictItemModel>> findDictItemList() {
        return CudResult.success(dictServiceImpl.findDictItemList());
    }

}
