
package com.cgnpc.bbxpark.settings.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cgnpc.bbxpark.settings.domain.DocumentViewRecords;
import com.cgnpc.bbxpark.settings.dto.model.DocumentViewRecordsModel;
import com.cgnpc.bbxpark.settings.dto.param.DocumentViewRecordsListParam;
import com.cgnpc.bbxpark.settings.dto.param.DocumentViewRecordsParam;

import java.util.List;



public interface IDocumentViewRecordsService extends IService<DocumentViewRecords> {



	/**
	 * 获取文档查看记录列表.
	 * @Param param 文档查看记录查询条件
	 * @Return 文档查看记录信息列表
	 */
	List<DocumentViewRecordsModel> list(DocumentViewRecordsListParam param);

	/**
	 * 新增文档查看记录.
	 * @Param param 文档查看记录信息
	 * @Return 新增文档查看记录是否成功
	 */
	Boolean add(DocumentViewRecordsParam param);


}
