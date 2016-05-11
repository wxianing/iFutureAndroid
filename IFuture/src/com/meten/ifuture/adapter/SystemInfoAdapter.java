package com.meten.ifuture.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.meten.ifuture.R;
import com.meten.ifuture.model.PushMessage;
import com.meten.ifuture.utils.DateUtils;

public class SystemInfoAdapter extends CustomBaseAdapter<PushMessage> {



	public SystemInfoAdapter(Context context) {
		super(context);
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Holder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.system_information_item, null);
			holder = new Holder();
			holder.tvTime = (TextView) convertView
					.findViewById(R.id.system_time_tv);
			holder.llItem = (LinearLayout) convertView
					.findViewById(R.id.system_item_ll);
			holder.tvContent = (TextView) convertView
					.findViewById(R.id.system_information_tv);
			convertView.setTag(holder);
		}else{
			holder = (Holder) convertView.getTag();
		}
		PushMessage msg = listData.get(position);
		if(position > 0 && DateUtils.isOneDay(msg.getTime(), listData.get(position - 1).getTime())){
			holder.tvTime.setVisibility(View.GONE);
			holder.llItem.setBackgroundResource(R.drawable.reward_item_bg);
		}else{
			holder.tvTime.setVisibility(View.VISIBLE);
			holder.tvTime.setText(DateUtils.getDateToString(msg.getTime()));
			holder.llItem.setBackgroundResource(R.drawable.reward_firstitem_bg);
		}
		holder.tvContent.setText(msg.getContent());
		return convertView;
	}
	private class Holder{
		TextView tvTime;
		LinearLayout llItem;
		TextView tvContent;
	}

}
