package com.meten.ifuture.activity.manager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.meten.ifuture.R;
import com.meten.ifuture.activity.SettingActivity;
import com.meten.ifuture.activity.WebActivity;
import com.meten.ifuture.activity.base.BaseHeadActivity;
import com.meten.ifuture.activity.teacher.MyStudentActivity;
import com.meten.ifuture.constant.Constant;
import com.meten.ifuture.constant.PushType;
import com.meten.ifuture.constant.URL;
import com.meten.ifuture.http.task.HttpTask;
import com.meten.ifuture.model.User;
import com.meten.ifuture.push.PushUtils;
import com.meten.ifuture.utils.SharedPreferencesUtils;
import com.meten.ifuture.utils.ViewUtils;

/**
 * Created by Cmad on 2015/3/10.
 */
public class ManagerMainActivity extends BaseHeadActivity implements View.OnClickListener {
    private LinearLayout llProceedStudent;
    private LinearLayout llPastStudent;
    private LinearLayout llComplain;
    private LinearLayout llPraise;
    private LinearLayout llSchoolRank;
    private LinearLayout llStudyAbroadInfo;
    private TextView tvProceedStudent;
    private TextView tvPastStudent;
    private TextView tvComplain;
    private TextView tvPraise;
    private TextView tvSchoolRank;
    private TextView tvStudyAbroadInfo;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manager_main);
        user = SharedPreferencesUtils.getInstance(this).getUser();
        initView();
    }

    private void initView() {
        hiddenBackBtn();
        getRightImageView().setImageResource(R.drawable.setting_selector);

        llProceedStudent = (LinearLayout) findViewById(R.id.proceed_student_ll);
        llPastStudent = (LinearLayout) findViewById(R.id.past_student_ll);
        llComplain = (LinearLayout) findViewById(R.id.student_complain_ll);
        llPraise = (LinearLayout) findViewById(R.id.student_praise_ll);
        llSchoolRank = (LinearLayout) findViewById(R.id.school_rank_ll);
        llStudyAbroadInfo = (LinearLayout) findViewById(R.id.study_abroad_info_ll);

        tvProceedStudent = (TextView) findViewById(R.id.proceed_student_tv);
        tvPastStudent = (TextView) findViewById(R.id.past_student_tv);
        tvComplain = (TextView) findViewById(R.id.student_complain_tv);
        tvPraise = (TextView) findViewById(R.id.student_praise_tv);
        tvSchoolRank = (TextView) findViewById(R.id.school_rank_tv);
        tvStudyAbroadInfo = (TextView) findViewById(R.id.study_abroad_info_tv);

        llProceedStudent.setOnClickListener(this);
        llPastStudent.setOnClickListener(this);
        llComplain.setOnClickListener(this);
        llPraise.setOnClickListener(this);
        llSchoolRank.setOnClickListener(this);
        llStudyAbroadInfo.setOnClickListener(this);
        getRightImageView().setOnClickListener(this);

        //检测是否有新版本
        HttpTask.detectionNewAppVersion(this, true, false);

        IntentFilter intentFilter = new IntentFilter(Constant.ACTION_REFRESH_MESSAGE_COUNT);
        registerReceiver(receiver,intentFilter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setAllNewMsg();
    }

    private void setAllNewMsg() {
        setNewMsg(PushType.ADMIN_NEW_STUDENT,tvProceedStudent);
        setNewMsg(PushType.ADMIN_FINISHED_STUDENT,tvPastStudent);
        setNewMsg(PushType.ADMIN_NEW_COMPLAIN,tvComplain);
        setNewMsg(PushType.ADMIN_NEW_PRAISE, tvPraise);
        setNewMsg(PushType.ADMIN_NEW_NEWS,tvStudyAbroadInfo);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.right_iv:
                intent.setClass(this, SettingActivity.class);
                break;
            case R.id.proceed_student_ll:
                intent.setClass(this, MyStudentActivity.class);
                updateNewMsg(PushType.ADMIN_NEW_STUDENT, tvProceedStudent);
                break;
            case R.id.past_student_ll:
                intent.setClass(this, WebActivity.class);
                intent.putExtra(Constant.URL, String.format(URL.WEB_FINISHED_STUDEND, user.getCode()));
                intent.putExtra(Constant.TITLE, getString(R.string.past_student));
                updateNewMsg(PushType.ADMIN_FINISHED_STUDENT, tvPastStudent);
                break;
            case R.id.student_complain_ll:
                intent.setClass(this, ComplainActivity.class);
                updateNewMsg(PushType.ADMIN_NEW_COMPLAIN, tvComplain);
                break;
            case R.id.student_praise_ll:
                intent.setClass(this, PraiseActivity.class);
                updateNewMsg(PushType.ADMIN_NEW_PRAISE, tvPraise);
                break;
            case R.id.school_rank_ll:
                intent.setClass(this, WebActivity.class);
                intent.putExtra(Constant.URL, URL.WEB_SCHOOL_RANK);
                intent.putExtra(Constant.TITLE, getString(R.string.school_rank));
                break;
            case R.id.study_abroad_info_ll:
                intent.setClass(this, WebActivity.class);
                intent.putExtra(Constant.URL, URL.WEB_STUDY_ABROAD_INFO);
                intent.putExtra(Constant.TITLE, getString(R.string.study_abroad_info));
                updateNewMsg(PushType.ADMIN_NEW_NEWS, tvStudyAbroadInfo);
                break;
        }
        startActivity(intent);
    }

    private void setNewMsg(PushType type,TextView view) {
        long count = PushUtils.getNewMessageCountWithPushType(this, user.getUserId(), type.toInt());
        if (count > 0) {
            ViewUtils.setViewDrawableLeftTips(view, true);
        }else{
            ViewUtils.setViewDrawableLeftTips(view,false);
        }
    }

    private void updateNewMsg(PushType type, TextView view){
        PushUtils.updateMsgWithPushType(this, user.getUserId(), type.toInt());
        ViewUtils.setViewDrawableLeftTips(view, false);
    }


    BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (Constant.ACTION_REFRESH_MESSAGE_COUNT.equals(intent.getAction())) {
                int pushType = intent.getIntExtra(Constant.PUSH_TYPE, -1);
                if (pushType == PushType.ADMIN_NEW_STUDENT.toInt()) {
                    setNewMsg(PushType.ADMIN_NEW_STUDENT,tvProceedStudent);
                } else if (pushType == PushType.ADMIN_FINISHED_STUDENT.toInt()) {
                    setNewMsg(PushType.ADMIN_FINISHED_STUDENT,tvPastStudent);
                } else if (pushType == PushType.ADMIN_NEW_COMPLAIN.toInt()) {
                    setNewMsg(PushType.ADMIN_NEW_COMPLAIN,tvComplain);
                } else if (pushType == PushType.ADMIN_NEW_PRAISE.toInt()) {
                    setNewMsg(PushType.ADMIN_NEW_PRAISE,tvPraise);
                } else if (pushType == PushType.ADMIN_NEW_NEWS.toInt()) {
                    setNewMsg(PushType.ADMIN_NEW_NEWS,tvStudyAbroadInfo);
                }
            }
        }

    };
}
