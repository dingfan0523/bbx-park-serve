
package com.cgnpc.bbxpark.space.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cgnpc.bbxpark.space.domain.Station;
import com.cgnpc.bbxpark.space.dto.model.*;
import com.cgnpc.bbxpark.space.dto.param.DepartmentMemberExParam;
import com.cgnpc.bbxpark.space.dto.param.StationListParam;
import com.cgnpc.bbxpark.space.dto.param.StationPageParam;
import com.cgnpc.bbxpark.space.dto.param.StationParam;

import java.util.List;


public interface IStationService extends IService<Station> {
	/**
	 * 获取空间工位列表(分页).
	 * @Param param 空间工位查询条件
	 * @Return 空间工位信息列表（分页）
	 */
	IPage<StationModel> page(StationPageParam param);

	/**
	 * 获取空间工位列表.
	 * @Param param 空间工位查询条件
	 * @Return 空间工位信息列表
	 */
	List<StationModel> list(StationListParam param);

	/**
	 * 分配空间工位.
	 * @Param param 空间工位信息
	 * @Return 分配空间工位是否成功
	 */
	Boolean allot(StationParam param);

	/**
	 * 校验空间工位.
	 * @param param
	 * @return
	 */
	Boolean check(StationParam param);

	/**
	 * 获取空间工位统计信息.
	 * @Return 统计信息
	 */
	StationStatisticsModel statistics(Long spaceId);

	/**
	 * 删除空间工位.
	 * @Param id 空间工位标识
	 * @Return 删除空间工位是否成功
	 */
	Boolean remove(Long id);

	/**
	 * 获取部门树.
	 * @param organizationId 组织机构标识
	 * @return 部门树DepartmentMemberModel
	 */
	List<OrgDeptExTreeNode> listOrgTree(Long organizationId);

	/**
	 * 获取部门成员列表(分页).
	 * @param param 部门成员查询条件
	 * @return 部门成员列表
	 */
	IPage<DepartmentMemberExModel> departmentMemberPage(DepartmentMemberExParam param);

	/***
	 * @Description 获取用户办公地点
	 * @author huangyongtao
	 * @date 2025/9/25 15:09
	 */
	ParkSpaceFullModel getUserStationSpace();
}
