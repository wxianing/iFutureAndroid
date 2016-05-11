package com.meten.ifuture.model.student;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Cmad on 2015/3/6.
 * 投诉回复实体类
 */
public class ComplainReply implements Serializable{
//    "Id": 1,
//            "": 2,
//            "Content": "sample string 3",
//            "CreateUserId": 4,
//            "CreateUserCnName": "sample string 5",
//            "CreateTime": "2015-03-06 13:51:38",
//            "Status": 7

    private int Id;
    private int FeedbackId;
    private String Content;

    @SerializedName("CreateTime")
    private String replyDate;

    @SerializedName("CreateUserCnName")
    private String replyUserName;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getFeedbackId() {
        return FeedbackId;
    }

    public void setFeedbackId(int feedbackId) {
        FeedbackId = feedbackId;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getReplyDate() {
        return replyDate;
    }

    public void setReplyDate(String replyDate) {
        this.replyDate = replyDate;
    }

    public String getReplyUserName() {
        return replyUserName;
    }

    public void setReplyUserName(String replyUserName) {
        this.replyUserName = replyUserName;
    }
}
