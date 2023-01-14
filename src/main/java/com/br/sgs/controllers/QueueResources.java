package com.br.sgs.controllers;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.br.sgs.config.security.UserDetailsImpl;
import com.br.sgs.dtos.QueueDto;
import com.br.sgs.exception.CompanyNotFound;
import com.br.sgs.exception.DescriptionAlredyInUse;
import com.br.sgs.exception.OperationNotAllowed;
import com.br.sgs.exception.QueueNotFound;
import com.br.sgs.models.QueueHistModel;
import com.br.sgs.models.QueueModel;
import com.br.sgs.services.CompanyService;
import com.br.sgs.services.QueueService;
import com.br.sgs.specifications.SpecificationTemplate;
import com.fasterxml.jackson.annotation.JsonView;

import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("${api.version}/queue")
public class QueueResources {

	@Autowired
	QueueService queueService;
	
	@Autowired
	CompanyService companyService;

	
	//@PreAuthorize("hasAnyRole('EMPLOYEE')")
	@PostMapping("/{companyId}")
	private ResponseEntity<Object> createQueue(@RequestBody @Validated (QueueDto.QueueView.RegistrationPost.class)
														@JsonView(QueueDto.QueueView.RegistrationPost.class) QueueDto queueDto,
														@PathVariable UUID companyId){
		
		log.info("POST createQueue queueDto received {} ", queueDto.toString());
        if(queueService.existsByDescription(queueDto.getDescription())){
            log.warn("Description {} is Already Taken ", queueDto.getDescription());
            throw new DescriptionAlredyInUse();
        }
        
        if (!companyService.existsById(companyId)) {
			log.warn("CompanyId {} not found ", companyId);
			throw new CompanyNotFound();
        }
        
        QueueModel queueModel = queueService.save(queueDto, companyId);
        
        log.debug("POST createQueue queueId saved {} ", queueModel.getQueueId());
        log.info("Queue saved successfully queueId {} ",  queueModel.getQueueId());
        
		return ResponseEntity.status(HttpStatus.CREATED).body(queueModel);
	}
	
	//@PreAuthorize("hasAnyRole('EMPLOYEE')")
	@PutMapping("/status/{companyId}/{queueId}")
	private ResponseEntity<Object> alternateStatus(@PathVariable UUID companyId, @PathVariable UUID queueId, Authentication authentication){
		
		UserDetails userDetails = (UserDetailsImpl) authentication.getPrincipal();
        log.info("Authentication {}",userDetails.getUsername());
		
		log.debug("POST alternateStatus queueId received {} ", queueId);
		Optional<QueueModel> queueModel = queueService.findById(queueId);
		
        if(!queueModel.isPresent()){
            log.warn("QueueId {} not found ", queueId);
            throw new QueueNotFound();
        }
		if (!companyService.existsById(companyId)) {
			log.warn("CompanyId {} not found ", companyId);
			throw new CompanyNotFound();
		}
		
		if (!companyId.equals(queueModel.get().getCompany().getCompanyId())) {
			log.warn("CompanyId is diferent");
			throw new OperationNotAllowed();
		}
		
        var roleModelUpdated = queueService.updateStatus(queueModel.get(), companyId, userDetails);
        
        log.debug("PUT alternateStatus queueId saved {} ", roleModelUpdated.getQueueId());
        log.info("Role saved successfully queueId {} ", roleModelUpdated.getQueueId());
        
		return ResponseEntity.status(HttpStatus.OK).body(roleModelUpdated);
	}
	
	//@PreAuthorize("hasAnyRole('EMPLOYEE')")
	@PutMapping("/{companyId}/{queueId}")
	private ResponseEntity<Object> update(@RequestBody @Validated (QueueDto.QueueView.QueuePut.class)
														@JsonView(QueueDto.QueueView.QueuePut.class) QueueDto queueDto,
														@PathVariable UUID companyId,
														@PathVariable UUID queueId){
		
		log.info("PUT alterate queueDto received {} ", queueDto.toString());
		Optional<QueueModel> queueModel = queueService.findById(queueId);
		
		if(!queueModel.isPresent()){
            log.warn("QueueId {} not found ", queueDto.getDescription());
            throw new QueueNotFound();
        }
		if (!companyService.existsById(companyId)) {
			log.warn("CompanyId {} not found ", companyId);
			throw new CompanyNotFound();
		}
		
		if (!companyId.equals(queueModel.get().getCompany().getCompanyId())) {
			log.warn("CompanyId is diferent");
			throw new OperationNotAllowed();
		}
		
        var roleModelUpdated = queueService.update(queueDto, companyId, queueModel.get().getQueueId());
        
        log.debug("PUT alterate idQueue saved {} ", roleModelUpdated.getQueueId());
        log.info("Role alterated successfully idQueue {} ", roleModelUpdated.getQueueId());
        
		return ResponseEntity.status(HttpStatus.OK).body(roleModelUpdated);
	}
	
	
	@GetMapping("/{companyId}/{queueId}")
	private ResponseEntity<QueueModel> getTerminalByCompanyIdAndTerminalId(@PathVariable(value="queueId") UUID queueId, 
																			  @PathVariable(value="companyId") UUID companyId){
		
		Optional<QueueModel> queue = queueService.findByIdAndCompanyId(queueId, companyId);
		return queue.isPresent() ? ResponseEntity.ok(queue.get()) : ResponseEntity.notFound().build();
	}
	
	@GetMapping("/{companyId}")
    public ResponseEntity<Page<QueueModel>> getAllByCompany(@PathVariable(value="companyId") UUID companyId,
    								SpecificationTemplate.QueueSpec spec,
    								@PageableDefault(page = 0, size = 10, sort = "queueId", direction = Sort.Direction.ASC) Pageable pageable){
		return ResponseEntity.status(HttpStatus.OK).body(queueService.findAllByCompany(SpecificationTemplate.queueCompanyId(companyId).and(spec), pageable));
    }
	
	@GetMapping("/hist/{companyId}/{queueId}")
    public ResponseEntity<Page<QueueHistModel>> getAllHistByCompany(@PathVariable(value="companyId") UUID companyId,
    								@PathVariable(value="queueId") UUID queueId,
    								SpecificationTemplate.QueueHistSpec spec,
    								@PageableDefault(page = 0, size = 10, sort = "queueHistId", direction = Sort.Direction.ASC) Pageable pageable){	

		return ResponseEntity.status(HttpStatus.OK).body(queueService.findAllHistByQueueIn(SpecificationTemplate.queueHistCompanyId(companyId, queueId).and(spec), pageable));
    }
	
}
