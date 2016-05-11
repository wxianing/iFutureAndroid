package com.meten.ifuture.popup;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.TextView;

import com.meten.ifuture.R;
import com.meten.ifuture.utils.ImageUtils;

public class SelectPicPopupWindow extends BasePopupWindow implements
		OnClickListener {

	private Button btnCamera, btnPhoto, btnCancel;
	private View mMenuView;
	private Activity ac;
    private TextView tvTitle;

	public SelectPicPopupWindow(Activity ac) {
		super(ac);
		this.ac = ac;
		LayoutInflater inflater = (LayoutInflater) ac
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.select_pic_popup, null);
		btnCamera = (Button) mMenuView.findViewById(R.id.camera_btn);
		btnPhoto = (Button) mMenuView.findViewById(R.id.photo_btn);
		btnCancel = (Button) mMenuView.findViewById(R.id.cancel_btn);
        tvTitle = (TextView) mMenuView.findViewById(R.id.title_tv);

        btnCancel.setOnClickListener(this);
		btnCamera.setOnClickListener(this);
		btnPhoto.setOnClickListener(this);
		// 设置SelectPicPopupWindow的View
		this.setContentView(mMenuView);
		// 设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(LayoutParams.FILL_PARENT);
		// 设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(LayoutParams.WRAP_CONTENT);
		// 设置SelectPicPopupWindow弹出窗体可点击
		this.setFocusable(true);
		// 设置SelectPicPopupWindow弹出窗体动画效果
		this.setAnimationStyle(R.style.AnimBottom);
		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		// 设置SelectPicPopupWindow弹出窗体的背景
		this.setBackgroundDrawable(dw);
		// mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
		mMenuView.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {

				int height = mMenuView.findViewById(R.id.pop_layout).getTop();
				int y = (int) event.getY();
				if (event.getAction() == MotionEvent.ACTION_UP) {
					if (y < height) {
						dismiss();
					}
				}
				return true;
			}
		});

	}

    public void setTitle(String title){
        tvTitle.setText(title);
    }

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.cancel_btn:
			dismiss();
			break;
		case R.id.camera_btn:
			ImageUtils.openCameraImage(ac);
			dismiss();
			break;
		case R.id.photo_btn:
			ImageUtils.openLocalImage(ac);
			dismiss();
			break;
		}

	}
	
	public void show() {
		showAtLocation(ac.getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
	}

}
