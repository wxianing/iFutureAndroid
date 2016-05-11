package com.meten.ifuture.activity.student;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.http.RequestParams;
import com.meten.ifuture.R;
import com.meten.ifuture.activity.base.BaseHeadActivity;
import com.meten.ifuture.constant.Constant;
import com.meten.ifuture.constant.URL;
import com.meten.ifuture.http.HttpRequestCallBack;
import com.meten.ifuture.http.HttpRequestUtils;
import com.meten.ifuture.http.RequestParamsUtils;
import com.meten.ifuture.model.ResultInfo;
import com.meten.ifuture.model.User;
import com.meten.ifuture.utils.SharedPreferencesUtils;
import com.meten.ifuture.utils.StringUtils;
import com.meten.ifuture.utils.ToastUtils;

public class ModifUserInfoActivity extends BaseHeadActivity implements
		OnClickListener, TextWatcher {

	private EditText edit;
	private ImageView ivDelete;

	private User user;
	private int id;
	private String values;
    private String oldValues = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.modif_user_info_activity);
		user = SharedPreferencesUtils.getInstance(this).getUser();
		id = getIntent().getIntExtra(Constant.KEY_TYPE, R.id.userinfo_mobile);
		initView();
	}

	private void initView() {
		edit = (EditText) findViewById(R.id.edit);
		ivDelete = (ImageView) findViewById(R.id.delete_iv);

		TextView tvRight = getRightTextView();
		tvRight.setText("保存");
        ColorStateList csl= getResources().getColorStateList(R.color.commit_or_save_textcolor_selector);
        tvRight.setTextColor(csl);
		tvRight.setVisibility(View.VISIBLE);

		hiddenRightImageView();
		switch (id) {
		case R.id.userinfo_mobile:
			setTitle(getString(R.string.mobile).replace("　", ""));
			edit.setText(user.getMobile());
            edit.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
            oldValues = user.getMobile();
			break;
		case R.id.userinfo_parent_mobile:
			setTitle(getString(R.string.parent_mobile).trim());
			edit.setText(user.getParentMobile());
            edit.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
            oldValues = user.getParentMobile();
			break;
		case R.id.userinfo_qq:
			setTitle(getString(R.string.qq_str).replace("　", ""));
			edit.setText(user.getQQ());
            edit.setInputType(EditorInfo.TYPE_CLASS_TEXT);
            oldValues = user.getQQ();
			break;
		case R.id.userinfo_wechat:
			setTitle(getString(R.string.wechat_str).replace("　", ""));
			edit.setText(user.getWechat());
            edit.setInputType(EditorInfo.TYPE_CLASS_TEXT);
            oldValues = user.getWechat();
			break;
		case R.id.userinfo_email:
			setTitle(getString(R.string.email_str).replace("　", ""));
			edit.setText(user.getEmail());
            edit.setInputType(EditorInfo.TYPE_CLASS_TEXT);
            oldValues = user.getEmail();
			break;
		case R.id.userinfo_addr:
			setTitle(getString(R.string.addr).replace("　", ""));
			edit.setText(user.getAddress());
            edit.setInputType(EditorInfo.TYPE_CLASS_TEXT);
            oldValues = user.getAddress();
			break;
		}
		edit.setSelection(edit.getText().toString().length());

        if(!TextUtils.isEmpty(edit.getText().toString())){
            ivDelete.setVisibility(View.VISIBLE);
        }else{
            ivDelete.setVisibility(View.GONE);
        }
		
		tvRight.setOnClickListener(this);
		edit.addTextChangedListener(this);
		ivDelete.setOnClickListener(this);

	}
	
	public void saveValueInLocal(String values){
		switch (id) {
		case R.id.userinfo_mobile:
			user.setMobile(values);
			break;
		case R.id.userinfo_parent_mobile:
			user.setParentMobile(values);
			break;
		case R.id.userinfo_qq:
			user.setQQ(values);
			break;
		case R.id.userinfo_wechat:
			user.setWechat(values);
			break;
		case R.id.userinfo_email:
			user.setEmail(values);
			break;
		case R.id.userinfo_addr:
			user.setAddress(values);
			break;
		}
		SharedPreferencesUtils.getInstance(this).saveUser(user);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.right_tv:
			values = edit.getText().toString();
			if(TextUtils.isEmpty(values)){
				ToastUtils.show(this, "修改内容不能为空！");
				return;
			}

            if(oldValues.equals(values)){
                ToastUtils.show(this,"请修改后再提交！");
                return;
            }

            switch (id) {
                case R.id.userinfo_mobile:
                case R.id.userinfo_parent_mobile:
                    if(!StringUtils.isPhoneNumber(values)){
                        ToastUtils.show(this,"输入的手机号码不合法");
                        return;
                    }
                    break;

                case R.id.userinfo_email:
                    if(!StringUtils.isEmail(values)){
                        ToastUtils.show(this,"输入的邮箱不合法");
                        return;
                    }
                    break;

            }

			RequestParams params = RequestParamsUtils.modifUserInfo(getString(id), values);
			HttpRequestUtils.create(this).send(URL.MODIF_USER_INFO, params, callback);
			break;
		case R.id.delete_iv:
			edit.setText("");
			break;
		}
	}
	
	HttpRequestCallBack<ResultInfo> callback = new HttpRequestCallBack<ResultInfo>() {
		
		@Override
		public void onSuccess(ResultInfo info, int requestCode) {
			saveValueInLocal(values);
			setResult(1000);
			finish();
		}
	};

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		if(!TextUtils.isEmpty(s)){
			ivDelete.setVisibility(View.VISIBLE);
		}else{
			ivDelete.setVisibility(View.GONE);
		}
		
	}

	@Override
	public void afterTextChanged(Editable s) {
		
	}
}
