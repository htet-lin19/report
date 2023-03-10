package com.action;

import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.struts2.dispatcher.SessionMap;
import org.apache.struts2.interceptor.SessionAware;

import com.common.CommonCheck;
import com.common.CommonConstant;
import com.jdbc.DataConnection;
import com.opensymphony.xwork2.ActionSupport;

import ajd4jp.AJD;
import ajd4jp.AJDException;
import ajd4jp.Holiday;
import ajd4jp.Month;

public class LoginAction extends ActionSupport implements SessionAware {

	private static final long serialVersionUID = 1L;
	private String emp_cd, password, emp_name, role, group_cd, start_time, end_time, break_time, work_hour;
	SessionMap<String, String> sessionmap;
	private SessionMap<String, List<String>> sessionlist;
	
	public String getEmp_cd() {
		return emp_cd;
	}

	public void setEmp_cd(String emp_cd) {
		this.emp_cd = emp_cd;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) throws NoSuchAlgorithmException {
		this.password = new CommonCheck().decryptPassword(password);
		//this.password = password;
	}

	public String getEmp_name() {
		return emp_name;
	}

	public void setEmp_name(String emp_name) {
		this.emp_name = emp_name;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
	
   public String getGroup_cd() {
		return group_cd;
	}

	public void setGroup_cd(String group_cd) {
		this.group_cd = group_cd;
	}
	
	public String getStart_time() {
		return start_time;
	}

	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}

