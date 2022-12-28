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
import com.model.SignUpForm;
import com.opensymphony.xwork2.ActionSupport;

public class SignUpEntryAction extends ActionSupport implements SessionAware{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private SignUpForm signUp = new SignUpForm();
	
	SessionMap<String, Object> sessionmap;

	/**
	 * 初期表示処理
	 */
	public String execute() {
		// 権限チェック
		if (sessionmap.get("ID") == null) {
			return "session";
		} else if(!CommonCheck.isValidRole(CommonConstant.SCREEN_NO.SIGNUP_ENTRY.value, 
				sessionmap.get("ID").toString())) {
			return "role";
		}
		
		signUp.setEmp_cd("");
		signUp.setEmp_name("");
		signUp.setPassword("");
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
		} else if(!CommonCheck.isValidRole(CommonConstant.SCREEN_NO.SIGNUP_ENTRY.value, 
				sessionmap.get("ID").toString())) {
			return "role";
		}
		
		Connection conn = null;
		ResultSet rs;
		int status = 0;
		try {
			conn = DataConnection.getConnection();
			Timestamp time_stamp = new Timestamp(new Date().getTime());
			
			// 重複チェック
			String query1="SELECT login.id, employeemaster.position, employeemaster.emp_name "
					+ "FROM login RIGHT JOIN employeemaster "
					+ "ON login.emp_cd = employeemaster.emp_cd "
					+ "WHERE employeemaster.emp_cd=?";
			
			PreparedStatement ps1 = conn.prepareStatement(query1);
			ps1.setString(1, signUp.getEmp_cd());
			rs=ps1.executeQuery();
			
			if(rs.next()) {
				if(rs.getInt("id") == 0) {
					// ログイン情報がない場合、登録する
					String query = "INSERT INTO login(emp_cd,password,created_date) VALUES(?,?,?)";
					PreparedStatement ps = conn.prepareStatement(query); 
					ps.setString(1, signUp.getEmp_cd());
					ps.setString(2, new CommonCheck().decryptPassword(signUp.getPassword()));
					ps.setTimestamp(3, time_stamp);
		
					status = ps.executeUpdate();
				} else {
					addActionError("社員番号は登録済みです。");
				}
				
				if(status==1){					
					addActionMessage("登録しました。");
					signUp.setEmp_cd("");
					signUp.setEmp_name("");
					signUp.setPassword("");
				}
			} else{			    	
				addActionError("社員情報をまず登録してください。");
			}						
		} 
		catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
		return SUCCESS;
	}
	
	public SignUpForm getSignUp() {
		return signUp;
	}
	
	public void setSignUp(SignUpForm signUp) {
		this.signUp = signUp;
	}

	@Override
	public void setSession(Map<String, Object> session) {
		this.sessionmap = (SessionMap)session;
	}
}
