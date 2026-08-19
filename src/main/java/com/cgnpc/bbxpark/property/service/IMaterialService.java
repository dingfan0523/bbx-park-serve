package com.cgnpc.bbxpark.property.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cgnpc.bbxpark.property.domain.Material;
import com.cgnpc.bbxpark.property.dto.model.MaterialModel;
import com.cgnpc.bbxpark.property.dto.param.MaterialListParam;
import com.cgnpc.bbxpark.property.dto.param.MaterialPageParam;
import com.cgnpc.bbxpark.property.dto.param.MaterialParam;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/***
 * @Description 材料服务接口
 * @author huangyongtao
 * @date 2025/9/22 16:14
 */
public interface IMaterialService extends IService<Material> {

	/**
	 * 根据材料标识获得材料详情信息.
	 * @Param [id] 材料标识
	 * @Return 材料详情信息
	 */
	MaterialModel detail(Long id);

	/**
	 * 获取材料列表(分页).
	 * @Param param 材料查询条件
	 * @Return 材料信息列表（分页）
	 */
	IPage<MaterialModel> page(MaterialPageParam param);

	/**
	 * 获取材料列表.
	 * @Param param 材料查询条件
	 * @Return 材料信息列表
	 */
	List<MaterialModel> list(MaterialListParam param);

	/**
	 * 新增材料.
	 * @Param param 材料信息
	 * @Return 新增材料是否成功
	 */
	Boolean add(MaterialParam param);

	/**
	 * 删除材料.
	 * @Param id 材料标识
	 * @Return 删除材料是否成功
	 */
	Boolean remove(Long id);

	/**
	 * 编辑材料信息.
	 * @Param param 材料信息
	 * @Return 编辑材料是否成功
	 */
	Boolean edit(MaterialParam param);

	/***
	 * @Description 更新库存状态
	 * @author huangyongtao
	 * @date 2025/9/24 9:44
	 * @param material
	 */
	void handelStockStatus(Material material);

	/***
	 * @Description 材料导出
	 * @author huangyongtao
	 * @date 2025/9/23 15:39
	 * @param response
	 * @param param
	 */
	Boolean materialEasyExport(HttpServletResponse response, MaterialPageParam param);

}
