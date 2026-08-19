package com.cgnpc.bbxpark.problemReport.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cgnpc.bbxpark.acl.uic.service.IUserApiService;
import com.cgnpc.bbxpark.common.constant.MessageConstant;
import com.cgnpc.bbxpark.common.constant.WorkOrderCodePrefixConstant;
import com.cgnpc.bbxpark.common.enums.*;
import com.cgnpc.bbxpark.common.service.IMessageCommonService;
import com.cgnpc.bbxpark.common.utils.*;
import com.cgnpc.bbxpark.meeting.dto.model.ImportReturnModel;
import com.cgnpc.bbxpark.problemReport.domain.ProblemDevice;
import com.cgnpc.bbxpark.problemReport.domain.ProblemHandleRecord;
import com.cgnpc.bbxpark.problemReport.domain.ProblemReport;
import com.cgnpc.bbxpark.problemReport.mapper.ProblemDeviceRepository;
import com.cgnpc.bbxpark.problemReport.mapper.ProblemHandleRecordRepository;
import com.cgnpc.bbxpark.problemReport.mapper.ProblemReportRepository;
import com.cgnpc.bbxpark.problemReport.model.ProblemDeviceModel;
import com.cgnpc.bbxpark.problemReport.model.ProblemHandleRecordModel;
import com.cgnpc.bbxpark.problemReport.model.ProblemReportModel;
import com.cgnpc.bbxpark.problemReport.param.ProblemReportImportParam;
import com.cgnpc.bbxpark.problemReport.param.ProblemReportParam;
import com.cgnpc.bbxpark.problemReport.param.ProblemReportQueryParam;
import com.cgnpc.bbxpark.problemReport.param.ProblemValidationParam;
import com.cgnpc.bbxpark.problemReport.service.IProblemReportService;
import com.cgnpc.bbxpark.property.dto.model.PropertyScheduleUserModel;
import com.cgnpc.bbxpark.property.service.IPropertyScheduleService;
import com.cgnpc.bbxpark.restaurant.domain.RestaurantCardRecord;
import com.cgnpc.bbxpark.restaurant.dto.param.RestaurantCardRecordParam;
import com.cgnpc.bbxpark.restaurant.service.impl.RestaurantCardRecordListener;
import com.cgnpc.bbxpark.settings.domain.File;
import com.cgnpc.bbxpark.settings.domain.RegionManage;
import com.cgnpc.bbxpark.settings.dto.model.FileModel;
import com.cgnpc.bbxpark.settings.service.IAttentionManageService;
import com.cgnpc.bbxpark.settings.service.IFileService;
import com.cgnpc.bbxpark.settings.service.IRegionManageService;
import com.cgnpc.bbxpark.settings.service.IRegionSpaceRelationService;
import com.cgnpc.bbxpark.space.dto.model.ParkSpaceFullModel;
import com.cgnpc.bbxpark.space.dto.model.UserInfoModel;
import com.cgnpc.bbxpark.space.service.IParkSpaceService;
import com.cgnpc.bbxpark.workorder.domain.WorkOrder;
import com.cgnpc.bbxpark.workorder.domain.WorkOrderDevice;
import com.cgnpc.bbxpark.workorder.domain.WorkOrderRoman;
import com.cgnpc.bbxpark.workorder.dto.model.WorkOrderRomanModel;
import com.cgnpc.bbxpark.workorder.dto.param.WorkScheduleUserParam;
import com.cgnpc.bbxpark.workorder.mapper.WorkOrderRepository;
import com.cgnpc.bbxpark.workorder.mapper.WorkOrderRomanRepository;
import com.cgnpc.bbxpark.workorder.service.IWorkOrderDeviceService;
import com.cgnpc.bbxpark.workorder.service.IWorkScheduleUserService;
import com.cgnpc.cud.core.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.concurrent.Executor;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @create zhaoshuo
 * @time 2025/3/21
 * @desc 报事报修服务实现类
 */
@Service
@Slf4j
public class ProblemReportServiceImpl extends ServiceImpl<ProblemReportRepository, ProblemReport> implements IProblemReportService {

    @Autowired
    private ProblemReportRepository problemReportRepository;

    @Autowired
    private IParkSpaceService parkSpaceService;

    @Autowired
    private ProblemDeviceRepository problemDeviceRepository;

    @Autowired
    private ProblemHandleRecordRepository problemHandleRecordRepository;

    @Autowired
    private IUserApiService userApiService;

    @Autowired
    private WorkOrderRepository workOrderRepository;

    @Autowired
    private IFileService iFileService;

    @Autowired
    private WorkOrderRomanRepository workOrderRomanRepository;

    @Autowired
    private IWorkOrderDeviceService workOrderDeviceService;

    @Autowired
    private IMessageCommonService messageCommonService;

    @Autowired
    IAttentionManageService attentionManageService;

    @Autowired
    private IRegionSpaceRelationService regionSpaceRelationService;

    @Autowired
    private IRegionManageService regionManageService;

    @Autowired
    private IPropertyScheduleService propertyScheduleService;

    @Autowired
    private IWorkScheduleUserService workScheduleUserService;

    @Autowired
    @Qualifier("asyncEventBusExecutor")
    private Executor busExecutorService;


