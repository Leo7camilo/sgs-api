package com.br.sgs.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.br.sgs.model.Company;

public interface CompanyRepository extends JpaRepository<Company, UUID>{

}
