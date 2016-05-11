package com.meten.ifuture.adapter.student;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.meten.ifuture.R;
import com.meten.ifuture.adapter.CustomBaseAdapter;
import com.meten.ifuture.model.student.MyTeacher;
import com.meten.ifuture.view.CircularImage;

/**
 * Created by Cmad on 2015/3/6.
 */
public class ChooseComplainObjectAdapter extends CustomBaseAdapter<MyTeacher> {
    private int selectUserId = -1;
    /**
     * CustomBaseAdapter
     *
     * @param context
     */
    public ChooseComplainObjectAdapter(Context context) {
        super(context);
    }

    public void setSelectUserId(int userId){
        selectUserId = userId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = null;
        if(convertView == null){
            convertView = listContainer.inflate(R.layout.choose_complain_object_item,null);
            holder = new Holder();
            holder.tvName = (TextView) convertView.findViewById(R.id.name_tv);
            holder.tvRole = (TextView) convertView.findViewById(R.id.role_tv);
            holder.headImg = (CircularImage) convertView.findViewById(R.id.head_img);
            holder.ivSelect = (ImageView) convertView.findViewById(R.id.select_iv);
            convertView.setTag(holder);
        }else{
            holder = (Holder) convertView.getTag();
        }
        MyTeacher tea = listData.get(position);
        holder.headImg.setImageUrl(tea.getPhoto());
        holder.tvName.setText(tea.getName());
        holder.tvRole.setText(tea.getRoleName());

        if(selectUserId == tea.getUserId()){
            holder.ivSelect.setVisibility(View.VISIBLE);
        }else{
            holder.ivSelect.setVisibility(View.GONE);
        }

        return convertView;
    }


    private class Holder {
        CircularImage headImg;
        TextView tvName;
        TextView tvRole;
        ImageView ivSelect;
    }
}
