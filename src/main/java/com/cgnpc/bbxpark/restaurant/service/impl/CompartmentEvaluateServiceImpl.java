
package com.cgnpc.bbxpark.restaurant.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cgnpc.bbxpark.acl.uic.service.IUserApiService;
import com.cgnpc.bbxpark.common.constant.SystemResultCode;
import com.cgnpc.bbxpark.common.enums.Delete;
import com.cgnpc.bbxpark.common.enums.Status;
import com.cgnpc.bbxpark.common.utils.*;
import com.cgnpc.bbxpark.restaurant.domain.Compartment;
import com.cgnpc.bbxpark.restaurant.domain.CompartmentEvaluate;
import com.cgnpc.bbxpark.restaurant.dto.model.CompartmentEvaluateModel;
import com.cgnpc.bbxpark.restaurant.dto.param.CompartmentEvaluatePageParam;
import com.cgnpc.bbxpark.restaurant.dto.param.CompartmentEvaluateParam;
import com.cgnpc.bbxpark.restaurant.mapper.CompartmentEvaluateRepository;
import com.cgnpc.bbxpark.restaurant.mapper.CompartmentRepository;
import com.cgnpc.bbxpark.restaurant.service.ICompartmentEvaluateService;
import com.cgnpc.bbxpark.space.dto.model.UserInfoModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Service
public class CompartmentEvaluateServiceImpl extends ServiceImpl<CompartmentEvaluateRepository, CompartmentEvaluate> implements ICompartmentEvaluateService {
    @Autowired
    private IUserApiService userApiService;
    @Autowired
    private CompartmentRepository compartmentRepository;

    @Override
    public IPage<CompartmentEvaluateModel> page(CompartmentEvaluatePageParam param) {
        Long tenantId = WebFrameworkUtils.getHeaderTenantId();
        IPage<CompartmentEvaluate> page = baseMapper.selectPage(new Page<>(param.getCurrent(), param.getSize()), Wrappers.<CompartmentEvaluate>lambdaQuery()
                .eq(CompartmentEvaluate::getCompartmentId,param.getCompartmentId()).eq(tenantId != null,CompartmentEvaluate::getTenantId,tenantId).orderByDesc(CompartmentEvaluate::getCreateTime));
        if(CollectionUtils.isEmpty(page.getRecords())){
            return ConvertUtil.pageEmptyConvert(param.getCurrent(),param.getSize());
        }
        return ConvertUtil.pageConvert(page,BeanUtils.convertListTo(page.getRecords(),CompartmentEvaluateModel::new));
    }

    @Override
    public List<CompartmentEvaluateModel> list(Long compartmentId) {
        Long tenantId = WebFrameworkUtils.getHeaderTenantId();
        List<CompartmentEvaluate> list = list(Wrappers.<CompartmentEvaluate>lambdaQuery().eq(CompartmentEvaluate::getCompartmentId,compartmentId).eq(tenantId != null,CompartmentEvaluate::getTenantId,tenantId).orderByDesc(CompartmentEvaluate::getCreateTime));
        return BeanUtils.convertListTo(list,CompartmentEvaluateModel::new);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean add(CompartmentEvaluateParam param) {
        //校验是否重复评价
        AssertUtils.isFalse(verifyRepeat(param.getReserveId()), "不能重复评价");
        CompartmentEvaluate evaluate = BeanUtils.convertTo(param, CompartmentEvaluate::new);
        //评价人
        UserInfoModel userInfoModel = userApiService.getCurrentUserInfo();
        evaluate.setAppraiserId(userInfoModel.getId());
        evaluate.setAppraiserStaffid(userInfoModel.getStaffid());
        evaluate.setAppraiserName(userInfoModel.getUserName());
        //更新包间平均分
        updateSatisfaction(param.getCompartmentId(),param.getSatisfaction());
        return save(evaluate);
    }

    /**
     * 校验单个预约是否重复评价
     * @param reserveId 包间预约id
     */
    private boolean verifyRepeat(Long reserveId){
        return count(Wrappers.<CompartmentEvaluate>lambdaQuery().eq(CompartmentEvaluate::getReserveId,reserveId).eq(CompartmentEvaluate::getDeleted, Delete.NORMAL.getKey()))>0;
    }

    /**
     * 更新包间平均分
     * @param compartmentId 包间id
     * @param satisfaction 本次评价分
     */
    private void updateSatisfaction(Long compartmentId,Integer satisfaction){
        Compartment compartment = compartmentRepository.selectById(compartmentId);
        AssertUtils.notNull(compartment, SystemResultCode.RESULT_DATA_NONE.message());
        //计算平均分
        List<CompartmentEvaluate> list = list(Wrappers.<CompartmentEvaluate>lambdaQuery().eq(CompartmentEvaluate::getCompartmentId,compartmentId));
        int amount = list.stream().mapToInt(CompartmentEvaluate::getSatisfaction).sum();
        compartment.setSatisfaction(new BigDecimal(Integer.toString(amount + satisfaction)).divide(new BigDecimal(Integer.toString(list.size()+1)),1, RoundingMode.HALF_UP).doubleValue());
        compartmentRepository.updateById(compartment);
    }

    /**
     * 评价人匿名处理
     * @param list 列表
     * @return 匿名处理后的列表
     */
    private List<CompartmentEvaluateModel> anonymityHandle(String userId,List<CompartmentEvaluate> list){
        return list.stream().map(evaluate -> {
            CompartmentEvaluateModel model = BeanUtils.convertTo(evaluate,CompartmentEvaluateModel::new);
            if(Status.enabled.getKey().equals(evaluate.getAnonymityStatus())){
                model.setAppraiserName(evaluate.getAppraiserId().equals(userId) ? "匿名(我)":"匿名");
            }
            //其他字段置空
            model.setAppraiserId(null);
            model.setAnonymityStatus(null);
            return model;
        }).collect(Collectors.toList());
    }
}
