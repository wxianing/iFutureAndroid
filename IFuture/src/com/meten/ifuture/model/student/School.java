package com.meten.ifuture.model.student;

/**
 * 我的选校实体类
 */
public class School {
	// "StudentUniversityId": 1,
	// "StudentUserId": 13,
	// "CountryCnName": "美国",
	// "UniversityId": 1,
	// "UniversityCnName": "哈佛大学",
	// "UniversityProject": "哈佛项目",
	// "UniversityUrl": "http://www.harvard.edu/",
	// "SortNo": 1,
	// "StudentStatus": 1,
	// "TeacherStatus": 1

	private int StudentUniversityId;

	private int UniversityId;

	private String CountryCnName;

	private String UniversityCnName;

	private String UniversityProject;

	private String UniversityUrl;

	private int SortNo;

	private int StudentStatus;

	private int TeacherStatus;

	public int getStudentUniversityId() {
		return StudentUniversityId;
	}

	public void setStudentUniversityId(int studentUniversityId) {
		StudentUniversityId = studentUniversityId;
	}

	public int getUniversityId() {
		return UniversityId;
	}

	public void setUniversityId(int universityId) {
		UniversityId = universityId;
	}

	public String getCountryCnName() {
		return CountryCnName;
	}

	public void setCountryCnName(String countryCnName) {
		CountryCnName = countryCnName;
	}

	public String getUniversityCnName() {
		return UniversityCnName;
	}

	public void setUniversityCnName(String universityCnName) {
		UniversityCnName = universityCnName;
	}

	public String getUniversityProject() {
		return UniversityProject;
	}

	public void setUniversityProject(String universityProject) {
		UniversityProject = universityProject;
	}

	public String getUniversityUrl() {
		return UniversityUrl;
	}

	public void setUniversityUrl(String universityUrl) {
		UniversityUrl = universityUrl;
	}

	public int getSortNo() {
		return SortNo;
	}

	public void setSortNo(int sortNo) {
		SortNo = sortNo;
	}

	public int getStudentStatus() {
		return StudentStatus;
	}

	public void setStudentStatus(int studentStatus) {
		StudentStatus = studentStatus;
	}

	public int getTeacherStatus() {
		return TeacherStatus;
	}

	public void setTeacherStatus(int teacherStatus) {
		TeacherStatus = teacherStatus;
	}

}
