package com.action;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.struts2.dispatcher.SessionMap;
import org.apache.struts2.interceptor.SessionAware;
import com.common.CommonCheck;
import com.common.CommonConstant;
import com.jdbc.DataConnection;
import com.model.GroupListFormDetail;
import com.model.ReportContentForm;
import com.model.ReportContentListForm;
import com.opensymphony.xwork2.ActionSupport;

public class ReportListAction extends ActionSupport implements SessionAware {

	private static final long serialVersionUID = 1L;
	ReportContentListForm reportContentListFrm = new ReportContentListForm();
	List<ReportContentForm> reportConetentDetail = new ArrayList<ReportContentForm>();
	SessionMap<String, Object> sessionmap;
	private String reportFile;
	private InputStream inputStream;
	
	/**
	 * 初期表示
	 * 
	 * @return
	 */
	public String execute() {
		// 権限チェック
		if (sessionmap.get("ID") == null) {
			return "session";
		} else if (!CommonCheck.isValidRole(CommonConstant.SCREEN_NO.REPORT_ENTRY.value,
				sessionmap.get("ID").toString())) {
			return "role";
		}

		// 権限チェック
		checkPermission();

		// グループリスト取得
		getGroupList();

		// セッションクリア
		sessionmap.remove("reportlist_emp_cd");
		sessionmap.remove("reportlist_emp_name");
		sessionmap.remove("reportlist_start_date");
		sessionmap.remove("reportlist_end_date");
		sessionmap.remove("reportlist_group_cd");
		sessionmap.remove("reportlist_gl_flag");

		return "success";
	}

