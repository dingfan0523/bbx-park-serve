
package com.cgnpc.bbxpark.meeting.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.bbxpark.common.enums.*;
import com.cgnpc.bbxpark.common.utils.DateUtils;
import com.cgnpc.bbxpark.common.utils.EasyExcelUtils;
import com.cgnpc.bbxpark.common.utils.WebFrameworkUtils;
import com.cgnpc.bbxpark.meeting.dto.model.MeetingReserveDetailModel;
import com.cgnpc.bbxpark.meeting.dto.model.MeetingReserveModel;
import com.cgnpc.bbxpark.meeting.dto.model.MeetingSignModel;
import com.cgnpc.bbxpark.meeting.dto.param.MeetingReservePageParam;
import com.cgnpc.bbxpark.meeting.dto.param.MeetingSignPageParam;
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
import java.util.Arrays;
import java.util.List;


/***
 * @Description 会议导出
 * @author huangyongtao
 * @date 2024/9/25 11:36
 */
@Slf4j
@Service
public class MeetingReserveExportService {
    @Autowired
    private IMeetingReserveService meetingReserveService;
    @Autowired
    private IMeetingReserveSignService meetingReserveSignService;
    @Autowired
    private ITenantInfoService tenantInfoService;



    private static final float CELL_HEIGHT = 25;

    /***
     * @Description 会议列表导出
     * @author huangyongtao
     * @date 2024/9/25 11:43
     * @param response
     * @param param
     */
    public void meetingReserveExport(HttpServletResponse response, MeetingReservePageParam param) {
        try (XSSFWorkbook workbook = new XSSFWorkbook()){
            //生成excel
            TenantInfo tenantInfoModel = tenantInfoService.getById(WebFrameworkUtils.getHeaderTenantId());
            String title = tenantInfoModel.getName() + "会议列表";
            reserveDocument(workbook, param, title);
            EasyExcelUtils.downLoadExcel(title, response, workbook);
        } catch (Exception e) {
            log.error("会议列表下载失败", e);
        }
    }
    /***
     * @Description 会议签到列表导出
     * @author huangyongtao
     * @date 2024/9/25 11:43
     * @param response
     * @param param
     */
    public void meetingReserveSignExport(HttpServletResponse response, MeetingSignPageParam param) {
        try (XSSFWorkbook workbook = new XSSFWorkbook()){
            //生成excel
            String title = "会议签到表";
            reserveSignDocument(workbook, param, title);
            EasyExcelUtils.downLoadExcel(title, response, workbook);
        } catch (Exception e) {
            log.error("会议签到表下载失败", e);
        }
    }

    private void reserveSignDocument(XSSFWorkbook workbook, MeetingSignPageParam meetingSignPageParam, String title) {
        List<String> titles = Arrays.asList("签到人","签到时间","签到人部门","会前邀请","签到标识");
        //新建工作表
        XSSFSheet sheet = creatSheet(workbook);
        //表头
        creatHead(sheet, styleHead(workbook, HorizontalAlignment.CENTER),0,0, 0, titles.size() - 1, title);
        //内容说明
        creatHead(sheet, style(workbook, HorizontalAlignment.LEFT, false),1,1, 0, titles.size() - 1, handleReserveSignRemark(meetingSignPageParam));
        //标题
        creatTitleRow(sheet, style(workbook, HorizontalAlignment.LEFT, true), titles);
        meetingSignPageParam.setCurrent(1);
        meetingSignPageParam.setSize(Integer.MAX_VALUE);
        IPage<MeetingSignModel> pageResult =  meetingReserveSignService.page(meetingSignPageParam);
        if(CollectionUtil.isNotEmpty(pageResult.getRecords())){
            int row = 3;
            for(MeetingSignModel model : pageResult.getRecords()){
                XSSFRow row_3 = sheet.createRow(row);
                row_3.setHeightInPoints(CELL_HEIGHT);
                handleReserveSignRow(model, row_3, style(workbook, HorizontalAlignment.LEFT, false));
                row++;
            }
        }

    }

    private void reserveDocument(XSSFWorkbook workbook, MeetingReservePageParam meetingReservePageParam, String title) {
        List<String> titles = Arrays.asList("会议名称","会议状态","会议室名称","会议阶段","会议发起人","预约开始时间","预约结束时间","是否为无效会议","无效原因","无效操作人","实际开始时间","开始原因","实际结束时间","结束原因");
        //新建工作表
        XSSFSheet sheet = creatSheet(workbook);
        //表头
        creatHead(sheet, styleHead(workbook, HorizontalAlignment.CENTER),0,0, 0, titles.size() - 1, title);
        //内容说明
        creatHead(sheet, style(workbook, HorizontalAlignment.LEFT, false),1,1, 0, titles.size() - 1, handleReserveCondition(meetingReservePageParam));
        //标题
        creatTitleRow(sheet, style(workbook, HorizontalAlignment.LEFT, true), titles);
        meetingReservePageParam.setCurrent(1);
        meetingReservePageParam.setSize(Integer.MAX_VALUE);
        IPage<MeetingReserveModel> pageResult =  meetingReserveService.page(meetingReservePageParam);
        if(CollectionUtil.isNotEmpty(pageResult.getRecords())){
            int row = 3;
            for(MeetingReserveModel model : pageResult.getRecords()){
                XSSFRow row_3 = sheet.createRow(row);
                row_3.setHeightInPoints(CELL_HEIGHT);
                handleReserveRow(model, row_3, style(workbook, HorizontalAlignment.LEFT, false));
                row++;
            }
        }

    }

