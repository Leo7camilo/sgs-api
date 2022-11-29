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
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.br.sgs.dtos.TerminalDto;
import com.br.sgs.exception.DocumentAlredyInUse;
import com.br.sgs.models.TerminalModel;
import com.br.sgs.services.CompanyService;
import com.br.sgs.services.TerminalService;
import com.br.sgs.specifications.SpecificationTemplate;

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

	@PostMapping
	private ResponseEntity<Object> createTerminal(@PathVariable UUID uuidCompany, @Valid TerminalDto terminalDto){
		
		log.info("POST createTerminal received {} ", terminalDto.toString());
		if(terminalService.existsByName(terminalDto.getName())){
            log.warn("Name {} is Already Taken ", terminalDto.getName());
            throw new DocumentAlredyInUse();
        }
		
		TerminalModel terminalModel = terminalService.save(uuidCompany,terminalDto);
		return new ResponseEntity<Object>(terminalModel, HttpStatus.CREATED);
	}
	
	@PutMapping("/status/{idCompany}/{idTerminal}")
	private ResponseEntity<Object> alternateStatus(@PathVariable UUID idCompany, @PathVariable UUID idTerminal){
		
		log.debug("POST alternateStatus id received {} ", idTerminal);
		Optional<TerminalModel> terminalModel = terminalService.findById(idTerminal);
		
        if(!terminalModel.isPresent()){
            log.warn("idTerminal {} not found ", terminalModel.get().getName());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: idRole not found!");
        }
		if (companyService.existsById(idCompany)) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Company not found.");
		}
		
		if (idCompany != terminalModel.get().getCompany().getCompanyId()) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("CompanyId is diferent.");
		}
		
        var terminalModelUpdated = terminalService.updateStatus(terminalModel.get());
        
        log.debug("POST alternateStatus roleId saved {} ", terminalModelUpdated.getTerminalId());
        log.info("Role saved successfully roleId {} ", terminalModelUpdated.getTerminalId());
        
		return ResponseEntity.status(HttpStatus.CREATED).body(terminalModelUpdated);
	}

	@GetMapping("/{idTerminal}")
	private ResponseEntity<TerminalModel> getCompany(@PathVariable UUID idTerminal){
		Optional<TerminalModel> terminal = terminalService.findById(idTerminal);
		return terminal.isPresent() ? ResponseEntity.ok(terminal.get()) : ResponseEntity.notFound().build();
	}
	
	@GetMapping
    public ResponseEntity<Page<TerminalModel>> getAllCompany(SpecificationTemplate.TerminalSpec spec,
    								@PageableDefault(page = 0, size = 10, sort = "comapanyId", direction = Sort.Direction.ASC) Pageable pageable){
		    
        return ResponseEntity.status(HttpStatus.OK).body(terminalService.getAllTerminal(spec, pageable));  
    }
	
}
