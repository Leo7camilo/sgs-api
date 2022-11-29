package com.br.sgs.services.impl;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.br.sgs.dtos.RoleDto;
import com.br.sgs.enums.RoleState;
import com.br.sgs.models.CompanyModel;
import com.br.sgs.models.RoleModel;
import com.br.sgs.repository.CompanyRepository;
import com.br.sgs.repository.RoleRepository;
import com.br.sgs.services.RoleService;
import com.br.sgs.specifications.SpecificationTemplate.CompanySpec;

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
	public RoleModel save(RoleDto roleDto, UUID idCompany) {
		
		Optional<CompanyModel> companyModel = companyRepository.findById(idCompany);
		
		var roleModel = new RoleModel();
		BeanUtils.copyProperties(roleDto, roleModel);
		roleModel.setStatus(RoleState.ACTIVE);
		roleModel.setCompany(companyModel.get());
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


	
	

}
