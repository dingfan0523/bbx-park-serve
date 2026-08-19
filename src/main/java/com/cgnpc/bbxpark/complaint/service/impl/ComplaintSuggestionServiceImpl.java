
package com.cgnpc.bbxpark.complaint.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cgnpc.bbxpark.acl.uic.service.IUserApiService;
import com.cgnpc.bbxpark.settings.service.IAttentionManageService;
import com.cgnpc.bbxpark.common.constant.Constant;
import com.cgnpc.bbxpark.common.constant.MessageConstant;
import com.cgnpc.bbxpark.common.constant.SystemResultCode;
import com.cgnpc.bbxpark.common.enums.ComplaintSuggestionRomanTypeEnum;
import com.cgnpc.bbxpark.common.enums.ComplaintSuggestionStatusEnum;
import com.cgnpc.bbxpark.common.enums.ComplaintSuggestionTypeEnum;
import com.cgnpc.bbxpark.common.exception.GenericException;
import com.cgnpc.bbxpark.common.service.IMessageCommonService;
import com.cgnpc.bbxpark.common.utils.*;
import com.cgnpc.bbxpark.complaint.domain.ComplaintSuggestion;
import com.cgnpc.bbxpark.complaint.dto.model.ComplaintSuggestionModel;
import com.cgnpc.bbxpark.complaint.dto.model.ComplaintSuggestionRomanModel;
import com.cgnpc.bbxpark.complaint.dto.param.ComplaintSuggestionListParam;
import com.cgnpc.bbxpark.complaint.dto.param.ComplaintSuggestionPageParam;
import com.cgnpc.bbxpark.complaint.dto.param.ComplaintSuggestionParam;
import com.cgnpc.bbxpark.complaint.mapper.ComplaintSuggestionRepository;
import com.cgnpc.bbxpark.complaint.service.IComplaintSuggestionService;
import com.cgnpc.bbxpark.space.dto.model.UserInfoModel;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.IntStream;

/***
 * @Description 投诉建议主表;服务实现
 * @author huangyongtao
 * @date 2024/7/12 15:27
 */
@Slf4j
@Service("complaintSuggestionService")
public class ComplaintSuggestionServiceImpl extends ServiceImpl<ComplaintSuggestionRepository, ComplaintSuggestion> implements IComplaintSuggestionService {

    @Autowired
    private ComplaintSuggestionRomanServiceImpl complaintSuggestionRomanService;

    @Autowired
    private IUserApiService userApiService;

    @Value("${complaintSuggestion.url:}")
    private String complaintSuggestionUrl;

    @Autowired
    private IMessageCommonService messageCommonService;


    @Autowired
    IAttentionManageService attentionManageService;

    @Value("${hrcenter.department.canting:50707364}")
    private String cantingDepartmentId;

    /**
     * 查询投诉建议详情（包含流转信息）
     *
     * @Param [id] 投诉建议主表;标识
     * @Return 投诉建议主表;详情信息
     */
    @Override
    public ComplaintSuggestionModel detail(Long id) {
        ComplaintSuggestion complaintSuggestion = this.getById(id);
        AssertUtils.notNull(complaintSuggestion, SystemResultCode.RESULT_DATA_NONE.message());
        ComplaintSuggestionModel model = BeanUtils.convertTo(complaintSuggestion, ComplaintSuggestionModel::new);
        List<ComplaintSuggestionRomanModel> romanModels = complaintSuggestionRomanService.handleRomans(id);
        model.setRomanModels(romanModels);
        return model;
    }

    /**
     * 查询投诉建议详情
     *
     * @Param [id] 投诉建议主表;标识
     * @Return 投诉建议主表;详情信息
     */
    @Override
    public ComplaintSuggestionModel get(Long id) {
        ComplaintSuggestion complaintSuggestion = this.getById(id);
        AssertUtils.notNull(complaintSuggestion, SystemResultCode.RESULT_DATA_NONE.message());
        return BeanUtils.convertTo(complaintSuggestion, ComplaintSuggestionModel::new);
    }

