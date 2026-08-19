package com.cgnpc.bbxpark.restaurant.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cgnpc.bbxpark.common.utils.BeanUtils;
import com.cgnpc.bbxpark.restaurant.domain.RestaurantTime;
import com.cgnpc.bbxpark.restaurant.dto.model.RestaurantTimeModel;
import com.cgnpc.bbxpark.restaurant.dto.param.RestaurantTimeListParam;
import com.cgnpc.bbxpark.restaurant.mapper.RestaurantTimeRepository;
import com.cgnpc.bbxpark.restaurant.service.IRestaurantTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RestaurantTimeServiceImpl extends ServiceImpl<RestaurantTimeRepository, RestaurantTime> implements IRestaurantTimeService {

    /**
     * 注入repository.
     */
	@Autowired
	private RestaurantTimeRepository restaurantTimeRepository;


	@Override
	public List<RestaurantTimeModel> timeList(RestaurantTimeListParam param) {
		LambdaQueryWrapper<RestaurantTime> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(param.getRestaurantId()!= null, RestaurantTime::getRestaurantId, param.getRestaurantId());
		queryWrapper.eq(param.getType()!= null, RestaurantTime::getType, param.getType());
		List<RestaurantTime> restaurantTimes = restaurantTimeRepository.selectList(queryWrapper);
		return BeanUtils.convertListTo(restaurantTimes, RestaurantTimeModel::new);
	}
}
