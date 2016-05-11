package com.meten.ifuture.adapter.student;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.lidroid.xutils.http.RequestParams;
import com.meten.ifuture.R;
import com.meten.ifuture.adapter.CustomBaseAdapter;
import com.meten.ifuture.constant.Constant;
import com.meten.ifuture.constant.URL;
import com.meten.ifuture.http.HttpRequestCallBack;
import com.meten.ifuture.http.HttpRequestUtils;
import com.meten.ifuture.http.RequestParamsUtils;
import com.meten.ifuture.model.ResultInfo;
import com.meten.ifuture.model.student.Complain;
import com.meten.ifuture.utils.ViewUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Cmad on 2015/3/6.
 */
public class MyComplainAdapter extends CustomBaseAdapter<Complain> {
    private int type;
    private Map<Complain,Boolean> statusMap = new HashMap<Complain,Boolean>();
    /**
     * CustomBaseAdapter
     *
     * @param context
     */
    public MyComplainAdapter(Context context) {
        super(context);
    }

    @Override
    public void setListData(List<Complain> listData) {
        super.setListData(listData);
        if(listData != null ){
            for(Complain c : listData){
                statusMap.put(c,false);
            }
        }
    }

    @Override
    public void addData(List<Complain> data) {
        super.addData(data);
        if(data != null ){
            for(Complain c: data){
                statusMap.put(c,false);
            }
        }
    }

    @Override
    public void addData(Complain complain) {
        if(complain == null){
            return;
        }
        if (listData == null) {
            listData = new ArrayList<Complain>();
        }
        this.listData.add(0,complain);

        statusMap.put(complain,false);
        notifyDataSetChanged();
    }

    public void setType(int type){
        this.type = type;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = null;
        if(convertView == null){
            convertView = listContainer.inflate(R.layout.complain_item,parent,false);
            holder = new Holder();
            holder.tvComplainObject = (TextView) convertView.findViewById(R.id.complain_object_tv);
            holder.tvComplainDate = (TextView) convertView.findViewById(R.id.complain_date_tv);
            holder.tvComplainContent = (TextView) convertView.findViewById(R.id.complain_content_tv);
            holder.btnHand = (Button) convertView.findViewById(R.id.hand_btn);
            holder.btnBottomHand = (Button) convertView.findViewById(R.id.hand_bottom_btn);
            TextView tv = (TextView) convertView.findViewById(R.id.complain_hint_tv);
            convertView.setOnClickListener(listener);
            holder.btnHand.setOnClickListener(listener);
            holder.btnBottomHand.setOnClickListener(listener);
            convertView.setTag(holder);
            if(type == Constant.MANAGER){
                tv.setText(context.getString(R.string.complain_student));
            }
        }else{
            holder = (Holder) convertView.getTag();
        }

        Complain complain = listData.get(position);
        if(type == Constant.MANAGER){
            holder.tvComplainObject.setText(complain.getFromCnName());
        }else{
            holder.tvComplainObject.setText(complain.getToCnName()+" - "+complain.getToRoleName());
        }
        holder.tvComplainDate.setText(complain.getCreateTime());
        holder.tvComplainContent.setText(complain.getContent());
        holder.btnHand.setTag(position);
        holder.btnBottomHand.setTag(position);

        if(complain.getStatus() == Constant.UNHANDLED){
            holder.btnBottomHand.setEnabled(true);
            holder.btnHand.setEnabled(true);
            holder.btnBottomHand.setText("确认处理");
            holder.btnHand.setText("确认处理");
        }else{
            holder.btnBottomHand.setEnabled(false);
            holder.btnHand.setEnabled(false);
            holder.btnBottomHand.setText(complain.getStatusText());
            holder.btnHand.setText(complain.getStatusText());
        }

        setHandButtonIsShow(position, holder);
        return convertView;
    }

    private void setHandButtonIsShow(int position, Holder holder) {
        if(statusMap.get(listData.get(position))){
            holder.tvComplainContent.setSingleLine(false);
            holder.btnBottomHand.setVisibility(View.VISIBLE);
            holder.btnHand.setVisibility(View.GONE);
        }else{
            holder.tvComplainContent.setSingleLine(true);
            holder.btnBottomHand.setVisibility(View.GONE);
            holder.btnHand.setVisibility(View.VISIBLE);
        }
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.container:
                    Holder holder = (Holder) v.getTag();
                    if(ViewUtils.isOverFlowed(holder.tvComplainContent)){
                        int position = (int) holder.btnHand.getTag();
                        statusMap.put(listData.get(position), !statusMap.get(listData.get(position)));
                        setHandButtonIsShow(position, holder);
                    }
                    break;
                case R.id.hand_btn:
                case R.id.hand_bottom_btn:
                    int p = (int)v.getTag();
                    final Complain complain = listData.get(p);
                    RequestParams params = RequestParamsUtils.handComplain(complain.getId());
                    HttpRequestUtils.create(context).send(URL.HAND_COMPLAIN,params,p,new HttpRequestCallBack<ResultInfo>() {
                        @Override
                        public void onSuccess(ResultInfo info, int requestCode) {
                            complain.setStatus(Constant.HANDLED);
                            complain.setStatusText("已处理");
                            notifyDataSetChanged();
                        }
                    });
                    break;

            }
        }
    };

    private class Holder{
        TextView tvComplainObject;
        TextView tvComplainDate;
        TextView tvComplainContent;
        Button btnHand;
        Button btnBottomHand;
    }
}
