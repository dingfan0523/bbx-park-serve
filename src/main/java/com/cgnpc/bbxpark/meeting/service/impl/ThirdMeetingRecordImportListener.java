package com.cgnpc.bbxpark.meeting.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.cgnpc.bbxpark.meeting.dto.param.ThirdMeetingRecordImportParam;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

@Slf4j
public class ThirdMeetingRecordImportListener extends AnalysisEventListener<ThirdMeetingRecordImportParam> {
    private static String SYMBOL = "；";


    private List<ThirdMeetingRecordImportParam> addList = new ArrayList<>();

    public ThirdMeetingRecordImportListener() {

    }


    /**
     * 解析校验数据
     *
     * @Param
     * @Return
     */
    @Override
    public void invoke(ThirdMeetingRecordImportParam param, AnalysisContext analysisContext) {
        Integer rowIndex = analysisContext.readRowHolder().getRowIndex();
        try {
            if (allFieldsNull(param)) {
                return;
            }
        } catch (Exception e) {
            log.info("解析异常", e.getMessage());
        }
        StringBuilder sb = new StringBuilder();
        if(StrUtil.isEmpty(param.getMediaType())){
            sb.append("会议类型不能为空").append(SYMBOL);
        }
        if(StrUtil.isEmpty(param.getConferName())){
            sb.append("会议名称不能为空").append(SYMBOL);
        }
        if(StrUtil.isEmpty(param.getAccountName())){
            sb.append("发起人不能为空").append(SYMBOL);
        }
        if(StrUtil.isEmpty(param.getDepartmentName())){
            sb.append("使用部门不能为空").append(SYMBOL);
        }
        if(StrUtil.isEmpty(param.getRoomName())){
            sb.append("会议室名称不能为空").append(SYMBOL);
        }
        if(StrUtil.isEmpty(param.getStartTime())){
            sb.append("会议开始时间不能为空").append(SYMBOL);
        } else {
            if (!isValidTimeFormat(param.getStartTime(), "yyyy-MM-dd HH:mm")) {
                sb.append("会议开始时间格式错误").append(SYMBOL);
            }
        }
        if(StrUtil.isEmpty(param.getEndTime())){
            sb.append("会议结束时间不能为空").append(SYMBOL);
        } else {
            if (!isValidTimeFormat(param.getEndTime(), "yyyy-MM-dd HH:mm")) {
                sb.append("会议结束时间格式错误").append(SYMBOL);
            }
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

    public List<ThirdMeetingRecordImportParam> getAddList() {
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
