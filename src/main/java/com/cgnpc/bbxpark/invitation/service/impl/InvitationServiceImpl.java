
package com.cgnpc.bbxpark.invitation.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cgnpc.bbxpark.acl.uic.service.IUserApiService;
import com.cgnpc.bbxpark.settings.dto.model.SecurityDeviceRelationModel;
import com.cgnpc.bbxpark.settings.dto.model.SecurityManageModel;
import com.cgnpc.bbxpark.settings.service.ISecurityDeviceRelationService;
import com.cgnpc.bbxpark.common.constant.MessageConstant;
import com.cgnpc.bbxpark.common.constant.SystemResultCode;
import com.cgnpc.bbxpark.common.enums.InviteEndReasonEnum;
import com.cgnpc.bbxpark.common.enums.InviteReceiveTypeEnum;
import com.cgnpc.bbxpark.common.enums.InviteStatusEnum;
import com.cgnpc.bbxpark.common.enums.InviteVisitReasonTypeEnum;
import com.cgnpc.bbxpark.common.service.IMessageCommonService;
import com.cgnpc.bbxpark.common.utils.*;
import com.cgnpc.bbxpark.common.web.CudPageDto;
import com.cgnpc.bbxpark.invitation.domain.Invitation;
import com.cgnpc.bbxpark.invitation.dto.model.*;
import com.cgnpc.bbxpark.invitation.dto.param.*;
import com.cgnpc.bbxpark.invitation.mapper.InvitationRepository;
import com.cgnpc.bbxpark.invitation.service.IApprovalTaskService;
import com.cgnpc.bbxpark.invitation.service.IInvitationService;
import com.cgnpc.bbxpark.invitation.service.IInvitationSpaceRelationService;
import com.cgnpc.bbxpark.invitation.service.IInvitationVisitorService;
import com.cgnpc.bbxpark.space.domain.TenantInfo;
import com.cgnpc.bbxpark.space.dto.model.ParkSpaceFullModel;
import com.cgnpc.bbxpark.space.dto.model.UserInfoModel;
import com.cgnpc.bbxpark.space.service.IParkSpaceService;
import com.cgnpc.bbxpark.space.service.ITenantInfoService;
import com.cgnpc.bbxpark.config.eventbus.InviteEndEvent;
import com.google.common.eventbus.AsyncEventBus;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

/***
 * @Description 邀约服务实现
 * @author huangyongtao
 * @date 2025/8/1 14:37
 */
@Service("invitationService")
public class InvitationServiceImpl extends ServiceImpl<InvitationRepository, Invitation> implements IInvitationService {

	@Autowired
	private IParkSpaceService parkSpaceService;

	@Autowired
	private IInvitationVisitorService invitationVisitorService;

	@Autowired
	private IInvitationSpaceRelationService invitationSpaceRelationService;
	@Autowired
	private ISecurityDeviceRelationService securityDeviceRelationService;

	@Autowired
	private IApprovalTaskService approvalTaskService;

	@Autowired
	private IUserApiService userApiService;

	@Autowired
	private ITenantInfoService tenantInfoService;

	@Resource
	private AsyncEventBus asyncEventBus;

	@Autowired
	private IMessageCommonService messageCommonService;

    @Autowired
    private InvitationRepository invitationRepository;

	/**
	 * 根据邀约标识获得邀约详情信息.
	 * @Param [id] 邀约标识
	 * @Return 邀约详情信息
	 */
	@Override
	public InvitationModel detail(Long id, Boolean flag) {
		Invitation invitation = this.getById(id);
		AssertUtils.notNull(invitation, SystemResultCode.RESULT_DATA_NONE.message());
		InvitationModel model = BeanUtils.convertTo(invitation, InvitationModel::new);
		//接待人部门名称
		UserInfoModel userInfo = this.getUser(invitation.getReceiveUid());
		model.setReceiveDepartmentName(userInfo.getDepartmentName());
		//区域集合
		InvitationSpaceRelationListParam spaceRelationListParam = new InvitationSpaceRelationListParam();
		spaceRelationListParam.setInviteId(id);
		List<InvitationSpaceRelationModel> spaceRelationModels = invitationSpaceRelationService.list(spaceRelationListParam);
		if(flag){
			handleSpace(spaceRelationModels ,id);
		}else{
			handleRealSpace(spaceRelationModels);
		}
		model.setSpaceRelationList(spaceRelationModels);
		//访客集合
		InvitationVisitorListParam visitorListParam = new InvitationVisitorListParam();
		visitorListParam.setInviteId(id);
		List<InvitationVisitorModel> visitorModels = invitationVisitorService.list(visitorListParam);
		model.setVisitorList(visitorModels);
		//审核权限
		List<Long> businessIds = approvalTaskService.getApproveBusinessIds(Arrays.asList(id));
		if(CollectionUtil.isNotEmpty(businessIds) && InviteStatusEnum.APPROVE.getCode().equals(model.getInviteStatus()) &&  businessIds.contains(id)){
			model.setApproval(true);
		}
		return model;
	}

