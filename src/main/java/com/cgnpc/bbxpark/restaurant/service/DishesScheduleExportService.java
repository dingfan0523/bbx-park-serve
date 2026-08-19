
package com.cgnpc.bbxpark.restaurant.service;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.cgnpc.bbxpark.common.enums.Delete;
import com.cgnpc.bbxpark.common.utils.EasyExcelUtils;
import com.cgnpc.bbxpark.restaurant.domain.MealLine;
import com.cgnpc.bbxpark.restaurant.domain.Restaurant;
import com.cgnpc.bbxpark.restaurant.dto.model.RestaurantTimeModel;
import com.cgnpc.bbxpark.restaurant.dto.param.MealLineImportParam;
import com.cgnpc.bbxpark.restaurant.dto.param.RestaurantTimeListParam;
import com.cgnpc.bbxpark.restaurant.mapper.MealLineRepository;
import com.cgnpc.bbxpark.restaurant.mapper.RestaurantRepository;
import com.cgnpc.bbxpark.settings.dto.model.DictItemModel;
import com.cgnpc.bbxpark.settings.service.impl.DictServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


/***
 * @Description 导出产品周排模版
 * @author huangyongtao
 * @date 2024/7/23 9:42
 */
@Slf4j
@Service
public class DishesScheduleExportService {
    /**
     * 字典查询feign
     */
    @Autowired
    private DictServiceImpl dictService;
    @Autowired
    private IRestaurantTimeService restaurantTimeService;
    @Autowired
    private RestaurantRepository restaurantRepository;
    @Autowired
    private MealLineRepository mealLineRepository;

    private static final float CELL_HEIGHT = 25;

    public void templateGenerate(Long restaurantId,HttpServletResponse response, HttpServletRequest request) {
        try (XSSFWorkbook workbook = new XSSFWorkbook()){
            //获取入参
            String params = request.getParameter("params");
            Restaurant restaurant = restaurantRepository.selectById(restaurantId);
            List<MealLine> mealLines = mealLineRepository.selectList(Wrappers.<MealLine>lambdaQuery().eq(MealLine::getRestaurantId,restaurantId).eq(MealLine::getDeleted, Delete.NORMAL.getKey()));
            //入参转换成对象
            List<MealLineImportParam> mealLineFrontParams = mealLines.stream().map(mealLine -> {
                MealLineImportParam param = new MealLineImportParam();
                param.setRestaurantId(restaurantId);
                param.setRestaurantName(restaurant.getName());
                param.setMealLineId(mealLine.getId());
                param.setMealLineName(mealLine.getName());
                return param;
            }).collect(Collectors.toList());
            //生成模版
            exportDocument(workbook, mealLineFrontParams);
            EasyExcelUtils.downLoadExcel("菜品周排导入模板", response, workbook);
        } catch (Exception e) {
            log.error("菜品周排模板下载失败", e);
        }
    }

