package com.cgnpc.bbxpark.property.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cgnpc.bbxpark.common.utils.BeanUtils;
import com.cgnpc.bbxpark.property.domain.MaintainItem;
import com.cgnpc.bbxpark.property.dto.model.MaintainItemModel;
import com.cgnpc.bbxpark.property.dto.param.MaintainItemParam;
import com.cgnpc.bbxpark.property.mapper.MaintainItemRepository;
import com.cgnpc.bbxpark.property.service.IMaintainItemService;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/***
 * @Description 维保项目服务实现
 * @author huangyongtao
 * @date 2025/10/16 15:15
 */
@Service
public class MaintainItemServiceImpl extends ServiceImpl<MaintainItemRepository, MaintainItem> implements IMaintainItemService {

	/**
	 * 获取维保项目列表.
	 * @Param param 维保项目查询条件
	 * @Return 维保项目信息列表
	 */
	@Override
	@SneakyThrows
	public List<MaintainItemModel> list(Long maintainId) {
		List<MaintainItem> maintainItems = this.list(buildQuery(maintainId));
		return BeanUtils.convertListTo(maintainItems, MaintainItemModel::new);
	}

	/**
	 * 新增维保项目.
	 * @Param param 维保项目信息
	 * @Return 新增维保项目是否成功
	 */
	@Override
	public Boolean add(MaintainItemParam param) {
		MaintainItem maintainItem = BeanUtils.convertTo(param, MaintainItem::new);
		maintainItem.setId(null);
		return this.save(maintainItem);
	}

	/**
	 * 批量新增维保项目.
	 * @Param params 维保项目信息列表
	 * @Return 批量新增维保项目是否成功
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean addBatch(List<MaintainItemParam> params) {
		if (CollectionUtil.isEmpty(params)) {
			return true;
		}
		this.remove(params.get(0).getMaintainId());
		List<MaintainItem> maintainItems = BeanUtils.convertListTo(params, MaintainItem::new);
		return this.saveBatch(maintainItems);
	}

	/**
	 * 删除维保项目.
	 * @Param maintainId 维保项目标识
	 * @Return 删除维保项目是否成功
	 */
	@Override
	public Boolean remove(Long maintainId) {
		return this.remove(Wrappers.<MaintainItem>lambdaQuery().eq(MaintainItem::getMaintainId, maintainId));
	}


	private LambdaQueryWrapper<MaintainItem> buildQuery(Long maintainId) {
		LambdaQueryWrapper<MaintainItem> query = new LambdaQueryWrapper<>();
		// 根据维保id筛选
		query.eq(ObjectUtil.isNotEmpty(maintainId), MaintainItem::getMaintainId, maintainId);
		query.orderByAsc(MaintainItem::getTaskGroup);
        query.orderByAsc(MaintainItem::getCreateTime);
		return query;
	}
}