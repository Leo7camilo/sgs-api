package com.br.sgs.controllers;

import java.util.NoSuchElementException;
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

import com.br.sgs.dtos.RoleDto;
import com.br.sgs.exception.CompanyNotFound;
import com.br.sgs.exception.DescriptionAlredyInUse;
import com.br.sgs.exception.OperationNotAllowed;
import com.br.sgs.models.RoleModel;
import com.br.sgs.services.CompanyService;
import com.br.sgs.services.RoleService;
import com.br.sgs.specifications.SpecificationTemplate;
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
	
	@PostMapping("/{companyId}")
	private ResponseEntity<Object> createRole(@RequestBody @Validated (RoleDto.RoleView.RegistrationPost.class)
														@JsonView(RoleDto.RoleView.RegistrationPost.class) RoleDto roleDto,
														@PathVariable UUID companyId){
		
		log.debug("POST createRole roleDto received {} ", roleDto.toString());
        if(roleService.existsByDescription(roleDto.getDescription())){
            log.warn("Description {} is Already Taken ", roleDto.getDescription());
            throw new DescriptionAlredyInUse();
        }
        if (!companyService.existsById(companyId)) {
			log.warn("CompanyId {} not found ", companyId);
			throw new CompanyNotFound();
		}
        
        RoleModel roleModel = roleService.save(roleDto, companyId);
        
        log.debug("POST createRole roleId saved {} ", roleModel.getRoleId());
        log.info("Role saved successfully roleId {} ", roleModel.getRoleId());
        
		return ResponseEntity.status(HttpStatus.CREATED).body(roleModel);
	}
	
	
	@PutMapping("/status/{companyId}/{roleId}")
	private ResponseEntity<Object> alternateStatus(@PathVariable UUID companyId,
												   @PathVariable UUID roleId){
		
		log.debug("PUT alternateStatus roleId received {} ", roleId);
		Optional<RoleModel> roleModel = roleService.findById(roleId);
		
        if(!roleModel.isPresent()){
            log.warn("roleId {} not found ", roleId);
			throw new NoSuchElementException(); 
        }
		if (!companyService.existsById(companyId)) {
			log.warn("CompanyId {} not found ", companyId);
			throw new CompanyNotFound();
		}
		
		if (!companyId.equals(roleModel.get().getCompany().getCompanyId())) {
			log.warn("CompanyId is diferent");
			throw new OperationNotAllowed();
		}
		
        var roleModelUpdated = roleService.update(roleModel.get(), companyId);
        
        log.debug("PUT alternateStatus roleId saved {} ", roleModelUpdated.getRoleId());
        log.info("Role saved successfully roleId {} ", roleModelUpdated.getRoleId());
        
		return ResponseEntity.status(HttpStatus.OK).body(roleModelUpdated);
	}
	
	@PutMapping("/{companyId}/{roleId}")
	private ResponseEntity<Object> update(@RequestBody @Validated (RoleDto.RoleView.RolePut.class)
														@JsonView(RoleDto.RoleView.RolePut.class) RoleDto roleDto,
														@PathVariable UUID companyId,
														@PathVariable UUID roleId){
		
		log.debug("PUT alternateStatus roleDto received {} ", roleDto.toString());
		Optional<RoleModel> roleModel = roleService.findById(roleId);
		
        if(!roleModel.isPresent()){
            log.warn("roleId {} not found ", roleDto.getDescription());
			throw new NoSuchElementException(); 
        }
		if (!companyService.existsById(companyId)) {
			log.warn("CompanyId {} not found ", companyId);
			throw new CompanyNotFound();
		}
		
		if (!companyId.equals(roleModel.get().getCompany().getCompanyId())) {
			log.warn("CompanyId is diferent");
			throw new OperationNotAllowed();
		}
		
        var roleModelUpdated = roleService.update(roleId, companyId, roleDto);
        
        log.debug("PUT alternateStatus roleId saved {} ", roleModelUpdated.getRoleId());
        log.info("Role saved successfully roleId {} ", roleModelUpdated.getRoleId());
        
		return ResponseEntity.status(HttpStatus.OK).body(roleModelUpdated);
	}
	
	@GetMapping("/{companyId}/{roleId}")
	private ResponseEntity<RoleModel> getRoleByRoleIdCompanyId(@PathVariable(value="roleId") UUID roleId, 
																		  @PathVariable(value="companyId") UUID companyId){
		
		Optional<RoleModel> role = roleService.findByIdAndCompanyId(roleId, companyId);
		return role.isPresent() ? ResponseEntity.ok(role.get()) : ResponseEntity.notFound().build();
	}
	
	@GetMapping("/{companyId}")
    public ResponseEntity<Page<RoleModel>> getAllRole(@PathVariable(value="companyId") UUID companyId, SpecificationTemplate.RoleSpec spec,
    												  @PageableDefault(page = 0, size = 10, sort = "roleId", direction = Sort.Direction.ASC) Pageable pageable){
		
		return ResponseEntity.status(HttpStatus.OK).body(roleService.findAllByCompany(SpecificationTemplate.roleCompanyId(companyId).and(spec), pageable));
    }

}
