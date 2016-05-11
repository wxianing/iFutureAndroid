package com.meten.ifuture.model.student;

import java.io.Serializable;

/**
 * Created by Cmad on 2015/3/6.
 * 投诉的实体类
 */
public class Complain implements Serializable{
//    "Id": 1,
//            "FromUserId": 2,
//            "FromCnName": "sample string 3",
//            "CreateTime": "2015-03-06 13:51:38",
//            "ToUserId": 5,
//            "ToCnName": "sample string 6",
//            "Title": "sample string 7",
//            "Content": "sample string 8",
//            "Type": 9,
//            "UpdateUserId": 10,
//            "UpdateUserCnName": "sample string 11",
//            "UpdateTime": "2015-03-06 13:51:38",
//            "Status": 13,
//            "ListReply": [

    private int Id;
    private String CreateTime;
    private int FromUserId;
    private int ToUserId;
    private String FromCnName;
    private String ToCnName;
    private String Content;
    private int Status;
    private String StatusText;
    private String FromUserPhoto;
    private String ToRoleName;
//    private List<ComplainReply> ListReply;


    public String getToRoleName() {
        return ToRoleName;
    }

    public void setToRoleName(String toRoleName) {
        ToRoleName = toRoleName;
    }

    public String getFromUserPhoto() {
        return FromUserPhoto;
    }

    public void setFromUserPhoto(String fromUserPhoto) {
        FromUserPhoto = fromUserPhoto;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public String getStatusText() {
        return StatusText;
    }

    public void setStatusText(String statusText) {
        StatusText = statusText;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String createTime) {
        CreateTime = createTime;
    }

    public int getToUserId() {
        return ToUserId;
    }

    public int getFromUserId() {
        return FromUserId;
    }

    public void setFromUserId(int fromUserId) {
        FromUserId = fromUserId;
    }

    public String getFromCnName() {
        return FromCnName;
    }

    public void setFromCnName(String fromCnName) {
        FromCnName = fromCnName;
    }

    public void setToUserId(int toUserId) {
        ToUserId = toUserId;
    }

    public String getToCnName() {
        return ToCnName;
    }

    public void setToCnName(String toCnName) {
        ToCnName = toCnName;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

//    public List<ComplainReply> getListReply() {
//        return ListReply;
//    }
//
//    public void setListReply(List<ComplainReply> listReply) {
//        ListReply = listReply;
//    }
}
