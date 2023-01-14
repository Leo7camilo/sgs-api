package com.br.sgs.controllers;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.br.sgs.dtos.ClientDto;
import com.br.sgs.exception.CompanyNotFound;
import com.br.sgs.models.ClientModel;
import com.br.sgs.services.ClientService;
import com.br.sgs.services.CompanyService;
import com.br.sgs.specifications.SpecificationTemplate;
import com.fasterxml.jackson.annotation.JsonView;

import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("${api.version}/client")
public class ClientResources {

	@Autowired
	ClientService clientService;

	@Autowired
	CompanyService companyService;

	@PostMapping("/{companyId}")
	private ResponseEntity<Object> createClient(@PathVariable UUID companyId,
			@RequestBody @Validated(ClientDto.ClientView.RegistrationPost.class) @JsonView(ClientDto.ClientView.RegistrationPost.class) ClientDto clientDto) {
		log.debug("POST registerClient clientDto received {} ", clientDto.toString());

		if (clientService.existsByDocument(clientDto.getDocument())) {
			log.warn("Document {} is Already Taken ", clientDto.getDocument());
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: Document is Already Taken!");
		}

		ClientModel clientModel = clientService.save(clientDto, companyId);
		log.info("User saved successfully clientId {} ", clientModel.getClientId());
		return ResponseEntity.status(HttpStatus.CREATED).body(clientModel);
	}

	@PutMapping("/{companyId}/{clientId}")
	public ResponseEntity<Object> updateClient(@PathVariable(value = "companyId") UUID companyId,
			@PathVariable(value = "clientId") UUID clientId,
			@RequestBody @Validated(ClientDto.ClientView.ClientPut.class) @JsonView(ClientDto.ClientView.ClientPut.class) ClientDto clientDto) {
		log.info("PUT updateClient clientDto received {} ", clientDto.toString());

		Optional<ClientModel> clientModelOptional = clientService.findById(clientId);
		if (!clientModelOptional.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Client not found.");
		}

		if (!companyService.existsById(companyId)) {
			log.warn("CompanyId {} not found ", companyId);
			throw new CompanyNotFound();
		}

		var userModel = clientService.update(clientDto, companyId, clientModelOptional.get());
		return ResponseEntity.status(HttpStatus.OK).body(userModel);

	}
	
	@GetMapping("/{idCompany}/{idClient}")
	private ResponseEntity<ClientModel> getCompany(@PathVariable(value = "idCompany") UUID idCompany,
														@PathVariable(value = "idClient") UUID idClient){
		Optional<ClientModel> client = clientService.findByIdAndCompanyId(idClient, idCompany);
		return client.isPresent() ? ResponseEntity.ok(client.get()) : ResponseEntity.notFound().build();
	}
	
	@GetMapping("/by-company/{companyId}")
    public ResponseEntity<Page<ClientModel>> getAllCompany(SpecificationTemplate.ClientSpec spec,
    								@PageableDefault(page = 0, size = 10, sort = "clientId", direction = Sort.Direction.ASC) Pageable pageable,
    								@PathVariable(value = "companyId") UUID companyId){
		    
		if (!companyService.existsById(companyId)) {
			log.warn("CompanyId {} not found ", companyId);
			throw new CompanyNotFound();
		}
		return ResponseEntity.status(HttpStatus.OK).body(clientService.findAllByCompany(SpecificationTemplate.clientCompanyId(companyId).and(spec), pageable));
    }
	
	
	
}
