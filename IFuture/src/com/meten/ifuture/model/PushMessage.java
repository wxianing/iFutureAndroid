package com.meten.ifuture.model;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by Cmad on 2015/3/6.
 */
public class PushMessage {

    public static final String USER_ID = "userId";
    public static final String IS_READ = "isRead";
    public static final String PUSH_TYPE = "pushType";


    @SerializedName("Id")
    private String id;

    private int userId;

    @SerializedName("BusinessType")
    private int pushType;

    @SerializedName("Title")
    private String title;

    @SerializedName("Content")
    private String content;

    /**
     * 附加id
     */
    private int extraId;

    private Date date;

    @SerializedName("CreateTime")
    private String time;

    private boolean isRead;

    private int notificationId;

    @SerializedName("Data")
    private String data;

    public PushMessage(){};

    public PushMessage(String id,int userId,int pushType,String title, String content,int extraId,int notificationId){
        this.id = id;
        this.userId = userId;
        this.pushType = pushType;
        this.title = title;
        this.content = content;
        this.extraId = extraId;
        this.notificationId = notificationId;
        this.date = new Date();
    }


    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(int notificationId) {
        this.notificationId = notificationId;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean isRead) {
        this.isRead = isRead;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getExtraId() {
        if(extraId <= 0 && !TextUtils.isEmpty(data)){
            try {
                JSONObject jsonObject = new JSONObject(data);
                extraId = jsonObject.getInt("Id");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return extraId;
    }

    public void setExtraId(int extraId) {
        this.extraId = extraId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getPushType() {
        return pushType;
    }

    public void setPushType(int pushType) {
        this.pushType = pushType;
    }
}