    /**
     * 获取投诉建议主表;列表(分页).
     *
     * @Param param 投诉建议主表;查询条件
     * @Return 投诉建议主表;信息列表（分页）
     */
    @Override
    public IPage<ComplaintSuggestionModel> page(ComplaintSuggestionPageParam param) {
        IPage<ComplaintSuggestion> page = new Page<>(param.getCurrent(), param.getSize());
        ComplaintSuggestionListParam listParam = new ComplaintSuggestionListParam();
        BeanUtils.copyProperties(param, listParam);
        listParam.setTenantId(WebFrameworkUtils.getHeaderTenantId());
        IPage<ComplaintSuggestion> complaintSuggestionPage = this.page(page, handlePublicQuery(listParam));
        return ConvertUtil.pageConvert(complaintSuggestionPage,  BeanUtils.convertListTo(complaintSuggestionPage.getRecords(), ComplaintSuggestionModel::new));
    }

    /**
     * 获取投诉建议主表;列表.
     *
     * @Param param 投诉建议主表;查询条件
     * @Return 投诉建议主表;信息列表
     */
    @Override
    @SneakyThrows
    public List<ComplaintSuggestionModel> list(ComplaintSuggestionListParam param) {
        param.setTenantId(WebFrameworkUtils.getHeaderTenantId());
        List<ComplaintSuggestion> complaintSuggestions = this.list(handlePublicQuery(param));
        if (CollectionUtil.isEmpty(complaintSuggestions)) {
            return new ArrayList<>();
        }
        return BeanUtils.convertListTo(complaintSuggestions, ComplaintSuggestionModel::new);
    }

    /***
     * @Description 查询字段的处理
     * @author huangyongtao
     * @date 2024/7/17 11:31
     * @param param
     */
    private LambdaQueryWrapper<ComplaintSuggestion> handlePublicQuery(ComplaintSuggestionListParam param) {
        LambdaQueryWrapper<ComplaintSuggestion> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(ObjectUtil.isNotEmpty(param.getType()), ComplaintSuggestion::getType, param.getType())
                .eq(ObjectUtil.isNotEmpty(param.getStatus()), ComplaintSuggestion::getStatus, param.getStatus())
                .eq(ObjectUtil.isNotEmpty(param.getSubmitUid()), ComplaintSuggestion::getSubmitUid, param.getSubmitUid())
                .eq(ObjectUtil.isNotEmpty(param.getReplyUid()), ComplaintSuggestion::getReplyUid, param.getReplyUid())
                .in(ObjectUtil.isNotEmpty(param.getStatusList()), ComplaintSuggestion::getStatus, param.getStatusList())
                .like(ObjectUtil.isNotEmpty(param.getSubmitUname()), ComplaintSuggestion::getSubmitUname, param.getSubmitUname())
                .like(ObjectUtil.isNotEmpty(param.getTitle()), ComplaintSuggestion::getTitle, param.getTitle())
                .eq(ObjectUtil.isNotEmpty(param.getTenantId()), ComplaintSuggestion::getTenantId, param.getTenantId())
                .ge(ObjectUtil.isNotEmpty(param.getSubmitStartTime()), ComplaintSuggestion::getSubmitTime, param.getSubmitStartTime())
                .le(ObjectUtil.isNotEmpty(param.getSubmitEndTime()), ComplaintSuggestion::getSubmitTime, param.getSubmitEndTime())
                .orderByAsc(ComplaintSuggestion::getStatus)
                .orderByDesc(ComplaintSuggestion::getCreateTime);
        return queryWrapper;
    }

    /**
     * 新增投诉建议主表;.
     *
     * @Param param 投诉建议主表;信息
     * @Return 新增投诉建议主表;是否成功
     */
    @Override
    public Boolean add(ComplaintSuggestionParam param) {
        ComplaintSuggestion complaintSuggestion = BeanUtils.convertTo(param, ComplaintSuggestion::new);
        complaintSuggestion.setId(null);
        complaintSuggestion.setStatus(ComplaintSuggestionStatusEnum.REPLY.getCode());
        //处理人员信息
        handleUser(ComplaintSuggestionStatusEnum.REPLY, complaintSuggestion);
        this.save(complaintSuggestion);
        //发送关注消息
        sendAttentionMessage(complaintSuggestion);
        return true;
    }

