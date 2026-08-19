
package com.cgnpc.bbxpark.supplier.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cgnpc.bbxpark.common.entity.BaseExEntity;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/***
 * @Description 服务商数据模型实体
 * @author huangyongtao
 * @date 2025/11/14 11:00
 */
@Data
@TableName("bbx_supplier")
public class Supplier extends BaseExEntity implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

	/**
	*服务商名称.
	**/
	private String name;
    /**
     *服务商名称拼音.
     **/
    private String pinyin;
	/**
	*服务商类型（1->集成商；2->销售方；3->供货方；4->运维服务商）.
	**/
	private Integer type;
    /**
     * 进驻时间.
     **/
    private Date occupancyDate;
	/**
	*所在地区.
	**/
	private String region;
	/**
	*详细地址.
	**/
	private String address;
	/**
	*服务商描述.
	**/
	private String remark;

}