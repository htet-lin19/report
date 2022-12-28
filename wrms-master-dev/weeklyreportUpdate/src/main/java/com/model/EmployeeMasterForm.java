package com.model;

import java.util.ArrayList;
import java.util.List;

public class EmployeeMasterForm{
	String emp_cd;
	String emp_name;
	String button_event;
	int delete_id;
	String search;
	
	private List<GroupListFormDetail> groupFrmDetail = new ArrayList<GroupListFormDetail>();
	private List<EmployeeMasterFormDetail> employeeFrmDetail = new ArrayList<EmployeeMasterFormDetail>();

	public String getEmp_cd() {
		return emp_cd;
	}

	public void setEmp_cd(String emp_cd) {
		this.emp_cd = emp_cd;
	}

	public String getEmp_name() {
		return emp_name;
	}

	public void setEmp_name(String emp_name) {
		this.emp_name = emp_name;
	}

	public String getButton_event() {
		return button_event;
	}

	public void setButton_event(String button_event) {
		this.button_event = button_event;
	}

	public int getDelete_id() {
		return delete_id;
	}

	public void setDelete_id(int delete_id) {
		this.delete_id = delete_id;
	}

	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
	}
	
	public List<GroupListFormDetail> getGroupFrmDetail() {
		return groupFrmDetail;
	}

	public void setGroupFrmDetail(List<GroupListFormDetail> groupFrmDetail) {
		this.groupFrmDetail = groupFrmDetail;
	}

	public List<EmployeeMasterFormDetail> getEmployeeFrmDetail() {
		return employeeFrmDetail;
	}

	public void setEmployeeFrmDetail(List<EmployeeMasterFormDetail> employeeFrmDetail) {
		this.employeeFrmDetail = employeeFrmDetail;
	} 
}