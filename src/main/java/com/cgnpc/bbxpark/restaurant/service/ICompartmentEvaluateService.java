
package com.cgnpc.bbxpark.restaurant.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cgnpc.bbxpark.restaurant.domain.CompartmentEvaluate;
import com.cgnpc.bbxpark.restaurant.dto.model.CompartmentEvaluateModel;
import com.cgnpc.bbxpark.restaurant.dto.param.CompartmentEvaluatePageParam;
import com.cgnpc.bbxpark.restaurant.dto.param.CompartmentEvaluateParam;

import java.util.List;


public interface ICompartmentEvaluateService extends IService<CompartmentEvaluate> {
	/**
	 * 包间评价列表
	 * @param param 分页查询条件
	 * @return 包间评价列表
	 */
	IPage<CompartmentEvaluateModel> page(CompartmentEvaluatePageParam param);

	/**
	 * 包间评价列表
	 * @param compartmentId 包间id
	 * @return 包间评价列表
	 */
	List<CompartmentEvaluateModel> list(Long compartmentId);

	/**
	 * 新增包间评价
	 * @param param 参数
	 * @return 新增结果
	 */
	Boolean add(CompartmentEvaluateParam param);
}
