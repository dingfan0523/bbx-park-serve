
package com.cgnpc.bbxpark.settings.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cgnpc.bbxpark.settings.domain.ClickVolume;
import com.cgnpc.bbxpark.settings.dto.model.ClickVolumeModel;
import com.cgnpc.bbxpark.settings.dto.param.ClickVolumeListParam;
import com.cgnpc.bbxpark.settings.dto.param.ClickVolumeParam;
import com.cgnpc.bbxpark.settings.mapper.ClickVolumeRepository;
import com.cgnpc.bbxpark.settings.service.IClickVolumeService;
import com.cgnpc.bbxpark.common.utils.BeanUtils;
import com.cgnpc.bbxpark.common.utils.WebFrameworkUtils;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@Service("clickVolumeService")
public class ClickVolumeServiceImpl extends ServiceImpl<ClickVolumeRepository, ClickVolume> implements IClickVolumeService {


    /**
     * 获取点击量列表.
     *
     * @Param param 点击量查询条件
     * @Return 点击量信息列表
     */
    @Override
    @SneakyThrows
    public List<ClickVolumeModel> list(ClickVolumeListParam param) {
        List<ClickVolume> viewRecords = list(Wrappers.<ClickVolume>lambdaQuery().eq(ClickVolume::getTenantId, WebFrameworkUtils.getHeaderTenantId())
                .eq(ObjectUtil.isNotEmpty(param.getUserId()), ClickVolume::getUserId, param.getUserId()));
        List<ClickVolumeModel> volumeModels = BeanUtils.convertListTo(viewRecords, ClickVolumeModel::new);
        return volumeModels.stream().sorted(Comparator.comparing(ClickVolumeModel::getVisitCount).reversed()).collect(Collectors.toList());
    }

    /**
     * 新增点击量.
     *
     * @Param param 点击量信息
     * @Return 新增点击量是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean add(ClickVolumeParam param) {
        ClickVolume volume = getOne(Wrappers.<ClickVolume>lambdaQuery().eq(ClickVolume::getTenantId, WebFrameworkUtils.getHeaderTenantId())
                .eq(ObjectUtil.isNotEmpty(param.getUserId()), ClickVolume::getUserId, param.getUserId())
                .eq(ObjectUtil.isNotEmpty(param.getMenuCode()), ClickVolume::getMenuCode, param.getMenuCode()));
        if (ObjectUtil.isEmpty(volume)){
            ClickVolume clickVolume = BeanUtils.convertTo(param, ClickVolume::new);
            clickVolume.setId(null);
            clickVolume.setTenantId(WebFrameworkUtils.getHeaderTenantId());
            return save(clickVolume);
        }
        volume.setVisitCount(volume.getVisitCount() + 1);
        return updateById(volume);
    }


}
