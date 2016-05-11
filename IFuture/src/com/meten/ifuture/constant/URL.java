package com.meten.ifuture.constant;

public class URL {
    public static final int DEV = 0x00;
    public static final int TEST = 0x01;
    public static final int BETA = 0x02;
    public static final int ONLIN = 0x03;


    public static final String API_TEST = "http://172.21.4.87:8089/";
    public static final String API_DEV = "http://172.19.3.51:82/";
    public static final String API_ONLINE = "http://ifutureapi.meteni.com/";


    public static final String WEB_DEV = "http://172.19.3.51:82/";
    public static final String WEB_TEST = "http://172.21.4.87:8088/";
    public static final String WEB_ONLINE = "http://ifuture.meteni.com/";

    public static int VERSION = ONLIN;

    public static  String API_HOST = API_DEV;
    public static  String WEB_HOST = WEB_DEV;
    static {
        switch (VERSION) {
            case DEV:
                API_HOST = API_DEV;
                WEB_HOST = WEB_DEV;
                break;
            case TEST:
                API_HOST = API_TEST;
                WEB_HOST = WEB_TEST;
                break;
            case BETA:
                break;
            case ONLIN:
                API_HOST = API_ONLINE;
                WEB_HOST = WEB_ONLINE;
                break;
        }
    }


	
	public static final String LOGIN = API_HOST+"common/user/login"; //登录
	public static final String MODIF_USER_INFO = API_HOST+"common/user/update"; //修改用户信息
	public static final String UPLOAD = API_HOST+"common/file/upload"; //上传
	public static final String GET_MY_TEACHER = API_HOST+"student/listteacher"; //获取我的老师信息
	public static final String GET_MY_SCHOOL = API_HOST+"student/listuniversity"; //获取我的选校信息
	public static final String HAND_CHOOSE_SCHOOL = API_HOST+"student/choiceuniversity"; //处理选校（接受、拒绝）
	public static final String GET_RESOURCE = API_HOST+"student/listresource"; //获取我的资源（材料、文书）列表
	public static final String DELETE_RESOURCE = API_HOST+"student/deleteresource"; //删除资源
	public static final String GET_VALIDATE_CODE = API_HOST+"systemset/account/getvalidatecode"; //发送手机号码获取验证码
	public static final String RESET_PASSWORD = API_HOST+"systemset/account/resetpassword"; //重置密码
	public static final String GET_NEW_VERSION = API_HOST+"systemset/getlatestversoin"; //获取最新版本信息
	public static final String CHANGE_PASSWORD = API_HOST+"systemset/account/updatepassword"; //修改密码
	public static final String LOGIN_BY_THIRD = API_HOST+"common/user/loginbythird"; //第三方账号登录
	public static final String BIND_THIED_USER = API_HOST+"common/user/bindthridaccount"; //第三方账号绑定
	public static final String GET_BINDED_LIST = API_HOST+"common/user/getthridbindedlist"; //获取已绑定的列表
	public static final String UNBINDED_THIRD_USER = API_HOST+"common/user/unbindthridaccount"; //解除第三方账号绑定
	public static final String FEEDBACK = API_HOST+"systemset/addfeedback"; //系统反馈
	public static final String GET_AD_LIST = API_HOST+"common/advertise/getlist"; //获取广告列表
	public static final String GET_MY_STUDENT = API_HOST+"teacher/liststudent"; //获取我的学生
	public static final String ADD_COMPLAIN = API_HOST+"student/complain/add"; //添加投诉
	public static final String PRAISE = API_HOST+"student/praise/add"; //赞 or 取消赞
	public static final String GET_MY_COMPLAIN_LIST = API_HOST+"student/complain/getlist"; //获取我的投诉列表
	public static final String GET_COMPLAIN_DETAILS = API_HOST+"student/complain/get"; //根据id获取投诉详情
	public static final String SUBMIT_RESOURCE = API_HOST+"student/submitresource"; //提交材料or文书
	public static final String GET_AREA_LIST = API_HOST+"common/area/getlist"; //获取区域列表
	public static final String GET_TEACHER_LIST = API_HOST+"teacher/listteacherdropdown"; //获取老师列表
	public static final String GET_PRAISE_LIST = API_HOST+"manager/listpraise"; //获取表扬列表
	public static final String GET_ROLE_LIST = API_HOST+"manager/listrole"; //获取老师角色列表
	public static final String GET_COMPLAIN_LIST = API_HOST+"manager/listcomplain"; // 管理端获取投诉的老师列表
	public static final String GET_TEACHER_COMPLAIN_LIST = API_HOST+"manager/listcomplainbyteacheruserid"; // 管理端获取老师的投诉列表
	public static final String HAND_COMPLAIN = API_HOST+"student/complain/handle"; // 学生处理投诉
	public static final String GET_TEACHER_PRAISE_DETAILS = API_HOST+"manager/listpraisebyteacheruserid"; // 获取管理端老师表扬详情
	public static final String GET_SYSTEM_MESSAGE = API_HOST+"systemset/message/getlist"; // 获取消息列表





    public static final String WEB_STUDY_ABROAD_INFO = WEB_HOST+"mobile/news"; //留学资讯
    public static final String WEB_MY_PLAN = WEB_HOST+"mobile/student/planindex?code=%1$s&appid="+Constant.APPID; //我的规划
    public static final String WEB_MY_SCORE = WEB_HOST+"mobile/student/scoreindex?code=%1$s&appid="+Constant.APPID; //我的成绩
    public static final String WEB_FINISHED_STUDEND = WEB_HOST+"mobile/teacher/studentfinishd?code=%1$s&appId="+Constant.APPID; //过往学生
    public static final String WEB_SCHOOL_RANK = WEB_HOST+"mobile/universityrank";  //学校排名
    public static final String WEB_CHOOSE_SCHOOL = WEB_HOST+"mobile/student/universityindex?code=%1$s&appId="+Constant.APPID;   //选校
    public static final String WEB_ABOUT = WEB_HOST+"mobile/news/about";   //关于界面
    public static final String WEB_NEWS_DETAIL = WEB_HOST+"mobile/news/detail?id=";   //咨询详情页 或者 推送详情页



}
