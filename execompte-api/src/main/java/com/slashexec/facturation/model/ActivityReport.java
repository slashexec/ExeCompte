package com.slashexec.facturation.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class ActivityReport {
	
	@Id
	private String id;
	private String username;
	private Date periodStartDate;
	private List<PeriodDay> periodDays;
	private boolean billed;
	//Computed Fields
	private double periodTotalTime, periodTotalBusinessTime, periodTotalTeleWorkTime;
	
	private double unitPrice, totalHT , totalTTC, totalTVA;
	
	private String prestation;


	public ActivityReport() {
	}

	public ActivityReport(String id, String username, Date periodStartDate, boolean billed) {
		super();
		this.id = id;
		this.username = username;
		this.billed = billed;
		this.periodStartDate = periodStartDate;
		initPeriodData(periodStartDate);
	}

	private void initPeriodData(Date periodStartDate) {
		Calendar calTmp = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		calTmp.setTime(periodStartDate);

		//Forcing startDate to 1st of month
		Calendar startDate = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		startDate.set(calTmp.get(Calendar.YEAR), calTmp.get(Calendar.MONTH), 1, 0, 0, 0);
		startDate.set(Calendar.MILLISECOND, 0);

		//Setting periodStartDate  1st of month
		this.periodStartDate = startDate.getTime();

		//Generate period days for the whole month
		Calendar firstOfNextMonth = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		firstOfNextMonth.set(startDate.get(Calendar.YEAR), startDate.get(Calendar.MONTH) + 1, 1, 0, 0, 0);
		firstOfNextMonth.set(Calendar.MILLISECOND, 0);

		this.periodDays = new ArrayList<PeriodDay>();
		this.periodTotalBusinessTime = 0;
		this.periodTotalTeleWorkTime = 0;
		this.periodTotalTime = 0;

		int dayOfWeek;
		PeriodDay currentPeriodDay;

		calTmp.setTime(startDate.getTime());
		while (calTmp.before(firstOfNextMonth)) {
			dayOfWeek = calTmp.get(Calendar.DAY_OF_WEEK);

			currentPeriodDay = new PeriodDay(
					calTmp.getTime(), 
					(dayOfWeek != Calendar.SUNDAY && dayOfWeek != Calendar.SATURDAY), 
					true, //(dayOfWeek == Calendar.WEDNESDAY), 
					(dayOfWeek == Calendar.SUNDAY || dayOfWeek == Calendar.SATURDAY) ? 0 : 1
					);

			this.periodDays.add(currentPeriodDay);

			if (currentPeriodDay.isBusinessDay()) {
				this.periodTotalBusinessTime += currentPeriodDay.getTime();
			}

			if (currentPeriodDay.isTelework()) {
				this.periodTotalTeleWorkTime += currentPeriodDay.getTime();
			}
			this.periodTotalTime += currentPeriodDay.getTime();

			calTmp.add(Calendar.DATE, 1);

		}

	}


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Date getPeriodStartDate() {
		return periodStartDate;
	}

	public void setPeriodStartDate(Date periodStartDate) {
		this.periodStartDate = periodStartDate;
	}

	public List<PeriodDay> getPeriodDays() {
		return periodDays;
	}

	public void setPeriodDays(List<PeriodDay> periodDays) {
		this.periodDays = periodDays;
	}

	public boolean isBilled() {
		return billed;
	}

	public void setBilled(boolean billed) {
		this.billed = billed;
	}

	public double getPeriodTotalTime() {
		return periodTotalTime;
	}

	public void setPeriodTotalTime(double periodTotalTime) {
		this.periodTotalTime = periodTotalTime;
	}

	public double getPeriodTotalBusinessTime() {
		return periodTotalBusinessTime;
	}

	public void setPeriodTotalBusinessTime(double periodTotalBusinessTime) {
		this.periodTotalBusinessTime = periodTotalBusinessTime;
	}

	public double getPeriodTotalTeleWorkTime() {
		return periodTotalTeleWorkTime;
	}

	public void setPeriodTotalTeleWorkTime(double periodTotalTeleWorkTime) {
		this.periodTotalTeleWorkTime = periodTotalTeleWorkTime;
	}

	
	public double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public double getTotalHT() {
		return totalHT;
	}

	public void setTotalHT(double totalHT) {
		this.totalHT = totalHT;
	}

	public double getTotalTTC() {
		return totalTTC;
	}

	public void setTotalTTC(double totalTTC) {
		this.totalTTC = totalTTC;
	}

	public double getTotalTVA() {
		return totalTVA;
	}

	public void setTotalTVA(double totalTVA) {
		this.totalTVA = totalTVA;
	}

	public String getPrestation() {
		return prestation;
	}

	public void setPrestation(String prestation) {
		this.prestation = prestation;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (billed ? 1231 : 1237);
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((periodDays == null) ? 0 : periodDays.hashCode());
		result = prime * result + ((periodStartDate == null) ? 0 : periodStartDate.hashCode());
		long temp;
		temp = Double.doubleToLongBits(periodTotalBusinessTime);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(periodTotalTeleWorkTime);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(periodTotalTime);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((prestation == null) ? 0 : prestation.hashCode());
		temp = Double.doubleToLongBits(totalHT);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(totalTTC);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(totalTVA);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(unitPrice);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((username == null) ? 0 : username.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ActivityReport other = (ActivityReport) obj;
		if (billed != other.billed)
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (periodDays == null) {
			if (other.periodDays != null)
				return false;
		} else if (!periodDays.equals(other.periodDays))
			return false;
		if (periodStartDate == null) {
			if (other.periodStartDate != null)
				return false;
		} else if (!periodStartDate.equals(other.periodStartDate))
			return false;
		if (Double.doubleToLongBits(periodTotalBusinessTime) != Double.doubleToLongBits(other.periodTotalBusinessTime))
			return false;
		if (Double.doubleToLongBits(periodTotalTeleWorkTime) != Double.doubleToLongBits(other.periodTotalTeleWorkTime))
			return false;
		if (Double.doubleToLongBits(periodTotalTime) != Double.doubleToLongBits(other.periodTotalTime))
			return false;
		if (prestation == null) {
			if (other.prestation != null)
				return false;
		} else if (!prestation.equals(other.prestation))
			return false;
		if (Double.doubleToLongBits(totalHT) != Double.doubleToLongBits(other.totalHT))
			return false;
		if (Double.doubleToLongBits(totalTTC) != Double.doubleToLongBits(other.totalTTC))
			return false;
		if (Double.doubleToLongBits(totalTVA) != Double.doubleToLongBits(other.totalTVA))
			return false;
		if (Double.doubleToLongBits(unitPrice) != Double.doubleToLongBits(other.unitPrice))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}
	
	
	

}
