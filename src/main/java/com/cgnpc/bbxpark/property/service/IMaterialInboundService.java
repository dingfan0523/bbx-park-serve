package com.cgnpc.bbxpark.property.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cgnpc.bbxpark.property.domain.MaterialInbound;
import com.cgnpc.bbxpark.property.dto.model.MaterialInboundModel;
import com.cgnpc.bbxpark.property.dto.param.MaterialInboundListParam;
import com.cgnpc.bbxpark.property.dto.param.MaterialInboundPageParam;
import com.cgnpc.bbxpark.property.dto.param.MaterialInboundParam;

import java.util.List;

/***
 * @Description 材料入库服务接口
 * @author huangyongtao
 * @date 2025/9/22 16:18
 */
public interface IMaterialInboundService extends IService<MaterialInbound> {

	/**
	 * 获取材料入库列表(分页).
	 * @Param param 材料入库查询条件
	 * @Return 材料入库信息列表（分页）
	 */
	IPage<MaterialInboundModel> page(MaterialInboundPageParam param);

	/**
	 * 获取材料入库列表.
	 * @Param param 材料入库查询条件
	 * @Return 材料入库信息列表
	 */
	List<MaterialInboundModel> list(MaterialInboundListParam param);

	/**
	 * 批量新增材料入库.
	 * @Param params 材料入库信息列表
	 * @Return 批量新增材料入库是否成功
	 */
	Boolean addBatch(List<MaterialInboundParam> params);

    /***
     * @Description 获取入库单号
     * @author huangyongtao
     * @date 2025/9/23 15:50
     */
    MaterialInboundModel getInboundNo();
}
