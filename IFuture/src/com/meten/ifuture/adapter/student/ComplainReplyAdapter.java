package com.meten.ifuture.adapter.student;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.meten.ifuture.R;
import com.meten.ifuture.adapter.CustomBaseAdapter;
import com.meten.ifuture.model.student.ComplainReply;

/**
 * Created by Cmad on 2015/3/6.
 */
public class ComplainReplyAdapter extends CustomBaseAdapter<ComplainReply> {
    /**
     * CustomBaseAdapter
     *
     * @param context
     */
    public ComplainReplyAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = null;
        if(convertView == null){
            convertView = listContainer.inflate(R.layout.complain_details_reply_item,parent,false);
            holder = new Holder();
            holder.tvReplyUserNmae = (TextView) convertView.findViewById(R.id.reply_user_name_tv);
            holder.tvReplyDate = (TextView) convertView.findViewById(R.id.complain_date_tv);
            holder.tvReplyContent = (TextView) convertView.findViewById(R.id.complain_content_tv);
            convertView.setTag(holder);
        }else{
            holder = (Holder) convertView.getTag();
        }
        ComplainReply reply = listData.get(position);
        holder.tvReplyUserNmae.setText(reply.getReplyUserName());
        holder.tvReplyDate.setText(reply.getReplyDate());
        holder.tvReplyContent.setText(reply.getContent());
        return convertView;
    }

    private class Holder{
        TextView tvReplyUserNmae;
        TextView tvReplyDate;
        TextView tvReplyContent;
    }
}
