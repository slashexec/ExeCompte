package com.slashexec.facturation.model;

import java.util.Date;

public class PeriodDay {
	private Date day;
	private boolean isBusinessDay;
	private boolean isTelework;
	//compris entre 0.0 et 1.0
	private double time;
	public PeriodDay(Date day, boolean isBusinessDay, boolean isTelework, double time) {
		super();
		this.day = day;
		this.isBusinessDay = isBusinessDay;
		this.isTelework = isTelework;
		this.time = time;
	}
	public Date getDay() {
		return day;
	}
	public void setDay(Date day) {
		this.day = day;
	}
	public boolean isBusinessDay() {
		return isBusinessDay;
	}
	public void setBusinessDay(boolean isBusinessDay) {
		this.isBusinessDay = isBusinessDay;
	}
	public boolean isTelework() {
		return isTelework;
	}
	public void setTelework(boolean isTelework) {
		this.isTelework = isTelework;
	}
	public double getTime() {
		return time;
	}
	public void setTime(double time) {
		this.time = time;
	}
	
}