    /**
     * 保存问题报告及其关联设备信息
     *
     * @param param 问题报告参数对象，包含问题报告的基本信息及关联的设备列表
     * @return 保存成功返回true，否则返回false
     */
    @Override
    @Transactional
    public ProblemReportModel save(ProblemReportParam param) {
        try {
            // 将参数对象转换为问题报告实体对象
            ProblemReport problemReport = BeanUtils.convertTo(param, ProblemReport::new);
            Long tenantId = WebFrameworkUtils.getHeaderTenantId();
            String userId = WebFrameworkUtils.getHeaderUserId();
            UserInfoModel userInfo = userApiService.detail(userId);
            // 查询并获取空间详细信息
            Map<Long, ParkSpaceFullModel> fullSpaceMap = parkSpaceService.findFullSpaceMap(Collections.singletonList(problemReport.getSpaceId()), problemReport.getTenantId());
            ParkSpaceFullModel parkSpaceFullModel = fullSpaceMap.get(problemReport.getSpaceId());

            // 设置问题报告的空间名称和初始状态
            problemReport.setSpaceName(parkSpaceFullModel.getFullPath());
            problemReport.setStatus(ProblemStatusEnum.NEW.getCode());
            problemReport.setTenantId(tenantId);
            problemReport.setCreateBy(userInfo.getUserName());
            problemReport.setCreatorId(userInfo.getStaffid());

            // 插入问题报告记录，如果插入失败则返回false
            int insert = problemReportRepository.insert(problemReport);
            if (insert == 0) {
                log.error("报事报修新增失败");
                throw new BaseException("报事报修新增失败,请联系管理员");
            }

            List<String> deviceNames = new ArrayList<>();
            // 处理关联的设备问题列表，如果列表不为空则进行保存
            if (CollUtil.isNotEmpty(param.getProblemDeviceList())) {
                List<ProblemDevice> problemDeviceList = param.getProblemDeviceList();

                // 为每个设备问题设置基础信息，并进行批量插入
                problemDeviceList.stream().forEach(problemDevice -> {
                    problemDevice.setProblemId(problemReport.getId());
                    problemDevice.setTenantId(tenantId);
                    problemDevice.setDeleted(Status.enabled.getKey());
                    problemDevice.setCreatorId(userId);
                    problemDevice.setCreateBy(userInfo.getUserName());
                    problemDevice.setCreateTime(new Date());
                    deviceNames.add(problemDevice.getDeviceName() + "（设备位置：" + problemDevice.getSpaceName() + "）");
                });
                problemDeviceRepository.insertBatch(problemDeviceList);
            }
            // 文件上传
            if (CollUtil.isNotEmpty(param.getFileList())) {
                uploadFiles(param.getFileList(), problemReport.getId());
            }
            // 插入问题处理记录
            ProblemHandleRecord problemHandleRecord = new ProblemHandleRecord();
            problemHandleRecord.setProblemId(problemReport.getId());
            problemHandleRecord.setProblemId(problemReport.getId());
            problemHandleRecord.setDeleted(Status.enabled.getKey());
            problemHandleRecord.setOperateTime(new Date());
            problemHandleRecord.setOperator(userInfo.getUserName());
            problemHandleRecord.setOperatorStaffid(userInfo.getStaffid());
            problemHandleRecord.setLink(ProblemStatusEnum.NEW.getCode());
            problemHandleRecord.setTenantId(tenantId);
            problemHandleRecord.setCreatorId(userId);
            problemHandleRecord.setCreateBy(userInfo.getUserName());
            problemHandleRecord.setCreateTime(new Date());

            problemHandleRecordRepository.insert(problemHandleRecord);

            log.info("问题报告保存成功，id: {}", problemReport.getId());

            //关注人消息
            sendAttentionMessage(problemReport, userInfo, deviceNames);
            // 返回保存成功
            return BeanUtils.convertTo(problemReport, ProblemReportModel::new);
        } catch (Exception e) {
            log.error("报事报修新增失败", e);
            throw new BaseException("报事报修新增失败");
        }
    }

    /**
     * 批量上传文件
     *
     * @param fileList  文件列表
     * @param relatedId 关联的问题报告ID
     */
    private void uploadFiles(List<File> fileList, Long relatedId) {
        fileList.forEach(file -> {
            file.setType(FileTypeEnum.PROBLEMREPORT.getValue());
            file.setTenantId(WebFrameworkUtils.getHeaderTenantId());
            file.setCreatorId(WebFrameworkUtils.getHeaderUserId());
            file.setCreateTime(new Date());
            file.setRelatedId(relatedId);
        });

        iFileService.addBatch(fileList);
    }

