
package com.cgnpc.bbxpark.settings.dto.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
public class OssParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -4827649026888870972L;

//    @NotNull(groups = UpdateGroup.class)
    @ApiModelProperty(value = "对象存储主键.")
    private Long ossId;

//    @NotBlank(groups = InsertGroup.class)
    @Length(max = 255)
    @ApiModelProperty(value = "文件名.")
    private String fileName;

//    @NotBlank(groups = InsertGroup.class)
    @Length(max = 255)
    @ApiModelProperty(value = "原名.")
    private String originalName;

//    @NotBlank(groups = InsertGroup.class)
    @Length(max = 10)
    @ApiModelProperty(value = "文件后缀名.")
    private String fileSuffix;

//    @NotBlank(groups = InsertGroup.class)
    @Length(max = 500)
    @ApiModelProperty(value = "URL地址.")
    private String url;

    @ApiModelProperty(value = "状态（0正常 1停用）.")
    private Integer status = 0;

    @ApiModelProperty(value = "对象存储主键集合.")
    private List<Long> ossIds;

}
