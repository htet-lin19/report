/**	
 *

 作成履歴：2020/10/12 GICM AMTD
 * 作成概要：leavesummary, attendacesummary table用データ保持処理
 */
package com.model;

import java.util.Date;

public class AttendanceSummaryInfoFormDetail {
	String emp_cd;                    //社員番号						
	Date summary_month;               //対象年度							
	String total_work_hour;           //勤務時間							
	String legal_overtime;	          //法定時間外						
	String legal_over_workday;	      //法定休日					
	String midnight_work_hour;	      //深夜早朝	
	String wholeday_leave;            //全休
	String halfday_leave;             //半休 
	String compensatory_leave;        //代休				
	String releaving_leave;           //特別休暇	
	String month;                     //月を比較する
						
	public String getEmp_cd() {
		return emp_cd;
	}
	
	public void setEmp_cd(String emp_cd) {
		this.emp_cd = emp_cd;
	}
	
	public Date getSummary_month() {
		return summary_month;
	}
	
	public void setSummary_month(Date summary_month) {
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
	
	public String getWholeday_leave() {
		return wholeday_leave;
	}
	
	public void setWholeday_leave(String wholeday_leave) {
		this.wholeday_leave = wholeday_leave;
	}
	
	public String getHalfday_leave() {
		return halfday_leave;
	}
	
	public void setHalfday_leave(String halfday_leave) {
		this.halfday_leave = halfday_leave;
	}
	
	public String getCompensatory_leave() {
		return compensatory_leave;
	}
	
	public void setCompensatory_leave(String compensatory_leave) {
		this.compensatory_leave = compensatory_leave;
	}
	
	public String getReleaving_leave() {
		return releaving_leave;
	}
	
	public void setReleaving_leave(String releaving_leave) {
		this.releaving_leave = releaving_leave;
	}
	
	public String getMonth() {
		return month;
	}
	
	public void setMonth(String month) {
		this.month = month;
	}
	
}