    /**
     * 处理报事报修
     *
     * @param param 处理报事报修参数对象
     * @return 验证成功返回true，否则返回false
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean validationProblem(ProblemValidationParam param) {
        try {
            // 参数校验
            AssertUtils.notNull(param.getProblemId(), "报事报修id不能为空");
            AssertUtils.notNull(param.getProblemResultType(), "问题确认结果不能为空");

            // 获取当前用户信息
            String userId = WebFrameworkUtils.getHeaderUserId();
            Long tenantId = WebFrameworkUtils.getHeaderTenantId();

            UserInfoModel userInfo = userApiService.detail(userId);
            if (userInfo == null) {
                throw new BaseException("用户信息获取失败");
            }

            // 获取问题报告
            ProblemReport problemReport = problemReportRepository.selectById(param.getProblemId());
            AssertUtils.notNull(problemReport, "问题报告不存在");
            AssertUtils.isNotEquals(problemReport.getStatus(), ProblemStatusEnum.CONFIRMED.getCode(), "该问题已处理，请勿重复处理");

            // 根据问题结果类型处理逻辑
            if (param.getProblemResultType().equals(ProblemResultTypeEnum.TURN_TO_WORK_ORDER.getCode())) {
                 handleTurnToWorkOrder(param, problemReport, userInfo, tenantId, userId);
            } else if (param.getProblemResultType().equals(ProblemResultTypeEnum.NO_ACTION_REQUIRED.getCode())) {
                handleNoActionRequired(param, problemReport, userInfo, tenantId);
            } else {
                throw new BaseException("未知的问题结果类型");
            }

            // 更新问题报告状态
            updateProblemReportStatus(param, problemReport, ProblemStatusEnum.CONFIRMED.getCode());

            return true;
        } catch (Exception e) {
            log.error("处理报事报修失败", e);
            throw e; // 确保事务回滚
        }
    }



    private void handleTurnToWorkOrder(ProblemValidationParam param, ProblemReport problemReport, UserInfoModel userInfo, Long tenantId, String userId) {
        Integer number = workOrderRepository.selectCount(new LambdaQueryWrapper<WorkOrder>().eq(WorkOrder::getSource, WorkOrderSourceEnum.PERSON.getCode()).eq(ObjectUtil.isNotEmpty(tenantId), WorkOrder::getTenantId, tenantId));
        AssertUtils.notNull(param.getScheduleId(), "物业分组不能为空");
        AssertUtils.notNull(param.getDispatchType(), "派单方式不能为空");
        List<PropertyScheduleUserModel> scheduleUserModels = propertyScheduleService.findUserList(param.getScheduleId());
        //manager:是否是管理员，保持0：是；1否（前端要改的地方过多，保持不动）
        PropertyScheduleUserModel leader = scheduleUserModels.stream().filter(scheduleUserModel -> scheduleUserModel.getManager().equals((0))).findFirst().orElse(null);
        // 生成工单数据
        WorkOrder workOrder = new WorkOrder();
        workOrder.setName((Status.enabled.getKey().equals(param.getIsUrgency())? "【紧急】" : "") + problemReport.getProblemDesc() + "("+ cn.hutool.core.date.DateUtil.format(new Date(), "MMdd") + ")");
        workOrder.setCode(PlanDateUtil.getCode(WorkOrderCodePrefixConstant.BX,  Long.valueOf(number)));
        workOrder.setType(WorkOrderTypeEnum.REPAIR.getCode());
        workOrder.setSource(WorkOrderSourceEnum.PERSON.getCode());
        if(DispatchTypeEnum.ASSIGN.getCode().equals(param.getDispatchType())){
            workOrder.setProcessedPersonName(param.getHandleUname());
            workOrder.setProcessedPersonId(param.getHandleUid());
            workOrder.setProcessedPersonStaffid(param.getHandleStaffid());
            workOrder.setStatus(WorkOrderStatusEnum.REPORTED.getCode());
            UserInfoModel userPeron = userApiService.getSecondDeptByStaffNo(param.getHandleStaffid());
            workOrder.setDepartmentId(userPeron.getDepartmentSecondId());
            workOrder.setDepartmentName(userPeron.getDepartmentSecondName());
        }else if(DispatchTypeEnum.GROUPING.getCode().equals(param.getDispatchType())){
            workOrder.setStatus(WorkOrderStatusEnum.ALLOT.getCode());
        }else if(DispatchTypeEnum.LEADER.getCode().equals(param.getDispatchType()) && ObjectUtil.isNotEmpty(leader)){
            workOrder.setAllotUname(leader.getUserName());
            workOrder.setAllotUid(leader.getUserId());
            workOrder.setAllotUstaffid(leader.getStaffid());
            workOrder.setStatus(WorkOrderStatusEnum.ALLOT.getCode());
        }
        workOrder.setTenantId(tenantId);
        workOrder.setCreateTime(new Date());
        workOrder.setCreatorId(userInfo.getId());
        workOrder.setCreateBy(userInfo.getUserName());
        workOrder.setSpaceName(problemReport.getSpaceName());
        workOrder.setSpaceId(String.valueOf(problemReport.getSpaceId()));
        workOrder.setBusinessId(param.getProblemId());
        workOrder.setDispatchType(param.getDispatchType());
        if(Status.enabled.getKey().equals(param.getAuditType())){
            workOrder.setAuditUid(param.getAuditUid());
            workOrder.setAuditUname(param.getAuditUname());
            workOrder.setAuditStaffid(param.getAuditStaffid());
        }
        // 插入工单数据到数据库
        workOrderRepository.insert(workOrder);
        problemReport.setWorkOrderId(workOrder.getId());
        // 批量处理工单设备
        List<ProblemDevice> problemDevices = problemDeviceRepository.selectList(new LambdaQueryWrapper<ProblemDevice>().eq(ProblemDevice::getProblemId, problemReport.getId()));
        if (CollectionUtils.isNotEmpty(problemDevices)) {
            List<WorkOrderDevice> workOrderDevices = problemDevices.stream()
                    .map(device -> {
                        WorkOrderDevice workOrderDevice = new WorkOrderDevice();
                        workOrderDevice.setWorkOrderId(workOrder.getId());
                        workOrderDevice.setDeviceId(device.getDeviceId());
                        workOrderDevice.setDeviceName(device.getDeviceName());
                        workOrderDevice.setSpaceId(device.getSpaceId());
                        workOrderDevice.setSpaceFullPath(device.getSpaceName());
                        workOrderDevice.setTenantId(tenantId);
                        workOrderDevice.setCreateTime(new Date());
                        return workOrderDevice;
                    })
                    .collect(Collectors.toList());
            workOrderDeviceService.saveBatch(workOrderDevices); // 批量插入
        }

        // 插入工单流程记录
        WorkOrderRoman workOrderRoman = new WorkOrderRoman();
        workOrderRoman.setWorkOrderId(workOrder.getId());
        workOrderRoman.setTenantId(tenantId);
        if(DispatchTypeEnum.ASSIGN.getCode().equals(param.getDispatchType())){
            workOrderRoman.setRomanStatus(WorkOrderStatusEnum.REPORTED.getCode());
            workOrderRoman.setOperatorId(param.getHandleUid());
            workOrderRoman.setOperatorName(param.getHandleUname());
            workOrderRoman.setOperatorStaffid(param.getHandleStaffid());
            workOrderRoman.setOperator("处理人");
            workOrderRoman.setOperatorValue(param.getHandleStaffid() + " " + param.getHandleUname());
        }else if(DispatchTypeEnum.GROUPING.getCode().equals(param.getDispatchType())){
            workOrderRoman.setRomanStatus(WorkOrderStatusEnum.ALLOT.getCode());
            workOrderRoman.setOperator("分配人");
        }else if(DispatchTypeEnum.LEADER.getCode().equals(param.getDispatchType()) && ObjectUtil.isNotEmpty(leader)){
            workOrderRoman.setRomanStatus(WorkOrderStatusEnum.ALLOT.getCode());
            workOrderRoman.setOperatorId(leader.getUserId());
            workOrderRoman.setOperatorName(leader.getUserName());
            workOrderRoman.setOperatorStaffid(leader.getStaffid());
            workOrderRoman.setOperator("分配人");
            workOrderRoman.setOperatorValue(leader.getUserName());
        }
        workOrderRoman.setCreateTime(new Date());
        workOrderRomanRepository.insert(workOrderRoman);

        Map<Long, String> scheduleNameMap = propertyScheduleService.getScheduleNameMap(Collections.singletonList(param.getScheduleId()));
        List<WorkScheduleUserParam> workScheduleUsers = scheduleUserModels.stream().map(scheduleUser -> {
            WorkScheduleUserParam workScheduleUser = new WorkScheduleUserParam();
            BeanUtils.copyProperties(scheduleUser, workScheduleUser);
            workScheduleUser.setId(null);
            workScheduleUser.setWorkId(workOrder.getId());
            workScheduleUser.setScheduleId(param.getScheduleId());
            workScheduleUser.setScheduleName(scheduleNameMap.get(param.getScheduleId()));
            workScheduleUser.setTenantId(tenantId);
            return workScheduleUser;
        }).collect(Collectors.toList());
        //工单分组人员
        if(CollectionUtils.isNotEmpty(workScheduleUsers)){
            workScheduleUserService.addBatch(workScheduleUsers);
        }
        // 插入问题处理记录
        ProblemHandleRecord problemHandleRecord = new ProblemHandleRecord();
        problemHandleRecord.setProblemId(problemReport.getId());
        problemHandleRecord.setDeleted(0);
        problemHandleRecord.setCause(param.getCause());
        problemHandleRecord.setDescr(param.getDescr());
        problemHandleRecord.setOperateTime(new Date());
        problemHandleRecord.setOperator(userInfo.getUserName());
        problemHandleRecord.setOperatorStaffid(userInfo.getStaffid());
        problemHandleRecord.setLink(ProblemStatusEnum.CONFIRMED.getCode());
        problemHandleRecord.setResultType(ProblemResultTypeEnum.TURN_TO_WORK_ORDER.getCode());
        problemHandleRecord.setTenantId(tenantId);
        problemHandleRecord.setCreatorId(userId);
        problemHandleRecord.setCreateBy(userInfo.getUserName());
        problemHandleRecord.setCreateTime(new Date());
        problemHandleRecord.setHandleUmobile(param.getHandleUmobile());
        problemHandleRecord.setHandleUid(param.getHandleUid());
        problemHandleRecord.setHandleUname(param.getHandleUname());
        problemHandleRecord.setHandleStaffid(param.getHandleStaffid());

        problemHandleRecordRepository.insert(problemHandleRecord);

        //转工单通知报修人
        Map<String, String> variables1 = new HashMap<>();
        variables1.put("code",workOrder.getCode());
        messageCommonService.sendMessage(MessageConstant.TRANSFER_WORK_PROBLEM, tenantId, workOrder.getId(), problemReport.getCreatorId(), variables1);

        //紧急报事报修通知处理人
        if (Status.enabled.getKey().equals(param.getIsUrgency()) && ObjectUtil.isNotEmpty(workOrder.getProcessedPersonId())){
            Map<String, String> variables = new HashMap<>();
            variables.put("name",workOrder.getName());
            variables.put("code",workOrder.getCode());
            messageCommonService.sendMessage(MessageConstant.URGENCY_PROBLEM, tenantId, workOrder.getId(), workOrder.getProcessedPersonId(), variables);
        }
    }

    private void handleNoActionRequired(ProblemValidationParam param, ProblemReport problemReport, UserInfoModel userInfo, Long tenantId) {
        // 更新问题报告状态
        updateProblemReportStatus(param, problemReport, ProblemStatusEnum.CONFIRMED.getCode());

        // 插入问题处理记录
        ProblemHandleRecord problemHandleRecord = new ProblemHandleRecord();
        problemHandleRecord.setProblemId(problemReport.getId());
        problemHandleRecord.setDeleted(0);
        problemHandleRecord.setCause(param.getCause());
        problemHandleRecord.setDescr(param.getDescr());
        problemHandleRecord.setOperateTime(new Date());
        problemHandleRecord.setOperator(userInfo.getUserName());
        problemHandleRecord.setOperatorStaffid(userInfo.getStaffid());
        problemHandleRecord.setLink(ProblemStatusEnum.CONFIRMED.getCode());
        problemHandleRecord.setResultType(ProblemResultTypeEnum.NO_ACTION_REQUIRED.getCode());
        problemHandleRecord.setTenantId(tenantId);
        problemHandleRecord.setCreatorId(userInfo.getStaffid());
        problemHandleRecord.setCreateBy(userInfo.getUserName());
        problemHandleRecord.setCreateTime(new Date());

        problemHandleRecordRepository.insert(problemHandleRecord);

        //紧急报事报修通知处理人
        Map<String, String> variables = new HashMap<>();
        variables.put("name",problemReport.getProblemDesc());
        variables.put("cause", ProblemNoActionEnum.fromCode(param.getCause()));
        messageCommonService.sendMessage(MessageConstant.PROBLEM_HANDLE, tenantId, problemReport.getId(), problemReport.getCreatorId(), variables);
    }

    private void updateProblemReportStatus(ProblemValidationParam param, ProblemReport problemReport, Integer status) {
        problemReport.setId(param.getProblemId());
        problemReport.setStatus(status);
        problemReport.setProblemResultType(param.getProblemResultType());
        problemReportRepository.updateById(problemReport);
    }

    /**
     * 根据查询参数分页获取问题报修列表
     *
     * @param param 问题报告查询参数对象，包含分页信息和查询条件
     * @return 返回一个PaginationResult对象，包含分页数据和转换后的结果列表
     */
    @Override
    public IPage<ProblemReportModel> page(ProblemReportQueryParam param) {
        boolean builtRole = userApiService.parkAdmin();
        //获取当前登陆人的空间权限id集合
        List<Long> spaceIds = regionSpaceRelationService.queryAllSpaceIdByLoginUser();
        // 创建分页对象，设置当前页和每页大小
        IPage<ProblemReport> page = new Page<>(param.getCurrent(), param.getSize());
        if (CollUtil.isEmpty(spaceIds) && !builtRole) {
            return new Page<>();
        }

        // 创建Lambda查询包装器，用于后续的条件构造
        LambdaQueryWrapper<ProblemReport> queryWrapper = new LambdaQueryWrapper<>();

        // 根据空间ID进行in查询，如果空间ID集合不为空
        if (!builtRole){
            queryWrapper.in(ProblemReport::getSpaceId, spaceIds);
        }

        // 根据问题描述进行模糊查询，如果问题描述不为空
        queryWrapper.like(StringUtils.isNotEmpty(param.getProblemDesc()), ProblemReport::getProblemDesc, param.getProblemDesc());

        // 根据问题类型进行精确查询，如果问题类型不为空
        queryWrapper.eq(param.getProblemType() != null, ProblemReport::getProblemType, param.getProblemType());

        // 根据状态进行精确查询，如果状态不为空
        queryWrapper.eq(param.getStatus() != null, ProblemReport::getStatus, param.getStatus());

        // 根据空间ID进行精确查询，如果空间ID不为空
        queryWrapper.eq(param.getSpaceId() != null, ProblemReport::getSpaceId, param.getSpaceId());

        // 根据问题结果类型进行精确查询，如果问题结果类型不为空
        queryWrapper.eq(param.getProblemResultType() != null, ProblemReport::getProblemResultType, param.getProblemResultType());

        queryWrapper.eq(WebFrameworkUtils.getHeaderTenantId() != null, ProblemReport::getTenantId,WebFrameworkUtils.getHeaderTenantId());

        // 如果报告开始时间和结束时间都提供，则根据创建时间进行区间查询
        if (param.getReportStartTime() != null && param.getReportEndTime() != null) {
            queryWrapper.between(ProblemReport::getCreateTime, param.getReportStartTime(), param.getReportEndTime());
        }
        queryWrapper.orderByDesc(ProblemReport::getCreateTime);
        // 执行分页查询
        page = problemReportRepository.selectPage(page, queryWrapper);

        // 创建分页结果对象
        IPage<ProblemReportModel> result = new Page<>();
        // 设置当前页
        result.setCurrent((int) page.getCurrent());
        // 设置总记录数
        result.setTotal((int) page.getTotal());
        // 设置每页大小
        result.setSize((int) page.getSize());
        // 将查询结果转换为IocProductModel列表并设置到结果对象
        result.setRecords(BeanUtils.convertListTo(page.getRecords(), ProblemReportModel::new));

        result.getRecords().forEach(item -> {
            if (spaceIds.contains(item.getSpaceId())){
                item.setFlag(Boolean.TRUE);
            }else{
                item.setFlag(Boolean.FALSE);
            }
        });
        // 返回分页结果
        return result;
    }