	public String getEnd_time() {
		return end_time;
	}

	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}

	public String getBreak_time() {
		return break_time;
	}

	public void setBreak_time(String break_time) {
		this.break_time = break_time;
	}

	public String getWork_hour() {
		return work_hour;
	}

	public void setWork_hour(String work_hour) {
		this.work_hour = work_hour;
	}

	public String execute() throws ClassNotFoundException {
		
		/*if(password.length()>20){
			 addActionError("Enter data");
		}*/
		if (validate(emp_cd, password)) {
			sessionmap.put("ID", emp_cd);
			sessionmap.put("Password", password);
			try {
				Connection con = DataConnection.getConnection();
				PreparedStatement ps = con.prepareStatement("SELECT emp.emp_name, role.role_cd, role.role_name, emp.group_cd "
						+ "FROM login, employeemaster emp, role "
						+ "WHERE login.emp_cd = emp.emp_cd "
						+ "AND role.role_cd = emp.position "
						+ "AND login.emp_cd = ?");
				ps.setString(1, emp_cd);
				ResultSet rs = ps.executeQuery();
		
				int rowCount = 0;
				while (rs.next()) {
					sessionmap.put("NAME", rs.getString("emp_name"));
					sessionmap.put("ROLE", rs.getString("role_name"));
					sessionmap.put("ROLE_CD", rs.getString("role_cd"));
					sessionmap.put("GROUP_CD", rs.getString("group_cd"));
					rowCount++;
				}
				
				if(rowCount==0) {
					return ERROR;
				}
				
				ps = con.prepareStatement("SELECT att.start_time, att.end_time, att.work_hour, att.break_time "
								+ "FROM attendancemaster att WHERE att.emp_cd = '"+emp_cd+"'");
				rs = ps.executeQuery();
				while (rs.next()) {
					sessionmap.put("STARTTIME", rs.getString("start_time"));
					sessionmap.put("ENDTIME", rs.getString("end_time"));
					sessionmap.put("WORKHOUR", rs.getString("work_hour"));
					sessionmap.put("BREAKTIME", rs.getString("break_time"));
				}
				
				rs.close();
				ps.close();
				
				// ?????????????????????
				checknotification(); 
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			sessionmap.put("pageReportList", "0");
			sessionmap.put("searchReportList", "SEARCH");
			return SUCCESS;
		} else {
			return ERROR;
		}
	}

	public void setSession(Map map) {
		sessionmap = (SessionMap) map;
		sessionmap.put("login", "true");
		sessionmap.put("role", getRole());
		sessionmap.put("group_cd", getGroup_cd());
		sessionmap.put("STARTTIME", "");
		sessionmap.put("ENDTIME", "");
		sessionmap.put("WORKHOUR", "");
		sessionmap.put("BREAKTIME", "");
		
		sessionlist = (SessionMap)map;
	}

	/**
	 * ?????????????????????
	 * @return
	 */
	public String logout() {
		// ??????????????????
		if (sessionmap.get("ID") == null) {
			return "session";
		} else if(!CommonCheck.isValidRole(CommonConstant.SCREEN_NO.LOGOUT.value, 
				sessionmap.get("ID").toString())) {
			return "role";
		}
		
		sessionmap.invalidate();
		
		return SUCCESS;
	}
	
	public static boolean validate(String emp_cd, String password) {
		boolean status = false;
		try {
			//Class.forName("com.mysql.jdbc.Driver");
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection conn = DataConnection.getConnection();

			PreparedStatement ps = conn.prepareStatement("SELECT * FROM login WHERE emp_cd=? AND password=?");
			ps.setString(1, emp_cd);
			ps.setString(2, password);
			ResultSet rs = ps.executeQuery();
			status = rs.next();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return status;
	}
	
	/**
	 * ?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????10???????????????????????????
	 *
	 * 
	 */
	
	public void checknotification() throws ParseException {

		LocalDate localDate = LocalDate.now();
		YearMonth yearMonth = YearMonth.now();

		// localDate???1???10?????????????????????????????????????????????
		if (1 <= localDate.getDayOfMonth() && localDate.getDayOfMonth() <= 10) {
			yearMonth = yearMonth.minusMonths(1);
		}

		LocalDate endOfMonth = yearMonth.atEndOfMonth();

		if (localDate.equals(endOfMonth)
				|| (localDate.isAfter(endOfMonth) && localDate.isBefore(endOfMonth.plusDays(10)))) {
			List<String> remainDates = checkAttendance(yearMonth.getYear(),
					yearMonth.getMonthValue(), emp_cd);
			if (!remainDates.isEmpty() ) {
				sessionmap.put("NOTIFICATION_COUNT", Integer.toString(remainDates.size()));
				sessionlist.put("NOTIFICATION_LIST", remainDates);
			} else {
				sessionmap.put("NOTIFICATION_COUNT", "0");
			}
		} else {
			sessionmap.put("NOTIFICATION_COUNT", "0");
		}
	}	

	/**
	 * ???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
	 * @param emp_cd
	 *            String
	 * @param year
	 *            int
	 * @param month
	 *            int
	 * @return List<String>
	 * 
	 */
	public List<String> checkAttendance(int year, int month, String emp_cd) {
		List<String> cdays = new ArrayList<String>();
		try {
			cdays = getDateByMonth(year, month);
			List<String> dayList = getDateFromDB(emp_cd, year, month);

			for (String date : dayList) {
				if (cdays.contains(date)) {
					cdays.remove(date);
				}
			}
		} catch (AJDException e) {
			e.printStackTrace();
		}
		return cdays;
	}
	
	/**
	 * API???????????????????????????????????????????????????
	 * 
	 * @param year
	 *            int
	 * @param month
	 *            int
	 * @return List<String>
	 * 
	 */
	public List<String> getDateByMonth(int year, int month) throws AJDException {
		List<String> dayList = new ArrayList<String>();
		Month mon = new Month(year, month);
		for (AJD day : mon.getDays()) {
			int d = day.getDay();
			Holiday h = Holiday.getHoliday(day);

			if (h == null) {
				String dayName = day.getWeek().getJpName();
				if (!dayName.equals("???") && !dayName.equals("???")) {
					String date = String.format("%d-%02d-%02d", mon.getYear(), mon.getMonth(), d);
					dayList.add(date);
				}
			}
		}
		return dayList;
	}
	
	/**
	 * ???????????????????????????????????????????????????????????????
	 * 
	 * @param emp_cd
	 *            String
	 * @param year
	 *            int
	 * @param month
	 *            int
	 * @return List<String>
	 */

	public List<String> getDateFromDB(String emp_cd, int year, int month) {
		List<String> detailList = new ArrayList<String>();
		try {
			Connection conn = DataConnection.getConnection();
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM attendance WHERE attendance.emp_cd = ? "
					+ " AND YEAR(attendance.choose_date)= ? " + " AND MONTH(attendance.choose_date) = ?");
			ps.setString(1, emp_cd);
			ps.setString(2, Integer.toString(year));
			ps.setString(3, Integer.toString(month));
			ResultSet rs = ps.executeQuery();

			detailList = new ArrayList<String>();

			while (rs.next()) {

				String choose_date = rs.getString("choose_date");
				detailList.add(choose_date);
			}
			rs.close();
			ps.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return detailList;
	}
}
