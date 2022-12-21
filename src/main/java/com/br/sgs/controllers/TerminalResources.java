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

import com.br.sgs.dtos.TerminalDto;
import com.br.sgs.exception.CompanyNotFound;
import com.br.sgs.exception.NameAlredyInUse;
import com.br.sgs.exception.OperationNotAllowed;
import com.br.sgs.exception.TerminalNotFound;
import com.br.sgs.models.TerminalModel;
import com.br.sgs.services.CompanyService;
import com.br.sgs.services.TerminalService;
import com.br.sgs.specifications.SpecificationTemplate;
import com.fasterxml.jackson.annotation.JsonView;

import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("${api.version}/terminal")
public class TerminalResources {

	@Autowired
	TerminalService terminalService;
	
	@Autowired
	CompanyService companyService;

	@PostMapping("/{companyId}")
	private ResponseEntity<Object> createTerminal(@PathVariable UUID companyId, 
			@RequestBody @Validated(TerminalDto.TerminalView.RegistrationPost.class) @JsonView(TerminalDto.TerminalView.RegistrationPost.class) TerminalDto terminalDto){
		
		log.info("POST createTerminal received {} ", terminalDto.toString());
		if(terminalService.existsByName(terminalDto.getName())){
            log.warn("Name {} is Already Taken ", terminalDto.getName());
            throw new NameAlredyInUse();
        }
		
		TerminalModel terminalModel = terminalService.save(companyId,terminalDto);
		return new ResponseEntity<Object>(terminalModel, HttpStatus.CREATED);
	}
	
	@PutMapping("/status/{companyId}/{terminalId}")
	private ResponseEntity<Object> alternateStatus(@PathVariable(value="companyId") UUID companyId,
												   @PathVariable(value="terminalId") UUID terminalId){
		
		log.debug("POST alternateStatus id received {} ", terminalId);
		Optional<TerminalModel> terminalModel = terminalService.findById(terminalId);
		
        if(!terminalModel.isPresent()){
            log.warn("terminalId {} not found ", terminalModel.get().getName());
            throw new TerminalNotFound();
        }
		if (!companyService.existsById(companyId)) {
			log.warn("CompanyId {} not found ", companyId);
			throw new CompanyNotFound();
		}
		if (!companyId.equals(terminalModel.get().getCompany().getCompanyId())) {
			log.warn("CompanyId is diferent");
			throw new OperationNotAllowed();
		}
		
        var terminalModelUpdated = terminalService.updateStatus(terminalModel.get());
        
        log.debug("POST alternateStatus terminalId saved {} ", terminalModelUpdated.getTerminalId());
        log.info("Terminal saved successfully terminalId {} ", terminalModelUpdated.getTerminalId());
        
		return ResponseEntity.status(HttpStatus.OK).body(terminalModelUpdated);
	}

	@GetMapping("/{companyId}/{terminalId}")
	private ResponseEntity<TerminalModel> getTerminalByCompanyIdAndTerminalId(@PathVariable(value="terminalId") UUID terminalId, 
																			  @PathVariable(value="companyId") UUID companyId){
		Optional<TerminalModel> terminal = terminalService.findByIdAndCompanyId(terminalId, companyId);
		return terminal.isPresent() ? ResponseEntity.ok(terminal.get()) : ResponseEntity.notFound().build();
	}
	
	@GetMapping("/{companyId}")
    public ResponseEntity<Page<TerminalModel>> getAllCompany(@PathVariable(value="companyId") UUID companyId,
    								SpecificationTemplate.TerminalSpec spec,
    								@PageableDefault(page = 0, size = 10, sort = "terminalId", direction = Sort.Direction.ASC) Pageable pageable){
		return ResponseEntity.status(HttpStatus.OK).body(terminalService.findAllByCompany(SpecificationTemplate.terminalCompanyId(companyId).and(spec), pageable));
    }
	
}
