package com.br.sgs.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.br.sgs.services.AttendenceHistService;
import com.br.sgs.services.AttendenceService;
import com.br.sgs.utils.BusinessValidation;

import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("${api.version}/analytics")
public class AnalyticsResources {
	
	@Autowired
	BusinessValidation businessValidation;
	
	@Autowired
	AttendenceService attendenceService;
	
	@Autowired
	AttendenceHistService attendenceHistService;

	@GetMapping("/{companyId}/count-by-company")
	public ResponseEntity<Object> countAttendenceByCompany(@PathVariable UUID companyId) {
		log.info("GET countAttendenceByCompany received {} | {}", companyId);
		
		businessValidation.validateCompany(companyId);
		return ResponseEntity.status(HttpStatus.OK).body(attendenceService.countAttendenceByCompany(companyId));
	}
	
	@GetMapping("/{companyId}/count-by-company-and-atual-date")
	public ResponseEntity<Object> countAttendenceByCompanyAndDate(@PathVariable UUID companyId) {
		log.info("GET countAttendenceByCompanyAndDate received {} | {}", companyId);
		
		businessValidation.validateCompany(companyId);
		return ResponseEntity.status(HttpStatus.OK).body(attendenceService.countAttendenceByCompanyAndDate(companyId));
	}
	
	@GetMapping("/{companyId}/summarized-data")
	public ResponseEntity<Object> summarizedData(@PathVariable UUID companyId) {
		log.info("GET summarizedData received {} | {}", companyId);
		
		List<Object> responseBody = new ArrayList<>();
		
		businessValidation.validateCompany(companyId);
		responseBody.add(attendenceService.findMostFrequentAttendenceQueue(companyId));
		responseBody.add(attendenceService.countAttendenceByCompanyAndDate(companyId));
		
		return ResponseEntity.status(HttpStatus.OK).body(responseBody);
	}
	
	
	@GetMapping("/{companyId}/group-attendence-by-date")
	public ResponseEntity<Object> groupAttendenceByDate(@PathVariable UUID companyId) {
		log.info("GET groupAttendenceByDate received {}", companyId);
		
		businessValidation.validateCompany(companyId);
		return ResponseEntity.status(HttpStatus.OK).body(attendenceService.groupAttendenceByCompanyAndDate(companyId));
	}
	
	@GetMapping("/{companyId}/most-company-client")
	public ResponseEntity<Object> mostCompanyClient(@PathVariable UUID companyId) {
		log.info("GET mostCompanyClient received {}", companyId);
		
		businessValidation.validateCompany(companyId);
		return ResponseEntity.status(HttpStatus.OK).body(attendenceHistService.mostCompanyClientByCompanyId(companyId));
	}
	
	

}