    private void creatTitleRow( XSSFSheet sheet, XSSFCellStyle style, List<String> titles){
        XSSFRow row_2 = sheet.createRow(2);
        row_2.setHeightInPoints(CELL_HEIGHT + 5);
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
    private void handleReserveRow(MeetingReserveModel model, XSSFRow row, XSSFCellStyle style){
        //会议名称
        XSSFCell cell_0= row.createCell(0);
        cell_0.setCellStyle(style);
        cell_0.setCellValue(ObjectUtil.isEmpty(model.getReserveName()) ? "" : model.getReserveName());
        //会议状态
        XSSFCell cell_1= row.createCell(1);
        cell_1.setCellStyle(style);
        cell_1.setCellValue(Integer.valueOf(1).equals(model.getCancelFlag()) ? "取消" : "正常");
        //会议室名称
        XSSFCell cell_2= row.createCell(2);
        cell_2.setCellStyle(style);
        cell_2.setCellValue(ObjectUtil.isEmpty(model.getRoomName()) ? "" : model.getRoomName());
        //会议阶段
        XSSFCell cell_3= row.createCell(3);
        cell_3.setCellStyle(style);
        cell_3.setCellValue(MeetingReserveStatusEnum.getName(model.getStatus()));
        //会议发起人
        XSSFCell cell_4= row.createCell(4);
        cell_4.setCellStyle(style);
        cell_4.setCellValue(ObjectUtil.isEmpty(model.getReserveUname()) ? "" : model.getReserveUname());
        //预约开始时间
        XSSFCell cell_5= row.createCell(5);
        cell_5.setCellStyle(style);
        cell_5.setCellValue(ObjectUtil.isEmpty(model.getStartTime()) ? "" : DateUtils.format(model.getStartTime(),"yyyy-MM-dd HH:mm:ss"));
        //预约结束时间
        XSSFCell cell_6= row.createCell(6);
        cell_6.setCellStyle(style);
        cell_6.setCellValue(ObjectUtil.isEmpty(model.getEndTime()) ? "" : DateUtils.format(model.getEndTime(),"yyyy-MM-dd HH:mm:ss"));
        //是否为无效会议
        XSSFCell cell_7= row.createCell(7);
        cell_7.setCellStyle(style);
        cell_7.setCellValue(Integer.valueOf(1).equals(model.getInValidFlag()) ? "是" : "否");
        //无效原因
        XSSFCell cell_8= row.createCell(8);
        cell_8.setCellStyle(style);
        cell_8.setCellValue(ObjectUtil.isEmpty(model.getOperateReason()) ? "" : model.getOperateReason());
        //无效操作人
        XSSFCell cell_9= row.createCell(9);
        cell_9.setCellStyle(style);
        cell_9.setCellValue(ObjectUtil.isEmpty(model.getOperateUname()) ? "" : model.getOperateUname());
        //实际开始时间
        XSSFCell cell_10= row.createCell(10);
        cell_10.setCellStyle(style);
        cell_10.setCellValue(ObjectUtil.isEmpty(model.getRealStartTime()) ? "" : DateUtils.format(model.getRealStartTime(),"yyyy-MM-dd HH:mm:ss"));
        //开始原因
        XSSFCell cell_11= row.createCell(11);
        cell_11.setCellStyle(style);
        cell_11.setCellValue(ObjectUtil.isEmpty(model.getRealStartType()) ? "" : MeetingReserveStartTypeEnum.getName(model.getRealStartType()));
        //实际结束时间
        XSSFCell cell_12= row.createCell(12);
        cell_12.setCellStyle(style);
        cell_12.setCellValue(ObjectUtil.isEmpty(model.getRealEndTime()) ? "" : DateUtils.format(model.getRealEndTime(),"yyyy-MM-dd HH:mm:ss"));
        //结束原因
        XSSFCell cell_13= row.createCell(13);
        cell_13.setCellStyle(style);
        cell_13.setCellValue(ObjectUtil.isEmpty(model.getRealEndType()) ? "" : MeetingReserveEndTypeEnum.getName(model.getRealEndType()));
    }
    private String handleReserveCondition(MeetingReservePageParam meetingReservePageParam){
        String remark = "内容说明（列表下载时的检索条件） 会议名称：reserveName，会议室名称：roomName，会议发起人：reserveUname，是否无效：inValid，会议阶段：status，会议时间：startTime 至 endTime";
        //会议名称
        if(ObjectUtil.isEmpty(meetingReservePageParam.getReserveName())){
            remark = remark.replace("reserveName", "无");
        }else{
            remark = remark.replace("reserveName", meetingReservePageParam.getReserveName());
        }
        //会议室名称
        if(ObjectUtil.isEmpty(meetingReservePageParam.getRoomName())){
            remark = remark.replace("roomName", "无");
        }else{
            remark = remark.replace("roomName", meetingReservePageParam.getRoomName());
        }
        //会议发起人
        if(ObjectUtil.isEmpty(meetingReservePageParam.getReserveUname())){
            remark = remark.replace("reserveUname", "无");
        }else{
            remark = remark.replace("reserveUname", meetingReservePageParam.getReserveUname());
        }
        //是否无效
        if(ObjectUtil.isEmpty(meetingReservePageParam.getInValidFlag())){
            remark = remark.replace("inValid", "无");
        }else{
            remark = remark.replace("inValid", meetingReservePageParam.getInValidFlag().equals(Integer.valueOf(1))? "是":"否");
        }
        //会议阶段
        if(ObjectUtil.isEmpty(meetingReservePageParam.getStatus())){
            remark = remark.replace("status", "无");
        }else{
            remark = remark.replace("status", MeetingReserveStatusEnum.getName(meetingReservePageParam.getStatus()));
        }
        //会议开始时间
        if(ObjectUtil.isEmpty(meetingReservePageParam.getStartTime())){
            remark = remark.replace("startTime", "无");
        }else{
            remark = remark.replace("startTime", DateUtils.format(meetingReservePageParam.getStartTime(),"yyyy-MM-dd"));
        }
        //会议结束时间
        if(ObjectUtil.isEmpty(meetingReservePageParam.getEndTime())){
            remark = remark.replace("endTime", "无");
        }else{
            remark = remark.replace("endTime", DateUtils.format(meetingReservePageParam.getEndTime(),"yyyy-MM-dd"));
        }
        return remark;
    }

    private void handleReserveSignRow(MeetingSignModel model, XSSFRow row, XSSFCellStyle style){
        //签到人
        XSSFCell cell_0= row.createCell(0);
        cell_0.setCellStyle(style);
        cell_0.setCellValue(ObjectUtil.isEmpty(model.getSignUname()) ? "" : model.getSignUname());
        //签到时间
        XSSFCell cell_1= row.createCell(1);
        cell_1.setCellStyle(style);
        cell_1.setCellValue(ObjectUtil.isEmpty(model.getSignTime()) ? "" : DateUtils.format(model.getSignTime(),"yyyy-MM-dd HH:mm:ss"));
        //签到人部门
        XSSFCell cell_2= row.createCell(2);
        cell_2.setCellStyle(style);
        cell_2.setCellValue(ObjectUtil.isEmpty(model.getSignDepartment()) ? "" : model.getSignDepartment());
        //会前邀请
        XSSFCell cell_3= row.createCell(3);
        cell_3.setCellStyle(style);
        cell_3.setCellValue(Integer.valueOf(Status.enabled.getKey()).equals(model.getInvited()) ? "是" : "否");
        //签到标识
        XSSFCell cell_4= row.createCell(4);
        cell_4.setCellStyle(style);
        cell_4.setCellValue(MeetingSignTypeEnum.getName(model.getType()));
    }

    private String handleReserveSignRemark(MeetingSignPageParam meetingSignPageParam){
        MeetingReserveDetailModel reserveDetailModel =  meetingReserveService.detail(meetingSignPageParam.getReserveId());
        String remark = "会议名称：reserveName \n实际会议时间：realStartTime 至 realEndTime";
        //会议名称
        if(ObjectUtil.isEmpty(reserveDetailModel.getReserveName())){
            remark = remark.replace("reserveName", "无");
        }else{
            remark = remark.replace("reserveName", reserveDetailModel.getReserveName());
        }
        //会议实际开始时间
        if(ObjectUtil.isEmpty(reserveDetailModel.getRealStartTime())){
            remark = remark.replace("realStartTime", "无");
        }else{
            remark = remark.replace("realStartTime", DateUtils.format(reserveDetailModel.getRealStartTime(),"yyyy-MM-dd HH:mm:ss"));
        }
        //会议结束时间
        if(ObjectUtil.isEmpty(reserveDetailModel.getRealEndTime())){
            remark = remark.replace("realEndTime", "无");
        }else{
            remark = remark.replace("realEndTime", DateUtils.format(reserveDetailModel.getRealEndTime(),"yyyy-MM-dd HH:mm:ss"));
        }
        return remark;
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