    /**
     * 根据查询参数获取问题报告详情
     *
     * @param param 问题报告查询参数对象，包含查询条件
     * @return 返回一个ProblemReportModel对象，包含问题报告详情
     */
    @Override
    public ProblemReportModel detail(ProblemReportQueryParam param) {
        Long problemReportId = param.getId();
        AssertUtils.notNull(problemReportId, "报事报修ID不能为空");
        Long tenantId = WebFrameworkUtils.getHeaderTenantId();
        String userId = WebFrameworkUtils.getHeaderUserId();
        // 查询报事报修主表信息
        ProblemReport problemReport = problemReportRepository.selectById(problemReportId);
        AssertUtils.notNull(problemReport, "未找到对应的报事报修记录");
        UserInfoModel userInfo = userApiService.detail(WebFrameworkUtils.getHeaderUserId());


        // 查询关联的设备信息
        List<ProblemDevice> problemDevices = problemDeviceRepository.selectList(new QueryWrapper<ProblemDevice>().eq("problem_id", problemReportId));

        // 查询关联的流程信息
        List<ProblemHandleRecord> problemHandleRecords = problemHandleRecordRepository.selectList(new QueryWrapper<ProblemHandleRecord>().eq("problem_id", problemReportId));

        //查询文件信息
        List<FileModel> problemFiles = iFileService.findByTypeAndRelatedId(FileTypeEnum.PROBLEMREPORT.getValue(), problemReportId);

        // 构建返回的模型
        ProblemReportModel problemReportModel = new ProblemReportModel();
        BeanUtils.copyProperties(problemReport, problemReportModel);
        problemReportModel.setProblemDeviceList(BeanUtils.convertListTo(problemDevices, ProblemDeviceModel::new));
        problemReportModel.setProblemHandleRecordList(BeanUtils.convertListTo(problemHandleRecords, ProblemHandleRecordModel::new));
        problemReportModel.setFileList(problemFiles);
        //判断当前登录人是否是区域管理员
        int count = regionManageService.count(new LambdaQueryWrapper<RegionManage>().eq(RegionManage::getRegionStaffid, userId).eq(RegionManage::getTenantId, tenantId));
        //记录已查看流转状态
        if (count > 0){
            if (problemReport.getStatus().equals(ProblemStatusEnum.NEW.getCode())){
                // 插入问题处理记录
                ProblemHandleRecord problemHandleRecord = new ProblemHandleRecord();
                problemHandleRecord.setProblemId(problemReportId);
                problemHandleRecord.setDeleted(Status.enabled.getKey());
                problemHandleRecord.setOperateTime(new Date());
                problemHandleRecord.setOperator(userInfo.getUserName());
                problemHandleRecord.setOperatorStaffid(userInfo.getStaffid());
                problemHandleRecord.setLink(ProblemStatusEnum.VIEWED.getCode());
                problemHandleRecord.setTenantId(tenantId);
                problemHandleRecord.setCreatorId(userId);
                problemHandleRecord.setCreateBy(userInfo.getUserName());
                problemHandleRecord.setCreateTime(new Date());

                problemHandleRecordRepository.insert(problemHandleRecord);
                //修改报事报修状态为已查看
                problemReport.setStatus(ProblemStatusEnum.VIEWED.getCode());
                problemReportRepository.updateById(problemReport);
            }else if (problemReport.getStatus().equals(ProblemStatusEnum.VIEWED.getCode())){
                ProblemHandleRecord viewedRecord = problemHandleRecordRepository.selectOne(new QueryWrapper<ProblemHandleRecord>().eq("problem_id", problemReportId).eq("link", ProblemStatusEnum.VIEWED.getCode()));
                // 插入查看人
                String operatorStaffid = viewedRecord.getOperatorStaffid();
                if (!operatorStaffid.contains(userInfo.getStaffid())){
                    viewedRecord.setOperator(viewedRecord.getOperator()+","+userInfo.getUserName());
                    viewedRecord.setOperatorStaffid(viewedRecord.getOperatorStaffid()+","+userInfo.getStaffid());
                    problemHandleRecordRepository.updateById(viewedRecord);
                }
            }
        }
        return problemReportModel;
    }

