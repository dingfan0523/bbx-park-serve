package com.cgnpc.bbxpark.restaurant.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cgnpc.bbxpark.common.constant.SystemResultCode;
import com.cgnpc.bbxpark.common.enums.Delete;
import com.cgnpc.bbxpark.common.utils.AssertUtils;
import com.cgnpc.bbxpark.common.utils.BeanUtils;
import com.cgnpc.bbxpark.common.utils.ConvertUtil;
import com.cgnpc.bbxpark.common.utils.WebFrameworkUtils;
import com.cgnpc.bbxpark.restaurant.domain.Combo;
import com.cgnpc.bbxpark.restaurant.domain.CompartmentCombo;
import com.cgnpc.bbxpark.restaurant.dto.model.ComboModel;
import com.cgnpc.bbxpark.restaurant.dto.param.ComboListParam;
import com.cgnpc.bbxpark.restaurant.dto.param.ComboParam;
import com.cgnpc.bbxpark.restaurant.mapper.ComboRepository;
import com.cgnpc.bbxpark.restaurant.service.IComboService;
import com.cgnpc.bbxpark.restaurant.service.ICompartmentComboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 套餐服务实现
 * </p>
 *
 * @author gujun
 * @time 2024-07-22
 */
@Service
public class ComboServiceImpl extends ServiceImpl<ComboRepository, Combo> implements IComboService {
    @Autowired
    private ICompartmentComboService compartmentComboService;

    @Override
    public ComboModel getComboModel(Integer id) {
        Combo combo = this.getById(id);
        ComboModel model = BeanUtils.convertTo(combo,ComboModel::new);
        DecimalFormat df = new DecimalFormat("0.00");
        model.setPrice(df.format(combo.getPrice()));
        return model;
    }

    @Override
    public IPage<ComboModel> pageResult(ComboListParam param) {
        // 分页参数
        Long tenantId = WebFrameworkUtils.getHeaderTenantId();
        LambdaQueryWrapper<Combo> wrapper = Wrappers.<Combo>lambdaQuery()
                .eq(ObjectUtil.isNotNull(tenantId),Combo::getTenantId,tenantId)
                .like(StrUtil.isNotBlank(param.getName()), Combo::getName, param.getName())
                .eq(StrUtil.isNotBlank(param.getType()), Combo::getType, param.getType())
                .eq(ObjectUtil.isNotNull(param.getStatus()), Combo::getStatus, param.getStatus())
                .eq(Combo::getDeleted, Delete.NORMAL.getKey())
                .orderByDesc(Combo::getCreateTime);
        // 分页查询
        IPage<Combo> iPage  = this.page(new Page<>(param.getCurrent(), param.getSize()), wrapper);
        // Model 转换
        DecimalFormat df = new DecimalFormat("0.00");
        List<ComboModel> comboModels = iPage.getRecords().stream().map(c->{
            ComboModel model = BeanUtils.convertTo(c,ComboModel::new);
            model.setPrice(df.format(c.getPrice()));
            return model;
        }).collect(Collectors.toList());
        return ConvertUtil.pageConvert(iPage,comboModels);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Long saveOrUpdate(ComboParam param) {
        //校验数据
        validate(param);
        Combo combo = BeanUtils.convertTo(param, Combo::new);
        this.saveOrUpdate(combo);
        return combo.getId();
    }


    private void validate(ComboParam param) {
        Long tenantId = WebFrameworkUtils.getHeaderTenantId();
        int num = this.count(new LambdaQueryWrapper<Combo>()
                .eq(Combo::getName, param.getName())
                .eq(ObjectUtil.isNotNull(tenantId), Combo::getTenantId, WebFrameworkUtils.getHeaderTenantId())
                .eq(Combo::getDeleted, Delete.NORMAL.getKey())
                .ne(param.getId() != null, Combo::getId, param.getId()));
        AssertUtils.isTrue(num == 0, "套餐重复，请重试");
    }
    @Override
    public List<ComboModel> list(ComboListParam param) {
        Long tenantId = WebFrameworkUtils.getHeaderTenantId();
        List<Combo> combos = this.list(new LambdaQueryWrapper<Combo>().eq(ObjectUtil.isNotEmpty(param.getStatus()), Combo::getStatus, param.getStatus())
                .eq(ObjectUtil.isNotNull(tenantId),Combo::getTenantId,tenantId).eq(Combo::getDeleted, Delete.NORMAL.getKey()));
        return BeanUtils.convertListTo(combos, ComboModel::new);
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean remove(Long id) {
        Combo combo = this.getById(id);
        AssertUtils.notNull(combo, SystemResultCode.RESULT_DATA_NONE.message());
        //删除包间套餐关联表
        compartmentComboService.remove(new LambdaQueryWrapper<CompartmentCombo>().eq(CompartmentCombo::getComboId, id));
        combo.setDeleted(Delete.DELETED.getKey());
        return updateById(combo);
    }

}