	/**
	 * 获取邀约列表(分页).
	 * @Param param 邀约查询条件
	 * @Return 邀约信息列表（分页）
	 */
	@Override
	public IPage<InvitationModel> page(InvitationPageParam param) {
        param.setTenantId(WebFrameworkUtils.getHeaderTenantId());
		IPage<InvitationModel> result = invitationRepository.page(new Page<>(param.getCurrent(), param.getSize()),param);
		if (CollectionUtil.isEmpty(result.getRecords())) {
			return ConvertUtil.pageEmptyConvert(param.getCurrent(), param.getSize());
		}
		List<InvitationModel> models = result.getRecords();
		handleInvitationModels(models);
		return ConvertUtil.pageConvert(result.getCurrent(), result.getTotal(), result.getSize(), models);
	}

	@Override
	public IPage<InvitationModel> appPage(InvitationPageParam param) {
        param.setTenantId(WebFrameworkUtils.getHeaderTenantId());
		param.setInviteUid(WebFrameworkUtils.getHeaderUserId());
		IPage<InvitationModel> result =invitationRepository.pageApp(new Page<>(param.getCurrent(), param.getSize()),param);
		if (CollectionUtil.isEmpty(result.getRecords())) {
			return ConvertUtil.pageEmptyConvert(param.getCurrent(), param.getSize());
		}
		List<InvitationModel> models = result.getRecords();
		 handleInvitationModels(models);
        return ConvertUtil.pageConvert(result.getCurrent(), result.getTotal(), result.getSize(), models);
	}

	/**
	 * 获取邀约列表.
	 * @Param param 邀约查询条件
	 * @Return 邀约信息列表
	 */
	@Override
	@SneakyThrows
	public List<InvitationModel> list(InvitationListParam param) {
		List<Invitation> invitations = this.list(buildQuery(BeanUtils.convertTo(param, InvitationPageParam::new)));
		return BeanUtils.convertListTo(invitations, InvitationModel::new);
	}



