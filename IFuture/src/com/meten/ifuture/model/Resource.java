package com.meten.ifuture.model;

import com.google.gson.annotations.SerializedName;

/**
 * 资源实体类，我的材料、文书
 */
public class Resource {
	// "Id": 1,
	// "ResourceTypeId": 2,
	// "ResourceName": "sample string 3",
	// "ResourceUrl": "sample string 4",
	// "ResourceFileSize": "5",
	// "ResourceExtention": "sample string 6",
	// "CreateUserId": 7,
	// "CreateUserCnName": "sample string 8",
	// "CreateTime": "2015-02-11 10:57:51",
	// "Status": 10

	private int Id;

	private int ResourceTypeId;

	@SerializedName("ResourceName")
	private String name;

	@SerializedName("ResourceUrl")
	private String url;

    private String ThumbnailUrl;

	private String ResourceFileSize;

	private String ResourceExtention;

    private int progress;

    public String getThumbnailUrl() {
        return ThumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        ThumbnailUrl = thumbnailUrl;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public int getId() {
		return Id;
	}

	public void setId(int id) {
		Id = id;
	}

	public int getResourceTypeId() {
		return ResourceTypeId;
	}

	public void setResourceTypeId(int resourceTypeId) {
		ResourceTypeId = resourceTypeId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getResourceFileSize() {
		return ResourceFileSize;
	}

	public void setResourceFileSize(String resourceFileSize) {
		ResourceFileSize = resourceFileSize;
	}

	public String getResourceExtention() {
		return ResourceExtention;
	}

	public void setResourceExtention(String resourceExtention) {
		ResourceExtention = resourceExtention;
	}


    public void copy(Resource res){
        Id = res.getId();
        name = res.getName();
        ResourceTypeId = res.getResourceTypeId();
//        url = res.getUrl();
        ResourceExtention = res.getResourceExtention();
    }


}
