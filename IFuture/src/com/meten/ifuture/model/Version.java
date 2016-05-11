package com.meten.ifuture.model;

/**
 * Created by Administrator on 2015/2/27.
 * 版本信息实体类
 */
public class Version {

    private String VersionCode;
    private String VersionName;
    private String FilePath;
    private String FileSize;

    public String getVersionCode() {
        return VersionCode;
    }

    public void setVersionCode(String versionCode) {
        VersionCode = versionCode;
    }

    public String getVersionName() {
        return VersionName;
    }

    public void setVersionName(String versionName) {
        VersionName = versionName;
    }

    public String getFilePath() {
        return FilePath;
    }

    public void setFilePath(String filePath) {
        FilePath = filePath;
    }

    public String getFileSize() {
        return FileSize;
    }

    public void setFileSize(String fileSize) {
        FileSize = fileSize;
    }
}
