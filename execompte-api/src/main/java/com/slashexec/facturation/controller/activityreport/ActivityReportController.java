package com.slashexec.facturation.controller.activityreport;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.slashexec.facturation.model.ActivityReport;

@RestController
//@CrossOrigin(origins = "https://4200-c4fb21c2-9271-4845-8037-38cf0ae44765.ws-eu01.gitpod.io")
@CrossOrigin(origins = "${client.url}")
public class ActivityReportController {

	@Autowired
	private  ActivityReportService activityReportService;
	
	
	@GetMapping("/users/{username}/activityreports")
	public List<ActivityReport> getAllActivityReport(@PathVariable String username) {
		return activityReportService.findAll();
	}
	
	@GetMapping("/users/{username}/activityreports/{id}")
	public ResponseEntity<ActivityReport> getActivityReport(@PathVariable String username, @PathVariable String id) {
		
		ActivityReport activityReport = activityReportService.findById(id);
		if (activityReport != null) {
			return new ResponseEntity<ActivityReport> (activityReport, HttpStatus.OK);
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@PutMapping("/users/{username}/activityreports/{id}")
	public ResponseEntity<ActivityReport> updateActivityReport(@PathVariable String username, @PathVariable String id, @RequestBody ActivityReport activityReport) {
		
		ActivityReport updatedActivityReport = activityReportService.save(activityReport);
		return new ResponseEntity<ActivityReport> (updatedActivityReport, HttpStatus.OK);
	}
	
	@PostMapping("/users/{username}/activityreports")
	public ResponseEntity<ActivityReport> createActivityReport(@PathVariable String username, @RequestBody ActivityReport activityReport) {
		
		ActivityReport createdActivityReport = activityReportService.save(activityReport);
		//We want to return the URI of the new resource
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(createdActivityReport.getId()).toUri();
		
		return ResponseEntity.created(uri).build();
	}
	
	
	@DeleteMapping("/users/{username}/activityreports/{id}")
	public ResponseEntity<Void> deleteActivityReport(@PathVariable String username, @PathVariable String id) {
		
		activityReportService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
	
	@PutMapping("/users/{username}/activityreports/{id}/billing")
	public ResponseEntity<ActivityReport> billActivityReport(@PathVariable String username, @PathVariable String id, @RequestBody ActivityReport activityReport) throws IOException {
		ActivityReport updatedActivityReport = activityReportService.bill(activityReport);		
		return new ResponseEntity<ActivityReport> (updatedActivityReport, HttpStatus.OK);
	}
	
	@GetMapping("/users/{username}/activityreports/{id}/billing/download")
	public ResponseEntity<byte[]> downloadActivityReport(@PathVariable String username, @PathVariable String id) throws IOException {
		
		ActivityReport activityReport = activityReportService.findById(id);
		Path generatedFacture = activityReportService.billActivityReport(activityReport);
		byte[] factureAsBytes = Files.readAllBytes(generatedFacture);
		
		HttpHeaders respHeaders = new HttpHeaders();
		respHeaders.setContentLength(factureAsBytes.length);
		respHeaders.setContentType(new MediaType("application", "pdf"));
		respHeaders.setCacheControl("must-revalidate, post-check=0, pre-check=0");
		respHeaders.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + generatedFacture.getFileName().toString() );
		
		//CONTENT_DISPOSITION is not exposed by default (CORS)
		respHeaders.add(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
		return new ResponseEntity<byte[]>(factureAsBytes, respHeaders, HttpStatus.OK);
	}
	
}
