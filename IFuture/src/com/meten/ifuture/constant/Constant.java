package com.meten.ifuture.constant;

import android.os.Environment;
import android.text.TextUtils;

import com.meten.ifuture.AppManager;
import com.meten.ifuture.model.User;
import com.meten.ifuture.utils.SharedPreferencesUtils;

import java.io.File;

public class Constant {

    public static final String BASE_DIR = Environment
            .getExternalStorageDirectory()
            + File.separator
            + "iFuture"
            + File.separator;
    public static final String FILE_DIR = BASE_DIR + "file" + File.separator;
    public static final String CACHE_DIR = BASE_DIR +"cache"+File.separator;

	/**
	 * 用于intent传递类型值得key
	 */
	public static final String KEY_TYPE = "type";
	public static final String USER_ID = "userId";
	public static final String NICK_NAME = "NickName";
	public static final String PIC_URL = "PicUrl";
	public static final String THIRD_TYPE = "thirdType";
	public static final String VERSION_NAME = "versionName";
    public static final String URL = "url";
    public static final String TITLE = "title";
    public static final String COMPLAIN_OBJECT = "complian_object";
    public static final String COMPLAIN = "complain";
    public static final String COMPLAIN_ID = "complainId";
    public static final String PUSH_TYPE = "msgType";
    public static final String PUSH_EXTRA_ID = "Id";
    public static final String TEACHER_KEY = "teacher";
    public static final String STUDENT_KEY = "student";
    public static final String PAST_STUDENT = "pastStudent";


    public static final String STUDENT_ID = "StudentUserId";
    public static final String STUDENT_NAME = "StudentCnName";


    public static final String APPID = "102";


    //用户角色值 1：学生 2：教师  4：管理人员
    public static final int STUDENT = 1;
    public static final int TEACHER = 2;
    public static final int MANAGER = 4;


    public static final int REFRESH = 0x00;
    public static final int LOADMORE = 0x01;


	/**
	 * 我的选校：同意
	 */
	public static final int ACCEPT = 2;
	/**
	 * 我的选校：拒绝
	 */
	public static final int REFUSE = 3;

	/**
	 * 上传头像
	 */
	public static final int USER_ICON = 1;

	/**
	 * 我的材料
	 */
	public static final int DATUM = 2;

	/**
	 * 我的文书
	 */
	public static final int WRIT = 3;

	public static final int FILE_SELECT_CODE = 1001;

	public static String REQUEST_CODE = "";

	/**
	 * 网络请求访问成功的code值
	 */
	public static final int SUCCESS = 0;

	public static String getRequestCode() {
		if (TextUtils.isEmpty(REQUEST_CODE)) {
			User user = SharedPreferencesUtils.getInstance(
					AppManager.getAppManager().currentActivity()).getUser();
			if (user != null) {
				REQUEST_CODE = user.getCode();
			}
		}
		return REQUEST_CODE;
	}

	// action
	public static final String ACTION_REFRESH_PHOTO = "com.meten.ifuture.ACTION_REFRESH_PHOTO";
	public static final String ACTION_BIND_THIRD_USER = "com.meten.ifuture.ACTION_BIND_THIRD_USER";
	public static final String ACTION_NEW_VERSION = "com.meten.ifuture.ACTION_NEW_VERSION";
	public static final String ACTION_REFRESH_MESSAGE_COUNT = "com.meten.ifuture.ACTION_REFRESH_MESSAGE_COUNT";

	//bindactivity的传入类型
	public static final int BIND_USER = 1; //绑定用户
	public static final int RESET_PASSWORD = 2; //重置密码
	public static final int CHANGE_PASSWORD = 3; //修改密码
	public static final int BIND_USER_FOR_SETTING = 4; //修改密码


    //第三方登录类型
	public static final int QQ = 1;
	public static final int WECHAT = 2;
	public static final int WEIBO = 4;

    //第三方账号未绑定系统账号
    public static final int UNBIND = 402;

    //投诉文字的最大长度
    public static final int MAX_COMPLAIN_TEXT_SIZE = 256;


    //投诉未处理
    public static final int UNHANDLED = 1;
    //投诉已处理
    public static final int HANDLED = 2;

    /**
     * 系统消息类型
     */
    public static final int SYSTEM_MESSAGE = 205;
    /**
     * 用户业务消息类型
     */
    public static final int USER_MESSAGE = 206;

}
