package com.cgnpc.bbxpark.common.utils;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.write.handler.SheetWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @create zhaoshuo
 * @time 2025/2/13
 * @desc 初始化Excel标题
 */
public class CustomHeaderHandler implements SheetWriteHandler {
    private String mainTitle; // 动态主标题
    private Class<?> dataClass; // 数据模型类（用于反射获取列名）

    private static final String FONT_NAME = "宋体";

    public CustomHeaderHandler(String mainTitle, Class<?> dataClass) {
        this.mainTitle = mainTitle;
        this.dataClass = dataClass;
    }

    @Override
    public void beforeSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {
    }

    @Override
    public void afterSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {
        Sheet sheet = writeSheetHolder.getSheet();
        Workbook workbook = writeWorkbookHolder.getWorkbook();
        // -------------------- 第一行：动态主标题（合并所有列）--------------------
        Row mainTitleRow = sheet.createRow(0);
        Cell mainTitleCell = mainTitleRow.createCell(0);
        mainTitleCell.setCellValue(mainTitle);
        // 设置主标题样式
        CellStyle mainTitleStyle = createMainTitleStyle(workbook);
        // 获取列数（根据实体类字段数）
        int columnCount = getColumnCount();
        for (int col = 0; col <= columnCount - 1; col++) {
            Cell cell = sheet.getRow(0).getCell(col);
            if (cell == null) {
                cell = sheet.getRow(0).createCell(col);
            }
            cell.setCellStyle(mainTitleStyle);
//            列宽
            sheet.setColumnWidth(col, 20 * 256);
        }
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, columnCount - 1));

        // -------------------- 第二行：字段标题（从实体类注解获取）--------------------
        Row columnTitleRow = sheet.createRow(1);
        List<String> columnTitles = getColumnTitles();

        for (int i = 0; i < columnTitles.size(); i++) {
            Cell cell = columnTitleRow.createCell(i);
            cell.setCellValue(columnTitles.get(i));

            // 设置列标题样式
            CellStyle columnTitleStyle = createColumnTitleStyle(workbook);
            cell.setCellStyle(columnTitleStyle);
        }
        writeSheetHolder.setRelativeHeadRowIndex(2); // 数据起始行号为 2（即第三行）
    }

    // 获取列数（根据实体类中@ExcelProperty注解的字段数）
    private int getColumnCount() {
        return (int) Arrays.stream(dataClass.getDeclaredFields())
                .filter(f -> f.isAnnotationPresent(ExcelProperty.class))
                .count();
    }

    // 获取排序后的列标题列表
    private List<String> getColumnTitles() {
        return Arrays.stream(dataClass.getDeclaredFields())
                .filter(f -> f.isAnnotationPresent(ExcelProperty.class))
                .sorted(Comparator.comparingInt(f -> f.getAnnotation(ExcelProperty.class).index()))
                .map(f -> f.getAnnotation(ExcelProperty.class).value()[0])
                .collect(Collectors.toList());
    }

    // 主标题样式（加粗、居中、大字体）
    private CellStyle createMainTitleStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 16);
        font.setFontName(FONT_NAME);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        return style;
    }

    // 列标题样式（加粗、居中）
    private CellStyle createColumnTitleStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 12);
        font.setFontName(FONT_NAME);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        return style;
    }
}
