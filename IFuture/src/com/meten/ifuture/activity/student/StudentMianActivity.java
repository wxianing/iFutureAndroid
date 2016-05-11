package com.meten.ifuture.activity.student;

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
import com.meten.ifuture.constant.Constant;
import com.meten.ifuture.constant.PushType;
import com.meten.ifuture.constant.URL;
import com.meten.ifuture.http.task.HttpTask;
import com.meten.ifuture.model.User;
import com.meten.ifuture.push.PushUtils;
import com.meten.ifuture.utils.SharedPreferencesUtils;
import com.meten.ifuture.utils.ViewUtils;
import com.meten.ifuture.view.CircularImage;
import com.meten.ifuture.view.ScrollAdView;

public class StudentMianActivity extends BaseHeadActivity implements
        OnClickListener {
    private ScrollAdView mAdView;
    private CircularImage headImg;
    private TextView tvName;
    private TextView tvMsgCount;
    private LinearLayout llMyTeacher;
    private LinearLayout llMyScore;
    private LinearLayout llMyDatum;
    private LinearLayout llMyPlan;
    private LinearLayout llMyChooseScholl;
    private LinearLayout llMyWrit;
    private LinearLayout llComplain;
    private LinearLayout llSystemMsg;
    private TextView tvMyTeahcer;
    private TextView tvMyPlan;
    private TextView tvMyChooseSchool;
    //    private TextView tvMyDatum;
//    private TextView tvMyWrit;
//    private TextView tvMyComplain;
    private TextView tvMyScore;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_main_activity);
        user = SharedPreferencesUtils.getInstance(this).getUser();
        initView();
    }

    private void initView() {
        mAdView = (ScrollAdView) findViewById(R.id.adview);
        headImg = (CircularImage) findViewById(R.id.head_img);
        tvName = (TextView) findViewById(R.id.name_tv);
        llMyTeacher = (LinearLayout) findViewById(R.id.my_teacher_ll);
        llMyScore = (LinearLayout) findViewById(R.id.my_score_ll);
        llMyDatum = (LinearLayout) findViewById(R.id.my_datum_ll);
        llMyPlan = (LinearLayout) findViewById(R.id.my_plan_ll);
        llMyChooseScholl = (LinearLayout) findViewById(R.id.my_choose_school_ll);
        llMyWrit = (LinearLayout) findViewById(R.id.my_writ_ll);
        llComplain = (LinearLayout) findViewById(R.id.my_complain_ll);
        llSystemMsg = (LinearLayout) findViewById(R.id.system_msg_ll);
        tvMsgCount = (TextView) findViewById(R.id.msg_count_tv);
        tvMyTeahcer = (TextView) findViewById(R.id.my_teacher_tv);
        tvMyChooseSchool = (TextView) findViewById(R.id.my_choose_school_tv);
        tvMyPlan = (TextView) findViewById(R.id.my_plan_tv);
        tvMyScore = (TextView) findViewById(R.id.my_score_tv);

        hiddenBackBtn();
        mAdView.setScrollPeriod(4000);
        mAdView.startScroll();

        tvName.setText(user.getName());
        headImg.setBorderDrawable(R.drawable.home_headimg_border);
        headImg.setHasBorder(true);
        headImg.setImageUrl(user.getPhoto());

        updateUnreadMsgCount();

        showNewMessage();

        getRightImageView().setOnClickListener(this);
        headImg.setOnClickListener(this);
        llMyTeacher.setOnClickListener(this);
        llMyScore.setOnClickListener(this);
        llMyDatum.setOnClickListener(this);
        llMyPlan.setOnClickListener(this);
        llMyChooseScholl.setOnClickListener(this);
        llMyWrit.setOnClickListener(this);
        llComplain.setOnClickListener(this);
        llSystemMsg.setOnClickListener(this);

        IntentFilter filter = new IntentFilter();
        filter.addAction(Constant.ACTION_REFRESH_PHOTO);
        filter.addAction(Constant.ACTION_REFRESH_MESSAGE_COUNT);
        registerReceiver(receiver, filter);

        //检测是否有新版本
        HttpTask.detectionNewAppVersion(this, true, false);
    }

    /**
     * 查询显示新消息
     */
    private void showNewMessage() {
        setChooseSchoolNewMsg();
        setMyTeacherNewMsg();
        setMyPlanNewMsg();
        setMyScoreNewMsg();
    }

    private void setMyPlanNewMsg() {
        long newPlan = PushUtils.getNewMessageCountWithPushType(this, user.getUserId(), PushType.STUDENT_PLAN_UPDATE.toInt());
        if (newPlan > 0) {
            ViewUtils.setViewDrawableLeftTips(tvMyPlan, true);
        }else{
            ViewUtils.setViewDrawableLeftTips(tvMyPlan,false);
        }
    }

    private void setMyScoreNewMsg() {
        long newPlan = PushUtils.getNewMessageCountWithPushType(this, user.getUserId(), PushType.STUDENT_NEW_SCORE.toInt());
        if (newPlan > 0) {
            ViewUtils.setViewDrawableLeftTips(tvMyScore, true);
        }else{
            ViewUtils.setViewDrawableLeftTips(tvMyScore,false);
        }
    }

    private void setMyTeacherNewMsg() {
        long newTeahcerCount = PushUtils.getNewMessageCountWithPushType(this, user.getUserId(), PushType.STUDENT_NEW_TEACHER.toInt());
        if (newTeahcerCount > 0) {
            ViewUtils.setViewDrawableLeftTips(tvMyTeahcer, true);
        }else{
            ViewUtils.setViewDrawableLeftTips(tvMyTeahcer,false);
        }
    }

    private void setChooseSchoolNewMsg() {
        long newChooseSchool = PushUtils.getNewMessageCountWithPushType(this, user.getUserId(), PushType.STUDENT_NEW_UNIVERSITY.toInt());
        newChooseSchool += PushUtils.getNewMessageCountWithPushType(this, user.getUserId(), PushType.STUDENT_UNIVERSITY_TATUS.toInt());
        if (newChooseSchool > 0) {
            ViewUtils.setViewDrawableLeftTips(tvMyChooseSchool, true);
        }else{
            ViewUtils.setViewDrawableLeftTips(tvMyChooseSchool, false);
        }
    }


    private void updateUnreadMsgCount() {
        long unreadMsgCount = PushUtils.getUnReadSystemMsgCount(this, user.getUserId());
        tvMsgCount.setText(unreadMsgCount + "");
        if (unreadMsgCount == 0) {
            tvMsgCount.setVisibility(View.INVISIBLE);
        } else {
            tvMsgCount.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.onResume();
        }
        showNewMessage();
        updateUnreadMsgCount();
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
            case R.id.my_teacher_ll:
                Intent myTeacherIt = new Intent(this, MyTeacherActivity.class);
                startActivity(myTeacherIt);
                PushUtils.updateMsgWithPushType(this, user.getUserId(), PushType.STUDENT_NEW_TEACHER.toInt());
                ViewUtils.setViewDrawableLeftTips(tvMyTeahcer, false);
                break;
            case R.id.my_choose_school_ll:
//                Intent csIt = new Intent(this, MyChooseSchoolActivity.class);
//                startActivity(csIt);

                Intent schoolIt = new Intent(this, WebActivity.class);
                schoolIt.putExtra(Constant.URL, String.format(URL.WEB_CHOOSE_SCHOOL, user.getCode()));
                schoolIt.putExtra(Constant.TITLE, getString(R.string.my_choose_school));
                startActivity(schoolIt);
                PushUtils.updateMsgWithPushType(this, user.getUserId(), PushType.STUDENT_NEW_UNIVERSITY.toInt());
                PushUtils.updateMsgWithPushType(this, user.getUserId(), PushType.STUDENT_UNIVERSITY_TATUS.toInt());
                ViewUtils.setViewDrawableLeftTips(tvMyChooseSchool, false);
                break;
            case R.id.my_datum_ll:
                Intent datumIt = new Intent(this, DatumWritActivity.class);
                datumIt.putExtra(Constant.KEY_TYPE, Constant.DATUM);
                startActivity(datumIt);
                break;
            case R.id.my_writ_ll:
                Intent writIt = new Intent(this, DatumWritActivity.class);
                writIt.putExtra(Constant.KEY_TYPE, Constant.WRIT);
                startActivity(writIt);
                break;
            case R.id.my_plan_ll:
                Intent webIt = new Intent(this, WebActivity.class);
                webIt.putExtra(Constant.URL, String.format(URL.WEB_MY_PLAN, user.getCode()));
                webIt.putExtra(Constant.TITLE, getString(R.string.my_plan));
                startActivity(webIt);
                PushUtils.updateMsgWithPushType(this, user.getUserId(), PushType.STUDENT_PLAN_UPDATE.toInt());
                ViewUtils.setViewDrawableLeftTips(tvMyPlan, false);
                break;
            case R.id.my_score_ll:
                Intent scoreIt = new Intent(this, WebActivity.class);
                scoreIt.putExtra(Constant.URL, String.format(URL.WEB_MY_SCORE, user.getCode()));
                scoreIt.putExtra(Constant.TITLE, getString(R.string.my_score));
                startActivity(scoreIt);
                PushUtils.updateMsgWithPushType(this, user.getUserId(), PushType.STUDENT_NEW_SCORE.toInt());
                ViewUtils.setViewDrawableLeftTips(tvMyScore, false);
                break;
            case R.id.right_iv:
                Intent setIt = new Intent(this, SettingActivity.class);
                startActivity(setIt);
                break;
            case R.id.my_complain_ll:
                Intent complainIt = new Intent(this, MyComplainActivity.class);
                startActivity(complainIt);
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
                        StudentMianActivity.this).getUser();
                headImg.setImageUrl(user.getPhoto());
            } else if (Constant.ACTION_REFRESH_MESSAGE_COUNT.equals(intent.getAction())) {
                int pushType = intent.getIntExtra(Constant.PUSH_TYPE, -1);
                if (pushType == PushType.STUDENT_NEW_TEACHER.toInt()) {
                    setMyTeacherNewMsg();
                } else if (pushType == PushType.STUDENT_NEW_UNIVERSITY.toInt()
                        || pushType == PushType.STUDENT_UNIVERSITY_TATUS.toInt()) {
                    setChooseSchoolNewMsg();
                } else if (pushType == PushType.STUDENT_PLAN_UPDATE.toInt()) {
                    setMyPlanNewMsg();
                } else if (pushType == PushType.STUDENT_NEW_SCORE.toInt()) {
                    setMyScoreNewMsg();
                } else if (pushType == PushType.SYSTEM_PUSH.toInt()) {
                    updateUnreadMsgCount();
                }
            }
        }

    };

}
