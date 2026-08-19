
package com.cgnpc.bbxpark.meeting.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.bbxpark.common.utils.DateUtils;
import com.cgnpc.bbxpark.common.utils.EasyExcelUtils;
import com.cgnpc.bbxpark.common.utils.ExcelExportUtils;
import com.cgnpc.bbxpark.common.utils.WebFrameworkUtils;
import com.cgnpc.bbxpark.meeting.dto.model.MeetingAttendantEvaluateDetailModel;
import com.cgnpc.bbxpark.meeting.dto.model.MeetingAttendantTaskPersonCountModel;
import com.cgnpc.bbxpark.meeting.dto.param.MeetingAttendantCountPageParam;
import com.cgnpc.bbxpark.meeting.dto.param.MeetingAttendantTaskCountDetailPageParam;
import com.cgnpc.bbxpark.space.domain.TenantInfo;
import com.cgnpc.bbxpark.space.service.ITenantInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;


/***
 * @Description 会服考核导出
 * @author huangyongtao
 * @date 2025/2/13 11:15
 */
@Slf4j
@Service
public class MeetingAttendantTaskCountExportService {
    @Autowired
    private IMeetingAttendantTaskCountService meetingAttendantTaskCountService;
    @Autowired
    private ITenantInfoService tenantInfoService;

    private static final float CELL_HEIGHT = 25;

    /**
     * 导出 Excel 文件
     *
     * @param response HttpServletResponse
     * @param fileName 文件名（无需后缀）
     * @param dataList 数据列表
     * @param clazz    数据模型类
     */
    public static void exportExcel(HttpServletResponse response, String fileName,
                                   List<?> dataList, Class<?> clazz, String title){
        try {
            // 设置响应头
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            String encodedFileName = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-Disposition", "attachment;filename=" + encodedFileName + ".xlsx");

            // 2. 导出 Excel
            ExcelExportUtils.exportExcelWithTwoHeaders(
                    response,
                    fileName,
                    title,
                    dataList,
                    clazz
            );
        }catch (Exception e){
            log.error("{}导出失败",fileName, e);
        }
    }


    /***
     * @Description 服考核统计表导出(Apache poi)
     * @author huangyongtao
     * @date 2025/2/13 10:59
     * @param response
     * @param param
     */
    public void attendantTaskCountExport(HttpServletResponse response, MeetingAttendantCountPageParam param) {
        try (XSSFWorkbook workbook = new XSSFWorkbook()){
            //生成excel
            TenantInfo tenantInfoModel = tenantInfoService.getById(WebFrameworkUtils.getHeaderTenantId());
            String title = tenantInfoModel.getName() + "会服考核统计表";
            attendantTaskCountDocument(workbook, param, title);
            EasyExcelUtils.downLoadExcel(title, response, workbook);
        } catch (Exception e) {
            log.error("会服考核统计表下载失败", e);
        }
    }

    /***
     * @Description 服考核统计表导出(easyExcel)
     * @author huangyongtao
     * @date 2025/2/18 10:59
     * @param response
     * @param param
     */
    public void attendantTaskCountEasyExport(HttpServletResponse response, MeetingAttendantCountPageParam param) {
        //生成excel
        TenantInfo tenantInfoModel = tenantInfoService.getById(WebFrameworkUtils.getHeaderTenantId());
        String title = tenantInfoModel.getName() + "会服考核统计表";
        param.setCurrent(1);
        param.setSize(Integer.MAX_VALUE);
        IPage<MeetingAttendantTaskPersonCountModel> pageResult =  meetingAttendantTaskCountService.attendantPage(param);
        if(CollectionUtil.isNotEmpty(pageResult.getRecords())){
            pageResult.getRecords().forEach(model->{
                model.setUserShowName(ObjectUtil.isEmpty(model.getUserName()) ? "" : model.getUserName());
                model.setRoomShowName(CollectionUtil.isEmpty(model.getRoomNameList()) ? "" : String.join(",", model.getRoomNameList()));
            });
        }
        exportExcel(response, title, pageResult.getRecords(), MeetingAttendantTaskPersonCountModel.class, title);
    }
   /***
    * @Description 会服评价详情表导出(Apache poi)
    * @author huangyongtao
    * @date 2025/2/13 11:00
    * @param response
    * @param param
    */
    public void attendantScoreDetailExport(HttpServletResponse response, MeetingAttendantTaskCountDetailPageParam param) {
        try (XSSFWorkbook workbook = new XSSFWorkbook()){
            TenantInfo tenantInfoModel = tenantInfoService.getById(WebFrameworkUtils.getHeaderTenantId());
            //生成excel
            String title = tenantInfoModel.getName() +  "会服评价详情表";
            attendantScoreDetailDocument(workbook, param, title);
            EasyExcelUtils.downLoadExcel(title, response, workbook);
        } catch (Exception e) {
            log.error("会服评价详情表下载失败", e);
        }
    }

