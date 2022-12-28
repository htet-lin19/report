package com.action;

import static java.time.DayOfWeek.MONDAY;
import static java.time.temporal.TemporalAdjusters.previousOrSame;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import org.apache.commons.lang.StringUtils;
import org.apache.struts2.dispatcher.SessionMap;
import org.apache.struts2.interceptor.SessionAware;
import com.common.CommonCheck;
import com.common.CommonConstant;
import com.jdbc.DataConnection;
import com.model.AttendanceListForm;
import com.model.AttendanceListFormDetail;
import com.model.ReportContentFormDetail;
import com.opensymphony.xwork2.ActionSupport;

public class AttendanceListAction extends ActionSupport implements SessionAware{

	private static final long serialVersionUID = 1L;
	AttendanceListForm attendanceListFrm = new AttendanceListForm();
	AttendanceListFormDetail attendanceListEidtFrm = new AttendanceListFormDetail();
	SessionMap<String, Object> sessionmap;

	/**
	 * 初期表示
	 * @return
	 */
	public String execute() {
		// 権限チェック
		if (sessionmap.get("ID") == null) {
			return "session";
		} else if(!CommonCheck.isValidRole(CommonConstant.SCREEN_NO.ATTENDANCE_LIST.value, 
				sessionmap.get("ID").toString())) {
			return "role";
		}
		
		//2020/11/16 GICM KZP 更新後、前の条件で再検索する対応　Start
		sessionmap.remove("attendancelist_empcd");
		//2020/11/16 GICM KZP 更新後、前の条件で再検索する対応　End
		sessionmap.remove("attendancelist_emp_name");
		sessionmap.remove("attendancelist_start_date");
		sessionmap.remove("attendancelist_end_date");
		
		// 権限設定
		checkPermission();
		
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
		} else if(!CommonCheck.isValidRole(CommonConstant.SCREEN_NO.ATTENDANCE_LIST.value, 
				sessionmap.get("ID").toString())) {
			return "role";
		}
		
		String emp_cd = "", emp_name = "", start_date = "", end_date = "";
		
		String group_cd = (String)sessionmap.get("GROUP_CD");
		String role = (String)sessionmap.get("ROLE");
		
		if ("search".equals(attendanceListFrm.getButton_event())) {
			// 社員番号
			emp_cd = attendanceListFrm.getEmp_cd();
			// 社員名
			emp_name = attendanceListFrm.getEmp_name();
			// 勤務日
			start_date = attendanceListFrm.getStart_date();
			end_date = attendanceListFrm.getEnd_date();
		} else {
			//2020/11/16 GICM KZP 更新後、前の条件で再検索する対応　Start
			if(!sessionmap.containsKey("attendancelist_empcd")) {
				// URL直接記入禁止
				return "role";
			}
			//2020/11/16 GICM KZP 更新後、前の条件で再検索する対応　End
			
			// セッションに設定した内容を取得する。
			//2020/11/16 GICM KZP 更新後、前の条件で再検索する対応　Start
			if(sessionmap.containsKey("attendancelist_empcd")) {
				emp_cd = (String) sessionmap.get("attendancelist_empcd");
			}
			//2020/11/16 GICM KZP 更新後、前の条件で再検索する対応　End
			if(sessionmap.containsKey("attendancelist_emp_name")) {
				emp_name = (String) sessionmap.get("attendancelist_emp_name");
			}			
			if(sessionmap.containsKey("attendancelist_start_date")) {
				start_date = (String) sessionmap.get("attendancelist_start_date");
			}			
			if(sessionmap.containsKey("attendancelist_end_date")) {
				end_date = (String) sessionmap.get("attendancelist_end_date");
			}
		}
		
		String sql = "SELECT *, employeemaster.position FROM attendance, employeemaster WHERE attendance.emp_cd = employeemaster.emp_cd ";
		
		if(StringUtils.isNotEmpty(emp_name)) {
			sql = sql + " AND employeemaster.emp_name LIKE '%"+emp_name.trim()+"%'";
		}
		
		if(StringUtils.isNotEmpty(emp_cd)) {
			sql = sql + " AND attendance.emp_cd LIKE '"+emp_cd.trim()+"%'";
		}
		