    public void exportDocument(XSSFWorkbook workbook, List<MealLineImportParam> mealLineFrontParams) {
        for(MealLineImportParam param :  mealLineFrontParams){
            //新建工作表
            XSSFSheet sheet = workbook.createSheet(param.getMealLineName());
            //单元格列宽
            sheet.setDefaultColumnWidth(20);
//            sheet.setColumnWidth(0, 15 * 256);
            //单元格行高
            sheet.setDefaultRowHeightInPoints(CELL_HEIGHT);
            //表头
            XSSFRow row_0 = sheet.createRow(0);
            //单元格行高
            row_0.setHeightInPoints(CELL_HEIGHT + 5);
            XSSFCell cell_0_0 = row_0.createCell(0);
            cell_0_0.setCellValue(param.getRestaurantName());
            cell_0_0.setCellStyle(styleHead(workbook));
            for(int i = 1; i <= 7; i++){
                XSSFCell cell_0_1 = row_0.createCell(i);
                cell_0_1.setCellValue("");
                cell_0_1.setCellStyle(style(workbook));
            }
            //合并单元格
            CellRangeAddress cra = new CellRangeAddress(0, 0, 0, 7);
            sheet.addMergedRegion(cra);

            //标题
            XSSFRow row_1 = sheet.createRow(1);
            //单元格行高
            row_1.setHeightInPoints(CELL_HEIGHT);
            List<String> titles = Arrays.asList("*出品日期","*用餐时间","*菜品名称","*类别","原料信息","克重（g）","*单价（元）","辣度建议（0-5）");
            for(int i = 0; i < titles.size(); i++){
                //设置sheet的默认样式
                sheet.setDefaultColumnStyle(i, style(workbook));
                XSSFCell cell_1_0 = row_1.createCell(i);
                cell_1_0.setCellStyle(style_bold(workbook));
                if(titles.get(i).contains("*")){
                    RichTextString str = new XSSFRichTextString(titles.get(i));
                    str.applyFont(0, 1, getFont(workbook, IndexedColors.RED.index));
                    str.applyFont(1, titles.get(i).length(), getFont(workbook, IndexedColors.BLACK.index));
                    cell_1_0.setCellValue(str);
                }else{
                    cell_1_0.setCellValue(titles.get(i));
                }
            }
            //获取辣度字典
            List<String> foodSpicyList = Objects.requireNonNull(dictService.findItemsByDictType("foodSpicy"))
                    .stream().map(DictItemModel::getLabel).collect(Collectors.toList());
            if(CollectionUtil.isNotEmpty(foodSpicyList)){
                effectivenessSelectData(sheet, 7, foodSpicyList);
            }
            //获取餐品类别字典
            List<String> foodCategoryList = Objects.requireNonNull(dictService.findItemsByDictType("foodCategory")).stream().map(DictItemModel::getLabel).collect(Collectors.toList());
            if(CollectionUtil.isNotEmpty(foodCategoryList)){
                effectivenessSelectData(sheet, 3, foodCategoryList);
            }
            //通过餐厅id查询用餐类型配置
            Long restaurantId = mealLineFrontParams.get(0).getRestaurantId();
            RestaurantTimeListParam timeListParam = new RestaurantTimeListParam();
            param.setRestaurantId(restaurantId);
            List<String> timeTypes = restaurantTimeService.timeList(timeListParam).stream().map(RestaurantTimeModel::getType).collect(Collectors.toList());
            //获取营业时间字典
            List<String> restaurantTimeList = Objects.requireNonNull(dictService.findItemsByDictType("restaurantTime"))
                    .stream().filter(p->timeTypes.contains(p.getCode())).map(DictItemModel::getLabel).collect(Collectors.toList());

            if(CollectionUtil.isNotEmpty(restaurantTimeList)){
                effectivenessSelectData(sheet, 1, restaurantTimeList);
            }
        }
    }

    /***
     * @Description 设置下拉框数据
     * @author huangyongtao
     * @date 2024/7/23 16:23
     */
    private void effectivenessSelectData(XSSFSheet sheet, Integer columnIndex, List<String> selectDateList) {
        //创建一个数据验证帮助器
        XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper(sheet);
        //创建一个下拉列表
        DataValidationConstraint provConstraint = dvHelper.createExplicitListConstraint(selectDateList.toArray(new String[0]));
        //设置数据验证的单元格区域: 四个参数分别是起始行、终止行、起始列、终止列
        CellRangeAddressList proRangeAddressList = new CellRangeAddressList(2, 1002, columnIndex, columnIndex);
        //创建数据验证对象
        DataValidation provinceDataValidation = dvHelper.createValidation(provConstraint, proRangeAddressList);
        //创建错误提示
        provinceDataValidation.createErrorBox("错误提示", "您输入的内容，不符合限制条件");
        //显示错误提示
        provinceDataValidation.setShowErrorBox(true);
        //显示下拉箭头
        provinceDataValidation.setSuppressDropDownArrow(true);
        //将数据验证添加到sheet中
        sheet.addValidationData(provinceDataValidation);

    }


    /***
     * @Description 日期格式校验，输入日期为yyyy-MM-dd格式
     * @author huangyongtao
     * @date 2024/7/23 14:33
     */
    private void setDateFormatValidation(XSSFSheet sheet, int columnIndex) {
        DataValidationHelper dvHelper = new XSSFDataValidationHelper(sheet);
        // 设置整列的数据验证，确保日期大于等于1900-01-01
        DataValidationConstraint dateConstraint = dvHelper.createDateConstraint(
                DataValidationConstraint.OperatorType.GREATER_OR_EQUAL,
                "1900-01-01",
                null,
                "yyyy-MM-dd"
        );
        CellRangeAddressList dateRange = new CellRangeAddressList(2, 1048575, columnIndex, columnIndex);
        DataValidation dateValidation = dvHelper.createValidation(dateConstraint, dateRange);
        // 设置错误消息
        dateValidation.createErrorBox("日期无效", "请输入格式为 yyyy-MM-dd 且大于等于 1900-01-01 的有效日期。");
        // 显示错误提示框并抑制下拉箭头
        dateValidation.setShowErrorBox(true);
        dateValidation.setSuppressDropDownArrow(true);
        // 将验证添加到工作表
        sheet.addValidationData(dateValidation);
    }

