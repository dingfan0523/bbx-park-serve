package com.cgnpc.bbxpark.workbench.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;


@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(description = "cud用户信息")
@TableName("cud_userinfo")
public class CudUserinfo extends Model<CudUserinfo> {

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 主题颜色
     */
    private String themeColor;

    /**
     * 界面放大(%)
     */
    private Integer interfaceZoom;

    /**
     * 页面宽度 responsive(自适应) fixed(固定宽度)
     */
    private String pageWidthMode;

    /**
     * 背景类型: color(颜色背景), image(图片背景)
     */
    @TableField(value = "\"type\"")
    private String type;

    private String bgColor;

    /**
     * 图片地址
     */
    private String imageUrl;

    /**
     * 图片模糊(%)
     */
    private Integer imageBlurLevel;

    /**
     * 图片蒙版颜色
     */
    private String imageMaskShape;

    /**
     * 图片蒙版(%)
     */
    private Integer imageMaskShapeLevel;

    /**
     * 快捷菜单设置
     */
    private String quickMenuSettings;

    /**
     * 头部设置
     */
    private String headerSettings;

    /**
     * 卡片间距 (px)
     */
    private Integer spacing;

    /**
     * 卡片圆角(px)
     */
    private Integer cornerRadius;

    /**
     * compact(紧凑), standard(标准), loose(宽松)
     */
    private String layout;

    /**
     * 斑马线
     */
    private Boolean zebraStriping;

    /**
     * 纵向分割线
     */
    private Boolean verticalBorders;

    /**
     * onHover(悬停展开), fixed(固定常驻)
     */
    private String actionButtonDisplay;

    /**
     * 按钮高度(px)
     */
    private Integer height;

    /**
     * 按钮圆角(px)
     */
    private Integer borderRadius;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 创建时间
     */
    private Date createDate;
    private String fontSize;
}
