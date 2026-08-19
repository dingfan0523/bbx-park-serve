
package com.cgnpc.bbxpark.workorder.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.util.DateUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cgnpc.bbxpark.acl.uic.model.SimpleStaffModel;
import com.cgnpc.bbxpark.acl.uic.service.IRoleApiService;
import com.cgnpc.bbxpark.acl.uic.service.IUserApiService;
import com.cgnpc.bbxpark.common.constant.Constant;
import com.cgnpc.bbxpark.settings.dto.model.ConfigInfoModel;
import com.cgnpc.bbxpark.settings.service.IAttentionManageService;
import com.cgnpc.bbxpark.common.constant.MessageConstant;
import com.cgnpc.bbxpark.common.constant.SystemResultCode;
import com.cgnpc.bbxpark.common.enums.*;
import com.cgnpc.bbxpark.common.exception.GenericException;
import com.cgnpc.bbxpark.common.service.IMessageCommonService;
import com.cgnpc.bbxpark.common.utils.*;
import com.cgnpc.bbxpark.device.dto.model.AlarmInfoModel;
import com.cgnpc.bbxpark.device.service.IAlarmInfoService;
import com.cgnpc.bbxpark.device.service.IDeviceLabelRelService;
import com.cgnpc.bbxpark.device.service.IIocDeviceService;
import com.cgnpc.bbxpark.problemReport.model.ProblemReportModel;
import com.cgnpc.bbxpark.problemReport.param.ProblemReportQueryParam;
import com.cgnpc.bbxpark.problemReport.service.IProblemReportService;
import com.cgnpc.bbxpark.property.dto.model.PropertyScheduleUserModel;
import com.cgnpc.bbxpark.property.service.IPropertyScheduleService;
import com.cgnpc.bbxpark.settings.service.IConfigInfoService;
import com.cgnpc.bbxpark.space.dto.model.UserInfoModel;
import com.cgnpc.bbxpark.space.service.IParkSpaceService;
import com.cgnpc.bbxpark.workorder.domain.*;
import com.cgnpc.bbxpark.workorder.dto.model.*;
import com.cgnpc.bbxpark.workorder.dto.param.*;
import com.cgnpc.bbxpark.workorder.mapper.WorkOrderRepository;
import com.cgnpc.bbxpark.workorder.service.*;
import com.cgnpc.cud.core.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;


/**
 * 工单主服务实现
 *
 * @author lhy
 * @date 2024/08/26 09:53:42
 */
@Slf4j
@Service
public class WorkOrderServiceImpl extends ServiceImpl<WorkOrderRepository, WorkOrder> implements IWorkOrderService {

    @Autowired
    private IWorkOrderDeviceService workOrderDeviceService;

    @Autowired
    private IWorkOrderRomanService workOrderRomanService;

//    @Autowired
//    private UserInfoFeignClient userInfoFeignClient;

    @Autowired
    private IUserApiService userApiService;

    @Autowired
    private IDeviceLabelRelService deviceLabelRelService;

    @Autowired
    private IIocDeviceService iocDeviceService;

    @Autowired
    private IParkSpaceService parkSpaceService;

    @Autowired
    private IMessageCommonService messageCommonService;
    @Autowired
    private IProblemReportService problemReportService;
    @Autowired
    private IAlarmInfoService alarmInfoService;
    @Autowired
    private WorkOrderRepository workOrderRepository;

    @Lazy
    @Autowired
    private IWorkPlanDetailService workPlanDetailService;

    @Autowired
    private IAttentionManageService attentionManageService;

    @Autowired
    private IPropertyScheduleService propertyScheduleService;

    @Autowired
    private IWorkScheduleUserService workScheduleUserService;

//    @Autowired
//    private IUserRoleCommonService userRoleCommonService;

    @Autowired
    private IRoleApiService roleApiService;

    @Autowired
    private IWorkMaterialService workMaterialService;

    @Lazy
    @Autowired
    private IWorkTaskService workTaskService;

    @Autowired
    private IWorkTaskItemService workTaskItemService;

    @Autowired
    private IWorkEvaluateService workEvaluateService;

    @Autowired
    private IConfigInfoService configInfoService;

    @Override
    public WorkOrderModel detail(Long id) {
        WorkOrder workOrder = this.getById(id);
        WorkOrderModel workOrderModel = BeanUtil.toBean(workOrder, WorkOrderModel.class);
        //图片
        workOrderModel.setProblemPictureUrlList(JsonUtil.convertJsonArrStrToList(workOrder.getProblemPictureUrl()));
        workOrderModel.setProcessedPictureUrlList(JsonUtil.convertJsonArrStrToList(workOrder.getProcessedPictureUrl()));

        //设备
        List<WorkOrderDevice> workOrderDevices = workOrderDeviceService.list(new LambdaQueryWrapper<WorkOrderDevice>().eq(WorkOrderDevice::getWorkOrderId, id));
        List<WorkOrderDeviceModel> workOrderDeviceModels = BeanUtils.convertListTo(workOrderDevices, WorkOrderDeviceModel::new);
        workOrderModel.setWorkOrderDeviceModels(workOrderDeviceModels);
        //流程
        List<WorkOrderRoman> workOrderRomanList = workOrderRomanService.list(new LambdaQueryWrapper<WorkOrderRoman>().eq(WorkOrderRoman::getWorkOrderId, id));
        workOrderModel.setWorkOrderRomanModels(BeanUtils.convertListTo(workOrderRomanList.stream().sorted(Comparator.comparing(WorkOrderRoman::getCreateTime)).collect(Collectors.toList()), WorkOrderRomanModel::new));

        if (workOrderModel.getSource().equals(WorkOrderSourceEnum.PERSON.getCode())){
            ProblemReportQueryParam problemReportQueryParam = new ProblemReportQueryParam();
            problemReportQueryParam.setId(workOrder.getBusinessId());
            ProblemReportModel detail = problemReportService.appDetail(problemReportQueryParam);
            workOrderModel.setProblemReportModel(detail);
        }else if (workOrderModel.getSource().equals(WorkOrderSourceEnum.ALARM.getCode())){
            AlarmInfoModel alarmInfo = alarmInfoService.detail(workOrder.getBusinessId());
            workOrderModel.setAlarmInfoModel(alarmInfo);
        }else{
            //工单计划详情
            getWorkPlan(workOrderModel);
        }
        //工单评价
        workOrderModel.setEvaluateModels(workEvaluateService.list(workOrderModel.getId()));
        //设置工单权限
        handleWorkPermission(workOrderModel);
        return workOrderModel;
    }


