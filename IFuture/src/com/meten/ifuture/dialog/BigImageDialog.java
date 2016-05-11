package com.meten.ifuture.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;
import com.lidroid.xutils.bitmap.callback.DefaultBitmapLoadCallBack;
import com.lidroid.xutils.http.RequestParams;
import com.meten.ifuture.R;
import com.meten.ifuture.constant.Constant;
import com.meten.ifuture.constant.URL;
import com.meten.ifuture.http.HttpRequestCallBack;
import com.meten.ifuture.http.HttpRequestUtils;
import com.meten.ifuture.http.RequestParamsUtils;
import com.meten.ifuture.model.Resource;
import com.meten.ifuture.model.ResultInfo;
import com.meten.ifuture.utils.ImageUtils;
import com.meten.ifuture.utils.ToastUtils;
import com.meten.ifuture.view.ProgressWheel;
import com.meten.ifuture.view.ZoomImageView;

public class BigImageDialog extends Dialog {
    private ZoomImageView image;
    private ProgressWheel loading;
    private ImageView ivDetele;
    private ImageView ivSave;
    private int resId;
    private HttpRequestCallBack<ResultInfo> callBack;
    private Bitmap mBitmap;

    public BigImageDialog(final Context context, final Resource res, final boolean isPastStudent) {
        super(context, R.style.myDialogTheme);
        setContentView(R.layout.big_image_dialog);
        resId = res.getId();

        Window dialogWindow = getWindow();
        dialogWindow.setWindowAnimations(R.style.dialogWindowAnim);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = LayoutParams.MATCH_PARENT;
        lp.height = LayoutParams.MATCH_PARENT;
        dialogWindow.setAttributes(lp);
        image = (ZoomImageView) findViewById(R.id.big_image);
        loading = (ProgressWheel) findViewById(R.id.progress_wheel);
        ivDetele = (ImageView) findViewById(R.id.delete_iv);
        ivSave = (ImageView) findViewById(R.id.save_iv);


        final BitmapUtils bu = new BitmapUtils(context);
        bu.display(image, res.getUrl(), new DefaultBitmapLoadCallBack<ZoomImageView>() {
            @Override
            public void onLoadFailed(ZoomImageView container, String uri, Drawable drawable) {
                super.onLoadFailed(container, uri, drawable);
                loading.setVisibility(View.GONE);
                ToastUtils.show(getContext(), "加载失败");
                dismiss();
            }

            @Override
            public void onLoadCompleted(ZoomImageView container, String uri, Bitmap bitmap, BitmapDisplayConfig config, BitmapLoadFrom from) {
                super.onLoadCompleted(container, uri, bitmap, config, from);
                mBitmap = bitmap;
                loading.setVisibility(View.GONE);
                if(isPastStudent){
                    ivDetele.setVisibility(View.GONE);
                }else{
                    ivDetele.setVisibility(View.VISIBLE);
                }
                ivSave.setVisibility(View.VISIBLE);
            }
        });

        ivDetele.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(getContext()).setTitle("提示")
                        .setMessage("确认删除？")
                        .setPositiveButton("确认", new AlertDialog.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                RequestParams params = RequestParamsUtils.deleteResource(resId);
                                params = RequestParamsUtils.addStudentParams(params,((Activity)context).getIntent());
                                HttpRequestUtils.create(getContext()).send(URL.DELETE_RESOURCE,
                                        params, resId, callBack);
                                dismiss();
                            }
                        }).setNegativeButton("取消", null).show();

            }
        });

        ivSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(ImageUtils.saveFile(mBitmap, Constant.FILE_DIR,res.getName())){
                   ToastUtils.show(getContext(),"图片成功保存到"+Constant.FILE_DIR+"目录");
               }else{
                   ToastUtils.show(getContext(),"保存失败");
               }
            }
        });

        image.setOnClickListener(new android.view.View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }


    public void setHttpCallBack(HttpRequestCallBack<ResultInfo> callBack){
        this.callBack = callBack;
    }


}
