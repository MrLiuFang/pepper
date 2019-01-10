package com.pepper.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;

import com.sun.istack.NotNull;

/**
 * 日期工具类
 *
 * @author jianglei
 * @version 1.0.0 2013-8-3
 * @since JDK 1.7
 */
public class DateUtil {
	/**
	 * 最未来的时间
	 */
	public static final Date MOST_FUTURE_TIME = new Date(Long.MAX_VALUE);
	/**
	 * 短日期格式
	 */
	public static final String SHORT_DATE_PATTERN = "yyyy-MM-dd";
	/**
	 * 短日期格式
	 */
	public static final String SHORT_DATE_CHINESE_PATTERN = "yyyy年MM月dd日";
	/**
	 * 时间格式
	 */
	public static final String TIME_PATTERN = "HH:mm:ss";
	/**
	 * 长日期格式
	 */
	public static final String LONG_DATE_PATTERN = SHORT_DATE_PATTERN + " "+ DateUtil.TIME_PATTERN;
	/**
	 * 没分隔符长日期格式
	 */
	public static final String LONG_DATE_NO_DELIMITER_PATTERN = "yyyyMMddHHmmss";

	private static final long MS_ONE_SECOND = 1000;

	private static final long MS_ONE_MINUTE = 60 * 1000;

	private static final long MS_ONE_HOUR = 60 * DateUtil.MS_ONE_MINUTE;

	private static final long MS_ONE_DAY = 24 * 60 * DateUtil.MS_ONE_MINUTE;

	/**
	 * 最大的日期
	 */
	public static final String maxDateStr = "9999-12-31";

	/**
	 * 最大的日期
	 */
	public static final String maxDateStr_ = "9999/12/31";

	/**
	 * 年月日
	 */
	public static final String yyMMdd = "yyyy-MM-dd";

	/**
	 * 年月日
	 */
	public static final String yyMMdd_ = "yyyy/MM/dd";

	/**
	 * 年月日时分
	 */
	public static final String yyMMddHHmm = "yyyy-MM-dd HH:mm";

	/**
	 * 年月日时分
	 */
	public static final String yyMMddHHmm_ = "yyyy/MM/dd HH:mm";

	/**
	 * 年月日时分秒
	 */
	public static final String yyMMddHHmmss = "yyyy-MM-dd HH:mm:ss";

	/**
	 * 年月日时分秒
	 */
	public static final String yyMMddHHmmss_ = "yyyy/MM/dd HH:mm:ss";

	/**
	 * 年月日时分秒毫秒
	 */
	public static final String yyMMddHHmmssSSS = "yyyy-MM-dd HH:mm:ss.SSS";

	/**
	 * 年月日时分秒毫秒
	 */
	public static final String yyMMddHHmmssSSS_ = "yyyy/MM/dd HH:mm:ss.SSS";

	/**
	 * 年月日时分秒毫秒（26位）
	 */
	public static final String yyMMddHHmmssSSSSSS = "yyyy-MM-dd HH:mm:ss.SSSSSS";

	/**
	 * 年月日时分秒毫秒（26位）
	 */
	public static final String yyMMddHHmmssSSSSSS_ = "yyyy/MM/dd HH:mm:ss.SSSSSS";

	/**
	 * 年月日时分秒（用于加入文件名中）
	 */
	public static final String yyMMddHHmmss4fileName = "yyyyMMddHHmmss";

	private DateUtil() {
	}

	/**
	 * 获取当前日期，短日期格式yyyy-MM-dd
	 *
	 * @return 当前日期
	 */
	public static String getCurrentDate() {
		return formatShort(new Date());
	}

	/**
	 * 获取当前时间，长日期格式yyyy-MM-dd HH:mm:ss
	 *
	 * @return 当前时间
	 */
	public static String getCurrentTime() {
		return formatLong(new Date());
	}

	/**
	 * 获取当前时间，长日期格式yyyyMMddHHmmss
	 *
	 * @return 当前时间
	 */
	public static String getCurrentTimeNoDelimiter() {
		return formatLongNoDelimiter(new Date());
	}

	/**
	 * 获取当前时间戳
	 *
	 * @return 当前时间戳
	 */
	public static Timestamp getCurrentTimestamp() {
		return new Timestamp(Calendar.getInstance().getTimeInMillis());
	}

