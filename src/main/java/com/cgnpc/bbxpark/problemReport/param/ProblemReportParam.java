package com.cgnpc.bbxpark.problemReport.param;

import com.cgnpc.bbxpark.settings.domain.File;
import com.cgnpc.bbxpark.problemReport.domain.ProblemDevice;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @create zhaoshuo
 * @time 2025/3/21
 * @desc 报事报修入参类
 */
@Data
public class ProblemReportParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -4281780296969979158L;

    /**
     * 问题需求描述
     */
    @ApiModelProperty(value = "问题需求描述")
    @NotNull(message = "问题需求描述不能为空")
    private String problemDesc;

    /**
     * 问题类型：1报事报修
     */
    @ApiModelProperty(value = "问题类型：1报事报修")
    @NotNull(message = "问题类型不能为空")
    private Integer problemType;

    /**
     * 空间位置id
     */
    @ApiModelProperty(value = "空间位置id")
    @NotNull(message = "空间位置id不能为空")
    private Long spaceId;

    /**
     * 空间位置名称
     */
    @ApiModelProperty(value = "空间位置名称")
    private String spaceName;

    @ApiModelProperty(value = "图片列表")
    private List<File> fileList;

    @ApiModelProperty(value = "设备列表")
    private List<ProblemDevice> problemDeviceList;

    /**
     * 问题联系人
     */
    @ApiModelProperty(value = "问题联系人")
    private String problemContact;

    /**
     * 联系电话
     */
    @ApiModelProperty(value = "联系电话")
    private String contactPhone;
}
