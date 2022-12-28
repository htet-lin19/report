/**
 *
勤務時間サマリ情報画面	
 * 作成履歴：2020/10/06 GICM AMTD
 * 作成概要：新規作成　勤務時間サマリ情報を表示する
 */
package com.action;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.dispatcher.SessionMap;
import org.apache.struts2.interceptor.SessionAware;

import com.common.CommonCheck;
import com.common.CommonConstant;
import com.jdbc.DataConnection;
import com.model.AttendanceSummaryInfoFormDetail;
import com.model.AttendanceSummaryInfoForm;
import com.opensymphony.xwork2.ActionSupport;

public class AttendanceSummaryInfoAction extends ActionSupport implements SessionAware{
	private static final long serialVersionUID = 1L;
	AttendanceSummaryInfoForm attendanceSummaryInfoFrm = new AttendanceSummaryInfoForm();
	AttendanceSummaryInfoFormDetail attendanceSummaryInfoDetail = new AttendanceSummaryInfoFormDetail();
	SessionMap<String, Object> sessionmap;
	HttpServletRequest request;

	/**
	 * 初期表示
	 * @return
	 */
	public String search() {
		// 権限チェック
		if (sessionmap.get("ID") == null) {
			return "session";
		} else if (!CommonCheck.isValidRole(CommonConstant.SCREEN_NO.ATTENDANCE_SUMMARY_INFO.value,
				sessionmap.get("ID").toString())) {
			return "role";
		}
			
		try {		
			String emp_cd; //ログインユーザーコードを設定する
			if(StringUtils.isNotEmpty(attendanceSummaryInfoFrm.getEmp_cd())) {
				emp_cd = attendanceSummaryInfoFrm.getEmp_cd(); //ログインユーザーコードを設定する
			}else {
				emp_cd = (String)sessionmap.get("ID"); //セッションログインユーザーコードを設定する
			}
			
			int this_year; //対象年度を設定する
			if(attendanceSummaryInfoFrm.getTarget_year()!=0) {
				this_year = attendanceSummaryInfoFrm.getTarget_year(); //ユーザーが選択する年分を設定する
			}else {
				this_year = getSystemYear(); //今年の年分を設定する
			}
			
			getYears(); //対象年度の値を初期する
			
			//休暇情報テーブルのデータを取得する
			String sql ="SELECT * FROM leaves WHERE emp_cd = '"+emp_cd.trim()+"' AND DATE_FORMAT(target_year, \"%Y\") = '"+ this_year+"'";
			Connection conn;
			conn = DataConnection.getConnection();
			PreparedStatement ps1 = conn.prepareStatement(sql);
			ResultSet rs1 = ps1.executeQuery();
			if(rs1.next()) {
				attendanceSummaryInfoFrm.setLastyear_carry_leave(rs1.getFloat("lastyear_carry_leave"));
				attendanceSummaryInfoFrm.setCur_year_allowed_leave(rs1.getFloat("cur_year_allowed_leave"));
				attendanceSummaryInfoFrm.setTotal_allowed_leave(rs1.getFloat("total_allowed_leave"));
				attendanceSummaryInfoFrm.setRemaining_leave(rs1.getFloat("remaining_leave"));
				}
			attendanceSummaryInfoFrm.setEmp_cd(emp_cd);
			attendanceSummaryInfoFrm.setTarget_year(this_year);
			
			//３Q年度分の月次休暇情報リストの値を初期する
			List<AttendanceSummaryInfoFormDetail> detailList = new ArrayList<>();
			for(int i=1;i<10;i++) {
				AttendanceSummaryInfoFormDetail monthList=new AttendanceSummaryInfoFormDetail();
				monthList.setMonth(Integer.toString(i+3));
				detailList.add(monthList);
			}
				
			//３Q年度分の月次休暇情報リストデータを設定する
			sql = "SELECT a.summary_month as a_month,a.total_work_hour,a.legal_overtime,a.legal_over_workday," 
					+ "a.midnight_work_hour,l.summary_month as l_month,l.wholeday_leave,l.halfday_leave,l.releaving_leave,l.compensatory_leave "
					+ "FROM attendancesummary a LEFT OUTER JOIN leavesummary l on a.emp_cd=l.emp_cd and a.summary_month=l.summary_month"
					+ " WHERE a.emp_cd = '"+emp_cd.trim()+"' AND DATE_FORMAT(a.summary_month, \"%Y\") = '"+ this_year
					+ "' UNION SELECT a.summary_month as a_month,a.total_work_hour,a.legal_overtime,a.legal_over_workday," 
					+ "a.midnight_work_hour,l.summary_month as l_month,l.wholeday_leave,l.halfday_leave,l.releaving_leave,l.compensatory_leave "
					+ "FROM attendancesummary a RIGHT OUTER JOIN leavesummary l on a.emp_cd=l.emp_cd and a.summary_month=l.summary_month"
					+ " WHERE l.emp_cd = '"+emp_cd.trim()+"' AND DATE_FORMAT(l.summary_month, \"%Y\") = '"+ this_year+"'";
	
			PreparedStatement ps2= conn.prepareStatement(sql);
			ResultSet rs2 = ps2.executeQuery();
			while(rs2.next()) {
				Date date;
				if(rs2.getDate("a_month")!=null) {
					date=rs2.getDate("a_month");
				}else {
					date=rs2.getDate("l_month");
				}
				
				String month=new SimpleDateFormat("M").format(date);
				for(int i=0;i<9;i++) {
					if(detailList.get(i).getMonth().matches(month)) {
						detailList.get(i).setTotal_work_hour(rs2.getString("total_work_hour"));
						detailList.get(i).setLegal_overtime(rs2.getString("legal_overtime"));
						detailList.get(i).setLegal_over_workday(rs2.getString("legal_over_workday"));
						detailList.get(i).setMidnight_work_hour(rs2.getString("midnight_work_hour"));
						detailList.get(i).setWholeday_leave(rs2.getString("wholeday_leave"));
						detailList.get(i).setHalfday_leave(rs2.getString("halfday_leave"));
						detailList.get(i).setReleaving_leave(rs2.getString("releaving_leave"));
						detailList.get(i).setCompensatory_leave(rs2.getString("compensatory_leave"));
					}
				}
			}
				
			int next_year=this_year+1; //年分を設定する
			//４Q年度の月次休暇情報リストの値を初期する
			for(int i=1;i<4;i++) {
				AttendanceSummaryInfoFormDetail monthList=new AttendanceSummaryInfoFormDetail();
				monthList.setMonth(Integer.toString(i));
				detailList.add(monthList);
			}
			
			//４Q年度の月次休暇情報リストデータを設定する
			sql = "SELECT a.summary_month as a_month,a.total_work_hour,a.legal_overtime,a.legal_over_workday," 
					+ "a.midnight_work_hour,l.summary_month as l_month,l.wholeday_leave,l.halfday_leave,l.releaving_leave,l.compensatory_leave "
					+ "FROM attendancesummary a LEFT OUTER JOIN leavesummary l on a.emp_cd=l.emp_cd and a.summary_month=l.summary_month"
					+ " WHERE a.emp_cd = '"+emp_cd.trim()+"' AND DATE_FORMAT(a.summary_month, \"%Y\") = '"+ next_year
					+ "' UNION SELECT a.summary_month as a_month,a.total_work_hour,a.legal_overtime,a.legal_over_workday,"
					+ "a.midnight_work_hour,l.summary_month as l_month,l.wholeday_leave,l.halfday_leave,l.releaving_leave,l.compensatory_leave "
					+ "FROM attendancesummary a RIGHT OUTER JOIN leavesummary l on a.emp_cd=l.emp_cd and a.summary_month=l.summary_month"
					+ " WHERE l.emp_cd = '"+emp_cd.trim()+"' AND DATE_FORMAT(l.summary_month, \"%Y\") = '"+ next_year+"'";
	
			PreparedStatement ps3= conn.prepareStatement(sql);
			ResultSet rs3 = ps3.executeQuery();
			while(rs3.next()) {
				Date date;
				if(rs3.getDate("a_month")!=null) {
					date=rs3.getDate("a_month");
				}else {
					date=rs3.getDate("l_month");
				}
				
				String month=new SimpleDateFormat("M").format(date);
				for(int i=9;i<12;i++) {
					if(detailList.get(i).getMonth().matches(month)) {
						detailList.get(i).setTotal_work_hour(rs3.getString("total_work_hour"));
						detailList.get(i).setLegal_overtime(rs3.getString("legal_overtime"));
						detailList.get(i).setLegal_over_workday(rs3.getString("legal_over_workday"));
						detailList.get(i).setMidnight_work_hour(rs3.getString("midnight_work_hour"));
						detailList.get(i).setWholeday_leave(rs3.getString("wholeday_leave"));
						detailList.get(i).setHalfday_leave(rs3.getString("halfday_leave"));
						detailList.get(i).setReleaving_leave(rs3.getString("releaving_leave"));
						detailList.get(i).setCompensatory_leave(rs3.getString("compensatory_leave"));
					}
				}
			}
			attendanceSummaryInfoFrm.setDetailList(detailList);	
			} catch (Exception e) {
				e.printStackTrace();
				return ERROR;
			}
		return SUCCESS;	
	}
	
