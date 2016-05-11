package com.meten.ifuture.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.meten.ifuture.AppManager;
import com.meten.ifuture.R;

public class HeadView extends LinearLayout {

	private ImageView ivBack;
	private TextView tvTitle;
	private ImageView ivRight;
	private TextView tvRight;
	private String title;
	
	public HeadView(Context context) {
		this(context, null);
	}

	public HeadView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	@SuppressLint("Recycle")
	public HeadView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.headView);
		title = a.getString(R.styleable.headView_titleText);
		initView();
	}

	private void initView() {
		LayoutInflater.from(getContext()).inflate(R.layout.public_head, this);
		ivBack = (ImageView) findViewById(R.id.back_iv);
		tvTitle = (TextView) findViewById(R.id.title_tv);
		ivRight = (ImageView) findViewById(R.id.right_iv);
		tvRight = (TextView) findViewById(R.id.right_tv);

		ivBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AppManager.getAppManager().finishActivity();
			}
		});
		
		if(!TextUtils.isEmpty(title)){
			tvTitle.setText(title);
		}
	}

	public void setTitle(String title) {
		tvTitle.setText(title);
	}

	public ImageView getRightImageView() {
		return ivRight;
	}
	
	public TextView getRightTextView(){
		return tvRight;
	}

	public void hiddenBackBtn() {
		ivBack.setVisibility(View.INVISIBLE);
	}
	
	public void hiddenRightImageView(){
		ivRight.setVisibility(View.INVISIBLE);
	}
	
	public void setLeftClickListener(OnClickListener listener){
		ivBack.setOnClickListener(listener);
	}

    public ImageView getLeftImageView(){
        return ivBack;
    }

}
