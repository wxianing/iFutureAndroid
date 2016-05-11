package com.meten.ifuture.activity.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.meten.ifuture.R;
import com.meten.imanager.pulltorefresh.library.PullToRefreshBase;
import com.meten.imanager.pulltorefresh.library.PullToRefreshListView;

public class BaseListActivity extends BaseHeadActivity {
	private PullToRefreshListView mListView;
    private LinearLayout topContainer;
    private LinearLayout bottomContainer;
    private LinearLayout floatBottomContainer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_activity);
		mListView = (PullToRefreshListView) findViewById(R.id.listview);
        topContainer = (LinearLayout) findViewById(R.id.top_cantainer);
        bottomContainer = (LinearLayout) findViewById(R.id.bottom_cantainer);
        floatBottomContainer = (LinearLayout) findViewById(R.id.float_bottom_cantainer);

        mListView.setMode(PullToRefreshBase.Mode.DISABLED);
        mListView.getRefreshableView().setOverScrollMode(View.OVER_SCROLL_NEVER);
    }

	public void setAdapter(BaseAdapter adapter) {
		mListView.setAdapter(adapter);
	}

	public ListView getListView() {
		return mListView.getRefreshableView();
	}

    public PullToRefreshListView getPullToRefreshListView(){
        return mListView;
    }


	public void setOnItemClickListener(OnItemClickListener listener) {
		mListView.setOnItemClickListener(listener);
	}

    public void addBottomView(int viewRes){
        LayoutInflater.from(this).inflate(viewRes,bottomContainer);
    }

    public void addFloatBottomView(int viewRes){
        LayoutInflater.from(this).inflate(viewRes,floatBottomContainer);
    }

    public void addBottomView(View view){
        bottomContainer.addView(view);
    }

    public void addTopView(int viewRes){
        LayoutInflater.from(this).inflate(viewRes,topContainer);
    }

    public void addTopView(View view){
        topContainer.addView(view);
    }

    public void setMode(PullToRefreshBase.Mode mode){
        mListView.setMode(mode);
    }

    public void setOnRefreshListener(PullToRefreshBase.OnRefreshListener2<ListView> listener){
        mListView.setOnRefreshListener(listener);
    }

    public void onRefreshComplete(){
        mListView.onRefreshComplete();
    }

}
