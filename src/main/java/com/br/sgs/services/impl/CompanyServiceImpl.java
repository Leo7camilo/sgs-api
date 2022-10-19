package com.br.sgs.services.impl;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;

import com.br.sgs.model.Company;
import com.br.sgs.repository.CompanyRepository;
import com.br.sgs.services.CompanyService;

public class CompanyServiceImpl implements CompanyService{
	
	@Autowired
	CompanyRepository companyRepository;

	@Override
	public Company save(Company company) {
		company.setDtCreated(LocalDateTime.now());
		return companyRepository.save(company);
	}

	@Override
	public Optional<Company> findById(UUID id) {
		return companyRepository.findById(id);
	}

	@Override
	public Company update(UUID id, @Valid Company company) {
		Optional<Company> companyFounded = companyRepository.findById(id);
		if(!companyFounded.isPresent()) {
			throw new NoSuchElementException();
		}
		
		company.setUuid(id);
		return companyRepository.save(company);
	}

	
	
	
	
}
