/**
 *
勤務時間インポート
 * 作成履歴：2020/10/19 GICM AMTD
 * 作成概要：更新　勤務時間情報をインポートする
 */
package com.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.struts2.dispatcher.SessionMap;
import org.apache.struts2.interceptor.SessionAware;

import com.common.CommonCheck;
import com.common.CommonConstant;
import com.common.FileHelper;
import com.common.FileValidation;
import com.common.Month;
import com.jdbc.DataConnection;
import com.model.AttendanceListFormDetail;
import com.model.ReportTime;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.inject.Scope;
import com.opensymphony.xwork2.inject.Scoped;

@Scoped(Scope.SESSION)
public class AttendanceImportAction extends ActionSupport implements SessionAware {
	private static final long serialVersionUID = 1L;
	private File file;
	private String fileContentType;
	private Month month;
	HttpServletRequest request;
	private SessionMap<String, Object> sessionmap;
	private ArrayList<ReportTime> reportTimes = new ArrayList<>();

	public String getFileContentType() {
		return fileContentType;
	}

	public void setFileContentType(String fileContentType) {
		this.fileContentType = fileContentType;
	}

	public Month getMonth() {
		return month;
	}

	public void setMonth(Month month) {
		this.month = month;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public ArrayList<ReportTime> getReportTimes() {
		return reportTimes;
	}

	public void setReportTimes(ArrayList<ReportTime> reportTimes) {
		this.reportTimes = reportTimes;
	}

	@Override
	public String execute() throws Exception {
		// 権限チェック
		if (sessionmap.get("ID") == null) {
			return "session";
		} else if (!CommonCheck.isValidRole(CommonConstant.SCREEN_NO.IMPORT.value, sessionmap.get("ID").toString())) {
			return "role";
		}
		return "AttendanceImport";
	}

	/**
	 * Ｅｘｃｅｌファイルを読む
	 * @return
	 * @throws FileNotFoundException
	 */
	@SuppressWarnings("resource")
	public String attendanceImport() throws FileNotFoundException {
		FileValidation fileValidation = new FileValidation();
		if (null != this.file) {
			if (fileValidation.validFileExtension(this.fileContentType)) {
				addActionError("Ｅｘｃｅｌファイル以外は無効です。");
				return INPUT;
			}
			try {
				FileInputStream stream = new FileInputStream(this.file);
				//2020/10/19 GICM AMTD アップロードしたファイルの社員番号を読む　対応　start
				FileInputStream stream2 = new FileInputStream(this.file);
				FileHelper fileHelper = new FileHelper();
				Workbook workbook = new XSSFWorkbook(stream2);
				XSSFSheet sheet = (XSSFSheet) workbook.getSheetAt(month.getId());
				XSSFRow row = sheet.getRow(0);
				XSSFCell cell = row.getCell(7);
				String emp_cd = cell.getStringCellValue();
				
				this.reportTimes.addAll(fileHelper.readFile(stream, month.getId()));
				if (fileValidation.invalidFileFormat(this.reportTimes.get(0).getExcelData())) {
					addActionError("アップロードしたファイルは勤怠ファイルではありません。");
					this.reportTimes = null;
					return INPUT;
				} else if (this.reportTimes.size() < 2) {
					addActionError("アップロードしたファイルに勤怠データがありません。");
					this.reportTimes = null;
				}else if(emp_cd.equals("")) {
					addActionError("アップロードしたファイルに社員番号がありません。");
					this.reportTimes = null;
					return INPUT;
				}else {
					boolean chk = chkMasterData(emp_cd);
					if(!chk) {
						addActionError("社員番号は社員マスターテーブルに存在していないです。"); 
						this.reportTimes = null;
						return INPUT;
					}else {
						fileHelper.totalResult(this.reportTimes);
						this.reportTimes.remove(0);
						sessionmap.put("reportTimes", this.reportTimes);
						sessionmap.put("emp_cd", emp_cd);
						return SUCCESS;
					}
				}
				//2020/10/19 GICM AMTD  Ｅｘｃｅｌアップロードしたファイルの社員番号を読む　対応　end
			} catch (Exception e) {
				addActionError("アップロードしたファイルは勤怠ファイルではありません");
				this.reportTimes = null;
				return INPUT;
			}
		} else {
			addActionError("Ｅｘｃｅｌファイルを選択してください。");
			this.reportTimes = null;
			return INPUT;
		}
		return SUCCESS;
	}

	/**
	 * マスター存在チェック
	 * @param emp_cd
	 * @return
	 * @throws Exception 
	 * 2020/10/21 GICM AMTD 新規作成
	 */
	private boolean chkMasterData(String emp_cd) throws Exception {
		String sql = "SELECT emp_cd FROM employeemaster WHERE emp_cd = '"+ emp_cd.trim() + "'";
		Connection conn = null;
			conn = DataConnection.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				return true;
			}
		return false;
	}

