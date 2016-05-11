package com.meten.ifuture.utils;


import com.meten.ifuture.BuildConfig;
import com.meten.ifuture.model.ResultInfo;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

public class ToastUtils {

	public static void show(Context context, String msg) {
		if(!TextUtils.isEmpty(msg)){
			Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
		}
	}
	
	public static void showMsg(Context context, ResultInfo info) {
		if(info != null && !TextUtils.isEmpty(info.getMsg())){
			show(context, info.getMsg());
		}
	}
	

	public static void showd(Context context, String msg) {
		if (BuildConfig.DEBUG) {
			Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
		}
	}
}
