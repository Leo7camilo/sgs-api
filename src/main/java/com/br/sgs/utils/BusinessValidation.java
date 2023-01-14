package com.br.sgs.utils;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.br.sgs.models.AttendenceModel;
import com.br.sgs.models.ClientModel;
import com.br.sgs.models.CompanyModel;
import com.br.sgs.models.TerminalModel;
import com.br.sgs.services.AttendenceService;
import com.br.sgs.services.ClientService;
import com.br.sgs.services.CompanyService;
import com.br.sgs.services.QueueService;
import com.br.sgs.services.TerminalService;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class BusinessValidation {

	@Autowired
	CompanyService companyService;

	@Autowired
	QueueService queueService;
	
	@Autowired
	ClientService clientService;
	
	@Autowired
	TerminalService terminalService;
	
	@Autowired
	AttendenceService attendenceService;

	public void validateQueueAndCompany(UUID companyId, UUID queueId) {
		if (!queueService.existsById(queueId)) {
			log.info("Queue {} Not Found ", queueId);
			throw new NoSuchElementException();
		}

		if (!companyService.existsById(companyId)) {
			log.info("Company {} not found. ", companyId);
			throw new NoSuchElementException();
		}
	}

	public void validateCompany(UUID companyId) {
		if (!companyService.existsById(companyId)) {
			log.info("Company {} not found. ", companyId);
			throw new NoSuchElementException();
		}
	}

	public CompanyModel validateCompanyOptional(UUID companyId) {

		Optional<CompanyModel> company = companyService.findById(companyId);

		if (!company.isPresent()) {
			log.warn("Company {} not found. ", companyId);
			throw new NoSuchElementException();
		}

		return company.get();
	}

	public ClientModel validateClientOptional(UUID clientId) {
		Optional<ClientModel> client = clientService.findById(clientId);
		if(!client.isPresent()) {
			log.warn("Client {} Not Found ", clientId);
			throw new NoSuchElementException();
		}
		return client.get();
	}

	public TerminalModel validateTerminalOptional(UUID companyId, UUID terminalId) {
		
		Optional<TerminalModel> terminal = terminalService.findByIdAndCompanyId(terminalId, companyId);
		if(!terminal.isPresent()) {
			log.warn("Terminal {} Not Found ", terminalId);
			throw new NoSuchElementException();
		}
		
		
		return terminal.get();
	}

	public AttendenceModel validateAttendeceOptional(UUID companyId, UUID attendenceId) {
		Optional<AttendenceModel> attendence = attendenceService.findByIdAndCompanyId(attendenceId, companyId);
		
		if(!attendence.isPresent()) {
			log.warn("Attendence {} Not Found ", attendenceId);
			throw new NoSuchElementException();
		}
		
		return attendence.get();
	}

}