		if(StringUtils.isNotEmpty(start_date) && (!start_date.equals(""))) {
			sql = sql + " AND attendance.choose_date >= '"+start_date+"'";
		}
		
		if(StringUtils.isNotEmpty(end_date) && (!end_date.equals(""))) {
			sql = sql + " AND attendance.choose_date <= '"+end_date+"'";
		}
		
		// propertiesファイルを取得
		ResourceBundle rb = ResourceBundle.getBundle("messages_ja");
				
		// フォームに設定する
		if(rb.getString("role.gl").equals(role)) {
			sql = sql + " AND employeemaster.group_cd = '"+group_cd+"'";
		}
		sql = sql + " ORDER BY attendance.choose_date ASC";
		
		Connection conn;
		try {
			conn = DataConnection.getConnection();
			PreparedStatement ps1 = conn.prepareStatement(sql);
			ResultSet rs = ps1.executeQuery();
			
			List<AttendanceListFormDetail> detailList = new ArrayList<AttendanceListFormDetail>(); 
			
			while(rs.next()){
				AttendanceListFormDetail detail = new AttendanceListFormDetail();
				
				detail.setId(rs.getInt("id"));
				detail.setStart_time(rs.getString("start_time"));
				detail.setEnd_time(rs.getString("end_time"));
				detail.setChoose_date(rs.getString("choose_date"));
				detail.setWork_hour(rs.getString("work_hour"));
				detail.setOvertime(rs.getString("overtime"));
				detail.setMidnight_overtime(rs.getString("midnight_overtime"));
				detail.setTask_description(rs.getString("task_description"));
				detail.setEmp_cd(rs.getString("emp_cd"));
				detail.setDay(rs.getString("day"));
				detail.setBreak_time(rs.getString("break_time"));
				detail.setEmp_name(rs.getString("emp_name"));
				//2020/10/08 GICM KZP 勤務時間一覧 対応 Start
				detail.setCompensatory_date(rs.getString("compensatory_date"));
				detail.setOvertime_sunday(rs.getString("overtime_sunday"));
				detail.setCompensatory_comment(rs.getString("compensatory_comment"));
				detail.setCompensatory_leave(rs.getString("compensatory_leave"));
				//2020/10/08 GICM KZP 勤務時間一覧  対応 End
				//2020/10/19 GICM KZP GLの場合、自分以外の勤務データを更新・削除不可能に設定する対応Start
				detail.setPosition(rs.getString("position"));
				//2020/10/19 GICM KZP GLの場合、自分以外の勤務データを更新・削除不可能に設定する対応Start
				
				detailList.add(detail);
			}
			
			if(detailList.size() > 0) {
				attendanceListFrm.setSearch("1");
				attendanceListFrm.setAttendanceListDetail(detailList);
			}  else {
				// 検索データなしの場合
				attendanceListFrm.setSearch("");
				addActionMessage("検索データがありません。");
			}
			
			attendanceListFrm.setEmp_cd(emp_cd);
			attendanceListFrm.setEmp_name(emp_name);
			attendanceListFrm.setStart_date(start_date);
			attendanceListFrm.setEnd_date(end_date);
			
			//　セッションに検索情報を設定する
			//2020/11/16 GICM KZP 更新後、前の条件で再検索する対応　Start
			sessionmap.put("attendancelist_empcd", emp_cd);
			//2020/11/16 GICM KZP 更新後、前の条件で再検索する対応  End
			sessionmap.put("attendancelist_emp_name", emp_name);
			sessionmap.put("attendancelist_start_date", start_date);
			sessionmap.put("attendancelist_end_date", end_date);
		} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}
		
		//　権限設定
		checkPermission();
		
		return SUCCESS;
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
		} else if(!CommonCheck.isValidRole(CommonConstant.SCREEN_NO.ATTENDANCE_LIST.value, 
				sessionmap.get("ID").toString())) {
			return "role";
		}
		
		int id = attendanceListFrm.getDelete_id();
		String sql = "SELECT choose_date FROM attendance WHERE id ='" +id+ "'";
		Connection conn = null;
		conn = DataConnection.getConnection();
	    PreparedStatement ps1 = conn.prepareStatement(sql);
	    ResultSet rs = ps1.executeQuery();	
		String chooseDate = "";			
		if(rs!=null && rs.next()) {
			chooseDate = rs.getString("choose_date");
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			//convert String to LocalDate
			LocalDate choose_date = LocalDate.parse(chooseDate, formatter);;
		    LocalDate started_date = choose_date.with(previousOrSame(MONDAY));
		    sql = "SELECT report_cd FROM reportcontent WHERE emp_cd ='" +sessionmap.get("ID").toString()+ "' AND started_date ='"+started_date+"'";
		    ps1 = conn.prepareStatement(sql);
		    rs = ps1.executeQuery();	
			String rptCd = "";		
			List<ReportContentFormDetail> detailList = new ArrayList<ReportContentFormDetail>(); 
			if(rs!=null && rs.next()) {
				rptCd = rs.getString("report_cd");
				sql = "SELECT * FROM reportcontentdetail WHERE report_cd ='" +rptCd+ "' AND work_date ='"+chooseDate+"' ORDER BY work_date,reportdetail_cd";
				ps1 = conn.prepareStatement(sql);
				rs = ps1.executeQuery();				
				while (rs.next()) {
					ReportContentFormDetail detail = new ReportContentFormDetail();
					detail.setReportdetail_cd(rs.getString("reportdetail_cd"));
					detail.setResult("");
					detail.setActual_time("");
					detail.setUpdated_date(getSystemDate());
					detailList.add(detail);
				}
			}
			if(detailList.size()>0){
				for (ReportContentFormDetail detail : detailList) {	
					sql = "UPDATE reportcontentdetail SET result=?, actual_time=?, updated_date=? WHERE reportdetail_cd=?";
					ps1=conn.prepareStatement(sql);
					ps1.setString(1, detail.getResult());
					ps1.setString(2, detail.getActual_time());
					ps1.setString(3, detail.getUpdated_date());
					ps1.setString(4, detail.getReportdetail_cd());
					ps1.executeUpdate();				
				}			
			}	
					
			}
		sql = "DELETE FROM attendance WHERE id = ?";
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
		
		//　権限設定
		checkPermission();
		
		return SUCCESS;
	}

	/**
	 * 権限チェック
	 */
	private void checkPermission(){
		// セッションデータ取得
		String emp_cd = (String)sessionmap.get("ID");
		String emp_name = (String)sessionmap.get("NAME");
		String role = (String)sessionmap.get("ROLE");
		// propertiesファイル取得
		ResourceBundle rb = ResourceBundle.getBundle("messages_ja");
		
		attendanceListFrm.setDisable_empcd("");
		attendanceListFrm.setDisable_empname("");
		
		//2020/10/12 GICM KZP GLの場合、自分以外の勤務データを更新・削除不可能に設定する対応Start
		// フォームに設定する
		if(rb.getString("role.member").equals(role)) {
			attendanceListFrm.setEmp_cd(emp_cd);
			attendanceListFrm.setEmp_name(emp_name);

			attendanceListFrm.setDisable_empcd("disabled");
			attendanceListFrm.setDisable_empname("disabled");
			attendanceListFrm.setDisabled_glaction("disabled");
		}else if(rb.getString("role.manager").equals(role)) {
			attendanceListFrm.setDisabled_glaction("disabled");
		}else if(rb.getString("role.gl").equals(role)) {
			attendanceListFrm.setDisabled_glaction("");
		}
		//2020/10/12 GICM KZP GLの場合、自分以外の勤務データを更新・削除不可能に設定する対応 Start
	}
	
	public AttendanceListForm getAttendanceListFrm() {
		return attendanceListFrm;
	}

	public void setAttendanceListFrm(AttendanceListForm attendanceListFrm) {
		this.attendanceListFrm = attendanceListFrm;
	}

	public AttendanceListFormDetail getAttendanceListEidtFrm() {
		return attendanceListEidtFrm;
	}

	public void setAttendanceListEidtFrm(AttendanceListFormDetail attendanceListEidtFrm) {
		this.attendanceListEidtFrm = attendanceListEidtFrm;
	}

	@Override
	public void setSession(Map<String, Object> session) {
		this.sessionmap = (SessionMap)session;
	}
	
	/**
	 * ã‚·ã‚¹ãƒ†ãƒ æ—¥ä»˜å�–å¾—
	 * @return
	 */
	private String getSystemDate(){
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		Date date = new Date();
		return dateFormat.format(date);
	}
}