    /**
     * 评价
     *
     * @param param 评价参数对象，包含评价内容和评分
     * @return 如果评价成功，则返回true
     */
    @Override
    public Boolean review(ProblemValidationParam param) {
        // 验证评价内容不能为空
//        AssertUtils.isNotEmpty(param.getEvaluation(), "评价内容不能为空");
        // 验证评分不能为空
        AssertUtils.notNull(param.getScore(), "评分不能为空");
        // 根据问题ID查询问题报告
        ProblemReport problemReport = problemReportRepository.selectById(param.getProblemId());
        // 如果未查询到问题报告，则抛出异常
        if (problemReport == null) {
            throw new BaseException("未查询到报事报修信息");
        }
        // 如果问题报告已被评价，则抛出异常
        if (problemReport.getScore() != null)
            throw new BaseException("已评价");

        if (!problemReport.getCreatorId().equals(WebFrameworkUtils.getHeaderUserId()))
            throw new BaseException("仅支持上报人评价");
        // 更新问题报告的评价和评分信息
        problemReport.setEvaluation(param.getEvaluation());
        problemReport.setScore(param.getScore());
        // 获取当前登录用户信息
        UserInfoModel loginUser = userApiService.detail(WebFrameworkUtils.getHeaderUserId());
        // 更新问题报告的评价人信息和评价时间
        problemReport.setEvaluationUname(loginUser.getUserName());
        problemReport.setEvaluationUstaffid(loginUser.getStaffid());
        problemReport.setEvaluationTime(new Date());
        // 更新问题报告
        problemReportRepository.updateById(problemReport);
        sendAttentionMessage(problemReport, loginUser);
        // 返回评价成功
        return Boolean.TRUE;
    }

