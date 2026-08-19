
package com.cgnpc.bbxpark.restaurant.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cgnpc.bbxpark.restaurant.domain.DishesGallery;
import com.cgnpc.bbxpark.restaurant.dto.model.DishesGalleryModel;
import com.cgnpc.bbxpark.restaurant.dto.param.DishesGalleryListParam;
import com.cgnpc.bbxpark.restaurant.dto.param.DishesGalleryPageParam;
import com.cgnpc.bbxpark.restaurant.dto.param.DishesGalleryParam;
import com.cgnpc.bbxpark.config.minio.model.FileModel;

import java.util.List;


public interface IDishesGalleryService extends IService<DishesGallery> {

    /**
     * 导入菜品库压缩包
     * @return
     */
    boolean importDishes(List<FileModel> fileModels);

    /**
     * 新增菜品库
     * @param param 新增菜品入参
     * @return 新增结果
     */
    Boolean add(DishesGalleryParam param);

    /**
     * 菜品库分页列表
     * @param param 分页查询参数
     * @return 分页结果
     */
    IPage<DishesGalleryModel> pageDishesGalleryModel(DishesGalleryPageParam param);

    /**
     * 菜品库列表
     * @param param 查询参数
     * @return 菜品库列表
     */
    List<DishesGalleryModel> dishesGalleryModel(DishesGalleryListParam param);

    /**
     * 删除菜品库
     * @param id 菜品标识
     * @return 删除结果
     */
    Boolean removeDishesGallery(Long id);
}
