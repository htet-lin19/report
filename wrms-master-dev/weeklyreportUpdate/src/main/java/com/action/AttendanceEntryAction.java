package com.action;

import static java.time.DayOfWeek.MONDAY;
import static java.time.temporal.TemporalAdjusters.previousOrSame;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts2.dispatcher.SessionMap;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.SessionAware;

import com.common.CommonCheck;
import com.common.CommonConstant;
import com.jdbc.DataConnection;
import com.model.AttendanceListFormDetail;
import com.model.ReportContentForm;
import com.model.ReportContentFormDetail;
import com.opensymphony.xwork2.ActionSupport;
import ajd4jp.AJD;
import ajd4jp.AJDException;
import ajd4jp.Holiday;
import ajd4jp.Month;

public class AttendanceEntryAction extends ActionSupport implements ServletRequestAware, SessionAware {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// 2020/11/09 GICM NC 勤務時間登録機能追加対応 Start
	static final int STANDARD_WORK_MINUTE = 480;
	static final String COMPENSATORY_DATE_ERROR="選択した代休取得該当日に勤務時間が登録されていません。";
	static final String COMPENSATORY_HOUR_ERROR="代休取得時間は0時間より大きい値を入力してください。";
	static final String OVERTIME_HOUR_ERROR="選択した代休取得該当日に法定時間外や法定休日の作業時間がありません。代休取得該当日をもう一度選択してください。";
	static final String COMPENSATORY_OVER_ERROR="代休取得時間が使用可能な時間を超えています。代休取得時間を正しく入力してください。";
	static final String COMPENSATORY_DUPLICATE_ERROR="選択されている代休取得該当日の法廷時間外や法定休日の作業時間は代休取得済みです。";
	
	static final String ACTUAL_WORK="勤務実績：";
	static final String SWUNG_DASH="~";
	static final String COMMA="、";
	static final String OVERTIME_NIGHT="深夜早朝";
	static final String HOUR="時間";
	static final String COMPENSATORY_DATE="代休取得日:";
	
	static final String COMPENSATORY_CORRESPONDING_DATE="代休取得該当日：";
	static final String COMPENSATORY_HOUR="時間代休";
	
	private static final String COMPENSATORY_ERROR = "compensatory_error";
	// 2020/11/09 GICM NC 勤務時間登録機能追加対応 End
	HttpServletRequest request;
	AttendanceListFormDetail attendanceListEidtFrm = new AttendanceListFormDetail();
	SessionMap<String, Object> sessionmap;
	SessionMap<String, List<String>> sessionlist;
	ReportContentForm reportContentFrm = new ReportContentForm();
	int rowIndex;
	/* For Edit Page */
	String hiddenId;
	String pageNo;

	public String getHiddenId() {
		return hiddenId;
	}

	public void setHiddenId(String hiddenId) {
		this.hiddenId = hiddenId;
	}

	public String getPageNo() {
		return pageNo;
	}

	public void setPageNo(String pageNo) {
		this.pageNo = pageNo;
	}

	public AttendanceListFormDetail getAttendanceListEidtFrm() {
		return attendanceListEidtFrm;
	}

	public void setAttendanceListEidtFrm(AttendanceListFormDetail attendanceListEidtFrm) {
		this.attendanceListEidtFrm = attendanceListEidtFrm;
	}

	public ReportContentForm getReportContentFrm() {
		return reportContentFrm;
	}

	public void setReportContentFrm(ReportContentForm reportContentFrm) {
		this.reportContentFrm = reportContentFrm;
	}

	public int getRowIndex() {
		return rowIndex;
	}

	public void setRowIndex(int rowIndex) {
		this.rowIndex = rowIndex;
	}

