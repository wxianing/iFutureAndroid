package com.meten.ifuture.activity;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.http.RequestParams;
import com.meten.ifuture.AppManager;
import com.meten.ifuture.R;
import com.meten.ifuture.activity.base.BaseHeadActivity;
import com.meten.ifuture.constant.Constant;
import com.meten.ifuture.constant.URL;
import com.meten.ifuture.dialog.ProgressDialog;
import com.meten.ifuture.http.HttpRequestCallBack;
import com.meten.ifuture.http.HttpRequestUtils;
import com.meten.ifuture.http.RequestParamsUtils;
import com.meten.ifuture.http.task.HttpTask;
import com.meten.ifuture.model.Config;
import com.meten.ifuture.model.ResultInfo;
import com.meten.ifuture.model.User;
import com.meten.ifuture.utils.CacheUtils;
import com.meten.ifuture.utils.DBHelper;
import com.meten.ifuture.utils.JsonParse;
import com.meten.ifuture.utils.ShareUtils;
import com.meten.ifuture.utils.SharedPreferencesUtils;
import com.meten.ifuture.utils.ToastUtils;

import java.util.HashMap;
import java.util.HashSet;

import cn.jpush.android.api.JPushInterface;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;

public class SettingActivity extends BaseHeadActivity implements
        OnCheckedChangeListener, PlatformActionListener {
    private static final int BIND_USER = 0x00;
    private static final int UNBIND_USER = 0x01;
    private static final int GET_THIRD_LIST = 0x02;

    private TextView tvCache;
    private CheckBox cbIsPush;
    private TextView tvWeibo;
    private TextView tvQQ;
    private TextView tvWeChart;
    private TextView tvVersion;
    private TextView tvSystemFeedback;

    private Button btnExit;
    private TextView tvAbout;
    private RelativeLayout rlCache;
    private TextView tvSmsShared;
    private TextView tvQrShared;
    private TextView tvChangePwd;

    private User user;
    private RelativeLayout rlUpdate;
    private int thirdLoginType;
    private ProgressDialog loadingDialog;

    private OnClickListener onClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.back_iv:
                    finish();
                    break;
                case R.id.exit_application_btn:
                    logout();
                    break;
                case R.id.about_tv:
                    Intent intent = new Intent(SettingActivity.this,
                            WebActivity.class);
                    intent.putExtra(Constant.URL, URL.WEB_ABOUT);
                    intent.putExtra(Constant.TITLE, getString(R.string.about));
                    startActivity(intent);
                    break;
                case R.id.cache_rl:
                    clearCache();
                    break;
                case R.id.qq_tv:
                    clickThirdBtn(Constant.QQ, QQ.NAME, v.isActivated());
                    break;
                case R.id.weibo_tv:
                    clickThirdBtn(Constant.WEIBO, SinaWeibo.NAME, v.isActivated());
                    break;
                case R.id.wechart_tv:
                    clickThirdBtn(Constant.WECHAT, Wechat.NAME, v.isActivated());
                    break;
                case R.id.versionUpate_rl:
                    HttpTask.detectionNewAppVersion(SettingActivity.this, true, true);
                    break;
			case R.id.sms_shared_tv:
				/*// 系统默认的action，用来打开默认的短信界面
				Intent smsShared = new Intent(Intent.ACTION_SENDTO);
				// 需要发短息的号码,电话号码之间用“;”隔开
				smsShared.setData(Uri.parse("smsto:"));
				smsShared.putExtra("sms_body", getString(R.string.sms_shared_body));
				startActivity(smsShared);*/
                ShareUtils.showShare(SettingActivity.this);
				break;
//			case R.id.qr_shared_tv:
//				Intent qrIntent = new Intent(SettingActivity.this, QRSharedActivity.class);
//				startActivity(qrIntent);
//				break;
                case R.id.change_pwd_tv:
                    Intent changePwd = new Intent(SettingActivity.this, BindActivity.class);
                    changePwd.putExtra(Constant.KEY_TYPE, Constant.CHANGE_PASSWORD);
                    startActivity(changePwd);
                    break;
                case R.id.system_feedback_tv:
                    Intent feedbackIt = new Intent(SettingActivity.this, SystemFeedbackActivity.class);
                    startActivity(feedbackIt);
                    break;
                default:
                    break;
            }

        }

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_setting);
        user = SharedPreferencesUtils.getInstance(this).getUser();
        initView();
        initDataSize();
        registerReceiver();
    }

    private void initView() {
        tvCache = (TextView) findViewById(R.id.cachesize_tv);
        rlCache = (RelativeLayout) findViewById(R.id.cache_rl);
        cbIsPush = (CheckBox) findViewById(R.id.on_off_tv);
        tvWeChart = (TextView) findViewById(R.id.wechart_tv);
        tvWeibo = (TextView) findViewById(R.id.weibo_tv);
        tvQQ = (TextView) findViewById(R.id.qq_tv);
        rlUpdate = (RelativeLayout) findViewById(R.id.versionUpate_rl);
        tvVersion = (TextView) findViewById(R.id.version_tv);
		tvSmsShared = (TextView) findViewById(R.id.sms_shared_tv);
//		tvQrShared = (TextView) findViewById(R.id.qr_shared_tv);
        tvChangePwd = (TextView) findViewById(R.id.change_pwd_tv);
        tvSystemFeedback = (TextView) findViewById(R.id.system_feedback_tv);
        setTitle("设置");

        hiddenRightImageView();

        btnExit = (Button) findViewById(R.id.exit_application_btn);
        tvAbout = (TextView) findViewById(R.id.about_tv);
        findViewById(R.id.back_iv).setOnClickListener(onClickListener);
        btnExit.setOnClickListener(onClickListener);
        tvAbout.setOnClickListener(onClickListener);
        rlCache.setOnClickListener(onClickListener);
        tvWeChart.setOnClickListener(onClickListener);
        tvWeibo.setOnClickListener(onClickListener);
        tvQQ.setOnClickListener(onClickListener);
        rlUpdate.setOnClickListener(onClickListener);
		tvSmsShared.setOnClickListener(onClickListener);
//		tvQrShared.setOnClickListener(onClickListener);
        tvChangePwd.setOnClickListener(onClickListener);
        tvSystemFeedback.setOnClickListener(onClickListener);

        cbIsPush.setOnCheckedChangeListener(this);

        //读取配置信息是否接收推送消息
        try {
            Config config = DBHelper.getDBUtils(this).findById(Config.class, user.getUserId() + "");
            if (config != null && !config.isPush()) {
                cbIsPush.setChecked(false);
            } else {
                cbIsPush.setChecked(true);
            }
        } catch (DbException e) {
            e.printStackTrace();
            cbIsPush.setChecked(true);
        }

        //检测是否有新版本
        HttpTask.detectionNewAppVersion(this, false, false);

        //获取第三方账号已绑定列表
        RequestParams params = RequestParamsUtils.createRequestParams();
        HttpRequestUtils.create(this).send(URL.GET_BINDED_LIST, params, GET_THIRD_LIST, callBack);

    }

    private void unbindThirdUser() {
        AlertDialog.Builder mDialog = new AlertDialog.Builder(this);
        mDialog.setTitle("解绑");
        mDialog.setMessage("是否确定解绑？");
        mDialog.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                RequestParams params = RequestParamsUtils.unbindThirdUser(thirdLoginType);
                HttpRequestUtils.create(SettingActivity.this).send(URL.UNBINDED_THIRD_USER, params, UNBIND_USER, callBack);
            }
        });
        mDialog.setNegativeButton(getString(R.string.cancel), null);
        mDialog.show();

    }


    HttpRequestCallBack<ResultInfo> callBack = new HttpRequestCallBack<ResultInfo>() {
        @Override
        public void onSuccess(ResultInfo info, int requestCode) {
            if (requestCode == GET_THIRD_LIST) {
                int[] thirdTypes = JsonParse.parseThirdTypes(info);
                if (thirdTypes != null) {
                    for (int t : thirdTypes) {
                        setThirdBtn(t, true);
                    }
                }
            } else if (requestCode == UNBIND_USER) {
                setThirdBtn(thirdLoginType, false);
                ToastUtils.show(SettingActivity.this, getString(R.string.unbind_success));
            } else if (requestCode == BIND_USER) {
                setThirdBtn(thirdLoginType, true);
                ToastUtils.show(SettingActivity.this, getString(R.string.bind_success));
            }
        }

        @Override
        public void onFailure(Context context, ResultInfo info, int requestCode) {
            if(requestCode == GET_THIRD_LIST){
                return;
            }
            super.onFailure(context, info, requestCode);
        }
    };

    private void registerReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constant.ACTION_BIND_THIRD_USER);
        filter.addAction(Constant.ACTION_NEW_VERSION);
        registerReceiver(receiver, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (Constant.ACTION_BIND_THIRD_USER.equals(intent.getAction())) {
                int thirdType = intent.getIntExtra(Constant.THIRD_TYPE, -1);
                setThirdBtn(thirdType, true);
            } else if (Constant.ACTION_NEW_VERSION.equals(intent.getAction())) {
                String versionName = intent.getStringExtra(Constant.VERSION_NAME);
                tvVersion.setText("有新版本：" + versionName);
            }

        }

    };


    private void clickThirdBtn(int type, String thirdName, boolean isBind) {
        thirdLoginType = type;
        if (isBind) {
            unbindThirdUser();
        } else {
            Platform qq = ShareSDK.getPlatform(thirdName);
            authorize(qq);
            loadingDialog = new ProgressDialog(SettingActivity.this);
            loadingDialog.show();
        }
    }


    private void logout() {
        AlertDialog.Builder mDialog = new AlertDialog.Builder(this);
        mDialog.setTitle(getString(R.string.logout));
        mDialog.setMessage("确定要注销吗?");
        mDialog.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
//                JPushInterface.stopPush(getApplicationContext());
                JPushInterface.setAliasAndTags(getApplicationContext(),"",new HashSet<String>());
                JPushInterface.clearAllNotifications(getApplicationContext());
                SharedPreferencesUtils.getInstance(SettingActivity.this)
                        .setIsAutoLogin(false);
                SharedPreferencesUtils.getInstance(SettingActivity.this).saveUser(null);
                Constant.REQUEST_CODE = "";
                Intent intent = new Intent(SettingActivity.this,
                        LoginActivity.class);
                startActivity(intent);
                AppManager.getAppManager().finishAllActivity();
            }
        });
        mDialog.setNegativeButton(getString(R.string.cancel), null);
        mDialog.show();
    }

    private void initDataSize() {
        String cacheSzieStr = CacheUtils.getCacheSizeStr(this);
        tvCache.setText(cacheSzieStr);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        Config config = new Config();
        config.setUserId(user.getUserId() + "");
        config.setPush(isChecked);
        try {
            DBHelper.getDBUtils(this).saveOrUpdate(config);
//            DbUtils.create(this).save(config);
        } catch (DbException e) {
            e.printStackTrace();
        }
        if (isChecked) {
            JPushInterface.resumePush(getApplicationContext());
            AppManager.setJpushAliasAndTags(getApplicationContext(),user);
        } else {
            JPushInterface.stopPush(getApplicationContext());
        }
    }

    private void authorize(Platform plat) {
        plat.setPlatformActionListener(this);
        if (plat.isValid()) {
            plat.removeAccount();
        }
        plat.SSOSetting(false);
        plat.showUser(null);
    }

    private void dismissLoadingDialog() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }


    @Override
    public void onCancel(Platform arg0, int arg1) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ToastUtils.show(SettingActivity.this, "取消授权");
                dismissLoadingDialog();
            }
        });
    }

    @Override
    public void onComplete(final Platform pla, int arg1,
                           HashMap<String, Object> res) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
