
package com.cgnpc.bbxpark.restaurant.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cgnpc.bbxpark.restaurant.domain.CompartmentReserve;
import com.cgnpc.bbxpark.restaurant.dto.model.*;
import com.cgnpc.bbxpark.restaurant.dto.param.*;

import java.util.List;

/***
 * @Description 包间预定服务接口
 * @author huangyongtao
 * @date 2024/7/30 15:00
 */
public interface ICompartmentReserveService extends IService<CompartmentReserve> {

	/**
	 * 根据包间预定标识获得包间预定详情信息.
	 * @Param [id] 包间预定标识
	 * @Return 包间预定详情信息
	 */
	CompartmentReserveModel detail(Long id);

	/**
	 * 移动端-根据包间预定标识获得包间预定详情信息
	 * @param id 包间预定id
	 * @return 包间预定详情信息
	 */
	AppCompartmentReserveDetailModel detailApp(Long id);

	/**
	 * 移动端-查询包间相关信息(可预定时间及套餐)
	 * @return 包间相关信息
	 */
	AppCompartmentExModel findCompartmentEx(AppCompartmentReserveTimeListParam param);

	/**
	 * 获取包间预定列表(分页).
	 * @Param param 包间预定查询条件
	 * @Return 包间预定信息列表（分页）
	 */
	IPage<CompartmentReserveModel> page(CompartmentReservePageParam param);

	/**
	 * 移动端-获取包间预定列表(分页).
	 * @Param param 包间预定查询条件
	 * @Return 包间预定信息列表（分页）
	 */
	IPage<AppCompartmentReserveModel> pageApp(AppCompartmentReservePageParam param);

	/**
	 * 获取包间预定列表.
	 * @Param param 包间预定查询条件
	 * @Return 包间预定信息列表
	 */
	List<CompartmentReserveGroupModel> list(CompartmentReserveListParam param);

	/**
	 * 移动端-获取包间预定列表.
	 * @Param param 包间预定查询条件
	 * @Return 包间预定信息列表
	 */
	List<AppCompartmentReserveGroupModel> listApp(AppCompartmentReserveListParam param);

	/**
	 * 新增包间预定.
	 * @Param param 包间预定信息
	 * @Return 新增包间预定是否成功
	 */
	Long add(CompartmentReserveParam param);

	/**
	 * 编辑包间预定信息.
	 * @Param param 包间预定信息
	 * @Return 编辑包间预定是否成功
	 */
	Boolean edit(CompartmentReserveParam param);

	/***
	 * @Description 取消包间预约
	 * @author huangyongtao
	 * @date 2024/7/31 17:01
	 * @param param
	 */
	Boolean cancel(CompartmentReserveParam param);

	/**
	 * 移动端-取消包间预约
	 * @param param 参数
	 * @return 结果
	 */
	Boolean cancelApp(CompartmentReserveParam param);

	/***
	 * @Description 到店
	 * @author huangyongtao
	 * @date 2024/7/31 17:01
	 * @param param
	 */
	Boolean arrive(CompartmentReserveParam param);

	/***
	 * @Description 查询包间可预订的时间
	 * @author huangyongtao
	 * @date 2024/8/2 10:05
	 * @param param
	 */
	List<CompartmentTimeModel> findTime(CompartmentReserveParam param);

	List<AppCompartmentTimeExModel> findTimeApp(AppCompartmentReserveTimeListParam param);

	/***
	 * @Description 更新过期状态
	 * @author huangyongtao
	 * @date 2024/8/2 10:05
	 */
	Boolean updateStatus();

	/***
	 * @Description 查询包间预约信息
	 * @author huangyongtao
	 * @date 2024/8/7 17:12
	 * @param param
	 */
	List<CompartmentReserveModel> findCompartmentReserve(CompartmentReserveParam param);

	/***
	 * @Description 查询包间营业时间范围
	 * @author huangyongtao
	 * @date 2024/8/8 14:41
	 * @param param
	 */
	CompartmentTimeModel findTimeRange(CompartmentReserveParam param);

	/**
	 * 移动端-获取最近的一条预约信息
	 * @return 预约信息
	 */
	AppSimpleReserveModel getNearest();
}
