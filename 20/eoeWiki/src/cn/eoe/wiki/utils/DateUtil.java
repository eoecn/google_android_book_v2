package cn.eoe.wiki.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.text.TextUtils;
/**
 * date util,could use the class to get time with formated
 * @author <a href="mailto:kris1987@qq.com">Kris.lee</a>
 * @version 1.0.0
 */
public class DateUtil {
	public static final long		MINUTE_MILLIS 					= 60 * 1000; 
	public static final long		HOUR_MILLIS 					= MINUTE_MILLIS * 60; 
	/**
	 * milliseconds of a day
	 */
	public static final long 		DAY_MILLIS 						= HOUR_MILLIS * 24;
	/**
	 * milliseconds of a half day
	 */
	public static final long 		HALF_DAY_MILLIS 				= DAY_MILLIS / 2;
	/**
	 * milliseconds of a week
	 */
	public static final long 		WEEK_MILLIS 					= DAY_MILLIS * 7;
	/**
	 * milliseconds of a month
	 */
	public static final long 		MONTH_MILLIS 					= WEEK_MILLIS * 30;
	public static final long 		HALF_MONTH_MILLIS 				= MONTH_MILLIS / 2;
	/**
	 * yyyyMMdd
	 */
	public static final String 		DATE_DEFAULT_FORMATE 			= "yyyyMMdd";
	/**
	 * yyyy-MM-dd HH:mm:ss   2010-05-11 17:22:26
	 */
	public static final String 		DATE_FORMATE_ALL 				= "yyyy-MM-dd HH:mm:ss";
	/**
	 * dd/MM/yyyy, hh:mm
	 */
	public static final String 		DATE_FORMATE_TRANSACTION		= "dd/MM/yyyy, hh:mm";
	/**
	 * MM/dd HH:mm
	 */
	public static final String 		DATE_FORMATE_DAY_HOUR_MINUTE 	= "MM/dd HH:mm";
	/**
	 * HH:mm
	 */
	public static final String 		DATE_FORMATE_HOUR_MINUTE 		= "HH:mm";
	public static final String 		DATE_FORMATE_HOUR_MINUTE_SECOND 		= "HH:mm:ss";
	
	public static SimpleDateFormat	 dateFormate =new SimpleDateFormat();
	/**
	 * 
	 * @param milliseconds 
	 * @return the time formated by "yyyy-MM-dd HH:mm:ss"
	 */
	public static String toTime(long milliseconds){
		return toTime(new Date(milliseconds),DATE_FORMATE_ALL);
	}
	/**
	 * 
	 * @param milliseconds
	 * @param pattern
	 * @return the time formated
	 */
	public static String toTime(long milliseconds,String pattern){
		return toTime(new Date(milliseconds),pattern);
	}
	/**
	 * 
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String toTime(Date date,String pattern){
		if(TextUtils.isEmpty(pattern))
		{
			pattern = DATE_DEFAULT_FORMATE;
		}
		dateFormate.applyPattern(pattern);
		if(date ==null)
		{
			date = new Date();
		}
		try
		{
			return dateFormate.format(date);
		}catch (Exception e) {
			return "";
		}
	}
	
}
