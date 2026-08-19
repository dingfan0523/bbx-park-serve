package com.cgnpc.bbxpark.property.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cgnpc.bbxpark.property.domain.MaterialRecord;
import com.cgnpc.bbxpark.property.dto.model.MaterialRecordModel;
import com.cgnpc.bbxpark.property.dto.param.MaterialRecordListParam;
import com.cgnpc.bbxpark.property.dto.param.MaterialRecordPageParam;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/***
 * @Description 材料记录服务接口
 * @author huangyongtao
 * @date 2025/9/22 16:16
 */
public interface IMaterialRecordService extends IService<MaterialRecord> {

	/**
	 * 根据材料记录标识获得材料记录详情信息.
	 * @Param [id] 材料记录标识
	 * @Return 材料记录详情信息
	 */
	MaterialRecordModel detail(Long id);

	/**
	 * 获取材料记录列表(分页).
	 * @Param param 材料记录查询条件
	 * @Return 材料记录信息列表（分页）
	 */
	IPage<MaterialRecordModel> page(MaterialRecordPageParam param);

	/**
	 * 获取材料记录列表.
	 * @Param param 材料记录查询条件
	 * @Return 材料记录信息列表
	 */
	List<MaterialRecordModel> list(MaterialRecordListParam param);


	/***
	 * @Description 材料记录导出
	 * @author huangyongtao
	 * @date 2025/9/23 15:39
	 * @param response
	 * @param param
	 */
	Boolean materialRecordEasyExport(HttpServletResponse response, MaterialRecordPageParam param);
}
