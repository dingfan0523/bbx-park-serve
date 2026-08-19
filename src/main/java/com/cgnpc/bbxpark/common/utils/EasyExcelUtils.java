package com.cgnpc.bbxpark.common.utils;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import com.cgnpc.cud.core.exception.BaseException;
import org.apache.poi.hssf.usermodel.DVConstraint;
import org.apache.poi.hssf.usermodel.HSSFDataValidation;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFDataValidation;
import org.apache.poi.xssf.usermodel.XSSFDataValidationHelper;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

public class EasyExcelUtils {
    /**
     * excel 导出
     *
     * @param list           数据
     * @param title          标题
     * @param sheetName      sheet名称
     * @param pojoClass      pojo类型
     * @param fileName       文件名称
     * @param isCreateHeader 是否创建表头
     * @param response
     */
    public static void exportExcel(List<?> list, String title, String sheetName, Class<?> pojoClass, String fileName, boolean isCreateHeader, HttpServletResponse response) throws IOException {
        ExportParams exportParams = new ExportParams(title, sheetName, ExcelType.XSSF);
        exportParams.setCreateHeadRows(isCreateHeader);
        defaultExport(list, pojoClass, fileName, response, exportParams);
    }

    /**
     * excel 导出
     *
     * @param list      数据
     * @param title     标题
     * @param sheetName sheet名称
     * @param pojoClass pojo类型
     * @param fileName  文件名称
     * @param response
     */
    public static void exportExcel(List<?> list, String title, String sheetName, Class<?> pojoClass, String fileName, HttpServletResponse response) throws IOException {
        defaultExport(list, pojoClass, fileName, response, new ExportParams(title, sheetName, ExcelType.XSSF));
    }

    /**
     * excel 导出
     *
     * @param list         数据
     * @param pojoClass    pojo类型
     * @param fileName     文件名称
     * @param response
     * @param exportParams 导出参数
     */
    public static void exportExcel(List<?> list, Class<?> pojoClass, String fileName, ExportParams exportParams, HttpServletResponse response) throws IOException {
        defaultExport(list, pojoClass, fileName, response, exportParams);
    }

    /**
     * excel 导出
     *
     * @param list     数据
     * @param fileName 文件名称
     * @param response
     */
    public static void exportExcel(List<Map<String, Object>> list, String fileName, HttpServletResponse response) throws IOException {
        defaultExport(list, fileName, response);
    }

    /**
     * 默认的 excel 导出
     *
     * @param list         数据
     * @param pojoClass    pojo类型
     * @param fileName     文件名称
     * @param response
     * @param exportParams 导出参数
     */
    public static void defaultExport(List<?> list, Class<?> pojoClass, String fileName, HttpServletResponse response, ExportParams exportParams) throws IOException {
        Workbook workbook = ExcelExportUtil.exportExcel(exportParams, pojoClass, list);
        downLoadExcel(fileName, response, workbook);
    }

    /**
     * 默认的 excel 导出
     *
     * @param list     数据
     * @param fileName 文件名称
     * @param response
     */
    public static void defaultExport(List<Map<String, Object>> list, String fileName, HttpServletResponse response) throws IOException {
        Workbook workbook = ExcelExportUtil.exportExcel(list, ExcelType.HSSF);
        downLoadExcel(fileName, response, workbook);
    }

    /**
     * 下载
     *
     * @param fileName 文件名称
     * @param response
     * @param workbook excel数据
     */
    public static void downLoadExcel(String fileName, HttpServletResponse response, Workbook workbook) throws IOException {
        try {
            fileName = URLEncoder.encode(fileName+".xlsx", "UTF-8");
            response.reset();
            // fileName后面设置编码格式是重点
            response.setHeader("Content-disposition", "attachment;filename="+fileName+";"+"filename*=utf-8''"+fileName);
            response.setContentType("application/octet-stream; charset=UTF-8");
            workbook.write(response.getOutputStream());
        } catch (Exception e) {
            throw new IOException(e.getMessage());
        }
    }

    public static void downLoadExcel(String fileName, HttpServletResponse response,HttpServletRequest request, Workbook workbook) throws IOException {
        try {
            fileName = encodeFileName(fileName,request);
            response.reset();
            // 设置正确的Excel MIME类型
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("UTF-8");
            // 设置下载头
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
            // 禁用缓存
            response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
            workbook.write(response.getOutputStream());
            workbook.close();
        } catch (Exception e) {
            throw new IOException(e.getMessage());
        }
    }

