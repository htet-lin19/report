package com.action;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.dispatcher.SessionMap;
import org.apache.struts2.interceptor.SessionAware;

import com.common.CommonCheck;
import com.common.CommonConstant;
import com.jdbc.DataConnection;
import com.model.CustomerMaster;
import com.opensymphony.xwork2.ActionSupport;

public class CustomerListAction extends ActionSupport implements SessionAware{
	
	private static final long serialVersionUID = 1L;
	private CustomerMaster customerListFrm = new CustomerMaster();
	private CustomerMaster customerListEditFrm = new CustomerMaster();
	SessionMap<String, Object> sessionmap;
	
	/**
	 * 初期表示
	 * @return
	 */
	public String execute() {
		// 権限チェック
		if (sessionmap.get("ID") == null) {
			return "session";
		} else if(!CommonCheck.isValidRole(CommonConstant.SCREEN_NO.CUSTOMER_LIST.value, 
				sessionmap.get("ID").toString())) {
			return "role";
		}
		sessionmap.remove("customerlist_customer_cd");
		sessionmap.remove("customerlist_customer_name");
		
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
		} else if(!CommonCheck.isValidRole(CommonConstant.SCREEN_NO.CUSTOMER_LIST.value, 
				sessionmap.get("ID").toString())) {
			return "role";
		}
		
		String customer_cd = "", customer_name="";
		if ("search".equals(customerListFrm.getButton_event())) {
			// お客様番号
			customer_cd = customerListFrm.getCustomer_cd();
			// お客様名
			customer_name = customerListFrm.getCustomer_name();
		} else {
			// セッションに設定した内容を取得する。
			if(sessionmap.containsKey("customerlist_customer_cd")) {
				customer_cd = (String) sessionmap.get("customerlist_customer_cd");
			}
			if(sessionmap.containsKey("customerlist_customer_name")) {
				customer_name = (String) sessionmap.get("customerlist_customer_name");
			}
		}
		
		String sql = "SELECT * FROM customermaster ";
		boolean sqlExists = false;

		if(StringUtils.isNotEmpty(customer_cd)) {
			sql = sql + " WHERE customermaster.customer_cd LIKE '"+customer_cd+"%'";
			sqlExists = true;
		}
		
		if(sqlExists && StringUtils.isNotEmpty(customer_name)) {
			sql = sql + " AND customermaster.customer_name LIKE '%"+customer_name+"%'";
		} else if(StringUtils.isNotEmpty(customer_name)) {
			sql = sql + " WHERE customermaster.customer_name LIKE '%"+customer_name+"%'";
		}
		
		try {
			List<CustomerMaster> customersList = new ArrayList<CustomerMaster>();
			Connection conn = DataConnection.getConnection();
			
			PreparedStatement ps1 = conn.prepareStatement(sql);
			ResultSet rs = ps1.executeQuery();
			while(rs.next()){
				CustomerMaster cus = new CustomerMaster();
				cus.setId(rs.getInt("id"));
				cus.setCustomer_cd(rs.getString("customer_cd"));
				cus.setCustomer_name(rs.getString("customer_name"));
				cus.setEmail(rs.getString("email"));
				cus.setPhone(rs.getString("phone"));
				cus.setAddress(rs.getString("address"));
				
				customersList.add(cus);
			}

			rs.close();
			ps1.close();
			
			if(customersList.size() > 0) {
				customerListFrm.setSearch("1");
			} else {
				// 検索データなしの場合
				customerListFrm.setSearch("");
				addActionMessage("検索データがありません。");
			}
			
			customerListFrm.setCustomer_cd(customer_cd);
			customerListFrm.setCustomer_name(customer_name);
			customerListFrm.setCustomersList(customersList);
			
		} catch (Exception e) {
			e.printStackTrace();
			return  ERROR;
		}
		return SUCCESS;
	}

	/**
	 * 更新処理
	 * @return
	 */
	public String edit(){
		try{
			// 権限チェック
			if (sessionmap.get("ID") == null) {
				return "session";
			} else if(!CommonCheck.isValidRole(CommonConstant.SCREEN_NO.CUSTOMER_LIST.value, 
					sessionmap.get("ID").toString())) {
				return "role";
			}
			
			// Created Date
			Timestamp fixed_time = new Timestamp(new Date().getTime());
			
			Connection conn = DataConnection.getConnection();
			String sql = "UPDATE customermaster SET customer_name = ?, email = ?, phone = ?, address = ?, modified_date = ? WHERE id = ?";
			
			PreparedStatement ps1 = conn.prepareStatement(sql);
			ps1.setString(1, customerListEditFrm.getCustomer_name().trim());
			if(StringUtils.isNotBlank(customerListEditFrm.getEmail())) {
				ps1.setString(2, customerListEditFrm.getEmail().trim());
			} else {
				ps1.setNull(2, Types.NULL);
			}
			if(StringUtils.isNotBlank(customerListEditFrm.getPhone())) {
				ps1.setString(3, customerListEditFrm.getPhone().trim());
			} else {
				ps1.setNull(3, Types.NULL);
			}
			if(StringUtils.isNotBlank(customerListEditFrm.getAddress())) {
				ps1.setString(4, customerListEditFrm.getAddress().trim());
			} else {
				ps1.setNull(4, Types.NULL);
			}
			ps1.setTimestamp(5, fixed_time);
			ps1.setInt(6, customerListEditFrm.getId());

			int value = ps1.executeUpdate();
			if (!(value == 1)) {
				addActionMessage("更新しました。");
			}
			ps1.close();
		}
		catch(Exception e){
            e.printStackTrace();
            return ERROR;    
        }
		return search();
	}
	
	/**
	 * 削除処理
	 * @return
	 */
	public String delete() {
		try {
			// 権限チェック
			if (sessionmap.get("ID") == null) {
				return "session";
			} else if(!CommonCheck.isValidRole(CommonConstant.SCREEN_NO.CUSTOMER_LIST.value, 
					sessionmap.get("ID").toString())) {
				return "role";
			}
			
			int id = customerListFrm.getDelete_id();
			
			Connection conn = DataConnection.getConnection();
			String sql = "DELETE FROM customermaster WHERE id = ?";
			PreparedStatement ps1 = conn.prepareStatement(sql);
			ps1.setInt(1, id);
			int value = ps1.executeUpdate();
			if (!(value == 1)) {
				addActionMessage("削除しました。");
			}
			ps1.close();
		} 
		catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		} 
		return search();
	}
	
	public CustomerMaster getCustomerListFrm() {
		return customerListFrm;
	}
	public void setCustomerListFrm(CustomerMaster customerListFrm) {
		this.customerListFrm = customerListFrm;
	}
	public CustomerMaster getCustomerListEditFrm() {
		return customerListEditFrm;
	}
	public void setCustomerListEditFrm(CustomerMaster customerListEditFrm) {
		this.customerListEditFrm = customerListEditFrm;
	}

	@Override
	public void setSession(Map<String, Object> session) {
		this.sessionmap = (SessionMap)session;
	}
}
