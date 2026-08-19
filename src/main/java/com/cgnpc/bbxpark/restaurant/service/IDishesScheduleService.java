
package com.cgnpc.bbxpark.restaurant.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cgnpc.bbxpark.restaurant.domain.DishesSchedule;
import com.cgnpc.bbxpark.restaurant.dto.model.AppDishesScheduleModel;
import com.cgnpc.bbxpark.restaurant.dto.model.DishesScheduleModel;
import com.cgnpc.bbxpark.restaurant.dto.param.*;

import java.util.List;


public interface IDishesScheduleService extends IService<DishesSchedule> {

    /**
     * 获取菜品库列表(分页).
     * @Param param 菜品库查询条件
     * @Return 菜品库信息列表（分页）
     */
    IPage<DishesScheduleModel> page(DishesSchedulePageParam param);

    /**
     * 获取菜品排班列表
     * @param param 参数
     * @return 菜品排班列表
     */
    List<DishesScheduleModel> list(DishesScheduleListParam param);

    /**
     * 移动端-获取菜品排班列表
     * @param param 参数
     * @return 菜品排班列表
     */
    List<AppDishesScheduleModel> listApp(AppDishesScheduleListParam param);

    /**
     * 移动端-获取本周菜品类型
     * @param param 参数
     * @return 菜品类型
     */
    List<String> listType(DishesTypeListParam param);

    /**
     * 获取菜品排班详情
     * @param id id
     * @return 排班详情
     */
    DishesScheduleModel detail(Long id);

    /**
     * 移动端-获取菜品排班详情
     * @param id id
     * @return 排班详情
     */
    AppDishesScheduleModel detailApp(Long id);

    /**
     * 新增菜品排班
     * @param param 参数
     * @return 新增结果
     */
    Boolean add(DishesScheduleParam param);

    /**
     * 编辑菜品排班
     * @param param 参数
     * @return 编辑结果
     */
    Boolean edit(DishesScheduleParam param);

    /**
     * 上架菜品排班
     * @param id id
     * @return 结果
     */
    Boolean enable(Long id);

    /**
     * 下架菜品排班
     * @param id id
     * @return 结果
     */
    Boolean disable(Long id);

    /**
     * 删除菜品排班
     * @param id id
     * @return 结果
     */
    Boolean remove(Long id);



    /**
     * 批量新增菜品排班
     * @param dishesScheduleParams 参数
     * @return 新增结果
     */
    Boolean adds(List<DishesScheduleParam> dishesScheduleParams);

}
