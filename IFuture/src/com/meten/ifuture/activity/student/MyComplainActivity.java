package com.meten.ifuture.activity.student;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.http.RequestParams;
import com.meten.ifuture.R;
import com.meten.ifuture.activity.base.BaseListActivity;
import com.meten.ifuture.adapter.student.MyComplainAdapter;
import com.meten.ifuture.constant.Constant;
import com.meten.ifuture.constant.URL;
import com.meten.ifuture.http.HttpRequestCallBack;
import com.meten.ifuture.http.HttpRequestUtils;
import com.meten.ifuture.http.RequestParamsUtils;
import com.meten.ifuture.model.ResultInfo;
import com.meten.ifuture.model.student.Complain;
import com.meten.ifuture.model.student.MyTeacher;
import com.meten.ifuture.utils.DateUtils;
import com.meten.ifuture.utils.JsonParse;
import com.meten.ifuture.utils.LogUtils;
import com.meten.ifuture.utils.StringUtils;
import com.meten.ifuture.utils.ToastUtils;
import com.meten.ifuture.utils.ViewUtils;
import com.meten.imanager.pulltorefresh.library.PullToRefreshBase;

import java.util.Date;
import java.util.List;

/**
 * Created by Cmad on 2015/3/6.
 * 我的投诉界面
 */
public class MyComplainActivity extends BaseListActivity implements PullToRefreshBase.OnRefreshListener2<ListView>, View.OnClickListener, AdapterView.OnItemClickListener {
    private static final int COMPLAIN = 0x10;
    private LinearLayout llComplain;
    private EditText etComplain;
    private Button btnComplain;
    private ImageView addComplainObject;

    private MyComplainAdapter adapter;
    private MyTeacher complainTea;
    private Complain complain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addBottomView(R.layout.complain_bottom);

        initView();
    }

    private void initView() {
        llComplain = (LinearLayout) findViewById(R.id.complain_ll);
        etComplain = (EditText) findViewById(R.id.complain_et);
        btnComplain = (Button) findViewById(R.id.complain_btn);
        addComplainObject = (ImageView) findViewById(R.id.add_complain_object_iv);

        adapter = new MyComplainAdapter(this);
        setAdapter(adapter);

        setTitle(getString(R.string.my_complain));
        hiddenRightImageView();
        llComplain.setVisibility(View.VISIBLE);
        addComplainObject.setVisibility(View.VISIBLE);
        setOnRefreshListener(this);
        setMode(PullToRefreshBase.Mode.BOTH);

        addComplainObject.setOnClickListener(this);
        btnComplain.setOnClickListener(this);
        setOnItemClickListener(this);

        RequestParams params = RequestParamsUtils.pageRequestParams(getClass().toString(), false);
        HttpRequestUtils.create(this).send(URL.GET_MY_COMPLAIN_LIST, params, Constant.REFRESH, callBack);

    }

    HttpRequestCallBack<ResultInfo> callBack = new HttpRequestCallBack<ResultInfo>() {
        @Override
        public void onSuccess(ResultInfo info, int requestCode) {
            if (requestCode == COMPLAIN) {
                ToastUtils.show(MyComplainActivity.this, "投诉成功!");
                int complainId = info.getData().getAsInt();
                LogUtils.e("complainId:"+complainId);
                etComplain.setText("");
                complain.setId(complainId);
                adapter.addData(complain);
            } else {
                List<Complain> data = JsonParse.parseDataOfPageToData(info, new TypeToken<List<Complain>>() {
                }.getType());
                if (data != null) {
                    if (requestCode == Constant.REFRESH) {
                        adapter.setListData(data);
                    } else if (requestCode == Constant.LOADMORE) {
                        adapter.addData(data);
                    }
                }
                onRefreshComplete();
            }


        }

        @Override
        public void onFailure(Context context, ResultInfo info, int requestCode) {
            super.onFailure(context, info, requestCode);
            onRefreshComplete();
        }
    };

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
        RequestParams params = RequestParamsUtils.pageRequestParams(getClass().toString(), false);
        HttpRequestUtils.create(this).isShowLoadingDilag(false).send(URL.GET_MY_COMPLAIN_LIST, params, Constant.REFRESH, callBack);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
        RequestParams params = RequestParamsUtils.pageRequestParams(getClass().toString(), true);
        HttpRequestUtils.create(this).isShowLoadingDilag(false).send(URL.GET_MY_COMPLAIN_LIST, params, Constant.LOADMORE, callBack);
    }

    @Override
    public void onClick(View v) {
        if (ViewUtils.isFastDoubleClick()) {
            return;
        }
        switch (v.getId()) {
            case R.id.add_complain_object_iv:
                Intent intent = new Intent(this, ChooseComplainObjectActivity.class);
                if(complainTea != null){
                    intent.putExtra(Constant.USER_ID,complainTea.getUserId());
                }
                startActivityForResult(intent, 0);
                break;
            case R.id.complain_btn:
                String complainContent = etComplain.getText().toString();
                if (StringUtils.isEmpty(complainContent)) {
                    ToastUtils.show(this, "投诉内容不能为空！！");
                    return;
                }
                if (complainContent.length() > Constant.MAX_COMPLAIN_TEXT_SIZE) {
                    ToastUtils.show(this, "投诉内容不能大于256个字");
                    return;
                }
                if (complainTea == null) {
                    ToastUtils.show(this, "请选择投诉对象！");
                    return;
                }
                complain = new Complain();
                complain.setContent(complainContent);
                complain.setCreateTime(DateUtils.getDateToString(new Date(), "yyyy.MM.dd HH:mm:ss"));
                complain.setToCnName(complainTea.getCnName());
                complain.setToUserId(complainTea.getUserId());
                complain.setToRoleName(complainTea.getRoleName());
                complain.setStatus(Constant.UNHANDLED);
                RequestParams params = RequestParamsUtils.addComplainOrPraise(complainTea.getUserId(), complainTea.getCnName(), complainContent);
                HttpRequestUtils.create(this).send(URL.ADD_COMPLAIN, params, COMPLAIN, callBack);
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(data != null){
            complainTea = (MyTeacher) data.getSerializableExtra(Constant.COMPLAIN_OBJECT);
            if(complainTea == null){
                return;
            }
            etComplain.setHint("@" + complainTea.getName());
//            etComplain.setText("");
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        /*Complain complain = adapter.getListData().get(position - 1);
        Intent intent = new Intent(this, ComplainDetailsActivity.class);
        if (complain.getId() == 0) {
            intent.putExtra(Constant.COMPLAIN, complain);
        } else {
            intent.putExtra(Constant.COMPLAIN_ID, complain.getId());
        }

        startActivity(intent);*/
    }
}
