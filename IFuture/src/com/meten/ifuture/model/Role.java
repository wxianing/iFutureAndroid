package com.meten.ifuture.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Cmad on 2015/3/10.
 */
public class Role extends Area {

    @SerializedName("RoleName")
    private String name;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }
}
