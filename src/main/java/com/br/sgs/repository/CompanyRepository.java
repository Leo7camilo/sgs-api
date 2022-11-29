package com.br.sgs.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.br.sgs.models.CompanyModel;

@Repository
public interface CompanyRepository extends JpaRepository<CompanyModel, UUID>, JpaSpecificationExecutor<CompanyModel> {

	boolean existsByDocument(String document);

}
