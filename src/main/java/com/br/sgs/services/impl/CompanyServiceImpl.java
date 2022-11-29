package com.br.sgs.services.impl;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.br.sgs.dtos.CompanyDto;
import com.br.sgs.models.CompanyModel;
import com.br.sgs.repository.CompanyRepository;
import com.br.sgs.services.CompanyService;
import com.br.sgs.specifications.SpecificationTemplate.CompanySpec;


@Service
public class CompanyServiceImpl implements CompanyService{
	
	@Autowired
	CompanyRepository companyRepository;

	@Override
	public CompanyModel save(CompanyDto companyDto) {
		
		var companyModel = new CompanyModel();
		BeanUtils.copyProperties(companyDto, companyModel);
		
		companyModel.setDtCreated(LocalDateTime.now());
		return companyRepository.save(companyModel);
	}

	@Override
	public Optional<CompanyModel> findById(UUID id) {
		return companyRepository.findById(id);
	}

	@Override
	public CompanyModel update(UUID id, @Valid CompanyModel company) {
		Optional<CompanyModel> companyFounded = companyRepository.findById(id);
		if(!companyFounded.isPresent()) {
			throw new NoSuchElementException();
		}
		
		company.setCompanyId(id);
		return companyRepository.save(company);
	}

	@Override
	public boolean existsById(UUID idCompany) {
		return companyRepository.existsById(idCompany);
	}
	

	@Override
	public Page<CompanyModel> getAllCompany(CompanySpec spec, Pageable pageable) {
		return companyRepository.findAll(spec, pageable);
	}

	@Override
	public boolean existsByDocument(String document) {
		return companyRepository.existsByDocument(document);
	}
	
}
