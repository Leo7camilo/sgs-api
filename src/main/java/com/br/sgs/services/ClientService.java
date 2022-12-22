package com.br.sgs.services;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.br.sgs.dtos.ClientDto;
import com.br.sgs.models.ClientModel;
import com.br.sgs.specifications.SpecificationTemplate.ClientSpec;

public interface ClientService {

	boolean existsByDocument(String document);

	ClientModel save(ClientDto clientDto, UUID idCompany);

	Optional<ClientModel> findById(UUID idClient);

	ClientModel update(ClientDto clientDto, UUID idCompany, ClientModel clientModel);

	boolean existsById(UUID idClient);

	Page<ClientModel> getAllClients(ClientSpec spec, Pageable pageable, UUID idCompany);

	Optional<ClientModel> findByIdAndCompanyId(UUID idClient, UUID idCompany);

	Page<ClientModel> findAllByCompany(Specification<ClientModel> spec, Pageable pageable);

}
