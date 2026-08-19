package com.cgnpc.bbxpark.common.utils;

import com.cgnpc.bbxpark.common.exception.GenericException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;

/**
 * @create zhaoshuo
 * @time 2025/2/12
 * @desc 环比时间计算工具类
 */
@Component
@Slf4j
public class TimeRangeUtils {

    /**
     * 获取上一个时间段
     * @param currentStart 当前时间段开始时间
     * @param type 时间类型（日, 周, 月, 季度, 年）
     * @return 包含上一个时间段开始和结束的TimeRange对象
     */
    public static TimeRange getPreviousTimeRange(LocalDate currentStart, String type) {
        TimeRange result;
        switch (type) {
            case "day":
                result = handleDaily(currentStart);
                break;
            case "week":
                result = handleWeekly(currentStart);
                break;
            case "month":
                result = handleMonthly(currentStart);
                break;
            case "quarter":
                result = handleQuarterly(currentStart);
                break;
            case "year":
                result = handleYearly(currentStart);
                break;
            default:
                throw GenericException.fail("timeType参数错误");
        }
        return result;
    }

    // 处理日类型
    private static TimeRange handleDaily(LocalDate date) {
        LocalDate previousDay = date.minusDays(1);
        return new TimeRange(
                previousDay.atStartOfDay(),
                previousDay.atTime(23, 59, 59)
        );
    }

    // 处理周类型（中国周：周一为第一天）
    private static TimeRange handleWeekly(LocalDate date) {
        // 找到当前周的周一
        LocalDate monday = date.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        // 计算上周的周一和周日
        LocalDate prevMonday = monday.minusWeeks(1);
        LocalDate prevSunday = prevMonday.plusDays(6);
        return new TimeRange(
                prevMonday.atStartOfDay(),
                prevSunday.atTime(23, 59, 59)
        );
    }

    // 处理月类型
    private static TimeRange handleMonthly(LocalDate date) {
        LocalDate firstDayOfMonth = date.withDayOfMonth(1);
        LocalDate prevMonthLastDay = firstDayOfMonth.minusDays(1);
        LocalDate prevMonthFirstDay = prevMonthLastDay.withDayOfMonth(1);
        return new TimeRange(
                prevMonthFirstDay.atStartOfDay(),
                prevMonthLastDay.atTime(23, 59, 59)
        );
    }

    // 处理季度类型（中国季度划分）
    private static TimeRange handleQuarterly(LocalDate date) {
        int month = date.getMonthValue();
        int year = date.getYear();

        // 确定当前季度
        int quarter = (month - 1) / 3 + 1;

        // 计算上一个季度
        int prevQuarter = quarter - 1;
        int prevYear = year;
        if (prevQuarter < 1) {
            prevQuarter = 4;
            prevYear--;
        }

        // 计算季度范围
        int startMonth = (prevQuarter - 1) * 3 + 1;
        LocalDate start = LocalDate.of(prevYear, startMonth, 1);
        LocalDate end = start.plusMonths(2).with(TemporalAdjusters.lastDayOfMonth());
        return new TimeRange(
                start.atStartOfDay(),
                end.atTime(23, 59, 59)
        );
    }

    // 处理年度类型
    private static TimeRange handleYearly(LocalDate date) {
        LocalDate prevYear = date.minusYears(1);
        return new TimeRange(
                prevYear.withDayOfYear(1).atStartOfDay(),
                prevYear.with(TemporalAdjusters.lastDayOfYear()).atTime(23, 59, 59)
        );
    }


    // 时间段对象
    public static class TimeRange {
        private final LocalDateTime start;
        private final LocalDateTime end;

        public TimeRange(LocalDateTime start, LocalDateTime end) {
            this.start = start;
            this.end = end;
        }

        public LocalDateTime getStart() {
            return start;
        }

        public LocalDateTime getEnd() {
            return end;
        }

        @Override
        public String toString() {
            return "开始时间: " + start + ", 结束时间: " + end;
        }
    }
}
