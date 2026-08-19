
package com.cgnpc.bbxpark.meeting.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cgnpc.bbxpark.common.entity.BaseExEntity;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/***
 * @Description 会议预约签到数据模型实体
 * @author huangyongtao
 * @date 2024/8/23 15:16
 */
@Data
@TableName("bbx_meeting_reserve_sign")
public class MeetingReserveSign extends BaseExEntity implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;
	/**
	*会议预约id.
	**/
	private Long reserveId;
	/**
	 * 会前邀请:1->是;0->否
	 */
	private Integer invited;
	/**
	 * 签到类型:1->本人签到;2->补签;3->代签到;4->代补签;5->未签到
	 */
	private Integer type;
	/**
	*签到人id.
	**/
	private String signUid;
	/**
	*签到人名称.
	**/
	private String signUname;
	/**
	*签到人工号.
	**/
	private String signStaffid;
	/**
	 * 签到人部门
	 */
	private String signDepartment;
	/**
	*签到时间.
	**/
	private Date signTime;
	/**
	 *操作人id.
	 **/
	private String operateUid;
	/**
	 *操作人名称.
	 **/
	private String operateUname;
	/**
	 *操作人工号.
	 **/
	private String operateStaffid;
}
