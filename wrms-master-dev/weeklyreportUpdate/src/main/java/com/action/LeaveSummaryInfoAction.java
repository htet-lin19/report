/**
 * 休暇情報管理画面	
 * 作成履歴：2020/10/13 Kyaw Zin Phyoe
 * 作成概要：新規作成　休暇サマリ情報ダウンロード・アップロードレイアウトする
 */
package com.action;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.ss.usermodel.FormulaEvaluator;
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
import com.common.LeaveFileValidation;
import com.common.Month;
import com.jdbc.DataConnection;
import com.model.LeaveSummaryInfoDetail;
import com.model.ReportTime;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.inject.Scope;
import com.opensymphony.xwork2.inject.Scoped;

@Scoped(Scope.SESSION)
public class LeaveSummaryInfoAction extends ActionSupport implements SessionAware {
	private static final long serialVersionUID = 1L;
	HttpServletRequest request;
	private SessionMap<String, Object> sessionmap;
	private InputStream inputStream;
	private String exportFile;
	private Month month;
	private File file;
	private String fileContentType;
	private ArrayList<ReportTime> reportTimes = new ArrayList<>();
	List<LeaveSummaryInfoDetail> leaveInfoList = new ArrayList<LeaveSummaryInfoDetail>();

	/**
	 * 初期表示
	 * 
	 * @return leaveSummaryInfo
	 */
	@Override
	public String execute() throws Exception {
		//権限チェック
		if (sessionmap.get("ID") == null) {
			return "session";
		} else if (!CommonCheck.isValidRole(CommonConstant.SCREEN_NO.LEAVE_SUMMARY_INFO.value,
				sessionmap.get("ID").toString())) {
			return "role";
		}

		return "leaveSummaryInfo";
	}

	/**
	 * 休暇サマリ情報Excel出力
	 * 
	 * @return
	 */
	public String exportExcel() {
		try {

			//権限チェック
			if (sessionmap.get("ID") == null) {
				return "session";
			} else if (!CommonCheck.isValidRole(CommonConstant.SCREEN_NO.LEAVE_SUMMARY_INFO.value,
					sessionmap.get("ID").toString())) {
				return "role";
			}
			
			//休暇レイアウト
			String templateFile = "LeaveSummary.xlsx";
            
			//String templateFile = "LeaveSummary.xlsx";
			ClassLoader classLoader = this.getClass().getClassLoader();
			InputStream inputStream = classLoader.getResourceAsStream(templateFile);
			
			//Excel出力ための、休暇サマリ情報のリストを取得
			List<LeaveSummaryInfoDetail> leaveInfoList = fetchList();
			
			if (leaveInfoList.size() == 0) {
				addActionError("対象データが存在しません。");
				return ERROR;
			}

			XSSFWorkbook leavebook = new XSSFWorkbook(inputStream);
			XSSFSheet sheet = leavebook.getSheetAt(0);
			
			String exportFile1 = "LeaveSummary" + "_" + LocalDate.now().getYear() + String.format("%02d",month.getId()+1) + ".xlsx";
			exportFile = exportFile1;
            
			//データ設定
			writeLeave(leavebook, leaveInfoList, sheet, month.getId());
			
			try {
				ByteArrayOutputStream boas = new ByteArrayOutputStream();
				leavebook.write(boas);
				setInputStream(new ByteArrayInputStream(boas.toByteArray()));
				
			} catch (IOException e) {
				e.printStackTrace();
				return ERROR;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return ERROR;
		} catch (IOException e) {
			e.printStackTrace();
			return ERROR;
		} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}

		return SUCCESS;
	}

