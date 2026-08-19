
package com.cgnpc.bbxpark.message.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cgnpc.bbxpark.acl.uic.service.IUserApiService;
import com.cgnpc.bbxpark.common.constant.SystemResultCode;
import com.cgnpc.bbxpark.common.enums.Delete;
import com.cgnpc.bbxpark.common.enums.Status;
import com.cgnpc.bbxpark.common.utils.*;
import com.cgnpc.bbxpark.common.web.CudPageDto;
import com.cgnpc.bbxpark.message.domain.LogisticsGuide;
import com.cgnpc.bbxpark.message.dto.req.LogisticsGuideListParam;
import com.cgnpc.bbxpark.message.dto.req.LogisticsGuidePageParam;
import com.cgnpc.bbxpark.message.dto.req.LogisticsGuideParam;
import com.cgnpc.bbxpark.message.dto.resp.AppLogisticsGuideDetailModel;
import com.cgnpc.bbxpark.message.dto.resp.AppLogisticsGuideModel;
import com.cgnpc.bbxpark.message.dto.resp.LogisticsGuideDetailModel;
import com.cgnpc.bbxpark.message.dto.resp.LogisticsGuideListModel;
import com.cgnpc.bbxpark.message.mapper.LogisticsGuideRepository;
import com.cgnpc.bbxpark.message.service.ILogisticsGuideService;
import com.cgnpc.bbxpark.space.dto.model.UserInfoModel;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 后勤指南服务实现
 * @author dingfan
 * @date 2024/10/12 13:56
 */
@Service
public class LogisticsGuideServiceImpl extends ServiceImpl<LogisticsGuideRepository, LogisticsGuide> implements ILogisticsGuideService {

    @Resource
    private IUserApiService userApiService;

    @Override
    public IPage<LogisticsGuideListModel> page(LogisticsGuidePageParam param) {
        IPage<LogisticsGuide> page = page(new Page<>(param.getCurrent(),param.getSize()), buildQuery(param.getTitle(),param.getStartTime(),param.getEndTime()));
        if(CollectionUtils.isEmpty(page.getRecords())){
            return ConvertUtil.pageEmptyConvert(page.getCurrent(),page.getSize());
        }
        //数据转换
        List<LogisticsGuideListModel> list = BeanUtils.convertListTo(page.getRecords(), LogisticsGuideListModel::new);
        userHandle(list);
        return ConvertUtil.pageConvert(page.getCurrent(),page.getTotal(),page.getSize(),list);
    }

    @Override
    public List<LogisticsGuideListModel> list(LogisticsGuideListParam param) {
        List<LogisticsGuide> guideList = list(buildQuery(param.getTitle(),param.getStartTime(),param.getEndTime()));
        List<LogisticsGuideListModel> list = BeanUtils.convertListTo(guideList, LogisticsGuideListModel::new);
        userHandle(list);
        return list;
    }

    @Override
    public LogisticsGuideDetailModel detail(Long id) {
        LogisticsGuide guide = getById(id);
        AssertUtils.notNull(guide, SystemResultCode.RESULT_DATA_NONE.message());
        return BeanUtils.convertTo(guide, LogisticsGuideDetailModel::new);
    }

    @Override
    public Boolean add(LogisticsGuideParam param) {
        //后勤指南标题唯一性校验
        AssertUtils.isFalse(verifyTitle(param.getId(), param.getTitle()), "“"+param.getTitle()+"”已存在，请确认");
        LogisticsGuide guide = BeanUtils.convertTo(param,LogisticsGuide::new);
        //默认字段
        guide.setId(null);
        guide.setPublisherId(userApiService.getCurrentStaffNo());
        guide.setPublishTime(new Date());
        guide.setContent(SimpleHtmlUtil.transform(guide.getContent()));
        return save(guide);
    }

    @Override
    public Boolean edit(LogisticsGuideParam param) {
        //后勤指南标题唯一性校验
        AssertUtils.isFalse(verifyTitle(param.getId(), param.getTitle()), "“"+param.getTitle()+"”已存在，请确认");
        LogisticsGuide guide = getById(param.getId());
        AssertUtils.notNull(guide, SystemResultCode.RESULT_DATA_NONE.message());
        param.setId(guide.getId());
        BeanUtils.copyProperties(param,guide);
        guide.setPublisherId(userApiService.getCurrentStaffNo());
        guide.setPublishTime(new Date());
        guide.setContent(SimpleHtmlUtil.transform(param.getContent()));
        return updateById(guide);
    }

    @Override
    public Boolean remove(Long id) {
        LogisticsGuide guide = getById(id);
        AssertUtils.notNull(guide, SystemResultCode.RESULT_DATA_NONE.message());
        guide.setDeleted(Delete.DELETED.getKey());
        return updateById(guide);
    }

