
package com.cgnpc.bbxpark.restaurant.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cgnpc.bbxpark.restaurant.domain.DishesEvaluate;
import com.cgnpc.bbxpark.restaurant.dto.model.DishesEvaluateModel;
import com.cgnpc.bbxpark.restaurant.dto.param.DishesEvaluatePageParam;
import com.cgnpc.bbxpark.restaurant.dto.param.DishesEvaluateParam;

import java.util.List;


public interface IDishesEvaluateService extends IService<DishesEvaluate> {
    /**
     * 菜品评价列表
     * @param param 分页查询条件
     * @return 菜品评价列表
     */
    IPage<DishesEvaluateModel> page(DishesEvaluatePageParam param);

    /**
     * 菜品评价列表
     * @param name 菜品名称
     * @return 菜品评价列表
     */
    List<DishesEvaluateModel> list(String name);

    /**
     * 新增菜品评价
     * @param param 参数
     * @return 新增结果
     */
    Boolean add(DishesEvaluateParam param);

    /**
     * 计算菜品平均分
     * @param name 菜品名称
     * @param satisfaction 本次评价分
     * @return 平均分
     */
    Double calculateAverage(String name,Integer satisfaction);

    /**
     * 计算菜品平均分并更新
     * @param name 菜品名称
     * @param satisfaction 本次评价分
     * @return 平均分
     */
    Double calculateAverageAndUpdate(String name,Integer satisfaction);
}
