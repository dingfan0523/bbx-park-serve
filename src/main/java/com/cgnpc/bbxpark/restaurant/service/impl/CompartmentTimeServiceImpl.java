
package com.cgnpc.bbxpark.restaurant.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cgnpc.bbxpark.common.utils.BeanUtils;
import com.cgnpc.bbxpark.restaurant.domain.Compartment;
import com.cgnpc.bbxpark.restaurant.domain.CompartmentTime;
import com.cgnpc.bbxpark.restaurant.dto.model.CompartmentTimeModel;
import com.cgnpc.bbxpark.restaurant.mapper.CompartmentRepository;
import com.cgnpc.bbxpark.restaurant.mapper.CompartmentTimeRepository;
import com.cgnpc.bbxpark.restaurant.service.ICompartmentTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class CompartmentTimeServiceImpl extends ServiceImpl<CompartmentTimeRepository, CompartmentTime> implements ICompartmentTimeService {
    /**
     * 注入repository.
     */
	@Autowired
	private CompartmentTimeRepository compartmentTimeRepository;

	@Autowired
	private CompartmentRepository compartmentRepository;

	/**
	 * @Param: restaurantId 餐厅id
	 * @Author lhy
	 * @Date  2024/8/7
	 * @Description: 通过餐厅id 查询餐厅下引用包间的营业时间集合
	 */
	@Override
	public List<CompartmentTimeModel> list(Long restaurantId) {
		if (ObjectUtil.isEmpty(restaurantId)){
			return new ArrayList<>();
		}
		List<Compartment> compartmentList = compartmentRepository.selectList(new LambdaQueryWrapper<Compartment>().eq(Compartment::getRestaurantId, restaurantId));
		if (CollUtil.isEmpty(compartmentList)){
			return new ArrayList<>();
		}
		List<Long> compartmentIds = compartmentList.stream().map(e -> e.getId()).collect(Collectors.toList());
		if (CollUtil.isEmpty(compartmentIds)){
			return new ArrayList<>();
		}
		List<CompartmentTime> compartments = this.list(new LambdaQueryWrapper<CompartmentTime>().in(CompartmentTime::getCompartmentId, compartmentIds));
		return BeanUtils.convertListTo(compartments, CompartmentTimeModel::new);
	}

}
