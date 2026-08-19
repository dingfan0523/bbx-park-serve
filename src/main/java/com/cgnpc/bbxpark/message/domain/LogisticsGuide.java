
package com.cgnpc.bbxpark.message.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cgnpc.bbxpark.common.entity.BaseExEntity;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/***
 * @Description 后勤指南数据模型实体
 * @author huangyongtao
 * @date 2024/8/23 15:13
 */
@Data
@TableName("bbx_logistics_guide")
public class LogisticsGuide extends BaseExEntity implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;
    /**
     * 标题
     **/
    private String title;
    /**
     * 摘要
     **/
    private String summary;
    /**
     * 内容
     **/
    private String content;
    /**
     * 发布者ID
     */
    private String publisherId;
    /**
     * 发布时间
     */
    private Date publishTime;
    /**
     * 排序号
     **/
    private Integer orderCode;
}
