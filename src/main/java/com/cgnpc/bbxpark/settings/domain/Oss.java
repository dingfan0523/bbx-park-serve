package com.cgnpc.bbxpark.settings.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cgnpc.bbxpark.common.entity.BaseExEntity;
import lombok.Data;

import java.io.Serializable;


@Data
@TableName("sys_oss")
public class Oss extends BaseExEntity implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

	/**
	*文件名.
	**/
	private String fileName;
	/**
	*文件后缀名.
	**/
	private String fileSuffix;
	/**
	*原名.
	**/
	private String originalName;
	/**
	*状态（1正常 0停用）.
	**/
	private Integer status = 1;
	/**
	*URL地址.
	**/
	private String url;
}
