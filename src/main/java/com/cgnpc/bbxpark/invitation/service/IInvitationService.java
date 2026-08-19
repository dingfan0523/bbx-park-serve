
package com.cgnpc.bbxpark.invitation.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cgnpc.bbxpark.invitation.domain.Invitation;
import com.cgnpc.bbxpark.invitation.dto.model.InvitationApproveModel;
import com.cgnpc.bbxpark.invitation.dto.model.InvitationModel;
import com.cgnpc.bbxpark.invitation.dto.param.InvitationListParam;
import com.cgnpc.bbxpark.invitation.dto.param.InvitationPageParam;
import com.cgnpc.bbxpark.invitation.dto.param.InvitationParam;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/***
 * @Description 邀约服务接口
 * @author huangyongtao
 * @date 2025/8/1 14:21
 */
public interface IInvitationService extends IService<Invitation> {

	/**
	 * 根据邀约标识获得邀约详情信息.
	 * @Param [id] 邀约标识
	 * @Return 邀约详情信息
	 */
	InvitationModel detail(Long id, Boolean flag);

	/**
	 * 获取邀约列表(分页).
	 * @Param param 邀约查询条件
	 * @Return 邀约信息列表（分页）
	 */
	IPage<InvitationModel> page(InvitationPageParam param);

	/**
	 * app获取邀约列表(分页).
	 * @Param param 邀约查询条件
	 * @Return 邀约信息列表（分页）
	 */
	IPage<InvitationModel> appPage(InvitationPageParam param);

	/**
	 * 获取邀约列表.
	 * @Param param 邀约查询条件
	 * @Return 邀约信息列表
	 */
	List<InvitationModel> list(InvitationListParam param);

	/**
	 * 新增邀约.
	 * @Param param 邀约信息
	 * @Return 新增邀约是否成功
	 */
	Long add(InvitationParam param);

	/**
	 * 取消邀约.
	 * @Param id 邀约标识
	 * @Return 取消邀约是否成功
	 */
	Boolean cancel(InvitationParam param);

	/**
	 * 审核邀约.
	 * @Param id 邀约标识
	 * @Return 取消邀约是否成功
	 */
	Boolean approve(Long id, Boolean approve, String remark);

	/***
	 * @Description 查询区域的审批信息
	 * @author huangyongtao
	 * @date 2025/8/4 17:23
	 * @param spaceId
	 */
	List<InvitationApproveModel> findApproveList(Long spaceId);

	/***
	 * @Description 邀约导出
	 * @author huangyongtao
	 * @date 2025/8/5 15:39
	 * @param response
	 * @param param
	 */
	Boolean invitationEasyExport(HttpServletResponse response, InvitationPageParam param);

	/***
	 * @Description 更新邀约状态任务
	 * @author huangyongtao
	 * @date 2025/8/5 17:07
	 * @param
	 */
	Boolean updateInviteStatusTask();

	/**
	 * app获取我审批的邀约列表(分页).
	 * @Param param 邀约查询条件
	 * @Return 邀约信息列表（分页）
	 */
	IPage<InvitationModel> allApprovePage(InvitationPageParam param);


	/**
	 * app获取我待审批的邀约列表(分页).
	 * @Param param 邀约查询条件
	 * @Return 邀约信息列表（分页）
	 */
	IPage<InvitationModel> pendingApprovePage(InvitationPageParam param);

	/**
	 * app获取我已审批的邀约列表(分页).
	 * @Param param 邀约查询条件
	 * @Return 邀约信息列表（分页）
	 */
	IPage<InvitationModel> approvePage(InvitationPageParam param);
}
