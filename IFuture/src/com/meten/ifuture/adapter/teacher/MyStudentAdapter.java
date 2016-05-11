package com.meten.ifuture.adapter.teacher;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.meten.ifuture.AppManager;
import com.meten.ifuture.R;
import com.meten.ifuture.activity.teacher.StudentInfoActivity;
import com.meten.ifuture.adapter.CustomBaseAdapter;
import com.meten.ifuture.constant.Constant;
import com.meten.ifuture.model.User;
import com.meten.ifuture.model.teacher.Student;
import com.meten.ifuture.view.CircularImage;

/**
 * Created by Administrator on 2015/3/3.
 */
public class MyStudentAdapter extends CustomBaseAdapter<Student>{
    /**
     * CustomBaseAdapter
     *
     * @param context
     */
    public MyStudentAdapter(Context context) {
        super(context);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = null;
        if(convertView == null){
            convertView = listContainer.inflate(R.layout.my_student_item,null);
            holder = new Holder();
            holder.headImg = (CircularImage) convertView.findViewById(R.id.head_img);
            holder.tvName = (TextView) convertView.findViewById(R.id.name_tv);
            holder.tvMobile = (TextView) convertView.findViewById(R.id.mobile_tv);
            holder.tvCenter = (TextView) convertView.findViewById(R.id.center_tv);
            holder.tvContract = (TextView) convertView.findViewById(R.id.contract_tv);
            holder.tvEmail = (TextView) convertView.findViewById(R.id.email_tv);
            holder.tvQQ = (TextView) convertView.findViewById(R.id.qq_tv);
            holder.llMobile = (LinearLayout) convertView.findViewById(R.id.mobile_ll);
            holder.llWechat = (LinearLayout) convertView.findViewById(R.id.wechat_ll);
            holder.llEmail = (LinearLayout) convertView.findViewById(R.id.email_ll);
            holder.llQQ = (LinearLayout) convertView.findViewById(R.id.qq_ll);
            convertView.setTag(holder);
        }else {
            holder = (Holder) convertView.getTag();
        }
        Student stu = listData.get(position);
        holder.headImg.setImageUrl(stu.getPhoto());
        holder.tvName.setText(stu.getName());
        holder.tvMobile.setText("电话:"+stu.getMobile());
        holder.tvContract.setText(stu.getContractType());
        holder.tvQQ.setText(stu.getQQ());
        holder.tvEmail.setText(stu.getEmail());
        holder.tvCenter.setText(stu.getCenterName());

        ClickListener listener = new ClickListener(stu);
        holder.llMobile.setOnClickListener(listener);
        holder.llQQ.setOnClickListener(listener);
        holder.llEmail.setOnClickListener(listener);
        holder.llWechat.setOnClickListener(listener);
        holder.headImg.setOnClickListener(listener);

        return convertView;
    }


    class ClickListener implements View.OnClickListener {
        private User stu;
        public ClickListener(User stu) {
            this.stu = stu;
        }

        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.mobile_ll:
                    AppManager.launchPhoneActivity(stu.getMobile());
                    break;
                case R.id.qq_ll:
                    AppManager.launchQQAPP(stu.getQQ());
                    break;
                case R.id.email_ll:
                    AppManager.launchEmailApp(stu.getEmail());
                    break;
                case R.id.wechat_ll:
                    AppManager.launchWechatApp();
                    break;
                case R.id.head_img:
                    Intent intent = new Intent(context, StudentInfoActivity.class);
//                    intent.putExtra("stuId",stu.getUserId());
//                    intent.putExtra("cnName",stu.getCnName());
                    intent.putExtra(Constant.STUDENT_KEY,stu);
                    context.startActivity(intent);
                    ((Activity)context).overridePendingTransition(R.anim.push_bottom_in,0);
                    break;
            }

        }

    }

    private class Holder{
        CircularImage headImg;
        TextView tvName;
        TextView tvMobile;
        TextView tvContract;
        TextView tvEmail;
        TextView tvQQ;
        TextView tvCenter;
        LinearLayout llMobile;
        LinearLayout llWechat;
        LinearLayout llEmail;
        LinearLayout llQQ;

    }


}
