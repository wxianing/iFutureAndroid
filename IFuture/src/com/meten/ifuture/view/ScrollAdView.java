package com.meten.ifuture.view;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.http.RequestParams;
import com.meten.ifuture.R;
import com.meten.ifuture.activity.WebActivity;
import com.meten.ifuture.adapter.ScrollAdViewAdapter;
import com.meten.ifuture.constant.Constant;
import com.meten.ifuture.constant.URL;
import com.meten.ifuture.http.HttpRequestCallBack;
import com.meten.ifuture.http.HttpRequestUtils;
import com.meten.ifuture.http.RequestParamsUtils;
import com.meten.ifuture.model.AD;
import com.meten.ifuture.model.ResultInfo;
import com.meten.ifuture.utils.JsonParse;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ScrollAdView extends LinearLayout implements OnPageChangeListener {

	private ViewPager mViewPager;
	private RadioGroup pointGroup;
	private ScrollAdViewAdapter adapter;
	private int totalPage;
	private int adIndex;
	private Timer timer;
	private boolean isScroll;
	private long period = 2000;

	public ScrollAdView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}


	private void initView() {
		LayoutInflater.from(getContext())
				.inflate(R.layout.scroll_ad_view, this);
		mViewPager = (ViewPager) findViewById(R.id.viewpager);
		pointGroup = (RadioGroup) findViewById(R.id.point_rg);

		mViewPager.setOnPageChangeListener(this);
		mViewPager.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					isScroll = false;
					break;
				case MotionEvent.ACTION_UP:
					isScroll = true;
					break;
				}
				return false;
			}
		});

        RequestParams params = RequestParamsUtils.createRequestParams();
        HttpRequestUtils.create(getContext()).isShowLoadingDilag(false).send(URL.GET_AD_LIST,params,callback);
	}

    HttpRequestCallBack<ResultInfo> callback = new HttpRequestCallBack<ResultInfo>() {
        @Override
        public void onSuccess(ResultInfo info, int requestCode) {
            List<AD> data = JsonParse.parseToObject(info,new TypeToken<List<AD>>(){}.getType());
            setData(data);
        }
    };

	public void setData(List<AD> data) {
		if (data == null) {
			return;
		}
		List<View> views = new ArrayList<View>();
		totalPage = data.size();
		for (AD ad : data) {
			ImageView img = new ImageView(getContext());
			android.view.ViewGroup.LayoutParams params = new LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			img.setLayoutParams(params);
			img.setScaleType(ScaleType.FIT_XY);
            BitmapUtils bitmapUtils = new BitmapUtils(getContext());
            bitmapUtils.configDefaultLoadingImage(R.drawable.ads_default);
            bitmapUtils.configDefaultLoadFailedImage(R.drawable.ads_default);
            bitmapUtils.display(img,ad.getPhoto());
			img.setOnClickListener(new ADSOnclick(ad));
			views.add(img);

			RadioButton rb = new RadioButton(getContext());
			rb.setButtonDrawable(R.drawable.scroll_ad_point_selector);
			pointGroup.addView(rb);
		}
		adapter = new ScrollAdViewAdapter(views);
		mViewPager.setAdapter(adapter);
		// pointGroup.check(1);
		((RadioButton) pointGroup.getChildAt(0)).setChecked(true);
	}

	class ADSOnclick implements OnClickListener {
		private AD ad;

		public ADSOnclick(AD ad) {
			this.ad = ad;
		}

		@Override
		public void onClick(View v) {
			// 跳转到广告展示页面
			if (!TextUtils.isEmpty(ad.getLink())) {
				Intent intent = new Intent(getContext(),
				WebActivity.class);
				intent.putExtra(Constant.URL, ad.getLink());
                intent.putExtra(Constant.TITLE, ad.getTitle());
				getContext().startActivity(intent);
			}
		}
	};

	/**
	 * 测试用 传入图片资源数组
	 * 
	 * @param ress
	 */
	public void setData(int[] ress) {
		List<View> views = new ArrayList<View>();
		totalPage = ress.length;
		for (int res : ress) {
			ImageView img = new ImageView(getContext());
			android.view.ViewGroup.LayoutParams params = new LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			img.setLayoutParams(params);
			img.setBackgroundResource(res);
			views.add(img);

			RadioButton rb = new RadioButton(getContext());
			rb.setButtonDrawable(R.drawable.scroll_ad_point_selector);
			pointGroup.addView(rb);
		}
		adapter = new ScrollAdViewAdapter(views);
		mViewPager.setAdapter(adapter);
		// pointGroup.check(1);
		((RadioButton) pointGroup.getChildAt(0)).setChecked(true);
	}

	/**
	 * 设置广告轮播时间
	 * 
	 * @param period
	 *            毫秒
	 */
	public void setScrollPeriod(long period) {
		this.period = period;
	}

	/**
	 * 开始轮播
	 */
	public void startScroll() {
		timer = new Timer();
		timer.schedule(task, 2000, period);
	}

	TimerTask task = new TimerTask() {

		@Override
		public void run() {
			if (isScroll) {
				handler.sendEmptyMessage(0);
			}
		}
	};

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (totalPage == 0) {
				return;
			}
			adIndex++;
			adIndex = adIndex % totalPage;
			mViewPager.setCurrentItem(adIndex);
		}
	};

	/**
	 * 允许轮播
	 */
	public void onResume() {
		isScroll = true;
	}

	/**
	 * 暂停轮播
	 */
	public void onPause() {
		isScroll = false;
	}

	/**
	 * 取消轮播
	 */
	public void onDestroy() {
		timer.cancel();
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageSelected(int position) {
		// TODO Auto-generated method stub
		// pointGroup.check(position + 1);
		Log.e("CMMM", "position:" + position);
		((RadioButton) pointGroup.getChildAt(position)).setChecked(true);
	}

}
