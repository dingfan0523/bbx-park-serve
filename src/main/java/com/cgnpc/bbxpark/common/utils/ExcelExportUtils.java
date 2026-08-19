package com.cgnpc.bbxpark.common.utils;

import com.alibaba.excel.EasyExcel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

/**
 * @create zhaoshuo
 * @time 2025/2/14
 * @desc
 */
@Component
@Slf4j
public class ExcelExportUtils {
    /**
     * 导出 Excel 文件（双标题）
     * @param response   HttpServletResponse
     * @param fileName   文件名（无需后缀）
     * @param mainTitle  主标题（如“苍南基地会议考核统计表”）
     * @param dataList   数据列表
     * @param dataClass  数据模型类（如 DepartmentMeetingStats.class）
     */
    public static void exportExcelWithTwoHeaders(
            HttpServletResponse response,
            String fileName,
            String mainTitle,
            List<?> dataList,
            Class<?> dataClass) throws IOException {

        // 设置响应头
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String encodedFileName = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-Disposition", "attachment;filename=" + encodedFileName + ".xlsx");

        // 配置写入参数（数据从第三行开始）
        EasyExcel.write(response.getOutputStream(), null)
                .registerWriteHandler(new CustomHeaderHandler(mainTitle, dataClass))
                .registerWriteHandler(new ExcelStyleHandler())
                .autoCloseStream(true)
                .sheet("数据详情")
                .doWrite(dataList);
    }
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

}
