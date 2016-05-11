package com.meten.ifuture.http.task;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.meten.ifuture.AppManager;
import com.meten.ifuture.R;
import com.meten.ifuture.constant.Constant;
import com.meten.ifuture.constant.URL;
import com.meten.ifuture.http.HttpRequestCallBack;
import com.meten.ifuture.http.HttpRequestUtils;
import com.meten.ifuture.http.RequestParamsUtils;
import com.meten.ifuture.model.ResultInfo;
import com.meten.ifuture.model.Version;
import com.meten.ifuture.utils.JsonParse;
import com.meten.ifuture.utils.ToastUtils;

import java.io.File;

/**
 * Created by Administrator on 2015/2/27.
 */
public class HttpTask {

    public static void detectionNewAppVersion(final Context context,final boolean isUpdate, final boolean showLoading) {
        RequestParams params = RequestParamsUtils.getNewAppVersion();
        HttpRequestUtils.create(context).isShowLoadingDilag(showLoading).send(URL.GET_NEW_VERSION, params, new HttpRequestCallBack<ResultInfo>() {
            @Override
            public void onSuccess(ResultInfo resultInfo, int requestCode) {
                final Version v = JsonParse.parseToObject(resultInfo, Version.class);
                if (v == null) {
                    return;
                }
                int versionCode = 0;
                try {
                    versionCode = Integer.parseInt(v.getVersionCode());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (versionCode > AppManager.getAppVersionCode(context)) {
                    if (isUpdate) {
                        new AlertDialog.Builder(context).setTitle("版本升级")
                                .setMessage("检测到有新版本，是否升级？")
                                .setPositiveButton("是", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        updateApp(context, v);
                                    }
                                }).setNegativeButton("否", null).create().show();
                    } else {
                        //发送广播刷新设置界面的新版本显示
                        Intent intent = new Intent(Constant.ACTION_NEW_VERSION);
                        intent.putExtra(Constant.VERSION_NAME, v.getVersionName());
                        context.sendBroadcast(intent);
                    }

                }else{
                    if(showLoading){
                        ToastUtils.show(context,"已是最新版本");
                    }
                }
            }
        });
    }


    public static void updateApp(final Context context, Version app) {
        HttpUtils http = new HttpUtils();
        final String appName = context.getString(R.string.app_name);
        final String savePath = Environment.getExternalStorageDirectory()
                .toString()
                + File.separator
                + appName
                + File.separator
                + appName
                + app.getVersionName() + ".apk";

        http.download(app.getFilePath(), savePath, new RequestCallBack<File>() {
            private NotificationManager updateNotificationManager;
            private Notification updateNotification;
            private NotificationCompat.Builder mBuilder;

            @Override
            public void onStart() {
                // 创建文件
                updateNotificationManager = (NotificationManager) context
                        .getSystemService(Context.NOTIFICATION_SERVICE);
                mBuilder = new NotificationCompat.Builder(context);
                mBuilder.setContentTitle("正在更新" + appName);
                mBuilder.setContentText("0%");
                mBuilder.setProgress(100,0,false);
                mBuilder.setSmallIcon(android.R.drawable.stat_sys_download);
                updateNotification = mBuilder.build();
                updateNotification.flags = Notification.FLAG_AUTO_CANCEL;
                updateNotificationManager.notify(101, updateNotification);
            }

            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                mBuilder.setContentText((int) current * 100 / total + "%");
                mBuilder.setProgress(100, (int) ( current * 100 / total),false);
                updateNotification = mBuilder.build();
                updateNotificationManager.notify(101, updateNotification);
            }

            @Override
            public void onSuccess(ResponseInfo<File> responseInfo) {
                updateNotificationManager.cancel(101);
                Uri uri = Uri.fromFile(new File(savePath));
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(uri,
                        "application/vnd.android.package-archive");
                context.startActivity(intent);
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                mBuilder.setContentText("下载失败.请重新下载！");
                updateNotification = mBuilder.build();
                updateNotificationManager.notify(101, updateNotification);

            }
        });
    }


}
