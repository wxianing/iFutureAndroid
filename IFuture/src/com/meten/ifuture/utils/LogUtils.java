package com.meten.ifuture.utils;

import com.meten.ifuture.BuildConfig;

import android.util.Log;


public class LogUtils {
	private static final String TAG = "iFuture";

	public static void e(String msg) {
		e(TAG, msg);
	}

	public static void i(String msg) {
		i(TAG, msg);
	}

	public static void d(String msg) {
		d(TAG, msg);
	}

	public static void v(String msg) {
		v(TAG, msg);
	}

	public static void w(String msg) {
		w(TAG, msg);
	}

	public static void e(String tag, String msg) {
		if (BuildConfig.DEBUG) {
			Log.e(tag, msg);
		}
	}

	public static void i(String tag, String msg) {
		if (BuildConfig.DEBUG) {
			Log.i(tag, msg);
		}
	}

	public static void d(String tag, String msg) {
		if (BuildConfig.DEBUG) {
			Log.d(tag, msg);
		}
	}

	public static void v(String tag, String msg) {
		if (BuildConfig.DEBUG) {
			Log.v(tag, msg);
		}
	}

	public static void w(String tag, String msg) {
		if (BuildConfig.DEBUG) {
			Log.w(tag, msg);
		}
	}
}
