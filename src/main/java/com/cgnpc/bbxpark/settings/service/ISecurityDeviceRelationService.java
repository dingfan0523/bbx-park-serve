
package com.cgnpc.bbxpark.settings.service;

import com.cgnpc.bbxpark.settings.domain.SecurityDeviceRelation;
import com.cgnpc.bbxpark.settings.dto.model.SecurityDeviceRelationModel;
import com.cgnpc.bbxpark.settings.dto.param.SecurityDeviceRelationListParam;
import com.cgnpc.bbxpark.settings.dto.param.SecurityDeviceRelationParam;
import com.cgnpc.cud.core.service.IBaseService;

import java.util.List;

/***
 * @Description 安全管理员设备关联服务接口
 * @author huangyongtao
 * @date 2025/8/1 11:33
 */
public interface ISecurityDeviceRelationService extends IBaseService<SecurityDeviceRelation> {


	/**
	 * 获取安全管理员设备关联列表.
	 * @Param param 安全管理员设备关联查询条件
	 * @Return 安全管理员设备关联信息列表
	 */
	List<SecurityDeviceRelationModel> list(SecurityDeviceRelationListParam param);

	/**
	 * 新增安全管理员设备关联.
	 * @Param param 安全管理员设备关联信息
	 * @Return 新增安全管理员设备关联是否成功
	 */
	Boolean add(SecurityDeviceRelationParam param);

	/***
	 * @Description 查询当前登录用户所管理的设备id
	 * @author huangyongtao
	 * @date 2025/8/1 14:06
	 */
	List<Long> queryAllDeviceIdByLoginUser();

	/***
	 * @Description 通过空间id查询安全管理员设备列表
	 * @author huangyongtao
	 * @date 2025/8/4 15:52
	 * @param spaceIds
	 */
	List<SecurityDeviceRelationModel> findAllBySpaceIds(List<Long> spaceIds);
}
