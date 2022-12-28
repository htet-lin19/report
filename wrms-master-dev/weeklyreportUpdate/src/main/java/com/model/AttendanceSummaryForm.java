/**	
 * 作成履歴：2020/10/06 Theint Sandari Kyaw
 * 作成概要：attendacesummary table用データ保持処理
 */
package com.model;

public class AttendanceSummaryForm {
	int id;                    //ID
	String emp_cd;             //社員番号
	String summary_month;      //対象年月
	String total_work_hour;    //勤務時間
	String legal_overtime;     //法定時間外
	String legal_over_workday; //法定休日
	String midnight_work_hour; //深夜早朝
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getEmp_cd() {
		return emp_cd;
	}
	public void setEmp_cd(String emp_cd) {
		this.emp_cd = emp_cd;
	}
	public String getSummary_month() {
		return summary_month;
	}
	public void setSummary_month(String summary_month) {
		this.summary_month = summary_month;
	}
	public String getTotal_work_hour() {
		return total_work_hour;
	}
	public void setTotal_work_hour(String total_work_hour) {
		this.total_work_hour = total_work_hour;
	}
	public String getLegal_overtime() {
		return legal_overtime;
	}
	public void setLegal_overtime(String legal_overtime) {
		this.legal_overtime = legal_overtime;
	}
	public String getLegal_over_workday() {
		return legal_over_workday;
	}
	public void setLegal_over_workday(String legal_over_workday) {
		this.legal_over_workday = legal_over_workday;
	}
	public String getMidnight_work_hour() {
		return midnight_work_hour;
	}
	public void setMidnight_work_hour(String midnight_work_hour) {
		this.midnight_work_hour = midnight_work_hour;
	}
}
