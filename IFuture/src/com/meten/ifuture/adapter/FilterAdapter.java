package com.meten.ifuture.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.meten.ifuture.R;
import com.meten.ifuture.model.IResource;

/**
 * Created by Cmad on 2015/3/9.
 *
 */
public class FilterAdapter extends CustomBaseAdapter {
    public static final int AREA = 0x00;
    public static final int TEACHER = 0x01;
//    private int checkedId;
    private int areaCheckedId;
    private int teacherCheckedId;
    private int typeId;
    /**
     * CustomBaseAdapter
     *
     * @param context
     */
    public FilterAdapter(Context context) {
        super(context);
    }

    public void setCheckedId(int id){
//        this.checkedId = id;
        if(typeId == AREA){
            areaCheckedId = id;
        }else if(typeId == TEACHER){
            teacherCheckedId = id;
        }
        notifyDataSetChanged();
    }

    public void setTeacherCheckedId(int id){
        this.teacherCheckedId =id;
    }


    public void setTypeId(int typeId){
        this.typeId = typeId;
    }

    public int getTypeId(){
        return typeId;
    }


    @Override
    public long getItemId(int position) {
        return ((IResource)listData.get(position)).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holer holer = null;
        if(convertView == null){
            convertView = listContainer.inflate(R.layout.filter_area_item,null);
            holer = new Holer();
            holer.ident = convertView.findViewById(R.id.ident);
            holer.tvAreaName = (TextView) convertView.findViewById(R.id.area_name_tv);
            convertView.setTag(holer);
        }else{
            holer = (Holer) convertView.getTag();
        }

        IResource resource = (IResource)listData.get(position);
        holer.tvAreaName.setText(resource.getName());
        if(typeId == AREA){
            showCheckedItem(convertView, holer, resource,areaCheckedId);
        }else if(typeId == TEACHER){
            showCheckedItem(convertView, holer, resource,teacherCheckedId);
        }

        return convertView;
    }

    private void showCheckedItem(View convertView, Holer holer, IResource resource, int id) {
        if(resource.getId() == id){
            convertView.setEnabled(true);
            holer.ident.setVisibility(View.VISIBLE);
        }else{
            convertView.setEnabled(false);
            holer.ident.setVisibility(View.INVISIBLE);
        }
    }

    private class Holer {
        View ident;
        TextView tvAreaName;
    }
}
