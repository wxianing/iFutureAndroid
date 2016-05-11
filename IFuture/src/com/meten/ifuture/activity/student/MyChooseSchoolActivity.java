package com.meten.ifuture.activity.student;

import java.util.List;

import android.os.Bundle;

import com.google.gson.reflect.TypeToken;
import com.meten.ifuture.R;
import com.meten.ifuture.activity.base.BaseListActivity;
import com.meten.ifuture.adapter.student.MyChooseSchoolAdapter;
import com.meten.ifuture.constant.URL;
import com.meten.ifuture.http.HttpRequestCallBack;
import com.meten.ifuture.http.HttpRequestUtils;
import com.meten.ifuture.http.RequestParamsUtils;
import com.meten.ifuture.model.ResultInfo;
import com.meten.ifuture.model.student.School;
import com.meten.ifuture.utils.JsonParse;

public class MyChooseSchoolActivity extends BaseListActivity {
	private MyChooseSchoolAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setTitle(getString(R.string.my_choose_school));
		hiddenRightImageView();
		
		adapter = new MyChooseSchoolAdapter(this);
		setAdapter(adapter);
		
		HttpRequestUtils.create(this).send(URL.GET_MY_SCHOOL, RequestParamsUtils.createRequestParams(), callback);
	}
	
	HttpRequestCallBack<ResultInfo> callback = new HttpRequestCallBack<ResultInfo>() {
		
		@Override
		public void onSuccess(ResultInfo info, int requestCode) {
			List<School> data = JsonParse.parseToObject(info, new TypeToken<List<School>>(){}.getType());
			adapter.setListData(data);
		}
	};
	
}
