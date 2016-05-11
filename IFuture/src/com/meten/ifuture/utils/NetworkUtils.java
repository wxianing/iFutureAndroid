package com.meten.ifuture.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 
 * 功能描述: 网络监测类
 * 
 */
public class NetworkUtils {
	public static String TAG = "NetworkConnectivity";

	/*
	 * 判断是否有网络
	 */
	public boolean isNetworkAvailable(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cm == null) {
			return false;
		} else {
			// 打印所有的网络状态
			NetworkInfo[] infos = cm.getAllNetworkInfo();
			if (infos != null) {
				for (int i = 0; i < infos.length; i++) {
					// Log.d(TAG, "isNetworkAvailable - info: " +
					// infos[i].toString());
					if (infos[i].getState() == NetworkInfo.State.CONNECTED) {
						LogUtils.d(TAG, "isNetworkAvailable -  I " + i);
					}
				}
			}

			// 如果仅仅是用来判断网络连接　　　　　　
			// 则可以使用 cm.getActiveNetworkInfo().isAvailable();
			NetworkInfo networkInfo = cm.getActiveNetworkInfo();
			if (networkInfo != null) {
				LogUtils.d(
						TAG,
						"isNetworkAvailable - 是否有网络： "
								+ networkInfo.isAvailable());
			} else {
				LogUtils.d(TAG, "isNetworkAvailable - 完成没有网络！");
				return false;
			}

			// 1、判断是否有3G网络
			if (networkInfo != null
					&& networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
				LogUtils.d(TAG, "isNetworkAvailable - 有3G网络");
				return true;
			} else {
				LogUtils.d(TAG, "isNetworkAvailable - 没有3G网络");
			}

			// 2、判断是否有wifi连接
			if (networkInfo != null
					&& networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
				LogUtils.d(TAG, "isNetworkAvailable - 有wifi连接");
				return true;
			} else {
				LogUtils.d(TAG, "isNetworkAvailable - 没有wifi连接");
			}
		}
		return false;
	}

	/**
	 * @descript 判断是否有网络连接
	 * @param context
	 * @return
	 */
	public boolean isNetworkConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager
					.getActiveNetworkInfo();
			if (mNetworkInfo != null) {
				return mNetworkInfo.isAvailable();
			}
		}
		return false;
	}

	/**
	 * @descrip 判断WIFI网络是否可用
	 * @param context
	 * @return
	 */
	public boolean isWifiConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mWiFiNetworkInfo = mConnectivityManager
					.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			if (mWiFiNetworkInfo != null) {
				return mWiFiNetworkInfo.isAvailable();
			}
		}
		return false;
	}

	/**
	 * 判断MOBILE网络是否可用
	 * 
	 * @param context
	 * @return
	 */
	public boolean isMobileConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mMobileNetworkInfo = mConnectivityManager
					.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			if (mMobileNetworkInfo != null) {
				return mMobileNetworkInfo.isAvailable();
			}
		}
		return false;
	}

	/**
	 * @descrip 获取当前网络连接的类型信息
	 * @param context
	 * @return
	 */
	public static int getConnectedType(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager
					.getActiveNetworkInfo();
			if (mNetworkInfo != null && mNetworkInfo.isAvailable()) {
				return mNetworkInfo.getType();
			}
		}
		return -1;
	}
}