    /**
     * 查询当前登录人上报历史
     * @param param 查询参数
     * @return 报事报修数据
     */
    @Override
    public IPage<ProblemReportModel> reportHistory(ProblemReportQueryParam param) {
        IPage<ProblemReport> page = new Page<>(param.getCurrent(), param.getSize());
        LambdaQueryWrapper<ProblemReport> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(param.getProblemType() != null, ProblemReport::getProblemType, param.getProblemType());
        queryWrapper.eq(param.getStatus() != null, ProblemReport::getStatus, param.getStatus());
        queryWrapper.eq(ProblemReport::getCreatorId , WebFrameworkUtils.getHeaderUserId());
        if (param.getReportStartTime() != null && param.getReportEndTime() != null) {
            queryWrapper.between(ProblemReport::getCreateTime, param.getReportStartTime(), param.getReportEndTime());
        }
        queryWrapper.orderByDesc(ProblemReport::getCreateTime);
        page = problemReportRepository.selectPage(page, queryWrapper);
        List<ProblemReportModel> reportModelList = BeanUtils.convertListTo(page.getRecords(), ProblemReportModel::new);
        Page<ProblemReportModel> objectPage = new Page<>(page.getCurrent(),page.getSize(),page.getTotal());
        objectPage.setRecords(reportModelList);
        return objectPage;
    }

