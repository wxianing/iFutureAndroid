package com.meten.ifuture.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Cmad on 2015/3/9.
 * 区域的实体类
 */
public class Area implements IResource{
    private int Id;

    @SerializedName("AreaName")
    private String name;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
