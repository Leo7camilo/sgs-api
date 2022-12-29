package com.br.sgs.services.impl;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.br.sgs.dtos.RoleDto;
import com.br.sgs.enums.RoleState;
import com.br.sgs.models.CompanyModel;
import com.br.sgs.models.RoleModel;
import com.br.sgs.repository.CompanyRepository;
import com.br.sgs.repository.RoleRepository;
import com.br.sgs.services.RoleService;

@Service
public class RoleServiceImpl implements RoleService {

	@Autowired
	RoleRepository roleRepository;
	
	@Autowired
	CompanyRepository companyRepository;

	@Override
	public boolean existsByDescription(String descriptions) {
		return roleRepository.existsByDescription(descriptions);
	}

	@Override
	public RoleModel save(RoleDto roleDto, UUID companyId) {
		
		Optional<CompanyModel> companyModel = companyRepository.findById(companyId);
		
		var roleModel = new RoleModel();
		BeanUtils.copyProperties(roleDto, roleModel);
		roleModel.setStatus(RoleState.ACTIVE);
		roleModel.setCompany(companyModel.get());
		roleModel.setDtUpdate(LocalDateTime.now(ZoneId.of("UTC")));
		
		return roleRepository.save(roleModel);
	}

	@Override
	public boolean existsById(UUID idRole) {
		return roleRepository.existsById(idRole);
	}

	@Override
	public Optional<RoleModel> findById(UUID idRole) {
		return roleRepository.findById(idRole);
	}

	@Override
	public RoleModel update(RoleModel roleModel, UUID idCompany) {
		
		if(roleModel.getStatus() == RoleState.ACTIVE)
			roleModel.setStatus(RoleState.DISABLE);
		else
			roleModel.setStatus(RoleState.ACTIVE);
		
		roleModel.setRoleId(roleModel.getRoleId());
		return roleRepository.save(roleModel);
	}

	@Override
	public Optional<RoleModel> findByIdAndCompanyId(UUID roleId, UUID companyId) {
		return roleRepository.findByRoleIdAndCompanyCompanyId(roleId, companyId);
	}

	@Override
	public Page<RoleModel> findAllByCompany(Specification<RoleModel> spec, Pageable pageable) {
		return roleRepository.findAll(spec, pageable);
	}

	@Override
	public RoleModel update(UUID idRole, UUID companyId, RoleDto roleDto) {
		
		Optional<CompanyModel> companyModel = companyRepository.findById(companyId);
		
		var roleModel = new RoleModel();
		BeanUtils.copyProperties(roleDto, roleModel);
		roleModel.setCompany(companyModel.get());
		roleModel.setStatus(RoleState.ACTIVE);
		roleModel.setRoleId(idRole);
		roleModel.setDtUpdate(LocalDateTime.now(ZoneId.of("UTC")));
		
		return roleRepository.save(roleModel);
	}
	
	

}