    @Override
    public List<WorkOrderRomanModel> workOrderProcess(ProblemValidationParam param) {
        if (param.getProblemId() == null)
            throw new BaseException("问题ID不能为空");
        LambdaQueryWrapper<WorkOrder> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(WorkOrder::getSource, WorkOrderSourceEnum.PERSON.getCode());
        queryWrapper.eq(WorkOrder::getBusinessId, param.getProblemId());
        WorkOrder workOrder = workOrderRepository.selectOne(queryWrapper);
        if (workOrder != null) {
            LambdaQueryWrapper<WorkOrderRoman> queryWrapper1 = new LambdaQueryWrapper<>();
            queryWrapper1.eq(WorkOrderRoman::getWorkOrderId, workOrder.getId());
            queryWrapper1.orderByAsc(WorkOrderRoman::getCreateTime);
            List<WorkOrderRoman> workOrderRomans = workOrderRomanRepository.selectList(queryWrapper1);
            List<WorkOrderRomanModel> workOrderRomanModels = BeanUtils.convertListTo(workOrderRomans, WorkOrderRomanModel::new);
            workOrderRomanModels.forEach(workOrderRomanModel -> {
                if (workOrderRomanModel.getRomanStatus() == WorkOrderStatusEnum.PROCESSING.getCode()) {
                    Double avgExpendTime  =workOrderRepository.getAvgExpendTimeByProcessedPersonId(workOrder.getProcessedPersonId(), WorkOrderStatusEnum.COMPLETED.getCode());
                    workOrderRomanModel.setOperatorValue("正在进行处理，预计耗时" + (avgExpendTime == null ? "--" : avgExpendTime) + "小时，有事呼叫处理人电话或小勤蜂联系");
                    workOrderRomanModel.setOperator(workOrderRomanModel.getOperatorStaffid() + " " + workOrderRomanModel.getOperatorName());
                    UserInfoModel result = userApiService.getByStaffNo(workOrder.getProcessedPersonId());
                    if (result != null){
                        workOrderRomanModel.setOperatorMobile(result.getMobile());
                    }
                }
                if (workOrderRomanModel.getRomanStatus() == WorkOrderStatusEnum.REPORTED.getCode()) {
                    workOrderRomanModel.setOperatorValue(workOrder.getProcessedPersonStaffid()+" "+workOrder.getProcessedPersonName());
                    workOrderRomanModel.setOperator("处理人");
                    workOrderRomanModel.setOperatorName(workOrder.getProcessedPersonName());
                    workOrderRomanModel.setOperatorStaffid(workOrder.getProcessedPersonStaffid());
                }
            });
            return workOrderRomanModels;
        }else {
            throw new BaseException("工单不存在");
        }
    }

    @Override
    public ProblemReportModel appDetail(ProblemReportQueryParam param) {
        Long problemReportId = param.getId();
        AssertUtils.notNull(problemReportId, "报事报修ID不能为空");
        // 查询报事报修主表信息
        ProblemReport problemReport = problemReportRepository.selectById(problemReportId);
        AssertUtils.notNull(problemReport, "未找到对应的报事报修记录");

        // 查询关联的设备信息
        List<ProblemDevice> problemDevices = problemDeviceRepository.selectList(new QueryWrapper<ProblemDevice>().eq("problem_id", problemReportId));

        // 查询关联的流程信息
        List<ProblemHandleRecord> problemHandleRecords = problemHandleRecordRepository.selectList(new QueryWrapper<ProblemHandleRecord>().eq("problem_id", problemReportId));

        //查询文件信息
        List<FileModel> problemFiles = iFileService.findByTypeAndRelatedId(FileTypeEnum.PROBLEMREPORT.getValue(), problemReportId);

        // 构建返回的模型
        ProblemReportModel problemReportModel = new ProblemReportModel();
        BeanUtils.copyProperties(problemReport, problemReportModel);
        problemReportModel.setProblemDeviceList(BeanUtils.convertListTo(problemDevices, ProblemDeviceModel::new));
        problemReportModel.setProblemHandleRecordList(BeanUtils.convertListTo(problemHandleRecords, ProblemHandleRecordModel::new));
        problemReportModel.setFileList(problemFiles);
        return problemReportModel;
    }

    @Override
    public Set<Long> getTodayDeviceIdList() {
        List<ProblemDevice> devices = problemDeviceRepository.selectList(Wrappers.<ProblemDevice>lambdaQuery()
                .eq(ObjectUtil.isNotEmpty(WebFrameworkUtils.getHeaderTenantId()), ProblemDevice::getTenantId, WebFrameworkUtils.getHeaderTenantId())
                .eq(ProblemDevice::getDeleted, Status.enabled.getKey())
                .ge(ProblemDevice::getCreateTime, DateUtil.beginOfDay(new Date()))
                .le(ProblemDevice::getCreateTime, DateUtil.endOfDay(new Date())));
        return CollectionUtil.isEmpty(devices) ? Collections.emptySet() : devices.stream().map(ProblemDevice::getDeviceId).collect(Collectors.toSet());
    }