    /**
     * 删除投诉建议主表;.
     *
     * @Param id 投诉建议主表;标识
     * @Return 删除投诉建议主表;是否成功
     */
    @Override
    public Boolean remove(Long id) {
        ComplaintSuggestion complaintSuggestion = this.getById(id);
        AssertUtils.notNull(complaintSuggestion, SystemResultCode.RESULT_DATA_NONE.message());
        return this.remove(id);
    }

    /**
     * 批量删除投诉建议主表;.
     *
     * @Param ids 投诉建议主表;标识列表
     * @Return 批量删除投诉建议主表;是否成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean removeBatch(List<Long> ids) {
        return this.removeBatch(ids);
    }

    /***
     * @Description 分配投诉建议
     * @author huangyongtao
     * @date 2024/7/16 9:35
     * @param param
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean assignment(ComplaintSuggestionParam param) {
        ComplaintSuggestion complaintSuggestionOld = this.getBaseMapper().selectForUpdate(param.getId());
        AssertUtils.notNull(complaintSuggestionOld, SystemResultCode.RESULT_DATA_NONE.message());
        ComplaintSuggestion complaintSuggestion = new ComplaintSuggestion();
        complaintSuggestion.setId(param.getId());
        complaintSuggestion.setReplyStaffid(param.getReplyStaffid());
        complaintSuggestion.setReplyUname(param.getReplyUname());
        complaintSuggestion.setReplyUid(param.getReplyUid());
        if(ObjectUtil.isNotEmpty(param.getReplyUid())){
            UserInfoModel replyUser = userApiService.getByStaffNo(param.getReplyUid());
            //二级部门id
            Optional<String[]> depart = findSecondDepart(replyUser.getDepartmentIdPath(),replyUser.getDepartmentNamePath());
            depart.ifPresent(d->{
                complaintSuggestion.setDepartmentId(d[0]);
                complaintSuggestion.setDepartmentName(d[1]);
            });
        }
        if (!ComplaintSuggestionStatusEnum.REPLY.getCode().equals(complaintSuggestionOld.getStatus())) {
            throw GenericException.fail(Constant.CHANGE_ERROR_MESSAGE);
        }
        complaintSuggestion.setStatus(ComplaintSuggestionStatusEnum.ASSIGNMENT.getCode());
        //处理人员信息
        handleUser(ComplaintSuggestionStatusEnum.ASSIGNMENT, complaintSuggestion);
        //发送邮件
        // messageCommonService.sendMessage();
        return this.updateById(complaintSuggestion);
    }

    private Optional<String[]> findSecondDepart(String departmentIdPath,String departmentNamePath){
        List<String> ids = Arrays.asList(departmentIdPath.split("\\\\"));
        List<String> names = Arrays.asList(departmentNamePath.split("\\\\"));
        return IntStream.range(0,ids.size() - 1).filter(i -> ids.get(i).equals(cantingDepartmentId))
                .mapToObj(i -> new String[]{ids.get(i),names.get(i)}).findFirst();
    }

    /***
     * @Description 审核投诉建议
     * @author huangyongtao
     * @date 2024/7/16 9:35
     * @param param
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean audit(ComplaintSuggestionParam param) {
        ComplaintSuggestion complaintSuggestionOld = this.getBaseMapper().selectForUpdate(param.getId());
        AssertUtils.notNull(complaintSuggestionOld, SystemResultCode.RESULT_DATA_NONE.message());
        ComplaintSuggestion complaintSuggestion = new ComplaintSuggestion();
        complaintSuggestion.setId(param.getId());
        complaintSuggestion.setStatus(param.getStatus());
        complaintSuggestion.setAuditRemark(param.getAuditRemark());
        if (!ComplaintSuggestionStatusEnum.AUDIT.getCode().equals(complaintSuggestionOld.getStatus())) {
            throw GenericException.fail(Constant.CHANGE_ERROR_MESSAGE);
        }
        //处理人员信息
        handleUser(ComplaintSuggestionStatusEnum.AUDIT_REJECT, complaintSuggestion);
        //保存流转信息
        complaintSuggestionRomanService.addRoman(complaintSuggestion, ComplaintSuggestionRomanTypeEnum.AUDIT.getCode(), param.getRomanId());
        //发送邮件
        if (ComplaintSuggestionStatusEnum.AUDIT_REJECT.getCode().equals(param.getStatus())) {
            complaintSuggestionOld.setAuditRemark(param.getAuditRemark());
            // messageCommonService.sendMessage();
        } else {
            //消息通知
            sendMessage(complaintSuggestionOld);
        }
        return this.updateById(complaintSuggestion);
    }

    /***
     * @Description 回复投诉建议
     * @author huangyongtao
     * @date 2024/7/16 9:35
     * @param param
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean reply(ComplaintSuggestionParam param) {
        ComplaintSuggestion complaintSuggestionOld = this.getBaseMapper().selectForUpdate(param.getId());
        AssertUtils.notNull(complaintSuggestionOld, SystemResultCode.RESULT_DATA_NONE.message());
        ComplaintSuggestion complaintSuggestion = new ComplaintSuggestion();
        complaintSuggestion.setId(param.getId());
        complaintSuggestion.setReplyRemark(param.getReplyRemark());
        //处理人员信息
        handleUser(ComplaintSuggestionStatusEnum.COMPLATE, complaintSuggestion);
        if (ComplaintSuggestionStatusEnum.REPLY.getCode().equals(complaintSuggestionOld.getStatus())) {
            complaintSuggestion.setStatus(ComplaintSuggestionStatusEnum.COMPLATE.getCode());
            complaintSuggestion.setCompleteTime(new Date());
            //消息通知
            sendMessage(complaintSuggestionOld);

        } else if (ComplaintSuggestionStatusEnum.ASSIGNMENT.getCode().equals(complaintSuggestionOld.getStatus()) || ComplaintSuggestionStatusEnum.AUDIT_REJECT.getCode().equals(complaintSuggestionOld.getStatus())) {
            if (!complaintSuggestionOld.getReplyUid().equals(WebFrameworkUtils.getHeaderUserId())) {
                throw GenericException.fail(Constant.CHANGE_ERROR_MESSAGE);
            }
            complaintSuggestion.setStatus(ComplaintSuggestionStatusEnum.AUDIT.getCode());
            //保存流转信息
            complaintSuggestionRomanService.addRoman(complaintSuggestion, ComplaintSuggestionRomanTypeEnum.REPLY.getCode(), Constant.SPACE_ROOT_ID);
        } else {
            throw GenericException.fail(Constant.CHANGE_ERROR_MESSAGE);
        }
        return this.updateById(complaintSuggestion);
    }

    /***
     * @Description 评价投诉建议
     * @author huangyongtao
     * @date 2024/7/16 15:35
     * @param param
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean comment(ComplaintSuggestionParam param) {
        ComplaintSuggestion complaintSuggestionOld = this.getBaseMapper().selectForUpdate(param.getId());
        AssertUtils.notNull(complaintSuggestionOld, SystemResultCode.RESULT_DATA_NONE.message());
        ComplaintSuggestion complaintSuggestion = new ComplaintSuggestion();
        complaintSuggestion.setId(param.getId());
        complaintSuggestion.setCommentRemark(param.getCommentRemark());
        complaintSuggestion.setCommentScore(param.getCommentScore());
        complaintSuggestion.setCommentTime(new Date());
        complaintSuggestion.setStatus(ComplaintSuggestionStatusEnum.COMMENT.getCode());
        if (!ComplaintSuggestionStatusEnum.COMPLATE.getCode().equals(complaintSuggestionOld.getStatus())) {
            throw GenericException.fail(Constant.CHANGE_ERROR_MESSAGE);
        }
        if (!complaintSuggestionOld.getSubmitUid().equals(WebFrameworkUtils.getHeaderUserId())) {
            throw GenericException.fail(SystemResultCode.PERMISSION_UNAUTHORISE.getMessage());
        }
        this.updateById(complaintSuggestion);
        sendAttentionMessage(complaintSuggestion, getUser());
        return true;
    }

    /***
     * @Description 获取当前登录用户信息
     * @author huangyongtao
     * @date 2024/7/17 11:30
     * @param
     */
    private UserInfoModel getUser() {
        return Objects.requireNonNull(userApiService.detail(WebFrameworkUtils.getHeaderUserId()));
    }