    /**
     * 处理不同浏览器的文件名编码
     */
    private static String encodeFileName(String fileName, HttpServletRequest request)
            throws UnsupportedEncodingException {

        String userAgent = request.getHeader("User-Agent");
        String encodedFileName = fileName + ".xlsx";

        // Chrome, Edge, Safari, Opera
        if (userAgent.contains("Chrome") || userAgent.contains("Safari") ||
                userAgent.contains("Edge") || userAgent.contains("Opera")) {
            encodedFileName = URLEncoder.encode(fileName, "UTF-8") + ".xlsx";
            encodedFileName = encodedFileName.replace("+", "%20");
        }
        // Firefox
        else if (userAgent.contains("Firefox")) {
            encodedFileName = "=?UTF-8?B?" +
                    Base64.getEncoder().encodeToString(fileName.getBytes("UTF-8")) +
                    "?=.xlsx";
        }
        // IE
        else if (userAgent.contains("MSIE") || userAgent.contains("Trident")) {
            encodedFileName = URLEncoder.encode(fileName, "UTF-8") + ".xlsx";
        }

        return encodedFileName;
    }

    /**
     * excel 导入
     *
     * @param filePath   excel文件路径
     * @param titleRows  标题行
     * @param headerRows 表头行
     * @param pojoClass  pojo类型
     * @param <T>
     * @return
     */
    public static <T> List<T> importExcel(String filePath, Integer titleRows, Integer headerRows, Class<T> pojoClass) throws IOException {
        if (StringUtils.isBlank(filePath)) {
            return null;
        }
        ImportParams params = new ImportParams();
        params.setTitleRows(titleRows);
        params.setHeadRows(headerRows);
        params.setNeedSave(false);
        params.setSaveUrl("/excel/");
        try {
            return ExcelImportUtil.importExcel(new File(filePath), pojoClass, params);
        } catch (NoSuchElementException e) {
            throw new IOException("模板不能为空");
        } catch (Exception e) {
            throw new IOException(e.getMessage());
        }
    }

    /**
     * excel 导入
     *
     * @param file      excel文件
     * @param pojoClass pojo类型
     * @param <T>
     * @return
     */
    public static <T> List<T> importExcel(MultipartFile file, Class<T> pojoClass) throws IOException {
        return importExcel(file, 1, 1, pojoClass);
    }

    /**
     * excel 导入
     *
     * @param file       excel文件
     * @param titleRows  标题行
     * @param headerRows 表头行
     * @param pojoClass  pojo类型
     * @param <T>
     * @return
     */
    public static <T> List<T> importExcel(MultipartFile file, Integer titleRows, Integer headerRows, Class<T> pojoClass) throws IOException {
        return importExcel(file, titleRows, headerRows, false, pojoClass);
    }

    /**
     * excel 导入
     *
     * @param file       上传的文件
     * @param titleRows  标题行
     * @param headerRows 表头行
     * @param needVerfiy 是否检验excel内容
     * @param pojoClass  pojo类型
     * @param <T>
     * @return
     */
    public static <T> List<T> importExcel(MultipartFile file, Integer titleRows, Integer headerRows, boolean needVerfiy, Class<T> pojoClass) throws IOException {
        if (file == null) {
            return null;
        }
        try {
            return importExcel(file.getInputStream(), titleRows, headerRows, needVerfiy, pojoClass);
        } catch (Exception e) {
//            throw GenericException.fail(e.getMessage(),e);
            throw new BaseException(e.getMessage());
        }
    }

    /**
     * excel 导入
     *
     * @param inputStream 文件输入流
     * @param titleRows   标题行
     * @param headerRows  表头行
     * @param needVerfiy  是否检验excel内容
     * @param pojoClass   pojo类型
     * @param <T>
     * @return
     */
    public static <T> List<T> importExcel(InputStream inputStream, Integer titleRows, Integer headerRows, boolean needVerfiy, Class<T> pojoClass) throws IOException {
        if (inputStream == null) {
            return null;
        }
        ImportParams params = new ImportParams();
        params.setTitleRows(titleRows);
        params.setHeadRows(headerRows);
        params.setSaveUrl("/home/zallds/excel/");
        params.setNeedSave(false);
        //params.setNeedVerfiy(needVerfiy);
        try {
            return ExcelImportUtil.importExcel(inputStream, pojoClass, params);
        } catch (NoSuchElementException e) {
//            throw GenericException.fail("excel文件不能为空",e);
            throw new BaseException("excel文件不能为空");
        } catch (Exception e) {
//            throw GenericException.fail(e.getMessage(),e);
            throw new BaseException(e.getMessage());
        }
    }

    /**
     * Excel 类型枚举
     */
    enum ExcelTypeEnum {
        XLS("xls"),
        XLSX("xlsx");
        private String value;

        ExcelTypeEnum(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }



