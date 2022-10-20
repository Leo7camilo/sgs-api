package com.br.sgs.resources;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.br.sgs.models.Company;
import com.br.sgs.services.CompanyService;

@RestController
@RequestMapping("${api.version}/company")
public class CompanyResources {
	
	
	@Autowired
	CompanyService companyService;
	
	@PostMapping
	private ResponseEntity<Company> createCompany(@Valid Company company){
		return new ResponseEntity<Company>(companyService.save(company), HttpStatus.CREATED);
	}
	
	@GetMapping("/{id}")
	private ResponseEntity<Company> getCompany(@PathVariable UUID id){
		Optional<Company> company = companyService.findById(id);
		return company.isPresent() ? ResponseEntity.ok(company.get()) : ResponseEntity.notFound().build();
		
	}
	
	@PutMapping("/{id}")
	private ResponseEntity<Company> putCompany(@PathVariable UUID id, @RequestBody @Valid Company company){
		return new ResponseEntity<Company>(companyService.update(id, company), HttpStatus.OK);
	}

}
