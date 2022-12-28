package com.action;

import java.util.List;
import java.util.Map;

import org.apache.struts2.dispatcher.SessionMap;
import org.apache.struts2.interceptor.SessionAware;

import com.common.CommonCheck;
import com.common.CommonConstant;
import com.model.AttendanceListForm;
import com.opensymphony.xwork2.ActionSupport;

public class HomeNotificationAction extends ActionSupport implements SessionAware {
	
	private static final long serialVersionUID = 1L;
	
	AttendanceListForm attendanceListFrm = new AttendanceListForm();
	SessionMap<String, List<String>> sessionlist;
	SessionMap<String, String> sessionmap;
	List<String> notificationList;
	
	
	public String execute() {
		// 権限チェック
		if (sessionmap.get("ID") == null) {
			return "session";
		} else if(!CommonCheck.isValidRole(CommonConstant.SCREEN_NO.ATTENDANCE_LIST.value, 
				sessionmap.get("ID").toString())) {
			return "role";
		}
		
		// 通知リストチェック
		String count = sessionmap.get("NOTIFICATION_COUNT");
		if (!count.equals("0")) {
			notificationList = (List<String>) sessionlist.get("NOTIFICATION_LIST");
			if (notificationList != null) {
				attendanceListFrm.setNotification_date(notificationList);
			}
		} else {
			notificationList = null;
			attendanceListFrm.setNotification_date(notificationList);
		}
		
		return SUCCESS;
	}

	public AttendanceListForm getAttendanceListFrm() {
		return attendanceListFrm;
	}

	public void setAttendanceListFrm(AttendanceListForm attendanceListFrm) {
		this.attendanceListFrm = attendanceListFrm;
	}

	@Override
	public void setSession(Map<String, Object> map) {
		this.sessionlist = (SessionMap)map;
		this.sessionmap = (SessionMap)map;
	}
}
