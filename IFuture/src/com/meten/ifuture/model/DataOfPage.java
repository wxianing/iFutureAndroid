package com.meten.ifuture.model;

import com.google.gson.JsonElement;

/**
 * Created by Cmad on 2015/3/6.
 */
public class DataOfPage {

    /**
     * 页数
     */
    private int PageIndex;
    /**
     * 所有学员总数
     */
    private int RecordCount;
    /**
     * 当前页数据列表
     */
    private JsonElement DataList;


    public int getPageIndex() {
        return PageIndex;
    }

    public void setPageIndex(int pageIndex) {
        PageIndex = pageIndex;
    }

    public int getRecordCount() {
        return RecordCount;
    }

    public void setRecordCount(int recordCount) {
        RecordCount = recordCount;
    }

    public JsonElement getDataList() {
        return DataList;
    }

    public void setDataList(JsonElement dataList) {
        DataList = dataList;
    }
}
