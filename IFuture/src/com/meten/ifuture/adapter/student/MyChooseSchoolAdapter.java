package com.meten.ifuture.adapter.student;

import com.lidroid.xutils.http.RequestParams;
import com.meten.ifuture.R;
import com.meten.ifuture.adapter.CustomBaseAdapter;
import com.meten.ifuture.constant.Constant;
import com.meten.ifuture.constant.URL;
import com.meten.ifuture.http.HttpRequestCallBack;
import com.meten.ifuture.http.HttpRequestUtils;
import com.meten.ifuture.http.RequestParamsUtils;
import com.meten.ifuture.model.ResultInfo;
import com.meten.ifuture.model.student.School;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MyChooseSchoolAdapter extends CustomBaseAdapter<School> {
	// 老师处理状态(1：申请中 2：文书完成 3：网申提交 4：审理中 5：等待结果 6：录取 7：拒绝）
	public MyChooseSchoolAdapter(Context context) {
		super(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder = null;
		if (convertView == null) {
			convertView = listContainer.inflate(
					R.layout.student_my_choose_school_item, null);
			holder = new Holder();
			holder.tvUniversity = (TextView) convertView
					.findViewById(R.id.name_tv);
			holder.tvProject = (TextView) convertView
					.findViewById(R.id.project_tv);
			holder.tvWebAddr = (TextView) convertView
					.findViewById(R.id.webAddr_tv);
			holder.tvResult = (TextView) convertView
					.findViewById(R.id.result_tv);
			holder.btnAccept = (Button) convertView
					.findViewById(R.id.accept_btn);
			holder.btnRefuse = (Button) convertView
					.findViewById(R.id.refuse_btn);
			holder.btnAccepted = (Button) convertView
					.findViewById(R.id.accepted_btn);
			holder.llState = (LinearLayout) convertView
					.findViewById(R.id.state_ll);
			holder.rlResult = (RelativeLayout) convertView
					.findViewById(R.id.result_rl);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		School school = listData.get(position);

		holder.tvUniversity.setText(school.getUniversityCnName());
		holder.tvProject.setText(school.getUniversityProject());
		holder.tvWebAddr.setText(school.getUniversityUrl());

		if (school.getStudentStatus() == Constant.ACCEPT) {
			setBtnVisibility(holder, false);
			holder.btnAccepted.setText("已接受");
		} else if (school.getStudentStatus() == Constant.REFUSE) {
			setBtnVisibility(holder, false);
			holder.btnAccepted.setText("已拒绝");
		} else {
			setBtnVisibility(holder, true);
		}

		setState(holder, school);

		ClickListener listener = new ClickListener(school);
		holder.btnAccept.setOnClickListener(listener);
		holder.btnRefuse.setOnClickListener(listener);

		return convertView;
	}

	private void setState(Holder holder, School school) {
		// 设置显示状态
		for (int i = 0; i < holder.llState.getChildCount(); i++) {
			if (i < school.getTeacherStatus()) {
				holder.llState.getChildAt(i).setEnabled(true);
			} else {
				holder.llState.getChildAt(i).setEnabled(false);
			}
		}
		if (school.getTeacherStatus() == 5) {
			holder.rlResult.setEnabled(false);
			holder.rlResult.setSelected(false);
			holder.rlResult.setActivated(true);
			holder.tvResult.setText("等待结果");
		} else if (school.getTeacherStatus() == 6) {
			holder.rlResult.setEnabled(true);
			holder.rlResult.setSelected(false);
			holder.rlResult.setActivated(false);
			holder.tvResult.setText("录取");
		} else if (school.getTeacherStatus() == 7) {
			holder.rlResult.setEnabled(false);
			holder.rlResult.setSelected(true);
			holder.rlResult.setActivated(false);
			holder.tvResult.setText("拒绝");
		} else {
			holder.rlResult.setEnabled(false);
			holder.rlResult.setSelected(false);
			holder.rlResult.setActivated(false);
			holder.tvResult.setText("结果");
		}
	}

	private void setBtnVisibility(Holder holder, boolean isShow) {
		if (isShow) {
			holder.btnAccept.setVisibility(View.VISIBLE);
			holder.btnRefuse.setVisibility(View.VISIBLE);
			holder.btnAccepted.setVisibility(View.GONE);
		} else {
			holder.btnAccept.setVisibility(View.GONE);
			holder.btnRefuse.setVisibility(View.GONE);
			holder.btnAccepted.setVisibility(View.VISIBLE);
		}
	}

	class ClickListener implements OnClickListener {
		private School school;

		public ClickListener(School school) {
			this.school = school;
		}

		@Override
		public void onClick(View v) {
			boolean isAccept = false;
			switch (v.getId()) {
			case R.id.accept_btn:
				isAccept = true;
				break;
			case R.id.refuse_btn:
				isAccept = false;
				break;
			}
			RequestParams params = RequestParamsUtils.handChooseShool(
					school.getStudentUniversityId(), isAccept);
			HttpRequestUtils.create(context).send(URL.HAND_CHOOSE_SCHOOL,
					params, new CallBack(school, isAccept));

		}

	}

	class CallBack extends HttpRequestCallBack<ResultInfo> {
		private School school;
		private boolean isAccept;

		public CallBack(School school, boolean isAccept) {
			this.school = school;
			this.isAccept = isAccept;
		}

		@Override
		public void onSuccess(ResultInfo info, int requestCode) {
			if(isAccept){
				school.setStudentStatus(2);
			}else{
				school.setStudentStatus(3);
			}
			notifyDataSetChanged();
		}

	}

	class Holder {
		TextView tvUniversity;
		TextView tvProject;
		TextView tvWebAddr;
		ImageView ivApply;
		ImageView ivWirt;
		ImageView ivWeb;
		ImageView ivHear;
		ImageView ivResult;
		TextView tvResult;
		Button btnAccept;
		Button btnAccepted;
		Button btnRefuse;
		LinearLayout llState;
		RelativeLayout rlResult;
	}

}
