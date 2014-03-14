package cn.lzs.share.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;

public class DateUtil {
	private static final Logger __logger = Logger.getLogger(DateUtil.class);

	public static String getDate(String timeType) {
		String strTime = null;
		try {
			SimpleDateFormat simpledateformat = new SimpleDateFormat(timeType);
			strTime = simpledateformat.format(new Date());
		} catch (Exception ex) {
			__logger.error("格式化日期错误 : " + ex.getMessage());
		}
		return strTime;
	}

	public static String getDate() {
		return getDate("yyyy-MM-dd HH:mm:ss");
	}

	public static boolean isToday(String comparedDate) {
		return getDate("yyyy-MM-dd").equals(comparedDate);
	}

	public static String getBeforeDate(String timePos, int day) {
		long dateLong = 0L;
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");

		if ((timePos == null) || ("".equals(timePos)))
			dateLong = new Date().getTime();
		else {
			try {
				dateLong = dateFormat.parse(timePos).getTime();
			} catch (ParseException e) {
				__logger.error("输入时间\"" + timePos + "\"不合法,parse时间错误 : "
						+ e.getMessage());
				return "0000:00:00 00:00:00";
			}
		}
		dateLong -= day * 24 * 60 * 60 * 1000;
		return dateFormat.format(new Date(dateLong));
	}

	public static String getBeforeDate(String timeType, String timePos, int day) {
		long dateLong = 0L;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(timeType);

		if ((timePos == null) || ("".equals(timePos)))
			dateLong = new Date().getTime();
		else {
			try {
				dateLong = simpleDateFormat.parse(timePos).getTime();
			} catch (ParseException e) {
				__logger.error("请检查\"" + timePos + "\"和\"" + timeType
						+ "\"的时间格式是否一样,parse时间错误 : " + e.getMessage());
				return "0000:00:00 00:00:00";
			}
		}
		dateLong -= day * 24 * 60 * 60 * 1000;
		return simpleDateFormat.format(new Date(dateLong));
	}

	public static String getAfterDate(String timeType, String timePos, int day) {
		long dateLong = 0L;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(timeType);

		if ((timePos == null) || ("".equals(timePos)))
			dateLong = new Date().getTime();
		else {
			try {
				dateLong = simpleDateFormat.parse(timePos).getTime();
			} catch (ParseException e) {
				__logger.error("请检查\"" + timePos + "\"和\"" + timeType
						+ "\"的时间格式是否一样,parse时间错误 : " + e.getMessage());
				return "0000:00:00 00:00:00";
			}
		}
		dateLong += day * 24 * 60 * 60 * 1000;
		return simpleDateFormat.format(new Date(dateLong));
	}

	/**
	 * 获取date 值所在的当天及下一天的日期，这个日期的时，分都是置0的
	 * @param date
	 * @return
	 */
	public static Date[] getDateWithinOneDay(Date date){
		Date result[]=new Date[2];
		Calendar c=Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 1);
		result[0]=c.getTime();
		c.add(Calendar.DAY_OF_MONTH, 1);
		result[1]=c.getTime();
		return result;
	}
	
	public static int getMinute(Date startDate, Date endDate) {
		return (int) ((endDate.getTime() - startDate.getTime()) / 1000L / 60L);
	}
}