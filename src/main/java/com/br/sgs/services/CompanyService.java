package com.br.sgs.services;

import java.util.Optional;
import java.util.UUID;

import javax.validation.Valid;

import com.br.sgs.models.Company;

public interface CompanyService {

	Company save(Company company);

	Optional<Company> findById(UUID id);

	Company update(UUID id, @Valid Company company);

	boolean existsById(UUID idCompany);

}
