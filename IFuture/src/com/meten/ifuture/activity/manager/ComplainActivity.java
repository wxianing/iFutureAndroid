package com.meten.ifuture.activity.manager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.http.RequestParams;
import com.meten.ifuture.R;
import com.meten.ifuture.activity.base.BaseHeadActivity;
import com.meten.ifuture.adapter.manager.TeacherAdapter;
import com.meten.ifuture.constant.Constant;
import com.meten.ifuture.constant.URL;
import com.meten.ifuture.http.HttpRequestCallBack;
import com.meten.ifuture.http.HttpRequestUtils;
import com.meten.ifuture.http.RequestParamsUtils;
import com.meten.ifuture.model.ResultInfo;
import com.meten.ifuture.model.Teacher;
import com.meten.ifuture.utils.JsonParse;
import com.meten.ifuture.view.FilterView;
import com.meten.imanager.pulltorefresh.library.PullToRefreshBase;

import java.util.List;

/**
 * Created by Cmad on 2015/3/11.
 */
public class ComplainActivity extends BaseHeadActivity implements PullToRefreshBase.OnRefreshListener2<ListView>,FilterView.OnCheckedListener, AdapterView.OnItemClickListener {
    private FilterView filterView;
    private TeacherAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        filterView = new FilterView(this);
        setContentView(filterView);
        
        initView();
    }

    private void initView() {
        setTitle(getString(R.string.student_complain));
        hiddenRightImageView();

        adapter = new TeacherAdapter(this);
        adapter.setType(Constant.MANAGER);
        filterView.getPullToRefreshListView().setAdapter(adapter);

        filterView.getPullToRefreshListView().setMode(PullToRefreshBase.Mode.BOTH);
        filterView.getPullToRefreshListView().setOnRefreshListener(this);

        filterView.setOnCheckedListener(this);
        filterView.getPullToRefreshListView().setOnItemClickListener(this);
        filterView.hiddenListView();

        loadData(true, false);
    }

    private void loadData(boolean isShowLoading,boolean isLoadMore) {
        int requestCode ;
        if(isLoadMore){
            requestCode = Constant.LOADMORE;
        }else{
            requestCode = Constant.REFRESH;
        }
        RequestParams params = RequestParamsUtils.getPraiseList(filterView.getCheckedAreaId(), filterView.getCheckedRoleId(), isLoadMore);
        HttpRequestUtils.create(this).isShowLoadingDilag(isShowLoading).send(URL.GET_COMPLAIN_LIST, params, requestCode, callBack);
    }

    HttpRequestCallBack<ResultInfo> callBack = new HttpRequestCallBack<ResultInfo>() {
        @Override
        public void onSuccess(ResultInfo info, int requestCode) {

            List<Teacher> data = JsonParse.parseDataOfPageToData(info, new TypeToken<List<Teacher>>() {
            }.getType());
            if (requestCode == Constant.REFRESH) {
                adapter.setListData(data);
            } else if (requestCode == Constant.LOADMORE) {
                adapter.addData(data);
            }
            filterView.getPullToRefreshListView().onRefreshComplete();
        }

        @Override
        public void onFailure(Context context, ResultInfo info, int requestCode) {
            super.onFailure(context, info, requestCode);
            filterView.getPullToRefreshListView().onRefreshComplete();
        }
    };

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
        loadData(false,false);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
        loadData(false,true);
    }

    @Override
    public void onChecked() {
        loadData(true,false);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Teacher teacher = adapter.getListData().get(position-1);
        Intent intent = new Intent(this,TeacherComplainActivity.class);
        intent.putExtra(Constant.TEACHER_KEY,teacher);
        startActivity(intent);
    }
}
