package com.cgnpc.bbxpark.common.utils;

import cn.hutool.core.util.NumberUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DateUtils {
    /**
     * LOGGER.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(DateUtils.class);

    /**
     * 时间类型，取出日期的长度。 yyyy-MM-dd HH:mm:ss
     */
    public static final int DAY_LEN = 10;

    /**
     * 时间类型，取出年度的长度。 yyyy-MM-dd HH:mm:ss
     */
    public static final int YEAR_LEN = 4;

    /**
     * 表示年度类型。
     */
    public static final int TYPE_YEAR = 1;

    /**
     * 表示月份类型。
     */
    public static final int TYPE_MONTH = 2;
    /**
     * 表示日期类。
     */
    public static final int TYPE_DATE = 3;

    /**
     * 表示小时类型。
     */
    public static final int TYPE_HOUR = 4;

    /**
     * 表示分类型。
     */
    public static final int TYPE_MINUTE = 5;

    /**
     * 表示秒类型。
     */
    public static final int TYPE_SECOND = 6;

    /**
     * 表示第几周类型。
     */
    public static final int TYPE_DAY_WEEK = 7;

    /**
     * 表示本月第几周类型。
     */
    public static final int TYPE_MONTH_WEEK = 8;

    /**
     * 表示本年度第几周类型。
     */
    public static final int TYPE_YEAR_WEEK = 9;

    /**
     * 表示当前毫秒。
     */
    public static final int TYPE_MILLI_SECOND = 10;

    /**
     * 表示一周有多少天。
     */
    public static final int WEEK_PER_DAYS = 7;
    /**
     * 表示一天有多少毫秒。
     */
    public static final int DAY_PER_TIME = 24 * 3600 * 1000;

    /**
     * 表示一年有12个月。
     */
    public static final int YEAR_MONTH = 12;

    /**
     * 表示月份中的最大的天数。
     */
    public static final int MONTH_MAX_DAYS = 31;

    /**
     * 每周的开始日期[星期一].
     */
    private static final int FIRST_DAY = Calendar.MONDAY;

    /**
     * 毫秒数.
     */
    private static final int MIL_SEC = 1000;

    private static String DEFAULT_DATE_PATTERN = "yyyy-MM-dd";

    /**
     * 构造函数，私有.
     */
    private DateUtils() {

    }

    /**
     * 日期转成字符串类型.
     *
     * @param date 要转换的日期对象
     * @return 字符串
     */
    public static String format(Date date) {
        return format(date, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 日期转成字符串类型(YYYY-MM-DD).
     *
     * @param date 要转换的日期对象
     * @return 字符串
     */
    public static String formatYMD(Date date) {
        return format(date, "yyyy-MM-dd");
    }

    /**
     * 获取当天的年月日.
     *
     * @return 字符串
     */
    public static String getCurrentYMD() {
        return format(new Date(), "yyyyMMdd");
    }

    /**
     * 获取当天的年份.
     *
     * @return 字符串
     */
    public static String getCurrentYear() {
        return format(new Date(), "yyyy");
    }

    /**
     * 查询当前日期是哪一周的.
     *
     * @return 数值型
     */
    public static int getCurrentWeek() {
        Calendar ca = Calendar.getInstance();
        ca.setTime(new Date());
        Date startDateOfYear = getMondayOfWeek(ca.get(Calendar.YEAR), 1);
        double distance = ((ca.getTime().getTime() - startDateOfYear.getTime())
                / (DAY_PER_TIME) + 1)
                / WEEK_PER_DAYS;
        // SystemType.out.println("====" + distance);
        if (distance < 0) { // 若是时间在第一个周一以前，则取去年最后一周
            return weekCnt(ca.get(Calendar.YEAR) - 1);
        }
        return (int) Math.ceil(distance);
    }

    /**
     * 获取当天的月份.
     *
     * @return 字符串
     */
    public static String getCurrentMonth() {
        return format(new Date(), "MM");
    }

    /**
     * 按指定的格式进行日期转换.
     *
     * @param date    要转换的日期对象
     * @param pattern 格式，默认为 “yyyy-MM-dd HH:mm:ss”
     * @return 字符串
     */
    public static String format(Date date, String pattern) {
        if (date == null) {
            return "";
        }
        if (pattern == null || pattern.equals("") || pattern.equals("null")) {
            pattern = "yyyy-MM-dd HH:mm:ss";
        }
        return new SimpleDateFormat(pattern).format(date);
    }

    /**
     * 把日期推前或延后N天.
     *
     * @param date 日期
     * @param n    推前或延后的天数[延后用负数表示]
     * @return 得到的日期
     */
    public static Date getNDayBeforeOrAfterDay(Date date, int n) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, n);
        return cal.getTime();
    }

    /**
     * 用于设定现在时间提前多少小时的
     * @param startDate:开始时间 ，yyyy-MM-dd HH:mm:ss
     * @param  hours :增加的时间 （提前，用正整数， 推后时间用负整数）
     */
    public static Date getNHourBeforeOrAfterHour(Date startDate,int hours){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String startTime = sdf.format(startDate);
//        System.out.println("现在时间："+startTime);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) - hours);
        String endTime = sdf.format(calendar.getTime());
//        System.out.println("提前"+hours+"小时："+ endTime);
        return DateUtils.format(endTime,"yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 用于设定现在时间提前多少秒的
     * @param startDate:开始时间 ，yyyy-MM-dd HH:mm:ss
     * @param  seconds :增加的时间 （提前，用正整数， 推后时间用负整数）
     */
    public static String getNSecondBeforeOrAfterSecond(Date startDate,int seconds){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String startTime = sdf.format(startDate);
//        System.out.println("现在时间："+startTime);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        calendar.set(Calendar.SECOND, calendar.get(Calendar.SECOND) - seconds);
        String endTime = sdf.format(calendar.getTime());
//        System.out.println("提前"+seconds+"秒："+ endTime);
        return endTime;
    }

    /**
     * 用于设定现在时间提前多少分钟的
     * @param startDate:开始时间 ，yyyy-MM-dd HH:mm:ss
     * @param  minutes :增加的时间 （提前，用正整数， 推后时间用负整数）
     */
    public static Date getNMinBeforeOrAfterMin(Date startDate,int minutes){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String startTime = sdf.format(startDate);
//        System.out.println("现在时间："+startTime);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE) - minutes);
        String endTime = sdf.format(calendar.getTime());
//        System.out.println("提前"+minutes+"分："+ endTime);
        return cn.hutool.core.date.DateUtil.parse(endTime);
    }

    /**
     * 字符串类型转日期类型.
     *
     * @param date 要转换的日期字符串
     * @return 日期类型
     */
    public static Date format(String date) {
        return format(date, null);
    }

    /**
     * 字符串类型转日期类型.
     *
     * @param date    要转换的日期字符串
     * @param pattern 日期格式：为null或""时，为“yyyy-MM-dd HH:mm:ss”
     * @return 日期类型
     */
    public static Date format(String date, String pattern) {
        if (pattern == null || pattern.equals("") || pattern.equals("null")) {
            pattern = "yyyy-MM-dd HH:mm:ss";
        }
        if (date == null || date.equals("") || date.equals("null")) {
            return new Date();
        }
        Date d = null;
        try {
            d = new SimpleDateFormat(pattern).parse(date);
        } catch (ParseException pe) {
            try {
                d = new SimpleDateFormat("yyyy-MM-dd").parse(date);
            } catch (ParseException e) {
                LOGGER.error("出现异常，异常信息为:" + e.getLocalizedMessage(), e);
            }
        }
        return d;
    }

    /**
     * 获取当前时间.
     *
     * @return 日期类型
     */
    public static Date currentDate() {
        Date str = new Date();
        return str;
    }

    /**
     * 获取当前时间字符串.
     *
     * @param pattern 日期格式：为null或""时，为“yyyy-MM-dd HH:mm:ss”
     * @return 字符串
     */
    public static String currentDateStr(String pattern) {
        if (pattern == null || pattern.equals("") || pattern.equals("null")) {
            pattern = "yyyy-MM-dd HH:mm:ss";
        }
        return format(new Date(), pattern);
    }

    /**
     * 字符串转Date 返回指定格式的时间，自动判断格式.
     *
     * @param str 字符串
     * @return 日期类型
     */

    public static Date strToDateTime(String str) {
        Date returnDate = null;
        if (NumberUtil.isLong(str)) {
            str = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Long.parseLong(str));
        }

        String pattern = "";
        if (str != null && !"".equals(str)) {
            if (str.length() > DAY_LEN) {
                pattern = "yyyy-MM-dd HH:mm:ss";
            } else {
                pattern = "yyyy-MM-dd";
            }
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);

            try {
                returnDate = sdf.parse(str);
            } catch (Exception ex) {
                LOGGER.error("出现异常，异常信息为:" + ex.getLocalizedMessage(), ex);
                return returnDate;
            }
        }

        return returnDate;
    }



    /**
     * 获取当前日期的，年，月，日，时，分，杪，星期几，本月第几周，本年第几周.
     *
     * @param field 年(1)、月(2)、日(3)、时(4)、分(5)、秒(6)、星期几(7)、本月第几周(8)、本年第几周(9)
     * @return 字符串
     */
    public static String getTimeParam(int field) {
        int param = 0;
        String returnValue = "";
        Calendar ca = Calendar.getInstance();
        switch (field) {
            case TYPE_YEAR:
                param = ca.get(Calendar.YEAR); // 获取年份
                break;
            case TYPE_MONTH:
                param = ca.get(Calendar.MONTH); // 获取月份
                break;
            case TYPE_DATE:
                param = ca.get(Calendar.DATE); // 获取日
                break;
            case TYPE_HOUR:
                param = ca.get(Calendar.HOUR); // 小时
                break;
            case TYPE_MINUTE:
                param = ca.get(Calendar.MINUTE); // 分
                break;
            case TYPE_SECOND:
                param = ca.get(Calendar.SECOND); // 秒
                break;
            case TYPE_MILLI_SECOND:
                param = ca.get(Calendar.MILLISECOND); // 毫秒
                break;
            case TYPE_DAY_WEEK:
                param = ca.get(Calendar.DAY_OF_WEEK) - 1; // 星期几，从周日开始算1
                if (param == 0) {
                    param = WEEK_PER_DAYS;
                }

                break;
            case TYPE_MONTH_WEEK:
                param = ca.get(Calendar.WEEK_OF_MONTH); // 本月第几周
                break;
            case TYPE_YEAR_WEEK:
                param = ca.get(Calendar.WEEK_OF_YEAR); // 本年第几周
                break;
            default:
                param = -1;
        }
        returnValue = param == -1 ? ""
                : String.valueOf(param);
        return returnValue;
    }

    /**
     * 计算两个时间相差天数计算两个时间相差天数.
     *
     * @param start 开始的日期，
     * @param end   结束日期
     * @return 天数
     */
    public static long daysBetween(String start, String end) {
        long days = 0;
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date d1 = format.parse(start);
            Date d2 = format.parse(end);
            long day1 = d1.getTime();
            long day2 = d2.getTime();
            days = (day2 - day1) / DAY_PER_TIME;
            if (days < 0) {
                return -1;
            }
        } catch (ParseException e) {
            LOGGER.error("出现异常，异常信息为:" + e.getLocalizedMessage(), e);
        }
        return days;
    }

    /**
     * 计算两个日期之间相差的总月数.
     *
     * @param start 开始日期
     * @param end   结束日期
     * @return 相差的总月数
     */

    public static int monthsBetween(Date start, Date end) {
        int num = 0;
        Calendar caStart = Calendar.getInstance();
        caStart.setTime(start);
        Calendar caEnd = Calendar.getInstance();
        caEnd.setTime(end);
        int endYear = caEnd.get(Calendar.YEAR);
        int endMonth = caEnd.get(Calendar.MONTH);

        int startYear = caStart.get(Calendar.YEAR);
        int startMonth = caStart.get(Calendar.MONTH);
        int year = (endYear - startYear);
        num += year * YEAR_MONTH;
        int month = endMonth - startMonth;
        num += month;
        int day = caEnd.get(Calendar.DATE) - caStart.get(Calendar.DATE);
        if (day > 0) {
            num += 1;
        }
        return num;
    }

    /**
     * 返回输入日期的，当月最后一天日期.
     *
     * @param date    日期类型
     * @param pattern 设置返回的格式
     * @return 字符串
     */
    public static String getLastDayOfMonth(Date date, String pattern) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        calendar.set(Calendar.DATE, calendar.getMaximum(Calendar.DATE));
        return format.format(calendar.getTime());
    }

    /**
     * 计算当前年龄.
     *
     * @param birthday 生日
     * @return 年龄
     */
    public static int getAge(String birthday) {
        Date now = new Date();
        String sNow = format(now, "yyyy");
        String sBir = birthday.substring(0, YEAR_LEN);
        int sAge = Integer.parseInt(sNow) - Integer.parseInt(sBir);
        return sAge;

    }

    /**
     * 增减日期，求几天前或几天后的日期.
     *
     * @param srcDate 指定的日期
     * @param inter   隔的天数 正数表示后几天，负数表示前几天
     * @return 几天前，或几天后的日期
     */
    public static Date addDate(Date srcDate, int inter) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(srcDate);
        cal.add(Calendar.DAY_OF_YEAR, inter);
        return cal.getTime();
    }

    /**
     * 增减月，求几月前或几月后的日期.
     *
     * @param srcDate 指定的日期
     * @param inter   隔的天数 正数表示后几月，负数表示前几月
     * @return 几月前，或几月后的日期
     */
    public static Date addMonth(Date srcDate, int inter) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(srcDate);
        cal.add(Calendar.MONTH, inter);
        return cal.getTime();
    }

    /**
     * 增减秒数.
     *
     * @param date 当前日期
     * @param sec  秒
     * @return 日期
     */
    public static Date addSecond(Date date, long sec) {
        return addTime(date, sec * MIL_SEC);
    }

    /**
     * 增减时间.
     *
     * @param date 当前日期
     * @param time 毫秒
     * @return 日期
     */
    public static Date addTime(Date date, long time) {
        if (date != null) {
            return new Date(date.getTime() + time);
        }
        return null;
    }

    /**
     * 查询某日期是星期几 .
     *
     * @param date 指定的日期，格式必须是yyyy-MM-dd
     * @return 星期几
     */
    public static String getWeekday(String date) {

        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdw = new SimpleDateFormat("E");
        Date d = null;
        try {
            d = sd.parse(date);
        } catch (ParseException e) {
            LOGGER.error("出现异常，异常信息为:" + e.getLocalizedMessage(), e);
        }
        return sdw.format(d);
    }

    /**
     * 查询某个年份有多少周。 注：周一处于哪一年份就标识该周是属于哪一年份的，以自然周计
     *
     * @param year 开始的年份
     * @return 总周数
     */
    public static int weekCnt(int year) {
        Date startDate = getMondayOfWeek(year, 1); // 取第一个周一
        Date endDate = getLastSundayOfYear(year); // 取最后一个周日
        return (int) (((endDate.getTime() - startDate.getTime())
                / (DAY_PER_TIME) + 1) / WEEK_PER_DAYS);
    }

    /**
     * 查询某个日期是哪一周的.
     *
     * @param date 日期类型，
     * @return 数值型
     */
    public static int getWeekday(Date date) {
        Calendar ca = Calendar.getInstance();
        ca.setTime(date);
        Date startDateOfYear = getMondayOfWeek(ca.get(Calendar.YEAR), 1);
        double distance = ((ca.getTime().getTime() - startDateOfYear.getTime())
                / (DAY_PER_TIME) + 1)
                / WEEK_PER_DAYS;
        // SystemType.out.println("====" + distance);
        if (distance < 0) { // 若是时间在第一个周一以前，则取去年最后一周
            return weekCnt(ca.get(Calendar.YEAR) - 1);
        }
        return (int) Math.ceil(distance);
    }

    /**
     * 某一日期在周历中被划为哪一年。
     *
     * @param date 日期类型
     * @return 年度，数值型
     */

    public static int whichYear(Date date) {
        Calendar ca = Calendar.getInstance();
        ca.setTime(date);
        Date startDateOfYear = getMondayOfWeek(ca.get(Calendar.YEAR), 1);
        double distance = ((ca.getTime().getTime() - startDateOfYear.getTime())
                / (DAY_PER_TIME) + 1)
                / WEEK_PER_DAYS;
        // SystemType.out.println("====" + distance);
        if (distance < 0) {
            return ca.get(Calendar.YEAR) - 1;
        }
        return ca.get(Calendar.YEAR);
    }

    /**
     * 查询某一周的星期一的日期。
     *
     * @param year   年度
     * @param weekId 周次
     * @return 周一的日期 ，日期型
     */
    public static Date getMondayOfWeek(int year, int weekId) {
        Calendar ca = Calendar.getInstance();
        ca.set(Calendar.YEAR, year);
        ca.set(Calendar.WEEK_OF_YEAR, weekId);
        ca.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        ca.set(Calendar.HOUR_OF_DAY, 0);
        if (ca.get(Calendar.YEAR) < year) { // 如果获取到的不是今年的日期，则取下个周一
            ca.set(Calendar.YEAR, year);
            ca.set(Calendar.WEEK_OF_YEAR, weekId + 1);
        }
        // SystemType.out.println(ca.getTime());
        return ca.getTime();
    }

    /**
     * 获取某年的最后一个周日。
     *
     * @param year 年度
     * @return 日期
     */
    public static Date getLastSundayOfYear(int year) {
        Calendar ca = Calendar.getInstance();
        ca.set(Calendar.YEAR, year);
        ca.set(Calendar.MONTH, Calendar.DECEMBER);
        ca.set(Calendar.DAY_OF_MONTH, MONTH_MAX_DAYS);
        if (ca.get(Calendar.DAY_OF_WEEK) > 1) { // 12月31日非周日
            ca.add(Calendar.DAY_OF_MONTH,
                    WEEK_PER_DAYS + 1 - ca.get(Calendar.DAY_OF_WEEK));
        }
        ca.set(Calendar.HOUR_OF_DAY, 0);
        // SystemType.out.println(ca.getTime());
        return ca.getTime();
    }

    // 以上这些方法为护理自有的周算法end

    /**
     * 得到指定月的天数。
     *
     * @param year  年度
     * @param month 月份
     * @return 天数
     */
    public static int getMonthLastDay(int year, int month) {
        Calendar a = Calendar.getInstance();
        a.set(Calendar.YEAR, year);
        a.set(Calendar.MONTH, month - 1);
        a.set(Calendar.DATE, 1); // 把日期设置为当月第一天
        a.roll(Calendar.DATE, -1); // 日期回滚一天，也就是最后一天
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }

    /**
     * 获取某周的第一天.
     *
     * @param date 当前周的日期
     * @return 周一的日期
     */
    public static Date getWeekFirstDay(Date date) {
        Calendar c = new GregorianCalendar();
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.setTime(date);
        c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek()); // Monday
        return c.getTime();
    }

    /**
     * 获取某周的最后一天.
     *
     * @param date 当前周的日期
     * @return 周日的日期
     */
    public static Date getWeekLastDay(Date date) {
        Calendar c = new GregorianCalendar();
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.setTime(date);
        c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek() + Calendar.FRIDAY); // Sunday
        return c.getTime();
    }

    /**
     * 判断时间是否存于某个时间段内.
     *
     * @param date      判断时间
     * @param beginTime 开始时间
     * @param endTime   结束时间
     * @return 是否处于
     */
    public static boolean checkTime(String date, String beginTime,
                                    String endTime) {
        if (date.compareToIgnoreCase(beginTime) >= 0
                && date.compareToIgnoreCase(endTime) <= 0) {
            return true;
        }
        return false;
    }

    /**
     * .
     */
    private static final int NOWTIME = 60000;

    /**
     * 判断用户的发送时间是否早于当前时间内.
     *
     * @param sendTime 发送时间
     * @return 是否早于当前时间
     */
    public static boolean isAfterNowTime(String sendTime) {
        Calendar c = Calendar.getInstance();
        long now = c.getTimeInMillis();
        c.setTime(strToDateTime(sendTime));
        long lastly = c.getTimeInMillis();
        return (now - lastly) >= NOWTIME;
    }

    /**
     * 获取每周的星期以及日期.
     *
     * @return 日期列表
     */
    public static List<String> getWeekdays() {
        List<String> weeks = new ArrayList<String>();
        Calendar calendar = Calendar.getInstance();
        // 设置周开始时间为每周的周一
        while (calendar.get(Calendar.DAY_OF_WEEK) != FIRST_DAY) {
            calendar.add(Calendar.DATE, -1);
        }
        for (int i = 0; i < WEEK_PER_DAYS; i++) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("EE MM-dd");
            weeks.add(dateFormat.format(calendar.getTime()));
            calendar.add(Calendar.DATE, 1);
        }
        return weeks;
    }

    /**
     * 整型时间转为日期类型(整型格式:yyyyMM或yyyyMMdd).
     *
     * @param intDate 整型时间转为日期类型.
     * @return 日期类型值.
     */
    public static Date intConvertDate(Integer intDate) {

        Date date = null;
        if (intDate != null) {
            String dateStr = String.valueOf(intDate);
            if (StringUtils.isBlank(dateStr)) {
                final int five = 5;
                final int seven = 7;
                String year = "", month = "", day = "";
                if (dateStr.length() >= seven) { // yyyyMMdd类型
                    year = dateStr.substring(0, five - 1);
                    month = dateStr.substring(five - 1, five + 1);
                    day = dateStr.substring(five + 1, dateStr.length());
                    date = DateUtils.format(year + "-" + month + "-" + day,
                            "yyyy-MM-dd");
                } else if (dateStr.length() >= five) { // yyyyMM类型
                    year = dateStr.substring(0, five - 1);
                    month = dateStr.substring(five - 1, dateStr.length());
                    date = DateUtils.format(year + "-" + month, "yyyy-MM");
                }
            }
        }
        return date;
    }

    /**
     * 日期转换为整型(yyyyMM)数据.
     *
     * @param date 日期值.
     * @return 整型数据.
     */
    public static Integer dateConvertYMInt(Date date) {

        Integer dateTime = null;
        if (date != null) {
            String dateStr = DateUtils.format(date, "yyyyMM");
            dateTime = Integer.parseInt(dateStr);
        }
        return dateTime;
    }

    /**
     * 日期转换为整型(yyyyMMDD)数据.
     *
     * @param date 日期值.
     * @return 整型数据.
     */
    public static Integer dateConvertYMDInt(Date date) {

        Integer dateTime = null;
        if (date != null) {
            String dateStr = DateUtils.format(date, "yyyyMMdd");
            dateTime = Integer.parseInt(dateStr);
        }
        return dateTime;
    }

    /**
     * 日期转换为长整型数据.
     *
     * @param date 日期值.
     * @return 整型数据.
     */
    public static Long dateConvertLong(Date date) {
        Long dateTime = 0L;
        final int len = 1000;
        if (date != null) {
            dateTime = date.getTime() / len; // 得到秒数，Date类型的getTime()返回毫秒数
        }
        return dateTime;
    }

    /**
     * 长整型数据转换为指定格式日期.
     *
     * @param dateTime 长整型数据.
     * @return 指定格式日期.
     */
    public static Date longConvertDate(Long dateTime) {

        Date date = null;
        final int len = 1000;
        if (dateTime != null) {
            // 先乘1000得到毫秒数，再转为Date类型
            date = new Date(dateTime * len);
        }
        return date;
    }

    /**
     * 获取每周的星期以及日期.
     *
     * @param year   年度.
     * @param weekid 周次.
     * @return 日期列表
     */
    public static List<String> getWeekdays(Integer year, Integer weekid) {
        List<String> weeks = new ArrayList<String>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(CalendarUtils.getWeekFirstDay(year, weekid));
        // 设置周开始时间为每周的周一
        while (calendar.get(Calendar.DAY_OF_WEEK) != FIRST_DAY) {
            calendar.add(Calendar.DATE, -1);
        }
        for (int i = 0; i < WEEK_PER_DAYS; i++) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("EE MM-dd");
            weeks.add(dateFormat.format(calendar.getTime()));
            calendar.add(Calendar.DATE, 1);
        }
        return weeks;
    }

    /**
     * 返回时间段内日期信息(总天数:天数,周六天数:天数,周日天数:天数)Map.
     *
     * @param beginDay 开始日期(yyyy-MM-dd).
     * @param endDay   结束日期(yyyy-MM-dd).
     * @return (总天数 : 天数, 周六天数 : 天数, 周日天数 : 天数)Map.
     */
    public static Map<String, Double> findDateMapInfo(
            String beginDay, String endDay) {
        Map<String, Double> dateMap = new HashMap<String, Double>();
        Date beginDate = DateUtils.format(beginDay, "yyyy-MM-dd");
        Date endDate = DateUtils.format(endDay, "yyyy-MM-dd");

        Calendar startCal = Calendar.getInstance();
        startCal.setTime(beginDate);
        Calendar endCal = Calendar.getInstance();
        endCal.setTime(endDate);

        final long allDayMills = 86400000;
        final int weekDays = 7;
        Long saturdays = 0L;
        Long sundays = 0L;
        long day = allDayMills; // 一天的millis
        // 共有多少天
        Long sumDays = (endCal.getTimeInMillis() - startCal.getTimeInMillis())
                / day;
        int weekNumber = startCal.get(Calendar.DAY_OF_WEEK); // beginDay是周几

        // 周日到周六分别是1~7
        saturdays = sumDays / weekDays; // 几个整周
        sundays = sumDays / weekDays; // 几个整周
        long yushu = sumDays % weekDays;

        if (weekNumber + yushu > weekDays) {
            ++saturdays; // 过了周六
            ++sundays; // 过了周六
        }
        if (weekNumber + yushu == weekDays) {
            ++saturdays; // 正好是周六
        }
        if (weekNumber == 1) {
            ++sundays; // 开始时间正好是周日
        }
        dateMap.put("总天数", Double.valueOf(sumDays + 1));
        dateMap.put("周六天数", Double.valueOf(saturdays));
        dateMap.put("周日天数", Double.valueOf(sundays));
        return dateMap;
    }

    /**
     * 取当前时间到24点的剩余秒数.
     *
     * @return 当前时间到24点的剩余秒数
     */
    public static int getDateExpired() {
        Date dt1 = new Date();
        System.out.println(dt1);
        //加1天
        Date dt2 = addDate(dt1, 1);
        System.out.println(dt2);
        String pattern = "yyyy-MM-dd";
        //去掉时间部分
        dt2 = format(DateUtils.format(dt2, pattern), pattern);
        System.out.println(dt2);
        long exp = (dt2.getTime() - dt1.getTime()) / MIL_SEC;
        System.out.println(exp);
        return (int) exp;
    }

    /**
     * 获取当前时间的int值，精确到秒.
     * 例如：2019年3月22日10时40分21秒，返回值为:20190322104021
     *
     * @return 获取当前时间的int值.
     */
    public static int getIntDatetime() {
        return (int) (System.currentTimeMillis() / MIL_SEC);
    }

    /**
     * 计算时差
     */
    public static String getTimeLag(Date startDate){
        if (Objects.isNull(startDate)){
            return "";
        }
        String time = "";
        try {
            //毫秒ms
            long diff = System.currentTimeMillis() - startDate.getTime();
            long diffSeconds = diff / 1000 % 60;
            long diffMinutes = diff / (60 * 1000) % 60;
            long diffHours = diff / (60 * 60 * 1000) % 24;
            long diffDays = diff / (24 * 60 * 60 * 1000);

            if (diffDays != 0){
                time = diffDays + "天";
            }
            if (diffHours != 0){
                time = time + diffHours + "时";
            }
            if (diffMinutes != 0){
                time = time + diffMinutes + "分";
            }
            if (diffSeconds != 0){
                time = time + diffSeconds + "秒";
            }
            return time;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return time;
    }

    /**
     * 获取指定日期n天后的开始时间（开始为00：00：00）
     * @param date 指定日期
     * @param n 隔间天
     * @return 指定日期n天后的开始时间
     */
    public static Date getStartDate(Date date, int n) {
        return addDate(strToDateTime(format(date, "yyyy-MM-dd") + " 00:00:00"), n);
    }

    /**未来时间
     * 根据指定时间获取
     * @param time
     * @param day
     * @return
     */
    public static Date getFutureDateTmdHmsByTime(Date time, int day) {
        if (day <= 0) {
            //默认2099年
            return parse("2099-01-01", DEFAULT_DATE_PATTERN);
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + day);
        return calendar.getTime();
    }

    /**字符串转日期
     * @param dateStr
     * @param pattern
     * @return
     */
    public static Date parse(String dateStr, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        if (dateStr == null || "".equals(dateStr)) {
            return null;
        }
        try {
            Date d = sdf.parse(dateStr);
            return d;
        } catch (ParseException e) {
            System.out.println("日期转换错误: " + e.getMessage());
            return null;
        }
    }

    /**
     * 获取未来第几天的日期
     *
     * @param day
     * @return
     */
    public static Date getFutureDateTmdHms(int day) {
        if (day <= 0) {
            //默认2099年
            return parse("2099-01-01", DEFAULT_DATE_PATTERN);
        }
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + day);
        return calendar.getTime();
    }

    private static final List<String> FORMAT_PATTERNS = Arrays.asList(
            "yyyy-MM-dd'T'HH:mm:ss",        // 2025-08-25T08:00:00
            "yyyy-MM-dd'T'HH:mm:ss.SSS",    // 2025-08-25T08:00:00.123
            "yyyy-MM-dd HH:mm:ss",          // 2025-08-25 08:00:00
            "yyyy-MM-dd HH:mm:ss.SSS",      // 2025-08-25 08:00:00.123
            "yyyy-MM-dd",                   // 2025-08-25
            "yyyy/MM/dd HH:mm:ss",          // 2025/08/25 08:00:00
            "yyyy/MM/dd",                   // 2025/08/25
            "dd-MM-yyyy HH:mm:ss",          // 25-08-2025 08:00:00
            "dd/MM/yyyy HH:mm:ss",          // 25/08/2025 08:00:00
            "yyyyMMddHHmmss",               // 20250825080000
            "yyyy-MM-dd'T'HH:mm:ssXXX",      // 2025-08-25T08:00:00+08:00 (带时区)
            "yyyy年MM月",      // 2025年07月
            "yyyy年M月"      // 2025年7月
    );

    /**
     * 将字符串解析为 java.util.Date，支持多种常见格式
     * @param dateStr 日期字符串
     * @return Date对象
     * @throws RuntimeException 当所有格式都无法解析时
     */
    public static Date parseDate(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return null;
        }
        for (String pattern : FORMAT_PATTERNS) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat(pattern);
                // 可选：设置宽松模式为false，增强严格校验
                sdf.setLenient(false);
                return sdf.parse(dateStr);
            } catch (ParseException e) {
                // 继续尝试下一个格式
            }
        }
        return null;
    }
}