    /***
     * @Description 会服评价详情表导出(easyExcel)
     * @author huangyongtao
     * @date 2025/2/13 10:59
     * @param response
     * @param param
     */
    public void attendantScoreDetailEasyExport(HttpServletResponse response, MeetingAttendantTaskCountDetailPageParam param) {
        //生成excel
        TenantInfo tenantInfoModel = tenantInfoService.getById(WebFrameworkUtils.getHeaderTenantId());
        String title = tenantInfoModel.getName() + "会服评价详情表";
        param.setCurrent(1);
        param.setSize(Integer.MAX_VALUE);
        IPage<MeetingAttendantEvaluateDetailModel> pageResult =  meetingAttendantTaskCountService.scoreDetailPage(param);
        if(CollectionUtil.isNotEmpty(pageResult.getRecords())){
            pageResult.getRecords().forEach(model->{
                model.setHandleShowName(ObjectUtil.isEmpty(model.getHandleUname()) ? "" : model.getHandleUname());
                model.setOperateShowName(ObjectUtil.isEmpty(model.getOperateUname()) ? "" : model.getOperateUname());
                model.setCreateTimeStr(ObjectUtil.isEmpty(model.getCreateTime()) ? "" : DateUtils.format(model.getCreateTime(),"yyyy-MM-dd HH:mm:ss"));
            });
        }
        exportExcel(response, title, pageResult.getRecords(), MeetingAttendantEvaluateDetailModel.class, title);
    }

    private void attendantScoreDetailDocument(XSSFWorkbook workbook, MeetingAttendantTaskCountDetailPageParam param, String title) {
        List<String> titles = Arrays.asList("会服人员姓名","评价分数","会议室","会议名称","评价内容","评价人", "评价时间");
        //新建工作表
        XSSFSheet sheet = creatSheet(workbook);
        //表头
        creatHead(sheet, styleHead(workbook, HorizontalAlignment.CENTER),0,0, 0, titles.size() - 1, title);
        //标题
        creatTitleRow(sheet, style(workbook, HorizontalAlignment.CENTER, true), titles);
        param.setCurrent(1);
        param.setSize(Integer.MAX_VALUE);
        IPage<MeetingAttendantEvaluateDetailModel> pageResult =  meetingAttendantTaskCountService.scoreDetailPage(param);
        if(CollectionUtil.isNotEmpty(pageResult.getRecords())){
            int row = 2;
            for(MeetingAttendantEvaluateDetailModel model : pageResult.getRecords()){
                XSSFRow row_3 = sheet.createRow(row);
                row_3.setHeightInPoints(CELL_HEIGHT);
                handleAttendantScoreDetailRow(model, row_3, style(workbook, HorizontalAlignment.CENTER, false));
                row++;
            }
        }

    }

