
package com.cgnpc.bbxpark.settings.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cgnpc.bbxpark.common.constant.SystemResultCode;
import com.cgnpc.bbxpark.common.enums.ModelTypeEnum;
import com.cgnpc.bbxpark.common.utils.AssertUtils;
import com.cgnpc.bbxpark.common.utils.BeanUtils;
import com.cgnpc.bbxpark.common.utils.WebFrameworkUtils;
import com.cgnpc.bbxpark.settings.domain.ScreenOverview;
import com.cgnpc.bbxpark.settings.dto.model.ApiScreenOverviewModel;
import com.cgnpc.bbxpark.settings.dto.model.ScreenOverviewModel;
import com.cgnpc.bbxpark.settings.dto.param.ScreenOverviewParam;
import com.cgnpc.bbxpark.settings.mapper.ScreenOverviewRepository;
import com.cgnpc.bbxpark.settings.service.IScreenOverviewService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ScreenOverviewServiceImpl extends ServiceImpl<ScreenOverviewRepository, ScreenOverview> implements IScreenOverviewService {
    @Override
    public List<ScreenOverviewModel> listBy() {
        return list(Wrappers.<ScreenOverview>lambdaQuery().eq(ScreenOverview::getTenantId, WebFrameworkUtils.getHeaderTenantId()))
                .stream().map(s-> {
                    ScreenOverviewModel model = new ScreenOverviewModel();
                    model.setId(s.getId());
                    model.setModelType(s.getModelType());
                    model.setModelName(s.getModelName());
                    model.setModelData(JSONObject.parseObject(s.getModelData()));
                    return model;
                }).collect(Collectors.toList());
    }

    @Override
    public ScreenOverviewModel detail(Long id) {
        ScreenOverview screenOverview = getById(id);
        AssertUtils.notNull(screenOverview, SystemResultCode.RESULT_DATA_NONE.message());
        ScreenOverviewModel model = BeanUtils.convertTo(screenOverview,ScreenOverviewModel::new);
        model.setId(screenOverview.getId());
        model.setModelType(screenOverview.getModelType());
        model.setModelName(screenOverview.getModelName());
        model.setModelData(JSONObject.parseObject(screenOverview.getModelData()));
        return model;
    }

    @Override
    public Boolean add(ScreenOverviewParam param) {
        ScreenOverview screenOverview = BeanUtils.convertTo(param,ScreenOverview::new);
       return save(screenOverview);
    }

    @Override
    public Boolean edit(ScreenOverviewParam param) {
        ScreenOverview screenOverview = getById(param.getId());
        AssertUtils.notNull(screenOverview, SystemResultCode.RESULT_DATA_NONE.message());
        BeanUtils.copyProperties(param,screenOverview);
        return updateById(screenOverview);
    }

    @Override
    public ApiScreenOverviewModel get() {
        List<ScreenOverview> list = list(Wrappers.<ScreenOverview>lambdaQuery().eq(ScreenOverview::getTenantId, WebFrameworkUtils.getHeaderTenantId()));
        ApiScreenOverviewModel model = new ApiScreenOverviewModel();
        list.forEach(s->{
            if(ModelTypeEnum.ZGHQ.getCode().equals(s.getModelType())){
                model.setLogisticsData(JSONObject.parseObject(s.getModelData()));
            }
        });
        return model;
    }
}
