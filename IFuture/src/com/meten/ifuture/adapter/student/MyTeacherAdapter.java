package com.meten.ifuture.adapter.student;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.http.RequestParams;
import com.meten.ifuture.AppManager;
import com.meten.ifuture.R;
import com.meten.ifuture.adapter.CustomBaseAdapter;
import com.meten.ifuture.constant.Constant;
import com.meten.ifuture.constant.URL;
import com.meten.ifuture.http.HttpRequestCallBack;
import com.meten.ifuture.http.HttpRequestUtils;
import com.meten.ifuture.http.RequestParamsUtils;
import com.meten.ifuture.model.ResultInfo;
import com.meten.ifuture.model.User;
import com.meten.ifuture.model.student.MyTeacher;
import com.meten.ifuture.utils.SharedPreferencesUtils;
import com.meten.ifuture.view.CircularImage;

public class MyTeacherAdapter extends CustomBaseAdapter<MyTeacher> {
    private Holder showPraiseHolder;
    private ComplainClickListener complainClickListener;
    private PraiseCallBack callBack = new PraiseCallBack();
    private User currentUser;

	public MyTeacherAdapter(Context context) {
		super(context);
        currentUser = SharedPreferencesUtils.getInstance(context).getUser();
	}

    public void setComplainClickListener(ComplainClickListener listener){
        this.complainClickListener = listener;
    }

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder = null;
		if(convertView == null){
			convertView = listContainer.inflate(R.layout.student_my_teacher_item, null);
			holder = new Holder();
			holder.headImg = (CircularImage) convertView.findViewById(R.id.head_img);
			holder.tvName = (TextView) convertView.findViewById(R.id.name_tv);
			holder.tvJob = (TextView) convertView.findViewById(R.id.job_tv);
			holder.tvComplain = (TextView) convertView.findViewById(R.id.complain_tv);
			holder.cbPraise = (CheckBox) convertView.findViewById(R.id.praise_cb);
			holder.tvMobile = (TextView) convertView.findViewById(R.id.mobile_tv);
			holder.tvWechat = (TextView) convertView.findViewById(R.id.wechat_tv);
			holder.tvEmial = (TextView) convertView.findViewById(R.id.email_tv);
			holder.tvQQ = (TextView) convertView.findViewById(R.id.qq_tv);
            holder.ivMore = (ImageView) convertView.findViewById(R.id.more_iv);
            holder.llMore = (LinearLayout) convertView.findViewById(R.id.more_ll);
            if(currentUser.getUserType() != Constant.STUDENT){
                holder.llMore.setVisibility(View.GONE);
                holder.ivMore.setVisibility(View.GONE);
            }
            convertView.setTag(holder);
            holder.ivMore.setTag(holder);
		}else{
			holder = (Holder) convertView.getTag();
		}
		MyTeacher tea = listData.get(position);
		holder.headImg.setImageUrl(tea.getPhoto());
		holder.tvName.setText(tea.getName());
		holder.tvMobile.setText(tea.getMobile());
		holder.tvQQ.setText(tea.getQQ());
		holder.tvEmial.setText(tea.getEmail());
		holder.tvWechat.setText(tea.getWechat());
		holder.tvJob.setText(tea.getRoleName());

        setPraiseStatus(holder.cbPraise, tea);


        ClickListener listener = new ClickListener(position);
		holder.tvMobile.setOnClickListener(listener);
		holder.tvWechat.setOnClickListener(listener);
		holder.tvEmial.setOnClickListener(listener);
		holder.tvQQ.setOnClickListener(listener);
        holder.ivMore.setOnClickListener(listener);
        holder.tvComplain.setOnClickListener(listener);
        holder.cbPraise.setOnClickListener(listener);
		
		return convertView;
	}

    private void setPraiseStatus(CheckBox cb, MyTeacher tea) {
        if(tea.getIsPraised() > 0){
            cb.setChecked(true);
            cb.setText(context.getString(R.string.praised));
        }else{
            cb.setChecked(false);
            cb.setText(context.getString(R.string.praise));
        }
    }

    class ClickListener implements OnClickListener{
		private MyTeacher tea;
        private int position;
		public ClickListener(int position) {
			this.tea = listData.get(position);
            this.position = position;
		}
		
		@Override
		public void onClick(View v) {

			switch(v.getId()){
                case R.id.mobile_tv:
                    AppManager.launchPhoneActivity(tea.getMobile());
                    break;
                case R.id.qq_tv:
                    AppManager.launchQQAPP(tea.getQQ());
                    break;
                case R.id.email_tv:
                    AppManager.launchEmailApp(tea.getEmail());
                    break;
                case R.id.wechat_tv:
                    AppManager.launchWechatApp();
                    break;
                case R.id.more_iv:
                    dismissPraiseLayout();
                    showPraiseHolder = (Holder) v.getTag();
                    v.setVisibility(View.INVISIBLE);
                    showPraiseHolder.llMore.setVisibility(View.VISIBLE);
                    return;
                case R.id.praise_cb:
//                    CheckBox checkBox = (CheckBox) v;
//                    if(checkBox.isChecked()){
//                        tea.setIsPraised(1);
//                        checkBox.setText(context.getString(R.string.praised));
//                    }else{
//                        tea.setIsPraised(0);
//                        checkBox.setText(context.getString(R.string.praise));
//                    }
                    RequestParams params = RequestParamsUtils.addComplainOrPraise(tea.getUserId(),tea.getCnName(),"");
                    HttpRequestUtils.create(context).send(URL.PRAISE,params,position,callBack);
                    return;
                case R.id.complain_tv:
                    if(complainClickListener != null){
                        complainClickListener.complainClick(tea);
                    }
                    break;
			}
            dismissPraiseLayout();
		}
		
	}


    class PraiseCallBack extends HttpRequestCallBack<ResultInfo>{

        @Override
        public void onSuccess(ResultInfo info, int requestCode) {
            MyTeacher tea = listData.get(requestCode);
            tea.setIsPraised(tea.getIsPraised() > 0 ? 0:1);
            setPraiseStatus(showPraiseHolder.cbPraise,tea);
//            notifyDataSetChanged();
        }

        @Override
        public void onFailure(Context context, ResultInfo info, int requestCode) {
            super.onFailure(context, info, requestCode);
//            MyTeacher tea = listData.get(requestCode);
//            tea.setIsPraised(0);
//            notifyDataSetChanged();
        }
    }

    public void dismissPraiseLayout(){
        if(showPraiseHolder != null){
            showPraiseHolder.llMore.setVisibility(View.GONE);
            showPraiseHolder.ivMore.setVisibility(View.VISIBLE);
            showPraiseHolder = null;
        }
    }
	
	
	class Holder{
		CircularImage headImg;
		TextView tvName;
		TextView tvJob;
		TextView tvComplain;
		CheckBox cbPraise;
		TextView tvMobile;
		TextView tvWechat;
		TextView tvEmial;
		TextView tvQQ;
        ImageView ivMore;
        LinearLayout llMore;
		
	}

    public interface ComplainClickListener{
        public void complainClick(MyTeacher tea);
    }
}
