package com.cgnpc.bbxpark.space.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.cgnpc.bbxpark.space.domain.UserSpace;
import com.cgnpc.bbxpark.space.dto.model.ParkSpaceTreeModel;
import com.cgnpc.bbxpark.space.dto.model.UserInfoModel;
import com.cgnpc.bbxpark.space.dto.model.UserSpaceModel;
import com.cgnpc.bbxpark.space.dto.param.UserInfoSpaceParam;
import com.cgnpc.bbxpark.space.dto.param.UserSpaceListParam;
import com.cgnpc.bbxpark.space.dto.param.UserSpaceParam;

import java.util.List;

/***
 * @Description 用户与空间访问权限服务接口
 * @author huangyongtao
 * @date 2024/7/1 16:34
 */
public interface IUserSpaceService extends IService<UserSpace> {

	/***
	 * @Description 获取用户相关的空间树
	 * @author huangyongtao
	 * @date 2024/7/2 11:07
	 * @param param
	 */
	List<ParkSpaceTreeModel> findTree(UserSpaceListParam param);

	/***
	 * @Description 查询用户分配的空间树
	 * @author huangyongtao
	 * @date 2024/7/25 11:40
	 * @param param
	 */
	List<ParkSpaceTreeModel> tree(UserSpaceListParam param);

	/**
	 * 获取用户与空间访问权限列表.
	 * @Param param 用户与空间访问权限查询条件
	 * @Return 用户与空间访问权限信息列表
	 */
	List<UserSpaceModel> list(UserSpaceListParam param);

	/**
	 * 新增用户与空间访问权限.
	 * @Param param 用户与空间访问权限信息
	 * @Return 新增用户与空间访问权限是否成功
	 */
	Boolean add(UserSpaceParam param);

	/**
	 * 删除用户与空间访问权限.
	 * @Param id 用户与空间访问权限标识
	 * @Return 删除用户与空间访问权限是否成功
	 */
	Boolean remove(Long id);

	/**
	 * 批量删除用户与空间访问权限.
	 * @Param ids 用户与空间访问权限标识列表
	 * @Return 批量删除用户与空间访问权限是否成功
	 */
	Boolean removeBatch(List<Long> ids);

	/***
	 * @Description 获取用户空间权限id集合
	 * @author huangyongtao
	 * @date 2024/8/21 11:45
	 */
	List<Long> findSpaceIds();

	/***
	 * @Description 根据空间id获取用户信息集合
	 * @author huangyongtao
	 * @date 2025/3/17 11:58
	 * @param condition
	 */
	List<UserInfoModel> findUserInfoBySpace(UserInfoSpaceParam condition);

}
