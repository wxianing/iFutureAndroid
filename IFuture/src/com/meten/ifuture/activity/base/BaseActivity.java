package com.meten.ifuture.activity.base;

import android.app.Activity;
import android.os.Bundle;

import com.meten.ifuture.AppManager;

public class BaseActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AppManager.getAppManager().addActivity(this);
//        AppManager.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);   //导致屏幕第一次点击无效
//        AppManager.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);   //导致屏幕第一次点击无效
	}
	
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		AppManager.getAppManager().removeActivity(this);
	}

}