	/**
	 * 按照指定格式解析字符串型日期值为日期对象
	 *
	 * @param date
	 *            字符串型日期
	 * @param pattern
	 *            日期格式
	 * @return 日期对象
	 */
	public static Date parse(final String date, final String pattern) {
		if (StringUtils.isEmpty(date)) {
			return null;
		}
		final DateFormat formater = new SimpleDateFormat(pattern);
		try {
			return formater.parse(date);
		} catch (final ParseException e) {
			return null;
		}
	}

	/**
	 * 按照指定格式解析字符串型日期值为日期对象
	 *
	 * @param date
	 *            字符串型日期
	 * @param pattern
	 *            日期格式
	 * @return 日期对象
	 */
	public static String parse(final Date date, final String pattern) {
		if (date == null) {
			return null;
		}
		final DateFormat formater = new SimpleDateFormat(pattern);
		try {
			return formater.format(date);
		} catch (final Exception e) {
			return null;
		}
	}

	/**
	 * 按照指定格式格式化日期对象为字符串型日期
	 *
	 * @param date
	 *            日期对象
	 * @param pattern
	 *            日期格式
	 * @return 字符串型日期
	 */
	public static String format(final Date date, final String pattern) {
		if (date == null) {
			return null;
		}
		final DateFormat formater = new SimpleDateFormat(pattern);
		return formater.format(date);
	}

	/**
	 * 按照短日期格式(yyyy-MM-dd)解析字符串型日期值为日期对象
	 *
	 * @param date
	 *            字符串型日期
	 * @return 日期对象
	 */
	public static Date parseShort(final String date) {
		return parse(date, DateUtil.SHORT_DATE_PATTERN);
	}

	/**
	 * 按照短日期格式(yyyy-MM-dd)格式化日期对象为字符串型日期
	 *
	 * @param date
	 *            日期对象
	 * @return 字符串型日期
	 */
	public static String formatShort(final Date date) {
		return format(date, DateUtil.SHORT_DATE_PATTERN);
	}

	/**
	 * 按照长日期格式(yyyy-MM-dd HH:mm:ss)解析字符串型日期值为日期对象
	 *
	 * @param date
	 *            字符串型日期
	 * @return 日期对象
	 */
	public static Date parseLong(final String date) {
		return parse(date, DateUtil.LONG_DATE_PATTERN);
	}

	/**
	 * 按照长日期格式(yyyy-MM-dd HH:mm:ss)转换日期对象为字符串型日期
	 *
	 * @param date
	 *            日期对象
	 * @return 字符串型日期
	 */
	public static String formatLong(final Date date) {
		return format(date, DateUtil.LONG_DATE_PATTERN);
	}

	/**
	 * 按照长日期格式(yyyyMMddHHmmss)转换日期对象为字符串型日期
	 *
	 * @param date
	 *            日期对象
	 * @return 字符串型日期
	 */
	public static String formatLongNoDelimiter(final Date date) {
		return format(date, DateUtil.LONG_DATE_NO_DELIMITER_PATTERN);
	}

	/**
	 * 获取指定时间的日历对象
	 *
	 * @param date
	 *            时间
	 * @return 日历对象
	 */
	public static Calendar getCalendar(final Date date) {
		if (date == null) {
			return null;
		}
		final Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c;
	}

	/**
	 * 计算指定两个时间之间的相差月份数。如果earlierDate晚于laterDate，则返回负值
	 *
	 * @param earlierDate
	 *            较早时间
	 * @param laterDate
	 *            较晚时间
	 * @return 天数差
	 */
	public static int monthsBetween(final Date earlierDate, final Date laterDate) {
		final Calendar earlierCalendar = Calendar.getInstance();
		final Calendar laterCalendar = Calendar.getInstance();
		earlierCalendar.setTime(earlierDate);
		laterCalendar.setTime(laterDate);
		final int months = (laterCalendar.get(Calendar.YEAR) - earlierCalendar.get(Calendar.YEAR)) * 12;
		return months - earlierCalendar.get(Calendar.MONTH) + laterCalendar.get(Calendar.MONTH);
	}