	/**
	 * 新增邀约.
	 * @Param param 邀约信息
	 * @Return 新增邀约是否成功
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public Long add(InvitationParam param) {
		//校验参数
		checkInvitationParam(param);
		List<Long> spaceIdList = param.getInvitationSpaceList().stream().filter(p -> ObjectUtil.isNotEmpty(p.getSpaceId())).map(InvitationSpaceRelationParam::getSpaceId).collect(Collectors.toList());
		AssertUtils.notEmpty(param.getInvitationSpaceList(), "请选择到访区域");
		//查询审批人信息
		List<SecurityDeviceRelationModel> securityDeviceRelationModels = securityDeviceRelationService.findAllBySpaceIds(spaceIdList);
		Invitation invitation = BeanUtils.convertTo(param, Invitation::new);
		if(CollectionUtil.isEmpty(securityDeviceRelationModels)){
			invitation.setInviteStatus(InviteStatusEnum.VISIT.getCode());
		}
		//处理接待人和邀约人
		handleReceive(invitation);
		invitation.setId(null);
		this.save(invitation);
		//保存到访人员信息
		handleInvitationVisitor(param, invitation.getId());
		//保存到访区域信息
		handleInvitationSpace(param, invitation.getId(), spaceIdList);
		//保存审批人信息
		handleApprovalTask(securityDeviceRelationModels, invitation.getId());
		//不需要审批，发送接待人消息
		if(CollectionUtil.isEmpty(securityDeviceRelationModels)){
			sendReceiveMessage(invitation);
		}
		return invitation.getId();
	}
	
	/**
	 * 取消邀约.
	 * @Param id 邀约标识
	 * @Return 启用邀约是否成功
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public synchronized Boolean cancel(InvitationParam param) {
		Invitation in = this.getById(param.getId());
		AssertUtils.notNull(in, SystemResultCode.RESULT_DATA_NONE.message());
		AssertUtils.notNull(param.getCancelRemark(), "取消原因不能为空");
		AssertUtils.isFalse(InviteStatusEnum.END.getCode().equals(in.getInviteStatus()), "邀约状态已变更，无法取消邀约");
		Invitation invitation = new Invitation();
	 	invitation.setId(param.getId());
		invitation.setInviteStatus(InviteStatusEnum.END.getCode());
		invitation.setEndReason(InviteEndReasonEnum.CANCEL.getCode());
		invitation.setCancelRemark(param.getCancelRemark());
		this.updateById(invitation);
		//邀约完成完成事件
		InviteEndEvent event = new InviteEndEvent();
		event.setInviteIdList(Arrays.asList(param.getId()));
		asyncEventBus.post(event);
		return true;
	}

	@Override
	public Boolean approve(Long id, Boolean approve, String remark) {
		Invitation in = this.getById(id);
		if(ObjectUtil.isEmpty(in)){
			return false;
		}
		Invitation invitation = new Invitation();
		invitation.setId(id);
		if(approve){
			invitation.setInviteStatus(in.getStartTime().before(DateUtil.date()) ? InviteStatusEnum.VISITING.getCode() : InviteStatusEnum.VISIT.getCode());
			//审批通过，发送接待人消息
			sendReceiveMessage(in);
		}else{
			invitation.setInviteStatus(InviteStatusEnum.END.getCode());
			invitation.setEndReason(InviteEndReasonEnum.APPROVE.getCode());
			//审批不通过，发送邀约人消息
			sendInviteMessage(in, remark);
		}
		return this.updateById(invitation);
	}

	@Override
	public List<InvitationApproveModel> findApproveList(Long spaceId) {
		//查询审批人信息
		List<SecurityDeviceRelationModel> securityDeviceRelationModels = securityDeviceRelationService.findAllBySpaceIds(Arrays.asList(spaceId));
		if(CollectionUtil.isEmpty(securityDeviceRelationModels)){
			return new ArrayList<>();
		}
		// 根据deviceId分组并直接转换为taskNodes
		List<InvitationApproveModel> taskNodes = securityDeviceRelationModels.stream()
				.filter(model -> ObjectUtil.isNotEmpty(model.getDeviceId()))
				.collect(Collectors.groupingBy(SecurityDeviceRelationModel::getDeviceId))
				.entrySet()
				.stream()
				.map(entry -> {
					Long deviceId = entry.getKey();
					List<SecurityDeviceRelationModel> relations = entry.getValue();

					InvitationApproveModel taskNode = new InvitationApproveModel();
					taskNode.setDeviceId(deviceId);
					taskNode.setDeviceName(relations.get(0).getDeviceName());
					// 获取该设备关联的所有安全管理员信息
					// 获取该设备关联的所有安全管理员信息并去重
					List<SecurityManageModel> securityManageList = relations.stream()
							.map(relation -> {
								SecurityManageModel securityManage = new SecurityManageModel();
								securityManage.setSecurityUid(relation.getSecurityUid());
								securityManage.setSecurityUname(relation.getSecurityUname());
								securityManage.setSecurityStaffid(relation.getSecurityStaffid());
								return securityManage;
							})
							.filter(securityManage -> ObjectUtil.isNotEmpty(securityManage.getSecurityUid()))
							.distinct()
							.collect(Collectors.toList());

					taskNode.setSecurityManageList(securityManageList);
					return taskNode;
				})
				.collect(Collectors.toList());

		return taskNodes;
	}

	@Override
	public Boolean invitationEasyExport(HttpServletResponse response, InvitationPageParam param) {
		//生成excel
		TenantInfo tenantInfoModel = tenantInfoService.getById(WebFrameworkUtils.getHeaderTenantId());
		param.setSize(Integer.MAX_VALUE);
		param.setCurrent(1);

		String title = tenantInfoModel.getName() + "邀约信息统计表";
		IPage<InvitationModel> pageResult =  this.page(param);
		List<InvitationExportModel> exportModels = new ArrayList<>();
		if(CollectionUtil.isNotEmpty(pageResult.getRecords())){
			pageResult.getRecords().forEach(model->{
				InvitationExportModel exportModel = new InvitationExportModel();
				BeanUtils.copyProperties(model, exportModel);
				exportModel.setInviteStatusStr(InviteStatusEnum.getName(model.getInviteStatus()));
				exportModel.setVisitReasonTypeStr(InviteVisitReasonTypeEnum.getName(model.getVisitReasonType()));
				exportModel.setStartTimeStr(ObjectUtil.isEmpty(model.getStartTime()) ? "" : DateUtils.format(model.getStartTime(),"yyyy-MM-dd HH:mm"));
				exportModel.setEndTimeStr(ObjectUtil.isEmpty(model.getEndTime()) ? "" : DateUtils.format(model.getEndTime(),"yyyy-MM-dd HH:mm"));
				exportModel.setCreateTimeStr(ObjectUtil.isEmpty(model.getCreateTime()) ? "" : DateUtils.format(model.getCreateTime(),"yyyy-MM-dd HH:mm:ss"));
				exportModels.add(exportModel);
			});
		}
		ExcelExportUtils.exportExcel(response, title,exportModels, InvitationExportModel.class, title);
		return true;
	}

	@Override
	public Boolean updateInviteStatusTask() {
		List<Invitation> invitations = this.list(Wrappers.<Invitation>lambdaQuery().ne(Invitation::getInviteStatus, InviteStatusEnum.END.getCode()));
		if(CollectionUtil.isEmpty(invitations)){
			return true;
		}
		List<Long> endIds = new ArrayList<>();
		//获取结束时间小于当前时间，状态为待审批的记录
		List<Long> timeOutIds = invitations.stream().filter(invitation -> invitation.getEndTime().before(DateUtil.date()) && InviteStatusEnum.APPROVE.getCode().equals(invitation.getInviteStatus())).map(Invitation::getId).collect(Collectors.toList());
		if (CollectionUtil.isNotEmpty(timeOutIds)){
			endIds.addAll(timeOutIds);
			this.update(Wrappers.<Invitation>lambdaUpdate().in(Invitation::getId, timeOutIds).set(Invitation::getInviteStatus, InviteStatusEnum.END.getCode()).set(Invitation::getEndReason, InviteEndReasonEnum.TIMEOUT.getCode()));
		}
		//获取结束时间小于当前时间，状态为审批通过的记录
		List<Long> normalIds = invitations.stream().filter(invitation -> invitation.getEndTime().before(DateUtil.date()) && !InviteStatusEnum.APPROVE.getCode().equals(invitation.getInviteStatus())).map(Invitation::getId).collect(Collectors.toList());
		if (CollectionUtil.isNotEmpty(normalIds)){
			endIds.addAll(normalIds);
			this.update(Wrappers.<Invitation>lambdaUpdate().in(Invitation::getId, normalIds).set(Invitation::getInviteStatus, InviteStatusEnum.END.getCode()).set(Invitation::getEndReason, InviteEndReasonEnum.NORMAL.getCode()));
		}
		//获取开始时间小于当前时间，结束时间大于当前时间，状态为待访问的记录
		List<Long> visitingIds = invitations.stream().filter(invitation -> invitation.getStartTime().before(DateUtil.date()) && invitation.getEndTime().after(DateUtil.date()) && InviteStatusEnum.VISIT.getCode().equals(invitation.getInviteStatus())).map(Invitation::getId).collect(Collectors.toList());
		if (CollectionUtil.isNotEmpty(visitingIds)){
			this.update(Wrappers.<Invitation>lambdaUpdate().in(Invitation::getId, visitingIds).set(Invitation::getInviteStatus, InviteStatusEnum.VISITING.getCode()));
		}
		//邀约完成完成事件
		if(CollectionUtil.isNotEmpty(endIds)){
			InviteEndEvent event = new InviteEndEvent();
			event.setInviteIdList(endIds);
			asyncEventBus.post(event);
		}
		return true;
	}

	@Override
	public IPage<InvitationModel> allApprovePage(InvitationPageParam param) {
        param.setTenantId(WebFrameworkUtils.getHeaderTenantId());
        param.setUserId(WebFrameworkUtils.getHeaderUserId());
		IPage<InvitationModel> result = invitationRepository.allApprovePage(new Page<>(param.getCurrent(), param.getSize()),param);
		if (CollectionUtil.isEmpty(result.getRecords())) {
			return ConvertUtil.pageEmptyConvert(param.getCurrent(), param.getSize());
		}
		List<InvitationModel> models = result.getRecords();
		List<Long> businessIds = approvalTaskService.getApproveBusinessIds(models.stream().map(InvitationModel::getId).collect(Collectors.toList()));
		handleInvitationModels(models, businessIds);
		return ConvertUtil.pageConvert(result.getCurrent(), result.getTotal(), result.getSize(), models);
	}

	@Override
	public IPage<InvitationModel> pendingApprovePage(InvitationPageParam param) {
        CudPageDto baseParam = new CudPageDto();
		baseParam.setCurrent(param.getCurrent());
		baseParam.setSize(param.getSize());
		IPage<Long> result = approvalTaskService.pendingApprovalPage(baseParam);
		if (CollectionUtil.isEmpty(result.getRecords())) {
			return ConvertUtil.pageEmptyConvert(param.getCurrent(), param.getSize());
		}
		List<Long> ids = result.getRecords();
		List<Invitation> invitations = (List<Invitation>) this.listByIds(ids);
		List<InvitationModel> models = BeanUtils.convertListTo(invitations, InvitationModel::new);
		// 根据ids集合的顺序对models进行排序
		models.sort(Comparator.comparingInt(m -> ids.indexOf(m.getId())));
		List<Long> businessIds = approvalTaskService.getApproveBusinessIds(models.stream().map(InvitationModel::getId).collect(Collectors.toList()));
		handleInvitationModels(models, businessIds);
		return ConvertUtil.pageConvert(baseParam.getCurrent(), result.getTotal(), baseParam.getSize(), models);
	}

	@Override
	public IPage<InvitationModel> approvePage(InvitationPageParam param) {
        CudPageDto baseParam = new CudPageDto();
		baseParam.setCurrent(param.getCurrent());
		baseParam.setSize(param.getSize());
		IPage<Long> result = approvalTaskService.approvedPage(baseParam);
		if (CollectionUtil.isEmpty(result.getRecords())) {
			return ConvertUtil.pageEmptyConvert(param.getCurrent(), param.getSize());
		}
		List<Long> ids = result.getRecords();
		List<Invitation> invitations = (List<Invitation>) this.listByIds(ids);
		List<InvitationModel> models = BeanUtils.convertListTo(invitations, InvitationModel::new);
		// 根据ids集合的顺序对models进行排序
		models.sort(Comparator.comparingInt(m -> ids.indexOf(m.getId())));
		handleInvitationModels(models);
		return ConvertUtil.pageConvert(baseParam.getCurrent(), result.getTotal(), baseParam.getSize(), models);
	}

	private void sendReceiveMessage(Invitation in){
		//区域集合
		InvitationSpaceRelationListParam spaceRelationListParam = new InvitationSpaceRelationListParam();
		spaceRelationListParam.setInviteId(in.getId());
		List<InvitationSpaceRelationModel> spaceRelationModels = invitationSpaceRelationService.list(spaceRelationListParam);
		//访客集合
		InvitationVisitorListParam visitorListParam = new InvitationVisitorListParam();
		visitorListParam.setInviteId(in.getId());
		List<InvitationVisitorModel> visitorModels = invitationVisitorService.list(visitorListParam);
		//拼接字符串，访客名称（公司名称）以、隔开
		StringBuilder visitors = new StringBuilder();
		visitorModels.forEach(m -> visitors.append(m.getName()).append("（").append(m.getCompany()).append("）").append("、"));
		visitors.deleteCharAt(visitors.length() - 1);
		//接待人通知消息
		Map<String, String> variables = new HashMap<>(6);
		variables.put("startTime", DateUtils.format(in.getStartTime(), "yyyy-MM-dd HH:mm"));
		variables.put("endTime", DateUtils.format(in.getEndTime(), "yyyy-MM-dd HH:mm"));
		variables.put("visitReasonType", InviteVisitReasonTypeEnum.getName(in.getVisitReasonType()));
		variables.put("spaces", CollectionUtil.isNotEmpty(spaceRelationModels) ? spaceRelationModels.stream().map(m -> m.getSpaceFullName()).collect(Collectors.joining(",")) : "");
		variables.put("num", CollectionUtil.isNotEmpty(visitorModels) ? String.valueOf(visitorModels.size()) : "0");
		variables.put("visitors", visitors.toString());
		messageCommonService.sendMessage(MessageConstant.INVITE_RECEIVE_NOTICE,
                in.getTenantId(), null, in.getReceiveUid(), variables);
	}

	private void sendInviteMessage(Invitation in, String remark){
		//访客集合
		InvitationVisitorListParam visitorListParam = new InvitationVisitorListParam();
		visitorListParam.setInviteId(in.getId());
		List<InvitationVisitorModel> visitorModels = invitationVisitorService.list(visitorListParam);
		//拼接字符串，访客名称（公司名称）以、隔开
		StringBuilder visitors = new StringBuilder();
		visitorModels.forEach(m -> visitors.append(m.getName()).append("（").append(m.getCompany()).append("）").append("、"));
		visitors.deleteCharAt(visitors.length() - 1);
		//邀约人通知消息
		Map<String, String> variables = new HashMap<>(2);
		variables.put("remark", remark);
		variables.put("visitors", visitors.toString());
		messageCommonService.sendMessage(MessageConstant.INVITE_APPROVAL_NOTICE, in.getTenantId(), in.getId(), in.getReceiveUid(), variables);
	}

	private void checkInvitationParam(InvitationParam param){
		AssertUtils.notNull(param.getReceiveType(), "接待类型不能为空");
		AssertUtils.notEmpty(param.getInvitationSpaceList(), "请选择到访区域");
		AssertUtils.notEmpty(param.getInvitationVisitorList(), "请填写到访人信息");
		//校验开始时间要大于当前时间，结束时间要大于开始时间
		AssertUtils.isTrue(param.getStartTime().after(DateUtil.date()), "到访开始时间已过，请重新选择");
		AssertUtils.isTrue(param.getEndTime().after(param.getStartTime()), "结束时间不能小于开始时间");
	}

	private void handleReceive(Invitation param){
		UserInfoModel userInfoModel = getUser(WebFrameworkUtils.getHeaderUserId());
		param.setInviteUid(WebFrameworkUtils.getHeaderUserId());
		param.setInviteUname(userInfoModel.getUserName());
		param.setInviteStaffid(userInfoModel.getStaffid());
		if(InviteReceiveTypeEnum.SELF.getCode().equals(param.getReceiveType())){
			param.setReceiveUid(WebFrameworkUtils.getHeaderUserId());
			param.setReceiveUname(userInfoModel.getUserName());
			param.setReceiveStaffid(userInfoModel.getStaffid());
		}else{
			AssertUtils.notNull(param.getReceiveUid(), "接待人不能为空");
			if(ObjectUtil.isEmpty(param.getReceiveUname())){
				UserInfoModel user = getUser(param.getReceiveUid());
				param.setReceiveUname(user.getUserName());
				param.setReceiveStaffid(user.getStaffid());
			}
		}

	}

	private void handleApprovalTask(List<SecurityDeviceRelationModel> securityDeviceRelationModels, Long id){
		//查询审批人信息
		if(CollectionUtil.isEmpty(securityDeviceRelationModels)){
			return;
		}
		// 根据deviceId分组并直接转换为taskNodes
		List<ApprovalTaskNode> taskNodes = securityDeviceRelationModels.stream()
				.filter(model -> ObjectUtil.isNotEmpty(model.getDeviceId()))
				.collect(Collectors.groupingBy(SecurityDeviceRelationModel::getDeviceId))
				.entrySet()
				.stream()
				.map(entry -> {
					Long deviceId = entry.getKey();
					List<SecurityDeviceRelationModel> relations = entry.getValue();

					ApprovalTaskNode taskNode = new ApprovalTaskNode();
					ExtendContent taskExtend = new ExtendContent();
					taskExtend.setSpaceId(relations.get(0).getSpaceId());
					taskExtend.setDeviceId(deviceId);
					taskExtend.setDeviceName(relations.get(0).getDeviceName());
					taskNode.setTaskExtend(taskExtend);
					// 获取该设备关联的所有安全管理员ID
					List<String> securityUidList = relations.stream()
							.map(SecurityDeviceRelationModel::getSecurityUid)
							.filter(ObjectUtil::isNotEmpty)
							.distinct()
							.collect(Collectors.toList());

					taskNode.setUserIds(securityUidList);
					return taskNode;
				})
				.collect(Collectors.toList());

		if(CollectionUtil.isEmpty(taskNodes)){
			return;
		}
		ApprovalTaskParam param = new ApprovalTaskParam();
		param.setBusinessId(id);
		param.setTaskNodes(taskNodes);
		approvalTaskService.createTask(param);
	}

	private void handleInvitationVisitor(InvitationParam param, Long id){
		//校验访客信息集合的姓名，手机号，公司名称，人脸图片
		param.getInvitationVisitorList().forEach(e -> {
			AssertUtils.notNull(e.getName(), "访客姓名不能为空");
			AssertUtils.notNull(e.getPhone(), e.getName() + "访客手机号不能为空");
			AssertUtils.notNull(e.getCompany(), e.getName() + "访客公司名称不能为空");
			AssertUtils.notNull(e.getFaceImg(), e.getName() + "访客人脸图片不能为空");
			e.setInviteId(id);
		});
		invitationVisitorService.addBatch(param.getInvitationVisitorList());
	}

	private void handleInvitationSpace(InvitationParam param, Long id, List<Long> spaceIdList){
		Map<Long, ParkSpaceFullModel> spaceMap = parkSpaceService.findFullSpaceMap(spaceIdList, WebFrameworkUtils.getHeaderTenantId());
		//将区域名称全称放入区域集合中
		param.getInvitationSpaceList().forEach(e -> {
			AssertUtils.notNull(e.getSpaceId(), "区域id不能为空");
			e.setInviteId(id);
			e.setSpaceName(spaceMap.get(e.getSpaceId()).getSpaceName());
			e.setSpaceFullName(spaceMap.get(e.getSpaceId()).getFullPath());
		});
		invitationSpaceRelationService.addBatch(param.getInvitationSpaceList());
	}


	private void handleSpace(List<InvitationSpaceRelationModel> spaceRelationModels, Long id){
		List<ApprovalTaskListModel> taskListModels = approvalTaskService.getApprovalTaskList(id);
		if(CollectionUtil.isEmpty(taskListModels)){
			return;
		}
		List<InvitationSpaceApprovalModel> approvalModels = new ArrayList<>();
		taskListModels.forEach(taskListModel -> {
			ExtendContent extendContent = JSONObject.parseObject(taskListModel.getExtendContent(), ExtendContent.class);
			InvitationSpaceApprovalModel model = new InvitationSpaceApprovalModel();
			model.setSpaceId(extendContent.getSpaceId());
			model.setDeviceName(extendContent.getDeviceName());
			model.setApproverList(taskListModel.getApproverList());
			approvalModels.add(model);
		});
		Map<Long, List<InvitationSpaceApprovalModel>> map = approvalModels.stream().collect(Collectors.groupingBy(InvitationSpaceApprovalModel::getSpaceId));
		spaceRelationModels.forEach(spaceRelationModel -> {
			spaceRelationModel.setApprovalModels(map.containsKey(spaceRelationModel.getSpaceId()) ? map.get(spaceRelationModel.getSpaceId()) : new ArrayList<>());
		});
	}

	private void handleRealSpace(List<InvitationSpaceRelationModel> spaceRelationModels){
		if(CollectionUtil.isEmpty(spaceRelationModels)){
			return;
		}
		List<Long> spaceId = spaceRelationModels.stream().map(InvitationSpaceRelationModel::getSpaceId).collect(Collectors.toList());
		//查询审批人信息
		List<SecurityDeviceRelationModel> securityDeviceRelationModels = securityDeviceRelationService.findAllBySpaceIds(spaceId);
		if(CollectionUtil.isEmpty(securityDeviceRelationModels)){
			return;
		}
		Map<Long, List<SecurityDeviceRelationModel>> spaceMap = securityDeviceRelationModels.stream().collect(Collectors.groupingBy(SecurityDeviceRelationModel::getSpaceId));
		spaceRelationModels.forEach(spaceRelationModel -> {
			if(spaceMap.containsKey(spaceRelationModel.getSpaceId())){
				// 根据deviceId分组并直接转换为taskNodes
				List<InvitationSpaceApprovalModel> taskNodes = spaceMap.get(spaceRelationModel.getSpaceId()).stream()
						.filter(model -> ObjectUtil.isNotEmpty(model.getDeviceId()))
						.collect(Collectors.groupingBy(SecurityDeviceRelationModel::getDeviceId))
						.entrySet()
						.stream()
						.map(entry -> {
							List<SecurityDeviceRelationModel> relations = entry.getValue();
							InvitationSpaceApprovalModel taskNode = new InvitationSpaceApprovalModel();
							taskNode.setSpaceId(spaceRelationModel.getSpaceId());
							taskNode.setDeviceName(relations.get(0).getDeviceName());
							// 获取该设备关联的所有安全管理员信息
							// 获取该设备关联的所有安全管理员信息并去重
							List<ApproverModel> securityManageList = relations.stream()
									.map(relation -> {
										ApproverModel securityManage = new ApproverModel();
										securityManage.setUserId(relation.getSecurityUid());
										securityManage.setUserName(relation.getSecurityUname());
										securityManage.setStaffid(relation.getSecurityStaffid());
										return securityManage;
									})
									.filter(securityManage -> ObjectUtil.isNotEmpty(securityManage.getUserId()))
									.distinct()
									.collect(Collectors.toList());

							taskNode.setApproverList(securityManageList);
							return taskNode;
						})
						.collect(Collectors.toList());
				spaceRelationModel.setApprovalModels(taskNodes);
			}else{
				spaceRelationModel.setApprovalModels(new ArrayList<>());
			}
		});
	}

	private void handleInvitationModels(List<InvitationModel> models, List<Long> businessIds){
		List<Long> ids = models.stream().map(InvitationModel::getId).collect(Collectors.toList());
		//区域集合
		InvitationSpaceRelationListParam spaceRelationListParam = new InvitationSpaceRelationListParam();
		spaceRelationListParam.setInviteIdList(ids);
		List<InvitationSpaceRelationModel> spaceRelationModels = invitationSpaceRelationService.list(spaceRelationListParam);
		Map<Long, List<InvitationSpaceRelationModel>> spaceRelationMap = spaceRelationModels.stream().collect(Collectors.groupingBy(InvitationSpaceRelationModel::getInviteId));
		//访客集合
		InvitationVisitorListParam visitorListParam = new InvitationVisitorListParam();
		visitorListParam.setInviteIdList(ids);
		List<InvitationVisitorModel> visitorModels = invitationVisitorService.list(visitorListParam);
		Map<Long, List<InvitationVisitorModel>> visitorMap = visitorModels.stream().collect(Collectors.groupingBy(InvitationVisitorModel::getInviteId));
		models.forEach(item -> {
			item.setSpaceNames(spaceRelationMap.containsKey(item.getId()) ? spaceRelationMap.get(item.getId()).stream().map(m -> m.getSpaceFullName()).collect(Collectors.joining(",")) : "");
			item.setVisitorNames(visitorMap.containsKey(item.getId()) ? visitorMap.get(item.getId()).stream().map(m -> m.getName()).collect(Collectors.joining(",")) : "");
			if(CollectionUtil.isNotEmpty(businessIds) && InviteStatusEnum.APPROVE.getCode().equals(item.getInviteStatus()) &&  businessIds.contains(item.getId())){
				item.setApproval(true);
			}
		});
	}

	private void handleInvitationModels(List<InvitationModel> models){
		this.handleInvitationModels(models, null);
	}

	/***
	 * @Description 获取用户信息
	 * @author huangyongtao
	 * @date 2024/8/2 10:34
	 * @param userId
	 */
	private UserInfoModel getUser(String userId) {
		return Objects.requireNonNull(userApiService.getByStaffNo(userId));
	}

