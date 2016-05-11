package com.meten.ifuture.activity.teacher;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.http.RequestParams;
import com.meten.ifuture.R;
import com.meten.ifuture.activity.base.BaseHeadActivity;
import com.meten.ifuture.adapter.FilterAdapter;
import com.meten.ifuture.adapter.teacher.MyStudentAdapter;
import com.meten.ifuture.constant.Constant;
import com.meten.ifuture.constant.URL;
import com.meten.ifuture.http.HttpRequestCallBack;
import com.meten.ifuture.http.HttpRequestUtils;
import com.meten.ifuture.http.RequestParamsUtils;
import com.meten.ifuture.model.Area;
import com.meten.ifuture.model.ResultInfo;
import com.meten.ifuture.model.Teacher;
import com.meten.ifuture.model.User;
import com.meten.ifuture.model.teacher.Student;
import com.meten.ifuture.utils.JsonParse;
import com.meten.ifuture.utils.LogUtils;
import com.meten.ifuture.utils.SharedPreferencesUtils;
import com.meten.imanager.pulltorefresh.library.PullToRefreshBase;
import com.meten.imanager.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/3/3.
 * 我的学员界面
 */
public class MyStudentActivity extends BaseHeadActivity implements View.OnClickListener {
    private static final int GET_AREA = 0x11;
    private static final int GET_TEACHER = 0x12;
    private MyStudentAdapter adapter;
    private FilterAdapter areaAdapter;
    private ListView filterListView;
    private CheckBox cbArea;
    private EditText etSearch;
    private LinearLayout filterLayout;
    private int currentAreaId = 0;
    private int currentTeacherId = 0;
    private String currentKeyword = "";
    private PullToRefreshListView mRefreshListView;
//    private PullToRefreshListView refreshListView;
    private User user;


    private RelativeLayout rlTeacherSearch;
    private LinearLayout llManagerFilter;
    private LinearLayout llManagerSearch;
    private CheckBox cbTeacher;
    private ImageButton ibSearch;
    private EditText etManagerSearch;
    private ImageView ivManagerSearch;
    private static List<Area> areaList;
    private static List<Teacher> teacherList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_student_filter);
        user = SharedPreferencesUtils.getInstance(this).getUser();

        initView();

        Drawable d = getResources().getDrawable(R.drawable.search_edit_bottom_bg);
        int w = d.getMinimumWidth();
        int h = d.getMinimumHeight();
        LogUtils.e("w:"+w+"   +h:"+h);
    }

    private void initView() {
        filterListView = (ListView) findViewById(R.id.listview);
        cbArea = (CheckBox) findViewById(R.id.area_cb);
        etSearch = (EditText) findViewById(R.id.search_et);
        mRefreshListView = (PullToRefreshListView) findViewById(R.id.refresh_listview);
        filterLayout = (LinearLayout) findViewById(R.id.filter_list_layout);
        rlTeacherSearch = (RelativeLayout) findViewById(R.id.teacher_search_rl);
        llManagerFilter = (LinearLayout) findViewById(R.id.manager_filter_ll);
        llManagerSearch = (LinearLayout) findViewById(R.id.manager_search_ll);
        cbTeacher = (CheckBox) findViewById(R.id.teacher_cb);
        ibSearch = (ImageButton) findViewById(R.id.search_ib);
        etManagerSearch = (EditText) findViewById(R.id.search_bottom_et);
        ivManagerSearch = (ImageView) findViewById(R.id.bottom_search_iv);


        areaAdapter = new FilterAdapter(this);
        filterListView.setAdapter(areaAdapter);
        if(user.getUserType() == Constant.TEACHER){
            setTitle(getString(R.string.my_student));
        }else if(user.getUserType() == Constant.MANAGER){
            setTitle(getString(R.string.proceed_student));
            rlTeacherSearch.setVisibility(View.GONE);
            llManagerFilter.setVisibility(View.VISIBLE);
        }
        hiddenRightImageView();

        adapter = new MyStudentAdapter(this);

        mRefreshListView.setAdapter(adapter);

        mRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
        mRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase refreshView) {
                getMyStudents(currentAreaId, currentTeacherId,null,false, true);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase refreshView) {
                getMyStudents(currentAreaId, currentTeacherId,null, true, true);
            }
        });


        filterListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                areaAdapter.setCheckedId((int)id);
                checkedFilterItem(position);
            }
        });

        etSearch.setOnEditorActionListener(editorActionListener);
        etManagerSearch.setOnEditorActionListener(editorActionListener);


        cbArea.setOnClickListener(this);
        cbTeacher.setOnClickListener(this);
        ibSearch.setOnClickListener(this);
        ivManagerSearch.setOnClickListener(this);
        filterLayout.setOnClickListener(this);

        getMyStudents(currentAreaId, currentTeacherId,currentKeyword,false,true);

        if(areaList == null){
            //获取区域列表
            HttpRequestUtils.create(this).isShowLoadingDilag(false).send(URL.GET_AREA_LIST,RequestParamsUtils.createRequestParams(),GET_AREA,callBack);
        }