    private void attendantTaskCountDocument(XSSFWorkbook workbook, MeetingAttendantCountPageParam param, String title) {
        List<String> titles = Arrays.asList("会服人员名称","服务会议室","服务次数","服务会议人数","平均分数");
        //新建工作表
        XSSFSheet sheet = creatSheet(workbook);
        //表头
        creatHead(sheet, styleHead(workbook, HorizontalAlignment.CENTER),0,0, 0, titles.size() - 1, title);
        //标题
        creatTitleRow(sheet, style(workbook, HorizontalAlignment.CENTER, true), titles);
        param.setCurrent(1);
        param.setSize(Integer.MAX_VALUE);
        IPage<MeetingAttendantTaskPersonCountModel> pageResult =  meetingAttendantTaskCountService.attendantPage(param);
        if(CollectionUtil.isNotEmpty(pageResult.getRecords())){
            int row = 2;
            for(MeetingAttendantTaskPersonCountModel model : pageResult.getRecords()){
                XSSFRow row_3 = sheet.createRow(row);
                row_3.setHeightInPoints(CELL_HEIGHT);
                handleAttendantTaskCountRow(model, row_3, style(workbook, HorizontalAlignment.CENTER, false));
                row++;
            }
        }

    }

    private void creatTitleRow( XSSFSheet sheet, XSSFCellStyle style, List<String> titles){
        XSSFRow row_2 = sheet.createRow(1);
        row_2.setHeightInPoints(CELL_HEIGHT + 10);
        for(int i = 0; i < titles.size(); i++){
            XSSFCell cell_2_0 = row_2.createCell(i);
            cell_2_0.setCellStyle(style);
            cell_2_0.setCellValue(titles.get(i));
        }
    }

    private XSSFSheet creatSheet(XSSFWorkbook workbook){
        //新建工作表
        XSSFSheet sheet = workbook.createSheet();
        //单元格列宽
        sheet.setDefaultColumnWidth(20);
        //单元格行高
        sheet.setDefaultRowHeightInPoints(CELL_HEIGHT);
        return sheet;
    }
    private void handleAttendantTaskCountRow(MeetingAttendantTaskPersonCountModel model, XSSFRow row, XSSFCellStyle style){
        //会服人员名称
        XSSFCell cell_0= row.createCell(0);
        cell_0.setCellStyle(style);
        cell_0.setCellValue(ObjectUtil.isEmpty(model.getUserName()) ? "" : model.getStaffid() + " " + model.getUserName());
        //服务会议室
        XSSFCell cell_1= row.createCell(1);
        cell_1.setCellStyle(style);
        cell_1.setCellValue(CollectionUtil.isEmpty(model.getRoomNameList()) ? "" : String.join(",", model.getRoomNameList()));
        //服务次数
        XSSFCell cell_2= row.createCell(2);
        cell_2.setCellStyle(style);
        cell_2.setCellValue(ObjectUtil.isEmpty(model.getAttendantTaskNum()) ? "" : model.getAttendantTaskNum() + "");
        //服务会议人数
        XSSFCell cell_3= row.createCell(3);
        cell_3.setCellStyle(style);
        cell_3.setCellValue(ObjectUtil.isEmpty(model.getPersonNum()) ? "" : model.getPersonNum() + "");
        //评价分数
        XSSFCell cell_4= row.createCell(4);
        cell_4.setCellStyle(style);
        cell_4.setCellValue(ObjectUtil.isEmpty(model.getAverageScore()) ? "" : model.getAverageScore());
    }

