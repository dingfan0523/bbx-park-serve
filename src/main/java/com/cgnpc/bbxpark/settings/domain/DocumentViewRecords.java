
package com.cgnpc.bbxpark.settings.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cgnpc.bbxpark.common.entity.BaseExEntity;
import lombok.Data;

import java.io.Serializable;


@Data
@TableName("bbx_document_view_records")
public class DocumentViewRecords extends BaseExEntity implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -3812741718960690663L;

	/**
	*租户id.
	**/
	private Long tenantId;
	/**
	*用户id.
	**/
	private String userId;

	/**
	 *文档类型 userPrivacyAgree:用户隐私协议.
	 **/
	private String documentType;



}
