package com.cgnpc.bbxpark.restaurant.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.cgnpc.bbxpark.restaurant.domain.RestaurantTime;
import com.cgnpc.bbxpark.restaurant.dto.model.RestaurantTimeModel;
import com.cgnpc.bbxpark.restaurant.dto.param.RestaurantTimeListParam;

import java.util.List;


public interface IRestaurantTimeService extends IService<RestaurantTime> {

    List<RestaurantTimeModel> timeList(RestaurantTimeListParam param);
}
