package com.meten.ifuture.utils;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

	/**
	 * 获得数字字符串，如果所传字符串为空或者不可转化为数字返回字符串0
	 * @param str
	 * @return
	 */
	public static String getIntString(String str) {
		return TextUtils.isEmpty(str) ? "0" : stringToInt(str)+"";
	}


    public static boolean isEmpty(String text){
        if(TextUtils.isEmpty(text) || TextUtils.isEmpty(text.trim())){
            return true;
        }
        return false;
    }

	/**
	 * 将字符串转化为int
	 * @param str
	 * @return
	 */
	public static int stringToInt(String str) {
		int result = 0;
		try {
			result = Integer.parseInt(str);
		} catch (Exception e) {
		}
		return result;
	}

	/**
	 * 如果是空字符串返回“-”
	 * @param str
	 * @return 
	 */
	public static String stringOrSeparator(String str) {
		return TextUtils.isEmpty(str) ? "-" : str;
	}
	
	
	/***
	 * 如果字符串不为空则直接返回，如果为null或者""则返回""
	 * @param str
	 * @return
	 */
	public static String stringOrEmpty(String str){
		return TextUtils.isEmpty(str) ? "" : str;
	}


    /**
     * 判断字符串是否为手机号码
     * @param text 字符串
     * @return
     */
    public static boolean  isPhoneNumber(String text){
        Pattern pat = Pattern.compile("^(13[0-9]|15[012356789]|17[0-9]|18[0-9]|14[57])[0-9]{8}$");
        Matcher mat = pat.matcher(text);
        return mat.matches();
    }


    /**
     * 判断字符串是否为合法邮箱
     * @param text 字符串
     * @return
     */
    public static boolean isEmail(String text){
        Pattern pat = Pattern.compile("^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$");
        Matcher mat = pat.matcher(text);
        return mat.matches();
    }


}
