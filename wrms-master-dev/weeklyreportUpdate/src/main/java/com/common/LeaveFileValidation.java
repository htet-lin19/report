/**	
 * 作成履歴：2020/10/19 Kyaw Zin Phyoe
 * 作成概要：LeaveSummaryFileValidation
 */
package com.common;

import java.util.ArrayList;

public class LeaveFileValidation {
	private final String XLS = "application/vnd.ms-excel";
	private final String XLSX = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
	final String HEADER_0 = "社員番号";
	final String HEADER_1 = "氏名";
	final String HEADER_2 = "前年度繰越分";
	final String HEADER_3 = "今年度付与分";
	final String HEADER_4 = "取得可能日数";
	final String HEADER_5 = "有給残日数";
	final String HEADER_6 = "半休";
	final String HEADER_7 = "全休";
	final String HEADER_8 = "特別休暇";
	final String HEADER_9 = "代休";
	

	public boolean validFileExtension(String fileName) {
		if (fileName.equalsIgnoreCase(XLS) || fileName.equalsIgnoreCase(XLSX)) {
			return false;
		}
		return true;
	}

	public boolean invalidFileFormat(ArrayList<String> list) {
		for (int i = 0; i < 10; i++) {
			if (i == 0 || i == 1 || i == 2 || i == 3 || i == 4 || i == 5 || i == 6 || i == 7 || i == 8 || i == 9) {
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
				default:
					break;
				}
			}
		}
		return false;
	}
}