    @Override
    public IPage<WorkOrderModel> page(WorkOrderPageParam param) {
        List<String> userIdList = getRoleUserId();
        IPage<WorkOrder> page = new Page<>(param.getCurrent(), param.getSize());
        String userId = userApiService.getCurrentStaffNo();
        log.info("当前登录账号：{}",userId);
        param.setTenantId(WebFrameworkUtils.getHeaderTenantId());
        param.setCreatorId(userId);
        if(CollectionUtil.isNotEmpty(userIdList) && userIdList.contains(userId)){
            param.setCreatorId(null);
        }
        IPage<WorkOrder> pageDate =  workOrderRepository.selectPageByParam(page,param);
        List<WorkOrderModel> workOrderModels = BeanUtils.convertListTo(pageDate.getRecords(), WorkOrderModel::new);
        workOrderModels.forEach(this::handleWorkPermission);
        return ConvertUtil.pageConvert(pageDate.getCurrent(), pageDate.getTotal(), pageDate.getSize(), workOrderModels);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean allot(WorkOrderAllotParam param) {
        WorkOrder workOrder = this.getById(param.getId());
        AssertUtils.notNull(workOrder,"工单不存在");
        AssertUtils.isTrue(workOrder.getStatus().equals(WorkOrderStatusEnum.ALLOT.getCode()), "该工单状态已变更,请确认");
        // 确认当前操作用户是否为工单的分配人
        String userId = userApiService.getCurrentStaffNo();
        log.info("当前登录账号：{}",userId);
        AssertUtils.isTrue(workOrder.getAllotUid().equals(userId), "非工单的分配人,请确认");
        // 插入工单流程记录
        WorkOrderRoman workOrderRoman = new WorkOrderRoman();
        workOrderRoman.setWorkOrderId(workOrder.getId());
        workOrderRoman.setTenantId(WebFrameworkUtils.getHeaderTenantId());
        workOrderRoman.setRomanStatus(WorkOrderStatusEnum.REPORTED.getCode());
        workOrderRoman.setOperatorId(param.getProcessedPersonId());
        workOrderRoman.setOperatorName(param.getProcessedPersonName());
        workOrderRoman.setOperatorStaffid(param.getProcessedPersonStaffid());
        workOrderRoman.setCreateTime(new Date());
        workOrderRoman.setOperator("处理人");
        workOrderRoman.setOperatorValue(param.getProcessedPersonName());
        workOrderRomanService.save(workOrderRoman);
        UserInfoModel userPerson = userApiService.getSecondDeptByStaffNo(param.getProcessedPersonId());
        //更改工单主表
        boolean flag = this.update(new LambdaUpdateWrapper<WorkOrder>()
                .set(ObjectUtil.isNotEmpty(param.getProcessedPersonId()), WorkOrder::getProcessedPersonId, param.getProcessedPersonId())
                .set(ObjectUtil.isNotEmpty(param.getProcessedPersonName()), WorkOrder::getProcessedPersonName, param.getProcessedPersonName())
                .set(ObjectUtil.isNotEmpty(param.getProcessedPersonStaffid()), WorkOrder::getProcessedPersonStaffid, param.getProcessedPersonStaffid())
                .set(ObjectUtil.isNotEmpty(userPerson.getDepartmentSecondId()), WorkOrder::getDepartmentId, userPerson.getDepartmentSecondId())
                .set(ObjectUtil.isNotEmpty(userPerson.getDepartmentSecondName()), WorkOrder::getDepartmentName, userPerson.getDepartmentSecondName())
                .set(WorkOrder::getStatus, WorkOrderStatusEnum.REPORTED.getCode())
                .set(WorkOrder::getUpdateTime, new Date())
                .eq(WorkOrder::getId, param.getId()));
        AssertUtils.isTrue(flag, "该工单状态已变更,请确认");
        return true;
    }

    @Override
    public Boolean grab(WorkOrderHandleParam param) {
        // 获取当前操作用户的详细信息
        UserInfoModel userInfo = userApiService.getSecondDeptByStaffNo(WebFrameworkUtils.getHeaderUserId());
        // 根据ID获取工单详情
        WorkOrder workOrder = this.getById(param.getId());
        AssertUtils.notNull(workOrder,"工单不存在");
        AssertUtils.isTrue(workOrder.getStatus().equals(WorkOrderStatusEnum.ALLOT.getCode()), "该工单状态已变更,请确认");
        workOrder.setStatus(WorkOrderStatusEnum.REPORTED.getCode());
        workOrder.setProcessedPersonId(userInfo.getId());
        workOrder.setProcessedPersonName(userInfo.getUserName());
        workOrder.setProcessedPersonStaffid(userInfo.getStaffid());
        workOrder.setDepartmentName(userInfo.getDepartmentSecondName());
        workOrder.setDepartmentId(userInfo.getDepartmentSecondId());
        this.updateById(workOrder);


        // 插入工单流程记录
        WorkOrderRoman workOrderRoman = new WorkOrderRoman();
        workOrderRoman.setWorkOrderId(workOrder.getId());
        workOrderRoman.setTenantId(WebFrameworkUtils.getHeaderTenantId());
        workOrderRoman.setRomanStatus(WorkOrderStatusEnum.REPORTED.getCode());
        workOrderRoman.setOperatorId(userInfo.getId());
        workOrderRoman.setOperatorName(userInfo.getUserName());
        workOrderRoman.setOperatorStaffid(userInfo.getStaffid());
        workOrderRoman.setCreateTime(new Date());
        workOrderRoman.setOperator("处理人");
        workOrderRoman.setOperatorValue(userInfo.getUserName());
        workOrderRomanService.save(workOrderRoman);
        return true;
    }

    @Override
    public Boolean transferHandle(WorkOrderAllotParam param) {
        WorkOrder workOrder = this.getById(param.getId());
        AssertUtils.notNull(workOrder,"工单不存在");
        AssertUtils.isTrue(workOrder.getStatus().equals(WorkOrderStatusEnum.REPORTED.getCode()) || workOrder.getStatus().equals(WorkOrderStatusEnum.PROCESSING.getCode()), "该工单状态已变更,请确认");
        String userId = userApiService.getCurrentStaffNo();
        log.info("工单id:{}, 工单处理人：{}， 当前登录账号：{}",workOrder.getId(), workOrder.getProcessedPersonId(), userId);
        // 确认当前操作用户是否为工单的分配人
        AssertUtils.isTrue(workOrder.getProcessedPersonId().equals(userId), "非工单的处理人,请确认");
        // 插入工单流程记录
        WorkOrderRoman workOrderRoman = new WorkOrderRoman();
        workOrderRoman.setWorkOrderId(workOrder.getId());
        workOrderRoman.setTenantId(WebFrameworkUtils.getHeaderTenantId());
        workOrderRoman.setRomanStatus(workOrder.getStatus());
        workOrderRoman.setOperatorId(param.getProcessedPersonId());
        workOrderRoman.setOperatorName(param.getProcessedPersonName());
        workOrderRoman.setOperatorStaffid(param.getProcessedPersonStaffid());
        workOrderRoman.setCreateTime(new Date());
        workOrderRoman.setOperator("处理人");
        workOrderRoman.setOperatorValue(param.getProcessedPersonName());
        workOrderRoman.setRedundancyOne(workOrder.getProcessedPersonId());
        workOrderRoman.setRedundancyTwo(workOrder.getProcessedPersonName());
        workOrderRoman.setRedundancyThree(workOrder.getProcessedPersonStaffid());
        workOrderRoman.setRedundancyFour(workOrder.getProcessedPersonName());
        workOrderRoman.setRedundancyFive("转派人");
        workOrderRomanService.save(workOrderRoman);
        UserInfoModel userPerson = userApiService.getSecondDeptByStaffNo(param.getProcessedPersonId());
        //更改工单主表
        boolean flag = this.update(new LambdaUpdateWrapper<WorkOrder>()
                .set(ObjectUtil.isNotEmpty(param.getProcessedPersonId()), WorkOrder::getProcessedPersonId, param.getProcessedPersonId())
                .set(ObjectUtil.isNotEmpty(param.getProcessedPersonName()), WorkOrder::getProcessedPersonName, param.getProcessedPersonName())
                .set(ObjectUtil.isNotEmpty(param.getProcessedPersonStaffid()), WorkOrder::getProcessedPersonStaffid, param.getProcessedPersonStaffid())
                .set(ObjectUtil.isNotEmpty(userPerson.getDepartmentSecondId()), WorkOrder::getDepartmentId, userPerson.getDepartmentSecondId())
                .set(ObjectUtil.isNotEmpty(userPerson.getDepartmentSecondName()), WorkOrder::getDepartmentName, userPerson.getDepartmentSecondName())
                .set(WorkOrder::getTransferUid, workOrder.getProcessedPersonId())
                .set(WorkOrder::getTransferUname, workOrder.getProcessedPersonName())
                .set(WorkOrder::getTransferStaffid, workOrder.getProcessedPersonStaffid())
                .set(WorkOrder::getUpdateTime, new Date())
                .eq(WorkOrder::getId, param.getId()));
        AssertUtils.isTrue(flag, "该工单状态已变更,请确认");
        //工单转派通知
        Map<String, String> variables = new HashMap<>();
        variables.put("name", workOrder.getName());
        variables.put("code", workOrder.getCode());
        variables.put("type", "处理");
        messageCommonService.sendMessage(MessageConstant.ORDER_TRANSFER_NOTICE, workOrder.getTenantId(), workOrder.getId(), param.getProcessedPersonId(), variables);
        return true;
    }

    @Override
    public Boolean transferAudit(WorkOrderAllotParam param) {
        WorkOrder workOrder = this.getById(param.getId());
        AssertUtils.notNull(workOrder,"工单不存在");
        String userId = userApiService.getCurrentStaffNo();
        log.info("工单id:{}, 工单处理人：{}， 当前登录账号：{}",workOrder.getId(), workOrder.getProcessedPersonId(), userId);
        AssertUtils.isTrue(workOrder.getStatus().equals(WorkOrderStatusEnum.AUDIT.getCode()), "该工单状态已变更,请确认");
        AssertUtils.isTrue(workOrder.getAuditUid().equals(userId), "非工单的审核人,请确认");
        // 插入工单流程记录
        WorkOrderRoman workOrderRoman = new WorkOrderRoman();
        workOrderRoman.setWorkOrderId(workOrder.getId());
        workOrderRoman.setTenantId(WebFrameworkUtils.getHeaderTenantId());
        workOrderRoman.setRomanStatus(workOrder.getStatus());
        workOrderRoman.setOperatorId(param.getProcessedPersonId());
        workOrderRoman.setOperatorName(param.getProcessedPersonName());
        workOrderRoman.setOperatorStaffid(param.getProcessedPersonStaffid());
        workOrderRoman.setCreateTime(new Date());
        workOrderRoman.setOperator("审核人");
        workOrderRoman.setOperatorValue(param.getProcessedPersonName());
        workOrderRoman.setRedundancyOne(workOrder.getAuditUid());
        workOrderRoman.setRedundancyTwo(workOrder.getAuditUname());
        workOrderRoman.setRedundancyThree(workOrder.getAuditStaffid());
        workOrderRoman.setRedundancyFour(workOrder.getAuditUname());
        workOrderRoman.setRedundancyFive("转派人");
        workOrderRomanService.save(workOrderRoman);

        //更改工单主表
        boolean flag = this.update(new LambdaUpdateWrapper<WorkOrder>()
                .set(ObjectUtil.isNotEmpty(param.getProcessedPersonId()), WorkOrder::getAuditUid, param.getProcessedPersonId())
                .set(ObjectUtil.isNotEmpty(param.getProcessedPersonName()), WorkOrder::getAuditUname, param.getProcessedPersonName())
                .set(ObjectUtil.isNotEmpty(param.getProcessedPersonStaffid()), WorkOrder::getAuditStaffid, param.getProcessedPersonStaffid())
                .set(WorkOrder::getUpdateTime, new Date())
                .eq(WorkOrder::getId, param.getId()));
        AssertUtils.isTrue(flag, "该工单状态已变更,请确认");
        //工单转派通知
        Map<String, String> variables = new HashMap<>();
        variables.put("name", workOrder.getName());
        variables.put("code", workOrder.getCode());
        variables.put("type", "审核");
        messageCommonService.sendMessage(MessageConstant.ORDER_TRANSFER_NOTICE, workOrder.getTenantId(), workOrder.getId(), param.getProcessedPersonId(), variables);
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean close(WorkOrderCloseParam param) {
        //2025/03/27工单改造移除工单关闭功能
//        WorkOrder workOrder = this.getById(param.getId());
//        AssertUtils.isTrue(workOrder.getStatus().equals(WorkOrderStatusEnum.REPORTED.getCode()), "该工单状态已变更,请确认");
//        Long tenantId = WebFrameworkUtils.getHeaderTenantId();
//        //添加工单流程
//        UserInfoModel userInfoModel = userInfoFeignClient.detail(userApiService.getCurrentStaffNo()).getBody().getResult();
//        WorkOrderRoman workOrderRoman = new WorkOrderRoman();
//        workOrderRoman.setId(null);
//        workOrderRoman.setTenantId(tenantId);
//        workOrderRoman.setWorkOrderId(param.getId());
//        workOrderRoman.setRomanStatus(WorkOrderStatusEnum.CLOSED.getCode());
//        workOrderRoman.setOperatorId(userInfoModel.getId());
//        workOrderRoman.setOperatorName(userInfoModel.getUserName());
//        workOrderRoman.setOperator("操作人");
//        workOrderRoman.setOperatorStaffid(userInfoModel.getStaffid());
//        workOrderRoman.setOperatorValue(userInfoModel.getStaffid() + " " + userInfoModel.getUserName());
//        workOrderRoman.setRemark(param.getCloseReason());
//        workOrderRomanService.save(workOrderRoman);
//        //更改工单主表
//        boolean flag = this.update(new LambdaUpdateWrapper<WorkOrder>().set(WorkOrder::getStatus, WorkOrderStatusEnum.CLOSED.getCode())
//                .set(StrUtil.isNotEmpty(param.getCloseReason()), WorkOrder::getCloseReason, param.getCloseReason())
//                .set(WorkOrder::getUpdateTime, DateUtil.date())
//                .eq(WorkOrder::getId, param.getId()).eq(WorkOrder::getUpdateTime, workOrder.getUpdateTime()));
//        AssertUtils.isTrue(flag, "该工单状态已变更,请确认");
//        //消息通知
//        Map<String, String> variables = new HashMap<>(4);
//        variables.put("createTime", DateUtils.format(workOrder.getCreateTime()));
//        variables.put("name", workOrder.getName());
//        variables.put("closeReason", param.getCloseReason());
//        messageCommonService.sendMessage(MessageConstant.ORDER_CLOSE, WebFrameworkUtils.getHeaderTenantId(), workOrder.getId(), workOrder.getCreatorId(), variables);
        return Boolean.TRUE;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean handle(WorkOrderHandleParam param) {
        Long tenantId = WebFrameworkUtils.getHeaderTenantId();
        WorkOrder workOrder = getById(param.getId());
        AssertUtils.isTrue(userApiService.getCurrentStaffNo().equals(workOrder.getProcessedPersonId()), "非当前处理人，无权限处理");
        AssertUtils.isTrue(workOrder.getStatus().equals(WorkOrderStatusEnum.PROCESSING.getCode()), "该工单状态已变更,请确认");
        Integer status;
        //添加工单流程
        WorkOrderRoman workOrderRoman = new WorkOrderRoman();
        workOrderRoman.setId(null);
        workOrderRoman.setTenantId(tenantId);
        workOrderRoman.setWorkOrderId(param.getId());
        if(ObjectUtil.isEmpty(workOrder.getAuditUid())){
            status = WorkOrderStatusEnum.COMPLETED.getCode();
            workOrderRoman.setOperatorId(workOrder.getProcessedPersonId());
            workOrderRoman.setOperatorName(workOrder.getProcessedPersonName());
            workOrderRoman.setOperatorStaffid(workOrder.getProcessedPersonStaffid());
            workOrderRoman.setOperator("处理人");
            workOrderRoman.setOperatorValue(workOrder.getProcessedPersonName());
        }else{
            status = WorkOrderStatusEnum.AUDIT.getCode();
            workOrderRoman.setOperatorId(workOrder.getAuditUid());
            workOrderRoman.setOperatorName(workOrder.getAuditUname());
            workOrderRoman.setOperatorStaffid(workOrder.getAuditStaffid());
            workOrderRoman.setOperator("审核人");
            workOrderRoman.setOperatorValue(workOrder.getAuditUname());
        }
        workOrderRoman.setRomanStatus(status);
        workOrderRoman.setRedundancyFour(param.getHandleResult() + "");
        workOrderRoman.setRedundancyFive(JsonUtil.convertListToJsonStr(param.getProcessedPictureUrlList()));
        workOrderRoman.setRemark(param.getProcessedDesc());
        workOrderRomanService.save(workOrderRoman);
        //站内信通知
//        sendMessage(workOrder.getId(), workOrder.getCreatorId(), "您好，您上报的工单已处理完成，您可对工单处理情况进行评价。", "完成通知");

        double expendTime = DateUtil.calculateHourDifference(workOrder.getAcceptTime(), new Date());
        boolean flag = this.update(new LambdaUpdateWrapper<WorkOrder>().set(WorkOrder::getStatus, status)
                .set(!CollectionUtils.isEmpty(param.getProcessedPictureUrlList()), WorkOrder::getProcessedPictureUrl, JsonUtil.convertListToJsonStr(param.getProcessedPictureUrlList()))
                .set(StrUtil.isNotEmpty(param.getProcessedDesc()), WorkOrder::getProcessedDesc, param.getProcessedDesc())
                .set(WorkOrder::getHandleResult, param.getHandleResult())
                .set(WorkOrder::getProcessMode,param.getProcessMode())
                .set(WorkOrderStatusEnum.COMPLETED.getCode().equals(status), WorkOrder::getEndTime,new Date())
                .set(WorkOrder::getUpdateTime, new Date())
                .set(WorkOrderStatusEnum.COMPLETED.getCode().equals(status), WorkOrder::getExpendTime, expendTime)
                .eq(WorkOrder::getId, param.getId()));
        AssertUtils.isTrue(flag, "该工单状态已变更,请确认");
        if (flag) {
//            if (workOrder.getSource().equals(WorkOrderSourceEnum.PERSON.getCode())){
//                //消息通知
//                Map<String, String> variables = new HashMap<>();
//                if (param.getHandleResult() == 1){
//                    variables.put("name", workOrder.getName());
//                    messageCommonService.sendMessage(MessageConstant.ORDER_COMPLETE, WebFrameworkUtils.getHeaderTenantId(), workOrder.getBusinessId(), workOrder.getCreatorId(), variables);
//                }else if (param.getHandleResult() == 2){
//                    variables.put("processedDesc", param.getProcessedDesc());
//                    variables.put("name", workOrder.getName());
//                    messageCommonService.sendMessage(MessageConstant.ORDER_UNCOMPLETE, WebFrameworkUtils.getHeaderTenantId(), workOrder.getBusinessId(), workOrder.getCreatorId(), variables);
//                    //通知分配人
//                    Map<String, String> variables2 = new HashMap<>();
//                    variables2.put("name", workOrder.getName());
//                    variables2.put("processedDesc", param.getProcessedDesc());
//                    messageCommonService.sendMessage(MessageConstant.ORDER_UNCOMPLETE_TOALLOT, WebFrameworkUtils.getHeaderTenantId(), workOrder.getId(), workOrder.getAllotUid(), variables2);
//                }
//            }
            if(WorkOrderStatusEnum.COMPLETED.getCode().equals(status)){
                //工单完成通知
                Map<String, String> variables = new HashMap<>();
                variables.put("name", workOrder.getName());
                variables.put("code", workOrder.getCode());
                messageCommonService.sendMessage(MessageConstant.ORDER_COMPLETED_NOTICE, workOrder.getTenantId(), workOrder.getId(), workOrder.getCreatorId(), variables);
            }
        }
        return flag;
    }

    @Override
    public Boolean audit(WorkOrderAuditParam param) {
        WorkOrder workOrder = this.getById(param.getId());
        AssertUtils.notNull(workOrder,"工单不存在");
        String userId = userApiService.getCurrentStaffNo();
        log.info("工单id:{}, 工单处理人：{}， 当前登录账号：{}",workOrder.getId(), workOrder.getProcessedPersonId(), userId);
        AssertUtils.isTrue(workOrder.getStatus().equals(WorkOrderStatusEnum.AUDIT.getCode()), "该工单状态已变更,请确认");
        AssertUtils.isTrue(workOrder.getAuditUid().equals(userId), "非工单的审核人,请确认");
        // 插入工单流程记录
        WorkOrderRoman workOrderRoman = new WorkOrderRoman();
        workOrderRoman.setWorkOrderId(workOrder.getId());
        workOrderRoman.setTenantId(WebFrameworkUtils.getHeaderTenantId());
        workOrderRoman.setRomanStatus(WorkOrderStatusEnum.COMPLETED.getCode());
        workOrderRoman.setOperatorId(workOrder.getAuditUid());
        workOrderRoman.setOperatorName(workOrder.getAuditUname());
        workOrderRoman.setOperatorStaffid(workOrder.getAuditStaffid());
        workOrderRoman.setCreateTime(new Date());
        workOrderRoman.setOperator("审核人");
        workOrderRoman.setRemark(param.getAuditRemark());
        workOrderRoman.setOperatorValue(workOrder.getAuditUname());
        workOrderRoman.setRedundancyFour(param.getAuditResult() + "");
        workOrderRomanService.save(workOrderRoman);
        double expendTime = DateUtil.calculateHourDifference(workOrder.getAcceptTime(), new Date());
        //更改工单主表
        boolean flag = this.update(new LambdaUpdateWrapper<WorkOrder>()
                .set(WorkOrder::getUpdateTime, new Date())
                .set(ObjectUtil.isNotEmpty(param.getAuditRemark()), WorkOrder::getAuditRemark, param.getAuditRemark())
                .set(ObjectUtil.isNotEmpty(param.getAuditResult()),WorkOrder::getAuditResult, param.getAuditResult())
                .set(WorkOrder::getEndTime, new Date())
                .set(WorkOrder::getExpendTime, expendTime)
                .set(WorkOrder::getStatus, WorkOrderStatusEnum.COMPLETED.getCode())
                .eq(WorkOrder::getId, param.getId()));
        AssertUtils.isTrue(flag, "该工单状态已变更,请确认");
        if(Status.enabled.getKey().equals(param.getAuditResult())){
            //工单完成通知
            Map<String, String> variables = new HashMap<>();
            variables.put("name", workOrder.getName());
            variables.put("code", workOrder.getCode());
            messageCommonService.sendMessage(MessageConstant.ORDER_COMPLETED_NOTICE, workOrder.getTenantId(), workOrder.getId(), workOrder.getCreatorId(), variables);
        }else{
            //工单审核不通过通知
            Map<String, String> variables = new HashMap<>();
            variables.put("name", workOrder.getName());
            variables.put("code", workOrder.getCode());
            messageCommonService.sendMessage(MessageConstant.ORDER_AUDIT_FAIL_NOTICE, workOrder.getTenantId(), workOrder.getId(), workOrder.getProcessedPersonId(), variables);
        }
        return flag;
    }

    @Override
    public Boolean remove(Long id) {
        WorkOrder workOrder = this.getById(id);
        AssertUtils.notNull(workOrder, SystemResultCode.RESULT_DATA_NONE.message());
        WorkOrder work = new WorkOrder();
        work.setId(id);
        work.setDeleted((int) Status.disabled.getKey());
        this.updateById(work);
        workPlanDetailService.update(new LambdaUpdateWrapper<WorkPlanDetail>().set(WorkPlanDetail::getDeleted, (int) Status.disabled.getKey()).eq(WorkPlanDetail::getWorkId, id));
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean fallback(WorkOrderBackParam param) {
        WorkOrder workOrder = getById(param.getId());
        AssertUtils.isTrue(userApiService.getCurrentStaffNo().equals(workOrder.getProcessedPersonId()), "非当前处理人，无权限处理");
        AssertUtils.isTrue(workOrder.getStatus().equals(WorkOrderStatusEnum.PROCESSING.getCode()), "该工单状态已变更,请确认");
        Long tenantId = WebFrameworkUtils.getHeaderTenantId();
        //工单处理人置空
        LambdaUpdateWrapper<WorkOrder> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(WorkOrder::getProcessedPersonId, null);
        updateWrapper.eq(WorkOrder::getId, workOrder.getId());
        update(updateWrapper);


        //添加工单流程
        UserInfoModel userInfoModel = userApiService.getCurrentUserInfo();
        WorkOrderRoman workOrderRoman = new WorkOrderRoman();
        workOrderRoman.setId(null);
        workOrderRoman.setTenantId(tenantId);
        workOrderRoman.setWorkOrderId(param.getId());
        workOrderRoman.setAppExhibition("0");
        workOrderRoman.setRomanStatus(WorkOrderStatusEnum.REPORTED.getCode());
        workOrderRoman.setOperatorId(userInfoModel.getId());
        workOrderRoman.setOperatorName(userInfoModel.getUserName());
        workOrderRoman.setOperator("操作人");
        workOrderRoman.setOperatorStaffid(userInfoModel.getStaffid());
        workOrderRoman.setOperatorValue(userInfoModel.getStaffid() + " " + userInfoModel.getUserName());
        workOrderRoman.setRemark(param.getReturnReason());
        workOrderRomanService.save(workOrderRoman);
        List<WorkOrderRoman> workOrderRomanList = workOrderRomanService.list(new LambdaQueryWrapper<WorkOrderRoman>().eq(WorkOrderRoman::getWorkOrderId, param.getId())
                .orderByAsc(WorkOrderRoman::getCreateTime));
        WorkOrderRoman orderRoman = workOrderRomanList.get(workOrderRomanList.size() - 2);
        orderRoman.setAppExhibition("0");
        workOrderRomanService.updateById(orderRoman);

        return this.update(new LambdaUpdateWrapper<WorkOrder>().set(WorkOrder::getStatus, WorkOrderStatusEnum.REPORTED.getCode())
                .set(StrUtil.isNotEmpty(param.getReturnReason()), WorkOrder::getReturnReason, param.getReturnReason())
                .set(WorkOrder::getUpdateTime, new Date())
                .eq(WorkOrder::getId, param.getId()));
    }

    @Override
    public IPage<WorkOrderModel> handlePage(WorkOrderPageParam param) {
        IPage<WorkOrder> page = new Page<>(param.getCurrent(), param.getSize());
        Long tenantId = WebFrameworkUtils.getHeaderTenantId();
        LambdaQueryWrapper<WorkOrder> wrapper = Wrappers.<WorkOrder>lambdaQuery()
                .eq(ObjectUtil.isNotNull(tenantId), WorkOrder::getTenantId, tenantId)
                .eq(WorkOrder::getProcessedPersonId, param.getUserId())
                .like(ObjectUtil.isNotEmpty(param.getName()), WorkOrder::getName, param.getName())
                .eq(ObjectUtil.isNotNull(param.getStatus()), WorkOrder::getStatus, param.getStatus())
                .in(CollUtil.isNotEmpty(param.getStatusList()), WorkOrder::getStatus, param.getStatusList())
                .eq(ObjectUtil.isNotEmpty(param.getSource()), WorkOrder::getSource, param.getSource())
                .eq(ObjectUtil.isNotEmpty(param.getType()), WorkOrder::getType, param.getType())
                .ge(ObjectUtil.isNotEmpty(param.getStartTime()), WorkOrder::getCreateTime, param.getStartTime())
                .le(ObjectUtil.isNotEmpty(param.getEndTime()), WorkOrder::getCreateTime, param.getEndTime())
                .eq(WorkOrder::getDeleted, Status.enabled.getKey())
                .orderByDesc(WorkOrder::getCreateTime);
        // 分页查询
        IPage<WorkOrder> iPage = this.page(page, wrapper);
        List<WorkOrderModel> workOrderModels = BeanUtils.convertListTo(iPage.getRecords(), WorkOrderModel::new);
        return ConvertUtil.pageConvert(iPage.getCurrent(), iPage.getTotal(), iPage.getSize(), workOrderModels);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public WorkOrderModel report(WorkOrderParam param) {
        Long tenantId = WebFrameworkUtils.getHeaderTenantId();
        UserInfoModel userInfoModel = userApiService.getCurrentUserInfo();
        List<WorkOrderDeviceParam> deviceParams = param.getWorkOrderDeviceParams();

        WorkOrder workOrder = BeanUtils.convertTo(param, WorkOrder::new);
        //图片
        workOrder.setProblemPictureUrl(JsonUtil.convertListToJsonStr(param.getProblemPictureUrlList()));
        workOrder.setSource(WorkOrderSourceEnum.PERSON.getCode());
        workOrder.setStatus(WorkOrderStatusEnum.REPORTED.getCode());
        workOrder.setCreateBy(userInfoModel.getUserName());
        workOrder.setCreatorId(userInfoModel.getStaffid());
        workOrder.setId(null);
        save(workOrder);

        //添加工单设备关联表
        if (CollUtil.isNotEmpty(deviceParams)) {
            List<WorkOrderDevice> workOrderDevices = BeanUtils.convertListTo(deviceParams, WorkOrderDevice::new);
            workOrderDevices.forEach(f -> {
                f.setWorkOrderId(workOrder.getId());
                f.setTenantId(tenantId);
            });
            workOrderDeviceService.saveBatch(workOrderDevices);
        }
        //添加工单流程
        WorkOrderRoman workOrderRoman = new WorkOrderRoman();
        workOrderRoman.setId(null);
        workOrderRoman.setTenantId(tenantId);
        workOrderRoman.setWorkOrderId(workOrder.getId());
        workOrderRoman.setRomanStatus(WorkOrderStatusEnum.REPORTED.getCode());
        workOrderRoman.setOperatorId(userInfoModel.getId());
        workOrderRoman.setOperatorName(userInfoModel.getUserName());
        workOrderRoman.setOperator("上报人");
        workOrderRoman.setOperatorStaffid(userInfoModel.getStaffid());
        workOrderRoman.setOperatorValue(userInfoModel.getStaffid() + " " + userInfoModel.getUserName());
        workOrderRoman.setRemark(param.getRemark());
        workOrderRomanService.save(workOrderRoman);
        return BeanUtil.toBean(workOrder, WorkOrderModel.class);
    }

    /**
     * 根据ID获取工作订单的详细信息，并转换为WorkOrderModel对象返回
     * 如果工作订单来源于问题上报或报警，则分别获取相应的问题报告详细信息或报警信息，并设置到WorkOrderModel对象中
     *
     * @param id 工作订单的ID
     * @return 包含工作订单详细信息的WorkOrderModel对象
     */
    @Override
    public WorkOrderModel appDetail(Long id) {
        // 根据ID获取工作订单对象
        WorkOrder workOrder = this.getById(id);
        // 将工作订单对象转换为WorkOrderModel对象
        WorkOrderModel workOrderModel = BeanUtil.toBean(workOrder, WorkOrderModel.class);

        // 获取与该工作订单相关的所有工作订单流程对象，并按创建时间排序
        List<WorkOrderRoman> workOrderRomanList = workOrderRomanService.list(new LambdaQueryWrapper<WorkOrderRoman>().eq(WorkOrderRoman::getWorkOrderId, id).orderByAsc(WorkOrderRoman::getCreateTime));
        workOrderModel.setWorkOrderRomanModels(BeanUtils.convertListTo(workOrderRomanList.stream().sorted(Comparator.comparing(WorkOrderRoman::getCreateTime)).collect(Collectors.toList()), WorkOrderRomanModel::new));

        // 根据工作订单的来源，获取相应的问题报告详细信息或报警信息
        if (workOrderModel.getSource().equals(WorkOrderSourceEnum.PERSON.getCode())){
            // 如果工作订单来源于问题上报，获取问题报告详细信息
            ProblemReportQueryParam problemReportQueryParam = new ProblemReportQueryParam();
            problemReportQueryParam.setId(workOrder.getBusinessId());
            ProblemReportModel detail = problemReportService.appDetail(problemReportQueryParam);
            workOrderModel.setProblemReportModel(detail);
        }else if (workOrderModel.getSource().equals(WorkOrderSourceEnum.ALARM.getCode())){
            // 如果工作订单来源于报警，获取报警信息
            AlarmInfoModel alarmInfo = alarmInfoService.detail(workOrder.getBusinessId());
            workOrderModel.setAlarmInfoModel(alarmInfo);
        }else {
            //工单计划详情
            getWorkPlan(workOrderModel);
        }
        //设置工单权限
        handleWorkPermission(workOrderModel);
        // 返回包含工作订单详细信息的WorkOrderModel对象
        return workOrderModel;
    }

    @Override
    public IPage<WorkOrderModel> historyPage(WorkOrderPageParam param) {
        IPage<WorkOrder> page = new Page<>(param.getCurrent(), param.getSize());
        Long tenantId = WebFrameworkUtils.getHeaderTenantId();
        LambdaQueryWrapper<WorkOrder> wrapper = Wrappers.<WorkOrder>lambdaQuery()
                .eq(ObjectUtil.isNotNull(tenantId), WorkOrder::getTenantId, tenantId)
                .eq(ObjectUtil.isNotNull(param.getStatus()), WorkOrder::getStatus, param.getStatus())
                .in(CollUtil.isNotEmpty(param.getStatusList()), WorkOrder::getStatus, param.getStatusList())
                .eq(WorkOrder::getCreatorId, param.getUserId())
                .ge(ObjectUtil.isNotEmpty(param.getStartTime()), WorkOrder::getCreateTime, param.getStartTime())
                .le(ObjectUtil.isNotEmpty(param.getEndTime()), WorkOrder::getCreateTime, param.getEndTime())
                .eq(WorkOrder::getDeleted, Status.enabled.getKey())
                .orderByDesc(WorkOrder::getCreateTime);
        // 分页查询
        IPage<WorkOrder> iPage = this.page(page, wrapper);
        List<WorkOrderModel> workOrderModels = BeanUtils.convertListTo(iPage.getRecords(), WorkOrderModel::new);
        List<Long> idList = workOrderModels.stream().map(WorkOrderModel::getId).collect(Collectors.toList());
        Map<Long, List<WorkOrderDevice>> map = new HashMap<>(4);
        if (CollUtil.isNotEmpty(idList)) {
            map = workOrderDeviceService.list(new LambdaQueryWrapper<WorkOrderDevice>().in(WorkOrderDevice::getWorkOrderId, idList)).stream().collect(Collectors.groupingBy(a -> a.getWorkOrderId()));
        }
        for (WorkOrderModel f : workOrderModels) {
            if (ObjectUtil.isEmpty(map)) {
                continue;
            }
            if (CollUtil.isEmpty(map.get(f.getId()))) {
                continue;
            }
            f.setWorkOrderDeviceModels(BeanUtils.convertListTo(map.get(f.getId()), WorkOrderDeviceModel::new));
        }
        return ConvertUtil.pageConvert(iPage.getCurrent(), iPage.getTotal(), iPage.getSize(), workOrderModels);
    }

    /**
     * 接受工单
     *
     * 该方法用于处理工单的接收流程它首先验证工单ID是否存在和有效，然后检查工单的状态是否为“已报告”，
     * 并确认当前操作用户是否为指定的工单处理人如果所有验证通过，则更新工单状态为“处理中”，
     * 并记录工单处理人信息以及处理时间此外，还会创建一条工单流程记录，用于跟踪工单处理过程
     *
     * @param param 包含工单ID的参数对象，用于指定需要接收的工单
     * @return 返回一个布尔值，表示工单是否成功被接收
     */
    @Override
    public Boolean receive(WorkOrderHandleParam param) {
        // 根据ID获取工单详情
        WorkOrder workOrder = this.getById(param.getId());
        // 验证工单是否存在
        AssertUtils.notNull(workOrder,"工单不存在");
        // 检查工单状态是否为“待处理”，以确保工单可以被接受
        if (!workOrder.getStatus().equals(WorkOrderStatusEnum.REPORTED.getCode()))
            throw new BaseException("该工单状态已变更,请确认");
        // 确认当前操作用户是否为工单指定的处理人
        if (!workOrder.getProcessedPersonId().equals(userApiService.getCurrentStaffNo()))
            throw new BaseException("非工单处理人");
        if(workOrder.getSource().equals(WorkOrderSourceEnum.PERSON.getCode()) || workOrder.getSource().equals(WorkOrderSourceEnum.ALARM.getCode())){
            double responseTime = DateUtil.calculateHourDifference(workOrder.getCreateTime(), new Date());
            workOrder.setResponseTime(responseTime);
        }
        // 更新工单的接受时间和状态为“处理中”
        workOrder.setAcceptTime(new Date());
        workOrder.setStatus(WorkOrderStatusEnum.PROCESSING.getCode());
        this.updateById(workOrder);

        // 获取当前操作用户的详细信息
        UserInfoModel userInfo = userApiService.getCurrentUserInfo();

        // 插入工单流程记录
        WorkOrderRoman workOrderRoman = new WorkOrderRoman();
        workOrderRoman.setWorkOrderId(workOrder.getId());
        workOrderRoman.setTenantId(WebFrameworkUtils.getHeaderTenantId());
        workOrderRoman.setRomanStatus(WorkOrderStatusEnum.PROCESSING.getCode());
        workOrderRoman.setOperatorId(userInfo.getId());
        workOrderRoman.setOperatorName(userInfo.getUserName());
        workOrderRoman.setOperatorStaffid(userInfo.getStaffid());
        workOrderRoman.setCreateTime(new Date());
        workOrderRoman.setOperator("处理人");
        workOrderRoman.setOperatorValue(userInfo.getStaffid() + " " + userInfo.getUserName());
        workOrderRomanService.save(workOrderRoman);
        return true;
    }

    /**
     * 评价工单
     * 此方法用于对已完成的工单进行评价，仅允许上报人和工单分配人进行评价
     *
     * @param param 包含评价信息的参数对象，包括工单ID、满意度评价、评价内容等
     * @return Boolean 如果评价成功，则返回Boolean.TRUE；否则抛出异常
     * @throws GenericException 如果工单已被评价或当前用户无权评价，则抛出异常
     */
    @Override
    public Boolean evaluation(WorkOrderEvaluationParam param) {
        // 获取工单ID和当前操作用户ID
        Long workOrderId = param.getId();
        String headerUserId = userApiService.getCurrentStaffNo();
        // 根据工单ID获取工单详情
        WorkOrder workOrder = this.getById(workOrderId);
        AssertUtils.notNull(workOrder,"工单不存在");
        workEvaluateService.list(workOrderId).forEach(workEvaluateModel -> {
            AssertUtils.isFalse(workEvaluateModel.getOperateUid().equals(headerUserId), "当前用户已评价");
        });
        // 获取当前用户信息
        UserInfoModel userInfoModel = userApiService.getByStaffNo(headerUserId);
        WorkEvaluateParam workEvaluateParam = new WorkEvaluateParam();
        workEvaluateParam.setWorkId(workOrderId);
        workEvaluateParam.setScore(param.getSatisfaction());
        workEvaluateParam.setContent(param.getEvaluateContent());
        workEvaluateParam.setOperateUid(headerUserId);
        workEvaluateParam.setOperateUname(userInfoModel.getUserName());
        workEvaluateParam.setOperateStaffid(userInfoModel.getStaffid());
        workEvaluateService.add(workEvaluateParam);
        //关注人消息
        sendAttentionMessage(workEvaluateParam, userInfoModel);
        // 返回评价成功信号
        return Boolean.TRUE;
    }

    @Override
    public void downloadExcel(WorkOrderPageParam param , HttpServletResponse response) {
        try {
            List<String> userIdList = getRoleUserId();
            String userId = userApiService.getCurrentStaffNo();
            param.setTenantId(WebFrameworkUtils.getHeaderTenantId());
            param.setCreatorId(userId);
            if(CollectionUtil.isNotEmpty(userIdList) && userIdList.contains(userId)){
                param.setCreatorId(null);
            }
            IPage<WorkOrder> page = new Page<>(1, Integer.MAX_VALUE);
            IPage<WorkOrder> pageDate =  workOrderRepository.selectPageByParam(page,param);
            List<WorkOrderExportModel> workOrderModels = BeanUtils.convertListTo(pageDate.getRecords(), WorkOrderExportModel::new);
            workOrderModels.forEach(workOrderModel -> {
                workOrderModel.setSourceDesc(WorkOrderSourceEnum.getName(workOrderModel.getSource()));
                workOrderModel.setAllotUname(ObjectUtil.isEmpty(workOrderModel.getAllotUstaffid()) ? "" : workOrderModel.getAllotUname());
                workOrderModel.setCreateTimeStr(DateUtils.format(workOrderModel.getCreateTime(), DateUtils.DATE_FORMAT_19));
                workOrderModel.setProcessedPersonName(ObjectUtil.isEmpty(workOrderModel.getProcessedPersonStaffid()) ? "": workOrderModel.getProcessedPersonName());
                workOrderModel.setTransferUname(ObjectUtil.isEmpty(workOrderModel.getTransferUid()) ? "否" : "是（" + workOrderModel.getTransferUname() + "）");
                workOrderModel.setStatusDesc(WorkOrderStatusEnum.getName(workOrderModel.getStatus()));
                workOrderModel.setDispatchTypeDesc(DispatchTypeEnum.getName(workOrderModel.getDispatchType()));
            });
            // 设置响应头
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            String encodedFileName = URLEncoder.encode("工单信息", "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-Disposition", "attachment;filename=" + encodedFileName + ".xlsx");


            ExcelExportUtils.exportExcelWithTwoHeaders(
                    response,
                    encodedFileName,
                    "苍南园区工单信息统计表",
                    workOrderModels,
                    WorkOrderExportModel.class
            );
        } catch (Exception e) {
            log.error("导出失败：",e);
            throw new BaseException("导出失败");
        }
    }


    @Override
    public void executeWorkOrderOutTime() {
        List<WorkOrder> workOrders = this.list(Wrappers.<WorkOrder>lambdaQuery()
                        .eq(WorkOrder::getOutStatus, Status.disabled.getKey())
                        .isNotNull(WorkOrder::getOutTime)
                        .eq(WorkOrder::getDeleted, Status.enabled.getKey())
                        .ne(WorkOrder::getStatus, WorkOrderStatusEnum.COMPLETED.getCode()));
        if (CollectionUtil.isEmpty(workOrders)){
            return;
        }
        workOrders.forEach(workOrder -> {
            if (workOrder.getOutTime().before(new Date())){
                String name = "【已超时】" +workOrder.getName();
                this.update(Wrappers.<WorkOrder>lambdaUpdate()
                        .eq(WorkOrder::getId, workOrder.getId())
                        .set(WorkOrder::getName, name)
                        .set(WorkOrder::getOutStatus, Status.enabled.getKey()));
                //超时消息通知
                if(ObjectUtil.isNotEmpty( workOrder.getProcessedPersonId())){
                    Map<String, String> variables = new HashMap<>();
                    variables.put("name", workOrder.getName());
                    variables.put("code", workOrder.getCode());
                    messageCommonService.sendMessage(MessageConstant.ORDER_OUT_TIME_NOTICE, workOrder.getTenantId(), workOrder.getId(), workOrder.getProcessedPersonId(), variables);
                }
                //工单催办通知
                if(WorkOrderStatusEnum.REPORTED.getCode().equals(workOrder.getStatus())){
                    String userId = ObjectUtil.isEmpty(workOrder.getAllotUid()) ? workOrder.getCreatorId() : workOrder.getAllotUid();
                    Map<String, String> variables = new HashMap<>();
                    variables.put("name", workOrder.getName());
                    variables.put("code", workOrder.getCode());
                    messageCommonService.sendMessage(MessageConstant.ORDER_URGE_NOTICE, workOrder.getTenantId(), workOrder.getId(), userId, variables);
                }
            }
        });
    }

    /**
     * 根据业务ID和业务类型查询工单id
     *
     * @param param 查询参数
     * @return 工单信息
     */
    @Override
    public Long queryByBusinessIdAndType(WorkOrderPageParam param) {
        try {
            if (param.getBusinessId() == null || StringUtils.isEmpty(param.getSource())){
                throw new BaseException("参数业务id和工单来源枚举不能为空");
            }
            WorkOrder workOrder = workOrderRepository.selectOne(new LambdaQueryWrapper<WorkOrder>().eq(WorkOrder::getBusinessId, param.getBusinessId()).eq(WorkOrder::getDeleted, Status.enabled.getKey()).eq(WorkOrder::getSource, param.getSource()).eq(WorkOrder::getTenantId, WebFrameworkUtils.getHeaderTenantId()));
            if (workOrder == null)
                throw new BaseException("未找到工单信息");
            return workOrder.getId();
        }catch (GenericException e){
            throw e;
        }catch (Exception e) {
            throw new BaseException("查询工单信息失败");
        }
    }

    @Override
    public IPage<WorkOrderModel> businessPage(WorkOrderPageParam param) {
        IPage<WorkOrder> page = new Page<>(param.getCurrent(), param.getSize());
        LambdaQueryWrapper<WorkOrder> wrapper = Wrappers.<WorkOrder>lambdaQuery()
                .eq(ObjectUtil.isNotEmpty(param.getSource()), WorkOrder::getSource, param.getSource())
                .eq(ObjectUtil.isNotEmpty(param.getBusinessId()), WorkOrder::getBusinessId, param.getBusinessId())
                .eq(ObjectUtil.isNotEmpty(WebFrameworkUtils.getHeaderTenantId()), WorkOrder::getTenantId, WebFrameworkUtils.getHeaderTenantId())
                .like(ObjectUtil.isNotEmpty(param.getName()), WorkOrder::getName, param.getName())
                .eq(ObjectUtil.isNotEmpty(param.getStatus()), WorkOrder::getStatus, param.getStatus())
                .in(CollectionUtil.isNotEmpty(param.getStatusList()), WorkOrder::getStatus, param.getStatusList())
                .ge(ObjectUtil.isNotEmpty(param.getStartTime()), WorkOrder::getEndTime, param.getStartTime())
                .le(ObjectUtil.isNotEmpty(param.getEndTime()), WorkOrder::getEndTime, param.getEndTime())
                .eq(WorkOrder::getDeleted, Status.enabled.getKey())
                .orderByDesc(WorkOrder::getCreateTime);
        // 分页查询
        IPage<WorkOrder> pageDate = this.page(page, wrapper);
        List<WorkOrderModel> workOrderModels = BeanUtils.convertListTo(pageDate.getRecords(), WorkOrderModel::new);
        return ConvertUtil.pageConvert(pageDate.getCurrent(), pageDate.getTotal(), pageDate.getSize(), workOrderModels);
    }

    @Override
    public Long queryWorkOrderCount(String source, Long tenantId) {
        return (long) this.count(new LambdaQueryWrapper<WorkOrder>().eq(WorkOrder::getSource, source).eq(WorkOrder::getDeleted, Status.enabled.getKey()).eq(ObjectUtil.isNotEmpty(tenantId), WorkOrder::getTenantId, tenantId));
    }

    @Override
    public WorkOrder addPlanWordOrder(WorkPlanModel plan, String spaceName){
        List<PropertyScheduleUserModel> scheduleUserModels = propertyScheduleService.findUserList(plan.getScheduleId());
        //manager:是否是管理员，保持0：是；1否（前端要改的地方过多，保持不动）
        PropertyScheduleUserModel leader = scheduleUserModels.stream().filter(scheduleUserModel -> scheduleUserModel.getManager().equals((0))).findFirst().orElse(null);
        // 创建工单
        WorkOrder workOrder = new WorkOrder();
        workOrder.setName(plan.getPlanName() + "("+ cn.hutool.core.date.DateUtil.format(new Date(), "MMdd") + ")");
        workOrder.setCode(plan.getPlanCode());
        workOrder.setType(plan.getPlanType());
        workOrder.setSource(plan.getPlanSource());
        if(DispatchTypeEnum.ASSIGN.getCode().equals(plan.getDispatchType())){
            workOrder.setProcessedPersonName(plan.getHandleUname());
            workOrder.setProcessedPersonId(plan.getHandleUid());
            workOrder.setProcessedPersonStaffid(plan.getHandleStaffid());
            workOrder.setStatus(WorkOrderStatusEnum.REPORTED.getCode());
            UserInfoModel userPeron = userApiService.getSecondDeptByStaffNo(plan.getHandleStaffid());
            workOrder.setDepartmentId(userPeron.getDepartmentSecondId());
            workOrder.setDepartmentName(userPeron.getDepartmentSecondName());
        }else if(DispatchTypeEnum.GROUPING.getCode().equals(plan.getDispatchType())){
            workOrder.setStatus(WorkOrderStatusEnum.ALLOT.getCode());
        }else if(DispatchTypeEnum.LEADER.getCode().equals(plan.getDispatchType()) && ObjectUtil.isNotEmpty(leader)){
            workOrder.setAllotUname(leader.getUserName());
            workOrder.setAllotUid(leader.getUserId());
            workOrder.setAllotUstaffid(leader.getStaffid());
            workOrder.setStatus(WorkOrderStatusEnum.ALLOT.getCode());
        }
        workOrder.setTenantId(plan.getTenantId());
        workOrder.setCreateTime(new Date());
        workOrder.setCreateBy(plan.getCreateBy());
        workOrder.setCreatorId(plan.getCreatorId());
        workOrder.setUpdateBy(plan.getCreateBy());
        workOrder.setUpdatorId(plan.getCreatorId());
        workOrder.setUpdateTime(new Date());
        workOrder.setSpaceName(spaceName);
        workOrder.setSpaceId(plan.getSpaceId());
        workOrder.setBusinessId(plan.getId());
        workOrder.setDispatchType(plan.getDispatchType());
        if(Status.enabled.getKey().equals(plan.getAuditType())){
            workOrder.setAuditUid(plan.getAuditUid());
            workOrder.setAuditUname(plan.getAuditUname());
            workOrder.setAuditStaffid(plan.getAuditStaffid());
        }
        //生成超时时间
        if(ObjectUtil.isNotEmpty(plan.getPlanDuration()) && ObjectUtil.isNotEmpty(plan.getPlanStartTime())){
            Date combinedDateTime = cn.hutool.core.date.DateUtil.parse(cn.hutool.core.date.DateUtil.format(new Date(), "yyyy-MM-dd") + " " + cn.hutool.core.date.DateUtil.format(plan.getPlanStartTime(), "HH:mm:ss"));
            Date timeout = cn.hutool.core.date.DateUtil.offsetHour(combinedDateTime, plan.getPlanDuration());
            workOrder.setOutTime(timeout);
        }
        //保存工单
        this.save(workOrder);
        //生成工单流程
        this.addWorkPlanRoman(plan, workOrder, leader);
        //生成工单详情
        this.addWorkPlanDetail(plan, workOrder);
        //保存工单分组人员
        this.addScheduleUser(plan, workOrder, scheduleUserModels);
        return workOrder;
    }

    @Override
    public Integer findOutStatus(Long id) {
        WorkOrder workOrder = this.getById(id);
        return workOrder.getOutStatus();
    }

    private void addWorkPlanRoman(WorkPlanModel plan, WorkOrder workOrder, PropertyScheduleUserModel leader){
        //生成工单流程
        WorkOrderRoman workOrderRoman = new WorkOrderRoman();
        workOrderRoman.setWorkOrderId(workOrder.getId());
        if(DispatchTypeEnum.ASSIGN.getCode().equals(plan.getDispatchType())){
            workOrderRoman.setRomanStatus(WorkOrderStatusEnum.REPORTED.getCode());
            workOrderRoman.setOperatorId(plan.getHandleUid());
            workOrderRoman.setOperatorName(plan.getHandleUname());
            workOrderRoman.setOperatorStaffid(plan.getHandleStaffid());
            workOrderRoman.setOperator("处理人");
            workOrderRoman.setOperatorValue(plan.getHandleStaffid() + " " + plan.getHandleUname());
        }else if(DispatchTypeEnum.GROUPING.getCode().equals(plan.getDispatchType())){
            workOrderRoman.setRomanStatus(WorkOrderStatusEnum.ALLOT.getCode());
            workOrderRoman.setOperator("分配人");
        }else if(DispatchTypeEnum.LEADER.getCode().equals(plan.getDispatchType()) && ObjectUtil.isNotEmpty(leader)){
            workOrderRoman.setRomanStatus(WorkOrderStatusEnum.ALLOT.getCode());
            workOrderRoman.setOperatorId(leader.getUserId());
            workOrderRoman.setOperatorName(leader.getUserName());
            workOrderRoman.setOperatorStaffid(leader.getStaffid());
            workOrderRoman.setOperator("分配人");
            workOrderRoman.setOperatorValue(leader.getStaffid() + " " + leader.getUserName());
        }
        workOrderRoman.setTenantId(workOrder.getTenantId());
        workOrderRoman.setCreateTime(new Date());
        workOrderRoman.setCreateBy(workOrder.getCreateBy());
        workOrderRoman.setCreatorId(workOrder.getCreatorId());
        workOrderRoman.setUpdateBy(workOrder.getUpdateBy());
        workOrderRoman.setUpdatorId(workOrder.getUpdatorId());
        workOrderRoman.setUpdateTime(new Date());
        workOrderRomanService.save(workOrderRoman);
    }

    private void addWorkPlanDetail(WorkPlanModel plan, WorkOrder workOrder){
        WorkPlanDetail planDetail = BeanUtils.convertTo(plan, WorkPlanDetail::new);
        planDetail.setId(null);
        planDetail.setSpaceName(workOrder.getSpaceName());
        planDetail.setWorkId(workOrder.getId());
        planDetail.setPlanId(plan.getId());
        planDetail.setPlanType(workOrder.getSource());
        planDetail.setTenantId(workOrder.getTenantId());
        planDetail.setCreateTime(new Date());
        planDetail.setCreateBy(workOrder.getCreateBy());
        planDetail.setCreatorId(workOrder.getCreatorId());
        planDetail.setUpdateBy(workOrder.getUpdateBy());
        planDetail.setUpdatorId(workOrder.getUpdatorId());
        planDetail.setUpdateTime(new Date());
        workPlanDetailService.save(planDetail);
    }

    private void addScheduleUser(WorkPlanModel plan, WorkOrder workOrder, List<PropertyScheduleUserModel> scheduleUserModels){
        if(CollectionUtil.isNotEmpty(scheduleUserModels)){
            List<WorkScheduleUser> workScheduleUsers = scheduleUserModels.stream().map(scheduleUser -> {
                WorkScheduleUser workScheduleUser = new WorkScheduleUser();
                BeanUtils.copyProperties(scheduleUser, workScheduleUser);
                workScheduleUser.setId(null);
                workScheduleUser.setWorkId(workOrder.getId());
                workScheduleUser.setScheduleId(plan.getScheduleId());
                workScheduleUser.setScheduleName(plan.getScheduleName());
                workScheduleUser.setTenantId(workOrder.getTenantId());
                workScheduleUser.setCreateTime(new Date());
                workScheduleUser.setCreateBy(workOrder.getCreateBy());
                workScheduleUser.setCreatorId(workOrder.getCreatorId());
                workScheduleUser.setUpdateBy(workOrder.getCreateBy());
                workScheduleUser.setUpdatorId(workOrder.getCreatorId());
                workScheduleUser.setUpdateTime(new Date());
                return workScheduleUser;
            }).collect(Collectors.toList());
            //工单分组人员
            workScheduleUserService.saveBatch(workScheduleUsers);
        }
    }


    private void getWorkPlan(WorkOrderModel workOrderModel) {
        List<WorkPlanDetail> details = workPlanDetailService.list(Wrappers.<WorkPlanDetail>lambdaQuery().eq(WorkPlanDetail::getWorkId, workOrderModel.getId()));
        if (CollectionUtil.isEmpty(details)){
            return;
        }
        List<WorkTaskModel> taskModels =  workTaskService.list(workOrderModel.getId());
        if(workOrderModel.getSource().equals(WorkOrderSourceEnum.PATROLPLAN.getCode())){
            taskModels = CollectionUtil.isEmpty(taskModels) ? CollectionUtil.newArrayList() :  taskModels.stream().filter(p->ObjectUtil.isNotEmpty(p.getName())).collect(Collectors.toList());
        }
        Map<Integer, List<WorkTaskModel>> taskMap = CollectionUtil.isEmpty(taskModels) ? new LinkedHashMap<>() : taskModels.stream().collect(Collectors.groupingBy(WorkTaskModel::getTaskGroup, LinkedHashMap::new, Collectors.toList()));
        List<WorkTaskItemModel> taskItemModels = workTaskItemService.list(workOrderModel.getId());
        Map<Integer, List<WorkTaskItemModel>> taskItemMap = CollectionUtil.isEmpty(taskItemModels) ? new LinkedHashMap<>() : taskItemModels.stream().collect(Collectors.groupingBy(WorkTaskItemModel::getTaskGroup, LinkedHashMap::new, Collectors.toList()));
        List<WorkTaskGroupModel> workTaskGroupModels = new ArrayList<>();
        WorkPlanDetailModel model = BeanUtils.convertTo(details.get(0), WorkPlanDetailModel::new);
        if(workOrderModel.getSource().equals(WorkOrderSourceEnum.METERPLAN.getCode())){
            //工单设备
            List<WorkOrderDevice> workOrderDevices = workOrderDeviceService.list(new LambdaQueryWrapper<WorkOrderDevice>().eq(WorkOrderDevice::getWorkOrderId, workOrderModel.getId()));
            List<WorkOrderDeviceModel> workOrderDeviceModels = CollectionUtil.isEmpty(workOrderDevices) ? CollectionUtil.newArrayList() :BeanUtils.convertListTo(workOrderDevices, WorkOrderDeviceModel::new);
            model.setWorkOrderDeviceList(workOrderDeviceModels);
            model.setWorkOrderDeviceNum((long) workOrderDeviceModels.size());
        }else{
            taskMap.forEach((key, value) -> {
                WorkTaskGroupModel workTaskGroupModel = new WorkTaskGroupModel();
                workTaskGroupModel.setTaskGroup(key);
                workTaskGroupModel.setWorkTaskModels(value);
                workTaskGroupModel.setName(value.get(0).getName());
                workTaskGroupModel.setType(WorkTaskTypeEnum.INVENTORY_DEVICE.getCode().equals(value.get(0).getBusinessType()) ? 2 : 1);
                workTaskGroupModel.setWorkTaskItemModels(taskItemMap.get(key));
                workTaskGroupModel.setTaskItemNum((CollectionUtil.isEmpty(taskItemMap.get(key)) ? 0L :  (long)taskItemMap.get(key).size()));
                workTaskGroupModel.setTaskNum(((long)value.size()));
                workTaskGroupModels.add(workTaskGroupModel);
            });
        }
        //工单任务组
        model.setWorkTaskGroupModels(workTaskGroupModels);
        //工单材料
        model.setWorkMaterialModels( workMaterialService.list(workOrderModel.getId()));
        //计划生成的第一个工单
        WorkPlanDetail detail = workPlanDetailService.getOne(Wrappers.<WorkPlanDetail>lambdaQuery().select(WorkPlanDetail::getId, WorkPlanDetail::getCreateTime)
                .eq(WorkPlanDetail::getPlanId, model.getPlanId())
                .eq(WorkPlanDetail::getDeleted, Status.enabled.getKey())
                .orderByAsc(WorkPlanDetail::getCreateTime)
                .last("LIMIT 1"));
        model.setWorkPlanFirstTime(ObjectUtil.isEmpty(detail) ? null : detail.getCreateTime());

        //计划工单完成的次数
        model.setWorkPlanCompleteNum((long) this.count(new LambdaQueryWrapper<WorkOrder>().eq(WorkOrder::getBusinessId, model.getPlanId())
                .eq(WorkOrder::getDeleted, Status.enabled.getKey())
                .eq(WorkOrder::getStatus, WorkOrderStatusEnum.COMPLETED.getCode())));
        workOrderModel.setWorkPlanDetailModel(model);
    }

    private void handleWorkPermission(WorkOrderModel model){
        String currentUserId = userApiService.getCurrentStaffNo();
        model.setNowUserId(currentUserId);

        // 处理权限 - 当前人是否是处理人
        if (ObjectUtil.isNotEmpty(model.getProcessedPersonId()) && model.getProcessedPersonId().equals(currentUserId)){
            model.setFlag(Boolean.TRUE);
            if(WorkOrderStatusEnum.PROCESSING.getCode().equals(model.getStatus())){
                model.setHandleFlag(Boolean.TRUE);
            }
        }
        // 分配权限 - 当前人是否是分配人
        if (ObjectUtil.isNotEmpty(model.getAllotUid()) && model.getAllotUid().equals(currentUserId) && WorkOrderStatusEnum.ALLOT.getCode().equals(model.getStatus())) {
            model.setAllotFlag(Boolean.TRUE);
        }

        // 抢单权限 - 当前人未分配且工单状态为待分配
        if (ObjectUtil.isEmpty(model.getAllotUid()) && WorkOrderStatusEnum.ALLOT.getCode().equals(model.getStatus())) {
            model.setGrabFlag(Boolean.TRUE);
        }

        // 接受权限 - 当前人是处理人且工单状态为待处理
        if (ObjectUtil.isNotEmpty(model.getProcessedPersonId()) && model.getProcessedPersonId().equals(currentUserId) && WorkOrderStatusEnum.REPORTED.getCode().equals(model.getStatus())) {
            model.setAcceptFlag(Boolean.TRUE);
        }

        // 审核权限 - 当前人是审核人且工单状态为待审核
        if (ObjectUtil.isNotEmpty(model.getAuditUid()) && model.getAuditUid().equals(currentUserId) && WorkOrderStatusEnum.AUDIT.getCode().equals(model.getStatus())) {
            model.setAuditFlag(Boolean.TRUE);
        }

        // 转派处理权限 - 当前人是处理人且工单状态为处理中
        if (ObjectUtil.isNotEmpty(model.getProcessedPersonId()) && model.getProcessedPersonId().equals(currentUserId)
                && Arrays.asList(WorkOrderStatusEnum.PROCESSING.getCode(), WorkOrderStatusEnum.REPORTED.getCode()).contains(model.getStatus())) {
            model.setTransferHandleFlag(Boolean.TRUE);
        }

        // 转派审核权限 - 当前人是审核人且工单状态为待审核
        if (ObjectUtil.isNotEmpty(model.getAuditUid()) && model.getAuditUid().equals(currentUserId) && WorkOrderStatusEnum.AUDIT.getCode().equals(model.getStatus())) {
            model.setTransferAuditFlag(Boolean.TRUE);
        }
    }

    private List<String> getRoleUserId(){
        ConfigInfoModel configInfo = configInfoService.getByCodeDetail(Constant.GENERAL_MANAGEMENT_ROLE_CONFIG);
        return ObjectUtil.isNotEmpty(configInfo) ? roleApiService.findStaffNoByRoleCode(configInfo.getValue()) : Collections.emptyList();
    }

    /***
     * @Description 关注人消息
     * @author huangyongtao
     * @date 2025/3/31 16:02
     * @param evaluate
     */
    private void sendAttentionMessage(WorkEvaluateParam evaluate, UserInfoModel userInfo) {
        if(!attentionManageService.checkAttention(userInfo.getId())){
            return;
        }
        Map<String, String> variables = new HashMap<>(4);
        variables.put("type", "工单");
        variables.put("attentionName", userInfo.getUserName());
        variables.put("content", evaluate.getContent());
        variables.put("score", evaluate.getScore() + "分");
        messageCommonService.sendMessage(MessageConstant.ATTENTION_EVALUATE_NOTICE, WebFrameworkUtils.getHeaderTenantId(), evaluate.getId(), new HashSet<>(), variables);
    }

}
