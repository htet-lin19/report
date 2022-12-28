package com.model;

import java.util.List;

public class AttendanceListFormDetail{
	int id;
	String start_time;
	String end_time;
	String choose_date;
	String work_hour;
	String overtime;
	String midnight_overtime;
	String compensatory_leave_hour;
	String releaving_leave_hour;
	String task_description;
	String midnight_compensatory_leave_hour;
	String emp_cd;
	String emp_name;
	String day;
	String break_time;
	
	//2020/10/06 GICM KZP シフト勤務　 対応 Start
	int shift_work;
	//2020/10/06 GICM KZP シフト勤務　 対応 End
	
	//2020/10/07 GICM KZP 印刷レイアウト変更 対応 Start
	String overtime_sunday;
	String compensatory_comment;
	//2020/10/07 GICM KZP 印刷レイアウト変更 対応 End
	
	String compensatory_date;
	String compensatory_leave;
	String position;
	
	//2020/11/03 GICM NC 勤務時間登録 対応 Start
	String kyuka;
	String compensatory_flg;
	//2020/11/03 GICM NC 勤務時間登録 対応 End
	
	// カレンダー上での祝日
	List<String> holidayList;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getStart_time() {
		return start_time;
	}
	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}
	public String getEnd_time() {
		return end_time;
	}
	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}
	public String getChoose_date() {
		return choose_date;
	}
	public void setChoose_date(String choose_date) {
		this.choose_date = choose_date;
	}
	public String getWork_hour() {
		return work_hour;
	}
	public void setWork_hour(String work_hour) {
		this.work_hour = work_hour;
	}
	public String getOvertime() {
		return overtime;
	}
	public void setOvertime(String overtime) {
		this.overtime = overtime;
	}
	public String getMidnight_overtime() {
		return midnight_overtime;
	}
	public void setMidnight_overtime(String midnight_overtime) {
		this.midnight_overtime = midnight_overtime;
	}
	public String getCompensatory_leave_hour() {
		return compensatory_leave_hour;
	}
	public void setCompensatory_leave_hour(String compensatory_leave_hour) {
		this.compensatory_leave_hour = compensatory_leave_hour;
	}
	public String getReleaving_leave_hour() {
		return releaving_leave_hour;
	}
	public void setReleaving_leave_hour(String releaving_leave_hour) {
		this.releaving_leave_hour = releaving_leave_hour;
	}
	public String getTask_description() {
		return task_description;
	}
	public void setTask_description(String task_description) {
		this.task_description = task_description;
	}
	public String getMidnight_compensatory_leave_hour() {
		return midnight_compensatory_leave_hour;
	}
	public void setMidnight_compensatory_leave_hour(String midnight_compensatory_leave_hour) {
		this.midnight_compensatory_leave_hour = midnight_compensatory_leave_hour;
	}
	public String getDay() {
		return day;
	}
	public void setDay(String day) {
		this.day = day;
	}
	public String getBreak_time() {
		return break_time;
	}
	public void setBreak_time(String break_time) {
		this.break_time = break_time;
	}
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
	public List<String> getHolidayList() {
		return holidayList;
	}
	public void setHolidayList(List<String> holidayList) {
		this.holidayList = holidayList;
	}
	public int getShift_work() {
		return shift_work;
	}
	public void setShift_work(int shift_work) {
		this.shift_work = shift_work;
	}
	public String getCompensatory_comment() {
		return compensatory_comment;
	}
	public void setCompensatory_comment(String compensatory_comment) {
		this.compensatory_comment = compensatory_comment;
	}
	public String getOvertime_sunday() {
		return overtime_sunday;
	}
	public void setOvertime_sunday(String overtime_sunday) {
		this.overtime_sunday = overtime_sunday;
	}
	public String getCompensatory_date() {
		return compensatory_date;
	}
	public void setCompensatory_date(String compensatory_date) {
		this.compensatory_date = compensatory_date;
	}
	public String getCompensatory_leave() {
		return compensatory_leave;
	}
	public void setCompensatory_leave(String compensatory_leave) {
		this.compensatory_leave = compensatory_leave;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public String getKyuka() {
		return day;
	}
	public void setKyuka(String day) {
		this.day = day;
	}
	public String getCompensatory_flg() {
		return compensatory_leave;
	}
	public void setCompensatory_flg(String compensatory_leave) {
		this.compensatory_leave = compensatory_leave;
	}

}