    @Override
    public IPage<AppLogisticsGuideModel> pageApp(CudPageDto param) {
        Long tenantId = WebFrameworkUtils.getHeaderTenantId();
        IPage<LogisticsGuide> page = page(new Page<>(param.getCurrent(),param.getSize()), Wrappers.<LogisticsGuide>lambdaQuery().eq(LogisticsGuide::getDeleted, Status.enabled).eq(tenantId != null,LogisticsGuide::getTenantId,tenantId).orderByAsc(LogisticsGuide::getOrderCode));
        if(CollectionUtils.isEmpty(page.getRecords())){
            return ConvertUtil.pageEmptyConvert(page.getCurrent(),page.getSize());
        }
        //数据转换
        List<AppLogisticsGuideModel> list = BeanUtils.convertListTo(page.getRecords(), AppLogisticsGuideModel::new);
        return ConvertUtil.pageConvert(page,list);
    }

    @Override
    public List<AppLogisticsGuideModel> listApp() {
        Long tenantId = WebFrameworkUtils.getHeaderTenantId();
        List<LogisticsGuide> guideList = list(Wrappers.<LogisticsGuide>lambdaQuery().eq(LogisticsGuide::getDeleted, Delete.NORMAL.getKey())
                .eq(tenantId != null,LogisticsGuide::getTenantId,tenantId).orderByAsc(LogisticsGuide::getOrderCode));
        return BeanUtils.convertListTo(guideList, AppLogisticsGuideModel::new);
    }

    @Override
    public AppLogisticsGuideDetailModel detailApp(Long id) {
        LogisticsGuide guide = getById(id);
        AssertUtils.notNull(guide, SystemResultCode.RESULT_DATA_NONE.message());
        AppLogisticsGuideDetailModel model = BeanUtils.convertTo(guide, AppLogisticsGuideDetailModel::new);
        //用户数据
        UserInfoModel userInfo = userApiService.getByStaffNo(guide.getPublisherId());
        model.setPublisherName(userInfo.getUserName());
        model.setPublisherStaffid(userInfo.getStaffid());
        return model;
    }

    /**
     * 构建查询条件
     * @param title 标题
     * @param start 时间范围查询(发布时间)起始值
     * @param end 时间范围查询(发布时间)结束值
     * @return 查询条件
     */
    private LambdaQueryWrapper<LogisticsGuide> buildQuery(String title, Date start,Date end){
        Long tenantId = WebFrameworkUtils.getHeaderTenantId();
        return Wrappers.<LogisticsGuide>lambdaQuery().eq(tenantId != null, LogisticsGuide::getTenantId,tenantId)
                .like(StringUtils.isNotEmpty(title), LogisticsGuide::getTitle,title)
                .ge(ObjectUtil.isNotEmpty(start), LogisticsGuide::getPublishTime, start)
                .le(ObjectUtil.isNotEmpty(end), LogisticsGuide::getPublishTime, end)
                .eq(LogisticsGuide::getDeleted, Delete.NORMAL.getKey()).orderByAsc(LogisticsGuide::getOrderCode);
    }

    /**
     * 用户信息处理
     * @param list 物流指南列表
     */
    private void userHandle(List<LogisticsGuideListModel> list){
        List<String> userIdList = list.stream().map(LogisticsGuideListModel::getPublisherId).distinct().collect(Collectors.toList());
        if(CollectionUtils.isEmpty(userIdList)){
            return;
        }
        //用户批量查询
        List<UserInfoModel> userInfoList = userApiService.getByStaffNos(userIdList);
        //数据聚合
        Map<String,UserInfoModel> userInfoMap = Optional.of(userInfoList).map(users -> users.stream()
                .collect(Collectors.toMap(UserInfoModel::getId, Function.identity()))).orElse(Collections.emptyMap());
        list.stream().filter(l->userInfoMap.containsKey(l.getPublisherId())).forEach(logisticsGuide -> {
            logisticsGuide.setPublisherName(userInfoMap.get(logisticsGuide.getPublisherId()).getUserName());
            logisticsGuide.setPublisherStaffid(userInfoMap.get(logisticsGuide.getPublisherId()).getStaffid());
        });
    }

    /**
     * 校验标题是否重复
     * @param id id
     * @param title 标题
     * @return true:重复 false:不重复
     */
    private boolean verifyTitle(Long id,String title){
        Long tenantId = WebFrameworkUtils.getHeaderTenantId();
        return count(Wrappers.<LogisticsGuide>lambdaQuery().eq(LogisticsGuide::getTitle, title)
                .eq(LogisticsGuide::getDeleted, Delete.NORMAL.getKey())
                .eq(tenantId != null,LogisticsGuide::getTenantId,tenantId)
                //如果id不为空,那么说明是编辑,需排除自身
                .ne(id != null, LogisticsGuide::getId, id)) > 0;
    }
}
