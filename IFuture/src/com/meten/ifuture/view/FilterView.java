package com.meten.ifuture.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.gson.reflect.TypeToken;
import com.meten.ifuture.R;
import com.meten.ifuture.adapter.FilterAdapter;
import com.meten.ifuture.constant.URL;
import com.meten.ifuture.http.HttpRequestCallBack;
import com.meten.ifuture.http.HttpRequestUtils;
import com.meten.ifuture.http.RequestParamsUtils;
import com.meten.ifuture.model.Area;
import com.meten.ifuture.model.ResultInfo;
import com.meten.ifuture.model.Role;
import com.meten.ifuture.utils.JsonParse;
import com.meten.imanager.pulltorefresh.library.PullToRefreshListView;

import java.util.List;

/**
 * Created by Cmad on 2015/3/10.
 */
public class FilterView extends LinearLayout implements View.OnClickListener, AdapterView.OnItemClickListener {

    private static final int GET_AREA = 0x10;
    private static final int GET_ROLE = 0x11;
    private CheckBox cbArea;
    private CheckBox cbTeacher;
    private ListView filterListview;
    private FilterAdapter filterAdapter;
    private PullToRefreshListView mRefreshListview;
    private LinearLayout filterListLayout;

    private List<Area> areaList;
    private List<Role> roleList;

    private int areaCheckedId;
    private int roleCheckedId;

    private int currentAreaId;
    private int currentRoleId;

    private ChooseType showType;

    private OnCheckedListener listener;

    public enum ChooseType {
        AREA, ROLE;
    }

    public FilterView(Context context) {
        this(context, null);
    }

    public FilterView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FilterView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        LayoutInflater.from(context).inflate(R.layout.has_filter_list_layout, this);
        initView();
    }

    public void setOnCheckedListener(OnCheckedListener listener) {
        this.listener = listener;
    }


    private void initView() {
        cbArea = (CheckBox) findViewById(R.id.area_cb);
        cbTeacher = (CheckBox) findViewById(R.id.teacher_cb);
        filterListview = (ListView) findViewById(R.id.filter_listview);
        mRefreshListview = (PullToRefreshListView) findViewById(R.id.refresh_listview);
        filterListLayout = (LinearLayout) findViewById(R.id.filter_list_layout);

        filterAdapter = new FilterAdapter(getContext());
        filterListview.setAdapter(filterAdapter);

        cbArea.setOnClickListener(this);
        cbTeacher.setOnClickListener(this);

        filterListview.setOnItemClickListener(this);

        HttpRequestUtils.create(getContext()).isShowLoadingDilag(false).send(URL.GET_AREA_LIST, RequestParamsUtils.createRequestParams(), GET_AREA, callBack);
        HttpRequestUtils.create(getContext()).isShowLoadingDilag(false).send(URL.GET_ROLE_LIST, RequestParamsUtils.createRequestParams(), GET_ROLE, callBack);
    }


    public PullToRefreshListView getPullToRefreshListView(){
        return mRefreshListview;
    }

    public int getCheckedAreaId() {
        return currentAreaId;
    }

    public int getCheckedRoleId() {
        return currentRoleId;
    }


    HttpRequestCallBack<ResultInfo> callBack = new HttpRequestCallBack<ResultInfo>() {
        @Override
        public void onSuccess(ResultInfo info, int requestCode) {
            if (requestCode == GET_AREA) {
                areaList = JsonParse.parseToObject(info, new TypeToken<List<Area>>() {
                }.getType());
                Area a = new Area();
                a.setId(0);
                a.setName("全部区域");
                areaList.add(0, a);
                return;
            }

            if (requestCode == GET_ROLE) {
                roleList = JsonParse.parseToObject(info, new TypeToken<List<Role>>() {
                }.getType());
                Role r = new Role();
                r.setId(0);
                r.setName("所有老师");
                roleList.add(0, r);
                return;
            }

            if (showType == ChooseType.AREA) {
                filterAdapter.setListData(areaList);
            } else {
                filterAdapter.setListData(roleList);
            }
//            ListViewUtils.setListViewMaxHeight(filterListview);
        }
    };


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.area_cb:
                if (cbArea.isChecked()) {
                    filterListLayout.setVisibility(View.VISIBLE);
                    filterAdapter.setListData(areaList);
                    showType = ChooseType.AREA;
                    filterAdapter.setCheckedId(areaCheckedId);
                    cbTeacher.setChecked(false);
//                    ListViewUtils.setListViewMaxHeight(filterListview);
                } else {
                    hiddenListView();
                }
                break;
            case R.id.teacher_cb:
                if (cbTeacher.isChecked()) {
                    filterListLayout.setVisibility(View.VISIBLE);
                    filterAdapter.setListData(roleList);
                    showType = ChooseType.ROLE;
                    filterAdapter.setCheckedId(roleCheckedId);
                    cbArea.setChecked(false);
//                    ListViewUtils.setListViewMaxHeight(filterListview);
                } else {
                    hiddenListView();
                }
                break;
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (showType == ChooseType.AREA) {
            areaCheckedId = (int) id;
            cbArea.setText(((Area) filterAdapter.getItem(position)).getName());
        } else {
            roleCheckedId = (int) id;
            cbTeacher.setText(((Area) filterAdapter.getItem(position)).getName());
        }

        hiddenListView();
        if (currentAreaId == areaCheckedId && currentRoleId == roleCheckedId) {
            return;
        }
        currentRoleId = roleCheckedId;
        currentAreaId = areaCheckedId;
        if (listener != null) {
            listener.onChecked();
        }

    }


    public interface OnCheckedListener {
        public void onChecked();
    }

    public void hiddenListView() {
        cbArea.setChecked(false);
        cbTeacher.setChecked(false);
        filterListLayout.setVisibility(View.GONE);
    }

    public boolean isShowListview() {
        if (filterListLayout.getVisibility() == View.VISIBLE) {
            return true;
        } else {
            return false;
        }
    }
}
