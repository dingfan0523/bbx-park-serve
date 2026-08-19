package com.cgnpc.bbxpark.property.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cgnpc.bbxpark.property.domain.InventoryItem;
import com.cgnpc.bbxpark.property.dto.param.InventoryItemParam;

import java.util.List;

/**
 * 盘点计划明细服务接口
 */
public interface IInventoryItemService extends IService<InventoryItem> {

	/**
	 * 批量新增盘点计划明细.
	 * @Param params 盘点计划明细信息列表
	 * @Return 批量新增盘点计划明细是否成功
	 */
	Boolean addBatch(List<InventoryItemParam> params);

	/**
	 * 删除盘点计划明细.
	 * @Param inventoryId 盘点计划明细标识
	 * @Return 删除盘点计划明细是否成功
	 */
	Boolean remove(Long inventoryId);
}
