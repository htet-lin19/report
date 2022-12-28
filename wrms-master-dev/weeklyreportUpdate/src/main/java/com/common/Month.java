package com.common;

public enum Month {
	JANUARY(0, "January"), 
	FEBRUARY(1, "February"), 
	MARCH(2, "March"), 
	APRIL(3, "April"), 
	MAY(4, "May"), 
	JUNE(5, "June"), 
	JULY(6, "July"), 
	AUGUST(7, "August"), 
	SEPTEMBER(8, "September"), 
	OCTOBER(9, "October"), 
	NOVEMBER(10, "November"), 
	DECEMBER(11, "December");
	
	private int id;
	private String month;


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	private Month() {
	}

	private Month(int id, String month) {
		this.id = id;
		this.month = month;
	}
}