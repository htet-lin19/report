package com.action;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Map;

import org.apache.struts2.dispatcher.SessionMap;
import org.apache.struts2.interceptor.SessionAware;

import com.common.CommonCheck;
import com.common.CommonConstant;
import com.jdbc.DataConnection;
import com.model.LoginForm;
import com.opensymphony.xwork2.ActionSupport;

public class EmpSignUpAction extends ActionSupport implements SessionAware{
	
	private static final long serialVersionUID = 1L;
	LoginForm loginForm = new LoginForm();
	SessionMap<String, String> sessionmap;
	
	public LoginForm getLoginForm() {
		return loginForm;
	}

	public void setLoginForm(LoginForm loginForm) {
		this.loginForm = loginForm;
	}

	/**
	 * 初期表示処理
	 */
	public String execute() {
		// 権限チェック
		if (sessionmap.get("ID") == null) {
			return "session";
		} else if(!CommonCheck.isValidRole(CommonConstant.SCREEN_NO.EMP_SIGNUP.value, 
				sessionmap.get("ID").toString())) {
			return "role";
		}
		
		loginForm.setPassword("");
		return SUCCESS;
	}
	
	/**
	 * 更新処理
	 * @return
	 */
	public String update(){
		// 権限チェック
		if (sessionmap.get("ID") == null) {
			return "session";
		} else if(!CommonCheck.isValidRole(CommonConstant.SCREEN_NO.EMP_SIGNUP.value, 
				sessionmap.get("ID").toString())) {
			return "role";
		}
		
		Connection dbConnection;
		
		String emp_cd,password,inputpassword;
	    emp_cd = loginForm.getEmp_cd();
	    password=loginForm.getPassword();
	    inputpassword=loginForm.getInputpassword();
	    
		try{			
			dbConnection = DataConnection.getConnection();		 
			String sql = "UPDATE login SET password=? WHERE emp_cd=? AND password=?";
			PreparedStatement pstmt=dbConnection.prepareStatement(sql);
			pstmt.setString(1, new CommonCheck().decryptPassword(inputpassword));
			pstmt.setString(2, emp_cd);
			pstmt.setString(3, new CommonCheck().decryptPassword(password));
			
			int status=pstmt.executeUpdate();
			
			if(status==1){
				return SUCCESS;
			}
			else{
				addActionError("更新できませんでした。");
				return SUCCESS;
			}						 
		 } catch(Exception e){
			e.printStackTrace();
			return ERROR;
	     }
	}

	@Override
	public void setSession(Map<String, Object> session) {
		this.sessionmap = (SessionMap)session;
	}
}
