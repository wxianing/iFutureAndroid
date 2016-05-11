package com.meten.ifuture.activity.manager;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioGroup;

import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.http.RequestParams;
import com.meten.ifuture.R;
import com.meten.ifuture.activity.base.BaseListActivity;
import com.meten.ifuture.activity.student.ComplainDetailsActivity;
import com.meten.ifuture.adapter.manager.TeacherComplainDetailsAdapter;
import com.meten.ifuture.constant.Constant;
import com.meten.ifuture.constant.URL;
import com.meten.ifuture.http.HttpRequestCallBack;
import com.meten.ifuture.http.HttpRequestUtils;
import com.meten.ifuture.http.RequestParamsUtils;
import com.meten.ifuture.model.ResultInfo;
import com.meten.ifuture.model.Teacher;
import com.meten.ifuture.model.student.Complain;
import com.meten.ifuture.utils.JsonParse;
import com.meten.imanager.pulltorefresh.library.PullToRefreshBase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Cmad on 2015/3/11.
 */
public class TeacherComplainActivity extends BaseListActivity implements PullToRefreshBase.OnRefreshListener2<ListView>, AdapterView.OnItemClickListener, RadioGroup.OnCheckedChangeListener {
    private TeacherComplainDetailsAdapter adapter;
    private Teacher teacher;
    private RadioGroup rgHandle;
    private int status ;
    private Map<Integer,List<Complain>> complainMap = new HashMap<Integer,List<Complain>>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addTopView(R.layout.manager_complain_details_filter);

        teacher = (Teacher) getIntent().getSerializableExtra(Constant.TEACHER_KEY);

        rgHandle = (RadioGroup) findViewById(R.id.handle_rg);


        setTitle(teacher.getName());
        hiddenRightImageView();
        setMode(PullToRefreshBase.Mode.BOTH);
        setOnRefreshListener(this);

        setOnItemClickListener(this);

        adapter = new TeacherComplainDetailsAdapter(this);
        adapter.setType(Constant.MANAGER);
        setAdapter(adapter);

        getPullToRefreshListView().getRefreshableView().setDivider(new ColorDrawable(getResources().getColor(R.color.divider_bg_color)));
        getPullToRefreshListView().getRefreshableView().setDividerHeight(getResources().getDimensionPixelSize(R.dimen.divider));

        rgHandle.setOnCheckedChangeListener(this);

        loadData(true, false);
    }

    private void loadData(boolean isShowLoading,boolean isLoadMore) {
        int requestCode ;
        if(isLoadMore){
            requestCode = Constant.LOADMORE;
        }else{
            requestCode = Constant.REFRESH;
        }
        RequestParams params = RequestParamsUtils.getTeacherComplainList(URL.GET_TEACHER_COMPLAIN_LIST, teacher.getUserId(), status,isLoadMore);
        HttpRequestUtils.create(this).isShowLoadingDilag(isShowLoading).send(URL.GET_TEACHER_COMPLAIN_LIST, params, requestCode, callBack);
    }

    HttpRequestCallBack<ResultInfo> callBack = new HttpRequestCallBack<ResultInfo>() {
        @Override
        public void onSuccess(ResultInfo info, int requestCode) {
            List<Complain> data = JsonParse.parseDataOfPageToData(info, new TypeToken<List<Complain>>() {
            }.getType());
            if (data != null) {
                if (requestCode == Constant.REFRESH) {
                    complainMap.put(status,data);
                    adapter.setListData(data);
                } else if (requestCode == Constant.LOADMORE) {
                    adapter.addData(data);
                }
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
        loadData(false, false);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
        loadData(false, true);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Complain complain = adapter.getListData().get(position - 1);
        Intent intent = new Intent(this, ComplainDetailsActivity.class);
        intent.putExtra(Constant.COMPLAIN_ID, complain.getId());

        startActivity(intent);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch(checkedId){
            case R.id.handle_all_rb:
                status = 0;
                break;
            case R.id.handled_rb:
                status = Constant.HANDLED;
                break;
            case R.id.unhandled_rb:
                status = Constant.UNHANDLED;
                break;
        }

        List<Complain> data = complainMap.get(status);
        if(data == null){
            loadData(true,false);
        }else{
            adapter.setListData(data);
        }
    }
}