	/**
	 * 计算指定两个时间之间的相差天数。如果earlierDate晚于laterDate，则返回负值
	 *
	 * @param earlierDate
	 *            较早时间
	 * @param laterDate
	 *            较晚时间
	 * @return 天数差
	 */
	public static int daysBetween(final Date earlierDate, final Date laterDate) {
		final Calendar earlierCalendar = setTimeToCalendar(earlierDate, 0, 0, 0, 0);
		final Calendar laterCalendar = setTimeToCalendar(laterDate, 0, 0, 0, 0);
		final long dms = laterCalendar.getTimeInMillis() - earlierCalendar.getTimeInMillis();
		return (int) (dms / DateUtil.MS_ONE_DAY);
	}

	/**
	 * 计算指定两个时间之间的相差小时之差。如果earlierDate晚于laterDate，则返回负值
	 *
	 * @param earlierDate
	 *            较早时间
	 * @param laterDate
	 *            较晚时间
	 * @return 小时之差
	 */
	public static int hoursBetween(final Date earlierDate, final Date laterDate) {
		final Calendar earlierCalendar = setTimeToCalendar(earlierDate, -1, 0, 0, 0);
		final Calendar laterCalendar = setTimeToCalendar(laterDate, -1, 0, 0, 0);
		final long dms = laterCalendar.getTimeInMillis() - earlierCalendar.getTimeInMillis();
		return (int) (dms / DateUtil.MS_ONE_HOUR);
	}

	/**
	 * 计算指定两个时间之间的相差分钟数。如果earlierDate晚于laterDate，则返回负值
	 *
	 * @param earlierDate
	 *            较早时间
	 * @param laterDate
	 *            较晚时间
	 * @return 分钟差
	 */
	public static int minutesBetween(final Date earlierDate, final Date laterDate) {
		final Calendar earlierCalendar = setTimeToCalendar(earlierDate, -1, -1, 0, 0);
		final Calendar laterCalendar = setTimeToCalendar(laterDate, -1, -1, 0, 0);
		final long dms = laterCalendar.getTimeInMillis() - earlierCalendar.getTimeInMillis();
		return (int) (dms / DateUtil.MS_ONE_MINUTE);
	}

	/**
	 * 计算指定两个时间之间的相差秒数。如果earlierDate晚于laterDate，则返回负值
	 *
	 * @param earlierDate
	 *            较早时间
	 * @param laterDate
	 *            较晚时间
	 * @return 秒差
	 */
	public static long secondsBetween(final Date earlierDate, final Date laterDate) {
		final Calendar earlierCalendar = setTimeToCalendar(earlierDate, -1, -1, -1, 0);
		final Calendar laterCalendar = setTimeToCalendar(laterDate, -1, -1, -1, 0);
		final long dms = laterCalendar.getTimeInMillis() - earlierCalendar.getTimeInMillis();
		return dms / DateUtil.MS_ONE_SECOND;
	}