	@SuppressWarnings("unchecked")
	public String insertImportList() {
		try {
			// 権限チェック
			if (sessionmap.get("ID") == null) {
				return "session";
			} else if (!CommonCheck.isValidRole(CommonConstant.SCREEN_NO.ATTENDANCE_ENTRY.value,
					sessionmap.get("ID").toString())) {
				return "role";
			}
			this.reportTimes = (ArrayList<ReportTime>) sessionmap.get("reportTimes");
			Connection conn = null;
			conn = DataConnection.getConnection();
			conn.setAutoCommit(false);
			ResultSet rs;
			ArrayList<String> messages = new ArrayList<>();
			for (int i = 0; i < this.reportTimes.size() - 1; i++) {
				AttendanceListFormDetail attendanceListEidtFrm = new AttendanceListFormDetail();
				attendanceListEidtFrm = concactToALFD(this.reportTimes.get(i).getExcelData());
				Timestamp fixed_time = new Timestamp(new Date().getTime());
				if (!checkDate(attendanceListEidtFrm)) {
					String query2 = "SELECT * FROM attendance WHERE choose_date='"
							+ attendanceListEidtFrm.getChoose_date() + "'AND emp_cd='"
							+ attendanceListEidtFrm.getEmp_cd() + "'";
					PreparedStatement ps2 = conn.prepareStatement(query2);
					rs = ps2.executeQuery();
					
					/*if (!rs.next()) {
						String query = "INSERT INTO attendance(start_time, end_time, choose_date, "
								+ "work_hour, overtime, midnight_overtime, "
								+ "compensatory_leave_hour, releaving_leave_hour, task_description, "
								+ "midnight_compensatory_leave_hour, created_date, modified_date, "
								+ "emp_cd, day, break_time) " + "VALUES(?,?,?," + "?,?,?," + "?,?,?," + "?,?,?,"
								+ "?,?,?)";
						PreparedStatement ps = conn.prepareStatement(query);
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
						
						// 休日確認		
						String wkhr = attendanceListEidtFrm.getWork_hour();
						if(wkhr.equals("00:00"))
						{
							ps.setString(14, "1");
						}
						else
						{
							ps.setString(14, "0");
						}
						
						ps.setString(15, attendanceListEidtFrm.getBreak_time());
						ps.executeUpdate();
						conn.commit();
					} else {
						messages.add(attendanceListEidtFrm.getChoose_date());
					}*/
					
					//2020/10/19 GICM AMTD 勤務時間情報を登録する　対応　start
					if (!rs.next()) {
						String query = "INSERT INTO attendance(start_time, end_time, choose_date, "
								+ "work_hour, overtime,overtime_sunday,  "
								+ "midnight_overtime,compensatory_comment, task_description, "
								+ " created_date, modified_date, "
								+ "emp_cd, day, break_time) " + "VALUES(?,?,?," + "?,?,?," + "?,?,?," + "?,?,"
								+ "?,?,?)";
						PreparedStatement ps = conn.prepareStatement(query);
						ps.setString(1, attendanceListEidtFrm.getStart_time());
						ps.setString(2, attendanceListEidtFrm.getEnd_time());
						ps.setString(3, attendanceListEidtFrm.getChoose_date());
						ps.setString(4, attendanceListEidtFrm.getWork_hour());
						ps.setString(5, attendanceListEidtFrm.getOvertime());
						ps.setString(6, attendanceListEidtFrm.getOvertime_sunday());
						ps.setString(7, attendanceListEidtFrm.getMidnight_overtime());
						ps.setString(8, attendanceListEidtFrm.getCompensatory_comment());
						ps.setString(9, attendanceListEidtFrm.getTask_description());
						ps.setTimestamp(10, fixed_time);
						ps.setTimestamp(11, fixed_time);
						ps.setString(12, attendanceListEidtFrm.getEmp_cd());
						
						// 休日確認		
						String wkhr = attendanceListEidtFrm.getWork_hour();
						if(wkhr.equals("00:00"))
						{
							ps.setString(13, "1");
						}
						else
						{
							ps.setString(13, "0");
						}
						
						ps.setString(14, attendanceListEidtFrm.getBreak_time());
						ps.executeUpdate();
						conn.commit();
					} else {
						messages.add(attendanceListEidtFrm.getChoose_date());
					}
					
				}
			}
			if (messages.size() == 0) {
				addActionMessage("登録しました。");
				this.reportTimes = null;
			} else if (messages.size() < reportTimes.size() - 1) {
				addActionError("以下の日付以外は登録しました。");
				for (String msg : messages) {
					addActionError(msg);
					this.reportTimes = null;
				}
			} else if (messages.size() == reportTimes.size() - 1) {
				addActionError("アップロードした時間外勤務ファイルは登録済の為登録出来ません。");
				this.reportTimes = null;
			}
			//2020/10/19 GICM AMTD 勤務時間情報を登録する　対応　end
		} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}
		return SUCCESS;
	}

	public Month[] getMonths() {
		Month[] months = Month.values();
		return months;
	}

	@Override
	public void setSession(Map<String, Object> arg0) {
		this.sessionmap = (SessionMap<String, Object>) arg0;
	}

	/*
	 * Excelファイルデータを設定する
	 * 2020/10/19 GICM AMTD　対応
	 */
	private AttendanceListFormDetail concactToALFD(ArrayList<String> list) {
		AttendanceListFormDetail attendanceListEidtFrm = new AttendanceListFormDetail();
		String str0 = list.get(0);
		String str2 = list.get(2);
		String str3 = list.get(3);
		String str4 = list.get(4);
		String str5 = list.get(5);
		String str6 = list.get(6);
		String str7 = list.get(7);
		String str8 = list.get(8);
		String str9 = list.get(9);
		String str10 = list.get(10);
		String str11 = list.get(11);
		//String str12 = list.get(12);
		attendanceListEidtFrm.setChoose_date(str0);
		if (1 == Integer.parseInt(str2)) {
			attendanceListEidtFrm.setDay("true");
		} else if (0 == Integer.parseInt(str2)) {
			attendanceListEidtFrm.setDay("false");
		}
		attendanceListEidtFrm.setStart_time(str3);
		attendanceListEidtFrm.setEnd_time(str4);
		attendanceListEidtFrm.setBreak_time(str5);
		attendanceListEidtFrm.setWork_hour(str6);
		attendanceListEidtFrm.setOvertime(str7);
		//attendanceListEidtFrm.setCompensatory_leave_hour(str9);
		//attendanceListEidtFrm.setReleaving_leave_hour(str10);
		//attendanceListEidtFrm.setMidnight_compensatory_leave_hour(str11);
		attendanceListEidtFrm.setOvertime_sunday(str8);
		attendanceListEidtFrm.setMidnight_overtime(str9);
		attendanceListEidtFrm.setCompensatory_comment(str10);
		attendanceListEidtFrm.setTask_description(str11);
		//String emp_cd = (String) sessionmap.get("ID");
		String emp_cd = (String) sessionmap.get("emp_cd");
		attendanceListEidtFrm.setEmp_cd(emp_cd);
		return attendanceListEidtFrm;
	}

	/**
	 * 日付チェック
	 * @param detail
	 * @return
	 * 2020/10/19 GICM AMTD　対応
	 */
	private boolean checkDate(AttendanceListFormDetail detail) {
		String start_time = detail.getStart_time();
		String end_time = detail.getEnd_time();
		String work_hour = detail.getWork_hour();
		String overtime = detail.getOvertime();
		String overtime_sunday = detail.getOvertime_sunday(); 
		String midnight_overtime = detail.getMidnight_overtime();
		String compensatory_comment  = detail.getCompensatory_comment();
		//String compensatory_leave_hour = detail.getCompensatory_leave_hour();
		//String releaving_leave_hour = detail.getReleaving_leave_hour();
		String task_description = detail.getTask_description();
		//String midnight_compensatory_leave_hour = detail.getMidnight_compensatory_leave_hour();
		String break_time = detail.getBreak_time();
		/*boolean flag = start_time.isEmpty() && end_time.isEmpty() && work_hour.isEmpty() && overtime.isEmpty()
				&& midnight_overtime.isEmpty() && compensatory_leave_hour.isEmpty() && releaving_leave_hour.isEmpty()
				&& task_description.isEmpty() && midnight_compensatory_leave_hour.isEmpty() && break_time.isEmpty();*/
		boolean flag = start_time.isEmpty() && end_time.isEmpty() && work_hour.isEmpty() && overtime.isEmpty()
				&& overtime_sunday.isEmpty() && midnight_overtime.isEmpty() && compensatory_comment.isEmpty()
				&& task_description.isEmpty() && break_time.isEmpty();
		return flag;
	}
}