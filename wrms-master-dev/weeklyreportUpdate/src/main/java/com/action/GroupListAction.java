package com.action;

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
import com.model.GroupListForm;
import com.model.GroupListFormDetail;
import com.opensymphony.xwork2.ActionSupport;

public class GroupListAction extends ActionSupport implements SessionAware {

	private static final long serialVersionUID = 1L;
	GroupListForm groupListFrm = new GroupListForm();
	GroupListFormDetail groupListEidtFrm = new GroupListFormDetail();
	SessionMap<String, Object> sessionmap;
	
	/**
	 * 初期表示
	 * @return
	 */
	public String execute() {
		// 権限チェック
		if (sessionmap.get("ID") == null) {
			return "session";
		} else if(!CommonCheck.isValidRole(CommonConstant.SCREEN_NO.GROUP_LIST.value, sessionmap.get("ID").toString())) {
			return "role";
		}
		
		sessionmap.remove("grouplist_group_cd");
		sessionmap.remove("grouplist_group_name");
		
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
		} else if(!CommonCheck.isValidRole(CommonConstant.SCREEN_NO.GROUP_LIST.value, sessionmap.get("ID").toString())) {
			return "role";
		}
		
		String group_cd = "", group_name="";
		if ("search".equals(groupListFrm.getButton_event())) {
			// グループ番号
			group_cd = groupListFrm.getGroup_cd();
			// グループ名
			group_name = groupListFrm.getGroup_name();
		} else {
			// セッションに設定した内容を取得する。
			if(sessionmap.containsKey("grouplist_group_cd")) {
				group_cd = (String) sessionmap.get("grouplist_group_cd");
			}
			if(sessionmap.containsKey("grouplist_group_name")) {
				group_name = (String) sessionmap.get("grouplist_group_name");
			}
		}
		
		String sql = "SELECT * FROM groupmaster";
		boolean sqlExists = false;

		if(StringUtils.isNotEmpty(group_cd)) {
			sql = sql + " WHERE groupmaster.group_cd LIKE '"+group_cd+"%'";
			sqlExists = true;
		}
		
		if(sqlExists && StringUtils.isNotEmpty(group_name)) {
			sql = sql + " AND groupmaster.group_name LIKE '%"+group_name+"%'";
		} else if(StringUtils.isNotEmpty(group_name)) {
			sql = sql + " WHERE groupmaster.group_name LIKE '%"+group_name+"%'";
		}
		
		Connection conn;
		try {
			conn = DataConnection.getConnection();
			PreparedStatement ps1 = conn.prepareStatement(sql);
			ResultSet rs = ps1.executeQuery();
			
			List<GroupListFormDetail> detailList = new ArrayList<GroupListFormDetail>(); 
			
			while(rs.next()){
				GroupListFormDetail detail = new GroupListFormDetail();
				
				detail.setId(rs.getInt("id"));
				detail.setGroup_cd(rs.getString("group_cd"));
				detail.setGroup_name(rs.getString("group_name"));
				
				detailList.add(detail);
			}
			
			if(detailList.size() > 0) {
				groupListFrm.setSearch("1");
			} else {
				groupListFrm.setSearch("");
				addActionMessage("検索データがありません。");
			}
			
			groupListFrm.setGroupListDetail(detailList);
			groupListFrm.setGroup_cd(group_cd);
			groupListFrm.setGroup_name(group_name);
			
			// セッションに検索情報を設定する
			sessionmap.put("grouplist_group_cd", group_cd);
			sessionmap.put("grouplist_group_name", group_name);
		} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}
		
		return SUCCESS;
	}
	
	/**
	 * 更新処理
	 * @return
	 */
	public String edit() {
		// 権限チェック
		if (sessionmap.get("ID") == null) {
			return "session";
		} else if(!CommonCheck.isValidRole(CommonConstant.SCREEN_NO.GROUP_LIST.value, sessionmap.get("ID").toString())) {
			return "role";
		}
		
		int id = groupListEidtFrm.getId();
		String group_name = groupListEidtFrm.getGroup_name();
		
		String sql = "UPDATE groupmaster SET group_name = ?, "
				+ "modified_date = CURDATE() "
				+ "WHERE id = ?";
		
		Connection conn;
		try {
			conn = DataConnection.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			
			ps.setString(1, group_name);
			ps.setInt(2, id);
			ps.executeUpdate();	 
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// データベースから再取得
		search();
		
		return "success";

	}
	
	/**
	 * 削除処理
	 * @return
	 */
	public String delete() {
		// 権限チェック
		if (sessionmap.get("ID") == null) {
			return "session";
		} else if(!CommonCheck.isValidRole(CommonConstant.SCREEN_NO.GROUP_LIST.value, sessionmap.get("ID").toString())) {
			return "role";
		}
		
		int id = groupListFrm.getDelete_id();
		
		String sql = "DELETE FROM groupmaster WHERE id = ?";
		Connection conn;
		try {
			conn = DataConnection.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			
			ps.setInt(1, id);
			ps.executeUpdate();	
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// データベースから再取得
		search();
		
		return "success";
	}

	public GroupListForm getGroupListFrm() {
		return groupListFrm;
	}

	public void setGroupListFrm(GroupListForm groupListFrm) {
		this.groupListFrm = groupListFrm;
	}

	public GroupListFormDetail getGroupListEidtFrm() {
		return groupListEidtFrm;
	}

	public void setGroupListEidtFrm(GroupListFormDetail groupListEidtFrm) {
		this.groupListEidtFrm = groupListEidtFrm;
	}
	
	@Override
	public void setSession(Map<String, Object> session) {
		this.sessionmap = (SessionMap)session;
	}
}