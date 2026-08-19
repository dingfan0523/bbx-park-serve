
package com.cgnpc.bbxpark.log.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cgnpc.bbxpark.acl.uic.service.IUserApiService;
import com.cgnpc.bbxpark.common.utils.BeanUtils;
import com.cgnpc.bbxpark.common.utils.ConvertUtil;
import com.cgnpc.bbxpark.common.utils.WebFrameworkUtils;
import com.cgnpc.bbxpark.log.domain.OperateLog;
import com.cgnpc.bbxpark.log.mapper.OperateLogRepository;
import com.cgnpc.bbxpark.log.service.IOperateLogService;
import com.cgnpc.bbxpark.log.vo.OperateLogModel;
import com.cgnpc.bbxpark.log.vo.OperateLogPageParam;
import com.cgnpc.bbxpark.log.vo.OperateLogParam;
import com.cgnpc.bbxpark.space.dto.model.UserInfoModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


@Service("operateLogService")
public class OperateLogServiceImpl extends ServiceImpl<OperateLogRepository, OperateLog> implements IOperateLogService {
	/**
	 * 注入repository.
	 */
	@Autowired
	private OperateLogRepository operateLogRepository;

	@Autowired
	private IUserApiService userApiService;



	/**
	 * 根据操作日志标识获得操作日志详情信息.
	 * @Param [id] 操作日志标识
	 * @Return 操作日志详情信息
	 */
	@Override
	public OperateLogModel detail(Long id) {
		OperateLog operateLog = this.getById(id);
		OperateLogModel convert = BeanUtils.convertTo(operateLog, OperateLogModel::new);
        UserInfoModel staffInfo = userApiService.getByStaffNo(operateLog.getOperatorId());
        if (staffInfo != null) {
			convert.setUserName(staffInfo.getUserName());
		}
		return convert;
	}
    @Async
    @Override
    public void save(OperateLogParam operateLogParam) {
        OperateLog operateLog = BeanUtils.convertTo(operateLogParam, OperateLog::new);
        operateLogRepository.insert(operateLog);
    }

    /**
	 * 获取操作日志列表(分页).
	 *
	 * @Param param 操作日志查询条件
	 * @Return 操作日志信息列表（分页）
	 */
	@Override
	public IPage<OperateLogModel> page(OperateLogPageParam param) {
        IPage<OperateLog> page = new Page<>(param.getCurrent(), param.getSize());
        LambdaQueryWrapper<OperateLog> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(StrUtil.isNotBlank(param.getModuleName()), OperateLog::getModuleName, param.getModuleName());
        IPage<OperateLog> operateLogPage = this.page(page, queryWrapper);
        IPage<OperateLogModel> operateLogModelIPage = ConvertUtil.pageConvert(operateLogPage, BeanUtils.convertListTo(operateLogPage.getRecords(), OperateLogModel::new));
        if (CollUtil.isNotEmpty(operateLogModelIPage.getRecords())) {
            operateLogModelIPage.getRecords().forEach(item -> {
                if (item.getStatus() == 1) {
                    item.setStatusName("失败");
                } else {
                    item.setStatusName("成功");
                }
            });
		}
        return operateLogModelIPage;
    }



	/**
	 * 新增操作日志.
	 *
	 * @Param param 操作日志信息
	 * @Return 新增操作日志是否成功
	 */
	public Boolean add(OperateLogParam param) {
		OperateLog operateLog = BeanUtils.convertTo(param, OperateLog::new);
		operateLog.setTenantId(WebFrameworkUtils.getHeaderTenantId());
		operateLogRepository.insert(operateLog);
		return true;
	}



}
