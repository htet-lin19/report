package com.action;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts2.dispatcher.SessionMap;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.SessionAware;
import com.common.CommonCheck;
import com.common.CommonConstant;
import com.jdbc.DataConnection;
import com.model.ReportContentForm;
import com.model.ReportContentFormDetail;
import com.opensymphony.xwork2.ActionSupport;

public class ReportEntryAction extends ActionSupport implements ServletRequestAware, SessionAware{
	
	private static final long serialVersionUID = 1L;
	HttpServletRequest request;
	ReportContentForm reportContentFrm = new ReportContentForm();
	SessionMap<String, Object> sessionmap;
	private String hiddenId;
	private String pageNo;
	int rowIndex;

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

	/**
	 * メニューから呼び出す処理
	 * @return
	 */
	public String execute(){
		// 権限チェック
		if (sessionmap.get("ID") == null) {
			return "session";
		} else if(!CommonCheck.isValidRole(CommonConstant.SCREEN_NO.REPORT_ENTRY.value, 
				sessionmap.get("ID").toString())) {
			return "role";
		}
		
		reportContentFrm.setEmp_cd((String)sessionmap.get("ID"));
		reportContentFrm.setSearch("");
		getCustomerList();
		request.setAttribute("reportContentFrm", reportContentFrm);
		return SUCCESS;
	}
	
