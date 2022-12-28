package com.action;

import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.dispatcher.SessionMap;
import org.apache.struts2.interceptor.SessionAware;

import com.common.CommonCheck;
import com.common.CommonConstant;
import com.jdbc.DataConnection;
import com.model.SignUpForm;
import com.opensymphony.xwork2.ActionSupport;

public class SignUpListAction extends ActionSupport implements SessionAware{

	private static final long serialVersionUID = 1L;
	private SignUpForm signUpForm = new SignUpForm();
	private SignUpForm signUpListtEidtFrm = new SignUpForm();
	SessionMap<String, Object> sessionmap;
	
	/**
	 * 初期表示
	 * @return
	 */
	public String execute() {
		// 権限チェック
		if (sessionmap.get("ID") == null) {
			return "session";
		} else if(!CommonCheck.isValidRole(CommonConstant.SCREEN_NO.SIGNUP_LIST.value, 
				sessionmap.get("ID").toString())) {
			return "role";
		}
		
		sessionmap.remove("signUplist_emp_cd");
		sessionmap.remove("signUplist_emp_name");
		
		return SUCCESS;
	}
	
	/**
	 * 検索処理
	 * @return
	 */
	public String search() {
		// 権限チェック
		if (sessionmap.get("ID") == null) {
			return "session";
		} else if(!CommonCheck.isValidRole(CommonConstant.SCREEN_NO.SIGNUP_LIST.value, 
				sessionmap.get("ID").toString())) {
			return "role";
		}
		
		String emp_cd="", emp_name="";
		
		if ("search".equals(signUpForm.getButton_event())) {
			emp_cd = signUpForm.getEmp_cd();
			emp_name = signUpForm.getEmp_name();
		}else {
			// セッションに設定した内容を取得する。
			if(sessionmap.containsKey("signUplist_emp_cd")) {
				emp_cd = (String) sessionmap.get("signUplist_emp_cd");
			}
			if(sessionmap.containsKey("signUplist_emp_name")) {
				emp_name = (String) sessionmap.get("signUplist_emp_name");
			}
		}
		
		String sql = "SELECT login.id, login.emp_cd, employeemaster.emp_name, "
				+ "login.password FROM login, employeemaster "
				+ "WHERE login.emp_cd = employeemaster.emp_cd";
		
		if(StringUtils.isNotBlank(emp_cd)) {
			sql = sql + " AND login.emp_cd = '"+emp_cd+"'";
		} 
		
		if(StringUtils.isNotBlank(emp_name)) {
			sql = sql + " AND employeemaster.emp_name LIKE '%"+emp_name+"%'";
		} 
	
		List<SignUpForm> signUpFormList = new ArrayList<SignUpForm>();
		Connection conn;
		try {
			conn = DataConnection.getConnection();
			PreparedStatement ps1 = conn.prepareStatement(sql);
			ResultSet rs = ps1.executeQuery();
			
			while(rs.next()) {
				SignUpForm detail = new SignUpForm();
				detail.setId(rs.getInt("id"));
				detail.setEmp_cd(rs.getString("emp_cd"));
				detail.setEmp_name(rs.getString("emp_name"));
				detail.setPassword(rs.getString("password"));
				signUpFormList.add(detail);
			}
			
			if(signUpFormList.size() > 0) {
				signUpForm.setSignUpFormList(signUpFormList);
				signUpForm.setSearch("1");
			}else {
				signUpForm.setSearch("");
				addActionMessage("検索データがありません。");
			}
			
			signUpForm.setEmp_cd(emp_cd);
			signUpForm.setEmp_name(emp_name);
			
			// セッションに検索情報を設定する
			sessionmap.put("signUplist_emp_cd", emp_cd);
			sessionmap.put("signUplist_emp_name", emp_name);
		} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}
		return SUCCESS;
	}
	
	/**
	 * 削除処理
	 * @return
	 */
	public String delete() {
		// 権限チェック
		if (sessionmap.get("ID") == null) {
			return "session";
		} else if(!CommonCheck.isValidRole(CommonConstant.SCREEN_NO.SIGNUP_LIST.value, 
				sessionmap.get("ID").toString())) {
			return "role";
		}
		
		int id = signUpForm.getDelete_id();
		
		// 削除処理
		String sql = "DELETE FROM login WHERE id = ?";
		Connection conn;
		try {
			conn = DataConnection.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			
			ps.setInt(1, id);
			ps.executeUpdate();	
		} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}
		
		// データベースから再取得
		search();
		
		return SUCCESS;
	}
	
	/**
	 * 更新処理
	 * @return
	 * @throws NoSuchAlgorithmException 
	 */
	public String edit() throws NoSuchAlgorithmException {
		// 権限チェック
		if (sessionmap.get("ID") == null) {
			return "session";
		} else if(!CommonCheck.isValidRole(CommonConstant.SCREEN_NO.SIGNUP_LIST.value, 
				sessionmap.get("ID").toString())) {
			return "role";
		}
		
		int id = signUpListtEidtFrm.getEdit_id();
		String emp_name = signUpListtEidtFrm.getEmp_name();
		String password = new CommonCheck().decryptPassword(signUpListtEidtFrm.getPassword());
		
		// 更新処理
		String sql = "UPDATE login SET emp_name = ?, password = ? WHERE id = ?";
		Connection conn;
		try {
			conn = DataConnection.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			
			ps.setString(1, emp_name);
			ps.setString(2, password);
			ps.setInt(3, id);
			
			ps.executeUpdate();	
		} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
			
		}
		
		// データベースから再取得
		search();
		
		return SUCCESS;
	}
	
	public SignUpForm getSignUpForm() {
		return signUpForm;
	}

	public void setSignUpForm(SignUpForm signUpForm) {
		this.signUpForm = signUpForm;
	}
	
	public SignUpForm getSignUpListtEidtFrm() {
		return signUpListtEidtFrm;
	}

	public void setSignUpListtEidtFrm(SignUpForm signUpListtEidtFrm) {
		this.signUpListtEidtFrm = signUpListtEidtFrm;
	}
	
	@Override
	public void setSession(Map<String, Object> session) {
		this.sessionmap = (SessionMap)session;
	}
}
