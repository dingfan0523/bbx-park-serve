package com.cgnpc.bbxpark.workbench.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@ApiModel(value = "CudUserinfoDto")
@Data
public class CudUserinfoDto {

    @ApiModelProperty(value = "主题颜色",required = true)
    @NotNull(message = "主题颜色不能为空")
    private String themeColor;

    @ApiModelProperty(value = "界面放大值")
    private Integer interfaceZoom;

    @ApiModelProperty(value = "页面宽度 responsive(自适应) fixed(固定宽度)")
    private String pageWidthMode;

    @ApiModelProperty(value = "背景设置")
    private BackgroundSettings backgroundSettings;

    @ApiModelProperty(value = "头部设置")
    private String headerSettings;

    @ApiModelProperty(value = "字体大小")
    private String fontSize;

    private String userId;

    @Data
    public static class BackgroundSettings{
        private String type;
        private String imageUrl;
        private Integer imageBlurLevel;
        private String imageMaskShape;
        private Integer imageMaskShapeLevel;
        private String bgColor;
    }

    @ApiModelProperty(value = "快捷菜单设置")
    private String quickMenuSettings;

    @ApiModelProperty(value = "卡片设置")
    private CardSettings cardSettings;

    @Data
    public static class CardSettings{
        private Integer spacing;
        private Integer cornerRadius;
    }

    @ApiModelProperty(value = "表格设置")
    private TableSettings tableSettings;

    @Data
    public static class TableSettings{
        private String layout;
        private Boolean zebraStriping;
        private Boolean verticalBorders;
        private String actionButtonDisplay;
    }

    @ApiModelProperty(value = "按钮表单设置")
    private ButtonFormSettings buttonFormSettings;

    @Data
    public static class ButtonFormSettings{
        private Integer height;
        private Integer borderRadius;
    }
}
