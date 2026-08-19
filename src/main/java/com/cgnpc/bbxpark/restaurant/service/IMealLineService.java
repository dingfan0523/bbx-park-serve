
package com.cgnpc.bbxpark.restaurant.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cgnpc.bbxpark.restaurant.domain.MealLine;
import com.cgnpc.bbxpark.restaurant.dto.model.AppMealLineModel;
import com.cgnpc.bbxpark.restaurant.dto.model.MealLineModel;
import com.cgnpc.bbxpark.restaurant.dto.param.AppMealLineListParam;
import com.cgnpc.bbxpark.restaurant.dto.param.MealLineListParam;
import com.cgnpc.bbxpark.restaurant.dto.param.MealLinePageParam;
import com.cgnpc.bbxpark.restaurant.dto.param.MealLineParam;

import java.util.List;

/**
 * 餐线服务接口
 * @author dingfan
 * @date 2024/7/18
 */
public interface IMealLineService extends IService<MealLine> {
    /**
     * 获取餐线列表(分页).
     * @Param param 餐线查询条件
     * @Return 餐线信息列表（分页）
     */
    IPage<MealLineModel> page(MealLinePageParam param);

    /**
     * 获取餐线列表
     * @param param 参数
     * @return 餐线列表
     */
    List<MealLineModel> list(MealLineListParam param);

    /**
     * 移动端-获取餐线列表
     * @param param 参数
     * @return 餐线列表
     */
    List<AppMealLineModel> listApp(AppMealLineListParam param);

    /**
     * 获取餐线详情
     * @param id id
     * @return 排班详情
     */
    MealLineModel detail(Long id);


    /**
     * 新增餐线
     * @param param 参数
     * @return 新增结果
     */
    Boolean add(MealLineParam param);

    /**
     * 编辑餐线
     * @param param 参数
     * @return 编辑结果
     */
    Boolean edit(MealLineParam param);

    /**
     * 上架餐线
     * @param id id
     * @return 结果
     */
    Boolean enable(Long id);

    /**
     * 下架餐线
     * @param id id
     * @return 结果
     */
    Boolean disable(Long id);

    /**
     * 删除餐线
     * @param id id
     * @return 结果
     */
    Boolean remove(Long id);

    /**
     * 删除餐厅下的餐线
     * @param id 餐厅id
     * @return 结果
     */
    Boolean removeByRestaurantId(Long id);
}
