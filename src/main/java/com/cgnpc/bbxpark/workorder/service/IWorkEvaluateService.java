
package com.cgnpc.bbxpark.workorder.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cgnpc.bbxpark.workorder.domain.WorkEvaluate;
import com.cgnpc.bbxpark.workorder.dto.model.WorkEvaluateModel;
import com.cgnpc.bbxpark.workorder.dto.param.WorkEvaluateParam;

import java.util.List;

/***
 * @Description 工单评价服务接口
 * @author huangyongtao
 * @date 2025/11/4 16:35
 */
public interface IWorkEvaluateService extends IService<WorkEvaluate> {

	/**
	 * 获取工单评价列表.
	 * @Param workId 工单标识
	 * @Return 工单评价信息列表
	 */
	List<WorkEvaluateModel> list(Long workId);

	/**
	 * 新增工单评价.
	 * @Param param 工单评价信息
	 * @Return 新增工单评价是否成功
	 */
	Boolean add(WorkEvaluateParam param);

	/**
	 * 删除工单评价.
	 * @Param id 工单评价标识
	 * @Return 删除工单评价是否成功
	 */
	Boolean remove(Long id);
}
