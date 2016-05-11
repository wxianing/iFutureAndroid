package com.meten.ifuture.http;

import android.content.Context;

import com.meten.ifuture.model.ResultInfo;
import com.meten.ifuture.utils.ToastUtils;

public abstract class HttpRequestCallBack <T>{
	
	public abstract void onSuccess(T t, int requestCode);

	public void onFailure(Context context, ResultInfo info, int requestCode) {
		ToastUtils.showMsg(context, info);
	}
	
	
	public void onLoading(long total, long current,
			boolean isUploading,int requestCode) {
		
	}
}
