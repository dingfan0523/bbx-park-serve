
package com.cgnpc.bbxpark.log.service;



import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cgnpc.bbxpark.log.domain.OperateLog;
import com.cgnpc.bbxpark.log.vo.OperateLogModel;
import com.cgnpc.bbxpark.log.vo.OperateLogPageParam;
import com.cgnpc.bbxpark.log.vo.OperateLogParam;


public interface IOperateLogService extends IService<OperateLog> {

	/**
	 * 获取操作日志列表(分页).
	 *
	 * @Param param 操作日志查询条件
	 * @Return 操作日志信息列表（分页）
	 */
    IPage<OperateLogModel> page(OperateLogPageParam param);

	/**
	 * 新增操作日志.
	 * @Param param 操作日志信息
	 * @Return 新增操作日志是否成功
	 */
	Boolean add(OperateLogParam param);

    /**
     * 根据操作日志标识获得操作日志详情信息.
     * @Param [id] 操作日志标识
     * @Return 操作日志详情信息
     */
    OperateLogModel detail(Long id);

    void save(OperateLogParam operateLogParam);
}