    private void handleAttendantScoreDetailRow(MeetingAttendantEvaluateDetailModel model, XSSFRow row, XSSFCellStyle style){
        //会服人姓名
        XSSFCell cell_0= row.createCell(0);
        cell_0.setCellStyle(style);
        cell_0.setCellValue(ObjectUtil.isEmpty(model.getHandleUname()) ? "" : model.getHandleStaffid() + " " + model.getHandleUname());
        //评价分数
        XSSFCell cell_1= row.createCell(1);
        cell_1.setCellStyle(style);
        cell_1.setCellValue(ObjectUtil.isEmpty(model.getScore()) ? "" : model.getScore() + "");
        //会议室
        XSSFCell cell_2= row.createCell(2);
        cell_2.setCellStyle(style);
        cell_2.setCellValue(ObjectUtil.isEmpty(model.getRoomName()) ? "" : model.getRoomName());
        //会议名称
        XSSFCell cell_3= row.createCell(3);
        cell_3.setCellStyle(style);
        cell_3.setCellValue(ObjectUtil.isEmpty(model.getReserveName()) ? "" : model.getReserveName());
        //评价内容
        XSSFCell cell_4= row.createCell(4);
        cell_4.setCellStyle(style);
        cell_4.setCellValue(ObjectUtil.isEmpty(model.getContent()) ? "" : model.getContent());
        //评价人
        XSSFCell cell_5= row.createCell(5);
        cell_5.setCellStyle(style);
        cell_5.setCellValue(ObjectUtil.isEmpty(model.getOperateUname()) ? "" : model.getOperateStaffid() + " " + model.getOperateUname());
        //评价时间
        XSSFCell cell_6= row.createCell(6);
        cell_6.setCellStyle(style);
        cell_6.setCellValue(ObjectUtil.isEmpty(model.getCreateTime()) ? "" : DateUtils.format(model.getCreateTime(),"yyyy-MM-dd HH:mm:ss"));
    }

    private void creatHead(XSSFSheet sheet, XSSFCellStyle style, int firstRow, int lastRow, int firstCol, int lastCol, String content){
        //表头
        XSSFRow row_0 = sheet.createRow(firstRow);
        //单元格行高
        row_0.setHeightInPoints(CELL_HEIGHT + 5);
        XSSFCell cell_0_0 = row_0.createCell(firstCol);
        cell_0_0.setCellValue(content);
        cell_0_0.setCellStyle(style);
        for(int i = 1; i <= lastCol; i++){
            XSSFCell cell_0_1 = row_0.createCell(i);
            cell_0_1.setCellValue("");
            cell_0_1.setCellStyle(style);
        }
        //合并单元格
        CellRangeAddress cra = new CellRangeAddress(firstRow, lastRow, firstCol, lastCol);
        sheet.addMergedRegion(cra);
    }

    /***
     * @Description 获取字体
     * @author huangyongtao
     * @date 2024/7/23 13:59
     */
    private XSSFFont getFont(XSSFWorkbook workbook){
        //创建字体
        XSSFFont font = workbook.createFont();
        //设置字体类型
        font.setFontName("宋体");
        //设置字号
        font.setFontHeight(11);
        //设置字体颜色
        font.setColor(IndexedColors.BLACK.index);
        return font;
    }


    private XSSFCellStyle createCommonCellStyle(XSSFWorkbook workbook, HorizontalAlignment align){
        //单元格样式
        XSSFCellStyle style = workbook.createCellStyle();
        style.setWrapText(true);
        style.setAlignment(align);// 文字水平居中
        style.setVerticalAlignment(VerticalAlignment.CENTER);// 文字垂直居中
        //设置边框样式
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }
    /**
     *
     * @Description 样式设置(居中,字号变大)
     * @author huangyt@dtinsure.com
     * @since V1.0
     */
    private XSSFCellStyle styleHead(XSSFWorkbook workbook, HorizontalAlignment align){
        //单元格样式
        XSSFCellStyle style = createCommonCellStyle(workbook, align);
        //创建字体
        XSSFFont font = getFont(workbook);
        //设置字体是否加粗
        font.setBold(true);
        //设置字号
        font.setFontHeight(14);
        //将字体加入样式
        style.setFont(font);
        return style;
    }


    /**
     *
     * @Description 样式设置
     * @author huangyt@dtinsure.com
     * @since V1.0
     */
    private XSSFCellStyle style(XSSFWorkbook workbook, HorizontalAlignment align, Boolean bold){
        //单元格样式
        XSSFCellStyle style =  createCommonCellStyle(workbook, align);
        //创建字体
        XSSFFont font = getFont(workbook);
        //设置字体是否加粗
        font.setBold(bold);
        //将字体加入样式
        style.setFont(font);
        return style;
    }
}
