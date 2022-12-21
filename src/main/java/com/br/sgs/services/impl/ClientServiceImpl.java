package com.br.sgs.services.impl;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.br.sgs.dtos.ClientDto;
import com.br.sgs.models.ClientModel;
import com.br.sgs.models.CompanyModel;
import com.br.sgs.repository.ClientRepository;
import com.br.sgs.repository.CompanyRepository;
import com.br.sgs.services.ClientService;
import com.br.sgs.specifications.SpecificationTemplate.ClientSpec;


@Service
public class ClientServiceImpl implements ClientService{
	
	
	@Autowired
	ClientRepository clientRepository;
	
	@Autowired
	CompanyRepository companyRepository;

	@Override
	public boolean existsByDocument(String document) {
		return clientRepository.existsByDocument(document);
	}

	@Override
	public ClientModel save(ClientDto clientDto, UUID idCompany) {		
		
		var clientModel = new ClientModel();
		BeanUtils.copyProperties(clientDto, clientModel);
		clientModel.setDtCreated(LocalDateTime.now(ZoneId.of("UTC")));
		
		Optional<CompanyModel> companyModel = companyRepository.findById(idCompany);
		clientModel.setCompany(companyModel.get());
		
		return clientRepository.save(clientModel);
	}

	@Override
	public Optional<ClientModel> findById(UUID idClient) {
		return clientRepository.findById(idClient);
	}

	
	@Override
	public Optional<ClientModel> findByIdAndCompanyId(UUID idClient, UUID idCompany) {
		return clientRepository.findByIdClientAndCompanyCompanyId(idClient, idCompany);
	}

	@Override
	public ClientModel update(ClientDto clientDto, UUID idCompany, UUID idClient) {
		var clientModel = new ClientModel();
		BeanUtils.copyProperties(clientDto, clientModel);
		clientModel.setIdClient(idClient);
		return clientRepository.save(clientModel);
	}

	@Override
	public boolean existsById(UUID idClient) {
		return clientRepository.existsById(idClient);
	}

	@Override
	public Page<ClientModel> getAllClients(ClientSpec spec, Pageable pageable, UUID idCompany) {
		return clientRepository.findByCompanyCompanyId(idCompany, spec, pageable);
	}

}
