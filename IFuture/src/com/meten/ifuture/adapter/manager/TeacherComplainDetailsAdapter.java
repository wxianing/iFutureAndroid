package com.meten.ifuture.adapter.manager;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.meten.ifuture.R;
import com.meten.ifuture.adapter.CustomBaseAdapter;
import com.meten.ifuture.constant.Constant;
import com.meten.ifuture.model.student.Complain;
import com.meten.ifuture.view.CircularImage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Cmad on 2015/3/6.
 */
public class TeacherComplainDetailsAdapter extends CustomBaseAdapter<Complain> {
    private int type;
    private Map<Integer,Boolean> statusMap = new HashMap<Integer,Boolean>();
    /**
     * CustomBaseAdapter
     *
     * @param context
     */
    public TeacherComplainDetailsAdapter(Context context) {
        super(context);
    }

    @Override
    public void setListData(List<Complain> listData) {
        super.setListData(listData);
        if(listData != null ){
            for(int i=0;i<listData.size();i++){
                statusMap.put(i,false);
            }
        }
    }

    @Override
    public void addData(List<Complain> data) {
        int oldsize = listData == null ? 0:listData.size();
        super.addData(data);
        if(data != null ){
            for(int i=oldsize;i<oldsize+data.size();i++){
                statusMap.put(i,false);
            }
        }
    }

    @Override
    public void addData(Complain complain) {
        int oldsize = listData == null ? 0:listData.size();
        if(complain == null){
            return;
        }
        if (listData == null) {
            listData = new ArrayList<Complain>();
        }
        this.listData.add(0,complain);

        if(oldsize > 0 && complain != null){
            statusMap.put(oldsize,false);
        }
        notifyDataSetChanged();
    }

    public void setType(int type){
        this.type = type;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = null;
        if(convertView == null){
            convertView = listContainer.inflate(R.layout.manager_teacher_complain_details,parent,false);
            holder = new Holder();
            holder.convertView = convertView;
            holder.tvName = (TextView) convertView.findViewById(R.id.name_tv);
            holder.headImg = (CircularImage) convertView.findViewById(R.id.head_img);
            holder.tvDate = (TextView) convertView.findViewById(R.id.complain_date_tv);
            holder.tvRole = (TextView) convertView.findViewById(R.id.role_tv);
            holder.tvStatus = (TextView) convertView.findViewById(R.id.complain_status_tv);
            holder.tvContent = (TextView) convertView.findViewById(R.id.complain_content_tv);
            holder.llContent = (LinearLayout) convertView.findViewById(R.id.complain_content_lv);
            holder.tvComplainObject = (TextView) convertView.findViewById(R.id.complain_object_tv);
            holder.headImg.setHasBorder(true);
            convertView.setOnClickListener(listener);
            convertView.setTag(holder);
        }else{
            holder = (Holder) convertView.getTag();
        }

        Complain complain = listData.get(position);
        holder.headImg.setImageUrl(complain.getFromUserPhoto());
        holder.tvName.setTag(position);
        holder.tvDate.setText(complain.getCreateTime());
        holder.tvContent.setText("投诉内容：" + complain.getContent());
        holder.tvComplainObject.setText(complain.getToCnName());
        holder.tvRole.setText(complain.getToRoleName());
        holder.tvStatus.setText(complain.getStatusText());
        if(complain.getStatus() == Constant.HANDLED){
           holder.tvStatus.setTextColor(context.getResources().getColor(R.color.head_bg_color));
        }else{
            holder.tvStatus.setTextColor(context.getResources().getColor(R.color.unhandled));
        }

        setHandButtonIsShow(position, holder);

        return convertView;
    }

    private void setHandButtonIsShow(int position, Holder holder) {
        if(statusMap.get(position)){
            holder.llContent.setVisibility(View.VISIBLE);
            holder.convertView.setBackgroundColor(context.getResources().getColor(R.color.complain_details_expanding));
        }else{
            holder.llContent.setVisibility(View.GONE);
            holder.convertView.setBackgroundColor(context.getResources().getColor(R.color.white));
        }
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
                Holder holder = (Holder) v.getTag();
                int position = (int) holder.tvName.getTag();
                statusMap.put(position, !statusMap.get(position));
                setHandButtonIsShow(position, holder);
            }

    };

    private class Holder{
        View convertView;
        CircularImage headImg;
        TextView tvName;
        TextView tvRole;
        TextView tvDate;
        TextView tvStatus;
        TextView tvContent;
        LinearLayout llContent;
        TextView tvComplainObject;
    }
}
