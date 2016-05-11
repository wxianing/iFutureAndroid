package com.meten.ifuture.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.lidroid.xutils.http.RequestParams;
import com.meten.ifuture.AppManager;
import com.meten.ifuture.R;
import com.meten.ifuture.activity.base.BaseHeadActivity;
import com.meten.ifuture.constant.Constant;
import com.meten.ifuture.constant.URL;
import com.meten.ifuture.http.HttpRequestCallBack;
import com.meten.ifuture.http.HttpRequestUtils;
import com.meten.ifuture.http.RequestParamsUtils;
import com.meten.ifuture.model.ResultInfo;
import com.meten.ifuture.utils.ToastUtils;

public class SystemFeedbackActivity extends BaseHeadActivity implements View.OnClickListener, TextWatcher {
    private ImageView ivRight;
    private EditText mEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.system_feedback_activity);

        ivRight = getRightImageView();
        mEditText = (EditText) findViewById(R.id.edit);

        setTitle(getString(R.string.system_feedback));

        ivRight.setImageResource(R.drawable.ic_sure_selector);
        ivRight.setOnClickListener(this);

        mEditText.addTextChangedListener(this);
    }


    @Override
    public void onClick(View v) {
        String text = mEditText.getText().toString();
        if(TextUtils.isEmpty(text) || TextUtils.isEmpty(text.trim())){
            ToastUtils.show(this,getString(R.string.feedback_not_empty));
            return;
        }
        if(text.length() > Constant.MAX_COMPLAIN_TEXT_SIZE){
            ToastUtils.show(this,"反馈内容不能大于256字符！");
            return;
        }

        RequestParams params = RequestParamsUtils.feedback(text, AppManager.getAppVersionCode(this));
        HttpRequestUtils.create(this).send(URL.FEEDBACK,params,callBack);
    }

    HttpRequestCallBack<ResultInfo> callBack = new HttpRequestCallBack<ResultInfo>() {
        @Override
        public void onSuccess(ResultInfo info, int requestCode) {
            ToastUtils.show(SystemFeedbackActivity.this,getString(R.string.feedback_success));
            finish();
        }
    };

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if(s.length() > Constant.MAX_COMPLAIN_TEXT_SIZE){
            ToastUtils.show(this,"反馈内容最大为256个字符！");
            mEditText.setText(s.subSequence(0,256));
            mEditText.setSelection(256);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
