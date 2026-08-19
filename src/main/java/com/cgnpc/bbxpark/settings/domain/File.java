package com.cgnpc.bbxpark.settings.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cgnpc.bbxpark.common.entity.BaseExEntity;
import lombok.Data;

import java.io.Serializable;

/***
 * @Description 会议预约数据模型实体
 * @author huangyongtao
 * @date 2024/8/23 15:08
 */
@Data
@TableName("bbx_file")
public class File extends BaseExEntity implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

	/**
	 * 业务类型:1->智慧会议 3->ioc产品,10->本地会议信息,20->视频会议信息
	 */
	private Integer type;
	/**
	*业务id.
	**/
	private Long relatedId;
	/**
	*文件名称.
	**/
	private String name;
	/**
	*文件地址.
	**/
	private String url;
}