	/**
	 * Excelに書き込む
	 * 
	 * @param leaveInfoList
	 * @param sheet
	 */
	private void writeLeave(XSSFWorkbook leavebook, List<LeaveSummaryInfoDetail> leaveInfoList, XSSFSheet sheet,
			int month) {
		// 日付
		LocalDate date = LocalDate.now();
		int year = date.getYear();
		int m = month + 1;
		String str1 = String.valueOf(year);
		String str2 = String.valueOf(m);
		String str = str1 + "/" + str2;
		XSSFRow row = sheet.getRow(0);
		XSSFCell cell = row.getCell(5);
		cell.setCellValue(str);

		//スタートインデックス設定
		int startRow = 3;
		for (LeaveSummaryInfoDetail detail : leaveInfoList) {
			int c = startRow++;
			for (int i = c; i < 3 + leaveInfoList.size();) {

				sheet.getRow(i).getCell(0).setCellValue(i - 2);
				
				//社員番号
				sheet.getRow(i).getCell(1).setCellValue(detail.getEmp_cd());
				//氏名
				sheet.getRow(i).getCell(2).setCellValue(detail.getEmp_name());
				
				if(detail.getLastyear_carry_leave() != 0.0) {
					//前年度繰越分
					sheet.getRow(i).getCell(3).setCellValue(new BigDecimal(detail.getLastyear_carry_leave()).toPlainString());
				}
				if(detail.getCur_year_allowed_leave() != 0.0) {
					//今年度付与分
					sheet.getRow(i).getCell(4).setCellValue(new BigDecimal(detail.getCur_year_allowed_leave()).toPlainString());
				}
				if(detail.getTotal_allowed_leave() != 0.0) {
					//取得可能日数
					sheet.getRow(i).getCell(5).setCellValue(new BigDecimal(detail.getTotal_allowed_leave()).toPlainString());
				}
				if(detail.getRemaining_leave() != 0.0) {
					//有給残日数
					sheet.getRow(i).getCell(6).setCellValue(new BigDecimal(detail.getRemaining_leave()).toPlainString());	
				}
				if(detail.getHalfday_leave() != 0) {
					//半休
					sheet.getRow(i).getCell(7).setCellValue(detail.getHalfday_leave());
				}
				if(detail.getWholeday_leave() != 0) {
					//全休
					sheet.getRow(i).getCell(8).setCellValue(detail.getWholeday_leave());
				}
				if(detail.getReleaving_leave() != 0) {
					//特別休暇
					sheet.getRow(i).getCell(9).setCellValue(detail.getReleaving_leave());
				}
				if(detail.getCompensatory_leave() != 0) {
					//代休
					sheet.getRow(i).getCell(10).setCellValue(detail.getCompensatory_leave());
				}	
				break;
			}
		}
		FormulaEvaluator evaluator = leavebook.getCreationHelper().createFormulaEvaluator();
		evaluator.evaluateAll();

	}
	
