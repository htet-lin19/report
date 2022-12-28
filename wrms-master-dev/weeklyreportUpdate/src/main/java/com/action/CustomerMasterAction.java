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
import com.model.CustomerMaster;
import com.opensymphony.xwork2.ActionSupport;

public class CustomerMasterAction extends ActionSupport implements SessionAware{
	
	private static final long serialVersionUID = 1L;
	CustomerMaster customerEntryFrm=new CustomerMaster();
	SessionMap<String, Object> sessionmap;

	/**
	 * 初期表示
	 */
	public String execute() {
		// 権限チェック
		if (sessionmap.get("ID") == null) {
			return "session";
		} else if(!CommonCheck.isValidRole(CommonConstant.SCREEN_NO.CUSTOMER_ENTRY.value, 
				sessionmap.get("ID").toString())) {
			return "role";
		}
		return SUCCESS;
	}
	
	/**
	 * 登録処理
	 * @return
	 */
	public String save() {
		try {
			// 権限チェック
			if (sessionmap.get("ID") == null) {
				return "session";
			} else if(!CommonCheck.isValidRole(CommonConstant.SCREEN_NO.CUSTOMER_ENTRY.value, 
					sessionmap.get("ID").toString())) {
				return "role";
			}
			
			Connection conn = null;
			ResultSet rs;
			int status = 0;
			
			conn = DataConnection.getConnection();
			Timestamp sqlDate = new Timestamp(new Date().getTime());
			String query2="SELECT * FROM customermaster WHERE customer_cd='"+customerEntryFrm.getCustomer_cd()+"'";
			PreparedStatement ps2 = conn.prepareStatement(query2);
			rs=ps2.executeQuery();
			if(!rs.next()){
				String query = "INSERT INTO customermaster(customer_cd, customer_name, email, "
						+ "phone, address, created_date, modified_date) "
						+ "VALUES(?,?,?,"
						+ "?,?,?,?)";
				PreparedStatement ps = conn.prepareStatement(query); 
				
				ps.setString(1, customerEntryFrm.getCustomer_cd());
				ps.setString(2, customerEntryFrm.getCustomer_name());
				ps.setString(3, customerEntryFrm.getEmail());
				ps.setString(4, customerEntryFrm.getPhone());
				ps.setString(5, customerEntryFrm.getAddress());
				ps.setTimestamp(6, sqlDate);
				ps.setTimestamp(7, sqlDate);
	
				status = ps.executeUpdate();
			}
			if(status==1){					
				addActionMessage("登録しました。");
				customerEntryFrm.setCustomer_cd("");
				customerEntryFrm.setCustomer_name("");
				customerEntryFrm.setEmail("");
				customerEntryFrm.setPhone("");
				customerEntryFrm.setAddress("");					  
			}			     
			else{			    	
			    addActionError("『お客様番号』が重複になっています");
			}			
		} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}		
		return SUCCESS;		
	}
	
	public CustomerMaster getCustomerEntryFrm() {
		return customerEntryFrm;
	}

	public void setCustomerEntryFrm(CustomerMaster customerEntryFrm) {
		this.customerEntryFrm = customerEntryFrm;
	}

	@Override
	public void setSession(Map<String, Object> session) {
		this.sessionmap = (SessionMap)session;
	}
}