	/**
	 * 閉じる処理
	 * @return
	 */
	public String close() {
		return SUCCESS;
	}
	
	/**
	 * 今年の値を取得する
	 * @return
	 */
	private int getSystemYear() {
		int year = Calendar.getInstance().get(Calendar.YEAR);
		return year;
	}
	
	/**
	 * 対象年度の初期値を取得する
	 */
	private void getYears(){
		int max = Calendar.getInstance().get(Calendar.YEAR);
		int min = max-3;
		List<Integer> years = new ArrayList<Integer>();
 		for(int i = min;i<=max;i++) {
			years.add(i);
		}
 		attendanceSummaryInfoFrm.setYearlist(years);
	}
	
	@Override
	public void setSession(Map<String, Object> session) {
		this.sessionmap = (SessionMap)session;
	}

	public AttendanceSummaryInfoForm getAttendanceSummaryInfoFrm() {
		return attendanceSummaryInfoFrm;
	}

	public void setAttendanceSummaryInfoFrm(AttendanceSummaryInfoForm attendanceSummaryInfoFrm) {
		this.attendanceSummaryInfoFrm = attendanceSummaryInfoFrm;
	}

	public AttendanceSummaryInfoFormDetail getAttendanceSummaryInfoDetail() {
		return attendanceSummaryInfoDetail;
	}

	public void setAttendanceSummaryInfoDetail(AttendanceSummaryInfoFormDetail attendanceSummaryInfoDetail) {
		this.attendanceSummaryInfoDetail = attendanceSummaryInfoDetail;
	}
	
}
