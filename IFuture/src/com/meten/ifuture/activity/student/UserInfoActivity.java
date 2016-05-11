package com.meten.ifuture.activity.student;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.http.RequestParams;
import com.meten.ifuture.R;
import com.meten.ifuture.activity.base.BaseHeadActivity;
import com.meten.ifuture.constant.Constant;
import com.meten.ifuture.constant.URL;
import com.meten.ifuture.http.HttpRequestCallBack;
import com.meten.ifuture.http.HttpRequestUtils;
import com.meten.ifuture.http.RequestParamsUtils;
import com.meten.ifuture.model.Resource;
import com.meten.ifuture.model.ResultInfo;
import com.meten.ifuture.model.User;
import com.meten.ifuture.popup.SelectPicPopupWindow;
import com.meten.ifuture.utils.ImageUtils;
import com.meten.ifuture.utils.JsonParse;
import com.meten.ifuture.utils.SharedPreferencesUtils;
import com.meten.ifuture.utils.ToastUtils;
import com.meten.ifuture.view.CircularImage;

public class UserInfoActivity extends BaseHeadActivity implements
		OnClickListener {
	private static final int MODIF_USER_INFO = 0x01;
	private CircularImage headImg;
	private TextView tvName;
	private TextView tvUserName;
	private TextView tvCenter;
	private TextView tvMobile;
	private TextView tvParentMobile;
	private TextView tvQQ;
	private TextView tvWechat;
	private TextView tvEmail;
	private TextView tvAddr;

	private LinearLayout llCenter;
	private LinearLayout llMobile;
	private LinearLayout llParentMobile;
	private LinearLayout llQQ;
	private LinearLayout llWechat;
	private LinearLayout llEmail;
	private LinearLayout llAddr;
	private User user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_info_activity);
		user = SharedPreferencesUtils.getInstance(this).getUser();
		initView();

	}

	private void initView() {
		headImg = (CircularImage) findViewById(R.id.head_img);
		tvName = (TextView) findViewById(R.id.name_tv);
		tvUserName = (TextView) findViewById(R.id.username_tv);
		tvCenter = (TextView) findViewById(R.id.center_tv);
		tvMobile = (TextView) findViewById(R.id.mobile_tv);
		tvParentMobile = (TextView) findViewById(R.id.parent_mobile_tv);
		tvQQ = (TextView) findViewById(R.id.qq_tv);
		tvWechat = (TextView) findViewById(R.id.wechat_tv);
		tvEmail = (TextView) findViewById(R.id.email_tv);
		tvAddr = (TextView) findViewById(R.id.addr_tv);

		llCenter = (LinearLayout) findViewById(R.id.center_ll);
		llMobile = (LinearLayout) findViewById(R.id.mobile_ll);
		llParentMobile = (LinearLayout) findViewById(R.id.parent_mobile_ll);
		llQQ = (LinearLayout) findViewById(R.id.qq_ll);
		llWechat = (LinearLayout) findViewById(R.id.wechat_ll);
		llEmail = (LinearLayout) findViewById(R.id.email_ll);
		llAddr = (LinearLayout) findViewById(R.id.addr_ll);

		hiddenRightImageView();
		setTitle(getString(R.string.userInfo));
		headImg.setBackgroundResource(R.drawable.user_info_head_border);
		headImg.setBorderSize(getResources().getDimension(R.dimen.dp2));

		setUserInfoInView(user);
        //教师端隐藏父母手机号的显示
        if(user.getUserType() == Constant.TEACHER){
            llParentMobile.setVisibility(View.GONE);
            findViewById(R.id.parent_bottom_divider).setVisibility(View.GONE);
        }


		// llCenter.setOnClickListener(this);
		llMobile.setOnClickListener(this);
		llParentMobile.setOnClickListener(this);
		llQQ.setOnClickListener(this);
		llWechat.setOnClickListener(this);
		llEmail.setOnClickListener(this);
		llAddr.setOnClickListener(this);
		headImg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				SelectPicPopupWindow popup = new SelectPicPopupWindow(
						UserInfoActivity.this);
				popup.show();

			}
		});
	}

	private void setUserInfoInView(User user) {
		tvName.setText(user.getName());
		tvUserName.setText(user.getUserName());
		tvCenter.setText(user.getCenterName());
		tvMobile.setText(user.getMobile());
		tvParentMobile.setText(user.getParentMobile());
		tvQQ.setText(user.getQQ());
		tvWechat.setText(user.getWechat());
		tvEmail.setText(user.getEmail());
		tvAddr.setText(user.getAddress());
		headImg.setImageUrl(user.getPhoto());
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent(this, ModifUserInfoActivity.class);
		int id = 0;
		switch (v.getId()) {
		case R.id.mobile_ll:
			id = R.id.userinfo_mobile;
			break;
		case R.id.parent_mobile_ll:
			id = R.id.userinfo_parent_mobile;
			break;
		case R.id.qq_ll:
			id = R.id.userinfo_qq;
			break;
		case R.id.wechat_ll:
			id = R.id.userinfo_wechat;
			break;
		case R.id.email_ll:
			id = R.id.userinfo_email;
			break;
		case R.id.addr_ll:
			id = R.id.userinfo_addr;
			break;
		}
		intent.putExtra(Constant.KEY_TYPE, id);
		startActivityForResult(intent, MODIF_USER_INFO);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_CANCELED) {
			return;
		}
		switch (requestCode) {
		case MODIF_USER_INFO:
			user = SharedPreferencesUtils.getInstance(this).getUser();
			setUserInfoInView(user);
			break;
		// 拍照获取图片
		case ImageUtils.GET_IMAGE_BY_CAMERA:
			// uri传入与否影响图片获取方式,以下二选一
			// 方式一,自定义Uri(ImageUtils.imageUriFromCamera),用于保存拍照后图片地址
			if (ImageUtils.imageUriFromCamera != null) {
				// 可以直接显示图片,或者进行其他处理(如压缩或裁剪等)
				// iv.setImageURI(ImageUtils.imageUriFromCamera);
				// 对图片进行裁剪
				ImageUtils.cropImage(this, ImageUtils.imageUriFromCamera, null);
				break;
			}

			break;
		// 手机相册获取图片
		case ImageUtils.GET_IMAGE_FROM_PHONE:
			if (data != null && data.getData() != null) {
				// 可以直接显示图片,或者进行其他处理(如压缩或裁剪等)
				// iv.setImageURI(data.getData());

				// 对图片进行裁剪
				ImageUtils.cropImage(this, data.getData(), data);
			}
			break;
		// 裁剪图片后结果
		case ImageUtils.CROP_IMAGE:
			if (ImageUtils.cropImageUri != null) {
				String picPath = ImageUtils.getAbsoluteImagePath(UserInfoActivity.this, ImageUtils.cropImageUri);
				RequestParams params = RequestParamsUtils.uploadFile(picPath,Constant.USER_ICON);
				HttpRequestUtils.create(UserInfoActivity.this).send(URL.UPLOAD, params, callback);
			}
			break;
		default:
			break;
		}
	}
	
	HttpRequestCallBack<ResultInfo> callback = new HttpRequestCallBack<ResultInfo>() {
		
		@Override
		public void onSuccess(ResultInfo info, int requestCode) {
			
			ToastUtils.show(UserInfoActivity.this, "上传成功");
			Resource res = JsonParse.parseToObject(info, Resource.class);
			if(res != null){
				headImg.setImageURI(ImageUtils.cropImageUri);
				user.setPhoto(res.getUrl());
				SharedPreferencesUtils.getInstance(UserInfoActivity.this).saveUser(user);
				Intent refreshPhoto = new Intent(Constant.ACTION_REFRESH_PHOTO);
				sendBroadcast(refreshPhoto);
			}
		}
		
	};

}
