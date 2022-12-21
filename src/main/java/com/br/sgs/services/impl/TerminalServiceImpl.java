package com.br.sgs.services.impl;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.br.sgs.dtos.TerminalDto;
import com.br.sgs.enums.TerminalState;
import com.br.sgs.models.CompanyModel;
import com.br.sgs.models.TerminalModel;
import com.br.sgs.repository.CompanyRepository;
import com.br.sgs.repository.TerminalRepository;
import com.br.sgs.services.TerminalService;
import com.br.sgs.specifications.SpecificationTemplate.TerminalSpec;


@Service
public class TerminalServiceImpl implements TerminalService{
	
	@Autowired
	TerminalRepository terminalRepository;
	
	@Autowired
	CompanyRepository companyRepository;
	

	@Override
	public TerminalModel save(UUID uuidCompany, TerminalDto terminalDto) {
		
		Optional<CompanyModel> company = companyRepository.findById(uuidCompany);
		if(!company.isPresent()) {
			throw new NoSuchElementException();
		}
		
		TerminalModel terminalModel = new TerminalModel();
		BeanUtils.copyProperties(terminalDto, terminalModel);
		terminalModel.setCompany(company.get());
		terminalModel.setDtChange(LocalDateTime.now(ZoneId.of("UTC")));
		terminalModel.setStatus(TerminalState.ACTIVE);
		
		return terminalRepository.save(terminalModel);
	}

	@Override
	public Optional<TerminalModel> findById(UUID id) {
		return terminalRepository.findById(id);
	}

	@Override
	public TerminalModel update(UUID companyId, TerminalDto terminalDto) {
		
		Optional<TerminalModel> terminal = terminalRepository.findByNameAndCompanyCompanyId(terminalDto.getName(), companyId);
		if(!terminal.isPresent()) {
			throw new NoSuchElementException();
		}
		
		Optional<CompanyModel> company = companyRepository.findById(companyId);
		if(!company.isPresent()) {
			throw new NoSuchElementException();
		}
		
		TerminalModel terminalModel = new TerminalModel();
		BeanUtils.copyProperties(terminalDto, terminalModel);
		terminalModel.setCompany(company.get());
		terminalModel.setDtChange(LocalDateTime.now(ZoneId.of("UTC")));
		
		return terminalRepository.save(terminalModel);
	}

	@Override
	public TerminalModel updateStatus(TerminalModel terminal) {
		
		if(terminal.getStatus().equals(TerminalState.ACTIVE))
			terminal.setStatus(TerminalState.DISABLE);
		else
			terminal.setStatus(TerminalState.ACTIVE);
			
		terminal.setDtChange(LocalDateTime.now(ZoneId.of("UTC")));
		return terminalRepository.save(terminal);
	}
	
	@Override
	public boolean existsByName(String name) {
		return terminalRepository.existsByName(name);
	}

	@Override
	public Page<TerminalModel> getAllTerminal(TerminalSpec spec, Pageable pageable) {
		return terminalRepository.findAll(spec, pageable);
	}

	@Override
	public Page<TerminalModel> findAllByCompany(Specification<TerminalModel> spec, Pageable pageable) {
		return terminalRepository.findAll(spec, pageable);
	}

	@Override
	public Optional<TerminalModel> findByIdAndCompanyId(UUID idTerminal, UUID companyId) {
		return terminalRepository.findByTerminalIdAndCompanyCompanyId(idTerminal, companyId);
	}

	
	
	
}
