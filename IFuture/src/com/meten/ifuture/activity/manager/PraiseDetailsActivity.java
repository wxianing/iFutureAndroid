package com.meten.ifuture.activity.manager;

import android.content.Context;
import android.os.Bundle;
import android.widget.ListView;

import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.http.RequestParams;
import com.meten.ifuture.activity.base.BaseListActivity;
import com.meten.ifuture.adapter.manager.PraiseDetailsAdapter;
import com.meten.ifuture.constant.Constant;
import com.meten.ifuture.constant.URL;
import com.meten.ifuture.http.HttpRequestCallBack;
import com.meten.ifuture.http.HttpRequestUtils;
import com.meten.ifuture.http.RequestParamsUtils;
import com.meten.ifuture.model.ResultInfo;
import com.meten.ifuture.model.student.Complain;
import com.meten.ifuture.utils.JsonParse;
import com.meten.imanager.pulltorefresh.library.PullToRefreshBase;

import java.util.List;

/**
 * Created by Cmad on 2015/3/21.
 */
public class PraiseDetailsActivity extends BaseListActivity implements PullToRefreshBase.OnRefreshListener2<ListView> {
    private int teacherUserId;
    private String teacherName;
    private PraiseDetailsAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        teacherUserId = getIntent().getIntExtra(Constant.USER_ID,-1);
        teacherName = getIntent().getStringExtra(Constant.TEACHER_KEY);

        initView();
    }

    private void initView() {
        setTitle(teacherName);

        hiddenRightImageView();

        adapter = new PraiseDetailsAdapter(this);
        setAdapter(adapter);

//        getPullToRefreshListView().getRefreshableView().setDivider(new ColorDrawable(getResources().getColor(R.color.divider_bg_color)));
//        getPullToRefreshListView().getRefreshableView().setDividerHeight(getResources().getDimensionPixelSize(R.dimen.divider));
        setOnRefreshListener(this);
        setMode(PullToRefreshBase.Mode.BOTH);

        RequestParams params = RequestParamsUtils.getPraiseDetails(URL.GET_TEACHER_PRAISE_DETAILS, teacherUserId,false);
        HttpRequestUtils.create(this).send(URL.GET_TEACHER_PRAISE_DETAILS,params,Constant.REFRESH,callBack);

    }

    HttpRequestCallBack<ResultInfo> callBack = new HttpRequestCallBack<ResultInfo>() {
        @Override
        public void onSuccess(ResultInfo info, int requestCode) {
            List<Complain> data = JsonParse.parseDataOfPageToData(info,new TypeToken<List<Complain>>(){}.getType());
            if(requestCode == Constant.REFRESH){
                adapter.setListData(data);
            }else if(requestCode == Constant.LOADMORE){
                adapter.addData(data);
            }
            onRefreshComplete();
        }

        @Override
        public void onFailure(Context context, ResultInfo info, int requestCode) {
            super.onFailure(context, info, requestCode);
            onRefreshComplete();
        }
    };

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
        RequestParams params = RequestParamsUtils.getPraiseDetails(URL.GET_TEACHER_PRAISE_DETAILS, teacherUserId,false);
        HttpRequestUtils.create(this).send(URL.GET_TEACHER_PRAISE_DETAILS,params,Constant.REFRESH,callBack);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
        RequestParams params = RequestParamsUtils.getPraiseDetails(URL.GET_TEACHER_PRAISE_DETAILS, teacherUserId,true);
        HttpRequestUtils.create(this).send(URL.GET_TEACHER_PRAISE_DETAILS,params,Constant.LOADMORE,callBack);
    }
}
