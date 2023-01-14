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

import com.br.sgs.dtos.ProfileDto;
import com.br.sgs.exception.CompanyNotFound;
import com.br.sgs.exception.NameAlredyInUse;
import com.br.sgs.exception.OperationNotAllowed;
import com.br.sgs.exception.TerminalNotFound;
import com.br.sgs.models.ProfileModel;
import com.br.sgs.services.CompanyService;
import com.br.sgs.services.ProfileService;
import com.br.sgs.specifications.SpecificationTemplate;
import com.fasterxml.jackson.annotation.JsonView;

import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("${api.version}/profile")
public class ProfileResources {

	@Autowired
	ProfileService profileService;
	
	@Autowired
	CompanyService companyService;

	@PostMapping("/{companyId}")
	private ResponseEntity<Object> createProfile(@PathVariable UUID companyId, 
			@RequestBody @Validated(ProfileDto.ProfileView.RegistrationPost.class) @JsonView(ProfileDto.ProfileView.RegistrationPost.class) ProfileDto profileDto){
		
		log.info("POST createProfile received {} ", profileDto.toString());
		if(profileService.existsByDescription(profileDto.getDescription())){
            log.warn("Name {} is Already Taken ", profileDto.getDescription());
            throw new NameAlredyInUse();
        }
		
		ProfileModel profileModel = profileService.save(companyId, profileDto);
		return new ResponseEntity<Object>(profileModel, HttpStatus.CREATED);
	}
	
	@PutMapping("/status/{companyId}/{profileId}")
	private ResponseEntity<Object> alternateStatus(@PathVariable(value="companyId") UUID companyId,
												   @PathVariable(value="profileId") UUID profileId){
		
		log.debug("PUT alternateStatus id received {} ", profileId);
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
		
        var profielModelUpdated = profileService.updateStatus(profileModel.get());
        
        log.debug("PUT alternateStatus profileId saved {} ", profielModelUpdated.getProfileId());
        log.info("Profile saved successfully terminalId {} ", profielModelUpdated.getProfileId());
        
		return ResponseEntity.status(HttpStatus.OK).body(profielModelUpdated);
	}

	@GetMapping("/{companyId}/{profileId}")
	private ResponseEntity<ProfileModel> getProfileByCompanyIdAndProfileId(@PathVariable(value="profileId") UUID profileId, 
																			  @PathVariable(value="companyId") UUID companyId){
		Optional<ProfileModel> profile = profileService.findByIdAndCompanyId(profileId, companyId);
		return profile.isPresent() ? ResponseEntity.ok(profile.get()) : ResponseEntity.notFound().build();
	}
	
	@GetMapping("/{companyId}")
    public ResponseEntity<Page<ProfileModel>> getAllCompany(@PathVariable(value="companyId") UUID companyId,
    								SpecificationTemplate.ProfileSpec spec,
    								@PageableDefault(page = 0, size = 10, sort = "profileId", direction = Sort.Direction.ASC) Pageable pageable){
		return ResponseEntity.status(HttpStatus.OK).body(profileService.findAllByCompany(SpecificationTemplate.profileCompanyId(companyId).and(spec), pageable));
    }
	
}