	/**
	 * 创建指定值的日期
	 *
	 * @param year
	 *            年
	 * @param month
	 *            月
	 * @param day
	 *            日
	 * @param hour
	 *            时
	 * @param minute
	 *            分
	 * @param second
	 *            秒
	 * @param millisecond
	 *            毫秒
	 * @return 日期
	 */
	public static Date createDate(final int year, final int month, final int day, final int hour, final int minute,
			final int second, final int millisecond) {
		final Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, year);
		c.set(Calendar.MONTH, month - 1); // 月份从0开始
		c.set(Calendar.DATE, day);
		c.set(Calendar.HOUR_OF_DAY, hour);
		c.set(Calendar.MINUTE, minute);
		c.set(Calendar.SECOND, second);
		c.set(Calendar.MILLISECOND, millisecond);
		return c.getTime();
	}

	/**
	 * 获取指定日期加上指定年数后的日期值。若年数为负，则实际进行减操作
	 *
	 * @param date
	 *            原日期
	 * @param years
	 *            年数
	 * @return 计算后的新日期
	 */
	public static Date addYears(final Date date, final int years) {
		if (years == 0) {
			return date;
		}
		final Calendar c = getCalendar(date);
		c.add(Calendar.YEAR, years);
		return c.getTime();
	}

	/**
	 * 获取指定日期加上指定月数后的日期值。若月数为负，则实际进行减操作。
	 *
	 * @param date
	 *            原日期
	 * @param days
	 *            月数
	 * @return 计算后的新日期
	 */
	public static Date addMonths(final Date date, final int months) {
		if (months == 0) {
			return date;
		}
		final Calendar c = getCalendar(date);
		c.add(Calendar.MONTH, months);
		return c.getTime();
	}

	/**
	 * 获取指定日期加上指定天数后的日期值。若天数为负，则实际进行减操作。
	 *
	 * @param date
	 *            原日期
	 * @param days
	 *            天数
	 * @return 计算后的新日期
	 */
	public static Date addDays(final Date date, @NotNull final Integer days) {
		if (days == 0) {
			return date;
		}
		final Calendar c = getCalendar(date);
		c.add(Calendar.DATE, days);
		return c.getTime();
	}

	/**
	 * 获取指定日期加上指定小时数后的日期值。若小时数为负，则实际进行减操作。
	 *
	 * @param date
	 *            原日期
	 * @param hours
	 *            小时数
	 * @return 计算后的新日期
	 */
	public static Date addHours(final Date date, final int hours) {
		if (hours == 0) {
			return date;
		}
		final Calendar c = getCalendar(date);
		c.add(Calendar.HOUR_OF_DAY, hours);
		return c.getTime();
	}

	/**
	 * 获取指定日期加上指定分钟数后的日期值。若分钟数为负，则实际进行减操作。
	 *
	 * @param date
	 *            原日期
	 * @param hours
	 *            分钟数
	 * @return 计算后的新日期
	 */
	public static Date addMinutes(final Date date, final int minutes) {
		if (minutes == 0) {
			return date;
		}
		final Calendar c = getCalendar(date);
		c.add(Calendar.MINUTE, minutes);
		return c.getTime();
	}

	/**
	 * 获取指定日期加上指定秒数后的日期值。若秒数为负，则实际进行减操作。
	 *
	 * @param date
	 *            原日期
	 * @param seconds
	 *            秒数
	 * @return 计算后的新日期
	 */
	public static Date addSeconds(final Date date, final int seconds) {
		if (seconds == 0) {
			return date;
		}
		final Calendar c = getCalendar(date);
		c.add(Calendar.SECOND, seconds);
		return c.getTime();
	}

	/**
	 * 为指定日期设置年月日，返回新日期
	 *
	 * @param date
	 *            原日期
	 * @param year
	 *            年
	 * @param month
	 *            月
	 * @param day
	 *            日
	 * @return 新日期
	 */
	public static Date setDate(final Date date, final int year, final int month, final int day) {
		final Calendar c = getCalendar(date);
		c.set(Calendar.YEAR, year);
		c.set(Calendar.MONTH, month - 1); // 月份从0开始
		c.set(Calendar.DATE, day);
		return c.getTime();
	}

	/**
	 * 为指定日期设置时分秒毫秒，返回新日期
	 *
	 * @param date
	 *            原日期
	 * @param hour
	 *            时
	 * @param minute
	 *            分
	 * @param second
	 *            秒
	 * @param millisecond
	 *            毫秒
	 * @return 新日期
	 */
	public static Date setTime(final Date date, final int hour, final int minute, final int second,
			final int millisecond) {
		final Calendar c = setTimeToCalendar(date, hour, minute, second, millisecond);
		return c == null ? null : c.getTime();
	}

	private static Calendar setTimeToCalendar(final Date date, final int hour, final int minute, final int second,
			final int millisecond) {
		final Calendar c = getCalendar(date);
		if (c == null) {
			return null;
		}
		if (hour >= 0) {
			c.set(Calendar.HOUR_OF_DAY, hour);
		}
		if (minute >= 0) {
			c.set(Calendar.MINUTE, minute);
		}
		if (second >= 0) {
			c.set(Calendar.SECOND, second);
		}
		if (millisecond >= 0) {
			c.set(Calendar.MILLISECOND, millisecond);
		}
		return c;
	}

	/**
	 * 获取指定日期集合中最早的日期
	 *
	 * @param dates
	 *            日期集合
	 * @return 最早的日期
	 */
	public static Date earliest(final Date... dates) {
		Date result = null;
		for (final Date date : dates) {
			if (result == null) {
				result = date;
			} else if (date.before(result)) {
				result = date;
			}
		}
		return result;
	}

	/**
	 * 获取指定日期集合中最晚的日期
	 *
	 * @param dates
	 *            日期集合
	 * @return 最晚的日期
	 */
	public static Date latest(final Date... dates) {
		Date result = null;
		for (final Date date : dates) {
			if (result == null) {
				result = date;
			} else if (date.after(result)) {
				result = date;
			}
		}
		return result;
	}

	public static int getYear(final Date date) {
		return getCalendar(date).get(Calendar.YEAR);
	}

	public static int getMonth(final Date date) {
		return getCalendar(date).get(Calendar.MONTH) + 1; // 月份从0开始
	}

	public static int getDay(final Date date) {
		return getCalendar(date).get(Calendar.DAY_OF_MONTH);
	}

	public static int getHour(final Date date) {
		return getCalendar(date).get(Calendar.HOUR_OF_DAY);
	}

	/**
	 * 判断指定日期是否周末
	 *
	 * @param date
	 *            日期
	 * @return 是否周末
	 */
	public static boolean isWeekend(final Date date) {
		final int weekday = getCalendar(date).get(Calendar.DAY_OF_WEEK);
		return weekday == Calendar.SUNDAY || weekday == Calendar.SATURDAY;
	}

	/**
	 * 字符串转换为日期
	 * 
	 * @param times
	 *            YYYY-MM-DD
	 * @return Date
	 * @throws ParseException
	 */
	public static final Date strToDate(@NotNull String times) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(yyMMdd);
		return sdf.parse(times);
	}

	/**
	 * 时间字符串转换为日期Date
	 * 
	 * @param datetime
	 * @param pattern
	 *            默认"yyyy-MM-dd HH:mm"
	 * @return
	 */
	public static final Date strToDateTime(@NotNull String datetime, @NotNull String pattern) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.parse(datetime);
	}

	/**
	 * 字符串转换为时间戳
	 * 
	 * @param times
	 *            YYYY-MM-DD HH:mm:SS.ssssss
	 * @return Date
	 * @throws ParseException
	 */
	public static final Date strToTimeStamp(@NotNull String times) throws ParseException {
		String pattern = yyMMddHHmmssSSSSSS;
		if (times.trim().length() == 23) {
			pattern = yyMMddHHmmssSSS;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.parse(times.trim());
	}

	/**
	 * 将日期转换为指定格式的字符串 如果两个都传null，得到当前日期的yyyy-MM-dd HH:mm:ss.SSS格式的字符串，用于存于DB。
	 * 
	 * @param date
	 * @param pattern
	 *            默认为 "yyyy-MM-dd HH:mm:ss.SSS"
	 * @return String
	 */
	public static final String dateToStr(@NotNull Date date, @NotNull String pattern) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.US);
		return sdf.format(date);
	}

	/**
	 * 获取当前时间的yyyy-MM-dd HH:mm:ss.SSS格式的字符串，用于存于DB。
	 * 
	 * @return
	 */
	public static final String getCurrentDBTimeString() {
		return dateToStr(null, null);
	}

	/**
	 * 获取上个月第一天
	 * 
	 * @return
	 */
	public static final Date getLastMonthFisrtDay() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, -1);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		return calendar.getTime();
	}

	/**
	 * 获取上个月最后一天
	 * 
	 * @return
	 */
	public static final Date getLastMonthLastDay() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.add(Calendar.DATE, -1);
		return calendar.getTime();
	}

	/**
	 * 获取昨天的最后一秒
	 * 
	 * @return
	 */
	public static final Date getLastDayLastSecond() {
		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 23,
				59, 59);
		return calendar.getTime();
	}

	/**
	 * 获取某天的最后一秒
	 * 
	 * @param date
	 * @return
	 */
	public static final Date getLastSecond(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 23,
				59, 59);
		return calendar.getTime();
	}

	/**
	 * 获取某天的凌晨零时零分零秒
	 * 
	 * @param date
	 * @return
	 */
	public static final Date getBeginTime(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 0,
				0, 0);
		return calendar.getTime();
	}

	/**
	 * 获取指定天数前某天的日期
	 * 
	 * @param days
	 * @return
	 */
	public static final Date getLastTheDay(Date date, int days) {
		final Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, days);
		return calendar.getTime();
	}

	/**
	 * 获取本月第一天
	 * 
	 * @author buggy3
	 * @return
	 */
	public static final Date getFirstDayThisMonth() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		return calendar.getTime();
	}

}
