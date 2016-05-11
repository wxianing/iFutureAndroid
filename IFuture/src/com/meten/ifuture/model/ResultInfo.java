package com.meten.ifuture.model;

import com.google.gson.JsonElement;

/**
 * 访问后台接口返回的数据实体类
 */
public class ResultInfo {

	private int code;

	private String msg;

	private JsonElement data;

	public ResultInfo(int code,String msg) {
		this.code = code;
		this.msg = msg;
	}
	public ResultInfo(int code) {
		this.code = code;
	}

	public ResultInfo() {
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public JsonElement getData() {
		return data;
	}

	public void setData(JsonElement data) {
		this.data = data;
	}

}
