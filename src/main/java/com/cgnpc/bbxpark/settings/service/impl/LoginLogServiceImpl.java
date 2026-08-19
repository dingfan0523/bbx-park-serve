
package com.cgnpc.bbxpark.settings.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cgnpc.bbxpark.acl.uic.service.IUserApiService;
import com.cgnpc.bbxpark.settings.domain.LoginLog;
import com.cgnpc.bbxpark.settings.dto.model.LoginLogModel;
import com.cgnpc.bbxpark.settings.dto.param.LoginLogPageParam;
import com.cgnpc.bbxpark.settings.service.ILoginLogService;
import com.cgnpc.bbxpark.common.utils.BeanUtils;
import com.cgnpc.bbxpark.common.utils.CollectionUtils;
import com.cgnpc.bbxpark.common.utils.ConvertUtil;
import com.cgnpc.bbxpark.log.mapper.LoginLogRepository;
import com.cgnpc.bbxpark.space.dto.model.UserInfoModel;
import com.cgnpc.cud.core.service.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("loginLogService")
public class LoginLogServiceImpl extends BaseServiceImpl<LoginLogRepository, LoginLog> implements ILoginLogService {
	/**
	 * 注入repository.
	 */
	@Autowired
	private LoginLogRepository loginLogRepository;

//	@Autowired
//	private UserInfoRepository userInfoRepository;

    @Autowired
    private IUserApiService userApiService;

	/**
	 * 根据登录日志标识获得登录日志详情信息.
	 * @Param [id] 登录日志标识
	 * @Return 登录日志详情信息
	 */
	@Override
	public LoginLogModel detail(Long id) {
		LoginLog loginLog = this.getById(id);
		LoginLogModel loginLogModel = BeanUtils.convertTo(loginLog, LoginLogModel::new);
        UserInfoModel userInfo = userApiService.getByStaffNo(loginLog.getUserId());
		if (userInfo != null) {
			loginLogModel.setUserName(userInfo.getUserName());
		}
		return loginLogModel;
	}

	/**
	 * 获取登录日志列表(分页).
	 *
	 * @Param param 登录日志查询条件
	 * @Return 登录日志信息列表（分页）
	 */
	@Override
	public IPage<LoginLogModel> page(LoginLogPageParam param) {
        IPage<LoginLog> filePage = this.page(new Page<>(param.getCurrent(), param.getSize()), Wrappers.lambdaQuery());
        return ConvertUtil.pageConvert(filePage,BeanUtils.convertListTo(filePage.getRecords(), LoginLogModel::new));
	}


	/**
	 * 批量删除登录日志.
	 * @Param ids 登录日志标识列表
	 * @Return 批量删除登录日志是否成功
	 */
	@Override
	public Boolean removeBatch(List<Long> ids) {
		if (CollectionUtils.isNotEmpty(ids)) {
			this.removeBatch(ids);
		}
		return true;
	}


	@Override
	public Boolean clean() {
		this.remove(Wrappers.emptyWrapper());
		return true;
	}
}