	/**
	 * 検索処理
	 * 
	 * @return
	 */
	public String search() {
		// 権限チェック
		if (sessionmap.get("ID") == null) {
			return "session";
		} else if (!CommonCheck.isValidRole(CommonConstant.SCREEN_NO.REPORT_ENTRY.value,
				sessionmap.get("ID").toString())) {
			return "role";
		}
		String str = (String) sessionmap.get("searchReportList");
		if (str.equals("RETURN")) {
			sessionmap.put("searchReportList", "SEARCH");
		} else {
			sessionmap.put("pageReportList", "0");
		}
		String emp_cd = "", emp_name = "", start_date = "", end_date = "", group_cd = "", gl_flag = "";

		if ("search".equals(reportContentListFrm.getButton_event())) {
			// 社員番号
			emp_cd = reportContentListFrm.getEmp_cd();
			// 社員名
			emp_name = reportContentListFrm.getEmp_name();
			// 週報提出日
			start_date = reportContentListFrm.getStart_date();
			end_date = reportContentListFrm.getEnd_date();
			// グループ名
			if (StringUtils.isEmpty(reportContentListFrm.getGroup_cd())) {
				group_cd = (String) sessionmap.get("GROUP_CD");
			} else {
				group_cd = reportContentListFrm.getGroup_cd();
			}
			// GLフラグ
			gl_flag = reportContentListFrm.getGl_flag();
		} else {
			if (!sessionmap.containsKey("reportlist_emp_cd")) {
				// URL直接記入禁止
				return "role";
			}

			// 社員番号
			emp_cd = (String) sessionmap.get("reportlist_emp_cd");
			// 社員名
			emp_name = (String) sessionmap.get("reportlist_emp_name");
			// 週報提出日
			start_date = (String) sessionmap.get("reportlist_start_date");
			end_date = (String) sessionmap.get("reportlist_end_date");
			// グループコード
			group_cd = (String) sessionmap.get("reportlist_group_cd");
			// GLフラグ
			gl_flag = (String) sessionmap.get("reportlist_gl_flag");
		}

		String sql = "SELECT reportcontent.report_cd, reportcontent.emp_cd, reportcontent.emp_comment, "
				+ "reportcontent.gl_comment, reportcontent.pe_comment, reportcontent.manager_comment, "
				+ "reportcontent.updated_date, employeemaster.emp_name, reportcontent.customer_name, "
				+ "reportcontent.started_date, reportcontent.created_date " + "FROM reportcontent, employeemaster "
				+ "LEFT JOIN groupmaster ON groupmaster.group_cd = employeemaster.group_cd "
				+ "WHERE reportcontent.emp_cd = employeemaster.emp_cd ";

		if (StringUtils.isNotEmpty(emp_name)) {
			// 社員名
			sql = sql + " AND employeemaster.emp_name LIKE '%" + emp_name + "%'";
		}

		if (StringUtils.isNotEmpty(emp_cd)) {
			// 社員番号
			sql = sql + " AND reportcontent.emp_cd LIKE '" + emp_cd + "%'";
		}

		if (StringUtils.isNotEmpty(start_date)) {
			// 開始日
			sql = sql + " AND reportcontent.started_date >= '" + start_date + "'";
		}

		if (StringUtils.isNotEmpty(end_date)) {
			// 終了日
			sql = sql + " AND reportcontent.started_date <= '" + end_date + "'";
		}

		if (StringUtils.isNotEmpty(group_cd) && !"-1".equals(group_cd)) {
			// グループコード
			sql = sql + " AND groupmaster.group_cd = '" + group_cd + "'";
		}

		if ("true".equals(gl_flag)) {
			// GLフラグ
			sql = sql + " AND employeemaster.gl_flag = '1'";
		}
		sql = sql + " ORDER BY reportcontent.started_date ASC";
		Connection conn;
		try {
			conn = DataConnection.getConnection();
			PreparedStatement ps1 = conn.prepareStatement(sql);
			ResultSet rs = ps1.executeQuery();
			List<ReportContentForm> detailList = new ArrayList<ReportContentForm>();
			
			while (rs.next()) {
				ReportContentForm detail = new ReportContentForm();

				detail.setCustomer_name(rs.getString("customer_name"));
				detail.setEmp_cd(rs.getString("emp_cd"));
				detail.setEmp_name(rs.getString("emp_name"));
				detail.setEmp_comment(rs.getString("emp_comment"));
				detail.setGl_comment(rs.getString("gl_comment"));
				detail.setManager_comment(rs.getString("manager_comment"));
				detail.setPe_comment(rs.getString("pe_comment"));
				detail.setReport_cd(rs.getString("report_cd"));
				detail.setStarted_date(rs.getString("started_date"));
				detail.setCreated_date(rs.getString("created_date"));
				detail.setUpdated_date(rs.getString("updated_date"));

				detailList.add(detail);
			}

			if (detailList.size() > 0) {
				// 検索結果がある場合
				reportContentListFrm.setSearch("1");
				reportContentListFrm.setReportConetentDetailList(detailList);
			} else {
				// 検索データなしの場合
				reportContentListFrm.setSearch("");
				addActionMessage("検索データがありません。");
			}

			// グループリスト取得
			getGroupList();

			reportContentListFrm.setEmp_cd(emp_cd);
			reportContentListFrm.setEmp_name(emp_name);
			reportContentListFrm.setStart_date(start_date);
			reportContentListFrm.setEnd_date(end_date);
			reportContentListFrm.setGroup_cd(group_cd);
			reportContentListFrm.setGl_flag(gl_flag);

			// 権限チェック
			checkPermission();

			// �Z�b�V�����Ɍ�������ݒ肷��
			sessionmap.put("reportlist_emp_cd", emp_cd);
			sessionmap.put("reportlist_emp_name", emp_name);
			sessionmap.put("reportlist_start_date", start_date);
			sessionmap.put("reportlist_end_date", end_date);
			sessionmap.put("reportlist_group_cd", group_cd);
			sessionmap.put("reportlist_gl_flag", gl_flag);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "success";
	}

	/**
	 * グループ名リスト取得
	 */
	private void getGroupList() {

		ArrayList<GroupListFormDetail> groupList = new ArrayList<GroupListFormDetail>();
		try {
			Connection conn = DataConnection.getConnection();
			String sql = "select group_cd, group_name from groupmaster";
			PreparedStatement ps1 = conn.prepareStatement(sql);
			ResultSet rs = ps1.executeQuery();
			while (rs.next()) {
				GroupListFormDetail detail = new GroupListFormDetail();
				detail.setGroup_cd(rs.getString("group_cd"));
				detail.setGroup_name(rs.getString("group_name"));
				groupList.add(detail);
			}
			rs.close();
			ps1.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		reportContentListFrm.setGroupList(groupList);
	}

	/**
	 * 削除処理
	 * 
	 * @return
	 */
	public String delete() {
		// 権限チェック
		if (sessionmap.get("ID") == null) {
			return "session";
		} else if (!CommonCheck.isValidRole(CommonConstant.SCREEN_NO.REPORT_ENTRY.value,
				sessionmap.get("ID").toString())) {
			return "role";
		}

		int id = reportContentListFrm.getDelete_report_cd();

		Connection conn;
		try {
			String sql = "DELETE FROM reportcontent WHERE report_cd = ?";
			conn = DataConnection.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);

			ps.setInt(1, id);
			ps.executeUpdate();
			ps.close();

			sql = "DELETE FROM reportcontentdetail WHERE report_cd = ?";
			ps = conn.prepareStatement(sql);
			ps.setInt(1, id);
			ps.executeUpdate();
			ps.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// データベースから再取得
		search();

		return "success";
	}

	/**
	 * 権限チェック
	 */
	private void checkPermission() {
		// セッションデータ取得
		String emp_cd = (String) sessionmap.get("ID");
		String emp_name = (String) sessionmap.get("NAME");
		String role = (String) sessionmap.get("ROLE");
		String group_cd = (String) sessionmap.get("GROUP_CD");

		// propertiesファイル取得
		ResourceBundle rb = ResourceBundle.getBundle("messages_ja");

		reportContentListFrm.setDisable_empcd("");
		reportContentListFrm.setDisable_empname("");
		reportContentListFrm.setDisable_groupcd("");

		// フォームに設定する
		if (rb.getString("role.gl").equals(role)) {
			reportContentListFrm.setGroup_cd(group_cd);
			reportContentListFrm.setDisable_groupcd("disabled");
		} else if (rb.getString("role.member").equals(role)) {
			reportContentListFrm.setEmp_cd(emp_cd);
			reportContentListFrm.setEmp_name(emp_name);
			reportContentListFrm.setGroup_cd(group_cd);

			reportContentListFrm.setDisable_empcd("disabled");
			reportContentListFrm.setDisable_empname("disabled");
			reportContentListFrm.setDisable_groupcd("disabled");
		}
	}

	/**
	 * Excel出し
	 * 
	 * @return
	 */
	public String excel() {
		if (sessionmap.get("ID") == null) {
			return "session";
		} else if (!CommonCheck.isValidRole(CommonConstant.SCREEN_NO.REPORT_ENTRY.value,
				sessionmap.get("ID").toString())) {
			return "role";
		}
		String str = (String) sessionmap.get("searchReportList");
		if (str.equals("RETURN")) {
			sessionmap.put("searchReportList", "SEARCH");
		} else {
			sessionmap.put("pageReportList", "0");
		}
		String emp_cd = "", emp_name = "", start_date = "", end_date = "", group_cd = "", gl_flag = "";

		emp_cd = (String) sessionmap.get("reportlist_emp_cd");
		
		emp_name = (String) sessionmap.get("reportlist_emp_name");

		start_date = (String) sessionmap.get("reportlist_start_date");
		end_date = (String) sessionmap.get("reportlist_end_date");

		group_cd = (String) sessionmap.get("reportlist_group_cd");

		gl_flag = (String) sessionmap.get("reportlist_gl_flag");

		String sql = "SELECT reportcontent.report_cd, reportcontent.emp_cd, reportcontent.emp_comment, "
				+ "reportcontent.gl_comment, reportcontent.pe_comment, reportcontent.manager_comment, "
				+ "reportcontent.updated_date, employeemaster.emp_name, reportcontent.customer_name, "
				+ "reportcontent.started_date, reportcontent.created_date, groupmaster.group_name " + "FROM reportcontent, employeemaster "
				+ "LEFT JOIN groupmaster ON groupmaster.group_cd = employeemaster.group_cd "
				+ "WHERE reportcontent.emp_cd = employeemaster.emp_cd ";

		if (StringUtils.isNotEmpty(emp_name)) {
			sql = sql + " AND employeemaster.emp_name LIKE '%" + emp_name + "%'";
		}

		if (StringUtils.isNotEmpty(emp_cd)) {
			sql = sql + " AND reportcontent.emp_cd LIKE '" + emp_cd + "%'";
		}

		if (StringUtils.isNotEmpty(start_date)) {
			sql = sql + " AND reportcontent.started_date >= '" + start_date + "'";
		}

		if (StringUtils.isNotEmpty(end_date)) {
			sql = sql + " AND reportcontent.started_date <= '" + end_date + "'";
		}

		if (StringUtils.isNotEmpty(group_cd) && !"-1".equals(group_cd)) {
			sql = sql + " AND groupmaster.group_cd = '" + group_cd + "'";
		}

		if ("true".equals(gl_flag)) {
			sql = sql + " AND employeemaster.gl_flag = '1'";
		}
		sql = sql + " ORDER BY reportcontent.emp_cd, reportcontent.started_date ASC";
		Connection conn;
		List<ReportContentForm> detailList = new ArrayList<ReportContentForm>();
		try {
			conn = DataConnection.getConnection();
			PreparedStatement ps1 = conn.prepareStatement(sql);
			ResultSet rs = ps1.executeQuery();

			while (rs.next()) {
				ReportContentForm detail = new ReportContentForm();
				detail.setCustomer_name(rs.getString("customer_name"));
				detail.setEmp_cd(rs.getString("emp_cd"));
				detail.setEmp_name(rs.getString("emp_name"));
				detail.setEmp_comment(rs.getString("emp_comment"));
				detail.setGl_comment(rs.getString("gl_comment"));
				detail.setManager_comment(rs.getString("manager_comment"));
				detail.setPe_comment(rs.getString("pe_comment"));
				detail.setReport_cd(rs.getString("report_cd"));
				detail.setStarted_date(rs.getString("started_date"));
				detail.setCreated_date(rs.getString("created_date"));
				detail.setUpdated_date(rs.getString("updated_date"));
				detail.setGroup_name(rs.getString("group_name"));
				detailList.add(detail);
			}
				String templateFile = "ReportExport_Format.xlsx";
				
				ClassLoader classLoader = this.getClass().getClassLoader();
				InputStream inputStream = classLoader.getResourceAsStream(templateFile);
		
				if(detailList.size() == 0) {
					addActionError("検索データがありません。");
					execute();
					return ERROR;
				}
		
				XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
				XSSFSheet sheet = workbook.getSheetAt(0);
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
				
				String reportFile1 = "ReportList_"+formatter.format(Calendar.getInstance().getTime())+".xlsx";
				reportFile = new String(reportFile1.getBytes(), "ISO8859_1");
				writeBook(workbook, detailList, sheet);
				ByteArrayOutputStream boas = new ByteArrayOutputStream();
				workbook.write(boas);
				setInputStream(new ByteArrayInputStream(boas.toByteArray()));
				return "success";
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "success";
	}
	
	/**
	 * Excel
	 * @param reportList
	 * @param sheet
	 * 作成(5/2020)ssl
	 */
	private void writeBook(XSSFWorkbook workbook, List<ReportContentForm> reportList, XSSFSheet sheet) {
		CellStyle style = workbook.createCellStyle();
		style.setBorderBottom(CellStyle.BORDER_THIN);
		style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
		style.setBorderRight(CellStyle.BORDER_THIN);
		style.setRightBorderColor(IndexedColors.BLACK.getIndex());
		style.setBorderLeft(CellStyle.BORDER_THIN);
		style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
		style.setBorderTop(CellStyle.BORDER_THIN);
		style.setTopBorderColor(IndexedColors.BLACK.getIndex());
		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		style.setWrapText(true);
		//get today's date
	    Date today = Calendar.getInstance().getTime();
	    //create a date "formatter"
	    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		XSSFRow row = sheet.createRow(0);
		XSSFCell cell = row.createCell(0);
		cell.setCellValue("Export Date : "+formatter.format(today));

		int i=3;
		for (ReportContentForm detail: reportList ) {
			row = sheet.createRow(i);
			XSSFCell cell0 = row.createCell(0);
			if(StringUtils.isNotBlank(detail.getGroup_name())){
				cell0.setCellValue(detail.getGroup_name());
			}
			cell0.setCellStyle(style);
			XSSFCell cell1 = row.createCell(1);
			if(StringUtils.isNotBlank(detail.getEmp_cd()) && StringUtils.isNotBlank(detail.getEmp_name())){
				cell1.setCellValue(detail.getEmp_cd()+" \n"+detail.getEmp_name());	
			}
			cell1.setCellStyle(style);
			XSSFCell cell2 = row.createCell(2);
			if(StringUtils.isNotBlank(detail.getStarted_date())){
				cell2.setCellValue(detail.getStarted_date());
			}
			cell2.setCellStyle(style);
			XSSFCell cell3 = row.createCell(3);
			if(StringUtils.isNotBlank(detail.getCreated_date()) && StringUtils.isNotBlank(detail.getUpdated_date())){	
				cell3.setCellValue(detail.getCreated_date()+" \n"+detail.getUpdated_date());	
			}
			cell3.setCellStyle(style);
			XSSFCell cell4 = row.createCell(4);
			if(StringUtils.isNotBlank(detail.getEmp_comment())){	
				cell4.setCellValue(detail.getEmp_comment());
			}
			cell4.setCellStyle(style);
			XSSFCell cell5 = row.createCell(5);
			if(StringUtils.isNotBlank(detail.getGl_comment())){
				cell5.setCellValue(detail.getGl_comment());	
			}
			cell5.setCellStyle(style);
			XSSFCell cell6 = row.createCell(6);
			if(StringUtils.isNotBlank(detail.getPe_comment())){	
				cell6.setCellValue(detail.getPe_comment());	
			}
			cell6.setCellStyle(style);
			XSSFCell cell7 = row.createCell(7);
			if(StringUtils.isNotBlank(detail.getManager_comment())){		
				cell7.setCellValue(detail.getManager_comment());	
			}		
			cell7.setCellStyle(style);
			i++;
		}
		FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
		evaluator.evaluateAll();
	}
	
	@Override
	public void setSession(Map<String, Object> session) {
		this.sessionmap = (SessionMap) session;
	}

	public ReportContentListForm getReportContentListFrm() {
		return reportContentListFrm;
	}

	public void setReportContentListFrm(ReportContentListForm reportContentListFrm) {
		this.reportContentListFrm = reportContentListFrm;
	}

	public List<ReportContentForm> getReportConetentDetail() {
		return reportConetentDetail;
	}

	public void setReportConetentDetail(List<ReportContentForm> reportConetentDetail) {
		this.reportConetentDetail = reportConetentDetail;
	}
	
	public String getReportFile() {
		return reportFile;
	}
	public void setReportFile(String reportFile) {
		this.reportFile = reportFile;
	}
	public InputStream getInputStream() {
		return inputStream;
	}
	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}
	
}