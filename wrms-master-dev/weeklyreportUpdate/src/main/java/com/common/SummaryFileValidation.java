/**
 * 勤務時間サマリ情報インポート画面	
 * 作成履歴：2020/10/06 Theint Sandari Kyaw
 * 作成概要：新規作成　勤務時間サマリ情報ファイル形式をチェックする
 */
package com.common;

import java.util.ArrayList;

public class SummaryFileValidation {
	private final String XLS = "application/vnd.ms-excel";
	private final String XLSX = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
	final String HEADER_0 = "社員番号";
	final String HEADER_1 = "氏名";
	final String HEADER_2 = "総労働時間";
	final String HEADER_3 = "法定時間外労働時間";
	final String HEADER_4 = "法定休日労働時間";
	final String HEADER_5 = "深夜早朝労働時間";
	
	public boolean validFileExtension(String fileName) {
		if (fileName.equalsIgnoreCase(XLS) || fileName.equalsIgnoreCase(XLSX)) {
			return false;
		}
		return true;
	}

	public boolean invalidFileFormat(ArrayList<String> list, ArrayList<String> arrayList) {
		for (int i = 0; i < 17; i++) {
			if (i == 0 || i == 1 || i == 2 || i == 3 || i == 4 || i == 5) {
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
				case 2:
					if (!str.equals(HEADER_2))
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
				default:
					break;
				}
			}
		}
		return false;
	}
}
