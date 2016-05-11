package com.meten.ifuture.utils;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.meten.ifuture.R;
import com.meten.ifuture.model.DataOfPage;
import com.meten.ifuture.model.ResultInfo;
import com.meten.ifuture.model.teacher.Student;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

public class JsonParse {
	
	public static ResultInfo parseToResultInfo(Context context,String result){
		ResultInfo info = null;
		if(result == null){
			info = new ResultInfo(R.id.serviceError, context.getString(R.id.serviceError));
			return info;
		}
		
		Gson gson = new Gson();
		try{
			info = gson.fromJson(result, ResultInfo.class);
		}catch(Exception e){
			info = new ResultInfo(R.id.dataError, context.getString(R.id.dataError));
		}
		return info;
	}

	public static <T> T parseToObject(ResultInfo resultInfo, Class<T> c) {
		return parseToObject(resultInfo.getData(),c);
	}

    public static <T> T parseToObject(JsonElement element, Class<T> c) {
		T t = null;
		Gson gson = new Gson();
		try {
			t = gson.fromJson(element, c);
		} catch (Exception e) {
            e.printStackTrace();
		}
		return t;
	}

	public static <T> T parseToObject(ResultInfo resultInfo, Type type) {
		return parseToObject(resultInfo.getData(),type);
	}

    public static <T> T parseToObject(JsonElement element, Type type) {
		T t = null;
		Gson gson = new Gson();
		try {
			t = gson.fromJson(element, type);
		} catch (Exception e) {
            e.printStackTrace();
		}
		return t;
	}


    public static <T> T parseDataOfPageToData(ResultInfo resultInfo, Type type) {
        DataOfPage dataOfPage = parseToObject(resultInfo,DataOfPage.class);
        return parseToObject(dataOfPage.getDataList(),type);
    }


    /**
     * 解析已绑定第三方账号的类型数组
     * @param resultInfo
     * @return
     */
    public static int[] parseThirdTypes(ResultInfo resultInfo){
        int[] types = null;
        String data = resultInfo.getData().getAsJsonArray().toString();
        try {
            JSONArray ja = new JSONArray(data);
            if(ja.length() > 0){
                types = new int[ja.length()];
                for(int i = 0; i<ja.length();i++){
                    JSONObject o = ja.getJSONObject(i);
                    int thirdtype = o.getInt("ThridAccountType");
                    types[i] = thirdtype;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return types;
    }
}
