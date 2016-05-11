package com.meten.ifuture.activity.student;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.http.RequestParams;
import com.meten.ifuture.R;
import com.meten.ifuture.activity.base.BaseHeadActivity;
import com.meten.ifuture.adapter.student.DatumWritExpandableAdapter;
import com.meten.ifuture.constant.Constant;
import com.meten.ifuture.constant.URL;
import com.meten.ifuture.dialog.ProgressDialog.ProgressType;
import com.meten.ifuture.http.HttpRequestCallBack;
import com.meten.ifuture.http.HttpRequestUtils;
import com.meten.ifuture.http.RequestParamsUtils;
import com.meten.ifuture.model.Resource;
import com.meten.ifuture.model.ResultInfo;
import com.meten.ifuture.model.User;
import com.meten.ifuture.model.student.ResourceType;
import com.meten.ifuture.utils.FileUtils;
import com.meten.ifuture.utils.ImageUtils;
import com.meten.ifuture.utils.JsonParse;
import com.meten.ifuture.utils.LogUtils;
import com.meten.ifuture.utils.SharedPreferencesUtils;
import com.meten.ifuture.utils.ToastUtils;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatumWritActivity extends BaseHeadActivity implements View.OnClickListener {
	private static final int GET_RESOURCE_CODE = 0x00;
	private static final int UPLOAD_RESOURCE_CODE = 0x01;
	private static final int SUBMIT = 0x03;
	private ExpandableListView mExListview;
	private static DatumWritExpandableAdapter adapter;
	private TextView tvType;
	private int type; //资源类型（材料 or 文书）

    private static int uploadId;

    private static Map<Integer, Resource> datumUploadRes = new HashMap<Integer,Resource>();
    private static Map<Integer, Resource> writUploadRes = new HashMap<Integer,Resource>();

    private User currentUser;
    private boolean isPastStudent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.datum_writ_activity);
		tvType = (TextView) findViewById(R.id.type_tv);
        currentUser = SharedPreferencesUtils.getInstance(this).getUser();
		type = getIntent().getIntExtra(Constant.KEY_TYPE, Constant.DATUM);
        isPastStudent = getIntent().getBooleanExtra(Constant.PAST_STUDENT,false);

        if(currentUser.getUserType() == Constant.STUDENT){
            setTitle("我的材料");
        }else{
            setTitle("学员材料");
        }

		hiddenRightImageView();

		if(type == Constant.WRIT){
            if(currentUser.getUserType() == Constant.STUDENT){
                setTitle(getString(R.string.my_writ));
            }else{
                setTitle("学员文书");
            }
			tvType.setVisibility(View.GONE);
		}

        if(currentUser.getUserType() == Constant.STUDENT){
            getRightTextView().setVisibility(View.VISIBLE);
            ColorStateList csl= getResources().getColorStateList(R.color.commit_or_save_textcolor_selector);
            getRightTextView().setTextColor(csl);
            getRightTextView().setText("提交");
            getRightTextView().setOnClickListener(this);
        }

		mExListview = (ExpandableListView) findViewById(R.id.expandable_lv);
		adapter = new DatumWritExpandableAdapter(this);
		adapter.setType(type);
        adapter.setPastStudent(isPastStudent);
		mExListview.setAdapter(adapter);

		RequestParams params = RequestParamsUtils.getResourceType(type);
        params = RequestParamsUtils.addStudentParams(params,getIntent());
		HttpRequestUtils.create(this).send(URL.GET_RESOURCE, params,
				GET_RESOURCE_CODE, callback);

        mExListview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    adapter.setIsDelete(false);
                    adapter.notifyDataSetChanged();
                }
                return false;
            }
        });

	}


    public Map<Integer, Resource> getUploadResMap(){
        if(type == Constant.DATUM){
            return datumUploadRes;
        }else {
            return writUploadRes;
        }
    }

	HttpRequestCallBack<ResultInfo> callback = new HttpRequestCallBack<ResultInfo>() {

		@Override
		public void onSuccess(ResultInfo info, int requestCode) {
			switch (requestCode) {
                case GET_RESOURCE_CODE:
                    List<ResourceType> data = JsonParse.parseToObject(info,
                            new TypeToken<List<ResourceType>>() {
                            }.getType());
                    addUploadResouce(data);
                    adapter.setListData(data);
                    for (int i = 0; i < data.size(); i++) {
                        mExListview.expandGroup(i);
                    }
                    break;
                case SUBMIT:
                    ToastUtils.show(DatumWritActivity.this,"提交成功");
                    break;
                default:
                    Resource res = JsonParse.parseToObject(info, Resource.class);
                    adapter.updateRes(res,requestCode);
                    getUploadResMap().remove(requestCode);
                    mExListview.expandGroup(adapter.getAddGroupPosition(), true);
                    break;
			}
		}

        @Override
        public void onFailure(Context context, ResultInfo info, int requestCode) {
            super.onFailure(context, info, requestCode);
            if(requestCode < 0){
                LogUtils.e("onLoading onFailure:");
                adapter.removeResWithId(requestCode);
                getUploadResMap().remove(requestCode);
            }
        }

        @Override
        public void onLoading(long total, long current, boolean isUploading, int requestCode) {
            int progress = (int)(100*current/total);
            if(getUploadResMap().get(requestCode) != null){
                LogUtils.e("onLoading progress:"+progress);
                getUploadResMap().get(requestCode).setProgress(progress);
                adapter.notifyDataSetChanged();
            }
        }
    };

    private void addUploadResouce( List<ResourceType> data) {

        for(Integer it : getUploadResMap().keySet()){
            Resource res = getUploadResMap().get(it);
            for(ResourceType resType : data){
                if(resType.getId() == res.getResourceTypeId()){
                    resType.getItems().add(res);
                    break;
                }
            }
        }
    }

    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_CANCELED) {
			return;
		}
		switch (requestCode) {
		// 拍照获取图片
		case ImageUtils.GET_IMAGE_BY_CAMERA:
			// uri传入与否影响图片获取方式,以下二选一
			// 方式一,自定义Uri(ImageUtils.imageUriFromCamera),用于保存拍照后图片地址
			if (ImageUtils.imageUriFromCamera != null) {
                String filePath = ImageUtils.getPath(this, ImageUtils.imageUriFromCamera);
                int id = createRes(filePath);

				uploadFile(filePath,id);
			}

			break;
		// 手机相册获取图片
		case ImageUtils.GET_IMAGE_FROM_PHONE:
			if (data != null && data.getData() != null) {
				Uri imgUri = data.getData();
				if (imgUri == null) {
					Bundle extras = data.getExtras();
					String mPhoto = String.valueOf(extras.getParcelable("data"));
					if (mPhoto == null) {
						String filePath = extras.getString("filePath");
						if (!TextUtils.isEmpty(filePath)) {
							imgUri = Uri.fromFile(new File(filePath));
						}
					}
				}
                String filePath = ImageUtils.getPath(this, imgUri);
                int id = createRes(filePath);
				uploadFile(filePath, id);
			}
			break;
		case Constant.FILE_SELECT_CODE:
			if (data != null && data.getData() != null) {
				Uri imgUri = data.getData();
				if (imgUri == null) {
					Bundle extras = data.getExtras();
					String mPhoto = String.valueOf(extras.getParcelable("data"));
					if (mPhoto == null) {
						String filePath = extras.getString("filePath");
						if (!TextUtils.isEmpty(filePath)) {
							imgUri = Uri.fromFile(new File(filePath));
						}
					}
				}
                String filePath = ImageUtils.getPath(this, imgUri);
                int id = createRes(filePath);
                uploadFile(filePath,id);
//				uploadFile(ImageUtils.getAbsoluteImagePath(this, imgUri),0);
			}
			break;
		default:
			break;
		}
	}

    private int createRes(String filePath) {
        Resource res = new Resource();
        uploadId--;
        res.setId(uploadId);
        res.setResourceExtention("jpg");
        res.setUrl(filePath);
        res.setThumbnailUrl(filePath);
        res.setResourceTypeId( adapter.getResourceType().getId());
        getUploadResMap().put(uploadId, res);
        adapter.getResourceType().getItems().add(res);
        adapter.notifyDataSetChanged();
        return uploadId;
    }

    public void uploadFile(final String filePath, final int id) {
		final ResourceType resType = adapter.getResourceType();
		String format = FileUtils.getFileFormat4FilePath(filePath);
        new Thread(){
            @Override
            public void run() {
                final File file = new File(filePath);
                Bitmap bitmap = ImageUtils.getBitmapByFile(file);
                final InputStream in = ImageUtils.compressImageToInputStream(bitmap);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        RequestParams params = RequestParamsUtils.uploadFile(in,resType.getId(), file.getName());
                        params = RequestParamsUtils.addStudentParams(params,getIntent());
                        HttpRequestUtils.create(DatumWritActivity.this).isShowLoadingDilag(false).setProgressType(ProgressType.CIRCLE)
                                .send(URL.UPLOAD, params, id, callback);
                    }
                });
            }
        }.start();


//		RequestParams params = RequestParamsUtils.uploadFile(filePath,
//				resType.getId());

	}

    @Override
    public void onClick(View v) {
        RequestParams params = RequestParamsUtils.getResourceType(type);
        params = RequestParamsUtils.addStudentParams(params,getIntent());
        HttpRequestUtils.create(this).send(URL.SUBMIT_RESOURCE,params, SUBMIT,callback);
    }

    @Override
    public void onBackPressed() {
        if(adapter.isDelete()){
            adapter.setIsDelete(false);
            adapter.notifyDataSetChanged();
        }else{
            super.onBackPressed();
        }
    }
}
