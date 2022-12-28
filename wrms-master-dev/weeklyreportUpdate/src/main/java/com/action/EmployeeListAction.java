package com.action;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.struts2.dispatcher.SessionMap;
import org.apache.struts2.interceptor.SessionAware;

import com.common.CommonCheck;
import com.common.CommonConstant;
import com.jdbc.DataConnection;
import com.model.EmployeeMasterForm;
import com.model.EmployeeMasterFormDetail;
import com.model.GroupListFormDetail;
import com.opensymphony.xwork2.ActionSupport;

public class EmployeeListAction extends ActionSupport implements SessionAware{

	private static final long serialVersionUID = 1L;
	EmployeeMasterForm employeeMasterFrm = new EmployeeMasterForm();
	EmployeeMasterFormDetail employeeMasterFrmDetail = new EmployeeMasterFormDetail();
	SessionMap<String, Object> sessionmap;
	
	/**
	 * 初期表示
	 * @return
	 */
	public String execute() {
		// 権限チェック
		if (sessionmap.get("ID") == null) {
			return "session";
		} else if(!CommonCheck.isValidRole(CommonConstant.SCREEN_NO.EMPLOYEE_LIST.value, 
				sessionmap.get("ID").toString())) {
			return "role";
		}
		
		sessionmap.remove("employeelist_emp_cd");
		sessionmap.remove("employeelist_emp_name");
		
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
		} else if(!CommonCheck.isValidRole(CommonConstant.SCREEN_NO.EMPLOYEE_LIST.value, 
				sessionmap.get("ID").toString())) {
			return "role";
		}
				
		String emp_cd = "", emp_name = "";
		
		if ("search".equals(employeeMasterFrm.getButton_event())) {
			// 社員番号
			emp_cd = employeeMasterFrm.getEmp_cd();
			// 社員名
			emp_name = employeeMasterFrm.getEmp_name();
		} else {
			// セッションに設定した内容を取得する。
			if(sessionmap.containsKey("employeelist_emp_cd")) {
				emp_cd = (String) sessionmap.get("employeelist_emp_cd");
			}
			if(sessionmap.containsKey("employeelist_emp_name")) {
				emp_name = (String) sessionmap.get("employeelist_emp_name");
			}
		}
		