	/**
	 * Edit
	 * 
	 * @return
	 */
	public String edit() {
		// 権限チェック
		if (sessionmap.get("ID") == null) {
			return "session";
		} else if (!CommonCheck.isValidRole(CommonConstant.SCREEN_NO.ATTENDANCE_LIST.value,
				sessionmap.get("ID").toString())) {
			return "role";
		}
		Connection conn;
		try {
			// 2018/10/15追加
			// 実行日取得
			Date date = new Date();
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			int month = c.get(Calendar.MONTH) + 1;
			int year = c.get(Calendar.YEAR);
			int lastMonth = month - 1;
			int lastYear = year;
			int nextMonth = month + 1;
			int nextYear = year;
			List<String> holidayList = new ArrayList<>();
			// 2018/10/15追加
			// 前月、今月、来月の祝日取得
			holidayList.addAll(getHolidaysByMonth(year, month));

			// 前月の祝日取得
			if (month == 1) {
				lastYear = year - 1;
				lastMonth = 12;
			}
			holidayList.addAll(getHolidaysByMonth(lastYear, lastMonth));

			// 来月の祝日取得
			if (month == 12) {
				nextYear = year + 1;
				nextMonth = 1;
			}
			holidayList.addAll(getHolidaysByMonth(nextYear, nextMonth));
			attendanceListEidtFrm.setHolidayList(holidayList);
			conn = DataConnection.getConnection();

			String sql = "SELECT * FROM attendance WHERE id ='" + hiddenId + "'";
			PreparedStatement ps1 = conn.prepareStatement(sql);
			ResultSet rs = ps1.executeQuery();
			if (rs.next()) {
				// set data to form
				attendanceListEidtFrm.setChoose_date(rs.getString("choose_date"));
				attendanceListEidtFrm.setDay(rs.getString("day"));
				attendanceListEidtFrm.setStart_time(rs.getString("start_time"));
				attendanceListEidtFrm.setEnd_time(rs.getString("end_time"));
				attendanceListEidtFrm.setBreak_time(rs.getString("break_time"));
				attendanceListEidtFrm.setWork_hour(rs.getString("work_hour"));
				attendanceListEidtFrm.setOvertime(rs.getString("overtime"));
				attendanceListEidtFrm.setMidnight_overtime(rs.getString("midnight_overtime"));
				attendanceListEidtFrm.setCompensatory_leave_hour(rs.getString("compensatory_leave_hour"));
				attendanceListEidtFrm.setReleaving_leave_hour(rs.getString("releaving_leave_hour"));
				attendanceListEidtFrm.setTask_description(rs.getString("task_description"));
				
				// 2020/10/19 GICM NC 勤務時間登録機能追加対応 Start
				attendanceListEidtFrm.setEmp_cd(rs.getString("emp_cd"));
				attendanceListEidtFrm.setCompensatory_date(rs.getString("compensatory_date"));
				attendanceListEidtFrm.setOvertime_sunday(rs.getString("overtime_sunday"));
				attendanceListEidtFrm.setCompensatory_comment(rs.getString("compensatory_comment"));
				// 2020/10/19 GICM NC 勤務時間登録機能追加対応 End
			}
			request.setAttribute("attendanceListEidtFrm", attendanceListEidtFrm);
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			// convert String to LocalDate
			LocalDate choose_date = LocalDate.parse(attendanceListEidtFrm.getChoose_date(), formatter);
			;
			LocalDate started_date = choose_date.with(previousOrSame(MONDAY));
			// 2020/11/09 GICM NC 勤務時間登録機能追加対応 Start
			sql = "SELECT report_cd FROM reportcontent WHERE emp_cd ='" + attendanceListEidtFrm.getEmp_cd()
					+ "' AND started_date ='" + started_date + "'";
			// 2020/11/09 GICM NC 勤務時間登録機能追加対応 End
			ps1 = conn.prepareStatement(sql);
			rs = ps1.executeQuery();
			String rptCd = "";
			if (rs != null && rs.next()) {
				rptCd = rs.getString("report_cd");
				sql = "SELECT * FROM reportcontentdetail WHERE report_cd ='" + rptCd + "' AND work_date ='"
						+ attendanceListEidtFrm.getChoose_date() + "' ORDER BY work_date,reportdetail_cd";
				ps1 = conn.prepareStatement(sql);
				rs = ps1.executeQuery();
				List<ReportContentFormDetail> detailList = new ArrayList<ReportContentFormDetail>();
				String wdate = "";
				while (rs.next()) {
					ReportContentFormDetail detail = new ReportContentFormDetail();
					detail.setReportdetail_cd(rs.getString("reportdetail_cd"));
					detail.setReport_cd(rs.getString("report_cd"));
					detail.setResult(rs.getString("result"));
					detail.setWork_date(rs.getString("work_date"));
					detail.setActual_time(rs.getString("actual_time"));
					detail.setCreated_date(rs.getString("created_date"));
					if (!wdate.equals(rs.getString("work_date"))) {
						detail.setMode("1");// EditMode
						wdate = rs.getString("work_date");
					} else {
						detail.setMode("0");
					}
					detailList.add(detail);
					reportContentFrm.setReportContentDetail(detailList);
					request.setAttribute("reportContentFrm", reportContentFrm);
				}
			} else {
				List<ReportContentFormDetail> detailList = new ArrayList<ReportContentFormDetail>();
				ReportContentFormDetail detail = new ReportContentFormDetail();
				detail.setMode("1");// EditMode
				detailList.add(detail);
				reportContentFrm.setReportContentDetail(detailList);
				request.setAttribute("reportContentFrm", reportContentFrm);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}	
		return SUCCESS;
	}

	//2020/10/19 GICM NC 勤務時間登録機能追加対応   Start
	/**
	 * 時間フォーマット(HH:mm)変更処理
	 * 
	 * @param minutes
	 * @return
	 */
	private String convertHourMinuteFormat(int minutes) {
		
		int hour = (int) ((Math.floor(minutes / 60)));
		int min = (minutes % 60);

		String resHour = null;
		String resMin = null;
		if (String.valueOf(hour).length() == 1) {
			resHour = "0" + hour;
		} else {
			resHour = "" + hour;
		}

		if (String.valueOf(min).length() == 1) {
			resMin = "0" + min;
		} else {
			resMin = "" + min;
		}

		return resHour + ":" + resMin;
	}
	//2020/10/19 GICM NC 勤務時間登録機能追加対応  End
	
	//2020/10/19 GICM NC 勤務時間登録機能追加対応  Start
	/**
	 * 残業時間削減処理
	 * 
	 * @return
	 * @throws SQLException 
	 */
	private String deleteCompensatoryDate() throws SQLException {
		
		String choose_date = attendanceListEidtFrm.getChoose_date();
		choose_date = choose_date.replace("-", "/").substring(5);
		String emp_cd = attendanceListEidtFrm.getEmp_cd();
		String start_time = null;
		String end_time = null;
		String work_time = null;
		String overtime = null;
		String midnight_overtime = null;
		String compensatory_comment = null;
		String overtime_sunday = null;
		String standard_start_hour = null;
		String standard_end_hour = null;
		int start_today_minute = 0;
		int end_today_minute = 0;
		int work_today_minute = 0;
		int compensatory_minute = 0;
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd");
			LocalDate date = LocalDate.parse(attendanceListEidtFrm.getCompensatory_date(), formatter);
			DayOfWeek dayOfWeek = date.getDayOfWeek();

			String sql = "SELECT start_time,end_time,work_hour,overtime,midnight_overtime,overtime_sunday,compensatory_comment "
					+ "FROM attendance WHERE choose_date ='" + attendanceListEidtFrm.getCompensatory_date()
					+ "' and emp_cd='" + emp_cd + "'";
			Connection conn=null;
			PreparedStatement ps =null;
			ResultSet rs=null;
			try {
				conn = DataConnection.getConnection();
				ps = conn.prepareStatement(sql);
				rs = ps.executeQuery();

				if ((rs.next())) {
					start_time = rs.getString("start_time");
					end_time = rs.getString("end_time");
					work_time = rs.getString("work_hour");
					overtime = rs.getString("overtime");
					midnight_overtime = rs.getString("midnight_overtime");
					overtime_sunday = rs.getString("overtime_sunday");
					compensatory_comment=rs.getString("compensatory_comment");
				}

				if (rs.getRow() == 0) {
					attendanceListEidtFrm.setCompensatory_date(attendanceListEidtFrm.getCompensatory_date());
					addActionError(COMPENSATORY_DATE_ERROR);
					return SUCCESS;
				}
				
				if (compensatory_comment != null && !compensatory_comment.isEmpty()) {
					attendanceListEidtFrm.setCompensatory_date(attendanceListEidtFrm.getCompensatory_date());
					addActionError(COMPENSATORY_DUPLICATE_ERROR);
					return SUCCESS;					
				}

			} catch (Exception e) {
				e.printStackTrace();
				return ERROR;
			} finally {
				if (rs != null ) {
					rs.close();
				}
				if (ps != null) {
					ps.close();
				}
			}
			
			//取得勤務マスターデータ
			String sql1 = "SELECT start_time,end_time FROM attendancemaster WHERE emp_cd ='" + emp_cd + "'";

			try {
				conn = DataConnection.getConnection();
				ps = conn.prepareStatement(sql1);
				rs = ps.executeQuery();
				if ((rs.next())) {
					standard_start_hour=rs.getString("start_time");
					standard_end_hour=rs.getString("end_time");
				}

			} catch (Exception e) {
				e.printStackTrace();
				return ERROR;
			} finally {
				if (rs != null ) {
					rs.close();
				}
				if (ps != null) {
					ps.close();
				}
			}
			
			int standard_start_minute = 0;
			if (standard_start_hour != null && !standard_start_hour.isEmpty()) {
				String[] standard_start_Arr = standard_start_hour.split(":", 2);
				String start_hour = standard_start_Arr[0];
				String start_minute = standard_start_Arr[1];
				standard_start_minute = Integer.parseInt(start_hour) * 60 + Integer.parseInt(start_minute);
			} else {
				standard_start_minute = 540; //デフォルト値 09:00 (Added By GICM NC on 2020/12/01)
			}
			
			int standard_end_minute = 0;
			if (standard_end_hour !=null && !standard_end_hour.isEmpty()) {
				String[] standard_end_Arr = standard_end_hour.split(":", 2);
				String end_hour = standard_end_Arr[0];
				String end_minute = standard_end_Arr[1];
				standard_end_minute = Integer.parseInt(end_hour) * 60 + Integer.parseInt(end_minute);
			} else {
				standard_end_minute = 1080; //デフォルト値 18:00 (Added By GICM NC on 2020/12/01)
			}

			//取得勤務時間
			String work_hour_today = attendanceListEidtFrm.getWork_hour();
			String[] work_hour_Arr = work_hour_today.split(":", 2);
			String hour = work_hour_Arr[0];
			String minute = work_hour_Arr[1];
			work_today_minute = Integer.parseInt(hour) * 60 + Integer.parseInt(minute);
			
			//取得開始時刻
			String start_hour_today = attendanceListEidtFrm.getStart_time();
			String[] start_hour_Arr = start_hour_today.split(":", 2);
			String start_hour = start_hour_Arr[0];
			String start_minute = start_hour_Arr[1];
			start_today_minute = Integer.parseInt(start_hour) * 60 + Integer.parseInt(start_minute);
			if (start_today_minute==0) {
				start_today_minute = 60*24;
			}
			
			//取得終了時刻
			String end_hour_today = attendanceListEidtFrm.getEnd_time();
			String[] end_hour_Arr = end_hour_today.split(":", 2);
			String end_hour = end_hour_Arr[0];
			String end_minute = end_hour_Arr[1];
			end_today_minute = Integer.parseInt(end_hour) * 60 + Integer.parseInt(end_minute);
			if (end_today_minute==0) {
				end_today_minute = 60*24;
			}
			
			//　代休時間計算
			if (work_today_minute==0 && attendanceListEidtFrm.getDay() != null && ("2".equals(attendanceListEidtFrm.getDay())
				|| "3".equals(attendanceListEidtFrm.getDay()))) {
				//代休と半休取得する場合
				compensatory_minute = 60*4;
				
			} else if ( work_today_minute < STANDARD_WORK_MINUTE) {
				//勤務時間がstandard work hourより小さい場合
				compensatory_minute = STANDARD_WORK_MINUTE - work_today_minute;
				
			} else if (work_today_minute > STANDARD_WORK_MINUTE && end_today_minute > standard_end_minute) {
				//勤務時間がstandard work hourより大きい場合　+　終了時間がstandard end hour より大きい場合
				compensatory_minute=start_today_minute-standard_start_minute-(60*1);
				
			} else if (work_today_minute > STANDARD_WORK_MINUTE && start_today_minute < standard_start_minute) { 
				//勤務時間がstandard work hourより大きい場合　+　開始時間がstandard start hour より小さい場合
				compensatory_minute=standard_end_minute-end_today_minute-(60*1);
			
			} else if (work_today_minute==0) {
				//全体代休取得する場合
				compensatory_minute=STANDARD_WORK_MINUTE;
			}
			
			if (compensatory_minute==0) {
				attendanceListEidtFrm.setCompensatory_date(attendanceListEidtFrm.getCompensatory_date());
				addActionError(COMPENSATORY_HOUR_ERROR);
				return SUCCESS;
			}
			
			int end_time_minute = 0;
			if (end_time != null && !end_time.isEmpty()) {
				String[] end_time_Arr = end_time.split(":", 2);
				String end_hours = end_time_Arr[0];
				String end_minutes = end_time_Arr[1];
				end_time_minute = Integer.parseInt(end_hours) * 60 + Integer.parseInt(end_minutes);
				end_time_minute -= compensatory_minute;
				if (end_time_minute < 0) {
					end_time_minute = (24 * 60) + end_time_minute;
				}
			}

			int work_time_minute = 0;
			int work_time_minutes = 0;
			if (work_time != null && !work_time.isEmpty()) {
				String[] work_time_Arr = work_time.split(":", 2);
				String work_hour = work_time_Arr[0];
				String work_minute = work_time_Arr[1];
				work_time_minute = Integer.parseInt(work_hour) * 60 + Integer.parseInt(work_minute);
				work_time_minutes = work_time_minute - compensatory_minute;
			}

			int overtime_minute = 0;
			int overtime_minutes = 0;
			if (overtime != null && !overtime.isEmpty()) {
				String[] overtime_Arr = overtime.split(":", 2);
				String overHour = overtime_Arr[0];
				String overMin = overtime_Arr[1];
				overtime_minute = Integer.parseInt(overHour) * 60 + Integer.parseInt(overMin);
				overtime_minutes = overtime_minute - compensatory_minute;
			}

			int overtime_sunday_minute = 0;
			int overtime_sunday_minutes = 0;
			if ((overtime_sunday != null && !overtime_sunday.isEmpty()) && dayOfWeek == DayOfWeek.SUNDAY) {
				String[] overtime_sunday_Arr = overtime_sunday.split(":", 2);
				String overtimeSundayHour = overtime_sunday_Arr[0];
				String overSundayMin = overtime_sunday_Arr[1];
				overtime_sunday_minute = Integer.parseInt(overtimeSundayHour) * 60
						+ Integer.parseInt(overSundayMin);
				overtime_sunday_minutes -= compensatory_minute;
			}

			int midnight_overtime_minute = 0;
			StringBuilder strBuilder=new StringBuilder();
			if (midnight_overtime != null && !midnight_overtime.isEmpty()) {
				String[] midnight_overtime_Arr = midnight_overtime.split(":", 2);
				String midnight_hour = midnight_overtime_Arr[0];
				String midnight_min = midnight_overtime_Arr[1];
				midnight_overtime_minute = Integer.parseInt(midnight_hour) * 60 + Integer.parseInt(midnight_min);

				if (midnight_overtime_minute > compensatory_minute) {
					midnight_overtime_minute -= compensatory_minute;
					strBuilder.append(ACTUAL_WORK);
					strBuilder.append(start_time);
					strBuilder.append(SWUNG_DASH);
					strBuilder.append(end_time);
					strBuilder.append(COMMA);
					strBuilder.append(OVERTIME_NIGHT);
					strBuilder.append(compensatory_minute / 60);
					strBuilder.append(HOUR);
					strBuilder.append(COMMA);
					strBuilder.append(COMPENSATORY_DATE);
					strBuilder.append(choose_date);
					compensatory_comment=strBuilder.toString();
				} else {
					strBuilder.append(ACTUAL_WORK);
					strBuilder.append(start_time);
					strBuilder.append(SWUNG_DASH);
					strBuilder.append(end_time);
					strBuilder.append(COMMA);
					strBuilder.append(OVERTIME_NIGHT);
					strBuilder.append(midnight_overtime_minute / 60);
					strBuilder.append(HOUR);
					strBuilder.append(COMMA);
					strBuilder.append(COMPENSATORY_DATE);
					strBuilder.append(choose_date);
					compensatory_comment=strBuilder.toString();
					
					midnight_overtime_minute = 0;
				}

			} else {
				strBuilder.append(ACTUAL_WORK);
				strBuilder.append(start_time);
				strBuilder.append(SWUNG_DASH);
				strBuilder.append(end_time);
				strBuilder.append(COMMA);
				strBuilder.append(COMPENSATORY_DATE);
				strBuilder.append(choose_date);
				compensatory_comment=strBuilder.toString();
			}
			//残業チェック
			if (!(dayOfWeek == DayOfWeek.SUNDAY)) {
				if (overtime != null && overtime.isEmpty()) {
					attendanceListEidtFrm.setCompensatory_date(attendanceListEidtFrm.getCompensatory_date());
					addActionError(OVERTIME_HOUR_ERROR);
					return SUCCESS;
				} else if (compensatory_minute > overtime_minute) {
					attendanceListEidtFrm.setCompensatory_date(attendanceListEidtFrm.getCompensatory_date());
					addActionError(COMPENSATORY_OVER_ERROR);
					return SUCCESS;
				}

			} else if (dayOfWeek == DayOfWeek.SUNDAY) {
				if ((overtime != null && overtime.isEmpty()) && (overtime_sunday != null && overtime_sunday.isEmpty())) {
					addActionError(OVERTIME_HOUR_ERROR);
					return SUCCESS;
				} else if (compensatory_minute > overtime_sunday_minute){
					attendanceListEidtFrm.setCompensatory_date(attendanceListEidtFrm.getCompensatory_date());
					addActionError(COMPENSATORY_OVER_ERROR); 
					return SUCCESS;
				}
			}
			String queryUpd1 = "UPDATE attendance SET end_time = ?,work_hour=?,overtime=?,compensatory_comment = ?,midnight_overtime=?,overtime_sunday=?  "
					+ " WHERE choose_date='" + attendanceListEidtFrm.getCompensatory_date() + "'AND emp_cd='"
					+ emp_cd + "'";
			PreparedStatement ps1=null;
			try {
				ps1 = conn.prepareStatement(queryUpd1);
				ps1.setString(1, convertHourMinuteFormat(end_time_minute));
				ps1.setString(2, convertHourMinuteFormat(work_time_minutes));
				if (overtime_minutes > 0) {
					ps1.setString(3, convertHourMinuteFormat(overtime_minutes));
				} else {
					ps1.setString(3, "");
				}
				ps1.setString(4, compensatory_comment);
				if (midnight_overtime_minute > 0) {
					ps1.setString(5, convertHourMinuteFormat(midnight_overtime_minute));
				} else {
					ps1.setString(5, "");
				}
				if (overtime_sunday_minutes > 0) {
					ps1.setString(6, convertHourMinuteFormat(overtime_sunday_minute));
				} else {
					ps1.setString(6, "");
				}
				ps1.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
				return ERROR;
			} finally {
				if (ps1 != null ) {
					ps1.close();
				}
				if (conn != null) {
					conn.close();
				}
			}
		
		return String.valueOf(compensatory_minute);
	}
	//2020/10/19 GICM NC 勤務時間登録機能追加対応 End
	
	/**
	 * 登録処理
	 * 
	 * @return
	 * @throws SQLException 
	 */
	public String insert() throws SQLException {
		Connection conn = null;
		ResultSet rs=null;
		PreparedStatement ps=null;
		try {
			// 権限チェック
			if (sessionmap.get("ID") == null) {
				return "session";
			} else if (!CommonCheck.isValidRole(CommonConstant.SCREEN_NO.ATTENDANCE_ENTRY.value,
					sessionmap.get("ID").toString())) {
				return "role";
			}

			// 通知回数チェック
			if (sessionmap.get("NOTIFICATION_COUNT") != null) {
				List<String> notificationList = (List<String>) sessionlist.get("NOTIFICATION_LIST");
				if (notificationList != null) {
					if (notificationList.contains(attendanceListEidtFrm.getChoose_date())) {
						notificationList.remove(attendanceListEidtFrm.getChoose_date());
						int count = Integer.parseInt(sessionmap.get("NOTIFICATION_COUNT").toString());
						count = count - 1;
						sessionmap.put("NOTIFICATION_COUNT", count + "");
					}
				}
			}
			// 2020/10/09 GICM NC 勤務時間登録機能追加対応 Start
			String compensatory_minute="";
			if (attendanceListEidtFrm.getCompensatory_date() != null && !attendanceListEidtFrm.getCompensatory_date().isEmpty()) {
				
				 String compensatory_status=deleteCompensatoryDate();
				 
				 if ("success".equals(compensatory_status)) { 
					 return SUCCESS;
				 } else if ("error".equals(compensatory_status)) {	 
					 return ERROR;	 
				 } else {
					 compensatory_minute=compensatory_status;
				 }
			}
			// 2020/10/14 GICM NC 勤務時間登録機能追加対応 End
			int status = 0;

			conn = DataConnection.getConnection();
			Timestamp fixed_time = new Timestamp(new Date().getTime());

			String query2 = "SELECT * FROM attendance WHERE choose_date='" + attendanceListEidtFrm.getChoose_date()
					+ "'AND emp_cd='" + attendanceListEidtFrm.getEmp_cd() + "'";
			PreparedStatement ps2 = conn.prepareStatement(query2);
			rs = ps2.executeQuery();
			if (!rs.next()) {
				// 2020/10/09 GICM NC 勤務時間登録機能追加対応 Start
				String query = "INSERT INTO attendance(start_time, end_time, choose_date, "
						+ "work_hour, overtime, midnight_overtime, "
						+ "compensatory_leave_hour, releaving_leave_hour, task_description, "
						+ "midnight_compensatory_leave_hour, created_date, modified_date, "
						+ "emp_cd, day, break_time,overtime_sunday,compensatory_leave,compensatory_date,compensatory_comment) "
						+ "VALUES(?,?,?," + "?,?,?," + "?,?,?," + "?,?,?,?,?," + "?,?,?,?,?)";
				// 2020/10/09 GICM NC 勤務時間登録機能追加対応 End
			    ps = conn.prepareStatement(query);
				ps.setString(1, attendanceListEidtFrm.getStart_time());
				ps.setString(2, attendanceListEidtFrm.getEnd_time());
				ps.setString(3, attendanceListEidtFrm.getChoose_date());
				ps.setString(4, attendanceListEidtFrm.getWork_hour());
				ps.setString(5, attendanceListEidtFrm.getOvertime());
				ps.setString(6, attendanceListEidtFrm.getMidnight_overtime());
				ps.setString(7, attendanceListEidtFrm.getCompensatory_leave_hour());
				ps.setString(8, attendanceListEidtFrm.getReleaving_leave_hour());
				ps.setString(9, attendanceListEidtFrm.getTask_description());
				ps.setString(10, attendanceListEidtFrm.getMidnight_compensatory_leave_hour());
				ps.setTimestamp(11, fixed_time);
				ps.setTimestamp(12, fixed_time);
				ps.setString(13, attendanceListEidtFrm.getEmp_cd());
				// 2020/10/09 GICM NC 勤務時間登録機能追加対応 Start
				if (attendanceListEidtFrm.getDay() != null && "1".equals(attendanceListEidtFrm.getDay()))
					ps.setString(14, "1");//全体
				else if (attendanceListEidtFrm.getDay() != null && "2".equals(attendanceListEidtFrm.getDay()))
					ps.setString(14, "2");//午前半休
				else if (attendanceListEidtFrm.getDay() != null && "3".equals(attendanceListEidtFrm.getDay()))
					ps.setString(14, "3");//午後半休
				else if (attendanceListEidtFrm.getDay() != null && "4".equals(attendanceListEidtFrm.getDay()))
					ps.setString(14, "4");//特別休暇
				else
					ps.setString(14, "0");//休暇なし
				ps.setString(15, attendanceListEidtFrm.getBreak_time());
				ps.setString(16, attendanceListEidtFrm.getOvertime_sunday());

				if (attendanceListEidtFrm.getCompensatory_leave() != null
						&& attendanceListEidtFrm.getCompensatory_leave().equals("true"))
					ps.setString(17, "1");

				else
					ps.setString(17, "0");
				ps.setString(18, attendanceListEidtFrm.getCompensatory_date());
				if (attendanceListEidtFrm.getCompensatory_date() != null && !attendanceListEidtFrm.getCompensatory_date().isEmpty()) {
					String compensatory_date = attendanceListEidtFrm.getCompensatory_date();
					compensatory_date = compensatory_date.replace("-", "/").substring(5);
					
					StringBuilder strBuilder=new StringBuilder();
					strBuilder.append(COMPENSATORY_CORRESPONDING_DATE);
					strBuilder.append(compensatory_date);
					strBuilder.append(COMMA);
					strBuilder.append(Integer.parseInt(compensatory_minute) / 60 );
					strBuilder.append(COMPENSATORY_HOUR);
					String compensatory_comment=strBuilder.toString();
					ps.setString(19, compensatory_comment);
				} else {
					ps.setString(19, "");
				}
				
				status = ps.executeUpdate();
				// 2020/10/14 GICM NC 勤務時間登録機能追加対応 End
			}

			if (status == 1) {
				// get Monday of choose Date
				String start_date = getMondayDate(attendanceListEidtFrm.getChoose_date());
				//store work record
				createWorkRecord(attendanceListEidtFrm.getEmp_cd(), start_date, attendanceListEidtFrm.getChoose_date(), attendanceListEidtFrm.getWork_hour(), reportContentFrm);		
			
				addActionMessage("登録しました。");
				attendanceListEidtFrm.setStart_time("");
				attendanceListEidtFrm.setEnd_time("");
				attendanceListEidtFrm.setChoose_date("");
				attendanceListEidtFrm.setWork_hour("");
				attendanceListEidtFrm.setOvertime("");
				attendanceListEidtFrm.setMidnight_overtime("");
				attendanceListEidtFrm.setCompensatory_leave_hour("");
				attendanceListEidtFrm.setReleaving_leave_hour("");
				attendanceListEidtFrm.setTask_description("");
				attendanceListEidtFrm.setMidnight_compensatory_leave_hour("");
				attendanceListEidtFrm.setDay("false");
				attendanceListEidtFrm.setBreak_time("");
				
				// 2020/10/20 GICM NC 勤務時間登録機能追加対応 Start
				attendanceListEidtFrm.setOvertime_sunday("");
				attendanceListEidtFrm.setCompensatory_date("");
				attendanceListEidtFrm.setCompensatory_comment("");
				// 2020/10/20 GICM NC 勤務時間登録機能追加対応 End

				execute();
			} else {
				addActionError("『社員番号、日付 』が重複になっています。");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		} finally {
			// 2020/11/09 GICM NC 勤務時間登録機能追加対応 Start
			if (rs != null ) {
				rs.close();
			}
			if (ps != null) {
				ps.close();
			}
			if (conn != null) {
				conn.close();
			}
			// 2020/11/09 GICM NC 勤務時間登録機能追加対応 End
		}
		return SUCCESS;
	}

	/**
	 * クリア処理
	 * 
	 * @return
	 */
	public String Clear() {
		// 権限チェック
		if (sessionmap.get("ID") == null) {
			return "session";
		} else if (!CommonCheck.isValidRole(CommonConstant.SCREEN_NO.ATTENDANCE_ENTRY.value,
				sessionmap.get("ID").toString())) {
			return "role";
		}

		attendanceListEidtFrm.setStart_time("");
		attendanceListEidtFrm.setEnd_time("");
		attendanceListEidtFrm.setChoose_date("");
		attendanceListEidtFrm.setWork_hour("");
		attendanceListEidtFrm.setOvertime("");
		attendanceListEidtFrm.setMidnight_overtime("");
		attendanceListEidtFrm.setCompensatory_leave_hour("");
		attendanceListEidtFrm.setReleaving_leave_hour("");
		attendanceListEidtFrm.setTask_description("");
		attendanceListEidtFrm.setMidnight_compensatory_leave_hour("");
		attendanceListEidtFrm.setDay("");
		attendanceListEidtFrm.setBreak_time("");
		// 2020/10/20 GICM NC 勤務時間登録機能追加対応 Start
		attendanceListEidtFrm.setOvertime_sunday("");
		attendanceListEidtFrm.setCompensatory_date("");
		attendanceListEidtFrm.setCompensatory_comment("");
		// 2020/10/20 GICM NC 勤務時間登録機能追加対応 End

		execute();
		return SUCCESS;
	}

	/**
	 * 初期表示処理
	 */
	public String execute() {
		// 権限チェック
		sessionmap.put("dateID", "date_0");
		if (sessionmap.get("ID") == null) {
			return "session";
		} else if (!CommonCheck.isValidRole(CommonConstant.SCREEN_NO.ATTENDANCE_ENTRY.value,
				sessionmap.get("ID").toString())) {
			return "role";
		}

		// セッションデータ取得
		String emp_cd = (String) sessionmap.get("ID");

		String sql = "SELECT * FROM attendancemaster WHERE emp_cd = '" + emp_cd + "'";

		// 2018/10/15追加
		// 実行日取得
		Date date = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int month = c.get(Calendar.MONTH) + 1;
		int year = c.get(Calendar.YEAR);
		int lastMonth = month - 1;
		int lastYear = year;
		int nextMonth = month + 1;
		int nextYear = year;
		List<String> holidayList = new ArrayList<>();

		Connection conn;
		try {
			conn = DataConnection.getConnection();
			PreparedStatement ps1 = conn.prepareStatement(sql);
			ResultSet rs = ps1.executeQuery();

			while (rs.next()) {
				attendanceListEidtFrm.setStart_time(rs.getString("start_time"));
				attendanceListEidtFrm.setEnd_time(rs.getString("end_time"));
				attendanceListEidtFrm.setWork_hour(rs.getString("work_hour"));
				attendanceListEidtFrm.setBreak_time(rs.getString("break_time"));
				//2020-11-16 GICM NC 勤務時間登録機能追加対応 Start
				attendanceListEidtFrm.setShift_work(rs.getInt("shift_work"));
				//2020-11-16 GICM NC 勤務時間登録機能追加対応 End
			}
			attendanceListEidtFrm.setMidnight_overtime(null);

			// 2018/10/15追加
			// 前月、今月、来月の祝日取得
			holidayList.addAll(getHolidaysByMonth(year, month));

			// 前月の祝日取得
			if (month == 1) {
				lastYear = year - 1;
				lastMonth = 12;
			}
			holidayList.addAll(getHolidaysByMonth(lastYear, lastMonth));

			// 来月の祝日取得
			if (month == 12) {
				nextYear = year + 1;
				nextMonth = 1;
			}
			holidayList.addAll(getHolidaysByMonth(nextYear, nextMonth));
			attendanceListEidtFrm.setHolidayList(holidayList);

			request.setAttribute("attendanceListEidtFrm", attendanceListEidtFrm);

			List<ReportContentFormDetail> detailList = new ArrayList<ReportContentFormDetail>();
			ReportContentFormDetail detail = new ReportContentFormDetail();
			detail.setMode("1");// EditMode
			detailList.add(detail);
			reportContentFrm.setReportContentDetail(detailList);
			request.setAttribute("reportContentFrm", reportContentFrm);
			hiddenId = null;

		} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}
		return SUCCESS;
	}

	/**
	 * AddRowボタン 作成(5/2020)ssl
	 */
	public String add() {
		if (sessionmap.get("ID") == null) {
			return "session";
		} else if (!CommonCheck.isValidRole(CommonConstant.SCREEN_NO.ATTENDANCE_ENTRY.value,
				sessionmap.get("ID").toString())) {
			return "role";
		}
		reportContentFrm = (ReportContentForm) request.getAttribute("reportContentFrm");	
		List<ReportContentFormDetail> detailListOldList = reportContentFrm.getReportContentDetail();
		List<ReportContentFormDetail> detailListNewList = new ArrayList<ReportContentFormDetail>();
		int rowNo = (int) request.getAttribute("rowIndex");
		int count = 0;
		for (ReportContentFormDetail detailOld : detailListOldList) {
			ReportContentFormDetail detailNew = new ReportContentFormDetail();
			detailNew = detailOld;
			detailListNewList.add(detailNew);
			if (rowNo == count) {
				detailNew = new ReportContentFormDetail();
				detailNew.setMode("0");
				detailListNewList.add(detailNew);
			}
			count++;
		}
		reportContentFrm.setReportContentDetail(detailListNewList);
		request.setAttribute("reportContentFrm", reportContentFrm);
		attendanceListEidtFrm.setChoose_date(attendanceListEidtFrm.getChoose_date());
		
		return SUCCESS;

	}

	/**
	 * Removeボタン 作成(5/2020)ssl
	 */
	public String remove() {
		if (sessionmap.get("ID") == null) {
			return "session";
		} else if (!CommonCheck.isValidRole(CommonConstant.SCREEN_NO.ATTENDANCE_ENTRY.value,
				sessionmap.get("ID").toString())) {
			return "role";
		}
		reportContentFrm = (ReportContentForm) request.getAttribute("reportContentFrm");
		List<ReportContentFormDetail> detailListOldList = reportContentFrm.getReportContentDetail();
		List<ReportContentFormDetail> detailListNewList = new ArrayList<ReportContentFormDetail>();
		int rowNo = (int) request.getAttribute("rowIndex");
		int count = 0;
		for (ReportContentFormDetail detailOld : detailListOldList) {
			if (rowNo != count) {
				ReportContentFormDetail detailNew = new ReportContentFormDetail();
				detailNew = detailOld;
				detailListNewList.add(detailNew);
			}
			count++;
		}
		reportContentFrm.setSearch(reportContentFrm.getSearch());
		reportContentFrm.setReportContentDetail(detailListNewList);
		reportContentFrm.setStarted_date(reportContentFrm.getStarted_date_hidden());
		request.setAttribute("reportContentFrm", reportContentFrm);
		return SUCCESS;
	}

	/**
	 * 初期表示処理
	 * @throws SQLException 
	 */
	public String update() throws SQLException {
		Connection conn = null;
		ResultSet rs=null;
		PreparedStatement ps=null;
		
		try {
			// 権限チェック
			if (sessionmap.get("ID") == null) {
				return "session";
			} else if (!CommonCheck.isValidRole(CommonConstant.SCREEN_NO.ATTENDANCE_ENTRY.value,
					sessionmap.get("ID").toString())) {
				return "role";
			}
			// 通知回数チェック
			if (sessionmap.get("NOTIFICATION_COUNT") != null) {
				List<String> notificationList = (List<String>) sessionlist.get("NOTIFICATION_LIST");
				if (notificationList != null) {
					if (notificationList.contains(attendanceListEidtFrm.getChoose_date())) {
						notificationList.remove(attendanceListEidtFrm.getChoose_date());
						int count = Integer.parseInt(sessionmap.get("NOTIFICATION_COUNT").toString());
						count = count - 1;
						sessionmap.put("NOTIFICATION_COUNT", count + "");
					}
				}
			}
			// 2020/10/09 GICM NC 勤務時間登録機能追加対応 Start
			String compensatory_minute="";
			if (attendanceListEidtFrm.getCompensatory_comment()==null || attendanceListEidtFrm.getCompensatory_comment().isEmpty()) {
				if (attendanceListEidtFrm.getCompensatory_date() != null
						&& !attendanceListEidtFrm.getCompensatory_date().isEmpty()) {
					String compensatory_status = deleteCompensatoryDate();
					if ("success".equals(compensatory_status) ) {
						return COMPENSATORY_ERROR;
					} else if ("error".equals(compensatory_status)) {
						return ERROR;
					}else {
						compensatory_minute = compensatory_status;
					}
				}
			}
			// 2020/10/14 GICM NC 勤務時間登録機能追加対応 End

			conn = DataConnection.getConnection();
			Timestamp fixed_time = new Timestamp(new Date().getTime());

			String query2 = "SELECT * FROM attendance WHERE choose_date='" + attendanceListEidtFrm.getChoose_date()
					+ "' AND emp_cd='" + attendanceListEidtFrm.getEmp_cd() + "'";
			// 2020/10/09 GICM NC 勤務時間登録機能追加対応 Start
			PreparedStatement ps1 = conn.prepareStatement(query2);
			rs = ps1.executeQuery();
			if (rs.next()) {
				if (attendanceListEidtFrm.getCompensatory_comment()!=null && !attendanceListEidtFrm.getCompensatory_comment().isEmpty()) {
					String queryUpd = "UPDATE attendance SET task_description = ?," 
							+ "modified_date = ? ," + "created_date = ? " + " WHERE choose_date='"
							+ attendanceListEidtFrm.getChoose_date() + "'AND emp_cd='" + attendanceListEidtFrm.getEmp_cd()
							+ "'";
					ps = conn.prepareStatement(queryUpd);
					ps.setString(1, attendanceListEidtFrm.getTask_description());
					ps.setTimestamp(2, fixed_time);
					ps.setTimestamp(3, rs.getTimestamp("created_date"));

					sessionmap.put("attendancelist_emp_cd", attendanceListEidtFrm.getEmp_cd());
					ps.executeUpdate();
				} else {
					String queryUpd = "UPDATE attendance SET start_time = ?, " + "end_time = ?, " + "choose_date = ?, "
							+ "work_hour = ?, " + "overtime = ?, " + "midnight_overtime = ?, "
							+ "compensatory_leave_hour = ?, " + "releaving_leave_hour = ?, " + "task_description = ?, "
							+ "midnight_compensatory_leave_hour = ?," + "day = ?," + "compensatory_leave = ?, " 
							+ "compensatory_date = ?," + "overtime_sunday = ?," + "compensatory_comment = ?," + "break_time = ?, "
							+ "modified_date = ? ," + "created_date = ? " + " WHERE choose_date='"
							+ attendanceListEidtFrm.getChoose_date() + "'AND emp_cd='" + attendanceListEidtFrm.getEmp_cd()
							+ "'";
			 // 2020/10/09 GICM NC 勤務時間登録機能追加対応 End
					ps = conn.prepareStatement(queryUpd);
					ps.setString(1, attendanceListEidtFrm.getStart_time());
					ps.setString(2, attendanceListEidtFrm.getEnd_time());
					ps.setString(3, attendanceListEidtFrm.getChoose_date());
					ps.setString(4, attendanceListEidtFrm.getWork_hour());
					ps.setString(5, attendanceListEidtFrm.getOvertime());
					ps.setString(6, attendanceListEidtFrm.getMidnight_overtime());
					ps.setString(7, attendanceListEidtFrm.getCompensatory_leave_hour());
					ps.setString(8, attendanceListEidtFrm.getReleaving_leave_hour());
					ps.setString(9, attendanceListEidtFrm.getTask_description());
					ps.setString(10, attendanceListEidtFrm.getMidnight_compensatory_leave_hour());
					// 2020/10/19 GICM NC 勤務時間登録機能追加対応 Start
					if (attendanceListEidtFrm.getDay() != null && "1".equals(attendanceListEidtFrm.getDay()))
						ps.setString(11, "1");//全体
					else if (attendanceListEidtFrm.getDay() != null && "2".equals(attendanceListEidtFrm.getDay()))
						ps.setString(11, "2");//午前半休
					else if (attendanceListEidtFrm.getDay() != null && "3".equals(attendanceListEidtFrm.getDay()))
						ps.setString(11, "3");//午後半休
					else if (attendanceListEidtFrm.getDay() != null && "4".equals(attendanceListEidtFrm.getDay()))
						ps.setString(11, "4");//特別休暇
					else
						ps.setString(11, "0");//休暇なし
					
					if (attendanceListEidtFrm.getCompensatory_leave() != null
							&& attendanceListEidtFrm.getCompensatory_leave().equals("true"))
						ps.setString(12, "1");
					else
						ps.setString(12, "0");
					
					ps.setString(13, attendanceListEidtFrm.getCompensatory_date());
					ps.setString(14, attendanceListEidtFrm.getOvertime_sunday());
					
					if (attendanceListEidtFrm.getCompensatory_date() != null && !attendanceListEidtFrm.getCompensatory_date().isEmpty()) {
						String compensatory_date = attendanceListEidtFrm.getCompensatory_date();
						compensatory_date = compensatory_date.replace("-", "/").substring(5);
						
						StringBuilder strBuilder=new StringBuilder();
						strBuilder.append(COMPENSATORY_CORRESPONDING_DATE);
						strBuilder.append(compensatory_date);
						strBuilder.append(COMMA);
						strBuilder.append(Integer.parseInt(compensatory_minute) / 60 );
						strBuilder.append(COMPENSATORY_HOUR);
						String compensatory_comment=strBuilder.toString();

						ps.setString(15, compensatory_comment);
					} else {
						ps.setString(15, "");
					}
					
					ps.setString(16, attendanceListEidtFrm.getBreak_time());
					ps.setTimestamp(17, fixed_time);
					ps.setTimestamp(18, rs.getTimestamp("created_date"));
					// 2020/10/19 GICM NC 勤務時間登録機能追加対応 End
					sessionmap.put("attendancelist_emp_cd", attendanceListEidtFrm.getEmp_cd());
					ps.executeUpdate();	
				}
				// get Monday of choose Date
				String start_date = getMondayDate(attendanceListEidtFrm.getChoose_date());
				
				//store work record
				createWorkRecord(attendanceListEidtFrm.getEmp_cd(), start_date, attendanceListEidtFrm.getChoose_date(), attendanceListEidtFrm.getWork_hour(), reportContentFrm);		
				
				return SUCCESS;
			}
		
			// else error occur
		} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		} finally {
			// 2020/11/09 GICM NC 勤務時間登録機能追加対応 Start
			if (rs != null ) {
				rs.close();
			}
			if (ps != null) {
				ps.close();
			}
			if (conn != null) {
				conn.close();
			}
			// 2020/11/09 GICM NC 勤務時間登録機能追加対応 End
		}
		return ERROR;
	}

