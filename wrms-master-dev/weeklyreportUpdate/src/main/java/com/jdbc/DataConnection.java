package com.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;

public class DataConnection {
	 public static Connection getConnection() throws Exception {
		 	//String driver = "com.mysql.jdbc.Driver";
		 	String driver = "com.mysql.cj.jdbc.Driver";
		    //String url = "jdbc:mysql://localhost/report_db?characterEncoding=UTF-8&amp;characterSetResults=UTF-8&amp;zeroDateTimeBehavior=convertToNull&amp;noAccessToProcedureBodies=true";
		 	String url = "jdbc:mysql://localhost:3306/report_db_1019?useSSL=false&characterEncoding=UTF-8&characterSetResults=UTF-8&zeroDateTimeBehavior=convertToNull&noAccessToProcedureBodies=true&serverTimezone=UTC";
		    String username = "root";		    
		    //String password = "GICReport@2019";
		    String password = "root";
		    Class.forName(driver);
		    Connection conn = DriverManager.getConnection(url, username, password);
		    return conn;
		  }
}