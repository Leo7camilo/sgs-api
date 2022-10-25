package com.br.sgs.resources;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.br.sgs.dtos.ClientDto;
import com.br.sgs.models.ClientModel;
import com.br.sgs.models.Company;
import com.br.sgs.services.ClientService;
import com.br.sgs.services.CompanyService;
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

	@PostMapping("/{idCompany}")
	private ResponseEntity<Object> createClient(@PathVariable UUID idCompany,
			@RequestBody @Validated(ClientDto.ClientView.RegistrationPost.class) @JsonView(ClientDto.ClientView.RegistrationPost.class) ClientDto clientDto) {
		log.debug("POST registerClient clientDto received {} ", clientDto.toString());

		if (clientService.existsByDocument(clientDto.getDocument())) {
			log.warn("Document {} is Already Taken ", clientDto.getDocument());
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: Document is Already Taken!");
		}

		ClientModel clientModel = clientService.save(clientDto, idCompany);
		log.debug("POST registerClient clientId saved {} ", clientModel.getIdClient());
		log.info("User saved successfully clientId {} ", clientModel.getIdClient());
		return ResponseEntity.status(HttpStatus.CREATED).body(clientModel);
	}

	@PutMapping("{idCompany}/{idClient}")
	public ResponseEntity<Object> updateClient(@PathVariable(value = "idCompany") UUID idCompany,
			@PathVariable(value = "idClient") UUID idClient,
			@RequestBody @Validated(ClientDto.ClientView.ClientPut.class) @JsonView(ClientDto.ClientView.ClientPut.class) ClientDto clientDto) {
		log.debug("PUT updateClient clientDto received {} ", clientDto.toString());

		Optional<ClientModel> clientModelOptional = clientService.findById(idClient);
		if (!clientModelOptional.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Client not found.");
		}

		if (companyService.existsById(idCompany)) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Company not found.");
		}

		var userModel = clientService.update(clientDto, idCompany, idClient);
		return ResponseEntity.status(HttpStatus.OK).body(userModel);

	}

}