    /**
     * 发送消息通知
     *
     * @param complaintSuggestion 投诉建议信息
     */
    private void sendMessage(ComplaintSuggestion complaintSuggestion) {
        Map<String, String> variables = new HashMap<>(4);
        variables.put("submitTime", DateUtils.format(complaintSuggestion.getSubmitTime()));
        variables.put("title", complaintSuggestion.getTitle());
        messageCommonService.sendMessage(MessageConstant.ADVICE_REPLAY, WebFrameworkUtils.getHeaderTenantId(), complaintSuggestion.getId(), complaintSuggestion.getSubmitUid(), variables);
    }

    /***
     * @Description 关注人消息
     * @author huangyongtao
     * @date 2025/3/31 16:02
     * @param complaintSuggestion
     */
    private void sendAttentionMessage(ComplaintSuggestion complaintSuggestion) {
        if(!attentionManageService.checkAttention(complaintSuggestion.getSubmitUid())){
            return;
        }
        Map<String, String> variables = new HashMap<>(4);
        variables.put("attentionName", complaintSuggestion.getSubmitUname());
        variables.put("type", ComplaintSuggestionTypeEnum.getName(complaintSuggestion.getType()));
        variables.put("content", complaintSuggestion.getContent());
        variables.put("title", complaintSuggestion.getTitle());
        messageCommonService.sendMessage(MessageConstant.ATTENTION_COMPLAINT_NOTICE,
                WebFrameworkUtils.getHeaderTenantId(), complaintSuggestion.getId(), new HashSet<>(), variables);
    }

