
package com.cgnpc.bbxpark.restaurant.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.cgnpc.bbxpark.restaurant.domain.CompartmentTime;
import com.cgnpc.bbxpark.restaurant.dto.model.CompartmentTimeModel;

import java.util.List;


public interface ICompartmentTimeService extends IService<CompartmentTime> {
  /**
    * @Param: restaurantId 餐厅id
    * @Author lhy
    * @Date  2024/8/7
    * @Description: 通过餐厅id 查询餐厅下引用包间的营业时间集合
    */
    List<CompartmentTimeModel> list(Long restaurantId);
}
