package com.slashexec.facturation.controller.activityreport;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.slashexec.facturation.model.ActivityReport;

@Service
public class ActivityReportService {
	
	@Autowired
	ActivityReportRepository  activityReportRepository;
	
	@Autowired
	private ActivityReportBilling activityReportBilling;
	
	private static List<ActivityReport> activityReports = new ArrayList<ActivityReport>();
//	private static int idCounter = 0;
//	static {
//		Calendar calendar = Calendar.getInstance();  
//		activityReports.add(new ActivityReport(++idCounter, "slashexec@gmail.com", calendar.getTime(), false));
//		
//		calendar.add(Calendar.MONTH, 1);
//		activityReports.add(new ActivityReport(++idCounter, "slashexec@gmail.com", calendar.getTime(), false));
//		
//		calendar.add(Calendar.MONTH, 1);
//		activityReports.add(new ActivityReport(++idCounter, "slashexec@gmail.com", calendar.getTime(), false));
//		
////		calendar.add(Calendar.MONTH, 1);
////		activityReports.add(new ActivityReport(++idCounter, "slashexec@gmail.com", calendar.getTime(), false));
//	}
	
	public ActivityReport save(ActivityReport activityReport) {
		if (StringUtils.isBlank(activityReport.getId()) ) {//NEW activity Report
			return activityReportRepository.save(activityReport);
		} else {//Update
			ActivityReport activityReportFound = findById(activityReport.getId());
			activityReportFound.setPeriodDays(activityReport.getPeriodDays());
			activityReportFound.setBilled(activityReport.isBilled());
			activityReportFound.setPeriodTotalTime(activityReport.getPeriodTotalTime());
			activityReportFound.setPeriodTotalTeleWorkTime(activityReport.getPeriodTotalTeleWorkTime());
			activityReportFound.setPeriodTotalBusinessTime(activityReport.getPeriodTotalBusinessTime());
			
			activityReportFound.setPrestation(activityReport.getPrestation());
			activityReportFound.setUnitPrice(activityReport.getUnitPrice());
			activityReportFound.setTotalHT(activityReport.getTotalHT());
			activityReportFound.setTotalTVA(activityReport.getTotalTVA());
			activityReportFound.setTotalTTC(activityReport.getTotalTTC());
			return activityReportRepository.save(activityReport);
		}
		
	}
	
	public ActivityReport bill(ActivityReport activityReport) {
		//Update
		ActivityReport activityReportFound = findById(activityReport.getId());
		if (activityReportFound == null) {
			return null;
		}
		activityReportFound.setPrestation(activityReport.getPrestation());
		activityReportFound.setUnitPrice(activityReport.getUnitPrice());
		activityReportFound.setTotalHT(activityReport.getTotalHT());
		activityReportFound.setTotalTVA(activityReport.getTotalTVA());
		activityReportFound.setTotalTTC(activityReport.getTotalTTC());
		activityReportFound.setBilled(true);
		
		return activityReportRepository.save(activityReportFound);

	}
	
	public List<ActivityReport> findAll() {
		return activityReportRepository.findAll();
	}
	
	public void deleteById(String id) {
		activityReportRepository.deleteById(id);
	}

	public ActivityReport findById(String id) {
		return activityReportRepository.findById(id).orElse(null);
	}
	
	public Path billActivityReport(ActivityReport activityReport) throws FileNotFoundException, IOException {
		
		return activityReportBilling.generateFacture(activityReport);
	}
	
 
	
}
