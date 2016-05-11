package com.meten.ifuture.activity.student;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.http.RequestParams;
import com.meten.ifuture.R;
import com.meten.ifuture.activity.base.BaseListActivity;
import com.meten.ifuture.adapter.student.MyTeacherAdapter;
import com.meten.ifuture.constant.Constant;
import com.meten.ifuture.constant.URL;
import com.meten.ifuture.http.HttpRequestCallBack;
import com.meten.ifuture.http.HttpRequestUtils;
import com.meten.ifuture.http.RequestParamsUtils;
import com.meten.ifuture.model.ResultInfo;
import com.meten.ifuture.model.User;
import com.meten.ifuture.model.student.MyTeacher;
import com.meten.ifuture.utils.JsonParse;
import com.meten.ifuture.utils.SharedPreferencesUtils;
import com.meten.ifuture.utils.StringUtils;
import com.meten.ifuture.utils.ToastUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyTeacherActivity extends BaseListActivity implements View.OnTouchListener, MyTeacherAdapter.ComplainClickListener, View.OnClickListener, TextWatcher {
    private static final int GET_MYTEACHER = 0x00;
    private static final int ADD_COMPLAIN = 0x01;
	private MyTeacherAdapter adapter;
    private LinearLayout llComplain;
    private EditText etComplain;
    private Button btnComplain;
    private MyTeacher complainTea;
    private User user;

    private Map<Integer,String> complainMap = new HashMap<Integer,String>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        addFloatBottomView(R.layout.complain_bottom);

        user = SharedPreferencesUtils.getInstance(this).getUser();

        llComplain = (LinearLayout) findViewById(R.id.complain_ll);
        etComplain = (EditText) findViewById(R.id.complain_et);
        btnComplain = (Button) findViewById(R.id.complain_btn);

        hiddenRightImageView();
		setTitle(getString(R.string.my_teacher));

        btnComplain.setOnClickListener(this);
		
		adapter = new MyTeacherAdapter(this);
		adapter.setComplainClickListener(this);
        setAdapter(adapter);

        getListView().setOnTouchListener(this);
        etComplain.addTextChangedListener(this);

        RequestParams params = RequestParamsUtils.createRequestParams();
        params = RequestParamsUtils.addStudentParams(params,getIntent());
		HttpRequestUtils.create(this).send(URL.GET_MY_TEACHER, params, GET_MYTEACHER, callback);
	}
	
	
	HttpRequestCallBack<ResultInfo> callback = new HttpRequestCallBack<ResultInfo>() {
		
		@Override
		public void onSuccess(ResultInfo info, int requestCode) {
            if(requestCode == GET_MYTEACHER){
                List<MyTeacher> data = JsonParse.parseToObject(info, new TypeToken<List<MyTeacher>>(){}.getType());
                adapter.setListData(data);
            }else if(requestCode == ADD_COMPLAIN){
                ToastUtils.show(MyTeacherActivity.this,"投诉成功!");
                etComplain.setText("");
                llComplain.setVisibility(View.GONE);
                complainMap.remove(complainTea.getUserId());
            }

		}
	};

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        adapter.dismissPraiseLayout();
        if(llComplain.getVisibility() == View.VISIBLE){
            llComplain.setVisibility(View.GONE);
        }
        return false;
    }


    @Override
    public void complainClick(MyTeacher tea) {
        llComplain.setVisibility(View.VISIBLE);
        etComplain.setHint("@"+tea.getName());
        complainTea = tea;
        String content = complainMap.get(tea.getUserId());
        if(content != null){
            etComplain.setText(content);
            etComplain.setSelection(content.length());
        }else{
            etComplain.setText("");
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.complain_btn:
                String complainContent = etComplain.getText().toString();
                if(StringUtils.isEmpty(complainContent)){
                    ToastUtils.show(this,"投诉内容不能为空！！");
                    return;
                }
                if(complainContent.length() > Constant.MAX_COMPLAIN_TEXT_SIZE){
                    ToastUtils.show(this,"投诉内容不能大于256个字");
                    return;
                }
                if(complainTea != null){
                   RequestParams params = RequestParamsUtils.addComplainOrPraise(complainTea.getUserId(), complainTea.getCnName(), complainContent);
                    HttpRequestUtils.create(this).send(URL.ADD_COMPLAIN,params,ADD_COMPLAIN,callback);
                }
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        complainMap.put(complainTea.getUserId(),s.toString());
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
