package com.meten.ifuture.adapter;

import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

public class ScrollAdViewAdapter extends PagerAdapter {
	
	private List<View> views;
	
	public ScrollAdViewAdapter(List<View> views) {
		this.views = views;
	}
	
	@Override 
	public int getCount() {
		// TODO Auto-generated method stub
		return views.size();
	}

	@Override
	public boolean isViewFromObject(View view, Object obj) {
		// TODO Auto-generated method stub
		return view == obj;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View)object);
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		// TODO Auto-generated method stub
		View view = views.get(position);
		container.addView(view);
		return view;
	}
	
	

}
