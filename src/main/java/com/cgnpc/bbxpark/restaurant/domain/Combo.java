package com.cgnpc.bbxpark.restaurant.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cgnpc.bbxpark.common.entity.BaseExEntity;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;


@Data
@TableName("bbx_combo")
public class Combo extends BaseExEntity implements Serializable {

    /** 套餐名称 */
    private String name ;
    /** 套餐图片 */
    private String imageUrl ;
    /** 套餐类型(字典) */
    private String type ;
    /** 套餐价格 */
    private BigDecimal price ;
    /** 套餐描述 */
    private String description ;
    /** 状态(1->上架;0->下架) */
    private Integer status ;

}
