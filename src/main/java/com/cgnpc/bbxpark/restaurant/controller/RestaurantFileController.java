
package com.cgnpc.bbxpark.restaurant.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.bbxpark.common.constant.Constant;
import com.cgnpc.bbxpark.common.constant.InsertGroup;
import com.cgnpc.bbxpark.common.constant.UpdateGroup;
import com.cgnpc.bbxpark.common.utils.WebFrameworkUtils;
import com.cgnpc.bbxpark.common.web.CudResult;
import com.cgnpc.bbxpark.meeting.dto.model.ImportReturnModel;
import com.cgnpc.bbxpark.restaurant.dto.model.RestaurantModel;
import com.cgnpc.bbxpark.restaurant.dto.model.RestaurantSpaceModel;
import com.cgnpc.bbxpark.restaurant.dto.model.RestaurantTimeModel;
import com.cgnpc.bbxpark.restaurant.dto.param.*;
import com.cgnpc.bbxpark.restaurant.service.IRestaurantFileService;
import com.cgnpc.bbxpark.restaurant.service.IRestaurantService;
import com.cgnpc.bbxpark.restaurant.service.IRestaurantSpaceService;
import com.cgnpc.bbxpark.restaurant.service.IRestaurantTimeService;
import com.cgnpc.bbxpark.settings.dto.model.FileModel;
import com.cgnpc.bbxpark.settings.dto.param.FilePageParam;
import com.cgnpc.bbxpark.settings.dto.param.FileParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(Constant.BASE_PATH + "/restaurant/file")
@Api(tags = "智慧餐厅-PC端-餐厅文件管理")
public class RestaurantFileController {
    /**
     * 餐厅服务接口.
     */
    @Autowired
    private IRestaurantFileService restaurantFileService;


    /**
     * 新增文件.
     */
    @ApiOperation(value = "导入文件")
    
    @PostMapping(value = "/import")
    public CudResult<ImportReturnModel> importFile(@RequestParam(value = "file") MultipartFile file,
                                                @RequestParam(value = "type") @ApiParam(value = "文件类型:31->一卡通消费信息;32->餐厅垃圾处理信息;33->入库餐料理信息;34->餐料库存信息") Integer type) {
        return CudResult.success(restaurantFileService.importFile(file, type));
    }
    /**
     * 删除文件.
     */
    @ApiOperation(value = "删除文件")
    @GetMapping(value = "/remove/{id}")
    public CudResult<Boolean> removeFile(@PathVariable @NotNull(message = "文件标识不能为空") Long id) {
        return CudResult.success(restaurantFileService.removeFile(id));
    }
    /**
     * 文件列表(分页).
     */
    @ApiOperation(value = "文件列表(分页)")
    @PostMapping(value = "/page")
    public CudResult<IPage<FileModel>> pageFile(@RequestBody FilePageParam param) {
        return CudResult.success(restaurantFileService.pageFile(param));
    }


}
