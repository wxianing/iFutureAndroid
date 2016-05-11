package com.meten.ifuture.push;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.meten.ifuture.AppManager;
import com.meten.ifuture.R;
import com.meten.ifuture.activity.SystemMessageActivity;
import com.meten.ifuture.activity.WebActivity;
import com.meten.ifuture.activity.manager.ComplainActivity;
import com.meten.ifuture.activity.manager.ManagerMainActivity;
import com.meten.ifuture.activity.manager.PraiseActivity;
import com.meten.ifuture.activity.student.MyComplainActivity;
import com.meten.ifuture.activity.student.MyTeacherActivity;
import com.meten.ifuture.activity.student.StudentMianActivity;
import com.meten.ifuture.activity.teacher.MyStudentActivity;
import com.meten.ifuture.activity.teacher.TeacherMianActivity;
import com.meten.ifuture.constant.Constant;
import com.meten.ifuture.constant.PushType;
import com.meten.ifuture.constant.URL;
import com.meten.ifuture.model.PushMessage;
import com.meten.ifuture.model.User;
import com.meten.ifuture.utils.DBHelper;
import com.meten.ifuture.utils.SharedPreferencesUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by Cmad on 2015/3/9.
 * 处理推送消息的工具类
 */
public class PushUtils {
    /**
     * 解析推送消息
     *
     * @param context
     * @param bundle
     * @return
     */
    public static PushMessage parsePushMessage(Context context, Bundle bundle) {
        User user = SharedPreferencesUtils.getInstance(context).getUser();
        int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
        String pushId = bundle.getString(JPushInterface.EXTRA_PUSH_ID);
        if (user == null) {
            return null;
        }
        String extra = bundle.getString(JPushInterface.EXTRA_EXTRA);
        String content = bundle.getString(JPushInterface.EXTRA_ALERT);
        String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
        int pushType = 0;
        int extraId = 0;
        try {
            JSONObject jo = new JSONObject(extra);
            pushType = jo.getInt(Constant.PUSH_TYPE);
            extraId = jo.getInt(Constant.PUSH_EXTRA_ID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        PushMessage msg = new PushMessage(pushId, user.getUserId(), pushType, title, content, extraId, notifactionId);

        try {
            DBHelper.getDBUtils(context).save(msg);
        } catch (DbException e) {
            e.printStackTrace();
        }
        return msg;
    }


    /**
     * 根据推送消息进行页面跳转
     *
     * @param context
     * @param msg     推送消息
     */
    public static void jumpActivity(Context context, PushMessage msg) {
        if (msg == null) {
            return;
        }
        Intent it = new Intent();
        it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        User user = SharedPreferencesUtils.getInstance(context).getUser();

        if(user == null){
            return;
        }

        if (msg.getPushType() == PushType.STUDENT_UNIVERSITY_TATUS.toInt()
                || msg.getPushType() == PushType.STUDENT_FINISH.toInt()
                || msg.getPushType() == PushType.STUDENT_NEW_UNIVERSITY.toInt()) {
            it.setClass(context, WebActivity.class);
            it.putExtra(Constant.TITLE, context.getString(R.string.my_choose_school));
            it.putExtra(Constant.URL, String.format(URL.WEB_CHOOSE_SCHOOL, user.getCode()));

        } else if (msg.getPushType() == PushType.STUDENT_NEW_TEACHER.toInt()) {
            it.setClass(context, MyTeacherActivity.class);

        } else if (msg.getPushType() == PushType.STUDENT_PLAN_UPDATE.toInt()) {
            it.setClass(context, WebActivity.class);
            it.putExtra(Constant.URL, String.format(URL.WEB_MY_PLAN, user.getCode()));
            it.putExtra(Constant.TITLE, context.getString(R.string.my_plan));

        } else if (msg.getPushType() == PushType.STUDENT_NEW_SCORE.toInt()) {
            it.setClass(context, WebActivity.class);
            it.putExtra(Constant.URL, String.format(URL.WEB_MY_SCORE, user.getCode()));
            it.putExtra(Constant.TITLE, context.getString(R.string.my_score));

        } else if (msg.getPushType() == PushType.STUDENT_COMPLAIN.toInt()) {
            it.setClass(context, MyComplainActivity.class);
//            it.putExtra(Constant.COMPLAIN_ID, msg.getExtraId());

        } else if (msg.getPushType() == PushType.TEACHER_NEW_STUDENT.toInt()) {
            it.setClass(context, MyStudentActivity.class);

        } else if (msg.getPushType() == PushType.ADMIN_NEW_STUDENT.toInt()) {
            it.setClass(context, MyStudentActivity.class);

        } else if (msg.getPushType() == PushType.ADMIN_FINISHED_STUDENT.toInt()) {
            it.setClass(context, WebActivity.class);
            it.putExtra(Constant.URL, String.format(URL.WEB_FINISHED_STUDEND, user.getCode()));
            it.putExtra(Constant.TITLE, context.getString(R.string.past_student));

        } else if (msg.getPushType() == PushType.ADMIN_NEW_COMPLAIN.toInt()) {
            it.setClass(context, ComplainActivity.class);

        } else if (msg.getPushType() == PushType.ADMIN_NEW_PRAISE.toInt()) {
            it.setClass(context, PraiseActivity.class);

        } else if (msg.getPushType() == PushType.ADMIN_NEW_NEWS.toInt()) {
            it.setClass(context, WebActivity.class);
            it.putExtra(Constant.URL, URL.WEB_STUDY_ABROAD_INFO);
            it.putExtra(Constant.TITLE, context.getString(R.string.study_abroad_info));

        } else if (msg.getPushType() == PushType.SYSTEM_PUSH.toInt()) {
            it.setClass(context, WebActivity.class);
            it.putExtra(Constant.URL, String.format(URL.WEB_NEWS_DETAIL + msg.getExtraId(), user.getCode()));
            it.putExtra(Constant.TITLE, msg.getContent());
        } else {
            //设置页面不跳转
            if (context instanceof SystemMessageActivity) {
                return;
            }
            if (user.getUserType() == Constant.TEACHER) {
                if (AppManager.getAppManager().currentActivity() instanceof TeacherMianActivity) {
                    return;
                }
                it.setClass(context, TeacherMianActivity.class);
            } else if (user.getUserType() == Constant.STUDENT) {
                if (AppManager.getAppManager().currentActivity() instanceof StudentMianActivity) {
                    return;
                }
                it.setClass(context, StudentMianActivity.class);
            } else if (user.getUserType() == Constant.MANAGER) {
                if (AppManager.getAppManager().currentActivity() instanceof ManagerMainActivity) {
                    return;
                }
                it.setClass(context, ManagerMainActivity.class);
            }
        }
        context.startActivity(it);
        //如果当前是在系统消息页面则不更新消息的已读、未读状态
        if (context instanceof SystemMessageActivity) {
            return;
        }
        msg.setRead(true);
        try {
            DBHelper.getDBUtils(context).update(msg, PushMessage.IS_READ);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新所有未读消息为已读
     *
     * @param context
     * @param userId  用户id
     */
    public static void updateAllPushMessage(Context context, int userId) {
        try {
            DbUtils du = DBHelper.getDBUtils(context);
            List<PushMessage> msgs = du.findAll(Selector
                    .from(PushMessage.class).where(PushMessage.USER_ID, "=",
                            userId).and(PushMessage.IS_READ, "=", false));
            if (msgs == null) {
                return;
            }
            for (PushMessage msg : msgs) {
                msg.setRead(true);
                JPushInterface.clearNotificationById(context, msg.getNotificationId());
            }

            du.updateAll(msgs, PushMessage.IS_READ);
            Intent it = new Intent(Constant.ACTION_REFRESH_MESSAGE_COUNT);
            context.sendBroadcast(it);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取未读系统消息条数
     *
     * @param context
     * @param userId  用户id
     * @return
     */
    public static long getUnReadSystemMsgCount(Context context, int userId) {
        DbUtils du = DBHelper.getDBUtils(context);
        long count = 0;
        try {
            count =
                    du.count(Selector.from(PushMessage.class).where(PushMessage.USER_ID,
                            "=", userId).and(PushMessage.IS_READ, "=", false).and(PushMessage.PUSH_TYPE, "=", PushType.SYSTEM_PUSH.toInt()));
        } catch (DbException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return count;
    }


    /**
     * 根据消息类型获取该类型的未读消息条数
     *
     * @param pushType 消息类型
     * @return
     */
    public static long getNewMessageCountWithPushType(Context context, int userId, int pushType) {
        DbUtils du = DBHelper.getDBUtils(context);
        long count = 0;
        try {
            count =
                    du.count(Selector.from(PushMessage.class).where(PushMessage.USER_ID,
                            "=", userId).and(PushMessage.IS_READ, "=", false).and(PushMessage.PUSH_TYPE, "=", pushType));

        } catch (DbException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return count;
    }


    /**
     * 更新未读的消息为已读
     *
     * @param context
     * @param userId   用户id
     * @param pushType 推送消息类型
     */
    public static void updateMsgWithPushType(Context context, int userId, int pushType) {
        DbUtils du = DBHelper.getDBUtils(context);
        try {
            List<PushMessage> data = du.findAll(Selector.from(PushMessage.class).where(PushMessage.USER_ID,
                    "=", userId).and(PushMessage.IS_READ, "=", false).and(PushMessage.PUSH_TYPE, "=", pushType));
            for (PushMessage pm : data) {
                pm.setRead(true);
                JPushInterface.clearNotificationById(context, pm.getNotificationId());
            }

            du.updateAll(data, PushMessage.IS_READ);
        } catch (DbException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
}
