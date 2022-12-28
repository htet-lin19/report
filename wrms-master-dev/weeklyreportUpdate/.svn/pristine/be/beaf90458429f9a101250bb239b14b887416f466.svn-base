package com.action;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import org.apache.struts2.dispatcher.SessionMap;
import org.apache.struts2.interceptor.SessionAware;

import com.common.CommonCheck;
import com.common.CommonConstant;
import com.jdbc.DataConnection;
import com.model.EmployeeMasterFormDetail;
import com.model.GroupListFormDetail;
import com.opensymphony.xwork2.ActionSupport;

public class EmployeeMasterAction extends ActionSupport implements SessionAware{
	
	private static final long serialVersionUID = 1L;
	EmployeeMasterFormDetail employeeMaster=new EmployeeMasterFormDetail();
	SessionMap<String, Object> sessionmap;
	
	public EmployeeMasterFormDetail getEmployeeMaster() {
		return employeeMaster;
	}

	public void setEmployeeMaster(EmployeeMasterFormDetail employeeMaster) {
		this.employeeMaster = employeeMaster;
	}

	/**
	 * 初期表示
	 */
	public String execute() {
		// 権限チェック
		if (sessionmap.get("ID") == null) {
			return "session";
		} else if(!CommonCheck.isValidRole(CommonConstant.SCREEN_NO.EMPLOYEE_ENTRY.value, 
				sessionmap.get("ID").toString())) {
			return "role";
		}
		
		getGroupList();
		return SUCCESS;
	}
	
	/**
	 * 保存処理
	 * @return
	 */
	public String save() {
		// 権限チェック
		if (sessionmap.get("ID") == null) {
			return "session";
		} else if(!CommonCheck.isValidRole(CommonConstant.SCREEN_NO.EMPLOYEE_ENTRY.value, 
				sessionmap.get("ID").toString())) {
			return "role";
		}
		
		Connection conn = null;
		ResultSet rs;
		int status = 0;
		try {
			conn = DataConnection.getConnection();
			// 作成日
			Timestamp time_stamp = new Timestamp(new Date().getTime());
			conn = DataConnection.getConnection();
			String query1="SELECT * FROM employeemaster WHERE emp_cd='"+employeeMaster.getEmp_cd()+"'";
			PreparedStatement ps1 = conn.prepareStatement(query1);
			rs=ps1.executeQuery();
			if(!rs.next()){
				String query = "INSERT INTO employeemaster(id,emp_cd,emp_name,position,created_date,modified_date,group_cd,gl_flag,emp_email) VALUES(?,?,?,?,?,?,?,?,?)";
				PreparedStatement ps = conn.prepareStatement(query); 
				ps.setInt(1, employeeMaster.getId());
				ps.setString(2, employeeMaster.getEmp_cd());
				ps.setString(3, employeeMaster.getEmp_name());
				ps.setString(4, employeeMaster.getPosition());
				ps.setTimestamp(5, time_stamp);
				ps.setTimestamp(6, time_stamp);
				if("-1".equals(employeeMaster.getGroup_cd())) {
					ps.setNull(7, Types.VARCHAR);
				} else {
					ps.setString(7,employeeMaster.getGroup_cd());
				}
				// GL フラグ
				if("true".equals(employeeMaster.getGl_flag())) {
					ps.setString(8, "1");
				} else {
					ps.setNull(8, Types.VARCHAR);
				}
				ps.setString(9, employeeMaster.getEmp_mail());
				status = ps.executeUpdate();
			}			
			if(status==1){	
				addActionMessage("登録しました。");
				employeeMaster.setEmp_cd("");
				employeeMaster.setEmp_name("");
				employeeMaster.setPosition("");
				employeeMaster.setGroup_cd("");
				employeeMaster.setGl_flag("");
				employeeMaster.setEmp_mail("");
			}
			else {	
				addActionError("『社員番号 』が重複になっています。");
				employeeMaster.setPosition(employeeMaster.getPosition());
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}
		getGroupList();
		
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
		} else if(!CommonCheck.isValidRole(CommonConstant.SCREEN_NO.EMPLOYEE_ENTRY.value, 
				sessionmap.get("ID").toString())) {
			return "role";
		}
				
		employeeMaster.setEmp_cd("");
		employeeMaster.setEmp_name("");
		employeeMaster.setPosition("");
		employeeMaster.setGl_flag("");
		employeeMaster.setEmp_mail("");
		employeeMaster.setGroup_cd("-1");
		getGroupList();
		return SUCCESS;
	}
	
	/**
	 * グループ名リスト取得
	 */
	private void getGroupList(){
		ArrayList<GroupListFormDetail> groupList = new ArrayList<GroupListFormDetail>();
		try {
			Connection conn = DataConnection.getConnection();
			String sql = "select group_cd, group_name from groupmaster";
			PreparedStatement ps1 = conn.prepareStatement(sql);
			ResultSet rs = ps1.executeQuery();
			while(rs.next()){
				GroupListFormDetail detail = new GroupListFormDetail();
				detail.setGroup_cd(rs.getString("group_cd"));
				detail.setGroup_name(rs.getString("group_name"));
				groupList.add(detail);
			}
			rs.close();
			ps1.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		employeeMaster.setGroupList(groupList);
	}

	@Override
	public void setSession(Map<String, Object> session) {
		this.sessionmap = (SessionMap)session;
	}
}

