package com.common;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.struts2.dispatcher.SessionMap;

import com.jdbc.DataConnection;

public class CommonCheck {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	static SessionMap<String, Object> sessionmap;
	
	/**
	 * 権限チェック
	 * @param screen_id
	 * @return
	 */
	public static boolean isValidRole(String screen_id, String emp_cd) {
		Connection conn = null;
		int count = 0;
		try {
			conn = DataConnection.getConnection();
			String query="SELECT COUNT(rol.role_id) FROM role_screen rol, employeemaster emp "
					+ "WHERE emp.position = rol.role_id "
					+ "AND rol.screen_id = ? "
					+ "AND emp.emp_cd = ?";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, screen_id);
			ps.setString(2, emp_cd);
			
			ResultSet rs=ps.executeQuery();
			
			if (rs.next()) {
				count = rs.getInt(1);
			}
			
			if (count > 0) {
				return true;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
	}
	
	public String decryptPassword(String input) throws NoSuchAlgorithmException{
		String result = input;
	    if(input != null) {
	        MessageDigest md = MessageDigest.getInstance("MD5"); //or "SHA-1"
	        md.update(input.getBytes());
	        BigInteger hash = new BigInteger(1, md.digest());
	        result = hash.toString(16);
	        while(result.length() < 32) { //40 for SHA-1
	            result = "0" + result;
	        }
	    }
	    return result;
	}
}
