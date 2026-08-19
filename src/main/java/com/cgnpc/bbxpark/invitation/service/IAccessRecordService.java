
package com.cgnpc.bbxpark.invitation.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cgnpc.bbxpark.invitation.domain.AccessRecord;
import com.cgnpc.bbxpark.invitation.dto.model.AccessRecordModel;
import com.cgnpc.bbxpark.invitation.dto.param.AccessRecordPageParam;

import javax.servlet.http.HttpServletResponse;


public interface IAccessRecordService extends IService<AccessRecord> {


	/**
	 * 获取通行记录列表(分页).
	 * @Param param 通行记录查询条件
	 * @Return 通行记录信息列表（分页）
	 */
	IPage<AccessRecordModel> page(AccessRecordPageParam param);

	/**
	 * 获取通行记录详情.
	 */
	AccessRecordModel detail(Long id);

	/**
	 * 导出访客通行记录.
	 * @param response 响应对象
	 * @param param 通行记录查询条件
	 * @return 导出结果
	 */
	Boolean export(HttpServletResponse response, AccessRecordPageParam param);

	/**
	 * 导出门禁通行记录.
	 * @param response 响应对象
	 * @param param 通行记录查询条件
	 * @return 导出结果
	 */
	Boolean doorExport(HttpServletResponse response, AccessRecordPageParam param);
}
