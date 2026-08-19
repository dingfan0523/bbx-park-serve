
package com.cgnpc.bbxpark.complaint.dto.param;

import com.cgnpc.bbxpark.common.web.CudPageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/***
 * @Description 投诉建议主表;分页参数模型
 * @author huangyongtao
 * @date 2024/7/12 16:30
 */
@Data
public class ComplaintSuggestionPageParam extends CudPageDto implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "主键id.")
    private Long id;

    @ApiModelProperty(value = "状态;10：待回复；20：已分配（待处理）；30：待审核；40：审核驳回；50：已完成；60：已评价")
    private Integer status;

    @ApiModelProperty(value = "状态集合;状态;10：待回复；20：已分配（待处理）；30：待审核；40：审核驳回；50：已完成；60：已评价")
    private List<Integer> statusList;

    @ApiModelProperty(value = "提交人工号.")
    private String submitStaffid;

    @ApiModelProperty(value = "提交时间.")
    private Date submitTime;

    @ApiModelProperty(value = "提交开始时间")
    private Date submitStartTime;

    @ApiModelProperty(value = "提交结束时间")
    private Date submitEndTime;

    @ApiModelProperty(value = "提交人id.")
    private String submitUid;

    @ApiModelProperty(value = "提交人名称.")
    private String submitUname;

    @ApiModelProperty(value = "类型;complaint：投诉；suggestion：建议.")
    private String type;

    @ApiModelProperty(value = "主键id集合.")
    private List<Long> ids;

    @ApiModelProperty(value = "园区ID-租户号.")
    private Long tenantId;

    @ApiModelProperty(value = "回复人id.")
    private String replyUid;

    @ApiModelProperty(value = "标题.")
    private String title;

}
