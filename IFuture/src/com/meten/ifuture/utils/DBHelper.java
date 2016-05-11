package com.meten.ifuture.utils;

import android.content.Context;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;
import com.meten.ifuture.model.PushMessage;

import java.util.List;

/**
 * Created by Cmad on 2015/4/9.
 */
public class DBHelper {
    public static DbUtils getDBUtils(Context context){

        DbUtils dbUtils = DbUtils.create(context, "xUtils.db", 3, new DbUtils.DbUpgradeListener() {
            @Override
            public void onUpgrade(DbUtils db, int oldVersion, int newVersion) {
                LogUtils.e("---------db update--------");
                /**
                 * 添加notificationId用于点击消息的时候清除通知栏消息 2015年4月22日15:33:29
                 * pushmsg里添加了time字段
                 */
                try {
                    List<PushMessage> data = db.findAll(PushMessage.class);
                    db.dropTable(PushMessage.class);
                    db.saveAll(data);
                } catch (DbException e) {
                    e.printStackTrace();
                }
            }
        });
        return dbUtils;
    }

}
