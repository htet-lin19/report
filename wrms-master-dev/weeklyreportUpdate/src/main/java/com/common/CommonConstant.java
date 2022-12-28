package com.common;

public class CommonConstant {
	// 画面リスト
	public static enum SCREEN_NO {
		LOGOUT("001"),
		REPORT_ENTRY("002"),
		REPORT_LIST("003"),		
		ATTENDANCE_ENTRY("004"),
		ATTENDANCE_LIST("005"),
		ATTENDANCE_SETTING("006"),		
		EMPLOYEE_ENTRY("007"),
		EMPLOYEE_LIST("008"),		
		CUSTOMER_ENTRY("009"),
		CUSTOMER_LIST("010"),
		GROUP_ENTRY("011"),
		GROUP_LIST("012"),
		SIGNUP_ENTRY("013"),
		SIGNUP_LIST("014"),
		EMP_SIGNUP("015"),
		EXPORT("016"),
		IMPORT("017"),
		ATTENDANCE_SUMMARY_INFO("018"),  //2020/10/06 GICM AMTD 勤務時間サマリ情報対応
		ATTENDANCE_SUMMARY_IMPORT("019"),  //2020/10/06 GICM TSDK 勤務時間サマリ情報インポート対応
		LEAVE_SUMMARY_INFO("020"); //2020/10/16 GICM KZP 休暇情報管理対応
		
	
		public String value;
		
		private SCREEN_NO(String value) {
			this.value = value;
		}
	}
}