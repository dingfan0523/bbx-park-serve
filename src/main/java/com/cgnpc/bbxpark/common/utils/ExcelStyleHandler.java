package com.cgnpc.bbxpark.common.utils;

import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.AbstractVerticalCellStyleStrategy;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;

/**
 * @author huangyongtao
 * @Description easyExel设置样式策略
 * @date 2025/2/18 14:13
 */
public class ExcelStyleHandler extends AbstractVerticalCellStyleStrategy  {

    private static final String FONT_NAME = "宋体";

    @Override
    protected WriteCellStyle contentCellStyle(Head head) {
        // 内容的策略
        WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
        //自动换行
//        contentWriteCellStyle.setWrapped(true);
        // 水平居中
        contentWriteCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        // 垂直居中
        contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        // 背景白色
        contentWriteCellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        // 边框
        contentWriteCellStyle.setBorderLeft(BorderStyle.THIN);
        contentWriteCellStyle.setBorderTop(BorderStyle.THIN);
        contentWriteCellStyle.setBorderRight(BorderStyle.THIN);
        contentWriteCellStyle.setBorderBottom(BorderStyle.THIN);
        WriteFont contentWriteFont = new WriteFont();
        // 字体大小
        contentWriteFont.setFontHeightInPoints((short) 11);
        // 字体样式
        contentWriteFont.setFontName(FONT_NAME);
        contentWriteCellStyle.setWriteFont(contentWriteFont);
        return contentWriteCellStyle;
    }
}
