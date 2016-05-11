package com.meten.ifuture.model.teacher;

import com.google.gson.annotations.SerializedName;
import com.meten.ifuture.model.User;

/**
 * Created by Cmad on 2015/3/4.
 * 我的学员item实体类
 */
public class Student extends User{
    /**
     * 合同类型
     */
    @SerializedName("StudentContractTypeName")
    private String contractType;

    public String getContractType() {
        return contractType;
    }

    public void setContractType(String contractType) {
        this.contractType = contractType;
    }
}
