package com.meten.ifuture.http;

import android.content.Intent;
import android.graphics.Bitmap;
import android.text.TextUtils;

import com.lidroid.xutils.http.RequestParams;
import com.meten.ifuture.constant.Constant;
import com.meten.ifuture.utils.ImageUtils;
import com.meten.ifuture.utils.LogUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class RequestParamsUtils {
    private static Map<String,Integer> pageIndexMap = new HashMap<String,Integer>();
    private static int pageSize = 20;

	public static RequestParams createRequestParams() {
		RequestParams params = new RequestParams();
		params.addHeader("_appId", Constant.APPID);
		params.addHeader("_code", Constant.getRequestCode());
		return params;
	}

    /**
     * 生成分页的请求参数
     * @param key key
     * @param isLoadMore 是否加载更多
     * @return
     */
    public static RequestParams pageRequestParams(String key, boolean isLoadMore){
        RequestParams params = createRequestParams();
        Integer v = pageIndexMap.get(key);
        int pageIndex = 1;
        if(v == null || !isLoadMore){
            pageIndexMap.put(key,pageIndex);
        }else{
            pageIndex = v.intValue();
            pageIndex++;
            pageIndexMap.put(key,pageIndex);
        }
        params.addBodyParameter("PageIndex",pageIndex+"");
        params.addBodyParameter("PageSize",pageSize+"");
        return params;
    }


    public static RequestParams addStudentParams(RequestParams params,Intent intent){
        int stuId = intent.getIntExtra(Constant.STUDENT_ID,-1);
        String stuName = intent.getStringExtra(Constant.STUDENT_NAME);
        if(stuId > 0 && !TextUtils.isEmpty(stuName)){
            params.addBodyParameter(Constant.STUDENT_ID,stuId+"");
            params.addBodyParameter(Constant.STUDENT_NAME,stuName);
        }
        return params;
    }


    /**
     * 获取用户消息列表
     * @param key
     * @param isLoadMore
     * @return
     */
    public static RequestParams getSystemMsg(String key, String type,boolean isLoadMore){
        RequestParams params = pageRequestParams(key,isLoadMore);
        params.addBodyParameter("Type",type);
        return params;
    }





	/**
	 * 登录
	 * 
	 * @param username
	 *            用户名
	 * @param password
	 *            密码
	 * @return 请求参数对象
	 */
	public static RequestParams login(String username, String password) {
		RequestParams params = createRequestParams();
		params.addBodyParameter("UserName", username);
		params.addBodyParameter("Password", password);
		return params;
	}

	/**
	 * 修改个人信息
	 * 
	 * @param key
	 *            修改的字段
	 * @param values
	 *            值
	 * @return
	 */
	public static RequestParams modifUserInfo(String key, String values) {
		RequestParams params = createRequestParams();
		params.addBodyParameter(key, values);
		return params;
	}

	/**
	 * 上传文件
	 * 
	 * @param filepath
	 *            本地文件地址
	 * @return
	 */
	public static RequestParams uploadBitmap(String filepath) {
		RequestParams params = createRequestParams();
		params.addBodyParameter("ResourceType", "1");

		Bitmap bitmap = ImageUtils.getSmallBitmap(filepath);
		params.addBodyParameter("file_upload",
				ImageUtils.getInputStream(bitmap), bitmap.getByteCount(),
				"head.jpeg");
		return params;
	}

	/**
	 * 上传文件
	 * 
	 * @param file
	 *            文件
	 * @return
	 */
	public static RequestParams uploadFile(File file, int resourceType,
			String fileName) {
		RequestParams params = createRequestParams();
		params.addBodyParameter("ResourceType", resourceType + "");
		params.addBodyParameter("file_upload", file, fileName, "application/octet-stream", null);
		// params.addBodyParameter("file_upload", file);
		return params;
	}

	public static RequestParams uploadFile(File file, int resourceType) {
		RequestParams params = createRequestParams();
		params.addBodyParameter("ResourceType", resourceType + "");
		params.addBodyParameter("file_upload", file);
		// params.addBodyParameter("file_upload", file);
		return params;
	}
    public static RequestParams uploadFile(InputStream in, int resourceType,String filename) {
		RequestParams params = createRequestParams();
		params.addBodyParameter("ResourceType", resourceType + "");
        try {
            params.addBodyParameter("file_upload", in,in.available(),filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // params.addBodyParameter("file_upload", file);
		return params;
	}

	public static RequestParams uploadFile(String filePath, int resourceType) {
		
		return uploadFile(new File(filePath), resourceType);
	}

	public static RequestParams uploadFile(String filePath, int resourceType,
			String fileName) {
		return uploadFile(new File(filePath), resourceType, fileName);
	}

	/**
	 * 接受或拒绝选校
	 * 
	 * @param StudentUniversityId
	 * @param isAccept
	 *            是否同意
	 * @return
	 */
	public static RequestParams handChooseShool(int StudentUniversityId,
			boolean isAccept) {
		RequestParams params = createRequestParams();
		params.addBodyParameter("StudentUniversityId", StudentUniversityId + "");
		params.addBodyParameter("AcceptFlag", isAccept + "");
		return params;
	}

	/**
	 * 获取资源列表
	 * 
	 * @param resourceType
	 *            资源类型id
	 * @return
	 */
	public static RequestParams getResourceType(int resourceType) {
		RequestParams params = createRequestParams();
		params.addBodyParameter("ResourceType", resourceType + "");
		return params;
	}
	/**
	 * 删除资源
	 * 
	 * @param resId 资源id
	 *          
	 * @return
	 */
	public static RequestParams deleteResource(int resId) {
		RequestParams params = createRequestParams();
		params.addBodyParameter("Id", resId + "");
		return params;
	}
	
	/**
	 * 发送手机号码获取验证码
	 * @param type 获取验证码类型
	 * @param mobile 手机号码    
	 * @return
	 */
	public static RequestParams getValidateCode(int type,String mobile) {
		RequestParams params = createRequestParams();
		params.addBodyParameter("CodeType", type + "");
		params.addBodyParameter("Mobile", mobile);
		return params;
	}
	/**
	 * 重置密码
	 * @param mobile 手机号码    
	 * @param code 验证码
	 * @param newPwd 新密码
	 * @return
	 */
	public static RequestParams resetPassword(String mobile,String code,String newPwd) {
		RequestParams params = createRequestParams();
		params.addBodyParameter("Mobile", mobile);
		params.addBodyParameter("Code", code);
		params.addBodyParameter("NewPassword", newPwd);
		return params;
	}

    /**
     * 检测最新版本信息
     * @return
     */
    public static RequestParams getNewAppVersion(){
        RequestParams params = createRequestParams();
        params.addBodyParameter("AppId", Constant.APPID);
        return params;
    }

    /**
     * 修改密码
     * @param oldPwd 旧密码
     * @param newPwd 新密码
     * @return
     */
    public static RequestParams changePassword(String oldPwd, String newPwd){
        RequestParams params = createRequestParams();
        params.addBodyParameter("OldPassword",oldPwd);
        params.addBodyParameter("NewPassword",newPwd);
        return params;
    }

    /**
     * 第三方账号登录
     * @param accountId 第三方账号userid
     * @param accountType 第三方账号类型
     * @return
     */
    public static RequestParams loginByThird(String accountId, int accountType){
        RequestParams params = createRequestParams();
        params.addBodyParameter("AccountId",accountId);
        params.addBodyParameter("AccountType",accountType+"");
        return params;
    }

    /**
     * 第三方账号绑定
     * @param code 验证码
     * @param mobile 手机号码
     * @param thirdType 第三方账号类型
     * @param thirdUserId 第三方用户id
     * @param nikName 第三方昵称
     * @param picUrl 第三方头像地址
     * @return
     */
    public static RequestParams bindThirdUser(String code, String mobile,int thirdType,String thirdUserId,String nikName,String picUrl){
        RequestParams params = createRequestParams();
        params.addBodyParameter("Code",code);
        params.addBodyParameter("Mobile",mobile);
        params.addBodyParameter("ThridAccountType",thirdType+"");
        params.addBodyParameter("ThridAccountId",thirdUserId);
        params.addBodyParameter("ThridAccountNickName",nikName);
        params.addBodyParameter("ThridAccountPicUrl",picUrl);
        return params;
    }

    /**
     * 解除第三方绑定
     * @param thirdType 第三方账号类型
     * @return
     */
    public static RequestParams unbindThirdUser(int thirdType){
        RequestParams params = createRequestParams();
        params.addBodyParameter("ThridAccountType",thirdType+"");
        return params;
    }

    /**
     * 系统反馈
     * @param content 反馈类容
     * @param versionCode 版本号
     * @return
     */
    public static RequestParams feedback(String content,int versionCode){
        RequestParams params = createRequestParams();
        params.addBodyParameter("ClientAppId",Constant.APPID); //客户端类型
        params.addBodyParameter("ClientAppVersion", versionCode+"");
        params.addBodyParameter("Content", content);
        return params;
    }

    /**
     * 获取我的学员
     * @param areaId 区域id
     * @param keyword 搜索关键字
     * @param isLoadMore 是否加载更多
     * @return
     */
    public static RequestParams getMyStudent(int areaId, int teacherId,String keyword, boolean isLoadMore){
        String methodName = new Throwable().getStackTrace()[1].getMethodName();
        LogUtils.e("MethodName:"+methodName);
        RequestParams params = pageRequestParams(methodName,isLoadMore);
        params.addBodyParameter("AreaId",areaId+"");
        params.addBodyParameter("Keyword",keyword);
        params.addBodyParameter("TeacherId", teacherId+"");

        return params;
    }

    /**
     * 添加投诉
     * @param toUserId 被投诉人userid
     * @param toCnName 被投诉人中文名
     * @param content 投诉内容
     * @return
     */
    public static  RequestParams addComplainOrPraise(int toUserId, String toCnName, String content){
        RequestParams params = createRequestParams();
        params.addBodyParameter("ToUserId",toUserId+"");
        params.addBodyParameter("ToCnName",toCnName);
        params.addBodyParameter("Content",content);
        return params;
    }

    /**
     * 根据投诉id获取投诉详情
     * @param id 投诉id
     * @return
     */
    public static RequestParams getComplainDetails(int id){
        RequestParams params = createRequestParams();
        params.addBodyParameter("Id",id+"");
        return params;
    }

    /**
     * 获取表扬列表
     * @param areaiId 区域id
     * @param roleId 身份id
     * @return
     */
    public static RequestParams getPraiseList(int areaiId,int roleId, boolean isLoadMore){
        String methodName = new Throwable().getStackTrace()[1].getMethodName();
        RequestParams params = pageRequestParams(methodName,isLoadMore);
        params.addBodyParameter("AreaId",areaiId+"");
        params.addBodyParameter("RoleId",roleId+"");
        return params;
    }

    /**
     * 获取老师的投诉列表
     * @param key 用于分页识别的key
     * @param id 教师id
     * @param isLoadMore 是否加载更多
     * @return
     */
    public static RequestParams getTeacherComplainList(String key,int id, int status,boolean isLoadMore){
        RequestParams params = pageRequestParams(key,isLoadMore);
        params.addBodyParameter("TeacherUserId",id+"");
        params.addBodyParameter("Status",status+"");
        return params;
    }

    /**
     * 确认处理投诉
     * @param id 投诉id
     * @return
     */
    public static RequestParams handComplain(int id){
        RequestParams params = createRequestParams();
        params.addBodyParameter("Id",id+"");
        return params;
    }

    /**
     * 获取老师表扬详情
     * @param teacherId 老师id
     * @return
     */
    public static RequestParams getPraiseDetails(String key,int teacherId,boolean isLoadMore){
        RequestParams params = pageRequestParams(key, isLoadMore);
        params.addBodyParameter("TeacherUserId",teacherId+"");
        return params;
    }

}