		try {
			List<EmployeeMasterFormDetail> listEmployee = new ArrayList<EmployeeMasterFormDetail>();
			Connection conn = DataConnection.getConnection();
			String sql = "SELECT employeemaster.id, employeemaster.group_cd, employeemaster.emp_cd, employeemaster.emp_name, employeemaster.gl_flag,employeemaster.emp_email, "
					+ " groupmaster.group_name, role.role_name position, role.role_cd role "
					+ " FROM role, employeemaster LEFT OUTER JOIN groupmaster ON employeemaster.group_cd=groupmaster.group_cd "
					+ " WHERE role.role_cd = employeemaster.position";
			String condition = "";
			if(emp_cd != null && !emp_cd.isEmpty()){
				condition = " AND emp_cd LIKE '"+emp_cd+"%'";
			}
			if(emp_name != null && !emp_name.isEmpty()){
				condition += " AND emp_name LIKE '%"+emp_name+"%'";
			}
			sql += condition;
			PreparedStatement ps1 = conn.prepareStatement(sql);
			ResultSet rs = ps1.executeQuery();
			while(rs.next()){
				EmployeeMasterFormDetail emp = new EmployeeMasterFormDetail();
				emp.setId(rs.getInt("id"));
				emp.setEmp_cd(rs.getString("emp_cd"));
				emp.setEmp_name(rs.getString("emp_name"));
				emp.setGroup_cd(rs.getString("group_cd"));
				emp.setGroup_name(rs.getString("group_name"));
				emp.setPosition(rs.getString("position"));
				emp.setRole(rs.getString("role"));
				emp.setGl_flag(rs.getString("gl_flag"));
				emp.setEmp_mail(rs.getString("emp_email"));
				
				listEmployee.add(emp);
			}
			
			sql = "SELECT group_cd, group_name FROM groupmaster";
			ps1 = conn.prepareStatement(sql);
			rs = ps1.executeQuery();
			ArrayList<GroupListFormDetail> groupList = new ArrayList<GroupListFormDetail>();
			while(rs.next()){
				GroupListFormDetail group = new GroupListFormDetail();
				group.setGroup_cd(rs.getString("group_cd"));
				group.setGroup_name(rs.getString("group_name"));
				groupList.add(group);
			}
			rs.close();
			ps1.close();
			
			if(listEmployee.size() > 0) {
				employeeMasterFrm.setSearch("1");
			}else {
				// 検索データなしの場合
				employeeMasterFrm.setSearch("");
				addActionMessage("�����f�[�^������܂���B");
			}
			
			employeeMasterFrmDetail.setGroupList(groupList);
			employeeMasterFrm.setEmployeeFrmDetail(listEmployee);
			
			// セッションに検索情報を設定する
			sessionmap.put("employeelist_emp_cd", emp_cd);
			sessionmap.put("employeelist_emp_name", emp_name);
			
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return  ERROR;
		}
	}
	
	/**
	 * 更新処理
	 * @return
	 * @throws Exception
	 */
	public String edit() throws Exception {
		// 権限チェック
		if (sessionmap.get("ID") == null) {
			return "session";
		} else if(!CommonCheck.isValidRole(CommonConstant.SCREEN_NO.EMPLOYEE_LIST.value, 
				sessionmap.get("ID").toString())) {
			return "role";
		}
		
		int id = employeeMasterFrmDetail.getId();
		String emp_name = employeeMasterFrmDetail.getEmp_name();
		String group_cd = employeeMasterFrmDetail.getGroup_cd();
		String position = employeeMasterFrmDetail.getRole();
		String gl_flag = employeeMasterFrmDetail.getGl_flag();
		String emp_email = employeeMasterFrmDetail.getEmp_mail();
		
		String sql = "UPDATE employeemaster SET emp_name = ?, "
				+ "position = ?, "
				+ "group_cd = ?, "
				+ "gl_flag =?, "
				+ "modified_date = CURDATE(), "
				+ "emp_email = ? "
				+ "WHERE id = ?";
		
		Connection conn;
		try {
			conn = DataConnection.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			
			ps.setString(1, emp_name);
			ps.setString(2, position);
			ps.setString(3, group_cd);
			
			if("true".equals(gl_flag)) {
				ps.setString(4, "1");
			} else {
				ps.setNull(4, Types.VARCHAR);
			}
			ps.setString(5, emp_email);
			ps.setInt(6, id);
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
	 * 削除処理
	 * @return
	 */
	public String delete() {
		// 削除処理権限チェック
		if (sessionmap.get("ID") == null) {
			return "session";
		} else if(!CommonCheck.isValidRole(CommonConstant.SCREEN_NO.EMPLOYEE_LIST.value, 
				sessionmap.get("ID").toString())) {
			return "role";
		}
		
		int id = employeeMasterFrm.getDelete_id();
		
		String sql = "DELETE FROM employeemaster WHERE id = ?";
		Connection conn;
		try {
			conn = DataConnection.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, id);
			int value = ps.executeUpdate();
			if (!(value == 1)) {
				addActionMessage("削除しました。");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}
		
		// データベースから再取得
		search();
		
		return SUCCESS;
	}

	public EmployeeMasterForm getEmployeeMasterFrm() {
		return employeeMasterFrm;
	}

	public void setEmployeeMasterFrm(EmployeeMasterForm employeeMasterFrm) {
		this.employeeMasterFrm = employeeMasterFrm;
	}

	public EmployeeMasterFormDetail getEmployeeMasterFrmDetail() {
		return employeeMasterFrmDetail;
	}

	public void setEmployeeMasterFrmDetail(EmployeeMasterFormDetail employeeMasterFrmDetail) {
		this.employeeMasterFrmDetail = employeeMasterFrmDetail;
	}

	public SessionMap<String, Object> getSessionmap() {
		return sessionmap;
	}

	public void setSessionmap(SessionMap<String, Object> sessionmap) {
		this.sessionmap = sessionmap;
	}

	@Override
	public void setSession(Map<String, Object> session) {
		this.sessionmap = (SessionMap)session;
	}
}
