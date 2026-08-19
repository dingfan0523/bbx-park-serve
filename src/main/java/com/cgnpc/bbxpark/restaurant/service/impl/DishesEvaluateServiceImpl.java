
package com.cgnpc.bbxpark.restaurant.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cgnpc.bbxpark.acl.uic.service.IUserApiService;
import com.cgnpc.bbxpark.settings.service.IAttentionManageService;
import com.cgnpc.bbxpark.common.constant.MessageConstant;
import com.cgnpc.bbxpark.common.enums.Status;
import com.cgnpc.bbxpark.common.service.IMessageCommonService;
import com.cgnpc.bbxpark.common.utils.*;
import com.cgnpc.bbxpark.restaurant.domain.DishesEvaluate;
import com.cgnpc.bbxpark.restaurant.domain.DishesGallery;
import com.cgnpc.bbxpark.restaurant.dto.model.DishesEvaluateModel;
import com.cgnpc.bbxpark.restaurant.dto.param.DishesEvaluatePageParam;
import com.cgnpc.bbxpark.restaurant.dto.param.DishesEvaluateParam;
import com.cgnpc.bbxpark.restaurant.mapper.DishesEvaluateRepository;
import com.cgnpc.bbxpark.restaurant.mapper.DishesGalleryRepository;
import com.cgnpc.bbxpark.restaurant.service.IDishesEvaluateService;
import com.cgnpc.bbxpark.space.dto.model.UserInfoModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 菜品评价服务实现
 * @author dingfan
 * @date 2024/7/18
 */
@Service
public class DishesEvaluateServiceImpl extends ServiceImpl<DishesEvaluateRepository, DishesEvaluate> implements IDishesEvaluateService {
    @Autowired
    private IUserApiService userApiService;
    @Autowired
    private DishesGalleryRepository dishesGalleryRepository;
    @Autowired
    IAttentionManageService attentionManageService;

    @Autowired
    private IMessageCommonService messageCommonService;


    @Override
    public IPage<DishesEvaluateModel> page(DishesEvaluatePageParam param) {
        Long tenantId = WebFrameworkUtils.getHeaderTenantId();
        IPage<DishesEvaluate> page = baseMapper.selectPage(new Page<>(param.getCurrent(), param.getSize()),Wrappers.<DishesEvaluate>lambdaQuery().eq(DishesEvaluate::getName,param.getName()).eq(tenantId != null,DishesEvaluate::getTenantId,tenantId).orderByDesc(DishesEvaluate::getCreateTime));
        if(CollectionUtils.isEmpty(page.getRecords())){
            return ConvertUtil.pageEmptyConvert(param.getCurrent(),param.getSize());
        }
        return ConvertUtil.pageConvert(page,BeanUtils.convertListTo(page.getRecords(),DishesEvaluateModel::new));
    }