    /**
     * @Description:    导出Excle，对指定列进行限定内容
     * @param firstRow    开始行号(下标0开始)
     * @param lastRow     结束行号，最大65535
     * @param firstCol    区域中第一个单元格的列号 (下标0开始)
     * @param lastCol     区域中最后一个单元格的列号
     * @param dataArray   下拉内容
     * @param sheetHidden 影藏的sheet编号（例如1,2,3），多个下拉数据不能使用同一个
     * @see <a href="http://poi.apache.org/components/spreadsheet/quick-guide.html#Validation"> POI官网</a>
     */
    public static void excelList(Workbook workbook, int firstRow, int lastRow, int firstCol, int lastCol, Object[] dataArray, int sheetHidden) {

        // 创建一个隐藏的sheet,用来存放下拉框数据
        String hiddenName = "hidden_" + (int) ((Math.random() * 9 + 1) * 100);
        Sheet sheet = workbook.getSheetAt(0);
        Sheet hidden = workbook.createSheet(hiddenName);
        Cell cell = null;
        for (int i = 0, length = dataArray.length; i < length; i++) {
            Object name = dataArray[i];
            Row row = hidden.createRow(i);
            cell = row.createCell(0);
            cell.setCellValue(name.toString());
        }

        Name namedCell = workbook.createName();
        namedCell.setNameName(hiddenName);

        // Excel下拉框引用公式
        namedCell.setRefersToFormula(hiddenName + "!$A$1:$A$" + dataArray.length);
        //加载数据,将名称为hidden的
        DVConstraint constraint = DVConstraint.createFormulaListConstraint(hiddenName);

        //  导出XLS
        // 设置数据有效性加载在哪个单元格上,四个参数分别是：起始行、终止行、起始列、终止列
        CellRangeAddressList addressList = new CellRangeAddressList(firstRow, lastRow, firstCol, lastCol);
        HSSFDataValidation validation = new HSSFDataValidation(addressList, constraint);


        // XLSX
        XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper((XSSFSheet) sheet);
        DataValidationConstraint dvConstraint = dvHelper.createFormulaListConstraint(hiddenName);
        dvConstraint.setOperator(0);
        CellRangeAddressList xssfAddressList = new CellRangeAddressList(firstRow, lastRow, firstCol, lastCol);
        XSSFDataValidation xssfDataValidation = (XSSFDataValidation) dvHelper.createValidation(
                dvConstraint, xssfAddressList);
        // 是否显示错误提示框(XSSFDataValidation默认为false,HSSFDataValidation默认为true)
        xssfDataValidation.setShowErrorBox(true);
        // 错误提示框的提示内容
        xssfDataValidation.createErrorBox("数据异常", "请进行修改");
        // 将sheet设置为隐藏
        workbook.setSheetHidden(sheetHidden, true);
        sheet.addValidationData(xssfDataValidation);
    }

    public static void excel(Workbook workbook, int firstRow, int lastRow, int firstCol, int lastCol, String[] dataArray, int sheetHidden,String hiddenName) {

        // 创建一个隐藏的sheet,用来存放下拉框数据
        Sheet hidden = workbook.createSheet(hiddenName);
        Sheet sheet = workbook.getSheetAt(0);
        Cell cell = null;
        for (int i = 0, length = dataArray.length; i < length; i++) {
            Object name = dataArray[i];
            Row row = hidden.createRow(i);
            cell = row.createCell(0);
            cell.setCellValue(name.toString());
            Font font = workbook.createFont();
            font.setFontHeightInPoints((short) 14);
            CellStyle style = workbook.createCellStyle();
            style.setFont(font);
            cell.setCellStyle(style);

        }
        Name namedCell = workbook.createName();
        namedCell.setNameName(hiddenName);

        // Excel下拉框引用公式
//        namedCell.setRefersToFormula(hiddenName + "!$A$1:$A$" + dataArray.length);
        //加载数据,将名称为hidden的
        DVConstraint constraint = DVConstraint.createFormulaListConstraint(hiddenName);

        //  导出XLS
        // 设置数据有效性加载在哪个单元格上,四个参数分别是：起始行、终止行、起始列、终止列
        CellRangeAddressList addressList = new CellRangeAddressList(firstRow, lastRow, firstCol, lastCol);
        HSSFDataValidation validation = new HSSFDataValidation(addressList, constraint);


        // XLSX
        XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper((XSSFSheet) sheet);
        DataValidationConstraint dvConstraint = dvHelper.createFormulaListConstraint(hiddenName);
        dvConstraint.setOperator(0);
        CellRangeAddressList xssfAddressList = new CellRangeAddressList(firstRow, lastRow, firstCol, lastCol);
        XSSFDataValidation xssfDataValidation = (XSSFDataValidation) dvHelper.createValidation(
                dvConstraint, xssfAddressList);
        // 是否显示错误提示框(XSSFDataValidation默认为false,HSSFDataValidation默认为true)
        xssfDataValidation.setShowErrorBox(true);
        // 错误提示框的提示内容
        xssfDataValidation.createErrorBox("数据异常", "请进行修改");
        // 将sheet设置为隐藏
        workbook.setSheetHidden(sheetHidden, false);

/*
        CellRangeAddress cellAddresses = new CellRangeAddress(firstRow, lastRow, firstCol, lastCol);
        sheet.addMergedRegion(cellAddresses);*/
        sheet.addValidationData(xssfDataValidation);
    }