    /***
     * @Description 获取字体
     * @author huangyongtao
     * @date 2024/7/23 13:59
     */
    private XSSFFont getFont(XSSFWorkbook workbook, short colorIndex){
        //创建字体
        XSSFFont font_red = workbook.createFont();
        //设置字体类型
        font_red.setFontName("宋体");
        //设置字体是否加粗
        font_red.setBold(true);
        //设置字号
        font_red.setFontHeight(11);
        //设置字体颜色
        font_red.setColor(colorIndex);
        return font_red;
    }

    /**
     *
     * @Description 样式设置(居中,加粗,字号变大,背景色)
     * @author huangyt@dtinsure.com
     * @since V1.0
     */
    private XSSFCellStyle styleHead(XSSFWorkbook workbook){
        //单元格样式
        XSSFCellStyle style = workbook.createCellStyle();
        style.setWrapText(true);
        style.setAlignment(HorizontalAlignment.CENTER);// 文字水平居中
        style.setVerticalAlignment(VerticalAlignment.CENTER);// 文字垂直居中
        //设置边框样式
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        // 设置边框颜色
//        style.setBottomBorderColor(IndexedColors.BLACK.index);
//        style.setTopBorderColor(IndexedColors.BLACK.index);
//        style.setLeftBorderColor(IndexedColors.BLACK.index);
//        style.setRightBorderColor(IndexedColors.BLACK.index);
        //设置前景颜色
        style.setFillForegroundColor(IndexedColors.SEA_GREEN.getIndex());
        //设置颜色填充规则
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        //创建字体
        XSSFFont font = workbook.createFont();
        //设置字体类型
        font.setFontName("宋体");
        //设置字体是否加粗
        font.setBold(true);
        //设置字体是否倾斜
//        font.setItalic(true);
        //设置字号
        font.setFontHeight(14);
        //设置字体颜色
        font.setColor(IndexedColors.BLACK.index);
        //将字体加入样式
        style.setFont(font);
        return style;
    }


    /**
     *
     * @Description 样式设置(居中)
     * @author huangyt@dtinsure.com
     * @since V1.0
     */
    private XSSFCellStyle style(XSSFWorkbook workbook){
        //单元格样式
        XSSFCellStyle style = workbook.createCellStyle();
        //自动换行-  不设置行高的时候，自动换行的行高才能自适应
        style.setWrapText(true);
        style.setAlignment(HorizontalAlignment.CENTER);// 文字水平居中
        style.setVerticalAlignment(VerticalAlignment.CENTER);// 文字垂直居中
        //设置边框样式
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        //创建字体
        XSSFFont font = workbook.createFont();
        //设置字体类型
        font.setFontName("宋体");
        //设置字号
        font.setFontHeight(11);
        //设置字体颜色
        font.setColor(IndexedColors.BLACK.index);
        //将字体加入样式
        style.setFont(font);
        return style;
    }
    /**
     *
     * @Description 样式设置(居中,加粗)
     * @author huangyt@dtinsure.com
     * @since V1.0
     */
    private XSSFCellStyle style_bold(XSSFWorkbook workbook){
        //单元格样式
        XSSFCellStyle style = workbook.createCellStyle();
        style.setWrapText(true);
        style.setAlignment(HorizontalAlignment.CENTER);// 文字水平居中
        style.setVerticalAlignment(VerticalAlignment.CENTER);// 文字垂直居中
        //设置边框样式
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        //创建字体
        XSSFFont font = workbook.createFont();
        //设置字体类型
        font.setFontName("宋体");
        //设置字体是否加粗
        font.setBold(true);
        //设置字号
        font.setFontHeight(11);
        //设置字体颜色
        font.setColor(IndexedColors.BLACK.index);
        //将字体加入样式
        style.setFont(font);
        return style;
    }
}
