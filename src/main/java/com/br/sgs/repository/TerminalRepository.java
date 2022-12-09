package com.br.sgs.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.br.sgs.models.TerminalModel;

@Repository
public interface TerminalRepository extends JpaRepository<TerminalModel, UUID>, JpaSpecificationExecutor<TerminalModel> {
	boolean existsByName(String name);
	
	Optional<TerminalModel> findByNameAndCompanyCompanyId(String name, UUID companyId);
}