	private LambdaQueryWrapper buildQuery(InvitationPageParam param) {
		LambdaQueryWrapper<Invitation> query = new LambdaQueryWrapper<>();
		// 根据到访事由筛选
		query.like(ObjectUtil.isNotEmpty(param.getVisitReasonType()), Invitation::getVisitReasonType, param.getVisitReasonType());
		// 根据邀约状态筛选
		query.eq(ObjectUtil.isNotEmpty(param.getInviteStatus()), Invitation::getInviteStatus, param.getInviteStatus());
		//根据邀约人id
		query.eq(ObjectUtil.isNotEmpty(param.getInviteUid()), Invitation::getInviteUid, param.getInviteUid());
		// 根据邀约人姓名筛选
		query.like(ObjectUtil.isNotEmpty(param.getInviteUname()), Invitation::getInviteUname, param.getInviteUname());
		//根据接待人id
		query.eq(ObjectUtil.isNotEmpty(param.getReceiveUid()), Invitation::getInviteUid, param.getReceiveUid());
		// 根据接待人姓名筛选
		query.like(ObjectUtil.isNotEmpty(param.getReceiveUname()), Invitation::getReceiveUname, param.getReceiveUname());
		//租户
		query.eq(ObjectUtil.isNotEmpty(WebFrameworkUtils.getHeaderTenantId()), Invitation::getTenantId, WebFrameworkUtils.getHeaderTenantId());
		return query;
	}
}
