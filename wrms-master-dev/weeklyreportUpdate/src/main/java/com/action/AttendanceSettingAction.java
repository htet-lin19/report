package com.action;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;

import org.apache.struts2.dispatcher.SessionMap;
import org.apache.struts2.interceptor.SessionAware;

import com.common.CommonCheck;
import com.common.CommonConstant;
import com.jdbc.DataConnection;
import com.model.AttendanceListFormDetail;
import com.opensymphony.xwork2.ActionSupport;

public class AttendanceSettingAction extends ActionSupport implements SessionAware{

	private static final long serialVersionUID = 1L;
	AttendanceListFormDetail attendance=new AttendanceListFormDetail();
	Map<String, Object> sessionmap;

	public AttendanceListFormDetail getAttendance() {
		return attendance;
	}

	public void setAttendance(AttendanceListFormDetail attendance) {
		this.attendance = attendance;
	}
	
	/**
	 * 初期表示
	 * @return
	 */
	public String execute() {
		// 権限チェック
		if (sessionmap.get("ID") == null) {
			return "session";
		} else if(!CommonCheck.isValidRole(CommonConstant.SCREEN_NO.ATTENDANCE_SETTING.value, 
				sessionmap.get("ID").toString())) {
			return "role";
		}
		
		// セッションデータ取得
		String emp_cd = (String)sessionmap.get("ID");
	
		String sql = "SELECT * FROM attendancemaster WHERE emp_cd = '"+emp_cd+"'";
		
		Connection conn;
		try {
			conn = DataConnection.getConnection();
			PreparedStatement ps1 = conn.prepareStatement(sql);
			ResultSet rs = ps1.executeQuery();
			
			while(rs.next()){				
				attendance.setStart_time(rs.getString("start_time"));
				attendance.setEnd_time(rs.getString("end_time"));
				attendance.setWork_hour(rs.getString("work_hour"));
				attendance.setBreak_time(rs.getString("break_time"));
				//2020/10/06 GICM KZP シフト勤務　 対応 Start
				attendance.setShift_work(rs.getInt("shift_work"));
				//2020/10/06 GICM KZP シフト勤務　 対応 End
			}
		}catch(Exception e) {
			e.printStackTrace();
			return ERROR;
		}

		return SUCCESS;
	}
	
	/**
	 * 登録処理
	 * @return
	 */
	public String insert() {
		// 権限チェック
		if (sessionmap.get("ID") == null) {
			return "session";
		} else if(!CommonCheck.isValidRole(CommonConstant.SCREEN_NO.ATTENDANCE_SETTING.value, 
				sessionmap.get("ID").toString())) {
			return "role";
		}
		
		Connection conn = null;
		ResultSet rs;
		int status = 0;
		try {			
			conn = DataConnection.getConnection();
			// Created Date
			Timestamp fixed_time = new Timestamp(new Date().getTime());
					
			String query2="SELECT * FROM attendancemaster WHERE emp_cd='"+attendance.getEmp_cd()+"'" ;
			PreparedStatement ps2 = conn.prepareStatement(query2);
			rs=ps2.executeQuery();
			if(!rs.next()) {
				
				/* String query = "INSERT INTO attendancemaster(emp_cd, start_time, end_time, "
						+ "break_time, work_hour, created_date, modified_date) "
						+ "VALUES(?,?,?,?,?,?,?)"; */
				String query = "INSERT INTO attendancemaster(emp_cd, start_time, end_time, "
						+ "break_time, work_hour, created_date, modified_date, shift_work) "
						+ "VALUES(?,?,?,?,?,?,?,?)";
				PreparedStatement ps = conn.prepareStatement(query); 
				ps.setString(1, attendance.getEmp_cd());
				ps.setString(2, attendance.getStart_time());
				ps.setString(3, attendance.getEnd_time());
				ps.setString(4, attendance.getBreak_time());
				ps.setString(5, attendance.getWork_hour());
				ps.setTimestamp(6, fixed_time);
				ps.setTimestamp(7, fixed_time);
				//2020/10/06 GICM KZP シフト勤務　 対応 Start
				ps.setInt(8, attendance.getShift_work());
				//2020/10/06 GICM KZP シフト勤務　 対応 End
				
				status = ps.executeUpdate();
				
				if(status==1){
					addActionMessage("登録しました。");
					clear();
				}
			} else {
				/* String query = "UPDATE attendancemaster SET start_time=?, end_time=?, break_time=?, work_hour=?, modified_date=? WHERE emp_cd=?"; */
				String query = "UPDATE attendancemaster SET start_time=?, end_time=?, break_time=?, work_hour=?, modified_date=?, shift_work=? WHERE emp_cd=?";
				PreparedStatement ps = conn.prepareStatement(query); 				
				
				ps.setString(1, attendance.getStart_time());
				ps.setString(2, attendance.getEnd_time());
				ps.setString(3, attendance.getBreak_time());
				ps.setString(4, attendance.getWork_hour());
				ps.setTimestamp(5, fixed_time);
				//2020/10/06 GICM KZP シフト勤務　 対応 Start
				ps.setInt(6, attendance.getShift_work());
				//2020/10/06 GICM KZP シフト勤務　 対応 End
				ps.setString(7, attendance.getEmp_cd());
				
				status = ps.executeUpdate();
				
				if(status==1){
					addActionMessage("更新しました。");
					clear();
				}				
			}
			
		}catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}		
		return SUCCESS;
	}
	
	/**
	 * クリア処理
	 * @return
	 */
	public String clear(){
		// 権限チェック
		if (sessionmap.get("ID") == null) {
			return "session";
		} else if(!CommonCheck.isValidRole(CommonConstant.SCREEN_NO.ATTENDANCE_SETTING.value, 
				sessionmap.get("ID").toString())) {
			return "role";
		}
		
		attendance.setStart_time("");
		attendance.setEnd_time("");
		attendance.setBreak_time("");
		attendance.setWork_hour("");
		//2020/10/06 GICM KZP シフト勤務　 対応 Start
		attendance.setShift_work(0);
		//2020/10/06 GICM KZP シフト勤務　 対応 End

		return SUCCESS;
	}

	@Override
	public void setSession(Map<String, Object> session) {
		this.sessionmap = (SessionMap)session;
	}
}
