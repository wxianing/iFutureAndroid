package com.meten.ifuture.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.http.RequestParams;
import com.meten.ifuture.AppManager;
import com.meten.ifuture.R;
import com.meten.ifuture.activity.base.BaseActivity;
import com.meten.ifuture.constant.Constant;
import com.meten.ifuture.constant.URL;
import com.meten.ifuture.dialog.ProgressDialog;
import com.meten.ifuture.http.HttpRequestCallBack;
import com.meten.ifuture.http.HttpRequestUtils;
import com.meten.ifuture.http.RequestParamsUtils;
import com.meten.ifuture.model.ResultInfo;
import com.meten.ifuture.utils.LogUtils;
import com.meten.ifuture.utils.SharedPreferencesUtils;
import com.meten.ifuture.utils.ToastUtils;

import java.util.HashMap;

import cn.jpush.android.api.JPushInterface;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;

public class LoginActivity extends BaseActivity implements OnClickListener,
        PlatformActionListener, OnFocusChangeListener {

    private Button btn_login;
    private EditText et_username;
    private EditText et_password;
    private ImageView tv_qq_login;
    private ImageView tv_weibo_login;
    private ImageView tv_wechart_login;
    private TextView tvForgetPwd;
    private TextView tvUserName;
    private TextView tvPwd;
    private Platform pla;
    private String strUserName;
    private String strPwd;
    private int thirdType;
    private CallBack callback;
    private ProgressDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        SharedPreferencesUtils.getInstance(this).saveUser(null);
        Constant.REQUEST_CODE = "";
        JPushInterface.clearAllNotifications(getApplicationContext());
        initView();
    }

    private void initView() {
        btn_login = (Button) findViewById(R.id.login_btn);
        et_password = (EditText) findViewById(R.id.password_et);
        et_username = (EditText) findViewById(R.id.username_et);
        tv_qq_login = (ImageView) findViewById(R.id.qq_login_tv);
        tv_wechart_login = (ImageView) findViewById(R.id.wechart_login_tv);
        tv_weibo_login = (ImageView) findViewById(R.id.weibo_login_tv);
        tvForgetPwd = (TextView) findViewById(R.id.forget_password_tv);
        tvUserName = (TextView) findViewById(R.id.username_tv);
        tvPwd = (TextView) findViewById(R.id.password_tv);

        String userName = SharedPreferencesUtils.getInstance(this)
                .getUserName();
        String pwd = SharedPreferencesUtils.getInstance(this).getPassword();
        if (!TextUtils.isEmpty(userName)) {
            et_username.setText(userName);
            et_password.setText(pwd);
            onFocusChange(et_username, true);
            onFocusChange(et_password, true);
        }

        btn_login.setOnClickListener(this);
        tv_qq_login.setOnClickListener(this);
        tv_wechart_login.setOnClickListener(this);
        tv_weibo_login.setOnClickListener(this);
        tvForgetPwd.setOnClickListener(this);
        tvUserName.setOnClickListener(this);
        tvPwd.setOnClickListener(this);

        et_username.setOnFocusChangeListener(this);
        et_password.setOnFocusChangeListener(this);

        callback = new CallBack();

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.login_btn:
                // Log.e("CMAD",
                // "width:"+btn_login.getHeight()+"  "+findViewById(R.id.password_rl).getHeight());
                strUserName = et_username.getText().toString();
                strPwd = et_password.getText().toString();
                if (TextUtils.isEmpty(strUserName) || TextUtils.isEmpty(strPwd)) {
                    Toast.makeText(this, "用户名或密码不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                }
                RequestParams params = RequestParamsUtils
                        .login(strUserName, strPwd);
                HttpRequestUtils.create(this).send(URL.LOGIN, params, callback);

                break;
            case R.id.qq_login_tv:
                Platform qq = ShareSDK.getPlatform(QQ.NAME);
                thirdType = Constant.QQ;
                authorize(qq);
                loadingDialog = new ProgressDialog(this);
                loadingDialog.show();
                break;
            case R.id.weibo_login_tv:
                Platform sina = ShareSDK.getPlatform(SinaWeibo.NAME);
                thirdType = Constant.WEIBO;
                authorize(sina);
                loadingDialog = new ProgressDialog(this);
                loadingDialog.show();
                break;
            case R.id.wechart_login_tv:
                Platform wechart = ShareSDK.getPlatform(Wechat.NAME);
                thirdType = Constant.WECHAT;
                authorize(wechart);
                loadingDialog = new ProgressDialog(this);
                loadingDialog.show();
                break;
            case R.id.forget_password_tv:
                Intent it = new Intent(this, BindActivity.class);
                it.putExtra(Constant.KEY_TYPE, Constant.RESET_PASSWORD);
                startActivity(it);
                break;
            case R.id.username_tv:
                et_username.requestFocus();
                break;
            case R.id.password_tv:
                et_password.requestFocus();
                break;
        }
    }

    class CallBack extends HttpRequestCallBack<ResultInfo> {

        @Override
        public void onSuccess(ResultInfo info, int requestCode) {

            SharedPreferencesUtils.getInstance(LoginActivity.this)
                    .saveUserNameAndPwd(strUserName, strPwd);
            AppManager.launchMainActivity(LoginActivity.this,info);
        }

        @Override
        public void onFailure(Context context, ResultInfo info, int requestCode) {
            super.onFailure(context, info, requestCode);
            if(requestCode == thirdType && info.getCode() == Constant.UNBIND){
                Intent intent = new Intent(LoginActivity.this,BindActivity.class);
                LogUtils.e(" onFailure userId:"+pla.getDb().getUserId());
                intent.putExtra(Constant.KEY_TYPE,Constant.BIND_USER);
                intent.putExtra(Constant.USER_ID,pla.getDb().getUserId());
                intent.putExtra(Constant.NICK_NAME,pla.getDb().getUserName());
                intent.putExtra(Constant.PIC_URL,pla.getDb().getUserIcon());
                intent.putExtra(Constant.THIRD_TYPE,thirdType);
                startActivity(intent);

            }
        }
    };

    private void authorize(Platform plat) {
        if (plat.isValid()) {
            plat.removeAccount();
        }
        plat.setPlatformActionListener(this);
        plat.SSOSetting(false);
        plat.showUser(null);
    }

    private void dismissLoadingDialog(){
        if(loadingDialog != null && loadingDialog.isShowing()){
            loadingDialog.dismiss();
        }
    }

    @Override
    public void onCancel(Platform arg0, int arg1) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ToastUtils.show(LoginActivity.this, "取消授权");
                dismissLoadingDialog();
            }
        });
    }

    @Override
    public void onComplete(final Platform pla, int arg1,
                           HashMap<String, Object> res) {
        this.pla = pla;
        LogUtils.e(res.toString());
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
//                ToastUtils.show(LoginActivity.this, "授权成功");
                LogUtils.e("userId:" + pla.getDb().getUserId());
                RequestParams params = RequestParamsUtils.loginByThird(pla.getDb().getUserId(), thirdType);
                HttpRequestUtils.create(LoginActivity.this).send(URL.LOGIN_BY_THIRD, params, thirdType, callback);
                dismissLoadingDialog();
            }
        });


    }

    @Override
    public void onError(Platform arg0, int arg1, Throwable arg2) {
        // TODO Auto-generated method stub

        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                ToastUtils.show(LoginActivity.this, "授权失败");
                dismissLoadingDialog();
            }
        });
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()) {
            case R.id.username_et:
                setHintText(hasFocus, et_username, tvUserName, getString(R.string.username));
                break;
            case R.id.password_et:
                setHintText(hasFocus, et_password, tvPwd, getString(R.string.password));
                break;
        }

    }

    private void setHintText(boolean hasFocus, EditText edit, TextView tv,
                             String text) {
        if (hasFocus) {
            tv.setText("");
        } else if (TextUtils.isEmpty(edit.getText().toString())) {
            tv.setText(text);
        }
    }

}
