
package com.cgnpc.bbxpark.meeting.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cgnpc.bbxpark.common.entity.BaseExEntity;
import lombok.Data;

import java.io.Serializable;

/***
 * @Description 会议室数据模型实体
 * @author huangyongtao
 * @date 2024/8/23 15:13
 */
@Data
@TableName("bbx_meeting_room")
public class MeetingRoom extends BaseExEntity implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;
    /**
     * 第三方会议室id.
     **/
    private String thirdRoomId;
    /**
     * 片区id.
     **/
    private String areaId;
    /**
     * 片区位置.
     **/
    private String areaName;
    /**
     * 空间位置id.
     **/
    private Long spaceId;
    /**
     * 会议室名称.
     **/
    private String roomName;
    /**
     * 会议室容量.
     **/
    private Integer roomVolume;
    /**
     * 提醒内容
     **/
    private String warnContent;
    /**
     * 会议室图片.
     **/
    private String imageUrl;
    /**
     * 是否清扫(1->是;0->否)
     **/
    private Integer swept;
    /**
     * 是否可用(1->是;0->否)
     **/
    private Integer validFlag;
    /**
     * 呼叫中(1->是;0->否)
     **/
    private Integer calling;
    /**
     * 排座(1->支持;0->不支持)
     */
    private Integer rowSeat;
    /**
     * 打印(1->支持;0->不支持)
     */
    private Integer print;
    /**
     * 来源(add->本系统内新增;sync->集团会议系统同步)
     */
    private String source;
    /**
     * 会议室类型(video->视频)
     */
    private String roomType;
    /**
     * 使用中(1->是;0->否)
     **/
    private Integer used;


}
