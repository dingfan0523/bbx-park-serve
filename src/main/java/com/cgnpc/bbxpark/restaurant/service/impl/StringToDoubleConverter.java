package com.cgnpc.bbxpark.restaurant.service.impl;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

/**
 * @author huangyongtao
 * @Description 自定义转换器：处理各种格式的数字转 Double
 * @date 2026/3/9 10:29
 *
 */
@Slf4j
public class StringToDoubleConverter implements Converter<Double> {

    @Override
    public Class<?> supportJavaTypeKey() {
        return Double.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return null; // 兼容字符串类型的数字
    }

    @Override
    public Double convertToJavaData(ReadCellData<?> cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) {
        BigDecimal cellValue = null;
        try {

            // 1. 处理 NUMBER 类型单元格（Excel 数字格式）
            if (CellDataTypeEnum.NUMBER.equals(cellData.getType())) {
                cellValue = cellData.getNumberValue();
            }
            // 2. 处理 STRING 类型单元格（文本格式数字、带符号/单位的数字）
            else if (CellDataTypeEnum.STRING.equals(cellData.getType())) {
                String strValue = cellData.getStringValue();
                // 1. 空值处理：返回 0.0 或 null（根据业务需求）
                if (strValue == null || strValue.trim().isEmpty() || "-".equals(cellValue) || "无".equals(cellValue)) {
                    cellValue = null;
                }
                if (strValue.equalsIgnoreCase("NaN") || strValue.equalsIgnoreCase("Infinity")) {
                    cellValue = null;
                } else {
                    // 清理字符串中的非数字内容（千分位、单位、特殊符号）
                    strValue = strValue.trim()
                            .replaceAll(",", "") // 去掉千分位：1,234 → 1234
                            .replaceAll("元", "") // 去掉金额单位
                            .replaceAll("￥", "")
                            .replaceAll("千克", "")
                            .replaceAll("[^0-9.-]", ""); // 只保留数字、小数点、负号
                    cellValue = new BigDecimal(strValue);
                }
            }
            // 3. 最终空值兜底（返回 0.0 或 null，根据业务需求调整）
            return cellValue == null ? 0.0 : cellValue.doubleValue();
        } catch (NumberFormatException e) {
            // 4. 解析失败时的兜底（返回 0.0 或抛出异常）
            log.info("单元格数据无法转为Double，内容：{}，行号：{}", cellValue, cellData.getRowIndex());
            return 0.0;
        }
    }
}
