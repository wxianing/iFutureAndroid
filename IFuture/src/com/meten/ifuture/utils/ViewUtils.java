package com.meten.ifuture.utils;


import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.widget.TextView;

import com.meten.ifuture.R;

public class ViewUtils {

	private static long lastClickTime;

	public static boolean isFastDoubleClick() {
		long time = System.currentTimeMillis();
		long timeD = time - lastClickTime;
		if (0 < timeD && timeD < 500) {
			return true;
		}
		lastClickTime = time;
		return false;
	}


    /**
     * 设置有新消息模块的红点
     *
     * @param view Textview
     */
    public static void setViewDrawableLeftTips(TextView view, boolean isShow) {
        Drawable[] compoundDrawables = view.getCompoundDrawables();
        if (isShow) {
            Drawable drawable = view.getResources().getDrawable(R.drawable.tips);
            compoundDrawables[2] = drawable;
        } else {
            compoundDrawables[2] = null;
        }
        for (Drawable d : compoundDrawables) {
            if (d != null) {
                d.setBounds(0, 0, d.getMinimumWidth(), d.getMinimumHeight());
            }
        }
        view.setCompoundDrawables(compoundDrawables[0], compoundDrawables[1], compoundDrawables[2], compoundDrawables[3]);
    }


    public static boolean isOverFlowed(TextView textView) {
        int showTextwidth = textView.getWidth()-textView.getPaddingLeft()-textView.getPaddingRight();
        Paint paint = textView.getPaint();
        float width = paint.measureText( textView.getText().toString());
        if (width > showTextwidth) {
            return true;
        }
        return false;
    }

}
