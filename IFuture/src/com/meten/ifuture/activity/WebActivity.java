package com.meten.ifuture.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.meten.ifuture.R;
import com.meten.ifuture.activity.base.BaseHeadActivity;
import com.meten.ifuture.activity.teacher.StudentInfoActivity;
import com.meten.ifuture.constant.Constant;
import com.meten.ifuture.constant.URL;
import com.meten.ifuture.model.User;
import com.meten.ifuture.utils.LogUtils;
import com.meten.ifuture.utils.ToastUtils;
import com.meten.ifuture.view.ProgressWebView;

public class WebActivity extends BaseHeadActivity implements OnClickListener, ProgressWebView.OnTitleDisplayCallBack {
    private ProgressWebView progressWebView;
    private WebView mWebView;
    private String currentTitle;
    private String currentUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview);

//		mWebView.loadUrl("http://172.19.3.33:8001/mobile/news");
//		mWebView.loadUrl("https://www.baidu.com");
        currentUrl = getIntent().getStringExtra(Constant.URL);
        initView();
        LogUtils.e("url:" + currentUrl);
        mWebView.loadUrl(currentUrl);
//        mWebView.loadDataWithBaseURL("example-app:", url, null, "UTF-8", null);
        currentTitle = getIntent().getStringExtra(Constant.TITLE);
        if (currentTitle != null) {
            setTitle(currentTitle);
        }
    }

    private void initView() {
        progressWebView = (ProgressWebView) findViewById(R.id.webview);
        mWebView = progressWebView.getWebView();
        progressWebView.setOnTitleDisplayCallBack(this);
        initWebView();
        hiddenRightImageView();
//		setLeftClickListener(this);

        if(!currentUrl.startsWith(URL.WEB_HOST)){
            getLeftImageView().setImageResource(R.drawable.close_selector);
        }
    }

    private void initWebView() {
        // 访问页面中有JavaScript,必须设置支持JavaScript
        mWebView.getSettings().setJavaScriptEnabled(true);

        // 启用mWebView访问文件数据
        mWebView.getSettings().setAllowFileAccess(true);
        // 设置支持缩放，该缩放只支持缩放按钮实现的放大与缩小（设置缩放有利于图标大小正常的显示）
        if(currentUrl.startsWith(URL.WEB_HOST)){
            mWebView.getSettings().setSupportZoom(false);
            mWebView.getSettings().setBuiltInZoomControls(false);
        }else{
            mWebView.getSettings().setSupportZoom(true);
            mWebView.getSettings().setBuiltInZoomControls(true);
        }

        // 设置缩放比例
        // mWebView.setInitialScale(35);
        // 设置是否显示网络图像---true,封锁网络图片，不显示 false----允许显示网络图片
        mWebView.getSettings().setBlockNetworkImage(false);
        // 支持多窗口
        mWebView.getSettings().supportMultipleWindows();
        // 设置自动加载图片
        mWebView.getSettings().setLoadsImagesAutomatically(true);
        // 如果需要用户输入账号密码，必须设置支持手势焦点
        mWebView.requestFocusFromTouch();
        // 取消滚动条
        mWebView.setScrollBarStyle(mWebView.SCROLLBARS_OUTSIDE_OVERLAY);

        // 设置缓存模式
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        mWebView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        // 开启 DOM storage API 功能
        mWebView.getSettings().setDomStorageEnabled(true);
        // 开启 database storage API 功能
        mWebView.getSettings().setDatabaseEnabled(true);

        String cacheDirPath = getFilesDir().getAbsolutePath() + "cache";
        System.out.println("cacheDirPath=" + cacheDirPath);
        // 设置数据库缓存路径
        mWebView.getSettings().setDatabasePath(cacheDirPath);
        // 设置 Application Caches 缓存目录
        mWebView.getSettings().setAppCachePath(cacheDirPath);
        // 开启 Application Caches 功能
        mWebView.getSettings().setAppCacheEnabled(true);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);


        mWebView.setWebViewClient(new WebViewClient() {


            // 用于加载新WebView,返回true代表着用完就消费掉
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                LogUtils.e("shouldOverrideUrlLoading url:" + url);
                if (currentUrl.startsWith(URL.WEB_HOST)) {
                    Intent intent = new Intent(WebActivity.this,
                            WebActivity.class);
                    intent.putExtra(Constant.URL, url);
                    startActivity(intent);
                } else {
                    view.loadUrl(url);
                }
                return true;
            }


            // 用于加载新Webview之后，一般在此消除缓冲区
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

            // 加载错误时调用，一般提示错误信息
            @Override
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);

                ToastUtils.show(WebActivity.this, "页面加载错误...");
            }

        });

        mWebView.addJavascriptInterface(this, "external");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }

    }


    @JavascriptInterface
    public void click(final int userId, final String name, final String photo) {
//        ToastUtils.show(this,"userId:"+userId+" name:"+name+" photo:"+photo);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (userId > 0 && !TextUtils.isEmpty(name)) {
                    User stu = new User();
                    stu.setUserId(userId);
                    stu.setCnName(name);
                    stu.setPhoto(photo);
                    Intent intent = new Intent(WebActivity.this, StudentInfoActivity.class);
                    intent.putExtra(Constant.STUDENT_KEY, stu);
                    intent.putExtra(Constant.PAST_STUDENT,true);
                    startActivity(intent);
                    overridePendingTransition(R.anim.push_bottom_in, 0);
                }
            }
        });

    }


    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onClick(View v) {
        onBackPressed();
    }

    @Override
    public void getTiltle(String title) {
        if (currentTitle == null) {
            setTitle(title);
        }
    }
}
