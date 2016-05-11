package com.meten.ifuture.activity.teacher;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.meten.ifuture.R;
import com.meten.ifuture.activity.SettingActivity;
import com.meten.ifuture.activity.SystemMessageActivity;
import com.meten.ifuture.activity.WebActivity;
import com.meten.ifuture.activity.base.BaseHeadActivity;
import com.meten.ifuture.activity.student.UserInfoActivity;
import com.meten.ifuture.constant.Constant;
import com.meten.ifuture.constant.PushType;
import com.meten.ifuture.constant.URL;
import com.meten.ifuture.http.task.HttpTask;
import com.meten.ifuture.model.User;
import com.meten.ifuture.push.PushUtils;
import com.meten.ifuture.utils.LogUtils;
import com.meten.ifuture.utils.SharedPreferencesUtils;
import com.meten.ifuture.utils.ViewUtils;
import com.meten.ifuture.view.CircularImage;
import com.meten.ifuture.view.ScrollAdView;

public class TeacherMianActivity extends BaseHeadActivity implements
		OnClickListener {
	private ScrollAdView mAdView;
	private CircularImage headImg;
	private TextView tvName;
	private LinearLayout llMyStudent;
	private LinearLayout llStudyAbroadInfo;
	private LinearLayout llPastStudent;
	private LinearLayout llSchoolRank;
    private LinearLayout llSystemMsg;
    private TextView tvMsgCount;
    private TextView tvMyStudent;
    private TextView tvPastStudent;

	private User user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.teacher_main_activity);
		user = SharedPreferencesUtils.getInstance(this).getUser();
		initView();
	}

	private void initView() {
		mAdView = (ScrollAdView) findViewById(R.id.adview);
		headImg = (CircularImage) findViewById(R.id.head_img);
		tvName = (TextView) findViewById(R.id.name_tv);
        llMyStudent = (LinearLayout) findViewById(R.id.my_student_ll);
		llStudyAbroadInfo = (LinearLayout) findViewById(R.id.study_abroad_info_ll);
		llPastStudent = (LinearLayout) findViewById(R.id.past_student_ll);
		llSchoolRank = (LinearLayout) findViewById(R.id.school_rank_ll);
        llSystemMsg = (LinearLayout) findViewById(R.id.system_msg_ll);
        tvMsgCount = (TextView) findViewById(R.id.msg_count_tv);
        tvMyStudent = (TextView) findViewById(R.id.my_student_tv);
        tvPastStudent = (TextView) findViewById(R.id.past_student_tv);

        hiddenBackBtn();
		mAdView.setScrollPeriod(4000);
		mAdView.startScroll();

		tvName.setText(user.getName());
        headImg.setBorderDrawable(R.drawable.home_headimg_border);
        headImg.setHasBorder(true);
		headImg.setImageUrl(user.getPhoto());
        setNewPastStudentMsg();
        setNewStudentMsg();

		getRightImageView().setOnClickListener(this);
		headImg.setOnClickListener(this);
        llMyStudent.setOnClickListener(this);
        llStudyAbroadInfo.setOnClickListener(this);
        llPastStudent.setOnClickListener(this);
        llSchoolRank.setOnClickListener(this);
        llSystemMsg.setOnClickListener(this);

		IntentFilter filter = new IntentFilter();
		filter.addAction(Constant.ACTION_REFRESH_PHOTO);
        filter.addAction(Constant.ACTION_REFRESH_MESSAGE_COUNT);
		registerReceiver(receiver, filter);

        //检测是否有新版本
        HttpTask.detectionNewAppVersion(this, true,false);

        updateUnreadMsgCount();
	}

    private void updateUnreadMsgCount() {
        long unreadMsgCount = PushUtils.getUnReadSystemMsgCount(this, user.getUserId());
        tvMsgCount.setText(unreadMsgCount+"");
        if(unreadMsgCount == 0){
            tvMsgCount.setVisibility(View.INVISIBLE);
        }else{
            tvMsgCount.setVisibility(View.VISIBLE);
        }
    }

	@Override
	protected void onResume() {
		super.onResume();
		if (mAdView != null) {
			mAdView.onResume();
		}
        updateUnreadMsgCount();
        setNewPastStudentMsg();
        setNewStudentMsg();
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (mAdView != null) {
			mAdView.onPause();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(receiver);
		if (mAdView != null) {
			mAdView.onDestroy();
		}
	}


    private void setNewStudentMsg(){
        long newStudent = PushUtils.getNewMessageCountWithPushType(this, user.getUserId(), PushType.TEACHER_NEW_STUDENT.toInt());
        if (newStudent > 0) {
            ViewUtils.setViewDrawableLeftTips(tvMyStudent, true);
        }else{
            ViewUtils.setViewDrawableLeftTips(tvMyStudent,false);
        }
    }

    private void setNewPastStudentMsg(){
        long newStudent = PushUtils.getNewMessageCountWithPushType(this, user.getUserId(), PushType.STUDENT_FINISH.toInt());
        if (newStudent > 0) {
            ViewUtils.setViewDrawableLeftTips(tvPastStudent, true);
        }else{
            ViewUtils.setViewDrawableLeftTips(tvPastStudent,false);
        }
    }

	@Override
	public void onClick(View v) {
		if (ViewUtils.isFastDoubleClick()) {
			return;
		}
		switch (v.getId()) {
            case R.id.head_img:
                Intent userInfoIt = new Intent(this, UserInfoActivity.class);
                startActivity(userInfoIt);
                break;
            case R.id.my_student_ll:
                //TODO 我的学生
                Intent stuIt = new Intent(this,MyStudentActivity.class);
                startActivity(stuIt);
                PushUtils.updateMsgWithPushType(this, user.getUserId(), PushType.TEACHER_NEW_STUDENT.toInt());
                ViewUtils.setViewDrawableLeftTips(tvMyStudent, false);
                break;
            case R.id.study_abroad_info_ll:
                Intent webIt = new Intent(this, WebActivity.class);
                webIt.putExtra(Constant.URL, URL.WEB_STUDY_ABROAD_INFO);
                webIt.putExtra(Constant.TITLE,getString(R.string.study_abroad_info));
                startActivity(webIt);
                break;
            case R.id.past_student_ll:
                Intent pastIt = new Intent(this, WebActivity.class);
                LogUtils.e("url:"+String.format(URL.WEB_FINISHED_STUDEND,user.getCode()));
                pastIt.putExtra(Constant.URL, String.format(URL.WEB_FINISHED_STUDEND,user.getCode()));
                pastIt.putExtra(Constant.TITLE,getString(R.string.past_student));
                startActivity(pastIt);
                PushUtils.updateMsgWithPushType(this, user.getUserId(), PushType.STUDENT_FINISH.toInt());
                ViewUtils.setViewDrawableLeftTips(tvPastStudent, false);
                break;
            case R.id.school_rank_ll:
                Intent srIt = new Intent(this, WebActivity.class);
                srIt.putExtra(Constant.URL, URL.WEB_SCHOOL_RANK);
                srIt.putExtra(Constant.TITLE,getString(R.string.school_rank));
                startActivity(srIt);
                break;
            case R.id.right_iv:
                Intent setIt = new Intent(this, SettingActivity.class);
                startActivity(setIt);
                break;
            case R.id.system_msg_ll:
                Intent systemMsgIt = new Intent(this, SystemMessageActivity.class);
                startActivity(systemMsgIt);
                break;
		}

	}

	BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (Constant.ACTION_REFRESH_PHOTO.equals(intent.getAction())) {
                user = SharedPreferencesUtils.getInstance(
                        TeacherMianActivity.this).getUser();
                headImg.setImageUrl(user.getPhoto());
			}else if(Constant.ACTION_REFRESH_MESSAGE_COUNT.equals(intent.getAction())){
                int pushType = intent.getIntExtra(Constant.PUSH_TYPE, -1);
                if (pushType == PushType.TEACHER_NEW_STUDENT.toInt()) {
                    setNewStudentMsg();
                } else if (pushType == PushType.STUDENT_FINISH.toInt()) {
                    setNewPastStudentMsg();
                } else if (pushType == PushType.SYSTEM_PUSH.toInt()) {
                    updateUnreadMsgCount();
                }
            }
		}

	};

}
