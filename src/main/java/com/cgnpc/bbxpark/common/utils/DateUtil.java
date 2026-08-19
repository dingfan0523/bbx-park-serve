package com.cgnpc.bbxpark.common.utils;

import cn.hutool.core.date.DateField;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具类
 *
 * @author dingfan
 * @date 2024/7/23 16:40
 */
public class DateUtil extends cn.hutool.core.date.DateUtil{
    /**
     * 获取今天开始时间
     *
     * @return 时间
     */
    public static Date getFirstTimeOfCurrent() {
        return getFirstTimeOfDate(new Date());
    }

    /**
     * 获取今天结束时间
     *
     * @return 时间
     */
    public static Date getLastTimeOfCurrent() {
        return getLastTimeOfDate(new Date());
    }

    /**
     * 获取指定日期的开始时间
     *
     * @return 时间
     */
    public static Date getFirstTimeOfDate(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        startTime(c);
        return c.getTime();
    }

    /**
     * 获取指定日期的结束时间
     *
     * @return 时间
     */
    public static Date getLastTimeOfDate(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        lastTime(c);
        return c.getTime();
    }

    /**
     * 获取指定日期的偏移量的开始时间
     *
     * @return 时间
     */
    public static Date getFirstTimeOfDateOffset(Date date, int offset) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DAY_OF_MONTH, offset);
        startTime(c);
        return c.getTime();
    }

    /**
     * 获取指定日期的偏移量的结束时间
     *
     * @return 时间
     */
    public static Date getLastTimeOfDateOffset(Date date, int offset) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DAY_OF_MONTH, offset);
        lastTime(c);
        return c.getTime();
    }

    /**
     * 获取本周开始时间
     *
     * @return 时间
     */
    public static Date getFirstTimeOfWeek() {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        if (c.get(Calendar.DAY_OF_WEEK) == 1) {
            c.add(Calendar.DAY_OF_MONTH, -1);
        }
        c.add(Calendar.DATE, c.getFirstDayOfWeek() - c.get(Calendar.DAY_OF_WEEK) + 1);
        startTime(c);
        return c.getTime();
    }

    /**
     * 获取本周结束时间
     *
     * @return 时间
     */
    public static Date getLastTimeOfWeek() {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        // 如果是周日直接返回
        if (c.get(Calendar.DAY_OF_WEEK) != 1) {
            c.add(Calendar.DATE, 7 - c.get(Calendar.DAY_OF_WEEK) + 1);
        }
        lastTime(c);
        return c.getTime();
    }

    /**
     * 获取指定年月的开始时间
     * @param year 年份,如:2025
     * @param month 月份,1-12;不传则返回当年开始时间
     * @return 开始时间
     */
    public static Date getFirstTimeBy(Integer year,Integer month){
        Calendar c = Calendar.getInstance();
        c.clear();
        c.set(Calendar.YEAR,year);
        c.set(Calendar.MONTH,month != null ? month - 1 : 0);
        c.set(Calendar.DAY_OF_MONTH,1);
        startTime(c);
        return c.getTime();
    }

    /**
     * 获取指定年月的结束时间
     * @param year 年份,如:2025
     * @param month 月份,1-12;不传则返回当年结束时间
     * @return 结束时间
     */
    public static Date getLastTimeBy(Integer year,Integer month){
        Calendar c = Calendar.getInstance();
        c.clear();
        c.set(Calendar.YEAR,year);
        if(month != null){
            c.set(Calendar.MONTH,month);
        }else {
            c.set(Calendar.MONTH,0);
            c.add(Calendar.YEAR,1);
        }
        c.set(Calendar.DAY_OF_MONTH,1);
        startTime(c);
        c.add(Calendar.MILLISECOND,-1);
        return c.getTime();
    }


    private static void startTime(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }

    private static void lastTime(Calendar calendar) {
        startTime(calendar);
        calendar.add(Calendar.HOUR_OF_DAY, 23);
        calendar.add(Calendar.MINUTE, 59);
        calendar.add(Calendar.SECOND, 59);
    }

    public static Date parseDate(Long occurred) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(occurred);
        return c.getTime();
    }

    /**
     * 计算两个Date类型的时间之间的小时差，精确到小数点后一位四舍五入
     *
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @return 小时差，精确到小数点后一位
     */
    public static double calculateHourDifference(Date startDate, Date endDate) {
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("开始时间和结束时间不能为空");
        }

        long startTime = startDate.getTime();
        long endTime = endDate.getTime();

        // 计算时间差（毫秒）
        long timeDifference = endTime - startTime;

        // 将毫秒转换为小时，并四舍五入到小数点后一位
        double hourDifference = Math.round((timeDifference / (1000.0 * 60 * 60)) * 10) / 10.0;

        return hourDifference;
    }

    public static boolean isOverlap(Date realStartTime, Date realEndTime, Date startTime, Date endTime) {
        return realStartTime.compareTo(endTime) <= 0 && startTime.compareTo(realEndTime) <= 0;
    }
}
