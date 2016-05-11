package com.meten.ifuture.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
import com.meten.ifuture.utils.LogUtils;
import com.meten.ifuture.utils.ToastUtils;

public class BindActivity extends BaseHeadActivity implements OnClickListener {
    private static final int GET_VALIDATE_CODE = 0x00;
    private static final int RESET_PWD = 0x10;
    private static final int BIND_USER = 0x02;
    private static final int CHANGE_PWD = 0x03;
    private EditText etData;
    private EditText etNewPwd;
    private EditText etNewPwdAgain;
    private TextView tvWarn;
    private Button btnCommit;
    private int type;
    private int handType = GET_VALIDATE_CODE;
    private CallBack callback;
    private String mobile;
    private String thirdUserId;
    private String nikName;
    private String picUrl;
    private int thirdType;
    private String code = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bind_activity);
        type = getIntent().getIntExtra(Constant.KEY_TYPE,
                Constant.RESET_PASSWORD);
        if (type == Constant.CHANGE_PASSWORD) {
            handType = CHANGE_PWD;
        }else if(type == Constant.BIND_USER){
            Intent intent = getIntent();
            thirdUserId = intent.getStringExtra(Constant.USER_ID);
            nikName = intent.getStringExtra(Constant.NICK_NAME);
            picUrl = intent.getStringExtra(Constant.PIC_URL);
            thirdType = intent.getIntExtra(Constant.THIRD_TYPE,Constant.QQ);
        }
        initView();
    }

    private void initView() {
        etData = (EditText) findViewById(R.id.data_edit);
        etNewPwd = (EditText) findViewById(R.id.new_pwd_edit);
        etNewPwdAgain = (EditText) findViewById(R.id.new_pwd_again_edit);
        tvWarn = (TextView) findViewById(R.id.warn_tv);
        btnCommit = (Button) findViewById(R.id.commit_btn);

        setEditTextView();
        hiddenRightImageView();

        btnCommit.setOnClickListener(this);

        callback = new CallBack();
    }

    private void setEditTextView() {
        tvWarn.setVisibility(View.GONE);
        etData.setText("");
        etNewPwd.setText("");
        etNewPwdAgain.setText("");
        switch (handType) {
            case GET_VALIDATE_CODE:
                etData.setInputType(EditorInfo.TYPE_CLASS_PHONE);
                etNewPwd.setVisibility(View.GONE);
                etNewPwdAgain.setVisibility(View.GONE);
                etData.setHint(getString(R.string.please_input_mobile));
                setTitle(getString(R.string.get_validata_code));
                btnCommit.setText(getString(R.string.get_validata_code));
                break;
            case RESET_PWD:
                etData.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
                etNewPwd.setVisibility(View.VISIBLE);
                etNewPwdAgain.setVisibility(View.VISIBLE);
                etData.setHint(getString(R.string.please_input_validata_code));
                setTitle(getString(R.string.reset_password));
                btnCommit.setText(getString(R.string.reset_password));
                break;
            case CHANGE_PWD:
                etData.setInputType(EditorInfo.TYPE_CLASS_TEXT|EditorInfo.TYPE_TEXT_VARIATION_PASSWORD);
                etNewPwd.setVisibility(View.VISIBLE);
                etNewPwdAgain.setVisibility(View.VISIBLE);
                etData.setHint(getString(R.string.please_input_old_password));
                btnCommit.setText(getString(R.string.change_password));
                setTitle(getString(R.string.change_password));
                break;
            case BIND_USER:
                etData.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
                etNewPwd.setVisibility(View.GONE);
                etNewPwdAgain.setVisibility(View.GONE);
                etData.setHint(getString(R.string.please_input_validata_code));
                setTitle(getString(R.string.please_input_validata_code));
                btnCommit.setText(getString(R.string.bind));
                break;

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.commit_btn:
                commit();
                break;
        }

    }

    private void commit() {
        switch (handType) {
            case GET_VALIDATE_CODE:
                mobile = etData.getText().toString();
                if (TextUtils.isEmpty(mobile)) {
                    ToastUtils.show(this, getString(R.string.mobile_not_empty));
                    return;
                }
                RequestParams params = RequestParamsUtils.getValidateCode(type,
                        mobile);
                HttpRequestUtils.create(this).send(URL.GET_VALIDATE_CODE, params,
                        handType, callback);
                break;
            case RESET_PWD:
                code = etData.getText().toString();
                String newPwd = etNewPwd.getText().toString();
                String newPwdAgain = etNewPwdAgain.getText().toString();
                if (TextUtils.isEmpty(code)) {
                    ToastUtils.show(this, getString(R.string.validata_code_not_empty));
                    return;
                }
                if (TextUtils.isEmpty(newPwd) || TextUtils.isEmpty(newPwdAgain)) {
                    ToastUtils.show(this, getString(R.string.old_password_not_empty));
                    return;
                }
                if (!newPwd.equals(newPwdAgain)) {
                    ToastUtils.show(this, getString(R.string.twice_password_unlikeness));
                    return;
                }
                RequestParams resetParams = RequestParamsUtils.resetPassword(mobile, code, newPwd);
                HttpRequestUtils.create(this).send(URL.RESET_PASSWORD, resetParams, handType, callback);

                break;
            case CHANGE_PWD:
                String oldPwd = etData.getText().toString();
                newPwd = etNewPwd.getText().toString();
                newPwdAgain = etNewPwdAgain.getText().toString();
                if (TextUtils.isEmpty(oldPwd)) {
                    ToastUtils.show(this, getString(R.string.old_password_not_empty));
                    return;
                }
                if (TextUtils.isEmpty(newPwd) || TextUtils.isEmpty(newPwdAgain)) {
                    ToastUtils.show(this, getString(R.string.new_password_not_empty));
                    return;
                }
                if (!newPwd.equals(newPwdAgain)) {
                    ToastUtils.show(this, getString(R.string.twice_password_unlikeness));
                    return;
                }
                RequestParams changePwdParams = RequestParamsUtils.changePassword(oldPwd,newPwd);
                HttpRequestUtils.create(this).send(URL.CHANGE_PASSWORD, changePwdParams, handType, callback);
                break;
            case BIND_USER:
                code = etData.getText().toString();
                if(type == Constant.BIND_USER){
                    if(TextUtils.isEmpty(code)){
                        ToastUtils.show(this,getString(R.string.validata_code_not_empty));
                        return;
                    }
                }
                LogUtils.e(" bind userId:" + thirdUserId);
                RequestParams bindParams = RequestParamsUtils.bindThirdUser(code,mobile,thirdType,thirdUserId,nikName,picUrl);
                HttpRequestUtils.create(this).send(URL.BIND_THIED_USER,bindParams,handType,callback);
                break;
        }

    }

    class CallBack extends HttpRequestCallBack<ResultInfo> {

        @Override
        public void onSuccess(ResultInfo info, int requestCode) {
            switch (requestCode) {
                case GET_VALIDATE_CODE:
                    if (type == Constant.RESET_PASSWORD) {
                        handType = RESET_PWD;
                    } else {
                        handType = BIND_USER;
                    }
                    setEditTextView();
                    break;
                case RESET_PWD:
                    ToastUtils.show(BindActivity.this, getString(R.string.reset_password_success));
                    finish();
                    break;
                case CHANGE_PWD:
                    ToastUtils.show(BindActivity.this, getString(R.string.change_passwrod_success));
                    Intent it = new Intent(BindActivity.this,LoginActivity.class);
                    startActivity(it);
                    AppManager.getAppManager().finishAllActivity();
                    break;
                case BIND_USER:
                    AppManager.launchMainActivity(BindActivity.this,info);
                    break;

            }

        }

        public void onFailure(android.content.Context context, ResultInfo info,
                              int requestCode) {
            super.onFailure(context, info, requestCode);
            tvWarn.setVisibility(View.VISIBLE);
            tvWarn.setText(info.getMsg());
        }
    }

}
