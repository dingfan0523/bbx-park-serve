package com.cgnpc.bbxpark.restaurant.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cgnpc.bbxpark.common.utils.AssertUtils;
import com.cgnpc.bbxpark.restaurant.domain.RestaurantSpace;
import com.cgnpc.bbxpark.restaurant.dto.param.RestaurantSpaceListParam;
import com.cgnpc.bbxpark.restaurant.mapper.RestaurantSpaceRepository;
import com.cgnpc.bbxpark.restaurant.service.IRestaurantSpaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RestaurantSpaceServiceImpl extends ServiceImpl<RestaurantSpaceRepository, RestaurantSpace> implements IRestaurantSpaceService {
    /**
     * 注入repository.
     */
	@Autowired
	private RestaurantSpaceRepository restaurantSpaceRepository;


	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public Boolean saveImg(List<RestaurantSpaceListParam> param) {
		param.forEach(e->{
			AssertUtils.notNull(e.getSpaceId(), "spaceId不能为空");
			AssertUtils.notNull(e.getRestaurantId(), "restaurantId不能为空");
			RestaurantSpace restaurantSpace = new RestaurantSpace();
			restaurantSpace.setFlowImageUrl(e.getFlowImageUrl());
			restaurantSpace.setMealLineImageUrl(e.getMealLineImageUrl());
			LambdaUpdateWrapper<RestaurantSpace> update = new LambdaUpdateWrapper<>();
			update.eq(RestaurantSpace::getSpaceId, e.getSpaceId());
			update.eq(RestaurantSpace::getRestaurantId, e.getRestaurantId());
			super.update(restaurantSpace, update);
		});
		return true;
	}
}
