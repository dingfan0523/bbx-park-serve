package com.cgnpc.bbxpark.settings.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cgnpc.bbxpark.common.entity.BaseExEntity;
import lombok.Data;

import java.io.Serializable;

/***
 * @Description 文件日志数据模型实体
 * @author huangyongtao
 * @date 2024/8/23 15:08
 */
@Data
@TableName("bbx_file_log")
public class FileLog extends BaseExEntity implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

	/**
	*文件名称.
	**/
	private String name;
	/**
	 * 文件大小
	 */
	private Long size;
}