	@Override
	public void setSession(Map<String, Object> session) {
		this.sessionmap = (SessionMap) session;
		sessionlist = (SessionMap) session;
	}

	public HttpServletRequest getServletRequest() {
		return request;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	/**
	 * 土日祝日の日付を取得
	 * 
	 * @param year
	 *            int
	 * @param month
	 *            int
	 * @return List<String>土日祝日一覧
	 * 
	 */
	public List<String> getHolidaysByMonth(int year, int month) throws AJDException {
		List<String> dayList = new ArrayList<String>();
		Month mon = new Month(year, month);
		for (AJD day : mon.getDays()) {
			int d = day.getDay();
			// 祝日を取得
			Holiday h = Holiday.getHoliday(day);
			String dayName = day.getWeek().getJpName();
			String date = String.format("%d-%02d-%02d", mon.getYear(), mon.getMonth(), d);
			if (h != null) {
				// 祝日の場合戻り値に日付を追加
				dayList.add(date);
			} else {
				if (dayName.equalsIgnoreCase("日")) {
					// 祝日でなくても土日の場合戻り値に日付を追加
					dayList.add(date);
				}
			}
		}
		return dayList;
	}

	/**
	 * システム日付取得
	 * 
	 * @return
	 */
	private String getSystemDate() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		Date date = new Date();
		return dateFormat.format(date);
	}

