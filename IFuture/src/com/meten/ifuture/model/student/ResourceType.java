package com.meten.ifuture.model.student;

import com.google.gson.annotations.SerializedName;
import com.meten.ifuture.model.Resource;

import java.util.ArrayList;
import java.util.List;

/**
 * 资源分类列表的实体类
 */
public class ResourceType {
	// "Id": 1,
	// "ParentId": 2,
	// "TypeName": "sample string 3",
	// "TypeLevel": 4,
	// "SortNo": 5,
	// "CreateUserId": 6,
	// "CreateUserCnName": "sample string 7",
	// "CreateTime": "2015-02-11 10:57:51",
	// "Status": 9,
	// "ListResource":

	private int Id;
	private int ParentId;
	@SerializedName("TypeName")
	private String name;

	@SerializedName("TypeLevel")
	private int level;

	@SerializedName("ListResource")
	private List<Resource> items;

	public int getId() {
		return Id;
	}

	public void setId(int id) {
		Id = id;
	}

	public int getParentId() {
		return ParentId;
	}

	public void setParentId(int parentId) {
		ParentId = parentId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public List<Resource> getItems() {
		if(items == null){
			items = new ArrayList<Resource>();
		}
		return items;
	}

	public void setItems(List<Resource> items) {
		this.items = items;
	}

}
