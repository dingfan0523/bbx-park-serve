package com.cgnpc.bbxpark.restaurant.service;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.cgnpc.bbxpark.settings.dto.model.DictItemModel;
import com.cgnpc.bbxpark.restaurant.dto.param.DishesScheduleImportParam;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

@Slf4j
public class DishesScheduleImportListener extends AnalysisEventListener<DishesScheduleImportParam> {
    private static String SYMBOL = "；";

    private Map<String, DictItemModel> foodSpicyMapp;
    private Map<String, DictItemModel> foodCategoryMap;
    private Map<String, DictItemModel> restaurantTimeMap;

    private List<String> timeTypes;

    private List<DishesScheduleImportParam> addList = new ArrayList<>();

    public DishesScheduleImportListener(List<String> timeTypes, Map<String, DictItemModel> foodSpicyMapp, Map<String, DictItemModel> foodCategoryMap, Map<String, DictItemModel> restaurantTimeMap) {
        this.timeTypes = timeTypes;
        this.foodSpicyMapp = foodSpicyMapp;
        this.foodCategoryMap = foodCategoryMap;
        this.restaurantTimeMap = restaurantTimeMap;
    }


    /**
     * 解析校验数据
     *
     * @Param
     * @Return
     */
    @Override
    public void invoke(DishesScheduleImportParam param, AnalysisContext analysisContext) {
        Integer rowIndex = analysisContext.readRowHolder().getRowIndex();
        try {
            if (allFieldsNull(param)) {
                return;
            }
        } catch (Exception e) {
            log.info("解析异常", e.getMessage());
        }
        StringBuilder sb = new StringBuilder();
        if (StrUtil.isEmpty(param.getProductionDate())) {
            sb.append("出品日期不能为空").append(SYMBOL);
        } else {
            if (!isValidTimeFormat(param.getProductionDate(), "yyyy/MM/dd")) {
                sb.append("出品日期格式错误").append(SYMBOL);
            } else if (!checkProductionDate(param.getProductionDate())) {
                sb.append("导入数据超期，请核对后重新导入").append(SYMBOL);
            }
        }
        if (StrUtil.isEmpty(param.getMealTime())) {
            sb.append("用餐时间不能为空").append(SYMBOL);
        } else if (!restaurantTimeMap.containsKey(param.getMealTime())) {
            sb.append("用餐时间数据不存在").append(SYMBOL);
        } else if (!timeTypes.contains(restaurantTimeMap.get(param.getMealTime()).getCode())) {
            sb.append("餐厅暂未配置用餐时间").append(SYMBOL);
        }
        if (StrUtil.isEmpty(param.getName())) {
            sb.append("菜品名称不能为空").append(SYMBOL);
        } else if (param.getName().length() > 30) {
            sb.append("菜品名称长度超过30个字符").append(SYMBOL);
        }
        if (StrUtil.isEmpty(param.getType())) {
            sb.append("菜品类别不能为空").append(SYMBOL);
        } else if (!foodCategoryMap.containsKey(param.getType())) {
            sb.append("菜品类别不存在").append(SYMBOL);
        }

        if (StrUtil.isNotEmpty(param.getInformation()) && param.getInformation().length() > 200) {
            sb.append("原料信息长度超过200个字符").append(SYMBOL);
        }

        if (StrUtil.isNotEmpty(param.getWeight()) && !isNumeric(param.getWeight())) {
            sb.append("克重只能为数字").append(SYMBOL);
        } else if (StrUtil.isNotEmpty(param.getWeight())) {
            if (checkLenth(param.getWeight()).length() > 4) {
                sb.append("克重超过四位数").append(SYMBOL);
            }
            if (param.getWeight().contains(".")) {
                String substring = param.getWeight().substring(param.getWeight().indexOf(".") + 1);
                if (substring.length() > 1) {
                    sb.append("克重未精确到小数点后一位").append(SYMBOL);
                }
            }
        }

        if (StrUtil.isEmpty(param.getPrice())) {
            sb.append("单价不能为空").append(SYMBOL);
        } else if (!isNumeric(param.getPrice())) {
            sb.append("单价只能为数字").append(SYMBOL);
        } else {
            if (checkLenth(param.getPrice()).length() > 4) {
                sb.append("单价超过四位数").append(SYMBOL);
            }
            if (param.getPrice().contains(".")) {
                String substring = param.getPrice().substring(param.getPrice().indexOf(".") + 1);
                if (substring.length() > 2) {
                    sb.append("单价未精确到小数点后两位").append(SYMBOL);
                }
            }
        }
        if (StrUtil.isNotEmpty(param.getPungencyDegree()) && !foodSpicyMapp.containsKey(param.getPungencyDegree())) {
            sb.append("辣度建议数据不存在").append(SYMBOL);
        }

        String errorMessage = sb.toString();
        param.setErrMessage(StrUtil.isEmpty(errorMessage) ? null : "第" + (rowIndex + 1) + "行: " + errorMessage.substring(0, errorMessage.length() - 1));
        param.setNumber(String.valueOf(rowIndex + 1));
        addList.add(param);
    }


    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {

    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
    }

    public List<DishesScheduleImportParam> getAddList() {
        return addList;
    }


    /**
     * 判断出品日期
     *
     * @param date 出品日期
     * @return
     */
    public static Boolean checkProductionDate(String date) {
        DateTime productionDate = DateUtil.parse(date, "yyyy/MM/dd");
        int dayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        int daysRemaining = Calendar.SATURDAY - dayOfWeek + 1;
        DateTime nowTime = DateUtil.parse(DateUtil.date().toString("yyyy/MM/dd"), "yyyy/MM/dd");
        if (!productionDate.isBefore(nowTime) && !productionDate.isAfter(DateUtil.offsetDay(nowTime, daysRemaining + 7))) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }



    public static boolean isValidTimeFormat(String timeStr, String pattern) {
        try {
            DateUtil.parse(timeStr, pattern);
            return Boolean.TRUE;
        } catch (Exception e) {
            return Boolean.FALSE;
        }
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

    /**
     * 校验字符长度（带小数）
     *
     * @param str
     * @return
     */
    public static String checkLenth(String str) {
        if (str.contains(".")) {
            return str.substring(0, str.indexOf("."));
        }
        return str;
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
