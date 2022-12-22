package com.br.sgs.controllers;

import java.util.Optional;
import java.util.UUID;

import javax.validation.Valid;

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

import com.br.sgs.dtos.CompanyDto;
import com.br.sgs.exception.DocumentAlredyInUse;
import com.br.sgs.models.CompanyModel;
import com.br.sgs.services.CompanyService;
import com.br.sgs.specifications.SpecificationTemplate;
import com.fasterxml.jackson.annotation.JsonView;

import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("${api.version}/company")
public class CompanyResources {
	
	
	@Autowired
	CompanyService companyService;
	
	
	@PostMapping
	private ResponseEntity<Object> createUser(@RequestBody @Validated (CompanyDto.CompanyView.RegistrationPost.class)
												@JsonView(CompanyDto.CompanyView.RegistrationPost.class) CompanyDto companyDto){
		log.info("POST createCompany Company received {} ", companyDto.toString());
		
		if(companyService.existsByDocument(companyDto.getDocument())){
            log.warn("Document {} is Already Taken ", companyDto.getDocument());
            throw new DocumentAlredyInUse();
        }
		
		return new ResponseEntity<Object>(companyService.save(companyDto), HttpStatus.CREATED);
	}
	
	
	
	@PutMapping("/{companyId}")
	private ResponseEntity<CompanyModel> putCompany(@PathVariable UUID companyId, @RequestBody @Valid CompanyModel company){
		log.info("PUT putCompany Company received {} ", company.toString());
		return new ResponseEntity<CompanyModel>(companyService.update(companyId, company), HttpStatus.OK);
	}
	
	@GetMapping("/{companyId}")
	private ResponseEntity<CompanyModel> getCompany(@PathVariable UUID companyId){
		Optional<CompanyModel> company = companyService.findById(companyId);
		return company.isPresent() ? ResponseEntity.ok(company.get()) : ResponseEntity.notFound().build();
		
	}
	
	@GetMapping
    public ResponseEntity<Page<CompanyModel>> getAllCompany(SpecificationTemplate.CompanySpec spec,
    								@PageableDefault(page = 0, size = 10, sort = "companyId", direction = Sort.Direction.ASC) Pageable pageable){
		    
        return ResponseEntity.status(HttpStatus.OK).body(companyService.getAllCompany(spec, pageable));
    }
	

}
