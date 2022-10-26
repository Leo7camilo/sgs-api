package com.br.sgs.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.br.sgs.models.Company;

@Repository
public interface CompanyRepository extends JpaRepository<Company, UUID>{

}
