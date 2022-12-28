package com.model;

import java.util.ArrayList;
import java.util.List;

public class ReportContentForm{
	private String report_cd;
	private String emp_cd;
	private String emp_name;
	private String started_date;
	private String created_date;
	private String updated_date;
	private String started_date_hidden;
	private String customer_name;
	private String manager_comment;
	private String pe_comment;
	private String gl_comment;
	private String emp_comment;
	private String search;
	private String disabled_pecomment;
	private String disabled_glcomment;
	private String disabled_empcomment;
	private String disabled_managercomment;
	private String group_name;

	private List<ReportContentFormDetail> reportContentDetail = new ArrayList<ReportContentFormDetail>(); 

	public String getReport_cd() {
		return report_cd;
	}

	public void setReport_cd(String report_cd) {
		this.report_cd = report_cd;
	}

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

	public String getStarted_date() {
		return started_date;
	}

	public void setStarted_date(String started_date) {
		this.started_date = started_date;
	}
	
	public String getCreated_date() {
		return created_date;
	}

	public void setCreated_date(String created_date) {
		this.created_date = created_date;
	}
	
	public String getUpdated_date() {
		return updated_date;
	}

	public void setUpdated_date(String updated_date) {
		this.updated_date = updated_date;
	}
	
	public String getStarted_date_hidden() {
		return started_date_hidden;
	}

	public void setStarted_date_hidden(String started_date_hidden) {
		this.started_date_hidden = started_date_hidden;
	}
	
	public String getCustomer_name() {
		return customer_name;
	}

	public void setCustomer_name(String customer_name) {
		this.customer_name = customer_name;
	}
	
	public String getManager_comment() {
		return manager_comment;
	}

	public void setManager_comment(String manager_comment) {
		this.manager_comment = manager_comment;
	}

	public String getPe_comment() {
		return pe_comment;
	}

	public void setPe_comment(String pe_comment) {
		this.pe_comment = pe_comment;
	}

	public String getGl_comment() {
		return gl_comment;
	}

	public void setGl_comment(String gl_comment) {
		this.gl_comment = gl_comment;
	}

	public String getEmp_comment() {
		return emp_comment;
	}

	public void setEmp_comment(String emp_comment) {
		this.emp_comment = emp_comment;
	}
	
	public String getDisabled_pecomment() {
		return disabled_pecomment;
	}

	public void setDisabled_pecomment(String disabled_pecomment) {
		this.disabled_pecomment = disabled_pecomment;
	}

	public String getDisabled_glcomment() {
		return disabled_glcomment;
	}

	public void setDisabled_glcomment(String disabled_glcomment) {
		this.disabled_glcomment = disabled_glcomment;
	}

	public String getDisabled_empcomment() {
		return disabled_empcomment;
	}

	public void setDisabled_empcomment(String disabled_empcomment) {
		this.disabled_empcomment = disabled_empcomment;
	}

	public String getDisabled_managercomment() {
		return disabled_managercomment;
	}

	public void setDisabled_managercomment(String disabled_managercomment) {
		this.disabled_managercomment = disabled_managercomment;
	}

	public List<ReportContentFormDetail> getReportContentDetail() {
		return reportContentDetail;
	}

	public void setReportContentDetail(List<ReportContentFormDetail> reportContentDetail) {
		this.reportContentDetail = reportContentDetail;
	}
	
	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
	}

	public String getGroup_name() {
		return group_name;
	}

	public void setGroup_name(String group_name) {
		this.group_name = group_name;
	}
}
