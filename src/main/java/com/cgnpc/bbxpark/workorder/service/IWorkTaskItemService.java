
package com.cgnpc.bbxpark.workorder.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cgnpc.bbxpark.workorder.domain.WorkTaskItem;
import com.cgnpc.bbxpark.workorder.dto.model.WorkTaskItemModel;
import com.cgnpc.bbxpark.workorder.dto.param.WorkTaskItemParam;

import java.util.List;

/***
 * @Description 工单任务项服务接口
 * @author huangyongtao
 * @date 2025/11/4 16:35
 */
public interface IWorkTaskItemService extends IService<WorkTaskItem> {

	/**
	 * 获取工单任务项列表.
	 * @Param workId 工单标识
	 * @Return 工单任务项信息列表
	 */
	List<WorkTaskItemModel> list(Long workId);

	/**
	 * 新增工单任务项.
	 * @Param param 工单任务项信息
	 * @Return 新增工单任务项是否成功
	 */
	Boolean add(WorkTaskItemParam param);

	/**
	 * 批量新增工单任务项.
	 * @Param params 工单任务项信息列表
	 * @Return 批量新增工单任务项是否成功
	 */
	Boolean addBatch(List<WorkTaskItemParam> params);

	/**
	 * 删除工单任务项.
	 * @Param id 工单任务项标识
	 * @Return 删除工单任务项是否成功
	 */
	Boolean remove(Long id);
}
