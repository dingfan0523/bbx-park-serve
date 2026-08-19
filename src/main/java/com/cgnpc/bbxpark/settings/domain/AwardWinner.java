
package com.cgnpc.bbxpark.settings.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cgnpc.bbxpark.common.entity.BaseExEntity;
import lombok.Data;

import java.io.Serializable;

/***
 * @Description 获奖人信息数据模型实体
 * @author huangyongtao
 * @date 2025/11/12 16:41
 */
@Data
@TableName("bbx_award_winner")
public class AwardWinner extends BaseExEntity implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

	/**
	*评优评奖表id.
	**/
	private Long awardId;
	/**
	*获奖人姓名.
	**/
	private String winnerName;
	/**
	*获奖人姓名拼音.
	**/
	private String winnerNamePinyin;
	/**
	*获奖说明.
	**/
	private String remark;
	/**
	*获奖人照片.
	**/
	private String photoUrl;
	/**
	*顺序.
	**/
	private Integer sortNumber;
}