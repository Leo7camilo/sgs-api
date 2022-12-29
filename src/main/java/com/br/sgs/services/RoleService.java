package com.br.sgs.services;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.br.sgs.dtos.RoleDto;
import com.br.sgs.models.RoleModel;

public interface RoleService {

	boolean existsByDescription(String descriptions);

	RoleModel save(RoleDto roleDto, UUID idCompany);

	boolean existsById(UUID idRole);

	Optional<RoleModel> findById(UUID idRole);

	RoleModel update(RoleModel RoleModel, UUID idCompany);

	Optional<RoleModel> findByIdAndCompanyId(UUID roleId, UUID companyId);

	Page<RoleModel> findAllByCompany(Specification<RoleModel> spec, Pageable pageable);

	RoleModel update(UUID idRole, UUID companyId, RoleDto roleDto);

}
