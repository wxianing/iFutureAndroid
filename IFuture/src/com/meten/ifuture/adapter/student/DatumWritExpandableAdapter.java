package com.meten.ifuture.adapter.student;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.http.RequestParams;
import com.meten.ifuture.AppManager;
import com.meten.ifuture.R;
import com.meten.ifuture.constant.Constant;
import com.meten.ifuture.constant.URL;
import com.meten.ifuture.dialog.BigImageDialog;
import com.meten.ifuture.http.HttpRequestCallBack;
import com.meten.ifuture.http.HttpRequestUtils;
import com.meten.ifuture.http.RequestParamsUtils;
import com.meten.ifuture.model.Resource;
import com.meten.ifuture.model.ResultInfo;
import com.meten.ifuture.model.student.ResourceType;
import com.meten.ifuture.popup.SelectPicPopupWindow;
import com.meten.ifuture.utils.ImageUtils;
import com.meten.ifuture.utils.LogUtils;
import com.meten.ifuture.view.wave.WaveView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DatumWritExpandableAdapter extends BaseExpandableListAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<ResourceType> data;
    private int type = Constant.DATUM;
    private ResourceType resType;
    private int addPosition;
    private boolean isDelete;
    private boolean isPastStudent;


    public DatumWritExpandableAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    public void setType(int type) {
        this.type = type;
    }

    public ResourceType getResourceType() {
        return resType;
    }

    /**
     * 更新数据 替换上传时本地的资源信息
     * @param res 上传成功返回的资源信息
     * @param code 本地资源id
     */
    public void updateRes(Resource res, int code){
        for(ResourceType t : getListData()){
            for(Resource r : t.getItems()){
                if(r.getId() == code){
                    r.copy(res);
                    notifyDataSetChanged();
                    return;
                }
            }
        }
    }

    /**
     * 上传失败时移除本地的资源信息
     * @param id 资源id
     */
    public void removeResWithId(int id){
        for(ResourceType t : getListData()){
            for(Resource r : t.getItems()){
                if(r.getId() == id){
                    t.getItems().remove(r);
                    notifyDataSetChanged();
                    return;
                }
            }
        }
    }

    public void addResource(){

    }


    public boolean isDelete(){
        return isDelete;
    }

    public void setIsDelete(boolean isDelete){
        this.isDelete = isDelete;
    }

    public void setPastStudent(boolean isPastStudent){
        this.isPastStudent = isPastStudent;
    }

    public int getAddGroupPosition() {
        return addPosition;
    }

    public void setListData(List<ResourceType> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public List<ResourceType> getListData() {
        if (data == null) {
            data = new ArrayList<ResourceType>();
        }
        return data;
    }

    @Override
    public int getGroupCount() {
        // TODO Auto-generated method stub
        if (data == null) {
            return 0;
        }
        return data.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if (data.get(groupPosition).getItems() == null) {
            return 0;
        }
        int count;
        if (data.get(groupPosition).getItems().size() % 3 == 0) {
            count = data.get(groupPosition).getItems().size() / 3;
        } else {
            count = data.get(groupPosition).getItems().size() / 3 + 1;
        }
        return count;
    }

    @Override
    public Object getGroup(int groupPosition) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        GroupHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.datum_writ_group, null);
            holder = new GroupHolder();
            holder.tvName = (TextView) convertView.findViewById(R.id.name_tv);
            holder.ivAdd = (ImageView) convertView.findViewById(R.id.add_iv);
            holder.indicator = (ImageView) convertView
                    .findViewById(R.id.indicator);
            holder.ivAdd.setOnClickListener(addClickListener);
            if(isPastStudent){
                holder.ivAdd.setVisibility(View.INVISIBLE);
            }
            convertView.setTag(holder);
        } else {
            holder = (GroupHolder) convertView.getTag();
        }

        ResourceType res = data.get(groupPosition);
        if (isExpanded) {
            holder.indicator.setSelected(true);
        } else {
            holder.indicator.setSelected(false);
        }

        holder.tvName.setText(res.getName());
        holder.ivAdd.setTag(groupPosition);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        RelativeLayout[] layouts = null;