	/**
	 * 日付を加算する
	 * 
	 * @param inputDate
	 * @param addNo
	 * @return
	 */
	private String addDate(String inputDate, int addNo) {
		String outputDate = "";
		try {
			SimpleDateFormat sourceDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date dt = sourceDateFormat.parse(inputDate);
			Calendar cal = Calendar.getInstance();
			cal.setTime(dt);
			cal.add(Calendar.DATE, addNo);
			outputDate = sourceDateFormat.format(cal.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return outputDate;
	}

	/**
	 * 選択日の月曜日を取る
	 * 
	 * @param chooseDate
	 * @return
	 */
	private String getMondayDate(String choose_date) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		// convert String to LocalDate
		LocalDate chooseDate = LocalDate.parse(choose_date, formatter);
		;
		LocalDate started_date = chooseDate.with(previousOrSame(MONDAY));
		return started_date.toString();
	}

	/**
	 * 実績処理
	 * 
	 * @throws Exception
	 * 作成(5/2020)ssl
	 */
	@SuppressWarnings("resource")
	private void createWorkRecord(String emp_cd, String started_date, String choose_date, String work_hour, ReportContentForm reportContentFrm) throws Exception{
		Connection conn = null;
		ResultSet rs;
		conn = DataConnection.getConnection();	
		String sql = "SELECT report_cd FROM reportcontent WHERE emp_cd ='" +emp_cd+ "' AND started_date ='"+started_date+"'";
		PreparedStatement ps1 = conn.prepareStatement(sql);
		rs = ps1.executeQuery();
		String rptCd = "";
		if (rs != null && rs.next()) {
			rptCd = rs.getString("report_cd");
			sql = "SELECT * FROM reportcontentdetail WHERE report_cd ='" +rptCd+ "' AND work_date ='"+choose_date+"' ORDER BY work_date,reportdetail_cd";
			ps1 = conn.prepareStatement(sql);
			rs = ps1.executeQuery();
			List<ReportContentFormDetail> detailList = new ArrayList<ReportContentFormDetail>();
			while (rs.next()) {
				ReportContentFormDetail detail = new ReportContentFormDetail();
				detail.setReportdetail_cd(rs.getString("reportdetail_cd"));
				detail.setReport_cd(rs.getString("report_cd"));
				detail.setContents(rs.getString("content"));
				detail.setPlan_progress(rs.getString("plan_progress"));
				detail.setPlan_time(rs.getString("plan_time"));
				detail.setResult(rs.getString("result"));
				detail.setWork_date(rs.getString("work_date"));
				detail.setActual_time(rs.getString("actual_time"));
				detail.setActual_progress(rs.getString("actual_progress"));
				detail.setCreated_date(rs.getString("created_date"));
				detail.setUpdated_date(rs.getString("updated_date"));
				detailList.add(detail);
			}
			if (detailList.size() > reportContentFrm.getReportContentDetail().size()) {
				for (int i = 0; i < detailList.size(); i++) {
					detailList.get(i).setUpdated_date(getSystemDate());
					if (i == 0) {
						detailList.get(i).setActual_time(work_hour);
					}
					if (i < reportContentFrm.getReportContentDetail().size()) {
						detailList.get(i).setResult(reportContentFrm.getReportContentDetail().get(i).getResult());
						sql = "UPDATE reportcontentdetail SET result=?, actual_time=?, updated_date=? WHERE reportdetail_cd=?";
						ps1 = conn.prepareStatement(sql);
						ps1.setString(1, detailList.get(i).getResult());
						ps1.setString(2, detailList.get(i).getActual_time());
						ps1.setString(3, detailList.get(i).getUpdated_date());
						ps1.setString(4, detailList.get(i).getReportdetail_cd());
						ps1.executeUpdate();
					} else {
						detailList.get(i).setResult("");
						if(detailList.get(i).getContents().equalsIgnoreCase("")||detailList.get(i).getContents().isEmpty()){
							sql = "DELETE FROM reportcontentdetail WHERE reportdetail_cd=?";
							ps1 = conn.prepareStatement(sql);
							ps1.setString(1, detailList.get(i).getReportdetail_cd());
							ps1.executeUpdate();
						} else {
							sql = "UPDATE reportcontentdetail SET result=?, actual_time=?, updated_date=? WHERE reportdetail_cd=?";
							ps1 = conn.prepareStatement(sql);
							ps1.setString(1, detailList.get(i).getResult());
							ps1.setString(2, detailList.get(i).getActual_time());
							ps1.setString(3, detailList.get(i).getUpdated_date());
							ps1.setString(4, detailList.get(i).getReportdetail_cd());
							ps1.executeUpdate();
						}
					}

				}
			} else {
				for (int i = 0; i < reportContentFrm.getReportContentDetail().size(); i++) {

					if (i == 0) {
						reportContentFrm.getReportContentDetail().get(i).setActual_time(work_hour);
						detailList.get(i).setActual_time(work_hour);
					}
					if (i < detailList.size()) {
						detailList.get(i).setUpdated_date(getSystemDate());
						detailList.get(i).setResult(reportContentFrm.getReportContentDetail().get(i).getResult());
						sql = "UPDATE reportcontentdetail SET result=?, actual_time=?, updated_date=? WHERE reportdetail_cd=?";
						ps1 = conn.prepareStatement(sql);
						ps1.setString(1, detailList.get(i).getResult());
						ps1.setString(2, detailList.get(i).getActual_time());
						ps1.setString(3, detailList.get(i).getUpdated_date());
						ps1.setString(4, detailList.get(i).getReportdetail_cd());
						ps1.executeUpdate();
					} else {
						reportContentFrm.getReportContentDetail().get(i).setReport_cd(detailList.get(0).getReport_cd());
						reportContentFrm.getReportContentDetail().get(i).setContents("");
						reportContentFrm.getReportContentDetail().get(i).setPlan_progress("");
						reportContentFrm.getReportContentDetail().get(i).setPlan_time("");
						reportContentFrm.getReportContentDetail().get(i).setWork_date(detailList.get(0).getWork_date());
						if (i != 0) {
							reportContentFrm.getReportContentDetail().get(i).setActual_time("");
						}
						reportContentFrm.getReportContentDetail().get(i).setActual_progress("");

						sql = "INSERT INTO reportcontentdetail (report_cd, work_date, content, plan_progress, plan_time, result, actual_progress, actual_time, created_date, updated_date) VALUES(?,?,?,?,?,?,?,?,?,?)";

						ps1 = conn.prepareStatement(sql);
						ps1.setString(1, reportContentFrm.getReportContentDetail().get(i).getReport_cd());
						ps1.setString(2, reportContentFrm.getReportContentDetail().get(i).getWork_date());
						ps1.setString(3, reportContentFrm.getReportContentDetail().get(i).getContents());
						ps1.setString(4, reportContentFrm.getReportContentDetail().get(i).getPlan_progress());
						ps1.setString(5, reportContentFrm.getReportContentDetail().get(i).getPlan_time());
						ps1.setString(6, reportContentFrm.getReportContentDetail().get(i).getResult());
						ps1.setString(7, reportContentFrm.getReportContentDetail().get(i).getActual_progress());
						ps1.setString(8, reportContentFrm.getReportContentDetail().get(i).getActual_time());
						ps1.setString(9, getSystemDate());
						ps1.setString(10, getSystemDate());
						ps1.executeUpdate();
					}
				}
			}
		} else {
			sql = "INSERT INTO reportcontent (emp_cd, started_date, customer_name, manager_comment, pe_comment, gl_comment, emp_comment, created_date, updated_date) VALUES(?,?,?,?,?,?,?,?,?)";
			ps1 = conn.prepareStatement(sql);
			ps1.setString(1, emp_cd);
			ps1.setString(2, started_date);
			ps1.setString(3, "-1");
			ps1.setString(4, "");
			ps1.setString(5, "");
			ps1.setString(6, "");
			ps1.setString(7, "");
			ps1.setString(8, getSystemDate());
			ps1.setString(9, getSystemDate());
			ps1.executeUpdate();	
			sql = "SELECT report_cd FROM reportcontent WHERE emp_cd ='" + emp_cd + "' AND started_date ='"+started_date+"'";
			ps1 = conn.prepareStatement(sql);
			rs = ps1.executeQuery();
			if (rs != null && rs.next()) {
				rptCd = rs.getString("report_cd");
			}
			if (!rptCd.isEmpty()) {
				List<ReportContentFormDetail> detailListOldList = new ArrayList<ReportContentFormDetail>();
				for (int i = 0; i < 7; i++) {
					ReportContentFormDetail detail = new ReportContentFormDetail();
					detail.setWork_date(addDate(started_date, i));
					detailListOldList.add(detail);
				}
				List<ReportContentFormDetail> detailListNewList = new ArrayList<ReportContentFormDetail>();
				for (ReportContentFormDetail detailOld : detailListOldList) {
					if (detailOld.getWork_date().equalsIgnoreCase(choose_date)) {
						int count = 0;
						for (ReportContentFormDetail chooseDateDetail : reportContentFrm.getReportContentDetail()) {
							chooseDateDetail.setWork_date(choose_date);
							if (count == 0) {
								chooseDateDetail.setActual_time(work_hour);
							}
							detailListNewList.add(chooseDateDetail);
							count++;
						}
					} else {
						detailListNewList.add(detailOld);
					}
				}
				for (ReportContentFormDetail detail : detailListNewList) {
					sql = "INSERT INTO reportcontentdetail (report_cd, work_date, content, plan_progress, plan_time, result, actual_progress, actual_time, created_date, updated_date) VALUES(?,?,?,?,?,?,?,?,?,?)";
					ps1 = conn.prepareStatement(sql);
					ps1.setString(1, rptCd);
					ps1.setString(2, detail.getWork_date());
					ps1.setString(3, detail.getContents());
					ps1.setString(4, detail.getPlan_progress());
					ps1.setString(5, detail.getPlan_time());
					ps1.setString(6, detail.getResult());
					ps1.setString(7, detail.getActual_progress());
					ps1.setString(8, detail.getActual_time());
					ps1.setString(9, getSystemDate());
					ps1.setString(10, getSystemDate());
					ps1.executeUpdate();
				}
			}
		}
	}
}