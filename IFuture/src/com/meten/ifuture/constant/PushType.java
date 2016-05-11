package com.meten.ifuture.constant;

/**
 * Created by Cmad on 2015/3/9.
 * 消息推送类型的枚举类
 */
public enum PushType {
    /**
     * 选校状态处理
     */
    STUDENT_UNIVERSITY_TATUS(1),
    /**
     * 学生完结处理
     */
    STUDENT_FINISH(2),
    /**
     * 投诉处理
     */
    STUDENT_COMPLAIN(3),
    /**
     * 有新的老师
     */
    STUDENT_NEW_TEACHER(4),
    /**
     * 规划有更新
     */
    STUDENT_PLAN_UPDATE(5),
    /**
     * 推荐了新学校
     */
    STUDENT_NEW_UNIVERSITY(6),

    /**
     * 成绩有更新
     */
    STUDENT_NEW_SCORE(7),
    /**
     * 有新的学生
     */
    TEACHER_NEW_STUDENT(201),
    /**
     * 学生接受|拒绝了学校
     */
    TEACHER_UNIVERSITY_STATUS(202),
    /**
     * 学生提交了材料
     */
    TEACHER_MATERIAL_SUBMITED(203),
    /**
     * 学生提交了文书
     */
    TEACHER_PAPERL_SUBMITED(204),

    /**
     * 新增进行学生
     */
    ADMIN_NEW_STUDENT(301),

    /**
     * 新增过往学生
     */
    ADMIN_FINISHED_STUDENT(302),

    /**
     * 新增投诉
     */
    ADMIN_NEW_COMPLAIN(303),

    /**
     *新增表扬
     */
    ADMIN_NEW_PRAISE(304),

    /**
     *新增留学讯息
     */
    ADMIN_NEW_NEWS(305),

    /**
     * 系统消息
     */
    SYSTEM_PUSH(205);

    private int tag;

    private PushType(int tag) {
        this.tag = tag;
    }

    public int toInt() {
        return tag;
    }
}
