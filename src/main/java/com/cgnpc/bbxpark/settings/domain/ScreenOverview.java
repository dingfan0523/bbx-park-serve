
package com.cgnpc.bbxpark.settings.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cgnpc.bbxpark.common.entity.BaseExEntity;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("bbx_screen_overview")
public class ScreenOverview extends BaseExEntity implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

	/**
	*模块名称.
	**/
	private String modelName;
    /**
     * 模块类型
     */
    private String modelType;
	/**
	* 模块数据(json数据:{"key":"","order":"10"})
	**/
	private String modelData;
}