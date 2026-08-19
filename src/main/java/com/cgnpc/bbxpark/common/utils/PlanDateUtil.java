
package com.cgnpc.bbxpark.common.utils;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;

import java.util.Date;

/**
 * @Description 计划日期工具类
 * @author huangyongtao
 * @date 2025/10/17 15:30
 */
public class PlanDateUtil {
	
	/**
	 * 判断当前日期是否满足计划的周期条件
	 *
	 * @param currentDate 当前日期
	 * @param periodType  周期类型
	 * @param periodSign  周期标识
	 * @param periodStartTime  第一次抄表时间
	 * @param planPeriod  抄表类型：周期；单次
	 * @return 是否满足条件
	 */
	public static boolean isDateValid(Date currentDate, String periodType, Integer periodSign, Date periodStartTime, Integer planPeriod) {
		if (planPeriod == null || periodStartTime == null) {
			return false;
		}
		if(DateUtil.beginOfDay(currentDate).equals(DateUtil.beginOfDay(periodStartTime))){
			return true;
		}else if(DateUtil.beginOfDay(currentDate).before(DateUtil.beginOfDay(periodStartTime))){
            return false;
        }else if(planPeriod == 2){
			return false;
		}
		if (periodType == null || periodSign == null) {
			return false;
		}
		Date startDate;
		Date endDate;
		switch (periodType) {
			case "year":
				startDate = DateUtil.beginOfYear(currentDate);
				endDate = DateUtil.endOfYear(currentDate);
				break;
			case "quarter":
				startDate = DateUtil.beginOfQuarter(currentDate);
				endDate = DateUtil.endOfQuarter(currentDate);
				break;
			case "month":
				startDate = DateUtil.beginOfMonth(currentDate);
				endDate = DateUtil.endOfMonth(currentDate);
				break;
			case "week":
				startDate = DateUtil.beginOfWeek(currentDate);
				endDate = DateUtil.endOfWeek(currentDate);
				break;
			case "day":
				return true; // 每天都满足条件
			default:
				return false; // 未知周期类型
		}
		if (periodSign == 1) {
			return DateUtil.beginOfDay(currentDate).equals(DateUtil.beginOfDay(startDate));
		} else if (periodSign == 2) {
			return DateUtil.beginOfDay(currentDate).equals(DateUtil.beginOfDay(endDate));
		}
		return false;
	}

	/**
	 * 获取下次执行日期
	 *
	 * @param currentDate 当前日期
	 * @param periodType  周期类型
	 * @param periodSign  周期标识
	 * @param periodStartTime  第一次抄表时间
	 * @param planPeriod  抄表类型：周期；单次
	 * @return 下次执行日期
	 */
	public static Date getNextDate(Date currentDate, String periodType, Integer periodSign, Date periodStartTime, Integer planPeriod) {
		if (planPeriod == null || periodStartTime == null) {
			return null;
		}
		if(DateUtil.beginOfDay(currentDate).before(DateUtil.beginOfDay(periodStartTime))){
			return periodStartTime;
		}else if(planPeriod == 2){
			return null;
		}
		if (periodType == null || periodSign == null) {
			return null;
		}
		switch (periodType) {
			case "year":
				Date startDate = DateUtil.beginOfYear(currentDate);
				Date endDate = DateUtil.endOfYear(currentDate);
				//下一年的开始时间和结束时间
				Date nextStartDate = DateUtil.offset(startDate, DateField.YEAR, 1);
				Date nextEndDate = DateUtil.offset(endDate, DateField.YEAR, 1);
				if (periodSign == 1) {
					return nextStartDate;
				} else if (periodSign == 2) {
					return currentDate.before(DateUtil.beginOfDay(endDate)) ? endDate : nextEndDate;
				}
			case "quarter":
				Date quarterStartDate = DateUtil.beginOfQuarter(currentDate);
				Date quarterEndDate = DateUtil.endOfQuarter(currentDate);
				//下一季度的开始时间和结束时间
				Date nextQuarterStartDate = DateUtil.offset(quarterStartDate, DateField.MONTH, 3);
				Date nextQuarterEndDate = DateUtil.offset(quarterEndDate, DateField.MONTH, 3);
				if (periodSign == 1) {
					return nextQuarterStartDate;
				} else if (periodSign == 2) {
					return currentDate.before(DateUtil.beginOfDay(quarterEndDate)) ? quarterEndDate : nextQuarterEndDate;
				}
			case "month":
				Date monthStartDate = DateUtil.beginOfMonth(currentDate);
				Date monthEndDate = DateUtil.endOfMonth(currentDate);
				//下一个月的开始时间和结束时间
				Date nextMonthStartDate = DateUtil.offset(monthStartDate, DateField.MONTH, 1);
				Date nextMonthEndDate = DateUtil.offset(monthEndDate, DateField.MONTH, 1);
				if (periodSign == 1) {
					return nextMonthStartDate;
				} else if (periodSign == 2) {
					return currentDate.before(DateUtil.beginOfDay(monthEndDate)) ? monthEndDate : nextMonthEndDate;
				}
			case "week":
				Date weekStartDate = DateUtil.beginOfWeek(currentDate);
				Date weekEndDate = DateUtil.endOfWeek(currentDate);
				//下一周的开始时间和结束时间
				Date nextWeekStartDate = DateUtil.offset(weekStartDate, DateField.DAY_OF_YEAR, 7);
				Date nextWeekEndDate = DateUtil.offset(weekEndDate, DateField.DAY_OF_YEAR, 7);
				if (periodSign == 1) {
					return nextWeekStartDate;
				} else if (periodSign == 2) {
					return currentDate.before(DateUtil.beginOfDay(weekEndDate)) ? weekEndDate : nextWeekEndDate;
				}
			case "day":
				//当前时间加一天
				return DateUtil.offsetDay(currentDate, 1);
			default:
				return null; // 未知周期类型
		}
	}

	/**
	 * 获取编号
	 * @param prefix 前缀
	 * @param number 编号
	 * @return
	 */
	public static String getCode(String prefix, Long number){
		//获取编号
		return prefix + DateUtil.format(new Date(), "yyyyMMdd") + String.format("%06d", number);
	}
}