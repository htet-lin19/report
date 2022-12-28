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
import com.model.GroupListFormDetail;
import com.opensymphony.xwork2.ActionSupport;

public class GroupEntryAction extends ActionSupport implements SessionAware{

	private static final long serialVersionUID = 1L;
	
	private GroupListFormDetail groupEntryFrm = new GroupListFormDetail();
	
	SessionMap<String, Object> sessionmap;

	/**
	 * 初期表示
	 */
	public String execute() {
		// 権限チェック
		if (sessionmap.get("ID") == null) {
			return "session";
		} else if(!CommonCheck.isValidRole(CommonConstant.SCREEN_NO.GROUP_ENTRY.value, 
				sessionmap.get("ID").toString())) {
			return "role";
		}
		
		return SUCCESS;
	}
	
	/**
	 * 登録処理
	 * @return
	 */
	public String insert(){
		// 権限チェック
		if (sessionmap.get("ID") == null) {
			return "session";
		} else if(!CommonCheck.isValidRole(CommonConstant.SCREEN_NO.GROUP_ENTRY.value, 
				sessionmap.get("ID").toString())) {
			return "role";
		}
				
		try{
			Connection conn = null;
			ResultSet rs;
			int status = 0;
			
			conn = DataConnection.getConnection();
			Timestamp time_stamp = new Timestamp(new Date().getTime());
			
			String query2="SELECT * FROM groupmaster WHERE group_cd='"+groupEntryFrm.getGroup_cd()+"'";
			PreparedStatement ps2 = conn.prepareStatement(query2);
			rs=ps2.executeQuery();
			
			if(!rs.next())
			{
				String query = "INSERT INTO groupmaster VALUES(?,?,?,?,?)";
				PreparedStatement ps = conn.prepareStatement(query); 
				ps.setInt(1,groupEntryFrm.getId());
				ps.setString(2, groupEntryFrm.getGroup_cd());
				ps.setString(3, groupEntryFrm.getGroup_name());
				ps.setTimestamp(4, time_stamp);
				ps.setTimestamp(5, time_stamp);
				status = ps.executeUpdate();
			}
			if(status==1){
				 addActionMessage("登録しました。");
				 groupEntryFrm.setGroup_cd("");
				 groupEntryFrm.setGroup_name("");
			}
			else{
				 addActionError("『グループ番号』が重複になっています");
			}					
		}
		catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}		
		return SUCCESS;
	}
	
	public GroupListFormDetail getGroupEntryFrm() {
		return groupEntryFrm;
	}
	
	public void setGroupEntryFrm(GroupListFormDetail groupEntryFrm) {
		this.groupEntryFrm = groupEntryFrm;
	}
	
	@Override
	public void setSession(Map<String, Object> session) {
		this.sessionmap = (SessionMap)session;
	}
}