	/**
	 * お客様リスト取得処理
	 */
	private void getCustomerList(){
		ArrayList<String> customerList = new ArrayList<String>();
		try {
			Connection conn = DataConnection.getConnection();
			String sql = "select * from customermaster";
			PreparedStatement ps1 = conn.prepareStatement(sql);
			ResultSet rs = ps1.executeQuery();
			while(rs.next()){
				customerList.add(rs.getString("customer_name"));
			}
			rs.close();
			ps1.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		request.setAttribute("customerList", customerList);
	}
	
	/**
	 * 新行追加
	 * @return
	 */
	public String add() {
		// 権限チェック
		if (sessionmap.get("ID") == null) {
			return "session";
		} else if(!CommonCheck.isValidRole(CommonConstant.SCREEN_NO.REPORT_ENTRY.value, 
				sessionmap.get("ID").toString())) {
			return "role";
		}
		
		reportContentFrm = (ReportContentForm)request.getAttribute("reportContentFrm");
		List<ReportContentFormDetail> detailListOldList = reportContentFrm.getReportContentDetail();
		List<ReportContentFormDetail> detailListNewList = new ArrayList<ReportContentFormDetail>();
		int rowNo = (int)request.getAttribute("rowIndex");
		int count = 0;
		for (ReportContentFormDetail detailOld:detailListOldList) {
			ReportContentFormDetail detailNew = new ReportContentFormDetail();
			detailNew = detailOld;
			
			detailListNewList.add(detailNew);
			
			if(rowNo == count) {
				detailNew = new ReportContentFormDetail();
				detailNew.setMode("0");
				//detailNew.setWork_date_hidden(detailOld.getWork_date());
				detailNew.setWork_date_hidden(detailOld.getWork_date()==null? detailOld.getWork_date_hidden(): detailOld.getWork_date());
				detailListNewList.add(detailNew);
			}
			
			count++;
		}
		
		reportContentFrm.setSearch(reportContentFrm.getSearch());
		reportContentFrm.setReportContentDetail(detailListNewList);
		reportContentFrm.setStarted_date(reportContentFrm.getStarted_date_hidden());
		request.setAttribute("reportContentFrm", reportContentFrm);
		getCustomerList();
		checkPermission();
		return SUCCESS;
	}

	/**
	 * 行削除
	 * @return
	 */
	public String remove() {
		// 権限チェック
		if (sessionmap.get("ID") == null) {
			return "session";
		} else if(!CommonCheck.isValidRole(CommonConstant.SCREEN_NO.REPORT_ENTRY.value, 
				sessionmap.get("ID").toString())) {
			return "role";
		}
		
		reportContentFrm = (ReportContentForm)request.getAttribute("reportContentFrm");
		List<ReportContentFormDetail> detailListOldList = reportContentFrm.getReportContentDetail();
		List<ReportContentFormDetail> detailListNewList = new ArrayList<ReportContentFormDetail>();
		int rowNo = (int)request.getAttribute("rowIndex");
		int count = 0;
		for (ReportContentFormDetail detailOld:detailListOldList) {
			
			if(rowNo != count) {
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
		
		getCustomerList();
		checkPermission();
		return SUCCESS;
	}

	/**
	 * 検索処理
	 * @return
	 */
	public String search() {
		try {
			// 権限チェック
			if (sessionmap.get("ID") == null) {
				return "session";
			} else if(!CommonCheck.isValidRole(CommonConstant.SCREEN_NO.REPORT_ENTRY.value, 
					sessionmap.get("ID").toString())) {
				return "role";
			}
			
			// 社員番号
			String emp_cd = reportContentFrm.getEmp_cd();
			//　週報提出日
			String started_date = reportContentFrm.getStarted_date();
			int rowCount = 0;
			
			// お客様データ取得
			getCustomerList();
			
			// 権限チェック
			checkPermission();
			
			Connection conn = DataConnection.getConnection();
			String sql = "SELECT * FROM reportcontent report, reportcontentdetail detail WHERE report.emp_cd ='" +emp_cd+ "' AND report.started_date ='"+started_date+"' AND report.report_cd = detail.report_cd ORDER BY detail.work_date,reportdetail_cd";
			PreparedStatement ps1 = conn.prepareStatement(sql);
			ResultSet rs = ps1.executeQuery();			
			
			List<ReportContentFormDetail> detailList = new ArrayList<ReportContentFormDetail>(); 
			String wdate = "";
			while (rs.next()) {
				
				if (rowCount==0) {
					// 社員番号と日付で検索したデータを設定する
					reportContentFrm.setEmp_cd(rs.getString("emp_cd"));
					reportContentFrm.setEmp_comment(rs.getString("emp_comment"));
					reportContentFrm.setGl_comment(rs.getString("gl_comment"));
					reportContentFrm.setManager_comment(rs.getString("manager_comment"));
					reportContentFrm.setPe_comment(rs.getString("pe_comment"));
					reportContentFrm.setStarted_date(rs.getString("started_date"));
					reportContentFrm.setCustomer_name(rs.getString("customer_name"));
				}
				
				ReportContentFormDetail detail = new ReportContentFormDetail();
				detail.setContents(rs.getString("content"));
				detail.setPlan_progress(rs.getString("plan_progress"));
				detail.setPlan_time(rs.getString("plan_time"));
				detail.setResult(rs.getString("result"));
				if (!wdate.equals(rs.getString("work_date"))) {
					detail.setWork_date(rs.getString("work_date"));
					detail.setMode("1");//EditMode
					wdate = rs.getString("work_date");
				}
				else{
					detail.setMode("0");
				}
					
				detail.setWork_date_hidden(rs.getString("work_date"));
				detail.setActual_time(rs.getString("actual_time"));
				detail.setActual_progress(rs.getString("actual_progress"));
				
				detailList.add(detail);
				
				rowCount++;
			}
			
			if (rowCount == 0) {
				// 検索データがない場合、空白行を表示		
				reportContentFrm.setEmp_comment("");
				reportContentFrm.setGl_comment("");
				reportContentFrm.setManager_comment("");
				reportContentFrm.setPe_comment("");
				for(int i=0; i<7; i++) {
					ReportContentFormDetail detail = new ReportContentFormDetail();
					detail.setWork_date(addDate(started_date, i));
					detail.setMode("1");//EditMode
					detailList.add(detail);
				}
			}
			
			reportContentFrm.setSearch("1");
			reportContentFrm.setReportContentDetail(detailList);
			request.setAttribute("reportContentFrm", reportContentFrm);
			
		} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}
		
		return SUCCESS;
	}
	
	/**
	 * 登録・更新処理
	 * @return
	 */
	public String insert() {
		// 権限チェック
		if (sessionmap.get("ID") == null) {
			return "session";
		} else if(!CommonCheck.isValidRole(CommonConstant.SCREEN_NO.REPORT_ENTRY.value, 
				sessionmap.get("ID").toString())) {
			return "role";
		}
		
		//　formデータ設定
		reportContentFrm = (ReportContentForm)request.getAttribute("reportContentFrm");
		List<ReportContentFormDetail> detailList = reportContentFrm.getReportContentDetail();
		
		if(validateData(detailList))
			return "success";
		
		//　社員番号
		String emp_cd = reportContentFrm.getEmp_cd();
		//　週報提出日
		String started_date = reportContentFrm.getStarted_date()==null? reportContentFrm.getStarted_date_hidden() : reportContentFrm.getStarted_date();
		Connection conn = null;
		try {
			conn = DataConnection.getConnection();
			conn.setAutoCommit(false);
			String sql = "SELECT report_cd FROM reportcontent WHERE emp_cd ='" +emp_cd+ "' AND started_date ='"+started_date+"'";
			PreparedStatement ps1 = conn.prepareStatement(sql);
			ResultSet rs = ps1.executeQuery();

			String rptCd = "";
			
			if(rs!=null && rs.next()) {
				rptCd = rs.getString("report_cd");
				sql = "UPDATE reportcontent SET customer_name=?, manager_comment=?, pe_comment=?, gl_comment=?, emp_comment=?, updated_date=? WHERE report_cd=?";
				ps1=conn.prepareStatement(sql);
				ps1.setString(1, reportContentFrm.getCustomer_name());
				ps1.setString(2, reportContentFrm.getManager_comment());
				ps1.setString(3, reportContentFrm.getPe_comment());
				ps1.setString(4, reportContentFrm.getGl_comment());
				ps1.setString(5, reportContentFrm.getEmp_comment());
				ps1.setString(6, getSystemDate());
				ps1.setString(7, rptCd);
				
				ps1.executeUpdate();
			} else {
				//　データベース登録
				//sql = "INSERT INTO reportcontent (emp_cd, started_date, customer_name, manager_comment, pe_comment, gl_comment, emp_comment, created_date, updated_date) VALUES(?,?,?,?,?,?,?,?,?)";
				sql = "INSERT INTO reportcontent (emp_cd, started_date, customer_name, manager_comment, pe_comment, gl_comment, emp_comment, created_date, updated_date) "+
					  "SELECT * FROM (SELECT ? AS emp_cd, ? AS started_date, ? AS customer_name, ? AS manager_comment, ? AS pe_comment, ? AS gl_comment, ? AS emp_comment, ? AS created_date, ? AS updated_date) AS tmp "+
					  "WHERE NOT EXISTS ( SELECT emp_cd,started_date From reportcontent Where emp_cd = ? and started_date = ?) LIMIT 1";
			
				ps1=conn.prepareStatement(sql);
				ps1.setString(1, reportContentFrm.getEmp_cd());
				ps1.setString(2, started_date);
				ps1.setString(3, reportContentFrm.getCustomer_name());
				ps1.setString(4, reportContentFrm.getManager_comment());
				ps1.setString(5, reportContentFrm.getPe_comment());
				ps1.setString(6, reportContentFrm.getGl_comment());
				ps1.setString(7, reportContentFrm.getEmp_comment());
				ps1.setString(8, getSystemDate());
				ps1.setString(9, getSystemDate());
				ps1.setString(10, reportContentFrm.getEmp_cd());
				ps1.setString(11, started_date);
				ps1.executeUpdate();
				
				sql = "SELECT report_cd FROM reportcontent WHERE emp_cd ='" + emp_cd + "' AND started_date ='"+started_date+"'";
				ps1 = conn.prepareStatement(sql);
				rs = ps1.executeQuery();
				if(rs!=null && rs.next()) {
					rptCd = rs.getString("report_cd");
				}
				
			}
			if(!rptCd.isEmpty()) {
				sql = "DELETE FROM reportcontentdetail WHERE report_cd=?";
				ps1=conn.prepareStatement(sql);
				ps1.setString(1, rptCd);
				ps1.executeUpdate();
				for (ReportContentFormDetail detail : detailList) {

					// reportcontentdetailテーブル登録				
					sql = "INSERT INTO reportcontentdetail (report_cd, work_date, content, plan_progress, plan_time, result, actual_progress, actual_time, created_date, updated_date) VALUES(?,?,?,?,?,?,?,?,?,?)";
					
					ps1=conn.prepareStatement(sql);
					ps1.setString(1, rs.getString("report_cd"));
					ps1.setString(2, detail.getWork_date()==null? detail.getWork_date_hidden(): detail.getWork_date());
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
			
			ps1.close();
			conn.commit();
			conn.setAutoCommit(true);
			
			String search = reportContentFrm.getSearch();

			clearCondition();
			
			if ("0".equals(search)) {
				reportContentFrm.setSearch("2");
				reportContentFrm.setEmp_cd(reportContentFrm.getEmp_cd());
				addActionMessage("更新しました。");
			} else {
				reportContentFrm.setSearch("");
				reportContentFrm.setEmp_cd((String)sessionmap.get("ID"));
				addActionMessage("登録しました。");
			}
		} catch (Exception e) {
			if (conn != null)
				try {
					conn.rollback();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			e.printStackTrace();
			return ERROR;
		}	
		
		return SUCCESS;
	}
	
	/**
	 * 週報一覧から更新処理
	 * @return
	 */
	public String edit() {
		try {
			// 権限チェック
			if (sessionmap.get("ID") == null) {
				return "session";
			} else if(!CommonCheck.isValidRole(CommonConstant.SCREEN_NO.REPORT_ENTRY.value, 
					sessionmap.get("ID").toString())) {
				return "role";
			}
			
			String report_cd = this.hiddenId;
			sessionmap.put("pageReportList", pageNo);
			sessionmap.put("searchReportList", "RETURN");
			// お客様リスト取得
			getCustomerList();
			
			// 権限チェック
			checkPermission();
			
			Connection conn = DataConnection.getConnection();
			String sql = "SELECT * FROM reportcontent report, reportcontentdetail detail WHERE report.report_cd ='" +report_cd+ "' AND report.report_cd = detail.report_cd ORDER BY detail.work_date,reportdetail_cd";
			PreparedStatement ps1 = conn.prepareStatement(sql);
			ResultSet rs = ps1.executeQuery();			
			
			int rowCount=0;
			List<ReportContentFormDetail> detailList = new ArrayList<ReportContentFormDetail>(); 
			String wdate = "";
			while (rs.next()) {
				
				if (rowCount==0) {
					// 社員番号と日付で検索したデータを設定する
					reportContentFrm.setEmp_cd(rs.getString("emp_cd"));
					reportContentFrm.setEmp_comment(rs.getString("emp_comment"));
					reportContentFrm.setGl_comment(rs.getString("gl_comment"));
					reportContentFrm.setManager_comment(rs.getString("manager_comment"));
					reportContentFrm.setPe_comment(rs.getString("pe_comment"));
					reportContentFrm.setStarted_date(rs.getString("started_date"));
					reportContentFrm.setCustomer_name(rs.getString("customer_name"));
				}
				
				ReportContentFormDetail detail = new ReportContentFormDetail();
				detail.setContents(rs.getString("content"));
				detail.setPlan_progress(rs.getString("plan_progress"));
				detail.setPlan_time(rs.getString("plan_time"));
				detail.setResult(rs.getString("result"));
				if (!wdate.equals(rs.getString("work_date"))) {
					detail.setWork_date(rs.getString("work_date"));
					detail.setMode("1");//EditMode
					wdate = rs.getString("work_date");
				}
				else{
					detail.setMode("0");
				}
					
				detail.setWork_date_hidden(rs.getString("work_date"));
				detail.setActual_time(rs.getString("actual_time"));
				detail.setActual_progress(rs.getString("actual_progress"));
				
				detailList.add(detail);
				
				rowCount++;
			}
			
			//　更新画面から遷移するとき
			reportContentFrm.setSearch("0");
			reportContentFrm.setReportContentDetail(detailList);
			request.setAttribute("reportContentFrm", reportContentFrm);
			
		} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}
		
		return SUCCESS;
	}
	
	/**
	 * 権限チェック
	 */
	private void checkPermission(){
		// セッションデータ取得
		String role = (String)sessionmap.get("ROLE");
		
		// 初期設定
		reportContentFrm.setDisabled_managercomment("");
		reportContentFrm.setDisabled_pecomment("");
		reportContentFrm.setDisabled_empcomment("");
		reportContentFrm.setDisabled_glcomment("");
		
		// propertiesファイル取得
		ResourceBundle rb = ResourceBundle.getBundle("messages_ja");
		
		// フォームに設定する
		if(rb.getString("role.gl").equals(role)) {
			reportContentFrm.setDisabled_managercomment("disabled");
			reportContentFrm.setDisabled_pecomment("disabled");
		} else if(rb.getString("role.manager").equals(role)) {
			reportContentFrm.setDisabled_empcomment("disabled");
			reportContentFrm.setDisabled_glcomment("disabled");
		} else if(rb.getString("role.member").equals(role)) {
			reportContentFrm.setDisabled_managercomment("disabled");
			reportContentFrm.setDisabled_glcomment("disabled");
			reportContentFrm.setDisabled_pecomment("disabled");
		} 
	}

	/**
	 * 検索条件をクリアする処理
	 */
	public String clearCondition(){	
		// 権限チェック
		if (sessionmap.get("ID") == null) {
			return "session";
		} else if(!CommonCheck.isValidRole(CommonConstant.SCREEN_NO.REPORT_ENTRY.value, 
				sessionmap.get("ID").toString())) {
			return "role";
		}
		
		// 検索条件エリアをクリアする
		reportContentFrm.setEmp_cd((String)sessionmap.get("ID"));
		reportContentFrm.setPe_comment("");
		reportContentFrm.setManager_comment("");
		reportContentFrm.setGl_comment("");
		reportContentFrm.setStarted_date("");
		reportContentFrm.setEmp_comment("");
		reportContentFrm.setCustomer_name("-1");
		
		reportContentFrm.setDisabled_empcomment("");
		reportContentFrm.setDisabled_glcomment("");
		reportContentFrm.setDisabled_managercomment("");
		reportContentFrm.setDisabled_pecomment("");
		
		// 検索結果エリアを非表示
		reportContentFrm.setSearch("");
		reportContentFrm.setReportContentDetail(new ArrayList<ReportContentFormDetail>());
		
		// お客様リスト取得
		getCustomerList();
		
		return SUCCESS; 
	}
	
	/**
	 * 検索条件をクリアする処理
	 */
	public String clearResult(){
		// 権限チェック
		if (sessionmap.get("ID") == null) {
			return "session";
		} else if(!CommonCheck.isValidRole(CommonConstant.SCREEN_NO.REPORT_ENTRY.value, 
				sessionmap.get("ID").toString())) {
			return "role";
		}
		
		// 検索条件エリアをクリアする
		reportContentFrm.setEmp_comment("");
		reportContentFrm.setPe_comment("");
		reportContentFrm.setManager_comment("");
		reportContentFrm.setGl_comment("");
				
		// 検索結果エリアを非表示
		reportContentFrm.setSearch("1");
		List<ReportContentFormDetail> detailList = reportContentFrm.getReportContentDetail();
		
		int count = 0;
		
		for(ReportContentFormDetail detail: detailList) {
			if(count == 0) {
				reportContentFrm.setStarted_date(detail.getWork_date());
				reportContentFrm.setStarted_date_hidden(detail.getWork_date());
			}		
			detail.setActual_progress("");
			detail.setContents("");
			detail.setPlan_progress("");
			detail.setPlan_time("");
			count++;
		}
		reportContentFrm.setReportContentDetail(detailList);
		
		// お客様リスト取得
		getCustomerList();
		reportContentFrm.setCustomer_name("");
		
		checkPermission();
		
		return SUCCESS; 
	}

	/**
	 * データ検証
	 * @return
	 */
	private boolean validateData(List<ReportContentFormDetail> detailList) {
		boolean isNoContents = false;
		boolean isActualPro = false;
		boolean isActualTime = false;
		boolean isPlanPro = false;
		boolean isPlanTime = false;
		boolean isAllEmpty = true;
		
		// propertiesファイル取得
		ResourceBundle rb = ResourceBundle.getBundle("messages_ja");
				
		for (ReportContentFormDetail detail:detailList) {
			detail.setContents(detail.getContents().trim());
			detail.setActual_progress(detail.getActual_progress().trim());
			detail.setActual_time(detail.getActual_time().trim());
			detail.setPlan_progress(detail.getPlan_progress().trim());
			detail.setPlan_time(detail.getPlan_time().trim());
			//　作業内容
			if (detail.getContents().isEmpty() && (!detail.getActual_progress().isEmpty() || 
					!detail.getActual_time().isEmpty() || !detail.getPlan_progress().isEmpty() ||
					!detail.getPlan_time().isEmpty()))
				isNoContents = true;
			if (detail.getPlan_time().equalsIgnoreCase("00:00") || detail.getActual_time().equalsIgnoreCase("00:00"))
				isNoContents = false;
			//　進捗結果
			if ( !isNumber(detail.getActual_progress()))
				isActualPro = true;
			//　進捗予定
			if ( !isNumber(detail.getPlan_progress()))
				isPlanPro = true;
			if (!detail.getContents().isEmpty() || !detail.getActual_progress().isEmpty() || 
					!detail.getActual_time().isEmpty() || !detail.getPlan_progress().isEmpty() ||
					!detail.getPlan_time().isEmpty())
				isAllEmpty = false;
		}
		if(isAllEmpty){
			addActionError(rb.getString("error.report.entry.empty"));
		}
		if (isNoContents && !isAllEmpty){
			addActionError(rb.getString("error.report.entry.content"));
		}
		if (isPlanPro){
			addActionError(rb.getString("error.report.entry.planpro"));
		}
		if (isPlanTime){
			addActionError(rb.getString("error.report.entry.plantime"));
		}
		if (isActualPro){
			addActionError(rb.getString("error.report.entry.actualpro"));
		}
		if (isActualTime){
			addActionError(rb.getString("error.report.entry.actualtime"));
		}
		if (!hasActionErrors()){
			return false;
		}
		reportContentFrm.setSearch("1");
		reportContentFrm.setReportContentDetail(detailList);
		reportContentFrm.setStarted_date(reportContentFrm.getStarted_date_hidden());
		request.setAttribute("reportContentFrm", reportContentFrm);
		
		getCustomerList();
		checkPermission();

		return true;
	}
	
	/**
	 * 
	 * @param val
	 * @return
	 */
	private boolean isNumber(String val)
	{
		if (val.isEmpty()) return true;
		try  
		  {  
		    Double.parseDouble(val);  
		  }  
		  catch(NumberFormatException nfe)  
		  {  
		    return false;  
		  }  
		  return true;
	}

	/**
	 * システム日付取得
	 * @return
	 */
	private String getSystemDate(){
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		Date date = new Date();
		return dateFormat.format(date);
	}
	
	/**
	 * 日付を加算する
	 * @param inputDate
	 * @param addNo
	 * @return
	 */
	private String addDate(String inputDate, int addNo) {
		String outputDate="";
		try {
			SimpleDateFormat sourceDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat desDateFormat = new SimpleDateFormat("yyyy/MM/dd");
			
			Date dt = sourceDateFormat.parse(inputDate);
			Calendar cal = Calendar.getInstance();
			cal.setTime(dt);
			cal.add(Calendar.DATE, addNo);			
			outputDate = desDateFormat.format(cal.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return outputDate;
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
	
	public HttpServletRequest getServletRequest() {
		return request;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	@Override
	public void setSession(Map<String, Object> session) {
		this.sessionmap = (SessionMap)session;
	}
}
