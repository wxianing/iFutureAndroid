package com.meten.ifuture.activity.teacher;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.meten.ifuture.R;
import com.meten.ifuture.activity.WebActivity;
import com.meten.ifuture.activity.base.BaseActivity;
import com.meten.ifuture.activity.student.DatumWritActivity;
import com.meten.ifuture.activity.student.MyTeacherActivity;
import com.meten.ifuture.constant.Constant;
import com.meten.ifuture.constant.URL;
import com.meten.ifuture.model.User;
import com.meten.ifuture.utils.SharedPreferencesUtils;
import com.meten.ifuture.view.CircularImage;

/**
 * Created by Cmad on 2015/3/21.
 */
public class StudentInfoActivity extends BaseActivity implements View.OnClickListener {
    private User student;
    private CircularImage headImg;
    private Button btnCancel;
    private TextView tvTeacher;
    private TextView tvPlan;
    private TextView tvChooseSchool;
    private TextView tvDatum;
    private TextView tvWrit;
    private TextView tvScore;

    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_info);

        student = (User) getIntent().getSerializableExtra(Constant.STUDENT_KEY);
        currentUser = SharedPreferencesUtils.getInstance(this).getUser();

        initView();
    }

    private void initView() {
        headImg = (CircularImage) findViewById(R.id.head_img);
        btnCancel = (Button) findViewById(R.id.cancel_btn);
        tvTeacher = (TextView) findViewById(R.id.stu_teacher_tv);
        tvPlan = (TextView) findViewById(R.id.stu_plan_tv);
        tvChooseSchool = (TextView) findViewById(R.id.stu_choose_school_tv);
        tvDatum = (TextView) findViewById(R.id.stu_datum_tv);
        tvWrit = (TextView) findViewById(R.id.stu_writ_tv);
        tvScore = (TextView) findViewById(R.id.stu_score_tv);

        btnCancel.setOnClickListener(this);
        tvTeacher.setOnClickListener(this);
        tvPlan.setOnClickListener(this);
        tvChooseSchool.setOnClickListener(this);
        tvDatum.setOnClickListener(this);
        tvWrit.setOnClickListener(this);
        tvScore.setOnClickListener(this);

        if (student != null) {
            headImg.setImageUrl(student.getPhoto());
            headImg.setBorderDrawable(R.drawable.stu_default_headimg);
            headImg.setBorderSize(getResources().getDimension(R.dimen.dp10));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel_btn:
                finish();
                overridePendingTransition(0,R.anim.activity_bottom_out);
                break;
            case R.id.stu_teacher_tv:
                startActivity(MyTeacherActivity.class,-1);
                break;
            case R.id.stu_plan_tv:
                startActivity(URL.WEB_MY_PLAN,"学生规划");
                break;
            case R.id.stu_choose_school_tv:
                startActivity(URL.WEB_CHOOSE_SCHOOL,"学生选校");
                break;
            case R.id.stu_datum_tv:
                startActivity(DatumWritActivity.class,Constant.DATUM);
                break;
            case R.id.stu_writ_tv:
                startActivity(DatumWritActivity.class,Constant.WRIT);
                break;
            case R.id.stu_score_tv:
                startActivity(URL.WEB_MY_SCORE,"学生成绩");
                break;

        }
    }

    private void startActivity(Class c,int type) {
        Intent intent = new Intent(this, c);
        if(type == Constant.DATUM || type == Constant.WRIT){
            intent.putExtra(Constant.PAST_STUDENT,getIntent().getBooleanExtra(Constant.PAST_STUDENT,false));
        }
        intent.putExtra(Constant.STUDENT_ID, student.getUserId());
        intent.putExtra(Constant.STUDENT_NAME, student.getCnName());
        intent.putExtra(Constant.KEY_TYPE, type);
        startActivity(intent);
    }

    private void startActivity(String url, String title) {
        Intent webIt = new Intent(this, WebActivity.class);
        url+=("&"+Constant.STUDENT_ID+"="+student.getUserId()+"&"+Constant.STUDENT_NAME+"="+student.getCnName());
        webIt.putExtra(Constant.URL, String.format(url, currentUser.getCode()));
        webIt.putExtra(Constant.TITLE, title);
        startActivity(webIt);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0,R.anim.activity_bottom_out);
    }
}
