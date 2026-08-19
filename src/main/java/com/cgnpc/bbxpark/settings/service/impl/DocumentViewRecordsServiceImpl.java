
package com.cgnpc.bbxpark.settings.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cgnpc.bbxpark.settings.domain.DocumentViewRecords;
import com.cgnpc.bbxpark.settings.dto.model.DocumentViewRecordsModel;
import com.cgnpc.bbxpark.settings.dto.param.DocumentViewRecordsListParam;
import com.cgnpc.bbxpark.settings.dto.param.DocumentViewRecordsParam;
import com.cgnpc.bbxpark.settings.mapper.DocumentViewRecordsRepository;
import com.cgnpc.bbxpark.settings.service.IDocumentViewRecordsService;
import com.cgnpc.bbxpark.common.utils.BeanUtils;
import com.cgnpc.bbxpark.common.utils.WebFrameworkUtils;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.List;


@Service("documentViewRecordsService")
public class DocumentViewRecordsServiceImpl extends ServiceImpl<DocumentViewRecordsRepository, DocumentViewRecords> implements IDocumentViewRecordsService {



	/**
	 * 获取文档查看记录列表.
	 * @Param param 文档查看记录查询条件
	 * @Return 文档查看记录信息列表
	 */
	@Override
	@SneakyThrows
	public List<DocumentViewRecordsModel> list(DocumentViewRecordsListParam param) {
		String userId = WebFrameworkUtils.getHeaderUserId();
		List<DocumentViewRecords> viewRecords = list(Wrappers.<DocumentViewRecords>lambdaQuery().eq(ObjectUtil.isNotEmpty(param.getDocumentType()), DocumentViewRecords::getDocumentType, param.getDocumentType())
				.eq(ObjectUtil.isNotEmpty(userId), DocumentViewRecords::getUserId, userId));
		return BeanUtils.convertListTo(viewRecords, DocumentViewRecordsModel::new);
	}

	/**
	 * 新增文档查看记录.
	 * @Param param 文档查看记录信息
	 * @Return 新增文档查看记录是否成功
	 */
	@Override
	public Boolean add(DocumentViewRecordsParam param) {
		DocumentViewRecords documentViewRecords = BeanUtils.convertTo(param, DocumentViewRecords::new);
		documentViewRecords.setId(null);
		documentViewRecords.setTenantId(WebFrameworkUtils.getHeaderTenantId());
		documentViewRecords.setUserId(WebFrameworkUtils.getHeaderUserId());
		return save(documentViewRecords);
	}


}
