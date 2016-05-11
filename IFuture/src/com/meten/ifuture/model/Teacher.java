package com.meten.ifuture.model;

import java.io.Serializable;

/**
 * Created by Cmad on 2015/3/10.
 * 表扬的实体类
 */
public class Teacher implements Serializable,IResource{
//    "AreaId": 1,
//            "UserId": 2,
//            "CnName": "sample string 3",
//            "EnName": "sample string 4",
//            "TeacherRoleName": "sample string 5",
//            "PraiseNum": 1
    private int AreaId;
    private int UserId;
    private String CnName;
    private String EnName;
    private String TeacherRoleName;
    /**
     * 表扬的数量 or 投诉的数量
     */
    private int Num;
    private String Photo;

    public String getPhoto() {
        return Photo;
    }

    public void setPhoto(String photo) {
        Photo = photo;
    }

    public int getAreaId() {
        return AreaId;
    }

    public void setAreaId(int areaId) {
        AreaId = areaId;
    }

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }

    public String getCnName() {
        return CnName;
    }

    public void setCnName(String cnName) {
        CnName = cnName;
    }

    public String getEnName() {
        return EnName;
    }

    public void setEnName(String enName) {
        EnName = enName;
    }

    public String getTeacherRoleName() {
        return TeacherRoleName;
    }

    public void setTeacherRoleName(String teacherRoleName) {
        TeacherRoleName = teacherRoleName;
    }

    public int getNum() {
        return Num;
    }

    public void setNum(int Num) {
        Num = Num;
    }

    @Override
    public int getId() {
        return UserId;
    }

    public String getName(){
        return EnName+" "+CnName;
    }
}
