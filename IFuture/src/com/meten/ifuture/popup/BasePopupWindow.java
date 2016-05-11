package com.meten.ifuture.popup;


import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.PopupWindow;

public class BasePopupWindow extends PopupWindow {

	public BasePopupWindow(Context context) {
		super(context);
	}

	@Override
	public void showAsDropDown(View anchor) {
//		setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
		super.showAsDropDown(anchor);
	}

	@SuppressLint("NewApi")
	@Override
	public void showAsDropDown(View anchor, int xoff, int yoff, int gravity) {
//		setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
		super.showAsDropDown(anchor, xoff, yoff, gravity);
	}

	@Override
	public void showAsDropDown(View anchor, int xoff, int yoff) {
//		setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
		super.showAsDropDown(anchor, xoff, yoff);
	}

	@Override
	public void showAtLocation(View parent, int gravity, int x, int y) {
		// TODO Auto-generated method stub
//		setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
		super.showAtLocation(parent, gravity, x, y);
	}

	@Override
	public void dismiss() {
//		setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
		super.dismiss();
	}
	
	

}
