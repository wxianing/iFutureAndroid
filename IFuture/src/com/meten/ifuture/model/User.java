package com.meten.ifuture.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * 用户实体类
 */
public class User extends BaseUser implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String Code;
	private int AreaId;
	private int SchoolId;
	private int UserType;
	private int ParentType;
	private String Parent;
	private String ParentMobile;
	private String Address;
	private String Remark;
	private int Status;
	private String AreaName;
	@SerializedName("SchoolName")
	private String centerName;

	public String getAreaName() {
		return AreaName;
	}

	public void setAreaName(String areaName) {
		AreaName = areaName;
	}

	public String getCenterName() {
		return centerName;
	}

	public void setCenterName(String centerName) {
		this.centerName = centerName;
	}

	public String getCode() {
		return Code;
	}

	public void setCode(String code) {
		Code = code;
	}

	public int getAreaId() {
		return AreaId;
	}

	public void setAreaId(int areaId) {
		AreaId = areaId;
	}

	public int getSchoolId() {
		return SchoolId;
	}

	public void setSchoolId(int schoolId) {
		SchoolId = schoolId;
	}

	public int getUserType() {
		return UserType;
	}

	public void setUserType(int userType) {
		UserType = userType;
	}

	public int getParentType() {
		return ParentType;
	}

	public void setParentType(int parentType) {
		ParentType = parentType;
	}

	public String getParent() {
		return Parent;
	}

	public void setParent(String parent) {
		Parent = parent;
	}

	public String getParentMobile() {
		return ParentMobile;
	}

	public void setParentMobile(String parentMobile) {
		ParentMobile = parentMobile;
	}

	public String getAddress() {
		return Address;
	}

	public void setAddress(String address) {
		Address = address;
	}

	public String getRemark() {
		return Remark;
	}

	public void setRemark(String remark) {
		Remark = remark;
	}

	public int getStatus() {
		return Status;
	}

	public void setStatus(int status) {
		Status = status;
	}

}
