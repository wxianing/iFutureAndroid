package com.meten.ifuture;

import android.app.Activity;
import android.app.Application;


import com.tencent.bugly.crashreport.CrashReport;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.android.api.JPushInterface;
import cn.sharesdk.framework.ShareSDK;

public class MainApplication extends Application {
	private List<Activity> activityTask = new ArrayList<Activity>();

	@Override
	public void onCreate() {
		super.onCreate();
		JPushInterface.init(this);
        ShareSDK.initSDK(this);
        CrashReport.initCrashReport(this,"900002729",BuildConfig.DEBUG);
        AppManager.application = this;
	}
}