    @Override
    public List<DishesEvaluateModel> list(String name) {
        Long tenantId = WebFrameworkUtils.getHeaderTenantId();
        List<DishesEvaluate> list = list(Wrappers.<DishesEvaluate>lambdaQuery().eq(DishesEvaluate::getName,name).eq(tenantId != null,DishesEvaluate::getTenantId,tenantId).orderByDesc(DishesEvaluate::getCreateTime));
        return BeanUtils.convertListTo(list,DishesEvaluateModel::new);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean add(DishesEvaluateParam param) {
        //校验用户当日是否已经评价该菜品（对于一个菜品，每个用户每日仅能评价一次，防止评价刷屏）
        AssertUtils.isFalse(verifyRepeat(param.getName()), "今日已评价，请明日再试");

        DishesEvaluate evaluate = BeanUtils.convertTo(param, DishesEvaluate::new);
        //评价人
        UserInfoModel userInfoModel = getUser();
        evaluate.setAppraiserId(userInfoModel.getId());
        evaluate.setAppraiserStaffid(userInfoModel.getStaffid());
        evaluate.setAppraiserName(userInfoModel.getUserName());
        //更新菜品库平均分
        calculateAverageAndUpdate(param.getName(),param.getSatisfaction());
        save(evaluate);
        sendAttentionMessage(evaluate, userInfoModel);
        return true;
    }


    @Override
    public Double calculateAverage(String name, Integer satisfaction) {
        Long tenantId = WebFrameworkUtils.getHeaderTenantId();
        List<DishesEvaluate> list = list(Wrappers.<DishesEvaluate>lambdaQuery().eq(DishesEvaluate::getName,name).eq(tenantId != null,DishesEvaluate::getTenantId,tenantId));
        int amount = list.stream().mapToInt(DishesEvaluate::getSatisfaction).sum();
        //计算平均分
        if(satisfaction == null){
            return CollectionUtils.isEmpty(list) ? null :new BigDecimal(Integer.toString(amount)).divide(new BigDecimal(Integer.toString(list.size())),1, RoundingMode.HALF_UP).doubleValue();
        }
        return new BigDecimal(Integer.toString(amount + satisfaction)).divide(new BigDecimal(Integer.toString(list.size()+1)),1, RoundingMode.HALF_UP).doubleValue();
    }

    @Override
    public Double calculateAverageAndUpdate(String name, Integer satisfaction) {
        Long tenantId = WebFrameworkUtils.getHeaderTenantId();
        DishesGallery gallery = dishesGalleryRepository.selectOne(Wrappers.<DishesGallery>lambdaQuery().eq(DishesGallery::getName,name).eq(tenantId != null,DishesGallery::getTenantId,tenantId));
        if(gallery != null){
            //计算平均分
            Double average = calculateAverage(name,satisfaction);
            gallery.setSatisfaction(average);
            dishesGalleryRepository.updateById(gallery);
            return average;
        }
        return null;
    }


    /**
     * 校验重复评价
     * @param name 菜品名称
     * @return 校验结果
     */
    private Boolean verifyRepeat(String name){
        String userId = WebFrameworkUtils.getHeaderUserId();
        return count(Wrappers.<DishesEvaluate>lambdaQuery().eq(DishesEvaluate::getAppraiserId,userId).eq(DishesEvaluate::getName,name).between(DishesEvaluate::getCreateTime,DateUtil.getFirstTimeOfCurrent(),DateUtil.getLastTimeOfCurrent()))>0;
    }

    /**
     * 评价人匿名处理
     * @param list 列表
     * @return 匿名处理后的列表
     */
    private List<DishesEvaluateModel> anonymityHandle(String userId, List<DishesEvaluate> list){
        return list.stream().map(evaluate -> {
            DishesEvaluateModel model = BeanUtils.convertTo(evaluate,DishesEvaluateModel::new);
            if(Status.enabled.getKey().equals(evaluate.getAnonymityStatus())){
                model.setAppraiserName(evaluate.getAppraiserId().equals(userId) ? "匿名(我)":"匿名");
            }
            //其他字段置空
            model.setAppraiserId(null);
            model.setAnonymityStatus(null);
            return model;
        }).collect(Collectors.toList());
    }

    /***
     * 获取当前登录用户信息
     * @author huangyongtao
     * @date 2024/7/17 11:30
     */
    private UserInfoModel getUser(){
        return Objects.requireNonNull(userApiService.detail(WebFrameworkUtils.getHeaderUserId()));
    }

    /***
     * @Description 关注人消息
     * @author huangyongtao
     * @date 2025/3/31 16:02
     * @param evaluate
     */
    private void sendAttentionMessage(DishesEvaluate evaluate, UserInfoModel userInfo) {
        if(!attentionManageService.checkAttention(userInfo.getId())){
            return;
        }
        Map<String, String> variables = new HashMap<>(4);
        variables.put("type", "菜品");
        variables.put("attentionName", userInfo.getUserName());
        variables.put("content", evaluate.getTaste());
        variables.put("score", evaluate.getSatisfaction() + "分");
        messageCommonService.sendMessage(MessageConstant.ATTENTION_EVALUATE_NOTICE, evaluate.getTenantId(), evaluate.getId(), new HashSet<>(), variables);
    }

}
