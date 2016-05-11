package com.meten.ifuture.activity.base;

import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.meten.ifuture.R;
import com.meten.ifuture.view.HeadView;

public class BaseHeadActivity extends BaseActivity {
	private HeadView headview;
	private LinearLayout container;
    private View status;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.base_activity);
		headview = (HeadView) findViewById(R.id.headview);
		container = (LinearLayout) findViewById(R.id.container);
        status = findViewById(R.id.status);

        headview.post(new Runnable() {
            @Override
            public void run() {
                if(Build.VERSION.SDK_INT >= 19){
                    ViewGroup.LayoutParams params =  status.getLayoutParams();
                    params.height = getStatusBarHeight();
                    status.setLayoutParams(params);
                }
            }
        });
	}

    private int getStatusBarHeight() {
        Rect frame = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);

        return frame.top;
    }

    @Override
	public void setContentView(int layoutResID) {
		LayoutInflater.from(this).inflate(layoutResID, container);
	}

    @Override
	public void setContentView(View view) {
        container.addView(view);
	}

	public void setTitle(CharSequence title) {
		headview.setTitle(title.toString());
	}
	public ImageView getRightImageView() {
		return headview.getRightImageView();
	}
	
	public TextView getRightTextView(){
		return headview.getRightTextView();
	}

	public void hiddenBackBtn() {
		headview.hiddenBackBtn();
	}
	
	public void hiddenRightImageView(){
		headview.hiddenRightImageView();
	}
	
	
	public void setLeftClickListener(OnClickListener listener){
		headview.setLeftClickListener(listener);
	}

    public ImageView getLeftImageView(){
        return headview.getLeftImageView();
    }
	
}
