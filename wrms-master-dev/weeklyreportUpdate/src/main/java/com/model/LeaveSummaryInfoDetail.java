/**	
 * 作成履歴：2020/10/13 Kyaw Zin Phyoe
 * 作成概要：leaves, leavesummary table用データ保持処理
 */
package com.model;

public class LeaveSummaryInfoDetail {
	
	private String emp_cd;                 //社員番号
	private String emp_name;               //氏名
	private float lastyear_carry_leave;    //前年度繰越分
	private float cur_year_allowed_leave;  //今年度付与分
	private float total_allowed_leave;     //取得可能日数
	private float remaining_leave;         //有給残日数
	private int wholeday_leave;            //全休
	private int halfday_leave;             //半休
	private int releaving_leave;           //特別休暇
	private int compensatory_leave;        //代休
	
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
	public int getWholeday_leave() {
		return wholeday_leave;
	}
	public int getHalfday_leave() {
		return halfday_leave;
	}
	public void setHalfday_leave(int halfday_leave) {
		this.halfday_leave = halfday_leave;
	}
	public int getReleaving_leave() {
		return releaving_leave;
	}
	public void setReleaving_leave(int releaving_leave) {
		this.releaving_leave = releaving_leave;
	}
	public int getCompensatory_leave() {
		return compensatory_leave;
	}
	public void setCompensatory_leave(int compensatory_leave) {
		this.compensatory_leave = compensatory_leave;
	}
	public void setWholeday_leave(int wholeday_leave) {
		this.wholeday_leave = wholeday_leave;
	}
}
