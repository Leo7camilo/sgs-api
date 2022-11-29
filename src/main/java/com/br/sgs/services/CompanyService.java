package com.br.sgs.services;

import java.util.Optional;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.br.sgs.dtos.CompanyDto;
import com.br.sgs.models.CompanyModel;
import com.br.sgs.specifications.SpecificationTemplate.CompanySpec;

public interface CompanyService {

	CompanyModel save(CompanyDto companyDto);

	Optional<CompanyModel> findById(UUID id);

	CompanyModel update(UUID id, @Valid CompanyModel company);

	boolean existsById(UUID idCompany);

	Page<CompanyModel> getAllCompany(CompanySpec spec, Pageable pageable);

	boolean existsByDocument(String document);

}
