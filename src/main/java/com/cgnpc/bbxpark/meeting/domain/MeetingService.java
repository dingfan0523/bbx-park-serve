
package com.cgnpc.bbxpark.meeting.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cgnpc.bbxpark.common.entity.BaseExEntity;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/***
 * @Description 会议室数据模型实体
 * @author huangyongtao
 * @date 2024/8/23 15:13
 */
@Data
@TableName("bbx_meeting_service")
public class MeetingService extends BaseExEntity implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;
    /**
     * 会服名称
     **/
    private String name;
    /**
     * 标准
     **/
    private String standard;
    /**
     * 提醒
     **/
    private String warn;
    /**
     * 说明
     **/
    private String instructions;
    /**
     * 价格
     */
    private BigDecimal price;
    /**
     * 是否通用/自助(1->是;0->否)
     */
    private Integer common;
    /**
     * 乐观锁
     **/
    private Integer revision;
}
