package com.meten.ifuture.model;

import com.google.gson.annotations.SerializedName;
import com.meten.ifuture.utils.StringUtils;

import java.io.Serializable;

/**
 * 用户基本类
 */
public class BaseUser implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int UserId;
	private String UserName;
	private String CnName;
	private String EnName;
	private String Mobile;
	private String Photo;
	private String QQ;
	@SerializedName("MicroMessage")
	private String wechat;
	private String Email;

    private String[] Tags;

    public String[] getTags() {
        return Tags;
    }

    public void setTags(String[] tags) {
        Tags = tags;
    }

    public int getUserId() {
		return UserId;
	}

	public void setUserId(int userId) {
		UserId = userId;
	}

	public String getUserName() {
		return StringUtils.stringOrEmpty(UserName);
	}

	public void setUserName(String userName) {
		UserName = userName;
	}

	public String getCnName() {
		return StringUtils.stringOrEmpty(CnName);
	}

	public void setCnName(String cnName) {
		CnName = cnName;
	}

	public String getEnName() {
		return StringUtils.stringOrEmpty(EnName);
	}

	public void setEnName(String enName) {
		EnName = enName;
	}

	public String getMobile() {
		return StringUtils.stringOrEmpty(Mobile);
	}

	public void setMobile(String mobile) {
		Mobile = mobile;
	}

	public String getPhoto() {
		return StringUtils.stringOrEmpty(Photo);
	}

	public void setPhoto(String photo) {
		Photo = photo;
	}

	public String getQQ() {
		return StringUtils.stringOrEmpty(QQ);
	}

	public void setQQ(String qQ) {
		QQ = qQ;
	}

	public String getWechat() {
		return StringUtils.stringOrEmpty(wechat);
	}

	public void setWechat(String wechat) {
		this.wechat = wechat;
	}

	public String getEmail() {
		return StringUtils.stringOrEmpty(Email);
	}

	public void setEmail(String email) {
		Email = email;
	}

	public String getName() {
		return StringUtils.stringOrEmpty(EnName + " " + CnName);
	}
}