//		ChildHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.resource_item, null);
            layouts = new RelativeLayout[3];
            layouts[0] = (RelativeLayout) convertView.findViewById(R.id.resource1);
            layouts[1] = (RelativeLayout) convertView.findViewById(R.id.resource2);
            layouts[2] = (RelativeLayout) convertView.findViewById(R.id.resource3);
            findView(layouts[0]);
            findView(layouts[1]);
            findView(layouts[2]);
            convertView.setTag(layouts);
        } else {
            layouts = (RelativeLayout[]) convertView.getTag();
        }


        for (int i = 0; i < 3; i++) {
            ChildHolder holder = (ChildHolder) layouts[i].getTag();
            if (childPosition * 3 + i < data.get(groupPosition).getItems().size()) {
                Resource item = data.get(groupPosition).getItems().get(childPosition * 3 + i);

                if(ImageUtils.isImage(item.getResourceExtention())){
                    displayImage(holder, item);
                    holder.llFile.setVisibility(View.GONE);
                }else{
                    holder.llFile.setVisibility(View.VISIBLE);
                    holder.tvFileName.setText(item.getName());
                    holder.tvFileFormat.setText("."+item.getResourceExtention());
                }

                holder.ivDelete.setTag(item.getId());
                holder.img.setTag(item);

                if(isDelete){
                    holder.ivDelete.setVisibility(View.VISIBLE);
                }else{
                    holder.ivDelete.setVisibility(View.GONE);
                }

                LogUtils.e("id:"+item.getId()+"  progress:"+item.getProgress());
                if(item.getId() < 0){
                    holder.wave.setVisibility(View.VISIBLE);
                    holder.wave.setProgress(item.getProgress());
                }else{
                    holder.wave.setVisibility(View.GONE);
                }
                holder.img.setVisibility(View.VISIBLE);

            } else {
                holder.img.setVisibility(View.GONE);
                holder.wave.setVisibility(View.GONE);
                holder.ivDelete.setVisibility(View.GONE);
            }


        }


        return convertView;
    }

    private void displayImage(ChildHolder holder, Resource item) {
        BitmapUtils bitmapUtils = new BitmapUtils(context);
        bitmapUtils.configDefaultLoadingImage(R.drawable.ads_default);
        bitmapUtils.configDefaultBitmapMaxSize(200,200);
        bitmapUtils.configDefaultLoadFailedImage(R.drawable.ads_default);
        bitmapUtils.display(holder.img,item.getThumbnailUrl());
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        return true;
    }




    OnClickListener addClickListener = new OnClickListener(){

        @Override
        public void onClick(View v) {

            addPosition = (int)v.getTag();
            resType = getListData().get(addPosition);
            //选择图片
//            if (type == Constant.DATUM) {
//                ImageUtils.openLocalImage(AppManager.getAppManager()
//                        .currentActivity());
            SelectPicPopupWindow popup = new SelectPicPopupWindow(
                    (Activity)context);
            popup.setTitle("选择图片");
            popup.show();
//            } else if (type == Constant.WRIT) {//选择文件
//                AppManager.showFileChooser();
//            }
        }

    };

    View.OnLongClickListener longClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            isDelete = true;
            notifyDataSetChanged();
            return false;
        }
    };


     OnClickListener onItemListener = new OnClickListener(){

        @Override
        public void onClick(final View v) {
            switch (v.getId()) {
                case R.id.delete_iv:

                    new AlertDialog.Builder(context).setTitle("提示")
                            .setMessage("确认删除？")
                            .setPositiveButton("确认", new AlertDialog.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    int id = (int)v.getTag();
                                    RequestParams params = RequestParamsUtils.deleteResource(id);
                                    params = RequestParamsUtils.addStudentParams(params, ((Activity) context).getIntent());
                                    HttpRequestUtils.create(context).send(URL.DELETE_RESOURCE,
                                            params, id, callback);
                                }
                            }).setNegativeButton("取消", null).show();


                    break;
                case R.id.resource_img:
                    Resource res = (Resource) v.getTag();
                    String url = res.getUrl();
                    String format = res.getResourceExtention();
                    if (ImageUtils.isImage(format)) {
                        BigImageDialog dialog = new BigImageDialog(context, res,isPastStudent);
                        dialog.setHttpCallBack(callback);
                        dialog.show();
                    } else {
                        HttpRequestUtils.create(context).download(url,
                                res.getName() + "." + res.getResourceExtention(),
                                0, downloadCallback);
                    }

                    break;
            }

        }

    };

    HttpRequestCallBack<ResultInfo> callback = new HttpRequestCallBack<ResultInfo>() {

        @Override
        public void onSuccess(ResultInfo info, int requestCode) {
            for(ResourceType t : getListData()){
                for(Resource r : t.getItems()){
                    if(r.getId() == requestCode){
                        t.getItems().remove(r);
                        notifyDataSetChanged();
                        isDelete = false;
                        return;
                    }
                }
            }

//            notifyDataSetChanged();

        }
    };
    HttpRequestCallBack<File> downloadCallback = new HttpRequestCallBack<File>() {

        @Override
        public void onSuccess(File file, int requestCode) {
            AppManager.getAppManager().openFile(file);
        }
    };

    private ChildHolder findView(View view) {
        ChildHolder holder = new ChildHolder();
        holder.img = (ImageView) view.findViewById(R.id.resource_img);
        holder.wave = (WaveView) view.findViewById(R.id.wave);
        holder.ivDelete = (ImageView) view.findViewById(R.id.delete_iv);
        holder.llFile = (LinearLayout) view.findViewById(R.id.file_ll);
        holder.tvFileName = (TextView) view.findViewById(R.id.filename_tv);
        holder.tvFileFormat = (TextView) view.findViewById(R.id.file_format_tv);
        holder.ivDelete.setOnClickListener(onItemListener);
        holder.img.setOnClickListener(onItemListener);
        if(!isPastStudent){
            holder.img.setOnLongClickListener(longClickListener);
        }
        view.setTag(holder);
        return holder;
    }

    class GroupHolder {
        TextView tvName;
        ImageView ivAdd;
        ImageView indicator;
    }


    class ChildHolder {
        ImageView img;
        WaveView wave;
        ImageView ivDelete;
        LinearLayout llFile;
        TextView tvFileName;
        TextView tvFileFormat;
    }


}