//				ToastUtils.show(SettingActivity.this, "授权成功");
                RequestParams params = RequestParamsUtils.bindThirdUser("", user.getMobile(), thirdLoginType, pla.getDb().getUserId(), pla.getDb().getUserName(), pla.getDb().getUserIcon());
                HttpRequestUtils.create(SettingActivity.this).send(URL.BIND_THIED_USER, params, BIND_USER, callBack);
                dismissLoadingDialog();
            }
        });

    }

    @Override
    public void onError(Platform arg0, int arg1, Throwable arg2) {

        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                ToastUtils.show(SettingActivity.this, "授权失败");
                dismissLoadingDialog();
            }
        });
    }


    private void setThirdBtn(int type, boolean isBind) {
        if (type == Constant.QQ) {
            tvQQ.setActivated(isBind);
            if(isBind){
                tvQQ.setText(getString(R.string.binded));
            }else{
                tvQQ.setText(getString(R.string.bind));
            }
        } else if (type == Constant.WEIBO) {
            tvWeibo.setActivated(isBind);
            if(isBind){
                tvWeibo.setText(getString(R.string.binded));
            }else{
                tvWeibo.setText(getString(R.string.bind));
            }
        } else if (type == Constant.WECHAT) {
            tvWeChart.setActivated(isBind);
            if(isBind){
                tvWeChart.setText(getString(R.string.binded));
            }else{
                tvWeChart.setText(getString(R.string.bind));
            }
        }
    }

    private void clearCache() {
        new AlertDialog.Builder(SettingActivity.this)
                .setTitle(getString(R.string.clearCache))
                .setMessage(getString(R.string.clearCacheHint))
                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        CacheUtils.clearCache(SettingActivity.this);
                        tvCache.setText(CacheUtils
                                .getCacheSizeStr(SettingActivity.this));
                    }
                }).setNegativeButton(getString(R.string.no), null).create().show();

    }

}
