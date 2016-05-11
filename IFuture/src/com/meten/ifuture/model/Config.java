package com.meten.ifuture.model;

import com.lidroid.xutils.db.annotation.Id;

/**
 * Created by Cmad on 2015/2/27.
 * 用户设置实体类
 */
public class Config {

    @Id(column="userId")
    private String userId;

    private boolean isPush;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isPush() {
        return isPush;
    }

    public void setPush(boolean isPush) {
        this.isPush = isPush;
    }
}
