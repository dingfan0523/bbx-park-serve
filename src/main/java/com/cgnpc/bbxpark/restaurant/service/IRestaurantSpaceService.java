package com.cgnpc.bbxpark.restaurant.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.cgnpc.bbxpark.restaurant.domain.RestaurantSpace;
import com.cgnpc.bbxpark.restaurant.dto.param.RestaurantSpaceListParam;

import java.util.List;

public interface IRestaurantSpaceService extends IService<RestaurantSpace> {

    Boolean saveImg(List<RestaurantSpaceListParam> param);
}
