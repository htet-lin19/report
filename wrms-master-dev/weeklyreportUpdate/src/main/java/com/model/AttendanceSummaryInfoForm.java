/**	
 *

 作成履歴：2020/10/06 GICM AMTD
 * 作成概要：leaves, leavesummary, attendacesummary table用データ保持処理
 */
package com.model;

import java.util.List;

public class AttendanceSummaryInfoForm {
	String emp_cd;		           //社員番号					
	int target_year;		       //年度			
	float lastyear_carry_leave;    //前年度繰越分						
	float cur_year_allowed_leave;  //今年度付与分						
	float total_allowed_leave;     //取得可能日数						
	float remaining_leave;         //有給残日数
	List<Integer> yearlist;        //対象年度の初期値リスト
	
	List<AttendanceSummaryInfoFormDetail> detailList;

	public String getEmp_cd() {
		return emp_cd;
	}

	public void setEmp_cd(String emp_cd) {
		this.emp_cd = emp_cd;
	}

	public int getTarget_year() {
		return target_year;
	}

	public void setTarget_year(int target_year) {
		this.target_year = target_year;
	}

	public float getLastyear_carry_leave() {
		return lastyear_carry_leave;
	}

	public void setLastyear_carry_leave(float lastyear_carry_leave) {
		this.lastyear_carry_leave = lastyear_carry_leave;
	}

	public float getCur_year_allowed_leave() {
		return cur_year_allowed_leave;
	}

	public void setCur_year_allowed_leave(float cur_year_allowed_leave) {
		this.cur_year_allowed_leave = cur_year_allowed_leave;
	}

	public float getTotal_allowed_leave() {
		return total_allowed_leave;
	}

	public void setTotal_allowed_leave(float total_allowed_leave) {
		this.total_allowed_leave = total_allowed_leave;
	}

	public float getRemaining_leave() {
		return remaining_leave;
	}

	public void setRemaining_leave(float remaining_leave) {
		this.remaining_leave = remaining_leave;
	}

	public List<Integer> getYearlist() {
		return yearlist;
	}

	public void setYearlist(List<Integer> yearlist) {
		this.yearlist = yearlist;
	}

	public List<AttendanceSummaryInfoFormDetail> getDetailList() {
		return detailList;
	}

	public void setDetailList(List<AttendanceSummaryInfoFormDetail> detailList) {
		this.detailList = detailList;
	}

}