//        if(teacherList == null){
            //获取区域列表
            HttpRequestUtils.create(this).isShowLoadingDilag(false).send(URL.GET_TEACHER_LIST,RequestParamsUtils.createRequestParams(),GET_TEACHER,callBack);
//        }

    }

    private void checkedFilterItem(int position) {
        if(areaAdapter.getTypeId() == FilterAdapter.AREA){
            Area area = (Area)areaAdapter.getItem(position);
            cbArea.setText(area.getName());
            if(area.getId() != 0){
                Teacher tea = teacherList.get(0);
                currentTeacherId = tea.getId();
                cbTeacher.setText(tea.getName());
                areaAdapter.setTeacherCheckedId(tea.getId());
            }
            getMyStudents(area.getId(),currentTeacherId,false);
            cbArea.setChecked(false);
        }else if(areaAdapter.getTypeId() == FilterAdapter.TEACHER){
            Teacher tea = (Teacher)areaAdapter.getItem(position);
            cbTeacher.setText(tea.getName());
            getMyStudents(currentAreaId, tea.getId(),false);
            cbTeacher.setChecked(false);
        }

        filterLayout.setVisibility(View.GONE);

    }

    private void getMyStudents(int areaId, int teaId,boolean isLoadMore) {
        getMyStudents(areaId, teaId,null,isLoadMore,false);
    }


    private TextView.OnEditorActionListener editorActionListener = new TextView.OnEditorActionListener() {

        @Override
        public boolean onEditorAction(TextView v, int actionId,
                                      KeyEvent event) {
            // 当actionId == XX_SEND 或者 XX_DONE时都触发
            // 或者event.getKeyCode == ENTER 且 event.getAction ==
            // ACTION_DOWN时也触发
            // 注意，这是一定要判断event != null。因为在某些输入法上会返回null。
            if (actionId == EditorInfo.IME_ACTION_SEARCH
                    || actionId == EditorInfo.IME_ACTION_DONE
                    || (event != null
                    && KeyEvent.KEYCODE_ENTER == event.getKeyCode() && KeyEvent.ACTION_DOWN == event
                    .getAction())) {
                getMyStudents(currentAreaId, currentTeacherId,v.getText().toString(),false,false);
            }
            return true;
        }
    };

    /**
     * 获取我的学生
     * @param areaId 区域id
     * @param isLoadMore 是否加载更多
     * @param initData 是否初始化数据
     */
    private void getMyStudents(int areaId, int teaId,String keyword,boolean isLoadMore, boolean initData) {

        if(currentAreaId == areaId && currentTeacherId == teaId && currentKeyword.equals(keyword) && !initData ){
            return;
        }
        currentAreaId = areaId;
        currentTeacherId = teaId;
        if(keyword == null){
            if(user.getUserType() == Constant.TEACHER){
                keyword = etSearch.getText().toString();
            }else if(user.getUserType() == Constant.MANAGER){
                keyword = etManagerSearch.getText().toString();
            }
        }
        currentKeyword = keyword;
        LogUtils.e("areaId:"+currentAreaId+"  teacherId:"+currentTeacherId);
        RequestParams params = RequestParamsUtils.getMyStudent(currentAreaId, currentTeacherId,currentKeyword, isLoadMore);
        int requestCode;
        if(isLoadMore){
            requestCode = Constant.LOADMORE;
        }else{
            requestCode = Constant.REFRESH;
        }
        HttpRequestUtils.create(this).send(URL.GET_MY_STUDENT, params, requestCode, callBack);
    }

    HttpRequestCallBack<ResultInfo> callBack = new HttpRequestCallBack<ResultInfo>() {
        @Override
        public void onSuccess(ResultInfo info, int requestCode) {
            if(requestCode == GET_AREA){
                areaList = JsonParse.parseToObject(info,new TypeToken<List<Area>>(){}.getType());
                Area area = new Area();
                area.setId(0);
                area.setName("全部区域");
                areaList.add(0, area);
//                areaAdapter.setListData(areaList);
//                ListViewUtils.setListViewMaxHeight(areaListView);
            }else

            if(requestCode == GET_TEACHER){
                teacherList = JsonParse.parseToObject(info,new TypeToken<List<Teacher>>(){}.getType());
                Teacher tea = new Teacher();
                tea.setCnName("全部老师");
                tea.setEnName("");
                tea.setUserId(0);
                teacherList.add(0,tea);

            }else {
                List<Student> data = JsonParse.parseDataOfPageToData(info, new TypeToken<List<Student>>() {
                }.getType());
                if (data != null) {
                    if (requestCode == Constant.REFRESH) {
                        adapter.setListData(data);
                    } else if (requestCode == Constant.LOADMORE) {
                        adapter.addData(data);
                    }
                }
                mRefreshListView.onRefreshComplete();
            }
        }
    };

    /**
     * 根据区域id筛选教师列表
     * @param id
     * @return
     */
    private List<Teacher> getTeacherListByAreaId(int id){
        if(id == 0){
            return teacherList;
        }
        Teacher t = teacherList.get(0);
        List<Teacher> list = new ArrayList<>();
        list.add(t);
        for(Teacher tea : teacherList){
            if(tea.getAreaId() == currentAreaId){
                list.add(tea);
            }
        }
        return list;
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.area_cb:
                if(cbArea.isChecked()){
                    cbTeacher.setChecked(false);
                    filterLayout.setVisibility(View.VISIBLE);
                    areaAdapter.setListData(areaList);
                    areaAdapter.setTypeId(FilterAdapter.AREA);
                }else{
                    filterLayout.setVisibility(View.GONE);
                }
                break;
            case R.id.teacher_cb:
                if(cbTeacher.isChecked()){
                    cbArea.setChecked(false);
                    filterLayout.setVisibility(View.VISIBLE);
                    areaAdapter.setListData(getTeacherListByAreaId(currentAreaId));
                    areaAdapter.setTypeId(FilterAdapter.TEACHER);
                }else{
                    filterLayout.setVisibility(View.GONE);
                }
                break;
            case R.id.search_ib:
                if(llManagerSearch.getVisibility() == View.GONE){
                    llManagerSearch.setVisibility(View.VISIBLE);
                }else{
                    llManagerSearch.setVisibility(View.GONE);
                    etManagerSearch.setText("");
                    currentKeyword = "";
                }
                break;
            case R.id.bottom_search_iv:
                getMyStudents(currentAreaId, currentTeacherId,false);
                break;
            case R.id.filter_list_layout:
                filterLayout.setVisibility(View.GONE);
                cbArea.setChecked(false);
                cbTeacher.setChecked(false);
                break;

        }
    }
}
