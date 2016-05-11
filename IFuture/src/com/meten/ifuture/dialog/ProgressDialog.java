package com.meten.ifuture.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.ProgressBar;

import com.meten.ifuture.R;
import com.meten.ifuture.view.CircleProgress;
import com.meten.ifuture.view.DonutProgress;
import com.meten.ifuture.view.ProgressWheel;

public class ProgressDialog extends Dialog {
	public enum ProgressType {
		DEFAULT,CIRCLE, DOUNT,WHEEL;
	}

	private CircleProgress circleProgress;
	private DonutProgress dountProgress;
	private ProgressBar defaultProgress;
	private ProgressWheel wheelProgress;
	private ProgressType type = ProgressType.DEFAULT;

	public ProgressDialog(Context context) {
		super(context, R.style.myDialogTheme);
		setContentView(R.layout.progress_dialog);
		circleProgress = (CircleProgress) findViewById(R.id.circle_progress);
		dountProgress = (DonutProgress) findViewById(R.id.donut_progress);
        wheelProgress = (ProgressWheel) findViewById(R.id.progress_wheel);
        defaultProgress = (ProgressBar) findViewById(R.id.default_progress);
        circleProgress.setProgress(0);
		dountProgress.setProgress(0);
//		hiddenProgress(defaultProgress);
        setProgressType(ProgressType.DEFAULT);
        setCanceledOnTouchOutside(false);
	}

	public void setProgressType(ProgressType type) {
		this.type = type;
		if (type == ProgressType.CIRCLE) {
			hiddenProgress(circleProgress);
		} else if (type == ProgressType.DOUNT) {
			hiddenProgress(dountProgress);
        } else if (type == ProgressType.WHEEL) {
			hiddenProgress(wheelProgress);
		}else{
			hiddenProgress(wheelProgress);
		}
	}

	private void hiddenProgress(View view) {
		circleProgress.setVisibility(View.GONE);
		dountProgress.setVisibility(View.GONE);
        wheelProgress.setVisibility(View.GONE);
		defaultProgress.setVisibility(View.GONE);
		view.setVisibility(View.VISIBLE);
	}

	public void setProgress(int p) {
		if (type == ProgressType.CIRCLE) {
			circleProgress.setProgress(p);
		} else if (type == ProgressType.DOUNT) {
			dountProgress.setProgress(p);
		}

	}

}
