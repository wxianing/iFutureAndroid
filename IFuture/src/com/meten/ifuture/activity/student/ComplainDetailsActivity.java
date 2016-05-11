package com.meten.ifuture.activity.student;

import android.os.Bundle;
import android.widget.TextView;

import com.lidroid.xutils.http.RequestParams;
import com.meten.ifuture.R;
import com.meten.ifuture.activity.base.BaseListActivity;
import com.meten.ifuture.adapter.student.ComplainReplyAdapter;
import com.meten.ifuture.constant.Constant;
import com.meten.ifuture.constant.URL;
import com.meten.ifuture.http.HttpRequestCallBack;
import com.meten.ifuture.http.HttpRequestUtils;
import com.meten.ifuture.http.RequestParamsUtils;
import com.meten.ifuture.model.ResultInfo;
import com.meten.ifuture.model.User;
import com.meten.ifuture.model.student.Complain;
import com.meten.ifuture.utils.JsonParse;
import com.meten.ifuture.utils.SharedPreferencesUtils;

/**
 * Created by Cmad on 2015/3/6.
 */
public class ComplainDetailsActivity extends BaseListActivity {
    private TextView tvComplainObject;
    private TextView tvComplainDate;
    private TextView tvComplainContent;

    private ComplainReplyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addTopView(R.layout.complain_details_top);


        initView();
    }

    private void initView() {
        tvComplainObject = (TextView) findViewById(R.id.complain_object_tv);
        tvComplainDate = (TextView) findViewById(R.id.complain_date_tv);
        tvComplainContent = (TextView) findViewById(R.id.complain_content_tv);
        tvComplainContent.setSingleLine(false);

        hiddenRightImageView();
        setTitle(getString(R.string.complain_details));

        Complain complain = (Complain) getIntent().getSerializableExtra(Constant.COMPLAIN);
        int complainId = getIntent().getIntExtra(Constant.COMPLAIN_ID, -1);

        adapter = new ComplainReplyAdapter(this);
        setAdapter(adapter);

        if (complain != null) {
            setDataInView(complain);
        } else if (complainId > 0) {
            RequestParams params = RequestParamsUtils.getComplainDetails(complainId);
            HttpRequestUtils.create(this).send(URL.GET_COMPLAIN_DETAILS, params, callback);
        }

    }

    private void setDataInView(Complain complain) {
        User user = SharedPreferencesUtils.getInstance(this).getUser();
        TextView tv = (TextView) findViewById(R.id.complain_hint_tv);
        if (user.getUserType() == Constant.MANAGER) {
            tv.setText(getString(R.string.complain_student));
            tvComplainObject.setText(complain.getFromCnName());
        } else {
            tvComplainObject.setText(complain.getToCnName());
        }
        tvComplainDate.setText(complain.getCreateTime());
        tvComplainContent.setText(complain.getContent());

//        adapter.setListData(complain.getListReply());
    }

    HttpRequestCallBack<ResultInfo> callback = new HttpRequestCallBack<ResultInfo>() {
        @Override
        public void onSuccess(ResultInfo info, int requestCode) {
            Complain complain = JsonParse.parseToObject(info, Complain.class);
            setDataInView(complain);
        }
    };
}
