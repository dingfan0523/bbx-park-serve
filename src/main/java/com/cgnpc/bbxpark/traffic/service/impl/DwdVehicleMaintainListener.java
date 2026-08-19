package com.cgnpc.bbxpark.traffic.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.cgnpc.bbxpark.traffic.dto.DwdVehicleMaintainParam;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class DwdVehicleMaintainListener extends AnalysisEventListener<DwdVehicleMaintainParam> {

    private List<String> timeTypes;

    // 表头行号（和代码中headRowNumber一致，比如3）
    private static final int HEAD_ROW_NUM = 0;

    @Getter
    private List<DwdVehicleMaintainParam> addList = new ArrayList<>();

    /**
     * 解析校验数据
     *
     * @Param
     * @Return
     */
    @Override
    public void invoke(DwdVehicleMaintainParam param, AnalysisContext analysisContext) {
        // 获取当前解析行的索引（0开始）
        int currentRowIndex = analysisContext.readRowHolder().getRowIndex();
        // 只处理表头行之后的真实数据行
        if (currentRowIndex <= HEAD_ROW_NUM) {
            return; // 跳过表头行/标题行
        }
        try {
            if (allFieldsNull(param)) {
                return;
            }
            if(ObjectUtil.isEmpty(param.getPlateNum())){
                return;
            }
        } catch (Exception e) {
            log.info("车辆保养信息第{}行解析失败：{}", currentRowIndex + 1 , e.getMessage());
        }
        addList.add(param);
    }


    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {

    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
    }

    /**
     * 数字格式校验
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        return str.matches("^[0-9]+(\\.[0-9]+)?$");
    }


    public static boolean allFieldsNull(Object obj) throws Exception {
        if (obj == null) {
            return true;
        }
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true); // 使私有字段也可以访问
            Object fieldValue = field.get(obj);
            if (fieldValue != null) {
                return false;
            }
        }
        return true;
    }

}
