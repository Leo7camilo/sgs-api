package com.br.sgs.controllers;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.br.sgs.dtos.ProfileDto;
import com.br.sgs.exception.CompanyNotFound;
import com.br.sgs.exception.OperationNotAllowed;
import com.br.sgs.exception.TerminalNotFound;
import com.br.sgs.models.ProfileModel;
import com.br.sgs.services.CompanyService;
import com.br.sgs.services.ProfileService;
import com.fasterxml.jackson.annotation.JsonView;

import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("${api.version}/permission")
public class PermissionResources {

	@Autowired
	ProfileService profileService;
	
	@Autowired
	CompanyService companyService;
	
	@PutMapping("/{companyId}/grant-permission/{profileId}")
	private ResponseEntity<Object> grantPermission(@PathVariable(value="companyId") UUID companyId,
												   @PathVariable(value="profileId") UUID profileId,
												   @RequestBody @Validated(ProfileDto.ProfileView.GrantPermission.class) 
														@JsonView(ProfileDto.ProfileView.GrantPermission.class) ProfileDto profileDto){
		
		log.debug("PUT grantPermission profileId received {} ", profileId);
		Optional<ProfileModel> profileModel = profileService.findById(profileId);
		
        if(!profileModel.isPresent()){
            log.warn("profileId {} not found ", profileId);
            throw new TerminalNotFound();
        }
		if (!companyService.existsById(companyId)) {
			log.warn("CompanyId {} not found ", companyId);
			throw new CompanyNotFound();
		}
		if (!companyId.equals(profileModel.get().getCompany().getCompanyId())) {
			log.warn("CompanyId is diferent");
			throw new OperationNotAllowed();
		}
		
        var profielModelUpdated = profileService.grantPermission(profileModel.get(), profileDto);
        
        log.debug("PUT grantPermission profileId saved {} ", profielModelUpdated.getProfileId());
        log.info("Profile saved successfully profileId {} ", profielModelUpdated.getProfileId());
        
		return ResponseEntity.status(HttpStatus.OK).body(profielModelUpdated);
	}
	
	@DeleteMapping("/{companyId}/grant-permission/{profileId}/{queueId}")
	private ResponseEntity<Object> removePermission(@PathVariable(value="companyId") UUID companyId,
												   @PathVariable(value="profileId") UUID profileId,
												   @PathVariable(value="queueId") UUID queueId){
		
		log.debug("PUT grantPermission profileId received {} ", profileId);
		Optional<ProfileModel> profileModel = profileService.findById(profileId);
		
        if(!profileModel.isPresent()){
            log.warn("profileId {} not found ", profileId);
            throw new TerminalNotFound();
        }
		if (!companyService.existsById(companyId)) {
			log.warn("CompanyId {} not found ", companyId);
			throw new CompanyNotFound();
		}
		if (!companyId.equals(profileModel.get().getCompany().getCompanyId())) {
			log.warn("CompanyId is diferent");
			throw new OperationNotAllowed();
		}
		
        var profielModelUpdated = profileService.removePermission(profileModel.get(), queueId);
        
        log.debug("PUT removePermission profileId saved {} ", profielModelUpdated.getProfileId());
        log.info("Profile saved successfully profileId {} ", profielModelUpdated.getProfileId());
        
		return ResponseEntity.status(HttpStatus.OK).body(profielModelUpdated);
	}
	
	
}
