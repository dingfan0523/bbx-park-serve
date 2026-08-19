
package com.cgnpc.bbxpark.settings.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cgnpc.bbxpark.common.entity.BaseExEntity;
import lombok.Data;

import java.io.Serializable;

/***
 * @Description 关注人管理数据模型实体
 * @author huangyongtao
 * @date 2025/3/11 10:17
 */
@Data
@TableName("bbx_attention_manage")
public class AttentionManage extends BaseExEntity implements Serializable{
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

	/**
	*关注人名称.
	**/
	private String attentionUname;
	/**
	*关注人工号.
	**/
	private String attentionStaffid;

    private String  attentionUid;

}
