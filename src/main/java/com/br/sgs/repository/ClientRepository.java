package com.br.sgs.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.br.sgs.models.ClientModel;
import com.br.sgs.specifications.SpecificationTemplate.ClientSpec;

@Repository
public interface ClientRepository extends JpaRepository<ClientModel, UUID>, JpaSpecificationExecutor<ClientModel> {
	
	boolean existsByDocument(String document);

	Page<ClientModel> findAllByCompanyCompanyId(ClientSpec spec, Pageable pageable, UUID companyId);

	Optional<ClientModel> findByIdClientAndCompanyCompanyId(UUID idClient, UUID idCompany);

	Page<ClientModel> findByCompanyCompanyId(UUID idCompany, ClientSpec spec, Pageable pageable);

}