    public static void select(Workbook workbook, int firstRow, int lastRow, int firstCol, int lastCol, String[] dataArray, int sheetHidden) {

        // 创建一个隐藏的sheet,用来存放下拉框数据
        String hiddenName = "hidden_" + (int) ((Math.random() * 9 + 1) * 100);
        Sheet sheet = workbook.getSheetAt(0);
        Sheet hidden = workbook.createSheet(hiddenName);
        Cell cell = null;
        for (int i = 0, length = dataArray.length; i < length; i++) {
            String name = dataArray[i];
            Row row = hidden.createRow(i);
            cell = row.createCell(0);
            cell.setCellValue(name);
        }

        Name namedCell = workbook.createName();
        namedCell.setNameName(hiddenName);

        // Excel下拉框引用公式
        namedCell.setRefersToFormula(hiddenName + "!$A$1:$A$" + dataArray.length);
        //加载数据,将名称为hidden的
        DVConstraint constraint = DVConstraint.createFormulaListConstraint(hiddenName);

        //  导出XLS
        // 设置数据有效性加载在哪个单元格上,四个参数分别是：起始行、终止行、起始列、终止列
        CellRangeAddressList addressList = new CellRangeAddressList(firstRow, lastRow, firstCol, lastCol);
        HSSFDataValidation validation = new HSSFDataValidation(addressList, constraint);


        // XLSX
        XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper((XSSFSheet) sheet);
        DataValidationConstraint dvConstraint = dvHelper.createFormulaListConstraint(hiddenName);
        dvConstraint.setOperator(0);
        CellRangeAddressList xssfAddressList = new CellRangeAddressList(firstRow, lastRow, firstCol, lastCol);
        XSSFDataValidation xssfDataValidation = (XSSFDataValidation) dvHelper.createValidation(
                dvConstraint, xssfAddressList);
        // 将sheet设置为隐藏
        workbook.setSheetHidden(sheetHidden, true);
        sheet.addValidationData(xssfDataValidation);
    }

    public static void selects(Workbook workbook, int firstRow, int lastRow, int firstCol, int lastCol, String[] dataArray, int sheetHidden) {
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < dataArray.length; i++){
            sb. append(dataArray[i]);
        }
        String hiddenName = sb.toString();
        // 创建一个隐藏的sheet,用来存放下拉框数据
        Sheet sheet = workbook.getSheetAt(0);
        Sheet hidden = workbook.createSheet(hiddenName);
        Cell cell = null;
        for (int i = 0, length = dataArray.length; i < length; i++) {
            String name = dataArray[i];
            Row row = hidden.createRow(i);
            cell = row.createCell(0);
            cell.setCellValue(name);
        }

        Name namedCell = workbook.createName();
        namedCell.setNameName(hiddenName);

        // Excel下拉框引用公式
        namedCell.setRefersToFormula(hiddenName + "!$A$1:$A$" + dataArray.length);
        //加载数据,将名称为hidden的
        DVConstraint constraint = DVConstraint.createFormulaListConstraint(hiddenName);

        //  导出XLS
        // 设置数据有效性加载在哪个单元格上,四个参数分别是：起始行、终止行、起始列、终止列
        CellRangeAddressList addressList = new CellRangeAddressList(firstRow, lastRow, firstCol, lastCol);
        HSSFDataValidation validation = new HSSFDataValidation(addressList, constraint);


        // XLSX
        XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper((XSSFSheet) sheet);
        DataValidationConstraint dvConstraint = dvHelper.createFormulaListConstraint(hiddenName);
        dvConstraint.setOperator(0);
        CellRangeAddressList xssfAddressList = new CellRangeAddressList(firstRow, lastRow, firstCol, lastCol);
        XSSFDataValidation xssfDataValidation = (XSSFDataValidation) dvHelper.createValidation(
                dvConstraint, xssfAddressList);
        // 将sheet设置为隐藏
        workbook.setSheetHidden(sheetHidden, true);
        sheet.addValidationData(xssfDataValidation);
    }




}
