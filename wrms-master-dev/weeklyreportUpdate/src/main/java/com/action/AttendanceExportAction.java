package com.action;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
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
import java.util.TimeZone;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.struts2.dispatcher.SessionMap;
import org.apache.struts2.interceptor.SessionAware;

import com.common.CommonCheck;
import com.common.CommonConstant;
import com.jdbc.DataConnection;
import com.model.AttendanceListForm;
import com.model.AttendanceListFormDetail;
import com.opensymphony.xwork2.ActionSupport;

public class AttendanceExportAction extends ActionSupport implements SessionAware{
	private static final long serialVersionUID = 1L;
	AttendanceListForm attendanceListFrm=new AttendanceListForm();
	List<AttendanceListFormDetail> attendanceDetailList = new ArrayList<AttendanceListFormDetail>();
	private InputStream inputStream;
	private String reportFile;
	private String start_date ="";
	private int year;
	private int month;
	SessionMap<String, String> sessionmap;

	public InputStream getInputStream() {
		return inputStream;
	}
	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}
	public String getReportFile() {
		return reportFile;
	}
	public void setReportFile(String reportFile) {
		this.reportFile = reportFile;
	}

	/**
	 * メニューから呼び出す処理
	 * @return
	 */
	public String execute(){
		// 権限チェック
		if (sessionmap.get("ID") == null) {
			return "session";
		} else if(!CommonCheck.isValidRole(CommonConstant.SCREEN_NO.EXPORT.value, 
				sessionmap.get("ID").toString())) {
			return "role";
		}
		
		checkPermission();
		
		attendanceListFrm.setStart_date(start_date);
		attendanceListFrm.setEmp_cd((String)sessionmap.get("ID"));
		return SUCCESS;
	}
	
	/**
	 * 対象社員の勤務時間詳細情報を1ヶ月分取得
	 * @return　1ヶ月分の勤怠リスト
	 */
	private List<AttendanceListFormDetail> fetchList() {
		try {
			Connection conn = DataConnection.getConnection();
			//2020/10/07 GICM KZP 印刷レイアウト変更 対応 Start
            /*String sql = "SELECT att.id, start_time, end_time, DATE_FORMAT(choose_date, '%m月%d日')choose_date, "
					+ "work_hour, overtime, midnight_overtime, "
					+ "compensatory_leave_hour, releaving_leave_hour, task_description, "
					+ "midnight_compensatory_leave_hour, break_time, day, "
					+ "att.emp_cd, emp.emp_name FROM attendance att, employeemaster emp WHERE att.emp_cd = emp.emp_cd";*/
			
			String sql = "SELECT att.id, start_time, end_time, DATE_FORMAT(choose_date, '%m月%d日')choose_date, "
					+ "work_hour, overtime, midnight_overtime, overtime_sunday"
					+ ", compensatory_comment, break_time, task_description, day"
					+ ", att.emp_cd, emp.emp_name FROM attendance att, employeemaster emp WHERE att.emp_cd = emp.emp_cd";
			//2020/10/07 GICM KZP 印刷レイアウト変更 対応 End
			
			if (StringUtils.isNotBlank(attendanceListFrm.getEmp_cd())) {
				sql = sql + " AND att.emp_cd = '" + attendanceListFrm.getEmp_cd() + "'";
			}

			if (StringUtils.isNotBlank(attendanceListFrm.getStart_date())) {
				sql = sql + " AND choose_date >= '" + start_date+ "' AND choose_date < ('" + start_date + "' + INTERVAL 1 MONTH)";
			}
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();

			while(rs.next()){
				AttendanceListFormDetail detail=new AttendanceListFormDetail();
				detail.setEmp_cd(rs.getString("emp_cd"));
				attendanceListFrm.setEmp_name(rs.getString("emp_name"));
				detail.setChoose_date(rs.getString("choose_date"));
				detail.setStart_time(rs.getString("start_time"));
				detail.setEnd_time(rs.getString("end_time"));
				detail.setBreak_time(rs.getString("break_time"));
				detail.setWork_hour(rs.getString("work_hour"));
				//2020/10/07 GICM KZP 印刷レイアウト変更 対応 Start
				detail.setOvertime(rs.getString("overtime"));
				detail.setMidnight_overtime(rs.getString("midnight_overtime"));
				//detail.setCompensatory_leave_hour(rs.getString("compensatory_leave_hour"));
				//detail.setReleaving_leave_hour(rs.getString("releaving_leave_hour"));
				detail.setOvertime_sunday(rs.getString("overtime_sunday"));
				detail.setCompensatory_comment(rs.getString("compensatory_comment"));
				//detail.setMidnight_compensatory_leave_hour(rs.getString("midnight_compensatory_leave_hour"));
				//2020/10/07 GICM KZP 印刷レイアウト変更 対応 End
				detail.setTask_description(rs.getString("task_description"));
				detail.setDay(rs.getString("day"));
				attendanceDetailList.add(detail);
			}

			attendanceListFrm.setAttendanceListDetail(attendanceDetailList);

			rs.close();
			ps.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return attendanceDetailList;
	}

	/**
	 * Excel出力
	 * @return
	 */
	public String excel() {
		try {
			// 権限チェック
			if (sessionmap.get("ID") == null) {
				return "session";
			} else if(!CommonCheck.isValidRole(CommonConstant.SCREEN_NO.EXPORT.value,
					sessionmap.get("ID").toString())) {
				return "role";
			}			

			// 勤怠フォーム情報がある場合、テンプレートファイルに書き込み準備を行う
			if(StringUtils.isNotBlank(attendanceListFrm.getStart_date())){
				String[] start_date_arr = attendanceListFrm.getStart_date().split("-", 0);
				year = Integer.parseInt(start_date_arr[0]);
				month = Integer.parseInt(start_date_arr[1]);
			}

			start_date = year + "-" + month + "-" + "01";			
					
			String templateFile = "E0000000_Attendance" + "_" + year + ".xlsx";
			
			//String templateFile = "E0000000_Attendance.xlsx";
			ClassLoader classLoader = this.getClass().getClassLoader();
			InputStream inputStream = classLoader.getResourceAsStream(templateFile);

			// Excel出力ための、勤務情報のリストを取得
			List<AttendanceListFormDetail> attendanceList = fetchList();

			if(attendanceList.size() == 0) {
				addActionError("対象データが存在しません。");
				execute();
				return ERROR;
			}

			XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
			XSSFSheet sheet = workbook.getSheetAt(month-1);

			// ファイル名設定			
			String reportFile1 = attendanceListFrm.getEmp_cd() + "(" +  attendanceListFrm.getEmp_name() + ")"  
					+ "_" + year +String.format("%02d",month) +  "_" + "Attendance" +
					 "".concat(".xlsx");

			reportFile = new String(reportFile1.getBytes(), "ISO8859_1");

			// データ設定
			writeBook(workbook, attendanceList, sheet);

			// 不要シート削除
			for(int i=11; i>=0; i--) {
				if(i != month-1)
					workbook.removeSheetAt(i);
			}

			//　シート名設定
			//　workbook.setSheetName(0, month +"月時間外勤務管理表");

			try {
				ByteArrayOutputStream boas = new ByteArrayOutputStream();
				workbook.write(boas);
				setInputStream(new ByteArrayInputStream(boas.toByteArray()));
			}
			catch (IOException e) {
				e.printStackTrace();
				return ERROR;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return ERROR;
		}catch (IOException e) {
			e.printStackTrace();
			return ERROR;
		}catch(Exception e)
		{
			e.printStackTrace();
			return ERROR;
		}
		
		return SUCCESS;
	}

	/**
	 * Excelに書き込む
	 * @param attendanceList
	 * @param sheet
	 */
	private void writeBook(XSSFWorkbook workbook, List<AttendanceListFormDetail> attendanceList, XSSFSheet sheet) {
		XSSFRow row = sheet.getRow(0);
		XSSFCell cell = row.getCell(1);
		cell.setCellValue(start_date);
		cell = row.getCell(7);
		cell.setCellValue(attendanceListFrm.getEmp_cd());
		cell = row.getCell(9);
		cell.setCellValue(attendanceListFrm.getEmp_name());

		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
		timeFormat.setTimeZone(TimeZone.getTimeZone("JTS"));

		int max = getMaxRow();

		for (AttendanceListFormDetail detail: attendanceList ) {
			for(int i=3; i<=max+4; i++) {

				// 日付
				XSSFCell cell1 = sheet.getRow(i).getCell(1);
				String attendanceDate="";
				if(cell1.getCellType() == Cell.CELL_TYPE_NUMERIC) {
					Date tempDate= DateUtil.getJavaDate(cell1.getNumericCellValue());
					SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日");
					attendanceDate = sdf.format(tempDate);
				}

				if(attendanceDate.equals(detail.getChoose_date())) 
				{
					if(StringUtils.isNotBlank(detail.getStart_time())){
						// 開始時刻
						sheet.getRow(i).getCell(4).setCellValue(DateUtil.convertTime(detail.getStart_time()));
					}
					if(StringUtils.isNotBlank(detail.getEnd_time())){
						// 終了時刻
						sheet.getRow(i).getCell(5).setCellValue(DateUtil.convertTime(detail.getEnd_time()));
					}
					if(StringUtils.isNotBlank(detail.getBreak_time())){
						// 休憩時間
						sheet.getRow(i).getCell(6).setCellValue(DateUtil.convertTime(detail.getBreak_time()));
					}
					if(StringUtils.isNotBlank(detail.getWork_hour())){
						//　作業時間
						sheet.getRow(i).getCell(7).setCellValue(DateUtil.convertTime(detail.getWork_hour()));
					}
					//2020/10/07 GICM KZP 印刷レイアウト変更 対応 Start
					if(StringUtils.isNotBlank(detail.getOvertime())){
						// 時間外勤務(深夜外)
						sheet.getRow(i).getCell(8).setCellValue(DateUtil.convertTime(detail.getOvertime()));
					}
					if(StringUtils.isNotBlank(detail.getOvertime_sunday())){
						// 法定休日
						sheet.getRow(i).getCell(9).setCellValue(DateUtil.convertTime(detail.getOvertime_sunday()));
					}
					if(StringUtils.isNotBlank(detail.getMidnight_overtime())){
						// 深夜残業
						sheet.getRow(i).getCell(10).setCellValue(DateUtil.convertTime(detail.getMidnight_overtime()));
					}
					// その他
					sheet.getRow(i).getCell(11).setCellValue(detail.getCompensatory_comment());
					
					// 備考
					sheet.getRow(i).getCell(12).setCellValue(detail.getTask_description());
					break;
					
					/*if(StringUtils.isNotBlank(detail.getOvertime())){
						// 時間外勤務(深夜外)
						sheet.getRow(i).getCell(8).setCellValue(DateUtil.convertTime(detail.getOvertime()));
					}if(StringUtils.isNotBlank(detail.getMidnight_overtime())){
						// 深夜時間外勤務
						sheet.getRow(i).getCell(9).setCellValue(DateUtil.convertTime(detail.getMidnight_overtime()));
					}
					if(StringUtils.isNotBlank(detail.getCompensatory_leave_hour())){
						// 代休勤務(深夜外)
						sheet.getRow(i).getCell(10).setCellValue(DateUtil.convertTime(detail.getCompensatory_leave_hour()));
					}
					if(StringUtils.isNotBlank(detail.getMidnight_compensatory_leave_hour())){
						// 深夜代休勤務
						sheet.getRow(i).getCell(11).setCellValue(DateUtil.convertTime(detail.getMidnight_compensatory_leave_hour()));
					}
					if(StringUtils.isNotBlank(detail.getReleaving_leave_hour())){
						// 代休取得
						sheet.getRow(i).getCell(12).setCellValue(DateUtil.convertTime(detail.getReleaving_leave_hour()));
					}
					// 備考
					sheet.getRow(i).getCell(13).setCellValue(detail.getTask_description());*/
					//2020/10/07 GICM KZP 印刷レイアウト変更 対応 End
				}
			}
		}

		FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
		evaluator.evaluateAll();
	}

	/**
	 * 最大行取得
	 * @return　シートの最大行
	 */
	private int getMaxRow() {
		Calendar c = Calendar.getInstance();
		c.set(year, month-1, 1);
		int max=c.getActualMaximum(Calendar.DAY_OF_MONTH);
		return max;
	}
	
	/**
	 * 権限チェック
	 */
	private void checkPermission() {
		// セッションデータ取得
		String role = (String) sessionmap.get("ROLE");

		// propertiesファイル取得
		ResourceBundle rb = ResourceBundle.getBundle("messages_ja");

		attendanceListFrm.setDisable_empcd("");

		// フォームに設定
		if (rb.getString("role.member").equals(role)) {
			attendanceListFrm.setDisable_empcd("disabled");
		}
	}


	public AttendanceListForm getAttendanceListFrm() {
		return attendanceListFrm;
	}
	public void setAttendanceListFrm(AttendanceListForm attendanceListFrm) {
		this.attendanceListFrm = attendanceListFrm;
	}
	public List<AttendanceListFormDetail> getAttendanceDetailList() {
		return attendanceDetailList;
	}
	public void setAttendanceDetailList(List<AttendanceListFormDetail> attendanceDetailList) {
		this.attendanceDetailList = attendanceDetailList;
	}

	@Override
	public void setSession(Map<String, Object> session) {
		this.sessionmap = (SessionMap)session;
	}
}
