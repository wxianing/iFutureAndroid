package com.meten.ifuture.view;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.meten.ifuture.R;

@SuppressWarnings("deprecation")
public class ProgressWebView extends LinearLayout {

    private ProgressBar progressbar;
    private OnTitleDisplayCallBack callBack;
    private WebView mWebView;

    public ProgressWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(LinearLayout.VERTICAL);
        progressbar = new ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal);
        progressbar.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, getResources().getDimensionPixelOffset(R.dimen.dp2)));
        addView(progressbar);
        mWebView = new WebView(context);
        mWebView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        //        setWebViewClient(new WebViewClient(){});
        mWebView.setWebChromeClient(new WebChromeClient());
        addView(mWebView);
    }

    public WebView getWebView(){
        return mWebView;
    }


    public void setOnTitleDisplayCallBack(OnTitleDisplayCallBack callBack){
        this.callBack = callBack;
    }


    public class WebChromeClient extends android.webkit.WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
                progressbar.setVisibility(GONE);
            } else {
                if (progressbar.getVisibility() == GONE)
                    progressbar.setVisibility(VISIBLE);
                progressbar.setProgress(newProgress);
            }
            super.onProgressChanged(view, newProgress);
        }



        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            if(callBack != null){
                callBack.getTiltle(title);
            }
        }
    }


    public interface OnTitleDisplayCallBack{
        public void getTiltle(String title);
    }

}
