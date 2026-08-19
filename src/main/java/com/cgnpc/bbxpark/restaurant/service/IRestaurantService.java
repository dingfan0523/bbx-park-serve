
package com.cgnpc.bbxpark.restaurant.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cgnpc.bbxpark.restaurant.domain.Restaurant;
import com.cgnpc.bbxpark.restaurant.dto.model.RestaurantModel;
import com.cgnpc.bbxpark.restaurant.dto.model.RestaurantSpaceModel;
import com.cgnpc.bbxpark.restaurant.dto.param.*;
import com.cgnpc.bbxpark.settings.dto.model.ConfigInfoModel;

import java.util.List;


public interface IRestaurantService extends IService<Restaurant>{

    Boolean add(RestaurantParam param);

    Boolean edit(RestaurantParam param);

    IPage<RestaurantModel> pageRestaurantModel(RestaurantPageParam param);
    IPage<RestaurantModel> pageAppRestaurantModel(RestaurantPageParam param);

    List<RestaurantModel> listAppRestaurantModel(RestaurantListParam param);
    List<RestaurantModel> listRestaurantModel(RestaurantListParam param);

    RestaurantModel detail(Long id);

    Boolean notificationSave(RestaurantNotificationParam param);

    Boolean enable(Long id);

    Boolean disable(Long id);

    Boolean removeRestaurantById(Long id);

    List<RestaurantSpaceModel> imgList(RestaurantSpaceParam param);

    ConfigInfoModel configNotic(ConfigInfoModel param);

    ConfigInfoModel configNotice(Long tenantId);
}
