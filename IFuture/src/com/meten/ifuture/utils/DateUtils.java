package com.meten.ifuture.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.Semaphore;

import android.text.TextUtils;


public class DateUtils {
	public static SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
	public static SimpleDateFormat dtformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	
	/**
	 * 將yyyy-MM-dd HH:mm:ss转化为指定格式
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String getDateToString(String date, String pattern) {
		try {
			return getDateToString(dtformat.parse(date), pattern);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return "";
	}
	
	/**
	 * 将yyyy-MM-dd HH:mm：ss转化为yyyy-MM-dd
	 * @param date
	 * @return
	 */
	public static String getDateToString(String date) {
		try {
			return getDateToString(dtformat.parse(date), "yyyy-MM-dd");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	
	/**
	 * 將yyyy-MM-dd HH:mm:ss转化为指定格式
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String getDateToString(Date date, String pattern) {
		if(date == null || TextUtils.isEmpty(pattern)){
			return "";
		}
		SimpleDateFormat df = new SimpleDateFormat(pattern);
		return df.format(date);
	}
	
	/**
	 * 
	 * @param startDate 开始时间 格式：yyyy-MM-dd
	 * @param endDate 结束时间 格式：yyyy-MM-dd
	 * @return true 结束时间小于开始时间，false 结束时间大于开始时间
	 */
	public static boolean isBeforStartDate(String startDate,String endDate){
		try {
			Date sDate = dateformat.parse(startDate);
			Date eDate = dateformat.parse(endDate);
			if(eDate.before(sDate)){
				return true;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	
	/**
	 * 
	 * @param date
	 * @return yyyy-MM-dd
	 */
	public static String getDateToString(Date date) {
		return dateformat.format(date);
	}
	
	/**
	 * 当天日期yyyy-MM-dd
	 * @return
	 */
	public static String getTodayDate(){
		Date date = new Date();
		return dateformat.format(date);
	}
	
	/**
	 * 获取当月第一天日期 yyyy-MM-dd
	 * @return
	 */
	public static String getMothFirstDayDate(){
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		return dateformat.format(calendar.getTime());
	}
	
	public static boolean isTenMinutes(String time1,String time2){
		try {
			Date date1 = dtformat.parse(time1);
			Calendar cl1 = Calendar.getInstance();
			cl1.setTime(date1);
			Date date2 = dtformat.parse(time2);
			Calendar cl2 = Calendar.getInstance();
			cl2.setTime(date2);
			
			long t1 = cl1.getTimeInMillis();
			long t2 = cl2.getTimeInMillis();
			if(Math.abs(t1-t2) < 1000*60*10){
				return true;
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	public static boolean isOneDay(String date1,String date2){
		try {
			return isOneDay(dateformat.parse(date1), dateformat.parse(date2));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	public static boolean isOneDay(Date date1,Date date2){
		
		try {
			Calendar cl1 = Calendar.getInstance();
			cl1.setTime(dateformat.parse(dateformat.format(date1)));
			Calendar cl2 = Calendar.getInstance();
			cl2.setTime(dateformat.parse(dateformat.format(date2)));
			if(cl1.equals(cl2)){
				return true;
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	
	public static boolean isBeforCurrentDate(String date){
		return isBeforCurrentDate(date, "yyyy-MM-dd HH:mm:ss");
	}
	
	public static boolean isBeforCurrentDate(String date,String dateformat){
		SimpleDateFormat df = new SimpleDateFormat(dateformat);
		Calendar calendar = Calendar.getInstance();
		try {
			calendar.setTime(df.parse(date));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Calendar currentCalendar = Calendar.getInstance();
		try {
			currentCalendar.setTime(df.parse(df.format(new Date())));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(calendar.before(currentCalendar)){
			return true;
		}
		
		return false;
	}
	
	/**
	 * 当期时间是否距离传入时间5小时以前
	 * @param date
	 * @return
	 */
	public static boolean currentDateIsBeforFiveHours(String date){
		return currentDateIsBeforFiveHours(date, "yyyy-MM-dd HH:mm:ss");
	}
	
	public static boolean currentDateIsBeforFiveHours(String date,String dateformat){
		SimpleDateFormat df = new SimpleDateFormat(dateformat);
		Calendar calendar = Calendar.getInstance();
		try {
			calendar.setTime(df.parse(date));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Calendar currentCalendar = Calendar.getInstance();
		try {
			currentCalendar.setTime(df.parse(df.format(new Date())));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		calendar.add(Calendar.HOUR_OF_DAY, -5);
		if(currentCalendar.before(calendar)){
			return true;
		}
		
		return false;
	}
	
	
	/**
	 * 获得HH：mm-HH：mm格式
	 * @param startTime yyyy-MM-dd HH:mm:ss
	 * @param endTime yyyy-MM-dd HH:mm:ss
	 * @return HH：mm-HH：mm
	 */
	public static String getStartAndEndTime(String startTime, String endTime){
		SimpleDateFormat df = new SimpleDateFormat("HH:mm");
		try {
			String s = df.format(dtformat.parse(startTime));
			String e = df.format(dtformat.parse(endTime));
			return s+"-"+e;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	
	
}
