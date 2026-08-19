package com.cgnpc.bbxpark.property.service.impl;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cgnpc.bbxpark.common.utils.BeanUtils;
import com.cgnpc.bbxpark.property.domain.InventoryItem;
import com.cgnpc.bbxpark.property.dto.param.InventoryItemParam;
import com.cgnpc.bbxpark.property.mapper.InventoryItemRepository;
import com.cgnpc.bbxpark.property.service.IInventoryItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 盘点计划明细服务实现
 */
@Slf4j
@Service
public class InventoryItemServiceImpl extends ServiceImpl<InventoryItemRepository, InventoryItem> implements IInventoryItemService {

	/**
	 * 批量新增盘点计划明细.
	 * @Param params 盘点计划明细信息列表
	 * @Return 批量新增盘点计划明细是否成功
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean addBatch(List<InventoryItemParam> params) {
		List<InventoryItem> inventoryItems = BeanUtils.convertListTo(params, InventoryItem::new);
		return saveBatch(inventoryItems);
	}

	/**
	 * 删除盘点计划明细.
	 * @Param id 盘点计划明细标识
	 * @Return 删除盘点计划明细是否成功
	 */
	@Override
	public Boolean remove(Long inventoryId) {
		return remove(Wrappers.<InventoryItem>lambdaQuery().eq(InventoryItem::getInventoryId, inventoryId));
	}
}