    @Override
    public ImportReturnModel importFile(MultipartFile file) {
        List<String> returnModels = new ArrayList<>();
        boolean success = false;
        try {
            ProblemReportImportListener listener = new ProblemReportImportListener();
            ZipSecureFile.setMinInflateRatio(-1.0d);
            EasyExcel.read(file.getInputStream(), ProblemReportImportParam.class, listener).headRowNumber(0).autoTrim(true).sheet(0).doRead();
            List<ProblemReportImportParam> addList = listener.getAddList();
            Date time = new Date();
            //校验通过数据
            List<ProblemReport> list = addList.stream().map(p -> {
                ProblemReport report = new ProblemReport();
                List<String> names = parseNameStr(p.getCreateBy());
                report.setProblemDesc(p.getProblemDesc());
                report.setProblemResultType(1);
                report.setProblemType(1);
                report.setStatus(3);
                report.setEvaluation(p.getEvaluation());
                report.setTenantId(6L);
                report.setEvaluationTime(time);
                report.setEvaluationUname(names.get(1));
                report.setEvaluationUstaffid(names.get(0));
                report.setScore(ObjectUtil.isEmpty(p.getScoreStr()) ? 4 : (p.getScoreStr().equals("满意")? 5 : 3));
                report.setCreateTime(DateUtil.parse(p.getCreateTimeStr(), "yyyy-MM-dd HH:mm:ss"));
                report.setCreatorId(names.get(0));
                report.setCreateBy("["+names.get(0)+"]" + names.get(1));
                report.setUpdateTime(DateUtil.parse(p.getCreateTimeStr(), "yyyy-MM-dd HH:mm:ss"));
                report.setUpdatorId(names.get(0));
                report.setSpaceId("办公".equals(p.getSpaceName()) ? 1L : 2338L);
                report.setSpaceName("办公".equals(p.getSpaceName()) ? "bbx" : "科技园");
                return report;
            }).collect(Collectors.toList());
            busExecutorService.execute(() -> {
                this.saveBatch(list);
            });
            returnModels.add("导入成功"+list.size()+"条");
            success = true;
        } catch (Exception e) {
            returnModels.add("报事报修信息导入异常，原因是:"+e.getMessage());
            log.error("报事报修导入异常，原因是{}", e.getMessage());
        }
        return ImportReturnModel.builder().success(success).messageList(returnModels).build();
    }


    /**
     * 解析姓名和工号
     * @param input 输入字符串（如：陈明娟(P291420)）
     * @return 解析结果（name=姓名，staff=工号），格式错误返回null
     */
    public List<String> parseNameStr(String input) {
        List<String> list = new ArrayList<>();
        // 正则表达式：匹配 任意字符(非括号内容) 的格式
        final Pattern PATTERN = Pattern.compile("^(.*)\\(([^)]+)\\)$");
        // 1. 空值校验
        if (input == null || input.trim().isEmpty()) {
            return null;
        }
        String trimInput = input.trim();

        // 2. 正则匹配
        Matcher matcher = PATTERN.matcher(trimInput);
        if (matcher.matches()) {
            // 分组1：括号外的姓名，分组2：括号内的工号
            String name = matcher.group(1).trim();
            String staff = matcher.group(2).trim();
            // 过滤空姓名/空工号
            if (name.isEmpty() || staff.isEmpty()) {
                return list;
            }
            list.add(staff);
            list.add(name);
            return list;
        }
        // 格式不匹配（如无括号、括号为
        return list;
    }

    /***
     * @Description 关注人消息
     * @author huangyongtao
     * @date 2025/3/31 16:02
     * @param problemReport
     */
    private void sendAttentionMessage(ProblemReport problemReport, UserInfoModel userInfo, List<String> deviceNames) {
        if(!attentionManageService.checkAttention(userInfo.getStaffid())){
            return;
        }
        Map<String, String> variables = new HashMap<>(4);
        variables.put("attentionName", userInfo.getUserName());
        variables.put("content", problemReport.getProblemDesc());
        variables.put("type", "问题报修");
        variables.put("device", CollectionUtil.isNotEmpty(deviceNames) ? String.join("，", deviceNames) : "无");
        variables.put("spaceName", problemReport.getSpaceName());
        messageCommonService.sendMessage(MessageConstant.ATTENTION_REPORT_NOTICE, problemReport.getTenantId(), problemReport.getId(), new HashSet<>(), variables);
    }

    /***
     * @Description 关注人消息
     * @author huangyongtao
     * @date 2025/3/31 16:02
     * @param evaluate
     */
    private void sendAttentionMessage(ProblemReport evaluate, UserInfoModel userInfo) {
        if(!attentionManageService.checkAttention(userInfo.getId())){
            return;
        }
        Map<String, String> variables = new HashMap<>(4);
        variables.put("type", "报事报修");
        variables.put("attentionName", userInfo.getUserName());
        variables.put("content", evaluate.getEvaluation());
        variables.put("score", evaluate.getScore() + "分");
        messageCommonService.sendMessage(MessageConstant.ATTENTION_EVALUATE_NOTICE, evaluate.getTenantId(), evaluate.getId(), new HashSet<>(), variables);
    }
}
