package com.meten.ifuture.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.http.RequestParams;
import com.meten.ifuture.R;
import com.meten.ifuture.activity.base.BaseListActivity;
import com.meten.ifuture.adapter.SystemInfoAdapter;
import com.meten.ifuture.constant.Constant;
import com.meten.ifuture.constant.PushType;
import com.meten.ifuture.constant.URL;
import com.meten.ifuture.http.HttpRequestCallBack;
import com.meten.ifuture.http.HttpRequestUtils;
import com.meten.ifuture.http.RequestParamsUtils;
import com.meten.ifuture.model.PushMessage;
import com.meten.ifuture.model.ResultInfo;
import com.meten.ifuture.model.User;
import com.meten.ifuture.push.PushUtils;
import com.meten.ifuture.utils.DBHelper;
import com.meten.ifuture.utils.JsonParse;
import com.meten.ifuture.utils.LogUtils;
import com.meten.ifuture.utils.SharedPreferencesUtils;
import com.meten.ifuture.utils.ViewUtils;
import com.meten.imanager.pulltorefresh.library.PullToRefreshBase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;

public class SystemMessageActivity extends BaseListActivity implements OnItemClickListener, OnClickListener, RadioGroup.OnCheckedChangeListener, PullToRefreshBase.OnRefreshListener2<ListView> {
    private SystemInfoAdapter adapter;
    private RadioGroup rbMessage;
    private List<PushMessage> data;
    private User user;
    private int msgType = Constant.SYSTEM_MESSAGE;
    private Map<Integer,List<PushMessage>> msgMap = new HashMap<Integer,List<PushMessage>>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addTopView(R.layout.message_filter);
        user = SharedPreferencesUtils.getInstance(this).getUser();
        initView();

        //TODO 改变所有消息状态为已读
//		PushUtils.updateAllPushMessage(this, user.getUserId());
        PushUtils.updateMsgWithPushType(this, user.getUserId(), PushType.SYSTEM_PUSH.toInt());
        JPushInterface.clearAllNotifications(this);
    }

    private void initView() {
        //TODO 从数据库读取消息列表
//        try {
//            data =DBHelper.getDBUtils(this).findAll(
//                    Selector.from(PushMessage.class).where(PushMessage.USER_ID, "=", user.getUserId()).and(PushMessage.PUSH_TYPE, "=", PushType.SYSTEM_PUSH.toInt()).orderBy("date", true));
//        } catch (DbException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }

        //从服务器获取系统消息
        getMessageByType(msgType,true,false);

        rbMessage = (RadioGroup) findViewById(R.id.message_rg);

        setTitle(getString(R.string.system_message));
        hiddenRightImageView();
//        getRightImageView().setImageResource(R.drawable.ic_system_msg_delete_selector);
//        getRightImageView().setOnClickListener(this);
        setMode(PullToRefreshBase.Mode.BOTH);
        initPullToRefreshListView();
        setOnRefreshListener(this);

        adapter = new SystemInfoAdapter(SystemMessageActivity.this);
        adapter.setListData(data);
        setAdapter(adapter);

        setOnItemClickListener(this);
        rbMessage.setOnCheckedChangeListener(this);
    }


    private HttpRequestCallBack<ResultInfo> callback = new HttpRequestCallBack<ResultInfo>() {
        @Override
        public void onSuccess(ResultInfo o, int requestCode) {
            List<PushMessage> data = JsonParse.parseDataOfPageToData(o,new TypeToken<List<PushMessage>>(){}.getType());
            if (data != null) {
                if (requestCode == Constant.REFRESH) {
                    msgMap.put(msgType,data);
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

    private void getMessageByType(int msgType,boolean isShowLoading,boolean isLoadMore) {
        RequestParams params = RequestParamsUtils.getSystemMsg(URL.GET_SYSTEM_MESSAGE,String.valueOf(msgType),isLoadMore);
        int requestCode;
        if(isLoadMore){
            requestCode = Constant.LOADMORE;
        }else{
            requestCode = Constant.REFRESH;
        }
        HttpRequestUtils.create(this).isShowLoadingDilag(isShowLoading).send(URL.GET_SYSTEM_MESSAGE,params,requestCode,callback);
    }

    private void initPullToRefreshListView() {
        int paddingSize = getResources().getDimensionPixelSize(R.dimen.dp15);
        LogUtils.e("padding size:" + paddingSize);
        getListView().setPadding(paddingSize, 0, paddingSize, 0);
        getListView().setBackgroundResource(R.drawable.round_rect_bg);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) getPullToRefreshListView().getLayoutParams();
        int margin = getResources().getDimensionPixelSize(R.dimen.dp10);
        params.leftMargin = margin;
        params.rightMargin = margin;
        params.topMargin = margin;
        params.bottomMargin = margin;
        getPullToRefreshListView().setLayoutParams(params);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        if (ViewUtils.isFastDoubleClick()) {
            return;
        }
        PushMessage msg = adapter.getItem(position - 1);
        PushUtils.jumpActivity(this, msg);
    }


    /**
     *  清除本地消息
     */
    private void clearSystemMsg() {
        try {
            DBHelper.getDBUtils(this).delete(PushMessage.class, WhereBuilder.b(PushMessage.USER_ID, "=", user.getUserId()));
            adapter.setListData(null);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        new AlertDialog.Builder(SystemMessageActivity.this)
                .setTitle(getString(R.string.clearSystemMsg))
                .setMessage(getString(R.string.clearSystemMsgHint))
                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        clearSystemMsg();
                    }
                }).setNegativeButton(getString(R.string.no), null).create().show();

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch(checkedId){
            case R.id.system_msg_rb:
                msgType = Constant.SYSTEM_MESSAGE;
                break;
            case R.id.user_msg_rb:
                msgType = Constant.USER_MESSAGE;
                PushUtils.updateAllPushMessage(this,user.getUserId());
                break;
        }

        List<PushMessage> data = msgMap.get(msgType);
        if(data == null){
            getMessageByType(msgType, true,false);
        }else{
            adapter.setListData(data);
        }
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
        getMessageByType(msgType,false,false);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
        getMessageByType(msgType,false,true);
    }
}
