/**
 * 勤務時間サマリ情報インポート画面	
 * 作成履歴：2020/10/06 Theint Sandari Kyaw
 * 作成概要：新規作成　勤務時間サマリ情報をインポートする
 */
package com.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts2.dispatcher.SessionMap;
import org.apache.struts2.interceptor.SessionAware;

import com.common.CommonCheck;
import com.common.CommonConstant;
import com.common.FileHelper;
import com.common.Month;
import com.common.SummaryFileValidation;
import com.jdbc.DataConnection;
import com.model.AttendanceSummaryForm;
import com.model.ReportTime;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.inject.Scope;
import com.opensymphony.xwork2.inject.Scoped;

@Scoped(Scope.SESSION)
public class AttendanceSummaryImportAction extends ActionSupport implements SessionAware {
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

	/**
	 * 初期表示
	 * @return AttendanceSummaryImport
	 */
	@Override
	public String execute() throws Exception {
		// 権限チェック
		if (sessionmap.get("ID") == null) {
			return "session";
		} else if (!CommonCheck.isValidRole(CommonConstant.SCREEN_NO.ATTENDANCE_SUMMARY_IMPORT.value, sessionmap.get("ID").toString())) {
			return "role";
		}
		return "AttendanceSummaryImport";
	}

	/**
	 * 勤務時間サマリ情報Excelファイルデータを読み取る
	 * @return
	 * @throws FileNotFoundException
	 */
	public String attendanceSummaryImport() throws FileNotFoundException {
		SummaryFileValidation summaryFileValidation = new SummaryFileValidation();
		if (null != this.file) {
			if (summaryFileValidation.validFileExtension(this.fileContentType)) {
				addActionError("Ｅｘｃｅｌファイル以外は無効です。");
				return INPUT;
			}
			try {
				FileInputStream stream = new FileInputStream(this.file);
				FileHelper fileHelper = new FileHelper();
				this.reportTimes.addAll(fileHelper.readSumFile(stream, 0));
				if (summaryFileValidation.invalidFileFormat(this.reportTimes.get(0).getExcelData(),
						this.reportTimes.get(1).getExcelData())) {
					addActionError("アップロードしたファイルは勤怠ファイルではありません。");
					this.reportTimes = null;
					return INPUT;
				} else if (this.reportTimes.size() < 3) {
					addActionError("アップロードしたファイルに勤怠データがありません。");
					this.reportTimes = null;
				} else {
					this.reportTimes.remove(0);
					this.reportTimes.remove(0);
					sessionmap.put("reportTimes", this.reportTimes);
					return SUCCESS;
				}
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
	 * 勤務時間サマリ情報をデータベースに登録する
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String insertSummaryImportList() {
		try {
			// 権限チェック
			if (sessionmap.get("ID") == null) {
				return "session";
			} else if (!CommonCheck.isValidRole(CommonConstant.SCREEN_NO.ATTENDANCE_SUMMARY_IMPORT.value,
					sessionmap.get("ID").toString())) {
				return "role";
			}
			this.reportTimes = (ArrayList<ReportTime>) sessionmap.get("reportTimes");
			Connection conn = null;
			conn = DataConnection.getConnection();
			conn.setAutoCommit(false);
			ResultSet rs;
			ArrayList<String> messages = new ArrayList<>();
			AttendanceSummaryForm attendanceSumFrm = new AttendanceSummaryForm();
			int year = Calendar.getInstance().get(Calendar.YEAR);
			int oldMonth = month.getId() + 1;
			String chgdate = year + "-" + oldMonth;
			DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-M");
			YearMonth yearMonth = YearMonth.parse(chgdate, dateFormat);
			LocalDate parsedDate = yearMonth.atDay(1);
			String saveDate = parsedDate.toString();
			
			String query1 = "SELECT * FROM attendancesummary WHERE DATE_FORMAT(summary_month, \"%Y-%m\")='" + yearMonth + "'";
			PreparedStatement ps1 = conn.prepareStatement(query1);
			rs = ps1.executeQuery();
			if (rs.next()) {
				String query2 = "DELETE FROM attendancesummary";
				query2 = query2 + " WHERE DATE_FORMAT(summary_month, \"%Y-%m\") = '"+yearMonth+"'";
				PreparedStatement ps2 = conn.prepareStatement(query2);
				ps2.executeUpdate();
				conn.commit();

				String query3 = "DELETE FROM leavesummary";
				query3 = query3 + " WHERE DATE_FORMAT(summary_month, \"%Y-%m\") = '"+yearMonth+"'";
				PreparedStatement ps3 = conn.prepareStatement(query3);
				ps3.executeUpdate();
				conn.commit();
			}
			
			for (int i = 0; i < this.reportTimes.size(); i++) {
			
				String emp_cd = this.reportTimes.get(i).getExcelData().get(0);
				String queryCount = "SELECT COUNT(*) AS rowcount FROM employeemaster WHERE emp_cd = '"+ emp_cd + "'";
				Statement stmt = conn.createStatement();
				ResultSet r = stmt.executeQuery(queryCount);
				r.next();
				if (r.getInt("rowcount") > 0) {
					attendanceSumFrm = concactToALFD(this.reportTimes.get(i).getExcelData());
					Timestamp fixed_time = new Timestamp(new Date().getTime());
					String summaryQuery = "INSERT INTO attendancesummary(emp_cd, summary_month, total_work_hour, legal_overtime, "
							+ "legal_over_workday, midnight_work_hour, created_date, modified_date) "
							+ "VALUES(?,?,?,?,?,?,?,?)";
					PreparedStatement summaryPs = conn.prepareStatement(summaryQuery);
					summaryPs.setString(1, attendanceSumFrm.getEmp_cd());
					summaryPs.setString(2, saveDate);
					summaryPs.setString(3, attendanceSumFrm.getTotal_work_hour());
					summaryPs.setString(4, attendanceSumFrm.getLegal_overtime());
					summaryPs.setString(5, attendanceSumFrm.getLegal_over_workday());
					summaryPs.setString(6, attendanceSumFrm.getMidnight_work_hour());
					summaryPs.setTimestamp(7, fixed_time);
					summaryPs.setTimestamp(8, fixed_time);
					summaryPs.executeUpdate();
					conn.commit();

					String leaveQuery = "INSERT INTO leavesummary (emp_cd, summary_month,wholeday_leave, halfday_leave, releaving_leave, compensatory_leave)"
							+ "VALUES (?, ?," + "(SELECT COUNT(day) AS wholeday_leave " + "FROM attendance "
							+ "WHERE day='1' " + "AND emp_cd='" + attendanceSumFrm.getEmp_cd() + "' "
							+ "AND DATE_FORMAT(choose_date, \"%Y-%m\")= '" + yearMonth + "')," +

							"(SELECT COUNT(day) AS halfday_leave " + "FROM attendance " + "WHERE (day='2' " + "OR day='3')"
							+ "AND emp_cd='" + attendanceSumFrm.getEmp_cd() + "' "
							+ "AND DATE_FORMAT(choose_date, \"%Y-%m\")= '" + yearMonth + "')," +

							"(SELECT COUNT(day) AS releaving_leave " + "FROM attendance " + "WHERE day='4' "
							+ "AND emp_cd='" + attendanceSumFrm.getEmp_cd() + "' "
							+ "AND DATE_FORMAT(choose_date, \"%Y-%m\")= '" + yearMonth + "')," +

							"(SELECT COUNT(compensatory_leave) AS compensatory_leave " + "FROM attendance "
							+ "WHERE compensatory_leave='1' " + "AND emp_cd='" + attendanceSumFrm.getEmp_cd() + "' "
							+ "AND DATE_FORMAT(choose_date, \"%Y-%m\")= '" + yearMonth + "') )";

					PreparedStatement leavePs = conn.prepareStatement(leaveQuery);
					leavePs.setString(1, attendanceSumFrm.getEmp_cd());
					leavePs.setString(2, saveDate);
					leavePs.executeUpdate();
					conn.commit();
				} else {
					messages.add(emp_cd);
				}
			}
			if (messages.size() == 0) {
				addActionMessage("登録しました。");
				this.reportTimes = null;
			} else if (messages.size() < reportTimes.size() - 1) {
				addActionError("以下の社員番号以外は登録しました。");
				for (String msg : messages) {
					addActionError(msg);
				}
				this.reportTimes = null;
			}
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

	/**
	 * リストからデータを読み取り、モデルに設定する
	 * @param list
	 * @return attendanceSumFrm
	 */
	private AttendanceSummaryForm concactToALFD(ArrayList<String> list) {
		AttendanceSummaryForm attendanceSumFrm = new AttendanceSummaryForm();
		String str1 = list.get(0);
		String str2 = list.get(2);
		String str3 = list.get(3);
		String str4 = list.get(4);
		String str5 = list.get(5);

		attendanceSumFrm.setEmp_cd(str1);
		attendanceSumFrm.setTotal_work_hour(str2);
		attendanceSumFrm.setLegal_overtime(str3);
		attendanceSumFrm.setLegal_over_workday(str4);
		attendanceSumFrm.setMidnight_work_hour(str5);
		return attendanceSumFrm;
	}
}