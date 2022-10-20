package com.br.sgs.services.impl;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;

import com.br.sgs.models.Company;
import com.br.sgs.models.Terminal;
import com.br.sgs.repository.CompanyRepository;
import com.br.sgs.repository.TerminalRepository;
import com.br.sgs.services.TerminalService;

public class TerminalServiceImpl implements TerminalService{
	
	@Autowired
	TerminalRepository terminalRepository;
	
	@Autowired
	CompanyRepository companyRepository;
	

	@Override
	public Terminal save(UUID uuidCompany, Terminal terminal) {
		
		Optional<Company> company = companyRepository.findById(uuidCompany);
		if(!company.isPresent()) {
			throw new NoSuchElementException();
		}
		
		terminal.setUuidCompany(uuidCompany);
		return terminalRepository.save(terminal);
	}

	@Override
	public Optional<Terminal> findById(UUID id) {
		return terminalRepository.findById(id);
	}

	@Override
	public Terminal update(UUID id, Terminal terminal) {
		terminal.setUuid(id);
		return terminalRepository.save(terminal);
	}

	


	
	
	
	
}