	/**
	 * 対象社員の休暇詳細情報を取得
	 * @return　休暇サマリ
	 */
	private List<LeaveSummaryInfoDetail> fetchList() {
		try {
			LocalDate localDate = LocalDate.now();
			LocalDate date = localDate.withMonth(month.getId() + 1);
            
			Connection conn = DataConnection.getConnection();
			
			String query = "SELECT tbl1.emp_cd, tbl1.emp_name, tbl1.lastyear_carry_leave, tbl1.cur_year_allowed_leave, tbl1.total_allowed_leave,\r\n" + 
					"tbl1.remaining_leave, tbl2.halfday_leave, tbl2.wholeday_leave, tbl2.releaving_leave, tbl2.compensatory_leave \r\n" + 
					"FROM\r\n" + 
					"(\r\n" + 
					" SELECT emp.emp_cd AS emp_cd, emp.emp_name AS emp_name, le.lastyear_carry_leave AS lastyear_carry_leave, \r\n" + 
					" le.cur_year_allowed_leave AS cur_year_allowed_leave, le.total_allowed_leave AS total_allowed_leave,\r\n" + 
					"(le.total_allowed_leave-(SUM(ls.wholeday_leave)+SUM(if(ls.halfday_leave != 0, ls.halfday_leave/2, 0)))) as remaining_leave\r\n" + 
					"FROM employeemaster emp LEFT JOIN leaves le ON le.emp_cd = emp.emp_cd\r\n" + 
					"AND IF(DATE_FORMAT('"+ date +"','%m')>'04',\r\n" + 
					"YEAR(le.target_year) = YEAR('"+ date +"'), \r\n" + 
					"YEAR(le.target_year) = YEAR(CONCAT(YEAR('"+ date +"')-1,'-01-01')))\r\n" + 
					"LEFT JOIN leavesummary ls ON ls.emp_cd = emp.emp_cd\r\n" + 
					"AND IF(DATE_FORMAT('"+ date +"','%m')>'04',\r\n" + 
					"DATE_FORMAT(ls.summary_month,'%Y-%m') >= DATE_FORMAT(CONCAT(YEAR('"+ date +"'),'-04-01'),'%Y-%m') AND \r\n" + 
					"DATE_FORMAT(ls.summary_month,'%Y-%m') < DATE_FORMAT('"+ date +"','%Y-%m'),\r\n" + 
					"DATE_FORMAT(ls.summary_month,'%Y-%m') >= DATE_FORMAT(CONCAT(YEAR('"+ date +"')-1,'-04-01'),'%Y-%m') AND\r\n" + 
					"DATE_FORMAT(ls.summary_month,'%Y-%m') < DATE_FORMAT('"+ date +"','%Y-%m'))\r\n" + 
					"GROUP BY emp_cd\r\n" + 
					")tbl1\r\n" + 
					"LEFT JOIN\r\n" + 
					"(\r\n" + 
					"SELECT emp.emp_cd AS emp_cd, SUM(ls.halfday_leave) AS halfday_leave, SUM(ls.wholeday_leave) AS wholeday_leave,\r\n" + 
					"SUM(ls.releaving_leave) AS releaving_leave, SUM(ls.compensatory_leave) AS compensatory_leave\r\n" + 
					"FROM employeemaster emp\r\n" + 
					"LEFT JOIN leavesummary ls ON ls.emp_cd = emp.emp_cd\r\n" + 
					"WHERE DATE_FORMAT(ls.summary_month,'%Y-%m') = DATE_FORMAT('"+ date +"','%Y-%m')\r\n" + 
					"GROUP BY emp_cd\r\n" + 
					")tbl2\r\n" + 
					"ON tbl1.emp_cd = tbl2.emp_cd;";

			String query1 = "SELECT tbl1.emp_cd, tbl1.emp_name, tbl1.lastyear_carry_leave, tbl1.cur_year_allowed_leave, tbl1.total_allowed_leave,\r\n" + 
					"tbl1.remaining_leave, tbl2.halfday_leave, tbl2.wholeday_leave, tbl2.releaving_leave, tbl2.compensatory_leave \r\n" + 
					"FROM\r\n" + 
					"(\r\n" + 
					" SELECT emp.emp_cd AS emp_cd, emp.emp_name AS emp_name, le.lastyear_carry_leave AS lastyear_carry_leave, \r\n" + 
					" le.cur_year_allowed_leave AS cur_year_allowed_leave, le.total_allowed_leave AS total_allowed_leave,\r\n" + 
					" (le.total_allowed_leave-(SUM(IF(att.day=1,1,0))+SUM(IF(att.day=2 or att.day=3,0.5,0)))) AS remaining_leave\r\n" + 
					"FROM employeemaster emp \r\n" + 
					"LEFT JOIN leaves le ON le.emp_cd = emp.emp_cd\r\n" + 
					"AND IF(DATE_FORMAT('"+ date +"','%m')>'04',\r\n" + 
					"YEAR(le.target_year) = YEAR('"+ date +"'),\r\n" + 
					"YEAR(le.target_year) = YEAR(CONCAT(YEAR('"+ date +"')-1,'-01-01')))\r\n" + 
					"LEFT JOIN attendance att ON att.emp_cd = emp.emp_cd\r\n" + 
					"AND IF(DATE_FORMAT('"+ date +"','%m')>'04',\r\n" + 
					"DATE_FORMAT(att.choose_date,'%Y-%m') >= DATE_FORMAT(CONCAT(YEAR('"+ date +"'),'-04-01'),'%Y-%m') AND\r\n" + 
					"DATE_FORMAT(att.choose_date,'%Y-%m') < DATE_FORMAT('"+ date +"','%Y-%m'),\r\n" + 
					"DATE_FORMAT(att.choose_date,'%Y-%m') >= DATE_FORMAT(CONCAT(YEAR('"+ date +"')-1,'-04-01'),'%Y-%m') AND\r\n" + 
					"DATE_FORMAT(att.choose_date,'%Y-%m') < DATE_FORMAT('"+ date +"','%Y-%m'))\r\n" + 
					"GROUP BY emp_cd\r\n" + 
					")tbl1\r\n" + 
					"LEFT JOIN\r\n" + 
					"(\r\n" + 
					"SELECT emp.emp_cd AS emp_cd, SUM(IF(att.day=2 or att.day=3,1,0)) AS halfday_leave, SUM(IF(att.day=1,1,0)) AS wholeday_leave,\r\n" + 
					"SUM(IF(att.day=4,1,0)) AS releaving_leave, SUM(att.compensatory_leave) AS compensatory_leave\r\n" + 
					"FROM employeemaster emp\r\n" + 
					"LEFT JOIN attendance att ON att.emp_cd = emp.emp_cd\r\n" + 
					"WHERE DATE_FORMAT(att.choose_date,'%Y-%m') = DATE_FORMAT('"+ date +"','%Y-%m')\r\n" + 
					"GROUP BY emp_cd\r\n" + 
					")tbl2\r\n" + 
					"ON tbl1.emp_cd = tbl2.emp_cd;";
			
			String queryCount = "SELECT COUNT(*) AS rowcount FROM leavesummary WHERE DATE_FORMAT(summary_month,'%Y-%m') = DATE_FORMAT('"+ date +"','%Y-%m')";
			Statement stmt = conn.createStatement();
			ResultSet r = stmt.executeQuery(queryCount);
			r.next();
			if(r.getInt("rowcount") > 0) {
				PreparedStatement ps = conn.prepareStatement(query);
				ResultSet rs = ps.executeQuery();
				while (rs.next()) {
					LeaveSummaryInfoDetail leaveList = new LeaveSummaryInfoDetail();
					String empExists = "SELECT COUNT(*) AS rowcount FROM leavesummary WHERE emp_cd = '"+ rs.getString("emp_cd") +"'";
					Statement stm = conn.createStatement();
					ResultSet re = stm.executeQuery(empExists);
					re.next();
					if(re.getInt("rowcount") > 0) {
						leaveList.setEmp_cd(rs.getString("emp_cd"));
						leaveList.setEmp_name(rs.getString("emp_name"));
						leaveList.setLastyear_carry_leave(rs.getFloat("lastyear_carry_leave"));
						leaveList.setCur_year_allowed_leave(rs.getFloat("cur_year_allowed_leave"));
						leaveList.setTotal_allowed_leave(rs.getFloat("total_allowed_leave"));
						leaveList.setRemaining_leave(rs.getFloat("remaining_leave"));
						leaveList.setHalfday_leave(rs.getInt("halfday_leave"));
						leaveList.setWholeday_leave(rs.getInt("wholeday_leave"));
						leaveList.setReleaving_leave(rs.getInt("releaving_leave"));
						leaveList.setCompensatory_leave(rs.getInt("compensatory_leave"));
					}else {
						//LeaveSummaryで存在しない中途メンバーのデータをattendanceテーブルから取得
						String query2 = "SELECT tbl1.emp_cd, tbl1.emp_name, tbl1.lastyear_carry_leave, tbl1.cur_year_allowed_leave, tbl1.total_allowed_leave,\r\n" + 
								"tbl1.remaining_leave, tbl2.halfday_leave, tbl2.wholeday_leave, tbl2.releaving_leave, tbl2.compensatory_leave \r\n" + 
								"FROM\r\n" + 
								"(\r\n" + 
								" SELECT emp.emp_cd AS emp_cd, emp.emp_name AS emp_name, le.lastyear_carry_leave AS lastyear_carry_leave, \r\n" + 
								" le.cur_year_allowed_leave AS cur_year_allowed_leave, le.total_allowed_leave AS total_allowed_leave,\r\n" + 
								" (le.total_allowed_leave-(SUM(IF(att.day=1,1,0))+SUM(IF(att.day=2 or att.day=3,0.5,0)))) AS remaining_leave\r\n" + 
								"FROM employeemaster emp \r\n" + 
								"LEFT JOIN leaves le ON le.emp_cd = emp.emp_cd\r\n" + 
								"AND IF(DATE_FORMAT('"+ date +"','%m')>'04',\r\n" + 
								"YEAR(le.target_year) = YEAR('"+ date +"'),\r\n" + 
								"YEAR(le.target_year) = YEAR(CONCAT(YEAR('"+ date +"')-1,'-01-01')))\r\n" + 
								"LEFT JOIN attendance att ON att.emp_cd = emp.emp_cd\r\n" + 
								"AND IF(DATE_FORMAT('"+ date +"','%m')>'04',\r\n" + 
								"DATE_FORMAT(att.choose_date,'%Y-%m') >= DATE_FORMAT(CONCAT(YEAR('"+ date +"'),'-04-01'),'%Y-%m') AND\r\n" + 
								"DATE_FORMAT(att.choose_date,'%Y-%m') < DATE_FORMAT('"+ date +"','%Y-%m'),\r\n" + 
								"DATE_FORMAT(att.choose_date,'%Y-%m') >= DATE_FORMAT(CONCAT(YEAR('"+ date +"')-1,'-04-01'),'%Y-%m') AND\r\n" + 
								"DATE_FORMAT(att.choose_date,'%Y-%m') < DATE_FORMAT('"+ date +"','%Y-%m'))\r\n" + 
								"GROUP BY emp_cd\r\n" + 
								")tbl1\r\n" + 
								"LEFT JOIN\r\n" + 
								"(\r\n" + 
								"SELECT emp.emp_cd AS emp_cd, SUM(IF(att.day=2 or att.day=3,1,0)) AS halfday_leave, SUM(IF(att.day=1,1,0)) AS wholeday_leave,\r\n" + 
								"SUM(IF(att.day=4,1,0)) AS releaving_leave, SUM(att.compensatory_leave) AS compensatory_leave\r\n" + 
								"FROM employeemaster emp\r\n" + 
								"LEFT JOIN attendance att ON att.emp_cd = emp.emp_cd\r\n" + 
								"WHERE DATE_FORMAT(att.choose_date,'%Y-%m') = DATE_FORMAT('"+ date +"','%Y-%m')\r\n" + 
								"GROUP BY emp_cd\r\n" + 
								")tbl2\r\n" + 
								"ON tbl1.emp_cd = tbl2.emp_cd\r\n" +
								"WHERE tbl1.emp_cd = '"+ rs.getString("emp_cd") +"'";
						
						PreparedStatement ps2 = conn.prepareStatement(query2);
						ResultSet rs2 = ps2.executeQuery();
						rs2.next();
						leaveList.setEmp_cd(rs2.getString("emp_cd"));
						leaveList.setEmp_name(rs2.getString("emp_name"));
						leaveList.setLastyear_carry_leave(rs2.getFloat("lastyear_carry_leave"));
						leaveList.setCur_year_allowed_leave(rs2.getFloat("cur_year_allowed_leave"));
						leaveList.setTotal_allowed_leave(rs2.getFloat("total_allowed_leave"));
						leaveList.setRemaining_leave(rs2.getFloat("remaining_leave"));
						leaveList.setHalfday_leave(rs2.getInt("halfday_leave"));
						leaveList.setWholeday_leave(rs2.getInt("wholeday_leave"));
						leaveList.setReleaving_leave(rs2.getInt("releaving_leave"));
						leaveList.setCompensatory_leave(rs2.getInt("compensatory_leave"));
					}
					leaveInfoList.add(leaveList);
				}
			}else {
				PreparedStatement ps1 = conn.prepareStatement(query1);
				ResultSet rs1 = ps1.executeQuery();
				while (rs1.next()) {
					LeaveSummaryInfoDetail leaveList = new LeaveSummaryInfoDetail();
					leaveList.setEmp_cd(rs1.getString("emp_cd"));
					leaveList.setEmp_name(rs1.getString("emp_name"));
					leaveList.setLastyear_carry_leave(rs1.getFloat("lastyear_carry_leave"));
					leaveList.setCur_year_allowed_leave(rs1.getFloat("cur_year_allowed_leave"));
					leaveList.setTotal_allowed_leave(rs1.getFloat("total_allowed_leave"));
					leaveList.setRemaining_leave(rs1.getFloat("remaining_leave"));
					leaveList.setHalfday_leave(rs1.getInt("halfday_leave"));
					leaveList.setWholeday_leave(rs1.getInt("wholeday_leave"));
					leaveList.setReleaving_leave(rs1.getInt("releaving_leave"));
					leaveList.setCompensatory_leave(rs1.getInt("compensatory_leave"));
					leaveInfoList.add(leaveList);
			}
		  }
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return leaveInfoList;
	}

	/**
	 * 休暇サマリ情報Excelファイルデータを読み取る
	 * 
	 * @return
	 */
	@SuppressWarnings("resource")
	public String leaveImport() {
		LeaveFileValidation fileValidation = new LeaveFileValidation();
		if (null != this.file) {
			if (fileValidation.validFileExtension(this.fileContentType)) {
				addActionError("Ｅｘｃｅｌファイル以外は無効です。");
				return INPUT;
			}
			try {
				FileInputStream streamDate = new FileInputStream(this.file);
				Workbook workbook = new XSSFWorkbook(streamDate);
				XSSFSheet sheet = (XSSFSheet) workbook.getSheetAt(0);
				XSSFRow row = sheet.getRow(0);
				XSSFCell cell = row.getCell(5);
				String strDate = cell.getStringCellValue();
				String[] parts = strDate.split("/");
				String year = parts[0];
				String month = parts[1];
				String dateString = year + "-" + month + "-" + "01";
				sessionmap.put("dateString", dateString);
				if(Integer.parseInt(month) >= 4) {
					String targetDateString = year + "-01-01";
					sessionmap.put("targetDate", targetDateString);
				}else {
					String targetDateString = Integer.parseInt(year)-1 + "-01-01";
					sessionmap.put("targetDate", targetDateString);
				}
				streamDate.close();
				
				FileInputStream stream = new FileInputStream(this.file);
				FileHelper fileHelper = new FileHelper();
				this.reportTimes.addAll(fileHelper.readLeaveFile(stream, 0));

				if (fileValidation.invalidFileFormat(this.reportTimes.get(0).getExcelData())) {
					addActionError("アップロードしたファイルは休暇ファイルではありません。");
					this.reportTimes = null;
					return INPUT;
				} else if (this.reportTimes.size() < 2) {
					addActionError("アップロードしたファイルは休暇データがありません。");
					this.reportTimes = null;
				} else {
					this.reportTimes.remove(0);
					sessionmap.put("reportTimes", this.reportTimes);
					return SUCCESS;
				}
			} catch (Exception e) {
				addActionError("アップロードしたファイルは休暇ファイルではありません。");
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
	 * 休暇サマリ情報をデータベースに登録する
	 * 
	 * @return
	 */
	@SuppressWarnings({ "unchecked" })
	public String insertLeaveImportList() {

		try {
			//権限チェック
			if (sessionmap.get("ID") == null) {
				return "session";
			} else if (!CommonCheck.isValidRole(CommonConstant.SCREEN_NO.LEAVE_SUMMARY_INFO.value,
					sessionmap.get("ID").toString())) {
				return "role";
			}
			this.reportTimes = (ArrayList<ReportTime>) sessionmap.get("reportTimes");
			Connection conn = null;
			conn = DataConnection.getConnection();
			conn.setAutoCommit(false);

			ArrayList<String> messages = new ArrayList<>();
			LeaveSummaryInfoDetail leaveInfoList = new LeaveSummaryInfoDetail();
			
			//対象年がある場合、データ削除
			String query1 = "DELETE FROM leaves WHERE YEAR(target_year) =  YEAR('" 
			        + sessionmap.get("targetDate") + "')";

			//対象年月がある場合、データ削除
			String query2 = "DELETE FROM leavesummary WHERE DATE_FORMAT(summary_month,'%Y-%m') = DATE_FORMAT('"
					+ sessionmap.get("dateString") + "','%Y-%m')";

			if (sessionmap.get("dateString") != null) {
				Statement stmt = conn.createStatement();
				stmt.executeUpdate(query1);
				stmt.executeUpdate(query2);
				stmt.close();
				conn.commit();
			}

			String leavesSql = "INSERT INTO leaves (emp_cd, target_year, lastyear_carry_leave, cur_year_allowed_leave, "
					+ "total_allowed_leave, remaining_leave, created_date, modified_date) VALUES(?,?,?,?,?,?,?,?)";

			String leaveSummarySql = "INSERT INTO leavesummary (emp_cd, summary_month, wholeday_leave, halfday_leave, "
					+ "releaving_leave, compensatory_leave, created_date, modified_date) VALUES(?,?,?,?,?,?,?,?)";

			PreparedStatement leaveStmt = conn.prepareStatement(leavesSql);
			PreparedStatement leaveSummaryStmt = conn.prepareStatement(leaveSummarySql);

			Date date = new Date();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			String timeStamp = formatter.format(date);

			for (int i = 0; i < this.reportTimes.size(); i++) {
				leaveInfoList = concactToALFD(this.reportTimes.get(i).getExcelData());
				
				String queryCount = "SELECT COUNT(*) AS rowcount FROM employeemaster WHERE emp_cd = '"+ leaveInfoList.getEmp_cd() + "'";
				Statement stmt = conn.createStatement();
				ResultSet r = stmt.executeQuery(queryCount);
				r.next();
				if (r.getInt("rowcount") > 0) {
					leaveStmt.setString(1, leaveInfoList.getEmp_cd());
					leaveStmt.setString(2, (String) sessionmap.get("targetDate"));
					leaveStmt.setFloat(3, leaveInfoList.getLastyear_carry_leave());
					leaveStmt.setFloat(4, leaveInfoList.getCur_year_allowed_leave());
					leaveStmt.setFloat(5, leaveInfoList.getTotal_allowed_leave());
					leaveStmt.setFloat(6, leaveInfoList.getRemaining_leave());
					leaveStmt.setString(7, timeStamp);
					leaveStmt.setString(8, timeStamp);

					leaveSummaryStmt.setString(1, leaveInfoList.getEmp_cd());
					leaveSummaryStmt.setString(2, (String) sessionmap.get("dateString"));
					leaveSummaryStmt.setInt(3, leaveInfoList.getWholeday_leave());
					leaveSummaryStmt.setInt(4, leaveInfoList.getHalfday_leave());
					leaveSummaryStmt.setInt(5, leaveInfoList.getReleaving_leave());
					leaveSummaryStmt.setInt(6, leaveInfoList.getCompensatory_leave());
					leaveSummaryStmt.setString(7, timeStamp);
					leaveSummaryStmt.setString(8, timeStamp);

					leaveStmt.execute();
					leaveSummaryStmt.execute();
					conn.commit();
				} else {
					messages.add(leaveInfoList.getEmp_cd());
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

	/**
	 * リストからデータを読み取り、モデルに設定する
	 * 
	 * @param list
	 * @return leaveInfoList
	 */
	private LeaveSummaryInfoDetail concactToALFD(ArrayList<String> list) {
		LeaveSummaryInfoDetail leaveInfoList = new LeaveSummaryInfoDetail();

		leaveInfoList.setEmp_cd(list.get(0));
		if(list.get(2) != "") {
			leaveInfoList.setLastyear_carry_leave(Float.parseFloat(list.get(2)));
		}
		if(list.get(3) != "") {
			leaveInfoList.setCur_year_allowed_leave(Float.parseFloat(list.get(3)));
		}
		if(list.get(4) != "") {
			leaveInfoList.setTotal_allowed_leave(Float.parseFloat(list.get(4)));
		}
		if(list.get(5) != "") {
			leaveInfoList.setRemaining_leave(Float.parseFloat(list.get(5)));
		}
		if(list.get(6) != "") {
			leaveInfoList.setHalfday_leave(Integer.parseInt(list.get(6)));
		}
		if(list.get(7) != "") {
			leaveInfoList.setWholeday_leave(Integer.parseInt(list.get(7)));
		}
		if(list.get(8) != "") {
			leaveInfoList.setReleaving_leave(Integer.parseInt(list.get(8)));
		}
		if(list.get(9) != "") {
			leaveInfoList.setCompensatory_leave(Integer.parseInt(list.get(9)));
		}
		return leaveInfoList;
	}

	@Override
	public void setSession(Map<String, Object> arg0) {
		this.sessionmap = (SessionMap<String, Object>) arg0;
	}

	public Month[] getMonths() {
		Month[] months = Month.values();
		return months;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public String getExportFile() {
		return exportFile;
	}

	public void setExportFile(String exportFile) {
		this.exportFile = exportFile;
	}

	public Month getMonth() {
		return month;
	}

	public void setMonth(Month month) {
		this.month = month;
	}

	public List<LeaveSummaryInfoDetail> getLeaveInfoList() {
		return leaveInfoList;
	}

	public void setLeaveInfoList(List<LeaveSummaryInfoDetail> leaveInfoList) {
		this.leaveInfoList = leaveInfoList;
	}

	public String getFileContentType() {
		return fileContentType;
	}

	public void setFileContentType(String fileContentType) {
		this.fileContentType = fileContentType;
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
}