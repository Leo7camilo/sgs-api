package com.br.sgs.controllers;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.br.sgs.dtos.AttendenceDto;
import com.br.sgs.models.AttendenceModel;
import com.br.sgs.models.ClientModel;
import com.br.sgs.models.CompanyModel;
import com.br.sgs.models.TerminalModel;
import com.br.sgs.services.AttendenceService;
import com.br.sgs.specifications.SpecificationTemplate;
import com.br.sgs.utils.BusinessValidation;
import com.fasterxml.jackson.annotation.JsonView;

import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("${api.version}/attendence")
public class AttendenceResources {
	
	
	/**
	 * Cria regra para chamada do client
	 */
	
	@Autowired
	BusinessValidation businessValidation;
	
	@Autowired
	AttendenceService attendenceService;

	@PostMapping("/{companyId}/client/{clientId}")
	private ResponseEntity<Object> createAttendence(@PathVariable UUID companyId, @PathVariable UUID clientId,
													@RequestBody @Validated (AttendenceDto.AttendenceView.RegistrationPost.class)
													@JsonView(AttendenceDto.AttendenceView.RegistrationPost.class) AttendenceDto attendenceDto) {

		log.info("POST createAttendence received {} ", attendenceDto.toString());
	
		ClientModel client = businessValidation.validateClientOptional(clientId);	
		CompanyModel company = businessValidation.validateCompanyOptional(companyId);

		attendenceService.save(attendenceDto, company, client);

		return new ResponseEntity<Object>(HttpStatus.CREATED);
	}

	@PutMapping("/{companyId}/calls/{queueId}/by-terminal/{terminalId}")
	private ResponseEntity<Object> callsClient(@PathVariable UUID companyId, @PathVariable UUID queueId, @PathVariable UUID terminalId,
												@RequestBody @Validated (AttendenceDto.AttendenceView.Calls.class)
												@JsonView(AttendenceDto.AttendenceView.Calls.class) AttendenceDto attendenceDto) {

		log.info("PUT callsClient received {} ", attendenceDto.toString());
		TerminalModel terminal = businessValidation.validateTerminalOptional(companyId, terminalId);
		businessValidation.validateQueueAndCompany(companyId, queueId);
		attendenceService.updateStatus(attendenceDto, companyId, queueId, terminal);

		return new ResponseEntity<Object>(HttpStatus.OK);
	}
	
	@PutMapping("/{companyId}/calls/by-terminal/{terminalId}/{attendenceId}")
	private ResponseEntity<Object> callsClientWithoutQueue(@PathVariable UUID companyId, @PathVariable UUID terminalId, @PathVariable UUID attendenceId) {

		log.info("PUT callsClientWithoutQueue received by terminalId{} ", terminalId.toString());
		TerminalModel terminal = businessValidation.validateTerminalOptional(companyId, terminalId);
		AttendenceModel attendence = businessValidation.validateAttendeceOptional(companyId, attendenceId);
		
		businessValidation.validateCompany(companyId);
		attendenceService.updateStatus(companyId, terminal, attendence);

		return new ResponseEntity<Object>(HttpStatus.OK);
	}

	
	@DeleteMapping("/{companyId}/calls/{queueId}/{clientId}")
	private ResponseEntity<Object> deleteClient(@PathVariable UUID companyId, @PathVariable UUID queueId, @PathVariable UUID clientId) {

		log.info("DELETE deleteClient received {} | {} | {}", companyId, queueId, clientId);
		businessValidation.validateQueueAndCompany(companyId, queueId);

		attendenceService.updateStatus(companyId, queueId, clientId);
		
		return new ResponseEntity<Object>(HttpStatus.NO_CONTENT);
	}
	
	
	@GetMapping("/{companyId}/get-next-password")
	public ResponseEntity<AttendenceDto> getNextPassword(@PathVariable UUID companyId) {
		
		log.info("GET getNextPassword received {} ", companyId);
		
		CompanyModel company = businessValidation.validateCompanyOptional(companyId);

		AttendenceDto attendenceDto = new AttendenceDto();
		Integer maxPassword = attendenceService.getNextPassword(companyId);
		
		if(maxPassword != null) {
			attendenceDto.setPassaword(maxPassword+1);
		}else {
			attendenceDto.setPassaword(1);
		}
		
		attendenceService.save(attendenceDto, company);
		return ResponseEntity.status(HttpStatus.OK).body(attendenceDto);
	}	
	
	
	@GetMapping("/{companyId}")
	public ResponseEntity<Page<AttendenceModel>> getAllAttendence(@PathVariable UUID companyId, SpecificationTemplate.AttendenceSpec spec,
			@PageableDefault(page = 0, size = 10, sort = "attendenceId", direction = Sort.Direction.ASC) Pageable pageable) {
		
		log.info("GET getAllAttendence received {} ", companyId);
		
		businessValidation.validateCompany(companyId);
		return ResponseEntity.status(HttpStatus.OK).body(attendenceService.getAllAttendence(SpecificationTemplate.attendenceCompanyId(companyId).and(spec), pageable));
	}


}
