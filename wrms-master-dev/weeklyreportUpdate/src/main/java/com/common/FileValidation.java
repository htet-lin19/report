package com.common;

import java.util.ArrayList;

public class FileValidation {
	private final String XLS = "application/vnd.ms-excel";
	private final String XLSX = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
	final String HEADER_0 = "日付";
	final String HEADER_1 = "曜日";
	final String HEADER_2 = "休日割増扱い";
	final String HEADER_3 = "開始時刻";
	final String HEADER_4 = "終了時刻";
	final String HEADER_5 = "休憩時間";
	final String HEADER_6 = "勤務時間";
	//final String HEADER_7 = "時間外勤務(深夜外)";//latest commit
	//final String HEADER_8 = "深夜時間外勤務";
	//final String HEADER_9 = "代休勤務(深夜外)";
	//final String HEADER_10 = "深夜代休勤務";
	//final String HEADER_11 = "代休取得";
	//final String HEADER_12 = "備考";
	//final String HEADER_13 = "所属長確認";
	final String HEADER_7 = "法定時間外";  //2020/10/19 GICM AMTD　新規作成
	final String HEADER_8 = "法定休日";    //2020/10/19 GICM AMTD　新規作成 
	final String HEADER_9 = "深夜早朝";    //2020/10/19 GICM AMTD　新規作成
	final String HEADER_10 = "その他";      //2020/10/19 GICM AMTD　新規作成
	final String HEADER_11 = "備考";
	final String HEADER_12 = "所属長確認";

	public boolean validFileExtension(String fileName) {
		if (fileName.equalsIgnoreCase(XLS) || fileName.equalsIgnoreCase(XLSX)) {
			return false;
		}
		return true;
	}
	
	/**
	 * 
	 * @param list
	 * @return
	 * 2020/10/19 GICM AMTD 勤務時間インポート　対応
	 */
	public boolean invalidFileFormat(ArrayList<String> list) {
		for (int i = 0; i < 13; i++) {
			if (i == 0 || i == 1 || i == 3 || i == 4 || i == 5 || i == 6 || i == 7
					|| i == 8 || i == 9 || i == 10 || i == 11) {
				String str = list.get(i);
				switch (i) {
				case 0:
					if (!str.equals(HEADER_0))
						return true;
					break;
				case 1:
					if (!str.equals(HEADER_1))
						return true;
					break;
				case 3:
					if (!str.equals(HEADER_3))
						return true;
					break;
				case 4:
					if (!str.equals(HEADER_4))
						return true;
					break;
				case 5:
					if (!str.equals(HEADER_5))
						return true;
					break;
				case 6:
					if (!str.equals(HEADER_6))
						return true;
					break;
				case 7:
					if (!str.equals(HEADER_7))
						return true;
					break;
				case 8:
					if (!str.equals(HEADER_8))
						return true;
					break;
				case 9:
					if (!str.equals(HEADER_9))
						return true;
					break;
				case 10:
					if (!str.equals(HEADER_10))
						return true;
					break;
				case 11:
					if (!str.equals(HEADER_11))
						return true;
					break;
				default:
					break;
				}
			}
		}
		return false;
	}
	
	/*public boolean invalidFileFormat(ArrayList<String> list) {
		for (int i = 0; i < 14; i++) {
			if (i == 0 || i == 1 || i == 3 || i == 4 || i == 5 || i == 6 || i == 8 || i == 12) {
				String str = list.get(i);
				switch (i) {
				case 0:
					if (!str.equals(HEADER_0))
						return true;
					break;
				case 1:
					if (!str.equals(HEADER_1))
						return true;
					break;
				case 3:
					if (!str.equals(HEADER_3))
						return true;
					break;
				case 4:
					if (!str.equals(HEADER_4))
						return true;
					break;
				case 5:
					if (!str.equals(HEADER_5))
						return true;
					break;
				case 6:
					if (!str.equals(HEADER_6))
						return true;
					break;
				case 8:
					if (!str.equals(HEADER_8))
						return true;
					break;
				case 12:
					if (!str.equals(HEADER_12))
						return true;
					break;
				default:
					break;
				}
			}
		}
		return false;
	}*/
	
	
}