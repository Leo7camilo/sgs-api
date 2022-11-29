package com.br.sgs.services;

import java.util.Optional;
import java.util.UUID;

import com.br.sgs.dtos.ClientDto;
import com.br.sgs.models.ClientModel;

public interface ClientService {

	boolean existsByDocument(String document);

	ClientModel save(ClientDto clientDto, UUID idCompany);

	Optional<ClientModel> findById(UUID idClient);

	ClientModel update(ClientDto clientDto, UUID idCompany, UUID idClient);

	boolean existsById(UUID idClient);

}
