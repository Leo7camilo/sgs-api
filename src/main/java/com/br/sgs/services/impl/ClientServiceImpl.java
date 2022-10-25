package com.br.sgs.services.impl;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.br.sgs.dtos.ClientDto;
import com.br.sgs.models.ClientModel;
import com.br.sgs.repository.ClientRepository;
import com.br.sgs.services.ClientService;


@Service
public class ClientServiceImpl implements ClientService{
	
	
	@Autowired
	ClientRepository clientRepository;

	@Override
	public boolean existsByDocument(String document) {
		return clientRepository.existsByDocument(document);
	}

	@Override
	public ClientModel save(ClientDto clientDto, UUID idCompany) {		
		
		var clientModel = new ClientModel();
		BeanUtils.copyProperties(clientDto, clientModel);
		clientModel.setDtCreated(LocalDateTime.now(ZoneId.of("UTC")));
		clientModel.setIdCompany(idCompany);		
		return clientRepository.save(clientModel);
	}

	@Override
	public Optional<ClientModel> findById(UUID idClient) {
		return clientRepository.findById(idClient);
	}

	@Override
	public ClientModel update(ClientDto clientDto, UUID idCompany, UUID idClient) {
		var clientModel = new ClientModel();
		BeanUtils.copyProperties(clientDto, clientModel);
		clientModel.setIdClient(idClient);
		return clientRepository.save(clientModel);
	}
	
	

}
