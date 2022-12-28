package com.model;

import java.util.ArrayList;
import java.util.List;

public class AttendanceListForm{
	private String emp_cd;
	private String emp_name;
	private String start_date;
	private String end_date;
	private String button_event;
	private int delete_id;
	private String search;
	private String disable_empcd;
	private String disable_empname;
	private List<String> notification_date;
	
	private List<AttendanceListFormDetail> attendanceListDetail = new ArrayList<AttendanceListFormDetail>(); 
	
	//2020/10/12 GICM KZP GLの場合、自分以外の勤務データを更新・削除不可能に設定する対応 Start
	private String disabled_glaction;
	//2020/10/12 GICM KZP GLの場合、自分以外の勤務データを更新・削除不可能に設定する対応End
	
	public String getEmp_cd() {
		return emp_cd;
	}
	public void setEmp_cd(String emp_cd) {
		this.emp_cd = emp_cd;
	}
	public String getEmp_name() {
		return emp_name;
	}
	public void setEmp_name(String emp_name) {
		this.emp_name = emp_name;
	}
	public String getStart_date() {
		return start_date;
	}
	public void setStart_date(String start_date) {
		this.start_date = start_date;
	}
	public String getEnd_date() {
		return end_date;
	}
	public void setEnd_date(String end_date) {
		this.end_date = end_date;
	}
	public String getButton_event() {
		return button_event;
	}
	public void setButton_event(String button_event) {
		this.button_event = button_event;
	}
	public int getDelete_id() {
		return delete_id;
	}
	public void setDelete_id(int delete_id) {
		this.delete_id = delete_id;
	}
	public String getSearch() {
		return search;
	}
	public void setSearch(String search) {
		this.search = search;
	}
	public String getDisable_empcd() {
		return disable_empcd;
	}
	public void setDisable_empcd(String disable_empcd) {
		this.disable_empcd = disable_empcd;
	}
	public String getDisable_empname() {
		return disable_empname;
	}
	public void setDisable_empname(String disable_empname) {
		this.disable_empname = disable_empname;
	}
	public List<AttendanceListFormDetail> getAttendanceListDetail() {
		return attendanceListDetail;
	}
	public void setAttendanceListDetail(List<AttendanceListFormDetail> attendanceListDetail) {
		this.attendanceListDetail = attendanceListDetail;
	}
	public List<String> getNotification_date() {
		return notification_date;
	}
	public void setNotification_date(List<String> notification_date) {
		this.notification_date = notification_date;
	}
	public String getDisabled_glaction() {
		return disabled_glaction;
	}
	public void setDisabled_glaction(String disabled_glaction) {
		this.disabled_glaction = disabled_glaction;
	}	
}