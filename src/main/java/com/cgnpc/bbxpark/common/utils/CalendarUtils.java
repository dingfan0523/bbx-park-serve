package com.cgnpc.bbxpark.common.utils;

import java.util.Calendar;
import java.util.Date;

public class CalendarUtils {
    /**
     * 第一周最少为几天.
     */
    private static final int MINI_DAY_IN_FIRST_WEEK = 7;

    /**
     *
     * .
     *
     */
    private CalendarUtils() {

    }

    /**
     * 查询某个年份有多少周。 注：周一处于哪一年份就标识该周是属于哪一年份的，以自然周计
     *
     * @param year
     *            开始的年份
     * @return 总周数
     */
    public static int getWeekCount(int year) {
        return DateUtils.weekCnt(year);
    }

    /**
     * 获取年份.
     * @param date 日期，如果日期为null，取当前日期
     * @return 年份
     */
    public static Integer getCurrentYear(Date date) {
        if (date == null) {
            date = new Date();
        }
        return Integer.valueOf(DateUtils.format(date, "yyyy"));
    }

    /**
     * 获取月份.
     * @param date 日期，如果日期为null，取当前日期
     * @return 月份
     */
    public static Integer getCurrentMonth(Date date) {
        if (date == null) {
            date = new Date();
        }
        return Integer.valueOf(DateUtils.format(date, "MM"));
    }

    /**
     *
     * 获取周次.
     *
     * @param date 日期，如果日期为null，取当前日期
     * @return 周次
     */
    public static Integer getCurrentWeek(Date date) {
        if (date == null) {
            date = new Date();
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setMinimalDaysInFirstWeek(MINI_DAY_IN_FIRST_WEEK);
        calendar.setTime(date);
        int iWeekNum = calendar.get(Calendar.WEEK_OF_YEAR);
        return iWeekNum;
    }

    /**
     * 获取某周的第一天.
     *
     * @param date
     *            当前周的日期
     * @return 周一的日期
     */
    public static Date getWeekFirstDay(Date date) {
        Calendar c = Calendar.getInstance();
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.setMinimalDaysInFirstWeek(MINI_DAY_IN_FIRST_WEEK);
        c.setTime(date);
        c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek()); // Monday
        return c.getTime();
    }

    /**
     *
     * 根据年份和周次获取当周的起始时间.
     *
     * @param year 年份
     * @param week 周次
     * @return 开始日期
     */
    public static Date getWeekFirstDay(int year, int week) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.WEEK_OF_YEAR, week);
        calendar.set(Calendar.YEAR, year);
        calendar.setMinimalDaysInFirstWeek(MINI_DAY_IN_FIRST_WEEK);
        if (calendar.get(Calendar.DAY_OF_WEEK) == 1) {
            calendar.add(Calendar.DAY_OF_WEEK_IN_MONTH, 1);
        }
        return getWeekFirstDay(calendar.getTime());
    }

    /**
     *
     * 根据年份和周次获取当周的结束时间.
     *
     * @param year 年份
     * @param week 周次
     * @return 结束日期
     */
    public static Date getWeekLastDay(int year, int week) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.WEEK_OF_YEAR, week);
        calendar.set(Calendar.YEAR, year);
        calendar.setMinimalDaysInFirstWeek(MINI_DAY_IN_FIRST_WEEK);
        if (calendar.get(Calendar.DAY_OF_WEEK) == 1) {
            calendar.add(Calendar.DAY_OF_WEEK_IN_MONTH, 1);
        }
        return getWeekLastDay(calendar.getTime());
    }

    /**
     * 获取某周的最后一天.
     *
     * @param date
     *            当前周的日期
     * @return 周日的日期
     */
    public static Date getWeekLastDay(Date date) {
        Calendar c = Calendar.getInstance();
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.setMinimalDaysInFirstWeek(MINI_DAY_IN_FIRST_WEEK);
        c.setTime(date);
        c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek() + Calendar.FRIDAY); // Sunday
        return c.getTime();
    }

    /**
     *
     * 判断某个时间跟当前时间相差的毫秒数.
     *
     * @param before 比较的时间
     * @return 相差十几的毫秒数
     */
    public static long compareDateBetweenNow(Date before) {
        Calendar c = Calendar.getInstance();
        long now = c.getTimeInMillis();
        c.setTime(before);
        long lastly = c.getTimeInMillis();
        return now - lastly;
    }
}
