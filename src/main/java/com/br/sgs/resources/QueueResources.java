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

import com.br.sgs.dtos.QueueDto;
import com.br.sgs.models.QueueModel;
import com.br.sgs.services.CompanyService;
import com.br.sgs.services.QueueService;
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
	
	@PostMapping("{idCompany}")
	private ResponseEntity<Object> createQueue(@RequestBody @Validated (QueueDto.QueueView.RegistrationPost.class)
														@JsonView(QueueDto.QueueView.RegistrationPost.class) QueueDto queueDto,
														@PathVariable UUID idCompany){
		
		log.debug("POST createQueue queueDto received {} ", queueDto.toString());
        if(queueService.existsByDescription(queueDto.getDescription())){
            log.warn("Description {} is Already Taken ", queueDto.getDescription());
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: Description is Already Taken!");
        }
        
        if (companyService.existsById(idCompany)) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Company not found.");
        }
        
        QueueModel queueModel = queueService.save(queueDto, idCompany);
        
        log.debug("POST createQueue queueId saved {} ", queueModel.getQueueId());
        log.info("Role saved successfully queueId {} ",  queueModel.getQueueId());
        
		return ResponseEntity.status(HttpStatus.CREATED).body(queueModel);
	}
	
	@PutMapping("{idCompany}/status/{idQueue}")
	private ResponseEntity<Object> alternateStatus(@RequestBody @Validated (QueueDto.QueueView.QueueStatusPut.class)
														@JsonView(QueueDto.QueueView.QueueStatusPut.class) QueueDto queueDto,
														@PathVariable UUID idCompany,
														@PathVariable UUID idQueue){
		
		log.debug("POST alternateStatus queueDto received {} ", queueDto.toString());
		Optional<QueueModel> queueModel = queueService.findById(idQueue);
		
        if(!queueModel.isPresent()){
            log.warn("idQueue {} not found ", queueDto.getDescription());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: idQueue not found!");
        }
		if (companyService.existsById(idCompany)) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Company not found.");
		}
		
		if (idCompany != queueModel.get().getIdCompany()) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("CompanyId is diferent.");
		}
		
        var roleModelUpdated = queueService.updateStatus(queueModel.get(), idCompany);
        
        log.debug("POST alternateStatus idQueue saved {} ", roleModelUpdated.getQueueId());
        log.info("Role saved successfully idQueue {} ", roleModelUpdated.getQueueId());
        
		return ResponseEntity.status(HttpStatus.CREATED).body(roleModelUpdated);
	}
	
	@PutMapping("{idCompany}/{idQueue}")
	private ResponseEntity<Object> alterate(@RequestBody @Validated (QueueDto.QueueView.QueuePut.class)
														@JsonView(QueueDto.QueueView.QueuePut.class) QueueDto queueDto,
														@PathVariable UUID idCompany,
														@PathVariable UUID idQueue){
		
		log.debug("PUT alterate queueDto received {} ", queueDto.toString());
		Optional<QueueModel> queueModel = queueService.findById(idQueue);
		
        if(!queueModel.isPresent()){
            log.warn("idQueue {} not found ", queueDto.getDescription());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: idQueue not found!");
        }
		if (companyService.existsById(idCompany)) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Company not found.");
		}
		
		if (idCompany != queueModel.get().getIdCompany()) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("CompanyId is diferent.");
		}
		
        var roleModelUpdated = queueService.update(queueDto, idCompany, queueModel.get().getQueueId());
        
        log.debug("PUT alterate idQueue saved {} ", roleModelUpdated.getQueueId());
        log.info("Role alterated successfully idQueue {} ", roleModelUpdated.getQueueId());
        
		return ResponseEntity.status(HttpStatus.CREATED).body(roleModelUpdated);
	}
	
}
