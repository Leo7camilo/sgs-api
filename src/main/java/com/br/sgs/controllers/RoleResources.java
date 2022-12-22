package com.br.sgs.controllers;

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

import com.br.sgs.dtos.RoleDto;
import com.br.sgs.models.RoleModel;
import com.br.sgs.services.CompanyService;
import com.br.sgs.services.RoleService;
import com.fasterxml.jackson.annotation.JsonView;

import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("${api.version}/role")
public class RoleResources {
	
	
	@Autowired
	RoleService roleService;
	
	@Autowired
	CompanyService companyService;
	
	@PostMapping("/{idCompany}")
	private ResponseEntity<Object> createRole(@RequestBody @Validated (RoleDto.RoleView.RegistrationPost.class)
														@JsonView(RoleDto.RoleView.RegistrationPost.class) RoleDto roleDto,
														@PathVariable UUID idCompany){
		
		log.debug("POST createRole roleDto received {} ", roleDto.toString());
        if(roleService.existsByDescription(roleDto.getDescription())){
            log.warn("Description {} is Already Taken ", roleDto.getDescription());
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: Description is Already Taken!");
        }
        if (companyService.existsById(idCompany)) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Company not found.");
		}
        
        RoleModel roleModel = roleService.save(roleDto, idCompany);
        
        log.debug("POST createRole roleId saved {} ", roleModel.getRoleId());
        log.info("Role saved successfully roleId {} ", roleModel.getRoleId());
        
		return ResponseEntity.status(HttpStatus.CREATED).body(roleModel);
	}
	
	
	@PutMapping("/{idCompany}/{idRole}")
	private ResponseEntity<Object> alternateStatus(@RequestBody @Validated (RoleDto.RoleView.RoleStatusPut.class)
														@JsonView(RoleDto.RoleView.RoleStatusPut.class) RoleDto roleDto,
														@PathVariable UUID idCompany,
														@PathVariable UUID idRole){
		
		log.debug("POST alternateStatus roleDto received {} ", roleDto.toString());
		Optional<RoleModel> roleModel = roleService.findById(idRole);
		
        if(!roleModel.isPresent()){
            log.warn("idRole {} not found ", roleDto.getDescription());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: idRole not found!");
        }
		if (companyService.existsById(idCompany)) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Company not found.");
		}
		
		if (idCompany != roleModel.get().getCompany().getCompanyId()) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("CompanyId is diferent.");
		}
		
        var roleModelUpdated = roleService.update(roleModel.get(), idCompany);
        
        log.debug("POST alternateStatus roleId saved {} ", roleModelUpdated.getRoleId());
        log.info("Role saved successfully roleId {} ", roleModelUpdated.getRoleId());
        
		return ResponseEntity.status(HttpStatus.CREATED).body(roleModelUpdated);
	}

}
