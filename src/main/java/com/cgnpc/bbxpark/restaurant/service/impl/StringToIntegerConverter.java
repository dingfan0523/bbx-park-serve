package com.cgnpc.bbxpark.restaurant.service.impl;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import lombok.extern.slf4j.Slf4j;

/**
 * @author huangyongtao
 * @Description 自定义转换器：Excel单元格内容 → Integer（兼容各种非标准格式）
 * @date 2026/3/9 10:36
 *
 */
@Slf4j
public class StringToIntegerConverter implements Converter<Integer> {

    @Override
    public Class<?> supportJavaTypeKey() {
        // 支持的Java类型：Integer
        return Integer.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        // 支持Excel单元格类型：字符串（覆盖绝大多数场景）
        return CellDataTypeEnum.STRING;
    }

    @Override
    public Integer convertToJavaData(ReadCellData<?> cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) {
        // 1. 获取单元格原始内容并去空格
        String cellValue = cellData.getStringValue();
        if (cellValue == null) {
            return 0; // 空值返回0，也可返回null（根据业务需求调整）
        }
        String cleanValue = cellValue.trim();

        // 2. 处理空/特殊占位符
        if (cleanValue.isEmpty() || "-".equals(cleanValue) || "无".equals(cleanValue) || "0.0".equals(cleanValue)) {
            return 0;
        }

        try {
            // 3. 移除千分位逗号、非数字字符（如次数后的"次"）
            cleanValue = cleanValue.replaceAll(",", "") // 去掉千分位：1,234 → 1234
                    .replaceAll("[^0-9.]", ""); // 只保留数字和小数点

            // 4. 处理小数（如5.9 → 5，或四舍五入为6，按需调整）
            if (cleanValue.contains(".")) {
                // 方案1：直接取整（舍弃小数部分）
                cleanValue = cleanValue.split("\\.")[0];
                // 方案2：四舍五入（如需保留小数后四舍五入，打开下面注释）
                // double doubleValue = Double.parseDouble(cleanValue);
                // return (int) Math.round(doubleValue);
            }

            // 5. 转换为Integer
            return Integer.parseInt(cleanValue);
        } catch (NumberFormatException e) {
            // 6. 解析失败兜底（打印日志+返回默认值，避免解析中断）
            log.info("单元格数据转换Integer失败，内容：{}，行号：{}", cellValue, cellData.getRowIndex());
            return 0;
        }
    }
}