    /***
     * @Description 关注人消息
     * @author huangyongtao
     * @date 2025/3/31 16:02
     * @param complaintSuggestion
     */
    private void sendAttentionMessage(ComplaintSuggestion complaintSuggestion, UserInfoModel userInfo) {
        if(!attentionManageService.checkAttention(userInfo.getId())){
            return;
        }
        Map<String, String> variables = new HashMap<>(4);
        variables.put("type", "投诉建议");
        variables.put("attentionName", userInfo.getUserName());
        variables.put("content", complaintSuggestion.getCommentRemark());
        variables.put("score", complaintSuggestion.getCommentScore() + "分");
        messageCommonService.sendMessage(MessageConstant.ATTENTION_EVALUATE_NOTICE, WebFrameworkUtils.getHeaderTenantId(), complaintSuggestion.getId(), new HashSet<>(), variables);
    }


    /***
     * @Description 处理人员信息
     * @author huangyongtao
     * @date 2024/7/17 11:29
     * @param statusEnum
     * @param complaintSuggestion
     */
    private void handleUser(ComplaintSuggestionStatusEnum statusEnum, ComplaintSuggestion complaintSuggestion) {
        UserInfoModel user = getUser();
        switch (statusEnum) {
            case REPLY:
                complaintSuggestion.setSubmitStaffid(user.getStaffid());
                complaintSuggestion.setSubmitUname(user.getUserName());
                complaintSuggestion.setSubmitUid(user.getId());
                complaintSuggestion.setSubmitTime(new Date());
                break;
            case ASSIGNMENT:
                complaintSuggestion.setAssignmentStaffid(user.getStaffid());
                complaintSuggestion.setAssignmentUname(user.getUserName());
                complaintSuggestion.setAssignmentUid(user.getId());
                complaintSuggestion.setAssignmentTime(new Date());
                break;
            case COMPLATE:
                complaintSuggestion.setReplyStaffid(user.getStaffid());
                complaintSuggestion.setReplyUname(user.getUserName());
                complaintSuggestion.setReplyUid(user.getId());
                complaintSuggestion.setReplyDate(new Date());
            case AUDIT_REJECT:
                complaintSuggestion.setAuditStaffid(user.getStaffid());
                complaintSuggestion.setAuditUname(user.getUserName());
                complaintSuggestion.setAuditUid(user.getId());
                complaintSuggestion.setAuditTime(new Date());
                break;
            default:
                break;
        }
    }

}
